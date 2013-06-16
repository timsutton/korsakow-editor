package org.korsakow.domain.command;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Interface;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.InterfaceInputMapper;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.DomHelper;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.ide.util.Template;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class LoadInterfaceCommand extends AbstractCommand
{
	public LoadInterfaceCommand(Helper request, Helper response) {
		super(request, response);
		
	}
	public void execute() throws CommandException
	{
		String file = request.getString("filename");
		try {
			Document doc = DomUtil.parseXML(new File(file));
			Collection<IInterface> interfaces = getInterfaces(doc);
			UoW.getCurrent().commit();
			response.set("interfaces", interfaces);
		} catch (SAXException e) {
			throw new CommandException(e);
		} catch (ParserConfigurationException e) {
			throw new CommandException(e);
		} catch (IOException e) {
			throw new CommandException(e);
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (SQLException e) {
			throw new CommandException(e);
		} catch (KeyNotFoundException e) {
			throw new CommandException(e);
		} catch (CreationException e) {
			throw new CommandException(e);
		} catch (XPathExpressionException e) {
			throw new CommandException(e);
		} catch (TransformerException e) {
			throw new CommandException(e);
		}
	}
	public static Collection<IInterface> getInterfaces(Document doc) throws MapperException, XPathExpressionException, CommandException, TransformerException, IOException, SAXException, ParserConfigurationException
	{
		// ====
		// we swap documents so that we can load the defaults
		// and then restore the document
		// its all thread-local so it should be safe
		// ====
		
		// find the templated ids
		List<Node> idElements = DomHelper.xpathAsList(doc, "/korsakow/descendant::*/id");
		// reserve as many ids as we need
		long idcounter = DataRegistry.advanceMaxId(idElements.size());
		long idcountermax = idcounter + idElements.size();
		Map<String, String> idMap = new HashMap<String, String>();
		// map out template-id to concrete ids
		for (Node node : idElements)
		{
			Element element = (Element)node;
			
			String text = element.getTextContent();
			if (text.startsWith("${") && text.endsWith("}"))
				text = text.substring(2, text.length()-1);
			if (!idMap.containsKey(text)) {
				if (idcounter > idcountermax)
					throw new CommandException("out of ids: " + idcounter);
				long id = ++idcounter;
				idMap.put(text, ""+id);
			}
		}
		// well its kind of shoddy, but we convert to text, replace, and back to dom
		String docStr = DomUtil.toXMLString(doc);
		Template template = new Template(docStr);
		template.setValues(idMap);
		docStr = template.format();
		doc = DomUtil.parseXMLString(docStr);
		
		Document oldDoc = DataRegistry.getDocument();
	    Collection<IInterface> interfaces = null;
		try {
			Document tempDoc = doc;
		    DataRegistry.setDocument(tempDoc);
		    interfaces = InterfaceInputMapper.findAll();
		    // we need concrete instances since we put back the doc
		    for (IInterface interf : interfaces) {
		    	interf.getVersion();
		    	for (IWidget widget : interf.getWidgets()) {
		    		widget.getVersion();
		    	}
		    }
		} finally {
			// otherwise an exception would corrupt things
		    DataRegistry.setDocument(oldDoc);
		}
	    
		
		// ====
	    // this is necessary to do since we swaped back the document
		// in short, we re-register the DO's in the current UoW.
		// ====
	    for (IInterface iinterf : interfaces) {
	    	// this is the only way we can register new
	    	Interface interf = InterfaceInputMapper.map(iinterf.getId());
	    	UoW.getCurrent().registerNew(interf);
	    }
	    
	    return interfaces;
	}

}
