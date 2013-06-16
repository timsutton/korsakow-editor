package org.korsakow.domain.k3.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.korsakow.domain.k3.K3Interface;
import org.korsakow.domain.k3.K3Widget;
import org.korsakow.ide.util.FileUtil;

/**
 * I found the interface file is easily corrupted, even with the official tool
 * so I take a very lenient attitude in parsing it!
 * 
 * @author David Reisch
 *
 */
public class K3InterfaceParser
{
	public K3Interface parse(File file) throws K3ParserException, IOException
	{
		// theres a bunch of crap in the file that we ignore here.
		
		LineContext context = new LineContext();
		context.lines = FileUtil.readFileLines(file);
		if (context.lines.size() < 9)
			throw new K3InterfaceParserException("Invalid interface file");
		context.gotoLine(8);
		Collection<K3Widget> widgets = parseWidgets(context);
		K3Interface interf = new K3Interface();
		interf.widgets = widgets;
		return interf;
	}
	private static Collection<K3Widget> parseWidgets(LineContext context) throws K3ParserException
	{
		
		if (-1 == context.nextLine().toUpperCase().indexOf("<STARTBOXES>"))
			throw new K3InterfaceParserException("expected '<STARTBOXES>', found: '" + context.currentLine() + "'", context.currentLine+1);
		
		Collection<K3Widget> widgets = new ArrayList<K3Widget>();
		while (true)
		{
			if (!context.hasNextLine())
				throw new K3InterfaceParserException("expected '<ENDBOXES>', found: EOF", context.currentLine+1);
			if (-1 != context.nextLine().toUpperCase().indexOf("<ENDBOXES>"))
				break;
			if (context.currentLine().length() == 0)
				continue;
			K3Widget widget = parseWidget(context);
			widgets.add(widget);
		}
		return widgets;
	}
	private static K3Widget parseWidget(LineContext context) throws K3ParserException
	{
		K3ParseUtil parseHelper = new K3ParseUtil(context);
		String line = context.currentLine();
		String[] bits = line.split(",");
		if (bits.length != 7)
			throw new K3InterfaceParserException("invalid widget line: " + line, context.currentLine+1);
		// bits[0].equals("NIX")
		// bits[1].equals("1")
		String type = parseHelper.parseString(bits[2]);
		int left = parseHelper.parseInt(bits[3]);
		int top = parseHelper.parseInt(bits[4]);
		int right = parseHelper.parseInt(bits[5]);
		int bottom = parseHelper.parseInt(bits[6]);
		
		K3Widget widget = new K3Widget();
		widget.type = type;
		widget.left = left;
		widget.top = top;
		widget.right = right;
		widget.bottom = bottom;
		
		return widget;
	}
}
