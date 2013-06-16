package org.korsakow.ide.ui.resources;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.PredicateType;
import org.korsakow.ide.resources.TriggerType;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.ide.ui.components.layout.VerticalFlowLayout;
import org.korsakow.ide.ui.components.model.KComboboxModel;
import org.korsakow.ide.ui.resources.cellrenderers.PredicateModelListCellRenderer;
import org.korsakow.ide.ui.resources.cellrenderers.PredicateTypeListCellRenderer;
import org.korsakow.ide.ui.resources.cellrenderers.RuleTypeListCellRenderer;
import org.korsakow.ide.ui.resources.cellrenderers.TriggerTypeListCellRenderer;
import org.korsakow.ide.util.UIHelper;
import org.korsakow.ide.util.UIResourceManager;

public class EventEditor extends JPanel
{
	public static final String AND = LanguageBundle.getString("eventeditor.and");
	public static final String OR = LanguageBundle.getString("eventeditor.or");
	public static final String[] AndOr = {
		AND,
		OR,
	};
	private JComboBox whenCombo;
	private JLabel whenLabel;
	private JComboBox ifCombo;
	private JComboBox ifArgCombo;
	private JLabel ifArgLabel;
	private JComponent ifArgPanel;
	private JList ifList;
	private JButton addIfButton;
	private JButton deleteIfButton;
	private JComboBox ifListAndOrCombo;
	private JComboBox thenCombo;
	private JLabel thenArgLabel;
	private JComboBox thenArgCombo;
	private JComponent thenArgPanel;
	
	public EventEditor()
	{
		init();
		initListeners();
	}
	public void init()
	{
		setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
		JPanel whenPanel = UIHelper.createHorizontalFlowLayoutPanel(
			whenCombo = new JComboBox(),
			whenLabel = new JLabel("")
		);
		whenLabel.setVisible(false);
		add(UIHelper.createLabelPanel("", LanguageBundle.getString("eventeditor.when.label"), whenPanel));
		whenCombo.setRenderer(new TriggerTypeListCellRenderer());
		
		JPanel thenPanel;
		add(thenPanel =
			UIHelper.createHorizontalFlowLayoutPanel(
				UIHelper.createLabelPanel("", LanguageBundle.getString("eventeditor.then.label"), thenCombo = new JComboBox()),
				Box.createHorizontalStrut(20),
				thenArgPanel = UIHelper.createWestEastPanel(
					thenArgLabel = new JLabel(),
					Box.createHorizontalStrut(5),
					thenArgCombo = new JComboBox()
				)
			)
		);
		thenCombo.setRenderer(new RuleTypeListCellRenderer());
		thenPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		thenArgPanel.setVisible(false);
//		thenPanel.add(thenArgCombo);

		
		add(Box.createVerticalStrut(30));
		
		JPanel ifPanel;
		add(ifPanel =
			UIHelper.createWestEastPanel(
				UIHelper.createHorizontalFlowLayoutPanel(
					addIfButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_ADD)),
					UIHelper.createLabelPanel("", LanguageBundle.getString("eventeditor.if.label"), ifCombo = new JComboBox()),
					Box.createHorizontalStrut(20),
					ifArgPanel = UIHelper.createWestEastPanel(
						ifArgLabel = new JLabel(),
						Box.createHorizontalStrut(5),
						ifArgCombo = new JComboBox()
					)
				),
				ifListAndOrCombo = new JComboBox(new DefaultComboBoxModel(AndOr))
			)
		);
//		add(ifArgCombo);
		ifCombo.setRenderer(new PredicateTypeListCellRenderer());
		ifArgPanel.setVisible(false);
		addIfButton.setEnabled(false);
		ifPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		
		
		Box ifListPanel = Box.createVerticalBox();
		add(ifListPanel);
		JScrollPane ifListScroll;
		ifListPanel.add(ifListScroll = new JScrollPane(ifList = new JList()));
		ifList.setCellRenderer(new PredicateModelListCellRenderer());
		ifListScroll.setPreferredSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		ifListScroll.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		JPanel ifButtonPanel;
		ifListPanel.add(ifButtonPanel = UIHelper.createHorizontalBoxLayoutPanel(
				Box.createHorizontalGlue(),
				addIfButton,
				deleteIfButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_DELETE)
		)));
		ifButtonPanel.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		deleteIfButton.setEnabled(false);
		ifListPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		ifListPanel.add(Box.createVerticalGlue());
	}
	public void initListeners()
	{
		ifList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				deleteIfButton.setEnabled(ifList.getModel().getSize() != 0);
			}
		});
		ifCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				addIfButton.setEnabled(ifCombo.getSelectedItem() != null);
			}
		});
	}
	public void setWhenEditable(boolean editable)
	{
		whenCombo.setVisible(editable);
		whenLabel.setVisible(!editable);
	}
	public void setWhen(TriggerType value)
	{
		whenCombo.setSelectedItem(value);
		whenLabel.setText(value.getDisplayName());
	}
	public TriggerType getWhen()
	{
		return (TriggerType)whenCombo.getSelectedItem();
	}
	public void setWhenChoices(Collection<TriggerType> choices)
	{
		whenCombo.setModel(new KComboboxModel(choices));
	}
	public void setIf(PredicateType value)
	{
		ifCombo.setSelectedItem(value);
	}
	public PredicateType getIf()
	{
		return (PredicateType)ifCombo.getSelectedItem();
	}
	public void setIfChoices(Collection<PredicateType> choices)
	{
		ifCombo.setModel(new KComboboxModel(choices));
		addIfButton.setEnabled(ifCombo.getSelectedItem() != null);
	}
	public void setIfListModel(DefaultListModel model)
	{
		ifList.setModel(model);
	}
	public void removeSelectedIf()
	{
		Object selected = ifList.getSelectedValue();
		if (selected != null)
			getIfListModel().removeElement(selected);
	}
	public DefaultListModel getIfListModel()
	{
		return (DefaultListModel)ifList.getModel();
	}
	public void setIfArgVisible(boolean visible)
	{
		ifArgPanel.setVisible(visible);
	}
	public void setIfArgLabel(String label)
	{
		ifArgLabel.setText(label);
	}
	/**
	 * @param renderer may be null, as when you just would pass an empty @choices to clear the list
	 */
	public void setIfArgChoices(Collection<?> choices, boolean editable, ListCellRenderer renderer)
	{
		// setModel causes a repaint so the order of these two operations is important
		if (renderer != null)
			ifArgCombo.setRenderer(renderer);
//		ifArgCombo.setEditable(editable); // disabled until proper support for free editing validation
		ifArgCombo.setModel(new KComboboxModel(choices));
	}
	public void setIfArg(Object value)
	{
		ifArgCombo.setSelectedItem(value);
	}
	public Object getIfArg()
	{
		return ifArgCombo.getSelectedItem();
	}
	public Object getIfListAndOr()
	{
		return ifListAndOrCombo.getSelectedItem();
	}
	public void setIfListAndOr(Object value)
	{
		ifListAndOrCombo.setSelectedItem(value);
	}
	public void setThen(RuleType value)
	{
		thenCombo.setSelectedItem(value);
	}
	public RuleType getThen()
	{
		return (RuleType)thenCombo.getSelectedItem();
	}
	public void setThenChoices(Collection<RuleType> choices)
	{
		thenCombo.setModel(new KComboboxModel(choices));
	}
	public void setThenArgVisible(boolean visible)
	{
		thenArgPanel.setVisible(visible);
	}
	public void setThenArgLabel(String label)
	{
		thenArgLabel.setText(label);
	}
	/**
	 * @param renderer may be null, as when you just would pass an empty @choices to clear the list
	 */
	public void setThenArgChoices(Collection<?> choices, boolean editable, ListCellRenderer renderer)
	{
		// setModel causes a repaint so the order of these two operations is important
		if (renderer != null)
			thenArgCombo.setRenderer(renderer);
//		thenArgCombo.setEditable(editable); // disabled until proper support for free editing validation
		thenArgCombo.setModel(new KComboboxModel(choices));
	}
	public void setThenArg(Object value)
	{
		thenArgCombo.setSelectedItem(value);
	}
	public Object getThenArg()
	{
		return thenArgCombo.getSelectedItem();
	}
	public void addWhenChangeListener(ActionListener listener)
	{
		whenCombo.addActionListener(listener);
	}
	public void addIfChangeListener(ActionListener listener)
	{
		ifCombo.addActionListener(listener);
	}
	public void addThenChangeListener(ActionListener listener)
	{
		thenCombo.addActionListener(listener);
	}
	public void addAddIfButtonActionListener(ActionListener listener)
	{
		addIfButton.addActionListener(listener);
	}
	public void addDeleteIfButtonActionListener(ActionListener listener)
	{
		deleteIfButton.addActionListener(listener);
	}
}
