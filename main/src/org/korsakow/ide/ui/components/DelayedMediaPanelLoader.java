/**
 * 
 */
package org.korsakow.ide.ui.components;

import java.io.FileNotFoundException;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.mapper.input.ImageInputMapper;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.resources.media.MediaFactory;
import org.korsakow.ide.resources.media.Playable;
import org.korsakow.ide.resources.media.PlayableImage;
import org.korsakow.ide.resources.media.UnsupportedMedia;
import org.korsakow.ide.util.UIUtil;

public class DelayedMediaPanelLoader
{
	public static void load(final JComponent container, final NewMediaPanel panel, IMedia media)
	{
		panel.setEnabled(false);
		
		PlayableConsumer consumer = new PlayableConsumer() {
			public void consume(final Playable playable) {
				UIUtil.runUITaskLater(new Runnable() {
					public void run() {
						panel.setPlayable(playable);
						panel.setEnabled(true);
						container.revalidate();
						container.repaint();
					}
				});
			}
		};
		
		if (media != null) {
			Long duration = null;
			if ( ResourceType.IMAGE.isInstance(media) ) {
				try {
					duration = ImageInputMapper.map( media.getId() ).getDuration();
				} catch (MapperException e) {
					Logger.getLogger(DelayedMediaPanelLoader.class).error("", e);
					duration = 0L;
				}
			}
			
			try {
				String filename = media.getAbsoluteFilename();
				Thread thread = new LoaderThread(consumer, filename, duration);
				thread.setPriority(Thread.MIN_PRIORITY);
				thread.start();
			} catch (FileNotFoundException e) {
				consumer.consume(new UnsupportedMedia(media.getFilename()));
			}
		} else
			consumer.consume(null);
		
	}
	private static interface PlayableConsumer
	{
		void consume(Playable playable);
	}
	private static class LoaderThread extends Thread
	{
		private final PlayableConsumer consumer;
		private final String filename;
		private final Long duration;
		
		public LoaderThread(PlayableConsumer consumer, String filename, Long duration) {
			this.consumer = consumer;
			this.filename = filename;
			this.duration = duration;
		}

		@Override
		public void run() {
			// loading the media can take a while
			final Playable playable = MediaFactory.getMediaNoThrow(filename);
			if (playable instanceof PlayableImage && duration != null)
				((PlayableImage)playable).setDuration(duration);
			consumer.consume(playable);
		}
	}
}