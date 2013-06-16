package org.korsakow.services.encoders.subtitle.srt;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.korsakow.services.encoders.subtitle.ISubtitle;

public class SrtEncoder
{
	private final List<ISubtitle> subtitles = new ArrayList<ISubtitle>();
	
	public void addSubtitle(ISubtitle subtitle)
	{
		subtitles.add(subtitle);
	}
	
	public void encode(OutputStream output) throws IOException
	{
		Collections.sort(subtitles);
		
		PrintWriter writer = new PrintWriter(output);
		
		int counter = 0;
		for (ISubtitle subtitle : subtitles)
		{
			writer.println(counter);
			writer.print(formatTime(subtitle.getStartTime()));
			writer.print(" --> ");
			writer.print(formatTime(subtitle.getEndTime()));
			writer.println();
			writer.println(subtitle.getText());
			writer.println();
		}
		
		output.close();
	}
	public String formatTime(long time)
	{
		int hours = (int)(time / 60 / 60 / 1000);
		int minutes = (int)(time / 60 / 1000);
		int seconds = (int)(time / 1000);
		int millis = (int)(time % 1000);
		return String.format("%.2d:%.2d:%.2d,%.3d", hours, minutes, seconds, millis);
	}
}
