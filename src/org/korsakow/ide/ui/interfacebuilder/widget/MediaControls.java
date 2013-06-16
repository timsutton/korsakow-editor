package org.korsakow.ide.ui.interfacebuilder.widget;

import javax.swing.JLabel;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.property.BooleanProperty;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.editors.MediaControlsWidgetEditor;

public class MediaControls extends WidgetModel
{
	private static class MediaControlsWidgetComponent extends WidgetComponent
	{
		public MediaControlsWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			add(new JLabel(LanguageBundle.getString("widget.mediacontrols.label")));
			setSize(640, 30);
		}
	    @Override
		public boolean getMaintainsAspectByDefaultWhenResized()
	    {
	    	return false;
	    }
	}
	private boolean playPause = true;
	private boolean playTime = true;
	private boolean scrubber = true;
	private boolean loading = true;
	private boolean totalTime = true;
	private boolean volume = true;
	private boolean scrubberInteractive = true;
	public MediaControls()
	{
		super(WidgetType.MediaControls);
		addProperty(new BooleanProperty("showPlayPause", false) {
			@Override
			public Boolean getValue() { return playPause; }
			@Override
			public void setValue(Boolean value) { playPause = value; }
		});
		addProperty(new BooleanProperty("showPlayTime", false) {
			@Override
			public Boolean getValue() { return playTime; }
			@Override
			public void setValue(Boolean value) { playTime = value; }
		});
		addProperty(new BooleanProperty("showScrubber", false) {
			@Override
			public Boolean getValue() { return scrubber; }
			@Override
			public void setValue(Boolean value) { scrubber = value; }
		});
		addProperty(new BooleanProperty("scrubberInteractive", false) {
			@Override
			public Boolean getValue() { return scrubberInteractive; }
			@Override
			public void setValue(Boolean value) { scrubberInteractive = value; }
		});
		addProperty(new BooleanProperty("showLoading", false) {
			@Override
			public Boolean getValue() { return loading; }
			@Override
			public void setValue(Boolean value) { loading = value; }
		});
		addProperty(new BooleanProperty("showTotalTime", false) {
			@Override
			public Boolean getValue() { return totalTime; }
			@Override
			public void setValue(Boolean value) { totalTime = value; }
		});
		addProperty(new BooleanProperty("showVolume", false) {
			@Override
			public Boolean getValue() { return volume; }
			@Override
			public void setValue(Boolean value) { volume = value; }
		});
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new MediaControlsWidgetComponent(this);
	}
	@Override
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new MediaControlsWidgetEditor(this);
	}
}
