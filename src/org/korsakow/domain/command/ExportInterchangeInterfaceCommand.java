package org.korsakow.domain.command;


import java.io.File;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.interchange.mapper.OutputMapperFactory;
import org.korsakow.domain.interchange.mapper.output.InterchangeInterfaceOutputMapper;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.mapper.input.InterfaceInputMapper;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class ExportInterchangeInterfaceCommand extends AbstractCommand{


	public ExportInterchangeInterfaceCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
		throws CommandException {
		
		try {
			
			long interfaceId = request.getLong("interface_id");
			String filename = request.getString("filename");
			
			IInterface interf = InterfaceInputMapper.map(interfaceId);
			
			Document document = DataRegistry.createDefaultEmptyDocument();
			
			InterchangeInterfaceOutputMapper interfaceOutputMapper = OutputMapperFactory.createInterfaceOutputMapper(document);
			Element interfaceElement = interfaceOutputMapper.output(interf);
			document.getDocumentElement().appendChild(interfaceElement);
			
			DomUtil.writeDomXML(document, new File(filename));
		} catch (Exception e) {
			throw new CommandException(e);
		}
	}
}
