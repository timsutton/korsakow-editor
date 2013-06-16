package org.korsakow.ide.ui.resources;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.korsakow.ide.ui.model.EventModel;
import org.korsakow.ide.ui.resources.cellrenderers.EventModelListCellRenderer;
import org.korsakow.ide.util.UIHelper;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;

public class EventsView extends JPanel
{
	private JList eventList;
	private JButton addEventButton;
	private JButton editEventButton;
	private JButton deleteEventButton;
	
	public EventsView()
	{
		init();
		initListeners();
	}
	public void init()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(UIHelper.createHorizontalBoxLayoutPanel(
			Box.createHorizontalGlue(),
			addEventButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_ADD)),
			editEventButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_EDIT)),
			deleteEventButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_DELETE))
		));
		editEventButton.setEnabled(false);
		deleteEventButton.setEnabled(false);
		JScrollPane eventListScroll = new JScrollPane(eventList = new JList());
		eventList.setCellRenderer(new EventModelListCellRenderer());
		add(eventListScroll);
	}
	public void initListeners()
	{
		eventList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				editEventButton.setEnabled(eventList.getSelectedValue() != null);
				deleteEventButton.setEnabled(eventList.getSelectedValue() != null);
			}
		});
	}
	public void setEvents(Collection<EventModel> events)
	{
		DefaultListModel listModel = new DefaultListModel();
		for (EventModel model : events)
			listModel.addElement(model);
		eventList.setModel(listModel);
	}
	public List<EventModel> getEvents()
	{
		List<EventModel> models = new ArrayList<EventModel>();
		ListModel listModel = eventList.getModel();
		for (int i = 0; i < listModel.getSize(); ++i)
			models.add((EventModel)listModel.getElementAt(i));
		return models;
	}
	public EventModel getSelectedEvent()
	{
		return (EventModel)eventList.getSelectedValue();
	}
	public void fireAddEventAction()
	{
		addEventButton.doClick();
	}
	public void addAddEventActionListener(ActionListener listener)
	{
		addEventButton.addActionListener(listener);
	}
	public void addEditEventActionListener(ActionListener listener)
	{
		editEventButton.addActionListener(listener);
	}
	public void addDeleteEventActionListener(ActionListener listener)
	{
		deleteEventButton.addActionListener(listener);
	}
	public void addEventDoubleClickActionListener(final ActionListener listener)
	{
		eventList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (UIUtil.isRegularDoubleClick(event))
					listener.actionPerformed(new ActionEvent(event.getSource(), ActionEvent.ACTION_PERFORMED, "doubleClick"));
			}
		});
	}
}
