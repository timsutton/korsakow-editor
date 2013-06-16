package org.korsakow.ide.ui.interfacebuilder.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Settings;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.ui.components.KList;
import org.korsakow.ide.ui.components.ToolTipRenderer;
import org.korsakow.ide.ui.dnd.WidgetTypeTransferable;
import org.korsakow.ide.ui.interfacebuilder.WidgetCellRenderer;
import org.korsakow.ide.util.UIUtil;

public class WidgetsPanel extends JPanel
{
	private static final Hashtable<WidgetType, String> tooltips = new Hashtable<WidgetType, String>();
	private static Map<String, Object[]> ListSections = new HashMap<String, Object[]>();
	static
	{
		ListSections.put("", new Object[] {
			WidgetType.MainMedia,
			WidgetType.SnuAutoLink,
			WidgetType.InsertText,
			WidgetType.Subtitles,
		});
		ListSections.put("Media Controls", new Object[] {
			" ",
			WidgetType.Scrubber,
			WidgetType.PlayButton,
			WidgetType.MasterVolume,
			WidgetType.PlayTime,
			WidgetType.TotalTime,
			WidgetType.FullscreenButton,
		});
		ListSections.put("Experimental", new Object[] {
			" ",
			WidgetType.MediaControls,
			WidgetType.Comments,
			WidgetType.History,
			WidgetType.SnuAutoMultiLink,
		});
		
		tooltips.put(WidgetType.MainMedia, LanguageBundle.getString("widget.mainmedia.tooltip"));
		tooltips.put(WidgetType.SnuAutoLink, LanguageBundle.getString("widget.snuautolink.tooltip"));
		tooltips.put(WidgetType.SnuFixedLink, LanguageBundle.getString("widget.snufixedlink.tooltip"));
		tooltips.put(WidgetType.SnuAutoMultiLink, LanguageBundle.getString("widget.snuautomultilink.tooltip"));
		tooltips.put(WidgetType.InsertText, LanguageBundle.getString("widget.inserttext.tooltip"));
		tooltips.put(WidgetType.Subtitles, LanguageBundle.getString("widget.subtitles.tooltip"));
		tooltips.put(WidgetType.MasterVolume, LanguageBundle.getString("widget.mastervolume.tooltip"));
		tooltips.put(WidgetType.MediaArea, LanguageBundle.getString("widget.mediaarea.tooltip"));
		tooltips.put(WidgetType.History, LanguageBundle.getString("widget.history.tooltip"));
		tooltips.put(WidgetType.Scrubber, LanguageBundle.getString("widget.scrubber.tooltip"));
	}
	private static boolean isAltDown;
	private KList widgetList;
	public WidgetsPanel()
	{
		initUI();
		initListeners();
	}
	private void initUI()
	{
		setLayout(new BorderLayout());
		
		DefaultListModel listModel = new DefaultListModel();
		
		for (Object o : ListSections.get(""))
			listModel.addElement(o);
		
		for (Object o : ListSections.get("Media Controls"))
			listModel.addElement(o);
		
		boolean showExperimental;
		try {
			ISettings settings = SettingsInputMapper.find();
			showExperimental = settings.getBoolean(Settings.ShowExperimentalWidgets);
		} catch (MapperException e) {
			showExperimental = false;
		}
		if (showExperimental)
			for (Object o : ListSections.get("Experimental"))
				listModel.addElement(o);
		
		widgetList = new KList();
		widgetList.setModel(listModel);
		UIUtil.setDragOnPress(widgetList);
		widgetList.setCellRenderer(new WidgetCellRenderer());
		widgetList.setToolTipRenderer(new MyToolTipRenderer());
		widgetList.setTransferHandler(new TransferHandler() {
			@Override
			public int getSourceActions(JComponent component)
			{
				return TransferHandler.COPY;
			}
			@Override
			public Transferable createTransferable(JComponent component)
			{
				JList list = (JList)component;
				Object value = list.getSelectedValue();
				if (value == null || (value instanceof WidgetType == false))
					return null;
				return new WidgetTypeTransferable((WidgetType)value, isAltDown);
			}
			@Override
			public boolean canImport(JComponent comp, DataFlavor[] flavours) {
				return false;
			}
			@Override
			public boolean importData(JComponent comp, Transferable t) {
				return false;
			}
		});
		widgetList.setDragEnabled(true);
		final JScrollPane scroll = new JScrollPane(widgetList) {
			@Override
			public Dimension getPreferredSize() {
				final Dimension preferredSize = widgetList.getPreferredSize();
				preferredSize.height += 10;
				return preferredSize;
			}
		};
		add(scroll);
	}
	private void initListeners()
	{
		widgetList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				WidgetsPanel.isAltDown = event.isAltDown();
			}
		});
		widgetList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Object value = widgetList.getSelectedValue();
				if (value instanceof WidgetType)
					widgetList.setToolTipText(tooltips.get(value));
				else
					widgetList.setToolTipText("");
			}
		});
	}
	private class MyToolTipRenderer implements ToolTipRenderer
	{
		public String getToolTipText(MouseEvent event)
		{
			JList list = (JList)event.getSource();
			int index = list.locationToIndex(event.getPoint());
			if (index == -1)
				return "";
			if (list.getModel().getElementAt(index) instanceof WidgetType == false)
				return null;
			WidgetType type = (WidgetType)list.getModel().getElementAt(index);
			return tooltips.get(type);
		}
		
	}
}
