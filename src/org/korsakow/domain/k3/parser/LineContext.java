/**
 * 
 */
package org.korsakow.domain.k3.parser;

import java.util.ArrayList;
import java.util.List;

public class LineContext
{
	public List<String> lines = new ArrayList<String>();
	public int currentLine = -1;
	public String peekNextLine() throws K3ParserException
	{
		if (!hasNextLine())
			throw new K3ParserException("unexpected EOF");
		return lines.get(currentLine+1);
	}
	public String nextLine() throws K3ParserException
	{
		if (!hasNextLine())
			throw new K3ParserException("unexpected EOF");
		return lines.get(++currentLine);
	}
	public boolean hasNextLine()
	{
		return currentLine < lines.size()-1;
	}
	public String currentLine()
	{
		return lines.get(currentLine);
	}
	public String gotoLine(int lineNum)
	{
		String line = lines.get(lineNum); // checks bounds
		currentLine = lineNum;
		return line;
	}
}