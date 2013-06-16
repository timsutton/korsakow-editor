package org.korsakow.ide;

import java.sql.ResultSet;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.korsakow.ide.util.Util;
import org.korsakow.services.finder.NodeListAdapter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathHelper
{
	private static String escapeArg(String arg)
	{
		if (arg.indexOf('"') != -1) { // avoid performance penalty
			// oh lordy, XPath 1.0 doesn't define an escaping mechanism
			// blogs.msdn.com/shjin/archive/2005/07/25/443077.aspx
			// this is undoubtedly terribly unperformant!
			String[] parts = arg.split("\"");
			if (arg.endsWith("\"")) // String.split doesn't include trailing empty strings
				parts = Util.arrayAdd(parts, "");
			String esc = "concat('', \""+Util.join(parts, "\",'\"',\"") + "\")"; // FuncCocnat only allows > 1 arguments
			return esc;
		} else
			return '"' + arg + '"';
	}
	public static String formatQuery(String format, Object... args)
	{
		String[] parts = format.split("[?]");
		if (parts.length-1 != args.length)
			throw new IllegalArgumentException("Number of positions (" + (parts.length-1) + ") doesn't match argument count: " + format + "(" + args.length + ")");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; ++i) {
			sb.append(parts[i])
				.append(escapeArg(args[i].toString()));
		}
		sb.append(parts[parts.length-1]);
		return sb.toString();
	}
	private static class ThreadLocalXPath extends ThreadLocal<XPath>
	{
		@Override
		protected XPath initialValue() {
			return XPathFactory.newInstance().newXPath();
		}
	}
	private static ThreadLocal<XPath> xpathLocal = new ThreadLocalXPath();
	private static XPath getXPath()
	{
		return xpathLocal.get();
	}
	
	private final Node inputSource;
	public XPathHelper(Node inputSource)
	{
		if (inputSource == null)
			throw new NullPointerException();
		this.inputSource = inputSource;
		
	}
	
	// =====================================================================================
	// =====================================================================================
	// nonstatic methods
	// =====================================================================================
	// =====================================================================================
	public Element xpathAsElement(String query) throws XPathExpressionException
	{
		return XPathHelper.xpathAsElement(inputSource, query);
	}
	public Element xpathAsElement(String query, Object... args) throws XPathExpressionException
	{
		return XPathHelper.xpathAsElement(inputSource, query, args);
	}
	public Node xpathAsNode(String query) throws XPathExpressionException
	{
		return XPathHelper.xpathAsNode(inputSource, query);
	}
	public Node xpathAsNode(String query, Object... args) throws XPathExpressionException
	{
		return XPathHelper.xpathAsNode(inputSource, query, args);
	}
	public NodeList xpathAsNodeList(String query) throws XPathExpressionException
	{
		return XPathHelper.xpathAsNodeList(inputSource, query);
	}
	public NodeList xpathAsNodeList(String query, Object... args) throws XPathExpressionException
	{
		return XPathHelper.xpathAsNodeList(inputSource, query, args);
	}
	public String xpathAsString(String query) throws XPathExpressionException
	{
		return XPathHelper.xpathAsString(inputSource, query);
	}
	public String xpathAsString(String query, Object... args) throws XPathExpressionException
	{
		return XPathHelper.xpathAsString(inputSource, query, args);
	}
	public int xpathAsInt(String query) throws XPathExpressionException
	{
		return XPathHelper.xpathAsInt(inputSource, query);
	}
	public int xpathAsInt(String query, Object... args) throws XPathExpressionException
	{
		return XPathHelper.xpathAsInt(inputSource, query, args);
	}
	public long xpathAsLong(String query) throws XPathExpressionException
	{
		return XPathHelper.xpathAsLong(inputSource, query);
	}
	public long xpathAsLong(String query, Object... args) throws XPathExpressionException
	{
		return XPathHelper.xpathAsLong(inputSource, query, args);
	}
	public boolean xpathAsBoolean(String query) throws XPathExpressionException
	{
		return XPathHelper.xpathAsBoolean(inputSource, query);
	}
	public boolean xpathAsBoolean(String query, Object... args) throws XPathExpressionException
	{
		return XPathHelper.xpathAsBoolean(inputSource, query, args);
	}
	public float xpathAsFloat(String query) throws XPathExpressionException
	{
		return XPathHelper.xpathAsFloat(inputSource, query);
	}
	public float xpathAsFloat(String query, Object... args) throws XPathExpressionException
	{
		return XPathHelper.xpathAsFloat(inputSource, query, args);
	}
	public double xpathAsDouble(String query) throws XPathExpressionException
	{
		return XPathHelper.xpathAsDouble(inputSource, query);
	}
	public ResultSet xpathAsResultSet(String query) throws XPathExpressionException
	{
		return XPathHelper.xpathAsResultSet(inputSource, query);
	}
	public ResultSet xpathAsResultSet(String query, Object... args) throws XPathExpressionException
	{
		return XPathHelper.xpathAsResultSet(inputSource, query, args);
	}
	public List<Node> xpathAsList(String query, Object... args) throws XPathExpressionException
	{
		return XPathHelper.xpathAsList(inputSource, query, args);
	}

	// =====================================================================================
	// =====================================================================================
	// static methods
	// =====================================================================================
	// =====================================================================================
	public static Element xpathAsElement(Node inputSource, String query) throws XPathExpressionException
	{
		return (Element)xpathAsNode(inputSource, query);
	}
	public static Element xpathAsElement(Node inputSource, String query, Object... args) throws XPathExpressionException
	{
		return xpathAsElement(inputSource, formatQuery(query, args));
	}
	public static Node xpathAsNode(Node inputSource, String query) throws XPathExpressionException
	{
//		System.out.println(query);
		return (Node)getXPath().evaluate(query, inputSource, XPathConstants.NODE);
	}
	public static Node xpathAsNode(Node inputSource, String query, Object... args) throws XPathExpressionException
	{
		return xpathAsNode(inputSource, formatQuery(query, args));
	}
	public static NodeList xpathAsNodeList(Node inputSource, String query) throws XPathExpressionException
	{
		return (NodeList)getXPath().evaluate(query, inputSource, XPathConstants.NODESET);
	}
	public static NodeList xpathAsNodeList(Node inputSource, String query, Object... args) throws XPathExpressionException
	{
		return xpathAsNodeList(inputSource, formatQuery(query, args));
	}
	public static List<Node> xpathAsList(Node inputSource, String query) throws XPathExpressionException
	{
		return new NodeListAdapter(xpathAsNodeList(inputSource, query));
	}
	public static List<Node> xpathAsList(Node inputSource, String query, Object... args) throws XPathExpressionException
	{
		return new NodeListAdapter(xpathAsNodeList(inputSource, query, args));
	}
	public static String xpathAsString(Node inputSource, String query) throws XPathExpressionException
	{
		//System.out.println(query);
		return getXPath().evaluate(query, inputSource);
	}
	public static String xpathAsString(Node inputSource, String query, Object... args) throws XPathExpressionException
	{
		return xpathAsString(inputSource, formatQuery(query, args));
	}
	public static int xpathAsInt(Node inputSource, String query) throws XPathExpressionException
	{
		return Integer.parseInt(xpathAsString(inputSource, query));
	}
	public static int xpathAsInt(Node inputSource, String query, Object... args) throws XPathExpressionException
	{
		return xpathAsInt(inputSource, formatQuery(query, args));
	}
	public static long xpathAsLong(Node inputSource, String query) throws XPathExpressionException
	{
		return Long.parseLong(xpathAsString(inputSource, query));
	}
	public static long xpathAsLong(Node inputSource, String query, Object... args) throws XPathExpressionException
	{
		return xpathAsLong(inputSource, formatQuery(query, args));
	}
	public static boolean xpathAsBoolean(Node inputSource, String query) throws XPathExpressionException
	{
		return Boolean.parseBoolean(xpathAsString(inputSource, query));
	}
	public static boolean xpathAsBoolean(Node inputSource, String query, Object... args) throws XPathExpressionException
	{
		return xpathAsBoolean(inputSource, formatQuery(query, args));
	}
	public static float xpathAsFloat(Node inputSource, String query) throws XPathExpressionException
	{
		return Float.parseFloat(xpathAsString(inputSource, query));
	}
	public static float xpathAsFloat(Node inputSource, String query, Object... args) throws XPathExpressionException
	{
		return xpathAsFloat(inputSource, formatQuery(query, args));
	}
	public static double xpathAsDouble(Node inputSource, String query) throws XPathExpressionException
	{
		return Double.parseDouble(xpathAsString(inputSource, query));
	}
	public static ResultSet xpathAsResultSet(Node inputSource, String query) throws XPathExpressionException
	{
		return new NodeListResultSet(xpathAsNodeList(inputSource, query));
	}
	public static ResultSet xpathAsResultSet(Node inputSource, String query, Object... args) throws XPathExpressionException
	{
		return new NodeListResultSet(xpathAsNodeList(inputSource, formatQuery(query, args)));
	}
	
}
