package org.korsakow.domain.command;

import java.io.File;
import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Image;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.TextFactory;
import org.korsakow.domain.Video;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.util.FileUtil;

public class CreateMediaFromFileCommand extends AbstractCommand {

	public static final String FILENAME = "file";
	public static final String MEDIA = "media";

	public CreateMediaFromFileCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		String filename = request.getString(FILENAME);
		File file = new File(filename);
		
		IMedia media = createMedia(file);
		response.set(MEDIA, media);
		try {
			UoW.getCurrent().commit();
		} catch (SQLException e) {
			throw new CommandException(e);
		} catch (KeyNotFoundException e) {
			throw new CommandException(e);
		} catch (CreationException e) {
			throw new CommandException(e);
		} catch (MapperException e) {
			throw new CommandException(e);
		}
	}

	private static IMedia createMedia(File file) throws CommandException
	{
		if (FileUtil.isVideoFile(file.getName()))
			return createVideo(file);
		if (FileUtil.isSoundFile(file.getName()))
			return createSound(file);
		if (FileUtil.isImageFile(file.getName()))
			return createImage(file);
		if (FileUtil.isTextFile(file.getName()))
			return createText(file);
		throw new CommandException("unknown media: " + file);
	}
	private static IVideo createVideo(File file)
	{
		Video video = VideoFactory.createNew();
		video.setName(file.getName());
		video.setFilename(file.getAbsolutePath());
		return video;
	}
	private static IImage createImage(File file)
	{
		Image image = ImageFactory.createNew();
		image.setName(file.getName());
		image.setFilename(file.getAbsolutePath());
		return image;
	}
	private static ISound createSound(File file)
	{
		ISound sound = SoundFactory.createNew();
		sound.setName(file.getName());
		sound.setFilename(file.getAbsolutePath());
		return sound;
	}
	private static IText createText(File file)
	{
		IText text = TextFactory.createNew();
		text.setName(file.getName());
		text.setFilename(file.getAbsolutePath());
		return text;
	}
}
