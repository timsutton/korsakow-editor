package org.korsakow.domain.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.RuleFactory;
import org.korsakow.domain.Snu;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.ISnu.BackgroundSoundMode;
import org.korsakow.domain.mapper.input.ImageInputMapper;
import org.korsakow.domain.mapper.input.MediaInputMapper;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.domain.proxy.InterfaceProxy;
import org.korsakow.domain.proxy.SoundProxy;
import org.korsakow.domain.proxy.UnknownMediaProxy;
import org.korsakow.ide.resources.ResourceType;

public class UpdateSnuCommand extends AbstractCommand{
	private final Log log = LogFactory.getLog(getClass());


	public static final String RULE_COUNT = "ruleCount";
	public static final String RULE_PROPERTY_VALUES = "rulePropertyValues";
	public static final String RULE_PROPERTY_IDS = "rulePropertyIds";
	public static final String RULE_TRIGGER_TIME = "ruleTriggerTime";
	public static final String RULE_KEYWORDS = "ruleKeywords";
	public static final String RULE_TYPE = "ruleType";
	public static final String RULE_NAME = "ruleName";
	
	public static final String SNU = "snu";
	public static final String KEYWORDS = "keywords";
	public static final String INSERT_TEXT = "insert_text";
	public static final String PREVIEW_TEXT = "preview_text";
	public static final String PREVIEW_MEDIA_ID = "preview_media_id";
	public static final String ENDER = "ender";
	public static final String STARTER = "starter";
	public static final String LOOPING = "looping";
	public static final String MAX_LINKS = "max_links";
	public static final String LIVES = "lives";
	public static final String INTERFACE_ID = "interface_id";
	public static final String BACKGROUND_SOUND_VOLUME = "background_sound_volume";
	public static final String BACKGROUND_SOUND_ID = "background_sound_id";
	public static final String BACKGROUND_SOUND_MODE = "background_sound_mode";
	public static final String BACKGROUND_SOUND_LOOPING = "background_sound_looping";
	public static final String RATING = "rating";
	public static final String MAIN_MEDIA_ID = "main_media_id";
	public static final String MAIN_MEDIA_CUSTOM_DURATION = "main_media_custom_duration";
	public static final String NAME = "name";
	public static final String ID = "id";

	public UpdateSnuCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Snu s = null;
			s = SnuInputMapper.map(request.getLong(ID));
			s.setName(request.getString(NAME));
			
			IMedia mainMedia = null;
			if (request.get(MAIN_MEDIA_ID) != null)
				mainMedia = MediaInputMapper.map( request.getLong(MAIN_MEDIA_ID) );
			s.setMainMedia(mainMedia);
			if (s.getMainMedia() != null) {
				s.getMainMedia().setName(s.getName());
				UoW.getCurrent().registerDirty(s.getMainMedia());
				((Response)response).addModifiedResource(s.getMainMedia());

				if (request.has(MAIN_MEDIA_CUSTOM_DURATION)) {
					if ( ResourceType.IMAGE.isInstance(mainMedia) ) {
						IImage image = ImageInputMapper.map(mainMedia.getId());
						image.setDuration( request.getLong(MAIN_MEDIA_CUSTOM_DURATION) );
						UoW.getCurrent().registerDirty(mainMedia);
					} else
						log.error(String.format("MAIN_MEDIA_CUSTOM_DURATION set for media other than image"));
				}
			}
			
			s.setRating(request.getFloat(RATING));
			
			ISound backgroundSound = null;
			if (request.get(BACKGROUND_SOUND_ID) != null)
				backgroundSound = new SoundProxy(request.getLong(BACKGROUND_SOUND_ID));
			s.setBackgroundSound(backgroundSound);
			s.setBackgroundSoundMode((BackgroundSoundMode)request.get(BACKGROUND_SOUND_MODE));
			s.setBackgroundSoundVolume(request.getFloat(BACKGROUND_SOUND_VOLUME));
			s.setBackgroundSoundLooping(request.getBoolean(BACKGROUND_SOUND_LOOPING));
			
			IInterface interf = null;
			if (request.get(INTERFACE_ID) != null)
				interf = new InterfaceProxy(request.getLong(INTERFACE_ID));
			s.setInterface(interf);

			s.setLives(request.getLong(LIVES));

			s.setMaxLinks(request.getLong(MAX_LINKS));
			
			s.setLooping(request.getBoolean(LOOPING));
			
			s.setStarter(request.getBoolean(STARTER));
			s.setEnder(request.getBoolean(ENDER));
			
			IMedia previewMedia = null;
			if (request.get(PREVIEW_MEDIA_ID) != null)
				previewMedia = new UnknownMediaProxy(request.getLong(PREVIEW_MEDIA_ID));
			s.setPreviewMedia(previewMedia);
			s.setPreviewText(request.getString(PREVIEW_TEXT));
			
			s.setInsertText(request.getString(INSERT_TEXT));
			
			s.setKeywords((Collection<IKeyword>)request.get(KEYWORDS));

			s.setRules(getRules(request));
			
			response.set(SNU, s);
			UoW.getCurrent().registerDirty(s);
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
	public static List<IRule> getRules(Helper request)
	{
		return getRules(request, "");
	}
	private static List<IRule> getRules(Helper request, String base)
	{
		int ruleCount = request.getInt(RULE_COUNT + base);
		List<IRule> rules = new ArrayList<IRule>();
		for (int i = 0; i < ruleCount; ++i)
		{
			String nextBase = base + "_" + i;
			IRule rule = RuleFactory.createNew();
			rule.setName(request.getString(RULE_NAME+nextBase));
			rule.setRuleType(request.getString(RULE_TYPE+nextBase));
			rule.setKeywords((Collection<IKeyword>)request.get(RULE_KEYWORDS+nextBase));
			rule.setTriggerTime(request.getLong(RULE_TRIGGER_TIME+nextBase));
			List<String> propertyIds = (List<String>)request.get(RULE_PROPERTY_IDS+nextBase);
			List<Object> propertyValues = (List<Object>)request.get(RULE_PROPERTY_VALUES+nextBase);
			for (int j = 0; j < propertyIds.size(); ++j)
				rule.setDynamicProperty(propertyIds.get(j), propertyValues.get(j));
			rules.add(rule);
			
			if (request.has(RULE_COUNT + nextBase))
			{
				rule.setRules(getRules(request, nextBase));
			}
		}
		return rules;
	}
}
