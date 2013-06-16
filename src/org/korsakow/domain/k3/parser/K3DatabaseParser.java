package org.korsakow.domain.k3.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.korsakow.domain.k3.K3Project;
import org.korsakow.domain.k3.K3ProjectSettings;
import org.korsakow.domain.k3.K3RatingCalculator;
import org.korsakow.domain.k3.K3Rule;
import org.korsakow.domain.k3.K3Snu;
import org.korsakow.domain.k3.code.K3Symbol;
import org.korsakow.ide.util.FileUtil;

/**
 * File format is flakey, so parsing is leaninent.
 * @author d
 *
 */
public class K3DatabaseParser
{
	public K3Project parse(File file) throws K3ParserException, IOException
	{
		LineContext context = new LineContext();
		context.lines = FileUtil.readFileLines(file);
		
		K3ProjectSettings settings = new K3ProjectSettings();
		context.gotoLine(14);
		parseSettings(context, settings);
		List<K3Snu> snus = new ArrayList<K3Snu>();
		context.gotoLine(24);
		while (context.hasNextLine()) {
			String next = context.peekNextLine();
			
			// satellite section
			if (next.startsWith("#") && next.contains("#LOOP#")) {
				// currently we just skip it
				context.nextLine();
				context.nextLine();
				while (!context.currentLine().startsWith("#"))
					context.nextLine();
				
				continue;
			}
			
			snus.add(parseSnu(context));
		}
		
		K3Project project = new K3Project();
		project.settings = settings;
		project.snus = snus;
		
		return project;
	}
	private static K3Snu parseSnu(LineContext context) throws K3ParserException
	{
		K3ParseUtil parseHelper = new K3ParseUtil(context);
		
		if (!context.nextLine().startsWith("#"))
			throw new K3ParserException("expected '#', found: '" + context.currentLine() + "'");
		K3Snu snu = new K3Snu();
		
		// line 1
		String line1 = context.nextLine();
//		System.out.println(line1);
		String[] bits = line1.split(",");
		if (bits.length > 0) snu.filename = parseHelper.parseString(bits[0]);
		if (bits.length > 1) snu.foldername = parseHelper.parseString(bits[1]);
		if (bits.length > 2) snu.lives = parseHelper.specialNullValue( parseHelper.parseLong(bits[2]), 999L);
		if (bits.length > 3) snu.backgroundSoundEnabled = parseHelper.parseBooleanPrefix(bits[3], "s");
		if (bits.length > 4) snu.duration = parseHelper.parseLong(bits[4]);
		if (bits.length > 5) snu.looping = parseHelper.parseBooleanPrefix(bits[5], "l");
		
		// line 2
		String line2 = context.nextLine();
		bits = line2.split(",");
		if (bits.length > 0) snu.previewText = parseHelper.parseString(bits[0]);
		if (bits.length > 1) snu.insertText = parseHelper.parseString(bits[1]);
		
		context.nextLine(); // rLink
		snu.movieRating = (float)K3RatingCalculator.calculate(parseHelper.parseFloat(context.nextLine()));
		context.nextLine(); // _sing/NL

		String line = "";
		
		while (context.hasNextLine()) {
			line = context.nextLine(); 
			if (line.startsWith("#"))
				break;
			bits = line.split(",");
			long time = (long)(parseHelper.parseFloat(bits[0].trim().split(" ")[0]) * 1000); // db file is in seconds
			Long maxLinks = (long)(parseHelper.parseFloat(bits[1].trim().split(" ")[0]));
			if (maxLinks == 0) // 0 means no max
				maxLinks = null;
			
			String code = "";

			K3Rule rule = new K3Rule();
			
			for (int i = 2; i < bits.length; ++i)
				code += bits[i] + K3Symbol.DEFAULT_WHITESPACE_STRING;
			
			rule.lineNumber = context.currentLine;
			
			rule.time = time;
			rule.maxLinks = maxLinks;
			rule.code = code;
			
			snu.rules.add(rule);
		}
		
		if (!line.startsWith("#"))
			throw new K3ParserException("expected '#', found: '" + line + "'");
		
		return snu;
	}
	private static void parseSettings(LineContext context, K3ProjectSettings settings) throws K3ParserException
	{
		K3ParseUtil parseHelper = new K3ParseUtil(context);
		// first configuration line
		String settingsLine1 = context.nextLine();
		String[] parts = settingsLine1.split(",");
		if (parts.length <= 29)
			throw new K3ParserException("missing first configuration line", context.currentLine+1);
		settings.startFilmFilename = parseHelper.parseString(parts[0]);
		settings.startFilmFoldername = parseHelper.parseString(parts[1]);
		settings.logWindow = parseHelper.parseString(parts[2]);
		settings.movieRatingFactor = parseHelper.parseFloat(parts[3]);
		settings.automaticClick = parseHelper.parseBooleanValue(parts[4], "clicklessMouseAn");
		settings.unknown16_6 = parseHelper.parseString(parts[5]);
		settings.keepOldLinksIfNoNewLinks = parseHelper.parseBooleanValue(parts[6], "ruecktrittAn");
		settings.randomLinkMode = parseHelper.parseBooleanValue(parts[7], "randomLinkAn");
		settings.endFilmFilename = parseHelper.parseString(parts[8]);
		settings.endFilmFoldername = parseHelper.parseString(parts[9]);
		settings.randomLinkIcon = parseHelper.parseString(parts[10]);
		settings.unknown16_12 = parseHelper.parseString(parts[11]);
		settings.unknown16_13 = parseHelper.parseString(parts[12]);
		settings.pseudoRandomLink = parseHelper.parseString(parts[13]);
		settings.presentationMode = parseHelper.parseBooleanPrefix(parts[14], "PresMode=");
		settings.positiveLinking = parseHelper.parseBooleanPrefix(parts[15], "posLink");
		settings.kairoSpecialSetting = parseHelper.parseBooleanPrefix(parts[16], "kairo=");
		settings.videoWidth = parseHelper.parseInt(parts[17]);
		settings.videoHeight = parseHelper.parseInt(parts[18]);
		settings.subtitles = parseHelper.parseString(parts[19]);
		settings.stageWidth = parseHelper.parseInt(parts[20]);
		settings.stageHeight = parseHelper.parseInt(parts[21]);
		settings.backgroundColor = parseHelper.parseInt(parts[22], "BgCol");
		settings.foregroundColor = parseHelper.parseInt(parts[23], "FoCol");
		settings.insertTextColor = parseHelper.specialNullValue( parseHelper.parseInt(parts[24], "iTxtC"), 999 );
		settings.insertTextFontFamily = parseHelper.specialNullValue( parseHelper.parseStringPrefix(parts[25], "iTxtF"), "999" );
		settings.previewTextColor = parseHelper.specialNullValue( parseHelper.parseInt(parts[26], "PTxtC"), 999 );
		settings.previewTextFontFamily = parseHelper.specialNullValue( parseHelper.parseStringPrefix(parts[27], "PTxtF"), "999" );
		settings.subtitleTextColor = parseHelper.specialNullValue( parseHelper.parseInt(parts[28], "STxtC"), 999 );
		settings.subtitleTextFontFamily = parseHelper.specialNullValue( parseHelper.parseStringPrefix(parts[29], "STxtF"), "999" );
		
		// second configuration line
		String settingsLine2 = context.nextLine();
		parts = settingsLine2.split(",");
		if (parts.length >  0) settings.insertTextSize = parseHelper.parseInt(parts[0], "iTxtS");
		if (parts.length >  1) settings.previewTextSize = parseHelper.parseInt(parts[1], "PTxtS");
		if (parts.length >  2) settings.subtitleTextSize = parseHelper.parseInt(parts[2], "STxtS");
		if (parts.length >  3) settings.link3Lines = parseHelper.parseBooleanPrefix(parts[3], "3Link3Lines=");
		if (parts.length >  4) settings.use3LinkInterface = parseHelper.parseBooleanPrefix(parts[4], "3Link=");
		if (parts.length >  5) settings.delayValue = parseHelper.parseInt(parts[5], "delayValue=");
		if (parts.length >  6) settings.loopSnus = parseHelper.parseBooleanPrefix(parts[6], "loopMain=");
		
		try {// version compatibility
			if (parts.length >  7) settings.loopPreviews = parseHelper.parseBooleanPrefix(parts[7], "prvwL=");
		} catch (K3ParserException e) {
			if (parts.length >  7) settings.loopPreviews = parseHelper.parseBooleanPrefix(parts[7], "loopPreview=");
		}
		
		if (parts.length >  8) settings.satelliteMode = parseHelper.parseBooleanPrefix(parts[8], "sat=");
		if (parts.length >  9) settings.backgroundSound = parseHelper.parseBooleanPrefix(parts[9], "BGsound=");
		if (parts.length > 10) settings.backgroundSoundVolume = parseHelper.parseInt(parts[10], "vol=")/255.0f;
		if (parts.length > 11) settings.chair = parseHelper.parseBooleanPrefix(parts[11], "chair=");
		if (parts.length > 12) settings.unknown17_13 = parseHelper.parseStringPrefix(parts[12], "TmRel=");
		if (parts.length > 13) settings.saveHistory = parseHelper.parseBoolean(parts[13].substring(parts[13].length()-1)); // this one is prefixed by a random number WTF!!!!!
		if (parts.length > 14) settings.databaseHistory = parts[14];
		if (parts.length > 15) settings.manyLinkPreviewWidth = parseHelper.parseInt(parts[15], "iconW");
		if (parts.length > 16) settings.manyLinkPreviewHeight = parseHelper.parseInt(parts[16], "iconH");
		if (parts.length > 17) settings.unknown17_18 = parts[17];
		if (parts.length > 18) settings.satelliteId = parts[18];
		if (parts.length > 19) settings.unknown17_20 = parts[19];
		if (parts.length > 20) settings.unknown17_21 = parts[20];
		if (parts.length > 21) settings.unknown17_22 = parts[21];
		if (parts.length > 22) settings.useSnuAsPreview = parseHelper.parseBooleanPrefix(parts[22], "mPrev");
		if (parts.length > 23) settings.manyLinkMaxLinks = parseHelper.parseInt(parts[23], "MaxLnk");
		if (parts.length > 24) settings.useNewInterface = parseHelper.parseBooleanPrefix(parts[24], "NEWi");
		if (parts.length > 25) settings.autoLinkMode = parseHelper.parseBooleanPrefix(parts[25], "AuLi");
		if (parts.length > 26) settings.autoLinkTimeout = parseHelper.parseInt(parts[26], "AuLiNach");
		
	}
}
