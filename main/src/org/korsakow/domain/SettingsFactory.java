package org.korsakow.domain;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.ide.DataRegistry;

public class SettingsFactory {
	public static Settings createNew(long id, long version)
	{
		Settings settings = new Settings(id, version);
		settings.setBoolean(Settings.EXPORT_FONTS, false);
		settings.setBoolean(Settings.EncodeVideoOnExport, true);
		settings.setString(Settings.VideoEncodingProfile, "flv_high");
		settings.setString(Settings.AdjustFilenamesOnSave, Settings.AdjustFilenames.Smart.getId());
		settings.setBoolean(Settings.PutSimilarResourcesAtTop, true);
		settings.setBoolean(Settings.ShowBackgroundPreview, true);
		settings.setBoolean(Settings.ShowExperimentalWidgets, false);

		settings.setBoolean(Settings.ExportVideos, true);
		settings.setBoolean(Settings.ExportImages, true);
		settings.setBoolean(Settings.ExportSounds, true);
		settings.setBoolean(Settings.ExportSubtitles, true);
		settings.setBoolean(Settings.ExportWebFiles, true);
		
		UoW.getCurrent().registerNew(settings);
		return settings;
	}
	public static Settings createNew()
	{
		return createNew(DataRegistry.getMaxId(), 0);
	}
	public static Settings createClean(long id, long version)
	{
		Settings settings = new Settings(id, version);
		settings.setBoolean(Settings.EXPORT_FONTS, false);
		settings.setBoolean(Settings.EncodeVideoOnExport, true);
		settings.setString(Settings.VideoEncodingProfile, "flv_high");
		settings.setString(Settings.AdjustFilenamesOnSave, Settings.AdjustFilenames.Smart.getId());
		settings.setBoolean(Settings.PutSimilarResourcesAtTop, true);
		settings.setBoolean(Settings.ShowBackgroundPreview, true);
		settings.setBoolean(Settings.ShowExperimentalWidgets, false);

		settings.setBoolean(Settings.ExportVideos, true);
		settings.setBoolean(Settings.ExportImages, true);
		settings.setBoolean(Settings.ExportSounds, true);
		settings.setBoolean(Settings.ExportSubtitles, true);
		settings.setBoolean(Settings.ExportWebFiles, true);

		UoW.getCurrent().registerClean(settings);
		return settings;
	}
}
