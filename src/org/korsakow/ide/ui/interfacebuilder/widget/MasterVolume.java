package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;

public class MasterVolume extends WidgetModel
{
	private static class MasterVolumeWidgetComponent extends WidgetComponent
	{
		private JLabel label;
		private JSlider slider;
		public MasterVolumeWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		protected void initUI()
		{
			super.initUI();
			setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			setSize(200, 80);
			add(label = new JLabel(LanguageBundle.getString("widget.mastervolume.label")));
			label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			slider = new JSlider(0, 100, 100);
			slider.setMajorTickSpacing(20);
			slider.setMinorTickSpacing(20);
			slider.setPaintTrack(true);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			Hashtable<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
			labels.put(0, new JLabel(LanguageBundle.getString("widget.mastervolume.ticks.0")));
			labels.put(100, new JLabel(LanguageBundle.getString("widget.mastervolume.ticks.100")));
			slider.setLabelTable(labels);
			slider.setOrientation(JSlider.HORIZONTAL);
			slider.setEnabled(false);
			add(slider);
		}
		protected void initListeners()
		{
			addComponentListener(new ComponentListener() {
				public void componentHidden(ComponentEvent e) {
				}
				public void componentMoved(ComponentEvent e) {
				}
				public void componentShown(ComponentEvent e) {
				}
				public void componentResized(ComponentEvent e) {
					slider.setPaintTicks(getSize().width > 70);
					slider.setPaintLabels(getSize().width > 70);
				}
			});
		}
	}
	protected WidgetComponent createComponent()
	{
		return new MasterVolumeWidgetComponent(this);
	}
	public MasterVolume()
	{
		super(WidgetType.MasterVolume);
//		addProperty(new Property("volume") {
//			public Object getValue() { return getVolume(); }
//			public void setValue(Object value) { setVolume(Float.parseFloat(value.toString())); }
//		});
	}
//	public void setVolume(float volume)
//	{
//		slider.setValue((int)(volume * 100));
//	}
//	public float getVolume()
//	{
//		return slider.getValue() / 100;
//	}
}







