package org.korsakow.domain.command;


import java.io.File;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interchange.mapper.InterchangeInputMapperFactory;
import org.korsakow.domain.interchange.mapper.input.InterchangeInterfaceInputMapper;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.ide.DomHelper;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.conversion.ConversionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ImportInterchangeInterfaceCommand extends AbstractCommand{


	public ImportInterchangeInterfaceCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
		throws CommandException {
		
		try {
			
			String filename = request.getString("filename");
			
			File file = new File(filename);
			Document document = DomUtil.parseXML(file);
			
			// versions pre 22 don't include version info (sorry!)
			if (!document.getDocumentElement().getTagName().equals("korsakow")) {
				Element root = document.createElement("korsakow");
				root.setAttribute("versionMajor", "0.0");
				root.setAttribute("versionMinor", "0.0");
				NodeList children = document.getChildNodes();
				int length = children.getLength();
				for (int i = 0; i < length; ++i) {
					if (children.item(i) instanceof Element) // has happened...
						root.appendChild(children.item(i));
				}
				document.appendChild(root);
			}
			ConversionFactory cf = new ConversionFactory(document);
			cf.convert();
			
			InterchangeInterfaceInputMapper interfaceInputMapper = InterchangeInputMapperFactory.createInterfaceInputMapper();
			
			UoW.newCurrent();
			
			IInterface interf = interfaceInputMapper.input(DomHelper.xpathAsElement(document, "//Interface"));
			UoW.getCurrent().registerNew(interf);
			
			UoW.getCurrent().commit();
			UoW.newCurrent();
			
			response.set("interface", interf);
			
		} catch (Exception e) {
			throw new CommandException(e);
		}
	}
}
