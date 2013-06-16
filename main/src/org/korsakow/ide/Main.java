package org.korsakow.ide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.Timer;

import org.apache.log4j.Logger;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.korsakow.ide.controller.ApplicationAdapter;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.ProjectExplorer;
import org.korsakow.ide.ui.SplashPage;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.services.encoders.image.ImageEncoderFactory;
import org.korsakow.services.encoders.image.JavaImageIOImageEncoder;
import org.korsakow.services.encoders.sound.SoundEncoderFactory;
import org.korsakow.services.encoders.sound.lame.plaf.LameEncoderOSX;
import org.korsakow.services.encoders.sound.lame.plaf.LameEncoderWin32;
import org.korsakow.services.encoders.video.VideoEncoderFactory;
import org.korsakow.services.encoders.video.ffmpeg.plaf.FFMpegEncoderOSX;
import org.korsakow.services.encoders.video.ffmpeg.plaf.FFMpegEncoderWin32;
import org.korsakow.services.updater.Updater;

import quicktime.QTException;
import quicktime.QTSession;

//import com.apple.eawt.ApplicationEvent;
//import com.apple.eawt.ApplicationListener;

public class Main {
	/**
	 * Introduced because ApplicationShutdownListener is added as a weak reference
	 */
	private static Main main;
	public static void main(String[] args) throws Exception {
		main = new Main(args);
	}

	
	private class ApplicationShutdownListener extends ApplicationAdapter implements Runnable
	{
		@Override
		public void onApplicationShutdown() {
			// do later so that other shutdown listeners can do their stuff
			UIUtil.runUITaskLater(this);
		}
		public void run() {
			try {
				shutdown();
			} catch (Exception e) {
				Logger.getLogger(Main.class).error("", e);
			}
		}
	}
	
	private final ApplicationShutdownListener applicationShutdownListener = new ApplicationShutdownListener();

	public Main(String[] args) throws Exception {
		if (Platform.getOS() == Platform.OS.UNKNOWN ||
			Platform.getArch() == Platform.Arch.UNKNOWN ||
			Platform.getArch() == Platform.Arch.POWERPC)
		{
			JOptionPane.showMessageDialog(null, 
					LanguageBundle.getString("general.errors.unsupportedplatform.message", 
							Platform.getOS().getCanonicalName(), 
							Platform.getArch().getCanonicalName()), 
					LanguageBundle.getString("general.errors.unsupportedplatform.title"), 
					JOptionPane.ERROR_MESSAGE);
		}
		
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");

		// These preferences are best set on init of the application
		if ( Platform.getOS() == Platform.OS.MAC ) {
//			System.setProperty("apple.laf.useScreenMenuBar", "true"); // removed per #840
			System.setProperty("com.apple.mrj.application.apple.menu.about.name",
					"Korsakow");
		}
		
		final Exception[] remoteException = new SQLException[1];
		Thread.currentThread().setUncaughtExceptionHandler(
				new UncaughtExceptionHandler() {
					public void uncaughtException(Thread thread,
							Throwable exception) {
						Logger.getLogger(Application.class)
								.error("", exception);
					}
				});

		setup();
		
		Logger.getLogger(Main.class).info("CommandLine Arguments");
		for (String arg : args) {
			Logger.getLogger(Main.class).info("\t" + arg + "\n");
		}
		
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() {
				UIUtil.setUpLAF();
			}
		});

		final JWindow splashDialog = new JWindow();

		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() {
				
				splashDialog.setAlwaysOnTop(true);
				SplashPage page = new SplashPage();
				page.setUUIDVisible(false);
				splashDialog.add(page);
				splashDialog.pack();

				UIUtil.centerOnScreen(splashDialog);
			}
		});
		
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow(){
			public void run() throws Throwable {
				// doing this separately avoids an issue where the splash shows up unpainted for a second
				splashDialog.setVisible(true);
				splashDialog.toFront();
			}
		});

		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Throwable {
				long beforeTime = System.currentTimeMillis();
				
				Application.initializeInstance();
				final Application app = Application.getInstance();
				app.addApplicationListener(applicationShutdownListener);
				
				final ProjectExplorer explorer = app.showProjectExplorer();
				UIUtil.centerOnScreen(explorer);
				
				long afterTime = System.currentTimeMillis();
				long delta = afterTime - beforeTime;
				long minTime = 2000;
				
				long timerTime = Math.max(1, minTime-delta);
				Timer timer = new Timer((int)timerTime, new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						splashDialog.dispose();
						explorer.setVisible(true);
						app.getProjectExplorerController().loadDefaultProject();
						
						Updater.checkAsynch();
					}
				});
				timer.setRepeats(false);
				timer.start();
			}
		});
	}

	private void shutdownLibs() throws Exception {
		Logger.getLogger(Main.class).info("shutdown libs");
		// QT is pretty quirky, and for example might keep processes hanging
		// around if the shutdown isnt complete
		// so we try our best to make sure each part of the shutdown is
		// attempted
		try {
			QTSession.exitMovies();
		} catch (Exception e) {
			Logger.getLogger(Main.class).error("", e);
		}
		try {
			QTSession.close();
		} catch (Exception e) {
			Logger.getLogger(Main.class).error("", e);
		}
	}

	private void shutdown() throws Exception {
		Logger.getLogger(Main.class).info("shutdown begin");
		try {
			shutdownUoW();
		} catch (Exception e) {
			Logger.getLogger(Main.class).error("", e);
		}
		try {
			DataRegistry.getConnection().commit();
		} catch (Exception e) {
			Logger.getLogger(Main.class).error("", e);
		}
		;
		try {
			shutdownLibs();
		} catch (Exception e) {
			Logger.getLogger(Main.class).error("", e);
		}
		Logger.getLogger(Main.class).info(
				"shutdown complete (this should be the last item logged)");
		System.exit(0);
	}

	private void shutdownUoW() throws Exception {
		final Exception[] remoteException = new SQLException[1];
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					DbRegistry.getDbConnection().rollback();
				} catch (SQLException e) {
					Logger.getLogger(Application.class).error("", e);
					remoteException[0] = e;
					return;
				}

			}
		};
		UIUtil.runUITaskNow(runnable);
		if (remoteException[0] != null)
			throw remoteException[0];
	}

	private void setup() throws Exception {
		// setup the UUID as early as possible as it is used in error reporting and logging.
		Application.getUUID(); // side effect causes the UUID to be persisted.
		setupLogging();
		setupPlatform();
		UIUtil.runUITaskNow(new Runnable() { public void run() { try {
			setupLibs();
		} catch (QTException e) {
			throw new RuntimeException(e);
		} } }); 
	}

	public static void setupLogging() {
		System.setProperty("org.korsakow.log.filename", Application.getLogfilename());
		Logger.getLogger(Main.class).info(Build.getAboutString());
		Logger.getLogger(Main.class).info(String.format("Java: JVM %s, JRE %s, ", System.getProperty("java.version"), System.getProperty("java.class.version")));
		Logger.getLogger(Main.class).info(String.format("Platform: %s %s\n\tDetected as: Operating System: %s, Architechture: %s", Platform.getArchString(), Platform.getOSString(), Platform.getOS().getCanonicalName(), Platform.getArch().getCanonicalName()));
		Logger.getLogger(Main.class).info(String.format("UUID: %s", Application.getUUID()));
	}

	private static void setupPlatform() {
		setupPlatformEncoders();
	}
	public static void setupPlatformEncoders() {
		switch (Platform.getOS()) {
		case MAC:
			SoundEncoderFactory.getDefaultFactory().addEncoder(
					new LameEncoderOSX.LameEncoderOSXDescription());
			VideoEncoderFactory.addEncoder(
					new FFMpegEncoderOSX.FFMpegEncoderOSXDescription());
			break;
		case WIN:
			// FontEncoderFactory.getDefaultFactory().addEncoder(new
			// SwfMillEncoderOSX.SwfMillEncoderWin32XDescription());
			SoundEncoderFactory.getDefaultFactory().addEncoder(
					new LameEncoderWin32.LameEncoderWin32Description());
			VideoEncoderFactory.addEncoder(
					new FFMpegEncoderWin32.FFMpegEncoderWin32Description());
			break;
		case NIX:
		default:
			System.out.println("Platform specific features not yet supported");
			break;
		}
		ImageEncoderFactory.addEncoder(new JavaImageIOImageEncoder.JavaImageIOEncoderDescription());
	}

	private static void setupLibs() throws QTException {
		try {
			QTSession.open();
		} catch (UnsatisfiedLinkError e) {
			JOptionPane.showMessageDialog(null, 
					"Korsakow requires quicktime to be installed in order to run.", 
					"Quicktime was not found", 
					JOptionPane.ERROR_MESSAGE);
			throw e;
		} catch (NoClassDefFoundError e) {
			JOptionPane.showMessageDialog(null, 
					"Korsakow requires quicktime to be installed in order to run.", 
					"Quicktime was not found", 
					JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}
}
