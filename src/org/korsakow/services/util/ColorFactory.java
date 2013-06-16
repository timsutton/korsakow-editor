package org.korsakow.services.util;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorFactory
{
	public static String toString(Color color)
	{
		return formatCSS(color);
	}
	/**
	 * #RGB
	 */
	public static String formatCSS(Color color)
	{
		int rgb = color.getRGB();
		rgb = rgb & 0xFFFFFF; // strip alpha for css
		String str = "" + Integer.toHexString(rgb);
		str = rightZeroPad(str, 6);
		return '#' + str;
	}
	public static Color createRGB(int rgb)
	{
		int r = (rgb & 0xFF0000)>>16;
		int g = (rgb & 0x00FF00)>>8;
		int b = (rgb & 0x0000FF);
		return new Color(r, g, b);
	}
	/**
	 * @str #rrggbbaa
	 */
	public static Color createRGB(String str)
	{
		return parseColor(str);
	}
	private static Color parseColor(String str)
	{
		Pattern pattern = Pattern.compile("#([a-zA-Z0-9]{2})([a-zA-Z0-9]{2})([a-zA-Z0-9]{2})");
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches())
			throw new NumberFormatException(str);
		int red = Integer.parseInt(matcher.group(1), 16);
		int green = Integer.parseInt(matcher.group(2), 16);
		int blue = Integer.parseInt(matcher.group(3), 16);
		return new Color(red, green, blue);
	}
	private static String rightZeroPad(String str, int length)
	{
		StringBuilder sb = new StringBuilder(str);
		for (int i = str.length(); i < length; ++i)
			sb.append('0');
		return sb.toString();
	}
}
