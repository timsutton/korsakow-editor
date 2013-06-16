package org.korsakow.ide.resources.media;

import java.awt.Component;

import javax.swing.JComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.korsakow.ide.exception.MediaRuntimeException;

import quicktime.QTException;
import quicktime.app.view.MoviePlayer;
import quicktime.app.view.QTFactory;
import quicktime.app.view.QTJComponent;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;

@SuppressWarnings("deprecation")
public class QTVideo extends AbstractPlayableVideo
{
	private final Log log = LogFactory.getLog(getClass());
	
	protected quicktime.std.movies.Movie innerMovie;
	private MoviePlayer innerMoviePlayer;
	long timeScale = 1000;

	private QTJComponent innerQTC = null;
	
	public QTVideo(String url) throws MediaRuntimeException {
		try {
			innerMovie = quicktime.std.movies.Movie.fromFile(
					OpenMovieFile.asRead(
					new QTFile(url)));
			innerMoviePlayer = new MoviePlayer(innerMovie);
			innerMovie.setActive (true);
			timeScale = innerMovie.getTRTime().getScale();
		} catch (QTException e) {
			log.error("URL="+e);
			throw new MediaRuntimeException(e);
		}
	}
	
	public Component getComponent() {
		try {
			if(innerQTC == null) {
				innerQTC = QTFactory.makeQTJComponent(innerMoviePlayer);
			}
			return (JComponent)innerQTC;
		} catch (QTException e) {
			throw new MediaRuntimeException(e);
		}
	}

	public long getDuration() {
		try {
			return (long)(innerMovie.getDuration()/(double)timeScale*1000);
		} catch (QTException e) {
			throw new MediaRuntimeException(e);
		}
	}

	public long getTime() {
		try {

			return (long)(innerMovie.getTime()/(double)timeScale*1000);
		} catch (QTException e) {
			throw new MediaRuntimeException(e);
		}
	}

	public void setTime(long time) {
		try {
			innerMoviePlayer.setTime((int)(time/1000.0*timeScale));
		} catch (QTException e) {
			throw new MediaRuntimeException(e);
		}
	}
	
	public void dispose() {
		try {
			if (innerQTC != null) {
				// we need to do this because QT may otherwise leak, depending on what happens in Swing; this essentially disconnects QT from Swing
				innerQTC.setMoviePlayer(null);
				innerQTC = null;
			}
			
			if(innerMovie != null) {
				innerMovie.setRate(0);
				innerMovie.stop();
				innerMovie.disposeQTObject();
			}
			
		} catch (QTException e) {
        	Logger.getLogger(QTVideo.class).debug(e);
			throw new MediaRuntimeException(e);
		}
		innerQTC = null;
		innerMovie = null;
	}

	public void start() {
		try {
			innerMovie.start();
		} catch (QTException e) {
			throw new MediaRuntimeException(e);
		}
	}

	public void stop() {
		try {
			innerMovie.stop();
		} catch (QTException e) {
			throw new MediaRuntimeException(e);
		}
	}

	public boolean isPlaying() {
		try {
			return innerMovie.getRate() != 0;
		} catch (QTException e) {
			throw new MediaRuntimeException(e);
		}
	}

	public void redraw() throws QTException {
		innerMoviePlayer.redraw(null);
	}

	public void setVolume(float volume) {
		try {
			innerMoviePlayer.setVolume(volume);
		} catch (QTException e) {
			throw new MediaRuntimeException(e);
		}
	}
	public float getVolume() {
		try {
			return innerMoviePlayer.getVolume();
		} catch (QTException e) {
			throw new MediaRuntimeException(e);
		}
	}
}
