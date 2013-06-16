package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.mapper.input.MediaInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.media.MediaFactory;
import org.korsakow.ide.resources.media.Playable;
import org.korsakow.ide.resources.property.AbstractProperty;
import org.korsakow.ide.resources.property.BooleanProperty;
import org.korsakow.ide.resources.property.LongProperty;
import org.korsakow.ide.resources.widget.PlayMode;
import org.korsakow.ide.resources.widget.ScalingPolicy;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.editors.MediaAreaWidgetEditor;
import org.korsakow.ide.ui.components.ResourceIcon;

public class MediaArea extends WidgetModel
{
	private static class MediaAreaWidgetComponent extends WidgetComponent
	{
		private JLabel label;
		private ResourceIcon icon;
		private Component mediaComponent;
		private AspectRatioWrapper wrapper;
		private Playable playable;
		public MediaAreaWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			setLayout(new BorderLayout());
			setSize(80, 80);
			
			add(label = new JLabel(LanguageBundle.getString("widget.mediaarea.label")), BorderLayout.NORTH);
			label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
			
			icon = new ResourceIcon();
		}
		@Override
		public void setEnabled(boolean b)
		{
			super.setEnabled(b);
		}
		public void setMedia(IMedia media)
		{
			if (media != null)
				icon.setResource(ResourceType.forId(media.getType()).getIcon(), media.getName());
			else
				icon.clear();
			
			if (playable != null) {
				playable.dispose();
				playable = null;
			}
			if (mediaComponent != null) {
				mediaComponent.getParent().remove(mediaComponent);
				mediaComponent = null;
			}
			label.setVisible(media == null);
			if (media != null) {
				playable = MediaFactory.getMediaNoThrow(media);
				mediaComponent = playable.getComponent();
				wrapper = new AspectRatioWrapper(mediaComponent, playable, getWidget().getScalingPolicy());
				add(wrapper, BorderLayout.CENTER);
			}
			revalidate();
			repaint();
		}
		@Override
		public MediaArea getWidget() {
			return (MediaArea)super.getWidget();
		}
		public AspectRatioWrapper getWrapper() {
			return wrapper;
		}
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new MediaAreaWidgetComponent(this);
	}
	@Override
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new MediaAreaWidgetEditor(this);
	}
	private IMedia media;
	private PlayMode playMode = PlayMode.Always;
	private Boolean looping = Boolean.FALSE;
	protected ScalingPolicy scalingPolicy = ScalingPolicy.MaintainAspectRatio;
	public MediaArea()
	{
		super(WidgetType.MediaArea);
		addProperty(new LongProperty("mediaId") {
			@Override
			public Long getValue() { return getMedia()!=null?getMedia().getId():null; }
			@Override
			public void setValue(Long value) {
				try {
					setMedia(value!=null?MediaInputMapper.map(value):null);
				} catch (Exception e) {
					Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
				}
			}
		});
		addProperty(new AbstractProperty("scalingPolicy") {
			@Override
			public Object getValue() { return getScalingPolicy().getId(); }
			@Override
			public void setValue(Object value) {
				if (value instanceof ScalingPolicy == false)
					value = ScalingPolicy.forId(value.toString());
				setScalingPolicy((ScalingPolicy)value);
			}
		});
		addProperty(new AbstractProperty("playMode") {
			@Override
			public Object getValue() { return getPlayMode().getId(); }
			@Override
			public void setValue(Object value) {
				if (value instanceof PlayMode == false)
					value = PlayMode.forId(value.toString());
				setPlayMode((PlayMode)value);
			}
		});
		addProperty(new BooleanProperty("looping", false) {
			@Override
			public Boolean getValue() { return getLooping(); }
			@Override
			public void setValue(Boolean value) {
				setLooping(value);
			}
		});
	}
	@Override
	public MediaAreaWidgetComponent getComponent() {
		return (MediaAreaWidgetComponent)super.getComponent();
	}
	public void setMedia(IMedia media)
	{
		getComponent().setMedia(media);
		IMedia oldMedia = this.media;
		this.media = media;
		firePropertyChange("media", oldMedia, media);
	}
	public IMedia getMedia()
	{
		return media;
	}
	public ScalingPolicy getScalingPolicy()
	{
		return scalingPolicy;
	}
	public void setScalingPolicy(ScalingPolicy scalingPolicy)
	{
		ScalingPolicy oldValue = scalingPolicy;
		this.scalingPolicy = scalingPolicy;
		if (getComponent().getWrapper() != null)
			getComponent().getWrapper().setScalingPolicy(scalingPolicy);
		firePropertyChange("scalingPolicy", oldValue, scalingPolicy);
	}
	public Boolean getLooping()
	{
		return looping;
	}
	public void setLooping(Boolean looping)
	{
		this.looping = looping;
	}
	public PlayMode getPlayMode()
	{
		return playMode;
	}
	public void setPlayMode(PlayMode playMode)
	{
		PlayMode oldValue = playMode;
		this.playMode = playMode;
		firePropertyChange("playMode", oldValue, playMode);
	}
	public static class AspectRatioWrapper extends JComponent
	{
		private final Component content;
		private final Playable playable;
		private ScalingPolicy scalingPolicy;
		public AspectRatioWrapper(Component content, Playable playable, ScalingPolicy scalingPolicy)
		{
			this.content = content;
			this.playable = playable;
			this.scalingPolicy = scalingPolicy;
			add(content);
			setLayout(null);
		}
		@Override
		public Dimension getPreferredSize() {
			return content.getPreferredSize();
		}
		@Override
		public void doLayout() {
			Dimension d = getSize();
			switch (scalingPolicy)
			{
			case ExactFit:
				d = getSize();
				break;
			case MaintainAspectRatio:
				d = playable.getAspectRespectingDimension(d);
				break;
			case ScaleDownMaintainAspectRatio:
				Dimension pref = content.getPreferredSize();
				if (d.width < pref.width || d.height < pref.height)
					d = playable.getAspectRespectingDimension(d);
				else
					d = pref;
				break;
			case None:
				d = content.getPreferredSize();
				break;
			}
			content.setSize(d);
			content.setLocation((getWidth() - d.width)/2, (getHeight() - d.height)	/2);
		}
		public void setScalingPolicy(ScalingPolicy scalingPolicy) {
			this.scalingPolicy = scalingPolicy;
			repaint();
			revalidate();
		}
	}
}
