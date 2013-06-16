package test.org.korsakow.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dsrg.soenea.uow.UoW;
import org.junit.Test;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.ProjectFactory;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.TextFactory;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.command.InsertImageCommand;
import org.korsakow.domain.command.InsertInterfaceCommand;
import org.korsakow.domain.command.InsertProjectCommand;
import org.korsakow.domain.command.InsertSnuCommand;
import org.korsakow.domain.command.InsertSoundCommand;
import org.korsakow.domain.command.InsertTextCommand;
import org.korsakow.domain.command.InsertVideoCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.UpdateImageCommand;
import org.korsakow.domain.command.UpdateInterfaceCommand;
import org.korsakow.domain.command.UpdateProjectCommand;
import org.korsakow.domain.command.UpdateSnuCommand;
import org.korsakow.domain.command.UpdateSoundCommand;
import org.korsakow.domain.command.UpdateTextCommand;
import org.korsakow.domain.command.UpdateVideoCommand;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.ui.controller.helper.ViewHelper;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;
import test.util.DomainTestUtil;

public class TestInsertCommand extends AbstractDomainObjectTestCase
{
	@Test public void testInsertText() throws Exception
	{
		IText obj = TextFactory.createNew();
		UoW.getCurrent().registerClean(obj);
		DomainTestUtil.initializeRandom(obj);
		
		Response response = new Response();
		Request request = new Request();
		request.set(UpdateTextCommand.KEYWORDS, obj.getKeywords());
		request.set(UpdateTextCommand.NAME, obj.getName());
		request.set(UpdateTextCommand.SOURCE, obj.getSource().getId());
		request.set(UpdateTextCommand.FILENAME, obj.getFilename());
		request.set(UpdateTextCommand.TEXTCONTENT, obj.getText());
		InsertTextCommand command = new InsertTextCommand(request, response);
		command.execute();
		Long id = ((IText)response.get(UpdateTextCommand.TEXT)).getId();
		
		UoW.getCurrent().commit();
		DataRegistry.flush();
		
		DomainTestUtil.assertEqual(dataFile, id, obj);
	}
	@Test public void testInsertSound() throws Exception
	{
		ISound obj = SoundFactory.createNew();
		UoW.getCurrent().registerClean(obj);
		DomainTestUtil.initializeRandom(obj);
		
		Response response = new Response();
		Request request = new Request();
		request.set(UpdateSoundCommand.KEYWORDS, obj.getKeywords());
		request.set(UpdateSoundCommand.NAME, obj.getName());
		request.set(UpdateSoundCommand.FILENAME, obj.getFilename());
		request.set(UpdateSoundCommand.SUBTITLES, obj.getSubtitles());
		InsertSoundCommand command = new InsertSoundCommand(request, response);
		command.execute();
		Long id = ((ISound)response.get(UpdateSoundCommand.SOUND)).getId();
		
		UoW.getCurrent().commit();
		DataRegistry.flush();
		
		DomainTestUtil.assertEqual(dataFile, id, obj);
	}
	@Test public void testInsertVideo() throws Exception
	{
		IVideo obj = VideoFactory.createNew();
		UoW.getCurrent().registerClean(obj);
		DomainTestUtil.initializeRandom(obj);
		
		Response response = new Response();
		Request request = new Request();
		request.set(UpdateVideoCommand.KEYWORDS, obj.getKeywords());
		request.set(UpdateVideoCommand.NAME, obj.getName());
		request.set(UpdateVideoCommand.FILENAME, obj.getFilename());
		InsertVideoCommand command = new InsertVideoCommand(request, response);
		command.execute();
		Long id = ((IVideo)response.get(UpdateVideoCommand.VIDEO)).getId();
		
		UoW.getCurrent().commit();
		DataRegistry.flush();
		
		DomainTestUtil.assertEqual(dataFile, id, obj);
	}
	@Test public void testInsertImage() throws Exception
	{
		IImage obj = ImageFactory.createNew();
		UoW.getCurrent().registerClean(obj);
		DomainTestUtil.initializeRandom(obj);
		
		Response response = new Response();
		Request request = new Request();
		request.set(UpdateImageCommand.KEYWORDS, obj.getKeywords());
		request.set(UpdateImageCommand.NAME, obj.getName());
		request.set(UpdateImageCommand.FILENAME, obj.getFilename());
		InsertImageCommand command = new InsertImageCommand(request, response);
		command.execute();
		Long id = ((IImage)response.get(UpdateImageCommand.IMAGE)).getId();
		
		UoW.getCurrent().commit();
		DataRegistry.flush();
		
		DomainTestUtil.assertEqual(dataFile, id, obj);
	}
	@Test public void testInsertSnu() throws Exception
	{
		ISnu obj = SnuFactory.createNew();
		UoW.getCurrent().registerClean(obj);
		DomainTestUtil.initializeRandom(obj);
		
		Response response = new Response();
		Request request = new Request();
		request.set(UpdateSnuCommand.KEYWORDS, obj.getKeywords());
		request.set(UpdateSnuCommand.NAME, obj.getName());
		request.set(UpdateSnuCommand.BACKGROUND_SOUND_ID, obj.getBackgroundSound()!=null?obj.getBackgroundSound().getId():null);
		request.set(UpdateSnuCommand.BACKGROUND_SOUND_MODE, obj.getBackgroundSoundMode());
		request.set(UpdateSnuCommand.BACKGROUND_SOUND_VOLUME, obj.getBackgroundSoundVolume());
		request.set(UpdateSnuCommand.BACKGROUND_SOUND_LOOPING, obj.getBackgroundSoundLooping());
		request.set(UpdateSnuCommand.ENDER, obj.getEnder());
		request.set(UpdateSnuCommand.INSERT_TEXT, obj.getInsertText());
		request.set(UpdateSnuCommand.INTERFACE_ID, obj.getInterface()!=null?obj.getInterface().getId():null);
		request.set(UpdateSnuCommand.LIVES, obj.getLives());
		request.set(UpdateSnuCommand.LOOPING, obj.getLooping());
		request.set(UpdateSnuCommand.MAIN_MEDIA_ID, obj.getMainMedia()!=null?obj.getMainMedia().getId():null);
		request.set(UpdateSnuCommand.MAX_LINKS, obj.getMaxLinks());
		request.set(UpdateSnuCommand.PREVIEW_MEDIA_ID, obj.getPreviewMedia()!=null?obj.getPreviewMedia().getId():null);
		request.set(UpdateSnuCommand.PREVIEW_TEXT, obj.getPreviewText());
		request.set(UpdateSnuCommand.RATING, obj.getRating());
		request.set(UpdateSnuCommand.STARTER, obj.getStarter());
		ViewHelper.addRulesToRequest(request, obj.getRules());
		InsertSnuCommand command = new InsertSnuCommand(request, response);
		command.execute();
		Long id = ((ISnu)response.get(UpdateSnuCommand.SNU)).getId();
		
		UoW.getCurrent().commit();
		DataRegistry.flush();
		
		DomainTestUtil.assertEqual(dataFile, id, obj);
	}
	@Test public void testInsertProject() throws Exception
	{
		IProject obj = ProjectFactory.createNew();
		UoW.getCurrent().registerClean(obj);
		DomainTestUtil.initializeRandom(obj);
		
		Response response = new Response();
		Request request = new Request();
		request.set(UpdateProjectCommand.KEYWORDS, obj.getKeywords());
		request.set(UpdateProjectCommand.NAME, obj.getName());
		request.set(UpdateProjectCommand.BACKGROUND_SOUND_ID, obj.getBackgroundSound()!=null?obj.getBackgroundSound().getId():null);
		request.set(UpdateProjectCommand.BACKGROUND_SOUND_VOLUME, obj.getBackgroundSoundVolume());
		request.set(UpdateProjectCommand.BACKGROUND_IMAGE_ID, obj.getBackgroundImage()!=null?obj.getBackgroundImage().getId():null);
		request.set(UpdateProjectCommand.CLICK_SOUND_ID, obj.getClickSound()!=null?obj.getClickSound().getId():null);
		request.set(UpdateProjectCommand.CLICK_SOUND_VOLUME, obj.getClickSoundVolume());
		request.set(UpdateProjectCommand.KEEP_LINKS, obj.getKeepLinksOnEmptySearch());
		request.set(UpdateProjectCommand.MAX_LINKS, obj.getMaxLinks());
		request.set(UpdateProjectCommand.MOVIE_HEIGHT, obj.getMovieHeight());
		request.set(UpdateProjectCommand.MOVIE_WIDTH, obj.getMovieWidth());
		request.set(UpdateProjectCommand.RANDOM_LINK_MODE, obj.getRandomLinkMode());
		request.set(UpdateProjectCommand.SPLASH_SCREEN_MEDIA_ID, obj.getSplashScreenMedia()!=null?obj.getSplashScreenMedia().getId():null);

		ViewHelper.addRulesToRequest(request, obj.getRules());
		InsertProjectCommand command = new InsertProjectCommand(request, response);
		command.execute();
		Long id = ((IProject)response.get(UpdateProjectCommand.PROJECT)).getId();
		
		UoW.getCurrent().commit();
		DataRegistry.flush();
		
		DomainTestUtil.assertEqual(dataFile, id, obj);
	}
//	@Test public void testInsertProjectRules() throws Exception
//	{
//		IProject project = ProjectFactory.createClean();
//		DomainTestUtil.initializeRandom(project);
//		
//		Response response = new Response();
//		Request request = new Request();
//		project.fillRequest(request);
//		ICommand command = new InsertProjectCommand(request, response);
//		command.execute();
//		Long id = request.getLong("id");
//		project.setId(id);
//		
//		project.setRules(DomainTestUtil.createRandomRules());
//		for (org.korsakow.ide.rules.Rule rule : project.getRules()) {
//			Request ruleRequest = new Request();
//			rule.fillRequest(ruleRequest);
//			Response ruleResponse = new Response();
//			InsertRuleCommand insertRuleCommand = new InsertRuleCommand(ruleRequest, ruleResponse);
//			insertRuleCommand.execute();
//			Long ruleId = ruleResponse.getLong("id");
//			rule.setId(ruleId);
//		}
//		
//		request = new Request();
//		project.fillRequest(request);
//		response = new Response();
//		command = new UpdateProjectCommand(request, response);
//		command.execute();
//				
//		UoW.getCurrent().commit();
//		DataRegistry.flush();
//
//		DomainTestUtil.assertEqual(dataFile, id, project);
//	}
//	@Test public void testInsertSnuRules() throws Exception
//	{
//		ISnu snu = SnuFactory.createClean();
//		DomainTestUtil.initializeRandom(snu);
//		
//		Response response = new Response();
//		Request request = new Request();
//		snu.fillRequest(request);
//		ICommand command = new InsertSnuCommand(request, response);
//		command.execute();
//		Long id = request.getLong("id");
//		snu.setId(id);
//		
//		snu.setRules(DomainTestUtil.createRandomRules());
//		for (org.korsakow.ide.rules.Rule rule : snu.getRules()) {
//			Request ruleRequest = new Request();
//			rule.fillRequest(ruleRequest);
//			Response ruleResponse = new Response();
//			InsertRuleCommand insertRuleCommand = new InsertRuleCommand(ruleRequest, ruleResponse);
//			insertRuleCommand.execute();
//			Long ruleId = ruleRequest.getLong("id");
//			rule.setId(ruleId);
//		}
//		
//		request = new Request();
//		snu.fillRequest(request);
//		response = new Response();
//		command = new UpdateSnuCommand(request, response);
//		command.execute();
//				
//		UoW.getCurrent().commit();
//		DataRegistry.flush();
//
//		DomainTestUtil.assertEqual(dataFile, id, snu);
//	}
	@Test public void testInsertInterface() throws Exception
	{
		IInterface obj = InterfaceFactory.createNew();
		UoW.getCurrent().registerClean(obj);
		DomainTestUtil.initializeRandom(obj, false);
		
		Response response = new Response();
		Request request = new Request();
		request.set(UpdateInterfaceCommand.KEYWORDS, obj.getKeywords());
		request.set(UpdateInterfaceCommand.NAME, obj.getName());
		request.set(UpdateInterfaceCommand.CLICK_SOUND_ID, obj.getClickSound()!=null?obj.getClickSound().getId():null);
		request.set(UpdateInterfaceCommand.CLICK_SOUND_VOLUME, obj.getClickSoundVolume());
		request.set(UpdateInterfaceCommand.GRID_HEIGHT, obj.getGridHeight());
		request.set(UpdateInterfaceCommand.GRID_WIDTH, obj.getGridWidth());
		request.set(UpdateInterfaceCommand.WIDGET_COUNT, obj.getWidgets().size());
		int i = 0;
		for (IWidget widget : obj.getWidgets()) {
			String base = "_" + i;
			request.set(UpdateInterfaceCommand.WIDGET_HEIGHT + base, widget.getHeight());
			request.set(UpdateInterfaceCommand.WIDGET_KEYWORDS + base, widget.getKeywords());
			request.set(UpdateInterfaceCommand.WIDGET_NAME + base, widget.getName());
			request.set(UpdateInterfaceCommand.WIDGET_PERSIST_ACTION + base, widget.getPersistAction().getId());
			request.set(UpdateInterfaceCommand.WIDGET_PERSIST_CONDITION + base, widget.getPersistCondition().getId());
			request.set(UpdateInterfaceCommand.WIDGET_TYPE + base, widget.getWidgetId());
			request.set(UpdateInterfaceCommand.WIDGET_WIDTH + base, widget.getWidth());
			request.set(UpdateInterfaceCommand.WIDGET_X + base, widget.getX());
			request.set(UpdateInterfaceCommand.WIDGET_Y + base, widget.getY());

			List<String> propertyIds = new ArrayList<String>();
			List<Object> propertyValues = new ArrayList<Object>();
			for (String propertyId : widget.getDynamicPropertyIds()) {
				propertyIds.add(propertyId);
				propertyValues.add(widget.getDynamicProperty(propertyId));
			}
			request.set(UpdateInterfaceCommand.WIDGET_PROPERTY_IDS + base, propertyIds);
			request.set(UpdateInterfaceCommand.WIDGET_PROPERTY_VALUES + base, propertyValues);
			
			++i;
		}
		InsertInterfaceCommand command = new InsertInterfaceCommand(request, response);
		command.execute();
		Long id = ((IInterface)response.get(UpdateInterfaceCommand.INTERFACE)).getId();
		
		UoW.getCurrent().commit();
		DataRegistry.flush();
		
		DomainTestUtil.assertEqual(dataFile, id, obj);
	}
}
