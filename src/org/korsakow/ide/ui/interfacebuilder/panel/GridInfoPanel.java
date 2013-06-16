package org.korsakow.ide.ui.interfacebuilder.panel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.MyButtonGroup;
import org.korsakow.ide.ui.constraints.NumberRangeTextFieldConstraint;
import org.korsakow.ide.ui.interfacebuilder.panel.InfoPanel.TextFieldUpdateListener;
import org.korsakow.ide.util.UIHelper;

public class GridInfoPanel extends JPanel
{
	private abstract class AbstractAspectConstraint implements ActionListener, FocusListener
	{
		protected Float aspect;
		public void setAspect(Float aspect)
		{
			this.aspect = aspect;
			validate();
		}
		public void focusLost(FocusEvent event) {
			JTextField textField = (JTextField)event.getSource();
			if (!textField.isEnabled())
				return;
			validate();
		}
		public void actionPerformed(ActionEvent event) {
			JTextField textField = (JTextField)event.getSource();
			if (!textField.isEnabled())
				return;
			validate();
		}
		protected abstract void validate();
		public void focusGained(FocusEvent e){}
	}
	public class WidthAspectContraist extends AbstractAspectConstraint
	{
		@Override
		protected void validate()
		{
			if (aspect != null) {
				int width = GridInfoPanel.this.getGridWidthValue();
				int height = (int)(width / aspect);
				GridInfoPanel.this.setGridHeightValue(height);
			}
		}
	}
	public class HeightAspectContraist extends AbstractAspectConstraint
	{
		@Override
		protected void validate()
		{
			if (aspect != null) {
				int height = GridInfoPanel.this.getGridHeightValue();
				int width = (int)(height * aspect);
				GridInfoPanel.this.setGridWidthValue(width);
			}
		}
	}
	private class AspectNoneAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			widthConstraint.setAspect(null);
			heightConstraint.setAspect(null);
		}
	}
	private class Aspect1x1Action implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			widthConstraint.setAspect(1.0f/1.0f);
			heightConstraint.setAspect(1.0f/1.0f);
		}
	}
	private class Aspect4x3Action implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			widthConstraint.setAspect(4.0f/3.0f);
			heightConstraint.setAspect(4.0f/3.0f);
		}
	}
	private class Aspect16x9Action implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			widthConstraint.setAspect(16.0f/9.0f);
			heightConstraint.setAspect(16.0f/9.0f);
		}
	}
	private JTextField gridWidthField;
	private JTextField gridHeightField;
	private NumberRangeTextFieldConstraint numberConstraint;
	private AbstractAspectConstraint widthConstraint;
	private AbstractAspectConstraint heightConstraint;
	private JRadioButton aspectNone;
	private JRadioButton aspect1x1Button;
	private JRadioButton aspect16x9Button;
	private JRadioButton aspect4x3Button;
	private MyButtonGroup aspectGroup;
	public GridInfoPanel()
	{
		initUI();
		initListeners();
	}
	private void initUI()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
		JPanel whPanel = new JPanel(new GridLayout(1, 2, 0, 0));
		Dimension fieldPreferredSize = new Dimension(50, 20);
		
		JPanel widthPanel = new JPanel();
		whPanel.add(widthPanel);
		widthPanel.add(new JLabel(LanguageBundle.getString("interfacebuilder.gridinfo.gridwidth.label")));
		widthPanel.add(gridWidthField = new JTextField());
		gridWidthField.setPreferredSize(fieldPreferredSize);
		
		JPanel heightPanel = new JPanel();
		whPanel.add(heightPanel);
		heightPanel.add(new JLabel(LanguageBundle.getString("interfacebuilder.gridinfo.gridheight.label")));
		heightPanel.add(gridHeightField = new JTextField());
		gridHeightField.setPreferredSize(fieldPreferredSize);
		
		JPanel aspectPanel = new JPanel(new GridLayout(2, 2));
		aspectPanel.add(aspectNone = new JRadioButton("Arbitrary"));
		aspectPanel.add(aspect4x3Button = new JRadioButton("4:3"));
		aspectPanel.add(aspect1x1Button = new JRadioButton("1:1"));
		aspectPanel.add(aspect16x9Button = new JRadioButton("16:9"));

		aspectNone.setSelected(true);
		aspectGroup = new MyButtonGroup();
		aspectGroup.add(aspectNone);
		aspectGroup.add(aspect1x1Button);
		aspectGroup.add(aspect4x3Button);
		aspectGroup.add(aspect16x9Button);

		add(UIHelper.createVerticalBoxLayoutPanel(
				whPanel,
				Box.createVerticalStrut(20),
				aspectPanel
		));
		add(Box.createVerticalGlue());
	}
	private void initListeners()
	{
		numberConstraint = new NumberRangeTextFieldConstraint(Integer.class, 0, null);
		numberConstraint.addAsListenerTo(gridWidthField);
		numberConstraint.addAsListenerTo(gridHeightField);
		
		widthConstraint = new WidthAspectContraist();
		gridWidthField.addFocusListener(widthConstraint);
		gridWidthField.addActionListener(widthConstraint);
		heightConstraint = new HeightAspectContraist();
		gridHeightField.addFocusListener(heightConstraint);
		gridHeightField.addActionListener(heightConstraint);
		
		aspectNone.addActionListener(new AspectNoneAction());
		aspect1x1Button.addActionListener(new Aspect1x1Action());
		aspect4x3Button.addActionListener(new Aspect4x3Action());
		aspect16x9Button.addActionListener(new Aspect16x9Action());
	}
//	public void setEnabled(boolean enabled)
//	{
//		gridWidthField.setEnabled(enabled);
//		gridHeightField.setEnabled(enabled);
//		if (!enabled) {
//			gridWidthField.setText(LanguageBundle.getString("interfacebuilder.gridinfo.novalue"));
//			gridHeightField.setText(LanguageBundle.getString("interfacebuilder.gridinfo.novalue"));
//		}
//	}
	public void addWidthChangeListener(ChangeListener listener)
	{
		TextFieldUpdateListener textFieldUpdater = new TextFieldUpdateListener();
		textFieldUpdater.addAsListenerTo(gridWidthField);
		textFieldUpdater.addChangeListener(listener);
		final AspectChangeNotifier aspectUpdater = new AspectChangeNotifier(gridWidthField, textFieldUpdater);
		aspectNone.addActionListener(aspectUpdater);
		aspect1x1Button.addActionListener(aspectUpdater);
		aspect4x3Button.addActionListener(aspectUpdater);
		aspect16x9Button.addActionListener(aspectUpdater);
	}
	public void addHeightChangeListener(ChangeListener listener)
	{
		TextFieldUpdateListener textFieldUpdater = new TextFieldUpdateListener();
		textFieldUpdater.addAsListenerTo(gridHeightField);
		textFieldUpdater.addChangeListener(listener);
		final AspectChangeNotifier aspectUpdater = new AspectChangeNotifier(gridHeightField, textFieldUpdater);
		aspectNone.addActionListener(aspectUpdater);
		aspect1x1Button.addActionListener(aspectUpdater);
		aspect4x3Button.addActionListener(aspectUpdater);
		aspect16x9Button.addActionListener(aspectUpdater);
	}
	public boolean isGridAspectLocked()
	{
		return !aspectNone.isSelected();
	}
	public void setGridWidthValue(int width)
	{
		gridWidthField.setText(""+width);
	}
	public int getGridWidthValue()
	{
		try {
			return Integer.parseInt(gridWidthField.getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	public void setGridHeightValue(int height)
	{
		gridHeightField.setText(""+height);
	}
	public int getGridHeightValue()
	{
		try {
			return Integer.parseInt(gridHeightField.getText());
		} catch (NumberFormatException e) {
			return 0;
		} 
	}
	private static class AspectChangeNotifier implements ActionListener
	{
		private final JTextField field;
		private final TextFieldUpdateListener updater;
		public AspectChangeNotifier(JTextField field, TextFieldUpdateListener updater)
		{
			this.field = field;
			this.updater = updater;
		}
		public void actionPerformed(ActionEvent event) {
			updater.notifyChange(field);
		}
	}
}
