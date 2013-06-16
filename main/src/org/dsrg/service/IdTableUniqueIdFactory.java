package org.dsrg.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.service.UniqueIdFactory;
import org.korsakow.ide.DataRegistry;
import org.korsakow.services.exception.BetterSQLException;
import org.w3c.dom.NodeList;
/**
 * This is a factory for generating unique Id's. It works on a dedicated ID table.
 * 
 * Note, this sucker doesn't lock tables at the moment! Beware! (sqlite doesn't seem to support it)
 * 
 * @author dave
 */
public class IdTableUniqueIdFactory extends UniqueIdFactory {

	private static int BUFFER_SIZE = 50;
	private final List<Long> availableIds = new ArrayList<Long>();
	
	private long max_id;
	public IdTableUniqueIdFactory(String idTable, String idField)
	{
	}
	/**
	 * Advances the max id by the specified amount, effectively reserving that many ids in the range [return-value, return-value+delta)
	 * By simply advancing and not setting, we avoid the possibility of race conditions.
	 * @return the old max id, the base of the reserved id range
	 */
	public synchronized long advanceMaxId(long delta)
	{
		long oldmax = max_id;
		availableIds.clear();
		max_id += delta;
		return oldmax;
	}
	@Override
	public synchronized long getId(String table, String field) throws SQLException
	{
		try {
			return getId();
		} catch (XPathExpressionException e) {
			throw new BetterSQLException(e);
		}
	}
	public synchronized long getId() throws XPathExpressionException
	{
		// notice how we keep the max_id between calls. its important that we dont clear it
		// since the setMaxId functionality relies on it
		if(availableIds.isEmpty()) {
//			String result = DataRegistry.getHelper().xpathAsString("/korsakow/descendant::*/id[not(text() <= /korsakow/descendant::*/id) and not(text() <= /korsakow/descendant::*/id)]");
			NodeList list = DataRegistry.getHelper().xpathAsNodeList("/korsakow/descendant::*/id");
			int length = list.getLength();
			for (int i = 0; i < length; ++i) {
				try {
					long id = Long.parseLong(list.item(i).getTextContent());
					if (id > max_id)
						max_id = id;
				} catch (NumberFormatException e) {
					continue;
				}
			}

			List<Long> ids = new ArrayList<Long>();
			for (int i = 0; i < BUFFER_SIZE; ++i) {
				++max_id;
				ids.add(max_id);
			}
			availableIds.addAll(ids);
		}
//		Logger.getLogger(IdTableUniqueIdFactory.class).
		return availableIds.remove(0);
	}

}
