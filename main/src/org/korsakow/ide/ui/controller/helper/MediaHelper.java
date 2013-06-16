package org.korsakow.ide.ui.controller.helper;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.MediaSource;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.UpdateImageCommand;
import org.korsakow.domain.command.UpdateSoundCommand;
import org.korsakow.domain.command.UpdateTextCommand;
import org.korsakow.domain.command.UpdateVideoCommand;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.ui.resources.ImageResourceView;
import org.korsakow.ide.ui.resources.SoundResourceView;
import org.korsakow.ide.ui.resources.TextResourceView;
import org.korsakow.ide.ui.resources.VideoResourceView;
import org.korsakow.ide.util.FileUtil;

public class MediaHelper {
	
	public static void initView(ImageResourceView view, IMedia media) throws MapperException
	{
		view.setResourceId(media.getId());
		view.setNameFieldText(media.getName());
		view.setKeywords(media.getKeywords());
		view.setFilename(media.getFilename());
		
		view.repaint();
		view.revalidate();
	}
	public static void initView(SoundResourceView view, ISound media) throws MapperException
	{
		view.setResourceId(media.getId());
		view.setNameFieldText(media.getName());
		view.setKeywords(media.getKeywords());
		view.setFilename(media.getFilename());
		view.setSubtitles(media.getSubtitles());
		
		view.repaint();
		view.revalidate();
	}
	public static void initView(TextResourceView view, IText media) throws MapperException, FileNotFoundException, IOException
	{
		view.setResourceId(media.getId());
		view.setNameFieldText(media.getName());
		view.setKeywords(media.getKeywords());
		view.setSource(media.getSource());
		
		switch (media.getSource())
		{
		case INLINE:
			view.setText(media.getText());
			view.setFilenameVisible(false);
			break;
		case FILE:
			view.setFilename(media.getFilename());
			String content = FileUtil.readFileAsString(media.getAbsoluteFilename());
			view.setText(content);
			break;
		}
		
		view.repaint();
		view.revalidate();
	}
	public static void initView(VideoResourceView view, IVideo media) throws MapperException
	{
		view.setResourceId(media.getId());
		view.setNameFieldText(media.getName());
		view.setKeywords(media.getKeywords());
		view.setFilename(media.getFilename());
		view.setSubtitles(media.getSubtitles());
		
		view.repaint();
		view.revalidate();
	}
	public static Request createRequest(SoundResourceView view, Long id)
	{
		Request request = new Request();
		
		request.set(UpdateSoundCommand.ID, id);
		request.set(UpdateSoundCommand.NAME, view.getNameFieldText().trim());
		request.set(UpdateSoundCommand.KEYWORDS, view.getKeywords());
		request.set(UpdateSoundCommand.FILENAME, view.getFilename());
		request.set(UpdateSoundCommand.SUBTITLES, view.getSubtitles().length()>0?view.getSubtitles():null);
		
		return request;
	}
	public static Request createRequest(ImageResourceView view, Long id)
	{
		Request request = new Request();
		
		request.set(UpdateImageCommand.ID, id);
		request.set(UpdateImageCommand.NAME, view.getNameFieldText().trim());
		request.set(UpdateImageCommand.KEYWORDS, view.getKeywords());
		request.set(UpdateImageCommand.FILENAME, view.getFilename());
		request.set(UpdateImageCommand.DURATION, view.getDuration());
		
		return request;
	}
	public static Request createRequest(TextResourceView view, Long id)
	{
		Request request = new Request();
		
		request.set(UpdateTextCommand.ID, id);
		request.set(UpdateTextCommand.NAME, view.getNameFieldText().trim());
		request.set(UpdateTextCommand.KEYWORDS, view.getKeywords());
		
		request.set(UpdateTextCommand.SOURCE, ((MediaSource)view.getSource()).getId());
		request.set(UpdateTextCommand.TEXTCONTENT, view.getText());
		
		switch ((MediaSource)view.getSource())
		{
		case INLINE:
			break;
		case FILE:
			request.set(UpdateTextCommand.FILENAME, view.getFilename());
			break;
		}
		
		return request;
	}
	public static Request createRequest(VideoResourceView view, Long id)
	{
		Request request = new Request();
		
		request.set(UpdateVideoCommand.ID, id);
		request.set(UpdateVideoCommand.NAME, view.getNameFieldText().trim());
		request.set(UpdateVideoCommand.KEYWORDS, view.getKeywords());
		request.set(UpdateVideoCommand.FILENAME, view.getFilename());
		request.set(UpdateVideoCommand.SUBTITLES, view.getSubtitles().length()>0?view.getSubtitles():null);
		
		return request;
	}
}
