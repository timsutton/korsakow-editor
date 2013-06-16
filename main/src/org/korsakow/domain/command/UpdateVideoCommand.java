package org.korsakow.domain.command;

import java.sql.SQLException;
import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Video;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.domain.mapper.input.VideoInputMapper;

public class UpdateVideoCommand extends AbstractCommand{


	public static final String VIDEO = "video";
	public static final String KEYWORDS = "keywords";
	public static final String SUBTITLES = "subtitles";
	public static final String FILENAME = "filename";
	public static final String NAME = "name";
	public static final String ID = "id";
	public UpdateVideoCommand(Helper request, Helper response) {
		super(request, response);
		
	}
	public void execute()
	throws CommandException {
		try {
			Video v = null;
			v = VideoInputMapper.map(request.getLong(ID));
			v.setName(request.getString(NAME));
			v.setFilename(request.getString(FILENAME));
			v.setSubtitles(request.getString(SUBTITLES));
			v.setKeywords((Collection<IKeyword>)request.get(KEYWORDS));
			response.set(VIDEO, v);
			
			for (ISnu snu : SnuInputMapper.findByMainMediaId(v.getId())) {
				snu.setName(v.getName());
				UoW.getCurrent().registerDirty(snu);
				((Response)response).addModifiedResource(snu);
			}
			
			UoW.getCurrent().registerDirty(v);
			UoW.getCurrent().commit();
			UoW.newCurrent();
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (SQLException e) {
			throw new CommandException(e);
		} catch (KeyNotFoundException e) {
			throw new CommandException(e);
		} catch (CreationException e) {
			throw new CommandException(e);
		}
	}
}
