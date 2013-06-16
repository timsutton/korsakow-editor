package org.korsakow.ide;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.service.IdTableUniqueIdFactory;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.service.ConnectionFactory;
import org.dsrg.soenea.service.UniqueIdFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.dsrg.soenea.uow.MapperFactory;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Event;
import org.korsakow.domain.Image;
import org.korsakow.domain.Interface;
import org.korsakow.domain.Pattern;
import org.korsakow.domain.Predicate;
import org.korsakow.domain.Project;
import org.korsakow.domain.Rule;
import org.korsakow.domain.RuntimeDomainException;
import org.korsakow.domain.Settings;
import org.korsakow.domain.Snu;
import org.korsakow.domain.Sound;
import org.korsakow.domain.Text;
import org.korsakow.domain.Trigger;
import org.korsakow.domain.Video;
import org.korsakow.domain.Widget;
import org.korsakow.domain.mapper.output.EventOutputMapper;
import org.korsakow.domain.mapper.output.ImageOutputMapper;
import org.korsakow.domain.mapper.output.InterfaceOutputMapper;
import org.korsakow.domain.mapper.output.PatternOutputMapper;
import org.korsakow.domain.mapper.output.PredicateOutputMapper;
import org.korsakow.domain.mapper.output.ProjectOutputMapper;
import org.korsakow.domain.mapper.output.RuleOutputMapper;
import org.korsakow.domain.mapper.output.SettingsOutputMapper;
import org.korsakow.domain.mapper.output.SnuOutputMapper;
import org.korsakow.domain.mapper.output.SoundOutputMapper;
import org.korsakow.domain.mapper.output.TextOutputMapper;
import org.korsakow.domain.mapper.output.TriggerOutputMapper;
import org.korsakow.domain.mapper.output.VideoOutputMapper;
import org.korsakow.domain.mapper.output.WidgetOutputMapper;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.ide.util.Util;
import org.korsakow.services.tdg.ImageTDG;
import org.korsakow.services.tdg.InterfaceTDG;
import org.korsakow.services.tdg.PatternTDG;
import org.korsakow.services.tdg.ProjectTDG;
import org.korsakow.services.tdg.SettingsTDG;
import org.korsakow.services.tdg.SnuTDG;
import org.korsakow.services.tdg.SoundTDG;
import org.korsakow.services.tdg.TextTDG;
import org.korsakow.services.tdg.VideoTDG;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DataRegistry
{
	public static class Factory extends ConnectionFactory
	{
		@Override
		public void defaultInitialization() throws SQLException {
		}
		@Override
		public Connection getConnection() throws SQLException {
			return DataRegistry.getConnection();
		}
	}
	
	private static DomSessionConnection conn;
	private static DomSession dom;
	private static File file;
	private static IdTableUniqueIdFactory idFactory;
	private static ThreadLocal<DomHelper> localDomHelper = new ThreadLocal<DomHelper>();
	private DataRegistry()
	{
	}
	/**
	 * Advances the max id by the specified amount, effectively reserving that many ids in the range [return-value, return-value+delta)
	 * By simply advancing and not setting, we avoid the possibility of race conditions.
	 * @return the old max id, the base of the reserved id range
	 */
	public static synchronized long advanceMaxId(long delta)
	{
		return idFactory.advanceMaxId(delta);
	}
	public static synchronized Long getMaxId()
	{
		try {
			return UniqueIdFactory.getMaxId("", "");
		} catch (SQLException e) {
			throw new RuntimeDomainException(e);
		}
	}
	public static synchronized void initialize(Document document, File file) throws SQLException, XPathExpressionException, KeyNotFoundException, CreationException, MapperException
	{
		try {
			DbRegistry.closeDbConnection();
		} catch (NullPointerException e) {
			// implementation detail
		}
		
		UoW.newCurrent();
		
		localDomHelper = new ThreadLocal<DomHelper>();
		dom = new DomSession(document);
		conn = new DomSessionConnection();
		DataRegistry.file = file;
		
		initTDG();
		initUoW();
		
		UniqueIdFactory.setFactory(idFactory = new IdTableUniqueIdFactory("id", "id"));
		
		UoW.getCurrent().commit();
	}
	public static synchronized File getFile()
	{
		return file;
	}
	public static synchronized void setFile(File file)
	{
		DataRegistry.file = file;
	}
	public static synchronized void flush() throws IOException, TransformerException
	{
		DomUtil.writeDomXML(getDocument(), getFile());
	}
//	public static synchronized DomSession getDomSession()
//	{
//		return dom;
//	}
	public static synchronized Document getHeadDocument()
	{
		return dom.getHeadDocument();
	}
	public static synchronized Document getDocument()
	{
		return dom.getDocument();
	}
	public static synchronized void setDocument(Document document)
	{
		dom.setDocument(document);
	}
	public static synchronized DomSession getDomSession()
	{
		return dom;
	}
	public static synchronized long getHeadVersion()
	{
		return dom.getHeadVersion();
	}
	public static synchronized void commit()
	{
		dom.commit();
	}
	public static synchronized void rollback()
	{
		dom.rollbackToHead();
	}
	public static synchronized void safeRollback()
	{
		if (!dom.tryRollbackToHead())
			throw new IllegalArgumentException();
	}
	public static synchronized DomHelper getHelper()
	{
		Document document = dom.getDocument();
		DomHelper helper = localDomHelper.get();
		// helper's document is out of date if it is not the same one we get from DomSession
		if (helper == null || helper.getDocument() != document) {
			helper = new DomHelper(document);
			localDomHelper.set(helper);
		}
		return helper;
	}
	public static synchronized Connection getConnection()
	{
		return conn;
	}
	/**
	 * This is out of place in this class.?
	 * @return
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public static synchronized Document createDefaultDocument() throws ParserConfigurationException, SAXException, IOException
	{
	    Document document = DomUtil.parseXML(ResourceManager.getResourceStream("defaultdocument.xml"));
	    document.getDocumentElement().setAttribute("versionMajor", Build.getVersion());
	    document.getDocumentElement().setAttribute("versionMinor", ""+Build.getRelease());
	    return document;
	}
	public static synchronized Document createDefaultEmptyDocument() throws ParserConfigurationException, SAXException, IOException
	{
	    Document document = DomUtil.createDocument();
	    document.appendChild(document.createElement("korsakow"));
	    document.getDocumentElement().setAttribute("versionMajor", Build.getVersion());
	    document.getDocumentElement().setAttribute("versionMinor", ""+Build.getRelease());
	    return document;
	}
	public static synchronized String debugDump()
	{
		try {
			return DomUtil.toXMLString(getDocument());
		} catch (Exception e) {
			return Util.getStackTraceString(e);
		}
	}
	public static synchronized String debugDumpHead()
	{
		try {
			return DomUtil.toXMLString(dom.getHeadDocument());
		} catch (Exception e) {
			return Util.getStackTraceString(e);
		}
	}
	public static synchronized void initUoW()
	{
		MapperFactory myDomain2MapperMapper = new MapperFactory();
		myDomain2MapperMapper.addMapping(Widget.class, WidgetOutputMapper.class);
		myDomain2MapperMapper.addMapping(Interface.class, InterfaceOutputMapper.class);
		myDomain2MapperMapper.addMapping(Video.class, VideoOutputMapper.class);
		myDomain2MapperMapper.addMapping(Sound.class, SoundOutputMapper.class);
		myDomain2MapperMapper.addMapping(Image.class, ImageOutputMapper.class);
		myDomain2MapperMapper.addMapping(Snu.class, SnuOutputMapper.class);
		myDomain2MapperMapper.addMapping(Project.class, ProjectOutputMapper.class);
		myDomain2MapperMapper.addMapping(Rule.class, RuleOutputMapper.class);
		myDomain2MapperMapper.addMapping(Predicate.class, PredicateOutputMapper.class);
		myDomain2MapperMapper.addMapping(Trigger.class, TriggerOutputMapper.class);
		myDomain2MapperMapper.addMapping(Event.class, EventOutputMapper.class);
		myDomain2MapperMapper.addMapping(Pattern.class, PatternOutputMapper.class);
		myDomain2MapperMapper.addMapping(Text.class, TextOutputMapper.class);
		myDomain2MapperMapper.addMapping(Settings.class, SettingsOutputMapper.class);

		UoW.initMapperFactory(myDomain2MapperMapper);
		UoW.newCurrent();
	}
	public static synchronized void initTDG() throws XPathExpressionException
	{
	    DbRegistry.setConFactory(new DataRegistry.Factory());
		
	    InterfaceTDG.createInterfaceTable();
		VideoTDG.createVideoTable();
		SoundTDG.createSoundTable();
		ImageTDG.createImageTable();
		SnuTDG.createSnuTable();
		ProjectTDG.createProjectTable();
		PatternTDG.createPatternTable();
		TextTDG.createTextTable();
		SettingsTDG.createSettingsTable();
	}
}
