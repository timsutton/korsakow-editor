package org.korsakow.domain.k3.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.korsakow.domain.k3.K3Subtitle;
import org.korsakow.ide.util.FileUtil;

/**
 * I found the interface file is easily corrupted, even with the official tool
 * so I take a very lenient attitude in parsing it!
 * 
 * @author David Reisch
 *
 */
public class K3SubtitleParser
{
	public List<K3Subtitle> parse(File file) throws K3ParserException, IOException
	{
		// theres a bunch of crap in the file that we ignore here.
		
		LineContext context = new LineContext();
		context.lines = FileUtil.readFileLines(file);
		
		context.gotoLine(9);
		List<K3Subtitle> subtitles = parseSubtitles(context);
		return subtitles;
	}
	private static K3Subtitle parseSubtitle(LineContext context) throws K3ParserException
	{
		K3ParseUtil parseHelper = new K3ParseUtil(context);
		
		K3Subtitle subtitle = new K3Subtitle();
		
		String line = parseHelper.parseString(context.currentLine());
		if (!(line.startsWith("<") && line.endsWith(">")))
			throw new K3ParserException("invalid start time: " + line);
		
		subtitle.startTime = parseHelper.parseLong(line) * 1000;
		
		StringBuilder sb = new StringBuilder();
		do {
			line = context.nextLine(); // throws on eof
			sb.append(line).append(FileUtil.LINE_SEPARATOR);
		} while	(!(line.startsWith("<") && line.endsWith(">")));
		
		subtitle.text = sb.toString();
		
		line = parseHelper.parseString(context.nextLine());
		subtitle.endTime = parseHelper.parseLong(line) * 1000;
		
		return subtitle;
	}
	private static List<K3Subtitle> parseSubtitles(LineContext context) throws K3ParserException
	{
		String line;
		
		List<K3Subtitle> subtitles = new ArrayList<K3Subtitle>();
		do {
			line = context.nextLine(); // throws on eof
			if (line.trim().length() == 0)
				continue;
			if (line.trim().toLowerCase().equals("<end>")) // this is the expected terminating condition
				break;
			subtitles.add(parseSubtitle(context));
		} while (true);
		
		return subtitles;
	}
}
