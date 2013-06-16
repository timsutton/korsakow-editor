package org.korsakow.ide.ui.resources;

import java.awt.BorderLayout;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.JPanel;
import javax.swing.text.StyledDocument;

import org.korsakow.domain.MediaSource;
import org.korsakow.ide.util.Util;

import com.hexidec.ekit.EkitCore;

public class TextResourceView extends MediaResourceView
{
	private Object mediaSource;
	private EkitCore ekitCore;
	protected void initUI()
	{
		super.initUI();
		
		List<String> toolbarSequence = Arrays.asList(
//				EkitCore.KEY_TOOL_OPEN,
//				EkitCore.KEY_TOOL_SAVE,
				
				EkitCore.KEY_TOOL_UNDO,
				EkitCore.KEY_TOOL_REDO,

				EkitCore.KEY_TOOL_SEP,
				
				EkitCore.KEY_TOOL_BOLD,
				EkitCore.KEY_TOOL_ITALIC,
				EkitCore.KEY_TOOL_UNDERLINE,
				EkitCore.KEY_TOOL_FONTS,
				
				EkitCore.KEY_TOOL_SEP,
				
				EkitCore.KEY_TOOL_UNICODE,
				EkitCore.KEY_TOOL_ANCHOR,
				
				EkitCore.KEY_TOOL_SEP,
				
				EkitCore.KEY_TOOL_COPY,
				EkitCore.KEY_TOOL_PASTE
		);
		
		String sDocument = null;
		String sStyleSheet = null;
		String sRawDocument = null;
		StyledDocument sdocSource = null;
		URL urlStyleSheet = null;
		boolean includeToolBar = true;
		boolean showViewSource = false;
		boolean showMenuIcons = true;
		boolean editModeExclusive = true;
		String sLanguage = Locale.getDefault().getLanguage();
		String sCountry = Locale.getDefault().getCountry();
		boolean base64 = false;
		boolean debugMode = true;
		boolean hasSpellChecker = false;
		boolean multiBar = false;
		String toolbarSeq = Util.join(toolbarSequence, "|");
		boolean useFormatting = true;
		
		ekitCore = new EkitCore(sDocument, sStyleSheet, sRawDocument, sdocSource, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, hasSpellChecker, multiBar, toolbarSeq, useFormatting);
		JPanel ekitPanel = new JPanel(new BorderLayout());
		ekitPanel.add(ekitCore.getToolBar(true), BorderLayout.NORTH);
		ekitPanel.add(ekitCore.getTextScrollPane(), BorderLayout.CENTER);
		mediaPanel.add(ekitPanel);
	}
	public void setText(String text)
	{
		// java and therefore ekit cant handle xhtml, so simplify <br /> to <br>
		text = text.replaceAll("<br\\s* (?:\\/)?>", "<br>");
		text = text.replaceAll("\n", "<br />");
		ekitCore.setDocumentText(text);
	}
	public String getText()
	{
		String body = ekitCore.getDocumentBody();
		if (body.startsWith("\n")) // artifact of ekit?
			body = body.substring(1);
		// we want to output xhtml though so back we go!
		body = body.replaceAll("\n", "<br />");
		body = body.replaceAll("<br\\s*>", "<br />");
		return "<html><body>" + body + "</body></html>";
	}
	public void setSource(Object mediaSource)
	{
		this.mediaSource = mediaSource;
	}
	public Object getSource()
	{
		return this.mediaSource;
	}
	public void dispose()
	{
		super.dispose();
	}
}
