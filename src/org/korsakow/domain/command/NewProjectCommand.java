package org.korsakow.domain.command;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.ProjectFactory;
import org.korsakow.domain.SettingsFactory;
import org.korsakow.domain.interchange.mapper.InterchangeInputMapperFactory;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.ide.util.UIUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class NewProjectCommand extends AbstractCommand{


	public NewProjectCommand(Helper request, Helper response) {
		super(request, response);
		
	}
	public void execute()
			throws CommandException {
		try {
			IProject p = newProject();
			response.set("project", p);
			UoW.getCurrent().commit();
			UoW.newCurrent();
			
		} catch (Exception e) {
			throw new CommandException(e);
		}
	}
	public static IProject newProject() throws ParserConfigurationException, SAXException, IOException, SQLException, XPathException, MapperException, KeyNotFoundException, CreationException
	{
		return newProject(null);
	}
	public static IProject newProject(File parentDir) throws ParserConfigurationException, SAXException, IOException, SQLException, XPathException, MapperException, KeyNotFoundException, CreationException
	{
		File file = File.createTempFile("korsakow", ".xml", parentDir);
		
	    DataRegistry.initialize(DataRegistry.createDefaultDocument(), file);
	    Document document = DataRegistry.getDocument(); // initialize modifies the doc

	    final int movieWidth = 1024;
	    final int movieHeight = 768;
	    
//		System.out.println(DataRegistry.debugDump());
		ISettings settings = SettingsFactory.createNew();
		
		IInterface defaultInterface = null;
		List<IInterface> interfaces = new ArrayList<IInterface>();
		interfaces.addAll(loadDefaultInterfaces(document));
		defaultInterface = interfaces.get(0);
		for (IInterface interf : interfaces) {
			UoW.getCurrent().registerNew(interf);
		}

		IProject project = ProjectFactory.createNew(
				"Untitled Project",
				new ArrayList<IKeyword>(),
				movieWidth, movieHeight,
				null, 1.0F, true,
				null, 1.0F,
				null, Color.black,
				null,
				false, false,
				null,
				defaultInterface,
				new ArrayList<IRule>(),
				new ArrayList<ISnu>(),
				interfaces,
				new ArrayList<IMedia>(),
				settings,
				UUID.randomUUID().toString()
				);
		
		// flush any changes we've done
		DataRegistry.setDocument(document);
		return project;
	}
	private static void centerToStage(IInterface interf, int stageWidth, int stageHeight)
	{
		// center the interface on the stage
		Rectangle bounds = interf.getBounds();
		int dx = (stageWidth - bounds.width)/2 - bounds.x;
		int dy = (stageHeight - bounds.height)/2 - bounds.y;
		for (IWidget widget : interf.getWidgets()) {
			widget.setX(widget.getX() + dx);
			widget.setY(widget.getY() + dy);
			UoW.getCurrent().registerDirty(widget);
		}
	}
	private static Collection<IInterface> loadDefaultInterfaces(Document document) throws SAXException, ParserConfigurationException, IOException, XPathExpressionException
	{
		org.korsakow.domain.interchange.mapper.input.InterchangeInterfaceInputMapper interfaceInputMapper = InterchangeInputMapperFactory.createInterfaceInputMapper();

		List<IInterface> interfs = new ArrayList<IInterface>(); // note, see coment below, this must currently be an ordered collection
		
		// currently this ordering makes 4x3_3Bottom the default interface. we hope to eventaully
		// decouple the concept of default interface from internal (and arbitrary) ordering
		IInterface _4x3_3Bottom = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/4x3_3Bottom.kif")), "//Interface"));
		interfs.add(_4x3_3Bottom);
		IInterface _4x3_3Top = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/4x3_3Top.kif")), "//Interface"));
		interfs.add(_4x3_3Top);
		IInterface _4x3_3Left = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/4x3_3Left.kif")), "//Interface"));
		interfs.add(_4x3_3Left);
		IInterface _4x3_3Right = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/4x3_3Right.kif")), "//Interface"));
		interfs.add(_4x3_3Right);
		
		IInterface _16x9_3Bottom = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/16x9_3Bottom.kif")), "//Interface"));
		interfs.add(_16x9_3Bottom);
		IInterface _16x9_3Top = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/16x9_3Top.kif")), "//Interface"));
		interfs.add(_16x9_3Top);
		IInterface _16x9_3Left = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/16x9_3Left.kif")), "//Interface"));
		interfs.add(_16x9_3Left);
		IInterface _16x9_3Right = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/16x9_3Right.kif")), "//Interface"));
		interfs.add(_16x9_3Right);
		
		IInterface galata_4_left = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/galata_4_LEFT.kif")), "//Interface"));
		interfs.add(galata_4_left);
		IInterface galata_4_middle = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/galata_4_MIDDLE.kif")), "//Interface"));
		interfs.add(galata_4_middle);
		IInterface galata_4_right = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/galata_4_RIGHT.kif")), "//Interface"));
		interfs.add(galata_4_right);
		IInterface galata_4_mixed = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/galata_8_MIXED.kif")), "//Interface"));
		interfs.add(galata_4_mixed);
		IInterface galata_two_big = interfaceInputMapper.input(XPathHelper.xpathAsElement(DomUtil.parseXML(ResourceManager.getResourceStream("interfaces/galata_TWO-BIG.kif")), "//Interface"));
		interfs.add(galata_two_big);
		
		FolderNode rootFolder = new FolderNode("/");
		FolderNode interfacesFolder = new FolderNode("Interfaces");
		rootFolder.add(interfacesFolder);
		
		FolderNode _16x9Folder = new FolderNode("16x9");
		interfacesFolder.add(_16x9Folder);
		
		FolderNode _4x3Folder = new FolderNode("4x3");
		interfacesFolder.add(_4x3Folder);
		
		FolderNode galataFolder = new FolderNode("Planet Galata");
		interfacesFolder.add(galataFolder);

		_16x9Folder.add(ResourceNode.create(_16x9_3Bottom));
		_16x9Folder.add(ResourceNode.create(_16x9_3Top));
		_16x9Folder.add(ResourceNode.create(_16x9_3Left));
		_16x9Folder.add(ResourceNode.create(_16x9_3Right));
		
		_4x3Folder.add(ResourceNode.create(_4x3_3Top));
		_4x3Folder.add(ResourceNode.create(_4x3_3Left));
		_4x3Folder.add(ResourceNode.create(_4x3_3Right));
		rootFolder.add(ResourceNode.create(_4x3_3Bottom));
		
		galataFolder.add(ResourceNode.create(galata_4_left));
		galataFolder.add(ResourceNode.create(galata_4_middle));
		galataFolder.add(ResourceNode.create(galata_4_right));
		galataFolder.add(ResourceNode.create(galata_4_mixed));
		galataFolder.add(ResourceNode.create(galata_two_big));
		
		document.getDocumentElement().appendChild(UIUtil.resourceTreeToDom(document, rootFolder));
		
		return interfs;
	}
}
