/**
 * 
 */
package org.korsakow.services.export.task;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.services.encoders.font.FontEncoder;
import org.korsakow.services.encoders.font.FontEncoderException;
import org.korsakow.services.encoders.font.FontEncoderFactory;
import org.korsakow.services.encoders.font.FontFormat;
import org.korsakow.services.export.Exporter;

public class FontExportTask extends AbstractTask
{
	private final Collection<Font> fontsToExport;
	private final File destFile;
	public FontExportTask(Collection<Font> fontsToExport, File destFile)
	{
		super();
		this.fontsToExport = fontsToExport;
		this.destFile = destFile;
	}
	@Override
	public String getTitleString()
	{
		return LanguageBundle.getString("export.task.encodingfonts");
	}
	@Override
	public void runTask() throws TaskException
	{
//		if (destFile.exists()) {
//			Boolean overwriteOption;
//			synchronized (exportOptions) {
//				overwriteOption = exportOptions.overwriteExisting;
//			}
//			// if option already set to false, then abort
//			if (overwriteOption == Boolean.FALSE)
//				return;
//			
//			// if undecided, ask
//			if (overwriteOption == null) {
//				DialogOptions dialogOptions =  Application.getInstance().showFileOverwriteDialog("File exists", destFile.getName() + " already exists, YES to overwrite or NO to skip.");
//				if (dialogOptions.applyToAll) {
//					// apply to all means set the global option
//					synchronized (exportOptions) {
//						exportOptions.overwriteExisting = dialogOptions.dialogResult;
//					}
//				}
//				if (!dialogOptions.dialogResult)
//					return;
//			}
//		}
		
		if (!fontsToExport.isEmpty()) {
			// this is for the benefit of the flash player
			// which due to a technicality can use only embedded fonts...
			fontsToExport.add(new Font("Arial", Font.PLAIN, 12));
			
			FontEncoderFactory.getDefaultFactory().addRequiredInputFormat(FontFormat.TTF);
			FontEncoderFactory.getDefaultFactory().addRequiredOutputFormat(FontFormat.SWF);
			destFile.getParentFile().mkdirs();
			try {
				FontEncoder fontEncoder = FontEncoderFactory.getDefaultFactory().createFontEncoder();
				fontEncoder.addAllFonts(fontsToExport);
				fontEncoder.encode(Exporter.FONT_EXPORT_FORMAT, new FileOutputStream(destFile));
			} catch (FontEncoderException e) {
				throw new TaskException(e);
			} catch (FileNotFoundException e) {
				throw new TaskException(e);
			}
		}
	}
}