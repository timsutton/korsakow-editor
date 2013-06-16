package org.korsakow.ide.ui.interfacebuilder.panel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.constraints.NumberRangeTextFieldConstraint;
import org.korsakow.ide.util.OrderedEventListenerList;
import org.korsakow.ide.util.UIUtil;

public class InfoPanel extends JPanel
{
	private JTextField xField;
	private JTextField yField;
	private JTextField widthField;
	private JTextField heightField;
	private NumberRangeTextFieldConstraint numberConstraint;
	private JLabel xLabel;
	private JLabel yLabel;
	private JLabel wLabel;
	private JLabel hLabel;
	public InfoPanel()
	{
		initUI();
		initListeners();
	}
	private void initUI()
	{
		Dimension fieldPreferredSize = new Dimension(50, 20);
		setLayout(new GridLayout(2, 2, 0, 10));
		JPanel xPanel = new JPanel();
		add(xPanel);
		xLabel = new JLabel(LanguageBundle.getString("interfacebuilder.infopanel.x.label"), JLabel.RIGHT);
		xPanel.add(xLabel);
		xPanel.add(xField = new JTextField());
		xField.setPreferredSize(fieldPreferredSize);

		JPanel yPanel = new JPanel();
		add(yPanel);
		yLabel = new JLabel(LanguageBundle.getString("interfacebuilder.infopanel.y.label"), JLabel.RIGHT);
		yPanel.add(yLabel);
		yPanel.add(yField = new JTextField());
		yField.setPreferredSize(fieldPreferredSize);
		
		JPanel widthPanel = new JPanel();
		add(widthPanel);
		wLabel = new JLabel(LanguageBundle.getString("interfacebuilder.infopanel.width.label"), JLabel.RIGHT);
		widthPanel.add(wLabel);
		widthPanel.add(widthField = new JTextField());
		widthField.setPreferredSize(fieldPreferredSize);
		
		JPanel heightPanel = new JPanel();
		add(heightPanel);
		hLabel = new JLabel(LanguageBundle.getString("interfacebuilder.infopanel.height.label"), JLabel.RIGHT);
		heightPanel.add(hLabel);
		heightPanel.add(heightField = new JTextField());
		heightField.setPreferredSize(fieldPreferredSize);
		
		setPreferredSize(new Dimension(Short.MAX_VALUE, 100));
	}
	@Override
	public void doLayout() {
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {

				Dimension d = new Dimension(Integer.MIN_VALUE, Integer.MIN_VALUE);
				d.width = Math.max(d.width, xLabel.getWidth());
				d.width = Math.max(d.width, yLabel.getWidth());
				d.width = Math.max(d.width, wLabel.getWidth());
				d.width = Math.max(d.width, hLabel.getWidth());
	
				d.height = Math.max(d.height, xLabel.getHeight());
				d.height = Math.max(d.height, yLabel.getHeight());
				d.height = Math.max(d.height, wLabel.getHeight());
				d.height = Math.max(d.height, hLabel.getHeight());
	
				if (d.equals(xLabel.getPreferredSize()))
					return;
				xLabel.revalidate();
				yLabel.revalidate();
				wLabel.revalidate();
				hLabel.revalidate();
				
				xLabel.setPreferredSize(d);
				yLabel.setPreferredSize(d);
				wLabel.setPreferredSize(d);
				hLabel.setPreferredSize(d);
			}
		});
		
		super.doLayout();
	}
	private void initListeners()
	{
		numberConstraint = new NumberRangeTextFieldConstraint(Integer.class, null, null);
		numberConstraint.addAsListenerTo(xField);
		numberConstraint.addAsListenerTo(yField);
		numberConstraint.addAsListenerTo(widthField);
		numberConstraint.addAsListenerTo(heightField);
	}
	@Override
	public void setEnabled(boolean enabled)
	{
		xField.setEnabled(enabled);
		yField.setEnabled(enabled);
		widthField.setEnabled(enabled);
		heightField.setEnabled(enabled);
		if (!enabled) {
			xField.setText(LanguageBundle.getString("interfacebuilder.infopanel.novalue"));
			yField.setText(LanguageBundle.getString("interfacebuilder.infopanel.novalue"));
			widthField.setText(LanguageBundle.getString("interfacebuilder.infopanel.novalue"));
			heightField.setText(LanguageBundle.getString("interfacebuilder.infopanel.novalue"));
		}
	}
	public void addXChangeListener(ChangeListener listener)
	{
		TextFieldUpdateListener textFieldUpdater = new TextFieldUpdateListener();
		textFieldUpdater.addAsListenerTo(xField);
		textFieldUpdater.addChangeListener(listener);
	}
	public void addYChangeListener(ChangeListener listener)
	{
		TextFieldUpdateListener textFieldUpdater = new TextFieldUpdateListener();
		textFieldUpdater.addAsListenerTo(yField);
		textFieldUpdater.addChangeListener(listener);
	}
	public void addWidthChangeListener(ChangeListener listener)
	{
		TextFieldUpdateListener textFieldUpdater = new TextFieldUpdateListener();
		textFieldUpdater.addAsListenerTo(widthField);
		textFieldUpdater.addChangeListener(listener);
	}
	public void addHeightChangeListener(ChangeListener listener)
	{
		TextFieldUpdateListener textFieldUpdater = new TextFieldUpdateListener();
		textFieldUpdater.addAsListenerTo(heightField);
		textFieldUpdater.addChangeListener(listener);
	}
	public void setXValue(int x)
	{
		xField.setText(""+x);
	}
	public int getXValue()
	{
		return Integer.parseInt(xField.getText());
	}
	public void setYValue(int y)
	{
		yField.setText(""+y);
	}
	public int getYValue()
	{
		return Integer.parseInt(yField.getText());
	}
	public void setWidthValue(int width)
	{
		widthField.setText(""+width);
	}
	public int getWidthValue()
	{
		return Integer.parseInt(widthField.getText());
	}
	public void setHeightValue(int height)
	{
		heightField.setText(""+height);
	}
	public int getHeightValue()
	{
		return Integer.parseInt(heightField.getText());
	}
	/**
	 * Aggregates "update" events for a text field, in the sense that the UI should update itself to reflect the current value of the textfield.
	 * In some sense, the user has finished editing the text field's value.
	 * These are different from swing "change" events which are updates while the user is still editing.
	 * For example pressing enter or losing focus.
	 * @author d
	 *
	 */
	public static class TextFieldUpdateListener implements ActionListener, FocusListener
	{
		private final OrderedEventListenerList listeners = new OrderedEventListenerList();
		public void addChangeListener(ChangeListener listener)
		{
			listeners.add(ChangeListener.class, listener);
		}
		/**
		 * The updater may listen on any number of events, so we hide the implementation from the caller in this method.
		 * This is the intended way to use the class.
		 * @param textField
		 */
		public void addAsListenerTo(JTextField textField)
		{
			textField.addActionListener(this);
			textField.addFocusListener(this);
		}
		public void notifyChange(final JTextField textField)
		{
			if (!textField.isEnabled()) // it seems we can still lose focus if the comp is disabled while focused
				return;
			// this is a hack. EventListeners are notified in arbitrary order. But we have to ensure that the info panel's validator listeners are run first...
			UIUtil.runUITaskLater(new Runnable() {
				public void run() {
					ChangeEvent event = new ChangeEvent(textField);
					for (ChangeListener listener : listeners.getListeners(ChangeListener.class))
						listener.stateChanged(event);
				}
			});
		}
		public void actionPerformed(ActionEvent e) {
			JTextField textField = (JTextField)e.getSource();
			notifyChange(textField);
		}
		public void focusLost(FocusEvent e) {
			JTextField textField = (JTextField)e.getSource();
			notifyChange(textField);
		}
		public void focusGained(FocusEvent e) {
		}
	}
}
