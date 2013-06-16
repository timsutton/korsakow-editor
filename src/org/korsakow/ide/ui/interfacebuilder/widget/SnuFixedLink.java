package org.korsakow.ide.ui.interfacebuilder.widget;


import java.awt.BorderLayout;
import java.awt.Component;

import org.apache.commons.logging.LogFactory;
import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.MediaInputMapper;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.media.MediaFactory;
import org.korsakow.ide.resources.media.Playable;
import org.korsakow.ide.resources.property.LongProperty;
import org.korsakow.ide.resources.widget.ScalingPolicy;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.WidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.editors.SnuFixedLinkWidgetEditor;
import org.korsakow.ide.ui.components.ResourceIcon;
import org.korsakow.ide.ui.interfacebuilder.widget.MediaArea.AspectRatioWrapper;

public class SnuFixedLink extends AbstractLink
{
	protected static class SnuFixedLinkWidgetComponent extends AbstractLinkWidgetComponent
	{
		protected ResourceIcon icon;
		private Component mediaComponent;
		private AspectRatioWrapper wrapper;
		private Playable playable;
		public SnuFixedLinkWidgetComponent(WidgetModel owner)
		{
			super(owner);
		}
		@Override
		protected void initUI()
		{
			super.initUI();
			setLayout(new BorderLayout());
			add(label, BorderLayout.NORTH);
			label.setText(LanguageBundle.getString("widget.snufixedlink.label"));
			
			icon = new ResourceIcon();
//			add(icon);
		}
		public void setSnu(Long snuId)
		{
			if (snuId != null) {
				ISnu snu;
				try {
					snu = SnuInputMapper.map( snuId );
				} catch (MapperException e) {
					Application.getInstance().showUnhandledErrorDialog( e );
					icon.clear();
					setMedia(null);
					return;
				}
				icon.setResource(ResourceType.forId(snu.getType()).getIcon(), snu.getName());
				try {
					setMedia(MediaInputMapper.map(snu.getMainMedia().getId()));
				} catch (MapperException e) {
					LogFactory.getLog(getClass()).error("", e);
					icon.clear();
					setMedia(null);
				}
			} else {
				icon.clear();
				setMedia(null);
			}
		}
		private void setMedia(IMedia media)
		{
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
				wrapper = new MediaArea.AspectRatioWrapper(mediaComponent, playable, getWidget().getScalingPolicy());
				add(wrapper, BorderLayout.CENTER);
			}
			revalidate();
			repaint();
		}
		@Override
		public SnuFixedLink getWidget() {
			return (SnuFixedLink)super.getWidget();
		}
		public AspectRatioWrapper getWrapper() {
			return wrapper;
		}
	}
	@Override
	protected WidgetComponent createComponent()
	{
		return new SnuFixedLinkWidgetComponent(this);
	}
	@Override
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new SnuFixedLinkWidgetEditor(this);
	}
	private Long snuId;
	public SnuFixedLink()
	{
		super(WidgetType.SnuFixedLink);
		scalingPolicy = ScalingPolicy.MaintainAspectRatio;

		addProperty(new LongProperty("snuId") {
			@Override
			public Long getValue() { return snuId; }
			@Override
			public void setValue(Long value) { setSnu(value); }
		});
	}
	@Override
	public SnuFixedLinkWidgetComponent getComponent() {
		return (SnuFixedLinkWidgetComponent)super.getComponent();
	}
	public void setSnu(Long snu)
	{
		Long oldSnu = snuId;
		snuId = snu;
		getComponent().setSnu(snu);
		firePropertyChange("snu", oldSnu, snuId);
	}
	public Long getSnu()
	{
		return snuId;
	}
	@Override
	public void setScalingPolicy(ScalingPolicy scalingPolicy)
	{
		if (getComponent().getWrapper() != null)
			getComponent().getWrapper().setScalingPolicy(scalingPolicy);
		super.setScalingPolicy(scalingPolicy);
	}
}
