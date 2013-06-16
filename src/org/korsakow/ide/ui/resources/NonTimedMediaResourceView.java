package org.korsakow.ide.ui.resources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.korsakow.ide.ui.components.NewMediaPanel;
import org.korsakow.ide.ui.laf.KorsakowSliderUI;
import org.korsakow.ide.util.UIHelper;


public class NonTimedMediaResourceView extends MediaResourceView
{
	protected boolean durationUpdating;
	protected long durationValue;
	protected JSlider durationSlider;
	protected JTextField durationField;
	@Override
	protected void initUI()
	{
		super.initUI();
		mediaPanel.add(UIHelper.createHorizontalFlowLayoutPanel(
				new JLabel("Duration"),
				durationSlider = new JSlider(0, 3600*1000, 30*1000),
				Box.createHorizontalStrut(10),
				durationField = new JTextField("5.0")
				));
		durationField.setBorder(BorderFactory.createLineBorder(Color.gray));
		durationField.setEditable(true);
		durationField.addFocusListener( new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				Long time = NewMediaPanel.parseTime( durationField.getText() );
				if ( time != null ) {
					durationValue = time;
				}
				durationUpdating = true;
				durationSlider.setValue( (int)durationValue );
				durationUpdating = false;
				durationField.setText( NewMediaPanel.formatTime( durationValue ) );
			}
			@Override
			public void focusGained(FocusEvent e) {
			}
		} );
		durationField.setPreferredSize(new Dimension(70, 15));
		durationSlider.setPaintLabels(true);
		durationSlider.putClientProperty( KorsakowSliderUI.SCROLL_INCREMENT, 1000 );
		durationSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				if ( durationUpdating )
					return;
				durationValue = durationSlider.getValue();
				durationField.setText( NewMediaPanel.formatTime( durationValue ) );
			}
		});
	}
	
	public long getDuration()
	{
		return durationValue;
	}
	public void setDuration(long duration)
	{
		durationValue = duration;
		durationUpdating = true;
		durationSlider.setValue((int)duration);
		durationUpdating = false;
		durationField.setText( NewMediaPanel.formatTime( durationValue ) );
	}
}
