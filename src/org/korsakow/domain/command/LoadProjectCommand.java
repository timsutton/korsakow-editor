package org.korsakow.domain.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.conversion.ConversionException;
import org.korsakow.services.conversion.ConversionFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class LoadProjectCommand extends AbstractCommand{


	public LoadProjectCommand(Helper request, Helper response) {
		super(request, response);
		
	}
	public void execute()
			throws CommandException {
		try {
			String filename = request.getString("filename");
			IProject p = loadProject(new File(filename));
			response.set("project", p);
			
			Collection<IMedia> media = p.getMedia();
			Collection<IMedia> missing = new HashSet<IMedia>();
			for (IMedia medium : media) {
				try {
					medium.getAbsoluteFilename();
				} catch (FileNotFoundException e) {
					missing.add(medium);
				}
			}
			
			if (!missing.isEmpty())
				response.set("missingMedia", missing);
			
			UoW.getCurrent().commit();
			UoW.newCurrent();
			
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (XPathExpressionException e) {
			throw new CommandException(e);
		} catch (SQLException e) {
			throw new CommandException(e);
		} catch (SAXException e) {
			throw new CommandException(e);
		} catch (ParserConfigurationException e) {
			throw new CommandException(e);
		} catch (IOException e) {
			throw new CommandException(e);
		} catch (KeyNotFoundException e) {
			throw new CommandException(e);
		} catch (CreationException e) {
			throw new CommandException(e);
		} catch (ConversionException e) {
			throw new CommandException(e);
		}
	}
	private IProject loadProject(File file) throws XPathExpressionException, SQLException, SAXException, ParserConfigurationException, IOException, MapperException, KeyNotFoundException, CreationException, ConversionException
	{
		Document document = DomUtil.parseXML(file);
		ConversionFactory cf = new ConversionFactory(document);
		cf.convert();
		if (!cf.getWarnings().isEmpty())
			response.set("warnings", cf.getWarnings());
		
		DataRegistry.initialize(document, file);
		IProject project = ProjectInputMapper.find();
		// this is for pre milestone 20, which don't have UUID
		if (project.getUUID() == null) {
			project.setUUID(UUID.randomUUID().toString());
			UoW.getCurrent().registerDirty(project);
		}
		return project;
	}
}
