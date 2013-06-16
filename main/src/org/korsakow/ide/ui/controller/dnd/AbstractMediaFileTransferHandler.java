/**
 * 
 */
package org.korsakow.ide.ui.controller.dnd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.CreateMediaFromFileCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.ui.dnd.InternalMediaTransferHandler;
import org.korsakow.ide.ui.dnd.AggregateFileTransferHandler.FileTransferHandler;
import org.korsakow.ide.util.FileUtil;

public abstract class AbstractMediaFileTransferHandler implements FileTransferHandler
{
	protected abstract boolean importMedia(List<? extends IMedia> media);
	private static IMedia newMediaInstance(File file) throws CommandException
	{
		Request request = new Request();
		request.set(CreateMediaFromFileCommand.FILENAME, file.getAbsolutePath());
		Response response = new Response();
		CommandExecutor.executeCommand(CreateMediaFromFileCommand.class, request, response);
		IMedia media = (IMedia)response.get(CreateMediaFromFileCommand.MEDIA);
		return media;
	}
	public static List<? extends IMedia> convertToMedia(List<File> files)
	{
		List<IMedia> media = new ArrayList<IMedia>();
		for (File file : files) {
			try {
				IMedia medium = newMediaInstance(file);
				medium.setName((file.getName()));
				medium.setFilename(file.getCanonicalPath());
				media.add(medium);
			} catch (IOException e) {
	        	Logger.getLogger(InternalMediaTransferHandler.class).error("", e);
				continue;
			} catch (CommandException e) {
	        	Logger.getLogger(InternalMediaTransferHandler.class).error("", e);
				continue;
			}
		}
		return media;
	}
	public boolean importData(List<File> files)
	{
		List<File> toKeep = new ArrayList<File>();
		for (File file : files) {
			if (!FileUtil.isVideoFile(file.getName()) &&
				!FileUtil.isSoundFile(file.getName()) &&
				!FileUtil.isImageFile(file.getName()) &&
				!FileUtil.isTextFile(file.getName()))
				continue;
			toKeep.add(file);
		}
		List<? extends IMedia> media = convertToMedia(toKeep);
		return importMedia(media);
	}
}