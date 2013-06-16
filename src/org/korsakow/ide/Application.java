package org.korsakow.ide;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.undo.UndoManager;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.service.Registry;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.ResourceInputMapper;
import org.korsakow.eawt.IApplication;
import org.korsakow.ide.controller.ApplicationListener;
import org.korsakow.ide.controller.ProjectEditAction;
import org.korsakow.ide.controller.ResourceEditAction;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ProjectExplorer;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.SplashPage;
import org.korsakow.ide.ui.components.keywordpool.KeywordPool;
import org.korsakow.ide.ui.components.linkpool.LinkPool;
import org.korsakow.ide.ui.components.snupool.SnuPool;
import org.korsakow.ide.ui.controller.CommonTaskScheduler;
import org.korsakow.ide.ui.controller.ProjectExplorerController;
import org.korsakow.ide.ui.controller.action.ExitAction;
import org.korsakow.ide.ui.controller.action.interf.EditInterfaceAction;
import org.korsakow.ide.ui.controller.action.media.EditImageAction;
import org.korsakow.ide.ui.controller.action.media.EditSoundAction;
import org.korsakow.ide.ui.controller.action.media.EditTextAction;
import org.korsakow.ide.ui.controller.action.media.EditVideoAction;
import org.korsakow.ide.ui.controller.action.snu.EditSnuAction;
import org.korsakow.ide.ui.dialogs.AlertDialog;
import org.korsakow.ide.ui.dialogs.EditingConflictDialog;
import org.korsakow.ide.ui.dialogs.ErrorDialog;
import org.korsakow.ide.ui.dialogs.MissingMediaDialog;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.util.EventListenerWeakList;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.PreferencesManager;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.ide.util.Util;
import org.korsakow.services.ErrorMailer;

public class Application
{
	private static Application instance;
	
	public static synchronized Application getInstance()
	{
		return instance;
	}
	/**
	 * Should call this somewhere at the beginning to handle the possible exception the first time its created.
	 * @return
	 * @throws Exception
	 */
	public static synchronized Application initializeInstance() throws Exception
	{
		return initializeInstance(new Application());
	}
	/**
	 * Used in testing.
	 * @param application
	 * @return
	 * @throws Exception
	 */
	public static synchronized Application initializeInstance(Application application) throws Exception
	{
		if (instance != null)
			throw new IllegalStateException("already initialized. should call this only once at startup");
		instance = application;
		return instance;
	}
	public static synchronized void destroyInstance()
	{
		if (instance != null) {
			instance.destroy();
		}
		instance = null;
	}
	

	private final Hashtable<Long, ResourceEditor> openEditors = new Hashtable<Long, ResourceEditor>();
	private final Hashtable<ResourceEditor, Long> openEditors2 = new Hashtable<ResourceEditor, Long>();
	private final Hashtable<ResourceType, ResourceEditAction> resourceEditActions = new Hashtable<ResourceType, ResourceEditAction>();
	private ProjectExplorer projectExplorer;
	private ProjectExplorerController projectExplorerController;
	private SnuPool snuPool;
	private KeywordPool keywordPool;
	private LinkPool linkPool;
	private ResourceTreeTable possiblePool;
	private final List<ResourceEditor> mostRecentFocusedEditors = new ArrayList<ResourceEditor>();
	private final CommonTaskScheduler commonTaskScheduler = new CommonTaskScheduler();
	/**
	 * Application is global. This means any listeners on it would otherwise have application-lifetime!
	 * Hence the listeners are weak. IMHO AWT fucked up not having listeners be always weak, in any case, its
	 * much easier to forcibly remember a reference than to forget it.
	 */
	private final EventListenerWeakList eventListeners = new EventListenerWeakList();
	private File saveFile = null;
	private long saveVersion = -1;
	
	private final IApplication platformApplication;
	
	private final UndoManager undoManager;
	
	private final WindowListener openEditorsListener = new WindowAdapter() {
	
		@Override
		public void windowActivated(WindowEvent event) {
			ResourceEditor editor = (ResourceEditor)event.getWindow();
			// dont double add, but also bump this one to the end
			if (mostRecentFocusedEditors.contains(editor))
				mostRecentFocusedEditors.remove(editor);
			mostRecentFocusedEditors.add(editor);
			Application.this.notifyApplicationWindowActivated(event);
		}
		@Override
		public void windowClosed(WindowEvent event) {
			ResourceEditor editor = (ResourceEditor)event.getSource();
			if (openEditors2.containsKey(editor))
				openEditors.remove(openEditors2.get(editor));
			openEditors2.remove(editor);
			mostRecentFocusedEditors.remove(editor);
			editor.removeWindowListener(openEditorsListener); // not neceesary since they dispose on close?
			System.gc(); // might help? especially with media resources?
			Application.this.notifyApplicationWindowClosed(event);
		}
	};
	
	protected Application() throws Exception
	{      
		UIUtil.runUITaskNow(new Runnable() {
			public void run() {
				Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
					public void uncaughtException(Thread thread, Throwable exception) {
						Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), LanguageBundle.getString("general.errors.uncaughtexception.message"), exception);
					}
				});
			}
		});
		
		String applicationClassName = "";
		switch (Platform.getOS())
		{
		case MAC:
			applicationClassName = Registry.getProperty("application_classname_osx");
			break;
		case WIN:
			applicationClassName = Registry.getProperty("application_classname_windows");
			break;
		case NIX:
			applicationClassName = Registry.getProperty("application_classname_linux");
			break;
		}
		platformApplication = (IApplication)Class.forName(applicationClassName).newInstance();
		
		UIUtil.setUpLAF();

		resourceEditActions.put(ResourceType.SNU, new EditSnuAction());
		resourceEditActions.put(ResourceType.VIDEO, new EditVideoAction());
		resourceEditActions.put(ResourceType.IMAGE, new EditImageAction());
		resourceEditActions.put(ResourceType.TEXT, new EditTextAction());
		resourceEditActions.put(ResourceType.SOUND, new EditSoundAction());
		resourceEditActions.put(ResourceType.INTERFACE, new EditInterfaceAction());
		resourceEditActions.put(ResourceType.PROJECT, new ProjectEditAction());
		
		undoManager = new UndoManager();
		undoManager.setLimit(999);
		
		UIManager.put("AbstractUndoableEdit.undoText", "");
		UIManager.put("AbstractUndoableEdit.redoText", "");
	}
	/**
	 * May not return. // TODO: why? then why the return value?
	 * @return whether the application has / is going to shutdown
	 */
	public boolean shutdown()
	{
		try {
			if (!ExitAction.checkForChangesAndPrompt())
				return false;
		} catch (MapperException e) {
			Logger.getLogger(getClass()).error("", e);
			return false;
		}
		if (!notifyWillShutdown())
			return false;
		notifyShutdown();
		return true;
	}
	/**
	 * TOOD: refactor this, perhaps into the Platform class
	 * @return
	 */
	public static String getUserHome()
	{
		return System.getProperty("user.home");
	}
	/**
	 * TODO: refactor this, perhaps into the Platform class, or maybe into Application
	 * @return
	 */
	public static String getKorsakowHome()
	{
		return getUserHome() + File.separatorChar + ".korsakow";
	}
	
	public static String getLogfilename()
	{
		return getKorsakowHome() + File.separatorChar + "korsakow.log";
	}
	
	
	public static String getUUID()
	{
		String uuid = PreferencesManager.getVersionIndependentPreferences().get("uuid", null);
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			PreferencesManager.getVersionIndependentPreferences().put("uuid", uuid);
		}
		return uuid;
	}
	
	public void notifyProjectLoaded(IProject activeProject)
	{
		for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
			listener.onProjectLoaded(activeProject);
	}
	public long getSaveVersion()
	{
		return saveVersion;
	}
	public void setSaveFile(File file, long version)
	{
		saveFile = file;
		saveVersion = version;
	}
	public File getSaveFile()
	{
		return saveFile;
	}
	
	public void enqueueCommonTask(Runnable runnable)
	{
		commonTaskScheduler.enqueue(runnable);
	}
	public void startCommonTasks()
	{
		commonTaskScheduler.start();
	}
	public void stopCommonTasks()
	{
		commonTaskScheduler.stop();
	}
	
	public IApplication getPlatformApplication()
	{
		return platformApplication;
	}
	
	public void clearRegistry()
	{
		for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
			listener.onResourcesCleared();
	}
	public void registerDeleted(final IResource resource)
	{
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
					listener.onResourceDeleted(resource);
			}
		});
	}
	
	private class NotifyKeywordsChangedTask implements Runnable
	{
		public void run()
		{
			for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
				listener.onKeywordsChanged();
		}
	}
	public void notifyApplicationWindowActivated(WindowEvent event)
	{
		for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
			listener.onWindowActivated(event);
	}
	public void notifyApplicationWindowClosed(WindowEvent event)
	{
		for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
			listener.onWindowClosed(event);
	}
	public void notifyKeywordsChanged()
	{
		commonTaskScheduler.enqueue(new NotifyKeywordsChangedTask());
	}
	public void notifyResourceAdded(IResource resource)
	{
		for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
			listener.onResourceAdded(resource);
	}
	public void notifyResourceModified(IResource resource)
	{
		for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
			listener.onResourceModified(resource);
	}
	public void notifyResourceDeleted(IResource resource)
	{
		for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
			listener.onResourceDeleted(resource);
	}
	public void notifyShutdown()
	{
		for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
			listener.onApplicationShutdown();
	}
	public boolean notifyWillShutdown()
	{
		boolean willShutdown = true;
		for (ApplicationListener listener : eventListeners.getListeners(ApplicationListener.class))
			willShutdown = willShutdown && listener.onApplicationWillShutdown();
		return willShutdown;
	}
	
	public ResourceEditor createResourceEditor(IResource resource)
	{
		ResourceEditor editor = new ResourceEditor();
		editor.setResizable(false);
		if (resource != null) {
			openEditors.put(resource.getId(), editor);
			openEditors2.put(editor, resource.getId());
		}
		editor.addWindowListener(openEditorsListener);
//		Dimension size = editor.getPreferredSize();
//		size.width = Math.min(size.width, 500);
//		size.height = Math.min(size.height, 500);
//		editor.setSize(size);
		return editor;
	}
	public ResourceEditor editSound(long id) throws Exception
	{
		return edit(ResourceType.SOUND, id);
	}
	public ResourceEditor editVideo(long id) throws Exception
	{
		return edit(ResourceType.VIDEO, id);
	}
	public ResourceEditor editNew(ResourceType type) throws Exception
	{
		return editNew(type, null);
	}
	public ResourceEditor editNew(ResourceType type, Long id) throws Exception
	{
		if (!resourceEditActions.containsKey(type)) {
			showUnhandledErrorDialog("No editor", "There is no editor for resources of type: " + type.getDisplayString()); // actually should never happen
			return null;
		}
		ResourceEditor editor = resourceEditActions.get(type).run(null);
		if (id != null)
			openEditors2.put(editor, id);
		return editor;
	}
	public ResourceEditor edit(ResourceType type, long id) throws Exception
	{
		if (!resourceEditActions.containsKey(type)) {
			showUnhandledErrorDialog("No editor", "There is no editor for resources of type: " + type.getDisplayString()); // actually should never happen
			return null;
		}
		ResourceEditor editor = resourceEditActions.get(type).run(ResourceInputMapper.map(id));
		return editor;
	}
	/**
	 * 
	 * @return may be null
	 */
	public ResourceEditor getMostRecentFocusedEditor()
	{
		if (mostRecentFocusedEditors.isEmpty())
			return null;
		return mostRecentFocusedEditors.get(mostRecentFocusedEditors.size()-1);
	}
	public ResourceEditor getOpenEditor(IResource resource)
	{
		if (resource != null && openEditors.containsKey(resource.getId()))
			return openEditors.get(resource.getId());
		return null;
	}
	/**
	 * Returns the currently open editors in order of most recently focused
	 * @return
	 */
	public Collection<ResourceEditor> getOpenEditors()
	{
		return new ArrayList<ResourceEditor>(mostRecentFocusedEditors); // cast OK since the source is a list
	}
	/**
	 * 
	 * @param editor
	 * @return TODO: could this ever return null?
	 * @throws MapperException 
	 */
	public IResource getResourceForEditor(ResourceEditor editor) throws MapperException
	{
		if (openEditors2.containsKey(editor))
			return ResourceInputMapper.map(openEditors2.get(editor));
		else
			return null;
	}
	public void setSnuPoolDialog(final JFrame dialog, final SnuPool pool)
	{
		snuPool = pool;
		pool.putClientProperty("dialog", dialog);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent event) {
				pool.putClientProperty("dialog", null);
				if (pool == snuPool) {
					snuPool = null;
				}
			}
		});
	}
	public JFrame getSnuPoolDialog()
	{
		if (snuPool == null)
			return null;
		return (JFrame)snuPool.getClientProperty("dialog");
	}
	public SnuPool getSnuPool()
	{
		return snuPool;
	}
	public void setLinkPoolDialog(final JFrame dialog, final LinkPool pool)
	{
		linkPool = pool;
		linkPool.putClientProperty("dialog", dialog);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent event) {
				pool.putClientProperty("dialog", null);
				if (pool == linkPool) {
					linkPool = null;
				}
			}
		});
	}
	public JFrame getLinkPoolDialog()
	{
		if (linkPool == null)
			return null;
		return (JFrame)linkPool.getClientProperty("dialog");
	}
	public LinkPool getLinkPool()
	{
		return linkPool;
	}
	public void setKeywordPoolDialog(final JFrame dialog, final KeywordPool pool)
	{
		keywordPool = pool;
		keywordPool.putClientProperty("dialog", dialog);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent event) {
				pool.putClientProperty("dialog", null);
				if (pool == keywordPool) {
					keywordPool = null;
				}
			}
		});
	}
	public JFrame getKeywordPoolDialog()
	{
		if (keywordPool == null)
			return null;
		return (JFrame)keywordPool.getClientProperty("dialog");
	}
	public KeywordPool getKeywordPool()
	{
		return keywordPool;
	}
	public void setPossiblePoolDialog(final JFrame dialog, final ResourceTreeTable pool)
	{
		possiblePool = pool;
		possiblePool.putClientProperty("dialog", dialog);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent event) {
				pool.putClientProperty("dialog", null);
				if (pool == possiblePool) {
					possiblePool = null;
				}
			}
		});
	}
	public JFrame getPossiblePoolDialog()
	{
		if (possiblePool == null)
			return null;
		return (JFrame)possiblePool.getClientProperty("dialog");
	}
	public ResourceTreeTable getPossiblePool()
	{
		return possiblePool;
	}
	public ProjectExplorer getProjectExplorer()
	{
		if (projectExplorer == null) {
			projectExplorer = new ProjectExplorer();
			projectExplorer.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent event) {
					shutdown();
				}
			});
			projectExplorerController = new ProjectExplorerController(projectExplorer);
		}
		return projectExplorer;
	}
	public ProjectExplorerController getProjectExplorerController()
	{
		return projectExplorerController;
	}
	public ProjectExplorer showProjectExplorer()
	{
		ProjectExplorer resourceExplorer = getProjectExplorer();
		resourceExplorer.pack();
		Dimension d = new Dimension(925, 600);
		resourceExplorer.setSize(d);
		UIUtil.constrainSizeToScreen(resourceExplorer);
		return resourceExplorer;
	}
	public EditingConflictDialog showEditingConflictDialog()
	{
		return showEditingConflictDialog(getProjectExplorer());
	}
	public EditingConflictDialog showEditingConflictDialog(JFrame parent)
	{
		EditingConflictDialog dialog = new EditingConflictDialog(parent);
		dialog.setTitle("Editing Conflict");
		dialog.setModal(true);
		dialog.setSize(new Dimension(320, 240));
		UIUtil.centerOnFrame(dialog, parent);
		return dialog;
	}
	public MissingMediaDialog showMissingMediaDialog()
	{
		return showMissingMediaDialog(getProjectExplorer());
	}
	public MissingMediaDialog showMissingMediaDialog(JFrame parent)
	{
		MissingMediaDialog dialog = new MissingMediaDialog(parent);
		dialog.setTitle("Missing Media");
		dialog.setModal(true);
		dialog.setSize(new Dimension(320, 240));
		UIUtil.centerOnFrame(dialog, parent);
		return dialog;
	}
	public void showUnhandledErrorDialog(Throwable details)
	{
		showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), details.getClass().getCanonicalName(), details);
	}
	public void showUnhandledErrorDialog(String title, Throwable details)
	{
		showUnhandledErrorDialog(title, details.getClass().getCanonicalName(), details);
	}
	public void showUnhandledErrorDialog(String title, String message, Throwable details)
	{
		showUnhandledErrorDialog(title, message, Util.getStackTraceString(details), details);
	}
	
	public void showUnhandledErrorDialog(String title, String message)
	{
		showUnhandledErrorDialog(title, message, "", null);
	}
	/**
	 * An error has occurred which we did not expect or did not know how to handle.
	 * The user is alerted and given the option to send us feedback.
	 * @param title
	 * @param message
	 * @param details
	 */
	public void showUnhandledErrorDialog(String title, String message, String details, Throwable cause)
	{
		Logger.getLogger(Application.class).error(message, cause);
		//This is where I should insert the request as to whether or not to mail an error
		try {
			String lastExceptionUUID = PreferencesManager.getPreferences(Application.class).get("errorMailer.last", "");
			String currentExceptionUUID = Util.getStackTraceUUID(cause);
			if (!lastExceptionUUID.equals(currentExceptionUUID)) {
				boolean sent = ErrorMailer.sendError(title, message, cause);
				if (sent) {
					PreferencesManager.getPreferences(Application.class).put("errorMailer.last", currentExceptionUUID);
					PreferencesManager.getPreferences(Application.class).flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			details += "\n\nAnd an Exception while trying to mail the error.\n"+Util.getStackTraceString(e);
		}
		
		ErrorDialog dialog = details.length()>0?new ErrorDialog(message, details):new ErrorDialog(message);
		JOptionPane.showMessageDialog(getProjectExplorer(), dialog, title, JOptionPane.ERROR_MESSAGE, UIResourceManager.getIcon(UIResourceManager.ICON_ERROR));
	}
	
	/**
	 * The error has been handled but we still alert the user. No mailback though.
	 * @param title
	 * @param message
	 * @param details
	 */
	public void showHandledErrorDialog(String title, String message, String details)
	{
		ErrorDialog dialog = details.length()>0?new ErrorDialog(message, details):new ErrorDialog(message);
		JOptionPane.showMessageDialog(getProjectExplorer(), dialog, title, JOptionPane.ERROR_MESSAGE, UIResourceManager.getIcon(UIResourceManager.ICON_ERROR));
	}
	public void showHandledErrorDialog(String title, String message)
	{
		showHandledErrorDialog(title, message, "");
	}
	public void showAlertDialog(Component parent, String title, JComponent message)
	{
		AlertDialog dialog = new AlertDialog(message);
		JOptionPane.showMessageDialog(parent, dialog, title, JOptionPane.INFORMATION_MESSAGE);
	}
	public void showAlertDialog(Component parent, String title, String message) {
		showAlertDialog(parent, title, AlertDialog.createMessage(message));
	}
	public void showAlertDialog(String title, String message)
	{
		showAlertDialog(getProjectExplorer(), title, AlertDialog.createMessage(message));
	}
	public void showAlertDialog(String title, JComponent message)
	{
		showAlertDialog(getProjectExplorer(), title, message);
	}
	public void showOneTimeAlertDialog(String dialogId, Component parent, String title, String message) {
		showOneTimeAlertDialog(dialogId, parent, title, AlertDialog.createMessage(message));
	}
	public void showOneTimeAlertDialog(String dialogId, Component parent, String title, JComponent message) {
		if (!PreferencesManager.getPreferences(AlertDialog.class).node(dialogId).getBoolean("show", true))
			return;
		AlertDialog dialog = new AlertDialog(message);
		dialog.setDontShowAgainVisible(true);
		dialog.setDontShowAgain(false);
		JOptionPane.showMessageDialog(parent, dialog, title, JOptionPane.INFORMATION_MESSAGE);
		if (dialog.getDontShowAgain()) {
			PreferencesManager.getPreferences(AlertDialog.class).node(dialogId).putBoolean("show", false);
		}
	}
	public boolean showOKCancelDialog(String title, Object message)
	{
		return showOKCancelDialog(getProjectExplorer(), title, message);
	}
	public boolean showOKCancelDialog(Component parent, String title, Object message)
	{
		int choice = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		return choice == JOptionPane.OK_OPTION;
	}
	public DialogOptions showFileOverwriteDialog(String title, String message)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(message));
		JCheckBox applyToAll = new JCheckBox();
		JPanel applyPanel = new JPanel();
		applyPanel.add(new JLabel("Apply to all"));
		applyPanel.add(applyToAll);
		panel.add(applyPanel);
		int choice = JOptionPane.showConfirmDialog(getProjectExplorer(), panel, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		DialogOptions options = new DialogOptions();
		options.dialogResult = choice == JOptionPane.YES_OPTION;
		options.applyToAll = applyToAll.isSelected();
		return options;
	}
	public File showFileOpenDialog(Component parent, File defaultFile)
	{
		return showFileOpenDialog(parent, defaultFile.getParentFile(), defaultFile);
	}
	public File showFileOpenDialog(Component parent, File defaultDir, File defaultFile)
	{
		return showFileOpenDialog(parent, defaultDir, defaultFile, null);
	}
	public File showFileOpenDialog(Component parent, File defaultDir, File defaultFile, FilenameFilter filter) 
	{
		FileDialog fileDialog = new FileDialog(getProjectExplorer());
		if (defaultFile != null) {
			if (defaultDir != null)
				fileDialog.setDirectory(defaultDir.getPath());
			fileDialog.setFile(defaultFile.getName());
		}
		if (filter != null)
			fileDialog.setFilenameFilter(filter); 
		fileDialog.setMode(FileDialog.LOAD);
		fileDialog.setVisible(true);
		String filename = fileDialog.getFile();
		return filename!=null?new File(fileDialog.getDirectory(), filename):null;
	}
	public File showFileSaveDialog(Component parent, File defaultFile)
	{
		FileDialog fileDialog = new FileDialog(getProjectExplorer());
		if (defaultFile != null) {
			fileDialog.setDirectory(defaultFile.getParent());
			fileDialog.setFile(defaultFile.getName());
		}
		fileDialog.setMode(FileDialog.SAVE);
		fileDialog.setVisible(true);
		String filename = fileDialog.getFile();
		return filename!=null?new File(fileDialog.getDirectory(), filename):null;
	}
	public File showDirOpenDialog(Component parent, File defaultDir)
	{
		if (Platform.isMacOS()) {
		    System.setProperty("apple.awt.fileDialogForDirectories", "true");
		    FileDialog dialog = null;
		    if (parent instanceof JFrame == false && parent instanceof JDialog == false) {
		    	parent = ((JComponent)parent).getTopLevelAncestor();
		    }
		    if (parent instanceof JFrame)
		    	dialog = new FileDialog((JFrame)parent);
		    else
		    	dialog = new FileDialog((JDialog)parent);
		    if (defaultDir != null)
		    	dialog.setDirectory(defaultDir.getAbsolutePath());
		    dialog.setVisible(true);
		    if (dialog.getDirectory() == null || dialog.getFile() == null)
		    	return null;
		    File file = new File(dialog.getDirectory(), dialog.getFile());
		    System.setProperty("apple.awt.fileDialogForDirectories", "false");
		    return file;
		} else {
			JFileChooser chooser = new JFileChooser();
			if (defaultDir != null)
				chooser.setCurrentDirectory(defaultDir);
			chooser.setDialogType(JFileChooser.OPEN_DIALOG);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.showOpenDialog(parent);
			File file = chooser.getSelectedFile();
			return file;
		}
	}
	/**
	 * 
	 * @param parent
	 * @param title
	 * @param choices
	 * @return the index into choices which was selected
	 */
	public int showRadioChoiceDialog(Component parent, String title, String[] choices)
	{
		ButtonGroup group = new ButtonGroup();
		Box panel = Box.createVerticalBox();
		Map<ButtonModel, Integer> map = new HashMap<ButtonModel, Integer>();
		for (int i = 0; i < choices.length; ++i)
		{
			JRadioButton button = new JRadioButton(choices[i], i==0);
			group.add(button);
			panel.add(button);
			map.put(button.getModel(), i);
		}
		JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE);
		Integer selection = map.get(group.getSelection());
		if (selection == null)
			selection = -1;
		return selection;
	}
	public void showAboutDialog()
	{
		final JDialog splashDialog = new JDialog(getProjectExplorer());
		splashDialog.setModal(true);
		splashDialog.setResizable(false);
		SplashPage page = new SplashPage();
		splashDialog.add(page);
		splashDialog.pack();
		UIUtil.centerOnFrame(splashDialog, getProjectExplorer());
		splashDialog.setVisible(true);
	}
	
	public void addWindowListener(WindowListener listener)
	{
		getProjectExplorer().addWindowListener(listener);
	}
	public void addApplicationListener(ApplicationListener listener)
	{
		eventListeners.add(ApplicationListener.class, listener);
	}
	
	private long busyCount = 0;
	public void beginBusyOperation()
	{
		beginBusyOperation(getProjectExplorer());
	}
	/**
	 * Call before beginning a lengthy operation in the UI thread. The effect might for example
	 * be to set the cursor to the "busy" cursor (hourglass on windows).
	 */
	public void beginBusyOperation(Component target)
	{
		if (busyCount++ >= 0) {
			target.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		}
	}
	public void endBusyOperation()
	{
		endBusyOperation(getProjectExplorer());
	}
	/**
	 * Call after completing a lengthy operation in the UI thread. If this is the last such operation
	 * the default cursor is restored.
	 */
	public void endBusyOperation(Component target)
	{
		if (--busyCount < 1) {
			target.setCursor(null);
		}
	}
	
	public void destroy()
	{
		commonTaskScheduler.stop();
	}
	public UndoManager getUndoManager()
	{
		return undoManager;
	}
}
