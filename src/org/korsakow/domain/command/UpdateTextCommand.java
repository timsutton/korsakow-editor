package org.korsakow.domain.command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.MediaSource;
import org.korsakow.domain.Text;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.domain.mapper.input.TextInputMapper;
import org.korsakow.ide.util.FileUtil;

public class UpdateTextCommand extends AbstractCommand{


	public static final String TEXT = "text";
	public static final String TEXTCONTENT = "text";
	public static final String KEYWORDS = "keywords";
	public static final String FILENAME = "filename";
	public static final String SOURCE = "source";
	public static final String NAME = "name";
	public static final String ID = "id";

	public UpdateTextCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Text v = null;
			v = TextInputMapper.map(request.getLong(ID));
			v.setName(request.getString(NAME));
			v.setSource(MediaSource.getById(request.getString(SOURCE)));
			switch (v.getSource())
			{
			case INLINE:
				v.setFilename("");
				v.setText(request.getString(TEXTCONTENT));
				break;
			case FILE:
				v.setText("");
				v.setFilename(request.getString(FILENAME));
				if (request.getString(TEXTCONTENT) != null)
					FileUtil.writeFileFromString(v.getAbsoluteFilename(), request.getString(TEXTCONTENT));
				break;
			}
			v.setKeywords((Collection<IKeyword>)request.get(KEYWORDS));
			response.set(TEXT, v);
			
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
		} catch (IOException e) {
			throw new CommandException(e);
		}
	}

}
