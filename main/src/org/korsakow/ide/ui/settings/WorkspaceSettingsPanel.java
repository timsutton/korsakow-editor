/**
 * 
 */
package org.korsakow.ide.ui.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.factory.UIFactory;
import org.korsakow.ide.util.UIHelper;

public class WorkspaceSettingsPanel extends JPanel implements ISettingsPanel
{
	private JRadioButton dontAdjustFilenameOnSave;
	private JRadioButton absoluteAdjustFilenameOnSave;
	private JRadioButton relativeAdjustFilenameOnSave;
	private JRadioButton smartAdjustFilenameOnSave;
	private JTextArea adjustFilenameInfo;
	private JCheckBox showExperimentalWidgetsCheck;
	private JCheckBox putSimilarAtTopCheck;

	public WorkspaceSettingsPanel()
	{
		initUI();
		initListeners();
	}
	private void initUI()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel panel;
		add(panel = createAdjustFilenameOnSavePanel());
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(panel = createInterfacePanel());
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		add(UIHelper.createVerticalBoxLayoutPanel(
				UIHelper.createLabelPanel("", LanguageBundle.getString("workspacesettings.putsimilarattop.label"), putSimilarAtTopCheck = new JCheckBox(""))
		));
		
		add(Box.createVerticalGlue());
	}
	private JPanel createAdjustFilenameOnSavePanel()
	{
		JPanel adjustPanel = new JPanel();
		adjustPanel.setLayout(new BorderLayout());
		adjustPanel.setBorder(BorderFactory.createTitledBorder(LanguageBundle.getString("miscsettings.adjustonsave.label")));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
//		adjustPanel.add(UIFactory.getFactory().createLabel("", "Absolute means you can move your project file around.\nRelative means if you move your project file you also have to move your media, but if you keep all your files together you can move the entire project around easily."));
		buttonPanel.add(dontAdjustFilenameOnSave = UIFactory.getFactory().createRadioButton("dontAdjustFilename", LanguageBundle.getString("miscsettings.dontadjust.label")));
		buttonPanel.add(absoluteAdjustFilenameOnSave = UIFactory.getFactory().createRadioButton("absoluteAdjustFilename", LanguageBundle.getString("miscsettings.absoluteadjust.label")));
		buttonPanel.add(relativeAdjustFilenameOnSave = UIFactory.getFactory().createRadioButton("relativeAdjustFilename", LanguageBundle.getString("miscsettings.relativeadjust.label")));
		buttonPanel.add(smartAdjustFilenameOnSave = UIFactory.getFactory().createRadioButton("smartAdjustFilename", LanguageBundle.getString("miscsettings.smartadjust.label")));
		ButtonGroup group = new ButtonGroup();
		group.add(dontAdjustFilenameOnSave);
		group.add(absoluteAdjustFilenameOnSave);
		group.add(relativeAdjustFilenameOnSave);
		group.add(smartAdjustFilenameOnSave);
		
		adjustFilenameInfo = UIFactory.getFactory().createTextArea("");
		adjustFilenameInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		adjustFilenameInfo.setBackground(buttonPanel.getBackground());
		adjustFilenameInfo.setPreferredSize(new Dimension(260, 200));
		adjustFilenameInfo.setWrapStyleWord(true);
		adjustFilenameInfo.setLineWrap(true);
		adjustFilenameInfo.setEditable(false);

		
		adjustPanel.add(buttonPanel, BorderLayout.CENTER);
		adjustPanel.add(adjustFilenameInfo, BorderLayout.EAST);
		return adjustPanel;
	}
	private JPanel createInterfacePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Experimental Features (unsupported)"));
		
		panel.add(UIHelper.createVerticalBoxLayoutPanel(
				UIHelper.createLabelPanel("Show Experimental Widgets", showExperimentalWidgetsCheck = new JCheckBox(""))
		));
		return panel;
	}
	private void initListeners()
	{
		ChangeListener adjustSelectionListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String text = "";
				if (getDontAdjustFilename()) {
					text = LanguageBundle.getString("miscsettings.dontadjust.desc");
				} else if (getRelativeAdjustFilename()) {
					text = LanguageBundle.getString("miscsettings.relativeadjust.desc");
				} else if (getAbsoluteAdjustFilename()) {
					text = LanguageBundle.getString("miscsettings.absoluteadjust.desc");
				} else if (getSmartAdjustFilename()) {
					text = LanguageBundle.getString("miscsettings.smartadjust.desc");
				}
				adjustFilenameInfo.setText(text);
			}
		};
		dontAdjustFilenameOnSave.getModel().addChangeListener(adjustSelectionListener);
		absoluteAdjustFilenameOnSave.getModel().addChangeListener(adjustSelectionListener);
		relativeAdjustFilenameOnSave.getModel().addChangeListener(adjustSelectionListener);
		smartAdjustFilenameOnSave.getModel().addChangeListener(adjustSelectionListener);
	}
	public boolean getDontAdjustFilename()
	{
		return dontAdjustFilenameOnSave.isSelected();
	}
	public void setDontAdjustFilename(boolean selected)
	{
		dontAdjustFilenameOnSave.setSelected(selected);
	}
	public boolean getAbsoluteAdjustFilename()
	{
		return absoluteAdjustFilenameOnSave.isSelected();
	}
	public void setAbsoluteAdjustFilename(boolean selected)
	{
		absoluteAdjustFilenameOnSave.setSelected(selected);
	}
	public boolean getRelativeAdjustFilename()
	{
		return relativeAdjustFilenameOnSave.isSelected();
	}
	public void setRelativeAdjustFilename(boolean selected)
	{
		relativeAdjustFilenameOnSave.setSelected(selected);
	}
	public boolean getSmartAdjustFilename()
	{
		return smartAdjustFilenameOnSave.isSelected();
	}
	public void setSmartAdjustFilename(boolean selected)
	{
		smartAdjustFilenameOnSave.setSelected(selected);
	}
	public boolean getShowExperimentalWidgets() {
		return showExperimentalWidgetsCheck.isSelected();
	}
	public void setShowExperimentalWidgets(boolean showExperimentalWidgets) {
		showExperimentalWidgetsCheck.setSelected(showExperimentalWidgets);
	}
	public boolean getPutSimilarAtTop()
	{
		return putSimilarAtTopCheck.isSelected();
	}
	public void setPutSimilarAtTop(boolean value)
	{
		putSimilarAtTopCheck.setSelected(value);
	}
	public void dispose() {
	}
}
