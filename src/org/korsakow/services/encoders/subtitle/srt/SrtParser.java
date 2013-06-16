package org.korsakow.services.encoders.subtitle.srt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Pair;
import org.korsakow.ide.util.Util;

public class SrtParser
{
	public static void main(String[] args) throws IOException {
		final String works = "/Users/d/Work/Korsakow/export/2srts/xWORKS.srt";
		final String broken = "/Users/d/Work/Korsakow/export/2srts/xDOES_NOT_WORK.srt";
		String path = broken;
		SrtParser parser = new SrtParser();
		String raw = FileUtil.readFileAsString(new File(path));
		parser.parseRaw(raw);
		System.out.println("Valid");
	}
	
	public static class CuePoint {
		public final String name;
		public final long begin;
		public final long duration;
		public final String content;
		public CuePoint(String name, long begin, long duration, String content) {
			this.name = name;
			this.begin = begin;
			this.duration = duration;
			this.content = content;
		}
	}
	private static final Pattern timeLinePattern = Pattern.compile("([0-9]{2}):([0-9]{2}):([0-9]{2}),([0-9]{3}) --> ([0-9]{2}):([0-9]{2}):([0-9]{2}),([0-9]{3})");
	public void parseRaw(String raw) {
		Map<Long, CuePoint> cuepoints = new LinkedHashMap<Long, CuePoint>();
		List<String> lines = Arrays.asList(raw.split("(?:\\r\\n)|\\n|\\r/"));
		int line = 0;
		int counter = 0;
		while ( line < lines.size() ) {
			if (lines.get(line).isEmpty()) {
				++line;
				continue;
			}
			Pair<Integer, CuePoint> ret = parseCuePoint(lines, line, counter);
			line = ret.getFirst();
			counter++;
			cuepoints.put(ret.getSecond().begin, ret.getSecond());
		}
	}
	
	public Pair<Integer, CuePoint> parseCuePoint(List<String> lines, int offset, int counter) {
		int count = Integer.parseInt(lines.get(offset++));
		if (count !=  counter + 1)
			throw new IllegalArgumentException("inconsistant file at line #" + (offset-1));
		
		final String timeLine = lines.get(offset++);
		Matcher matcher = timeLinePattern.matcher(timeLine);
		if (!matcher.matches())
			throw new IllegalArgumentException("invalid time at line #" + (offset-1) + " | " + timeLine);
		long begin = getTime(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4)) / 1000;
		long end =   getTime(matcher.group(5), matcher.group(6), matcher.group(7), matcher.group(8)) / 1000;
		
		List<String> content = new ArrayList<String>();
		for (; offset < lines.size(); ++offset) {
			if (!lines.get(offset).isEmpty()) {
				++offset;
				break;
			}
			content.add( lines.get(offset) );
		}
		String name = "" + counter;
		
		return new Pair<Integer, CuePoint>(offset, new CuePoint( name, begin, end-begin, Util.join(content, "\r")));
	}
	public long getTime( String hh, String mm, String ss, String ms ) {
		int h = Integer.parseInt(hh);
		int m = Integer.parseInt(mm);
		int s = Integer.parseInt(ss);
		int z = Integer.parseInt(ms);
		return (h*60*60 + m*60 + s) * 1000 + z;
	}
}
