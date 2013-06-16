package org.korsakow.ide.util;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * TODO: refactor this, using inheritance in place of switches
 * TODO: refactor this, perhaps it should be obtained from the Platform class
 * 
 * Named after the win32 API function call of the same name.
 * The term has kind of stuck with me over the years as representing the functionality in general.
 * 
 * A gateway to executing common platform dependant tasks in a manner similar to Runtime.exec but
 * based on actions not executable files.
 * 
 * Examples include opening urls in the user's default browser, opening explorer/finder with a particular file selected.
 * 
 * Unsurprisingly just about every method can throw a ShellException.
 * 
 * @author d
 *
 */
public class ShellExec
{
	public static class ShellException extends Exception
	{
		public ShellException(Throwable cause)
		{
			super(cause);
		}
		public ShellException(String msg)
		{
			super(msg);
		}
	}
	public static Process openUrl(URL url) throws ShellException
	{
		return openUrl(url.toString());
	}
	public static Process openUrl(String url) throws ShellException
	{
		try {
			switch (Platform.getOS())
			{
			case WIN:
				// Thanks: http://groups-beta.google.com/group/comp.lang.java.programmer/msg/bd52c25dad8c1589
				 return Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			case MAC:
				return Runtime.getRuntime().exec(new String[]{"open", url});
			default:
				throw new ShellException("not supported on this platform");
	//		case NIX:
//				break;
			}
		} catch (Exception e) {
			throw new ShellException(e);
		}
	}
	public static void revealInPlatformFilesystemBrowser(String path) throws ShellException
	{
		Logger.getLogger(ShellExec.class).debug("revealInPlatformFilesystemBrowser: " + path);
		try {
			switch (Platform.getOS())
			{
			case WIN:{
//				File file = ExternalsResourceManager.externalizePlatformScript("reveal.bat");
//				Runtime.getRuntime().exec(new String[]{file.getAbsolutePath(), path});
//				System.out.println(String.format("rundll32 SHELL32.DLL,ShellExec_RunDLL  \"%s\"", String.format("explorer /n,/select,\\\"%s\\\"", path)));
//				Runtime.getRuntime().exec(String.format("rundll32 SHELL32.DLL,ShellExec_RunDLL  \"%s\"", String.format("explorer /n,/select,\\\"%s\\\"", path)));
				File file = new File(path);
				if (file.isFile())
					file = file.getParentFile();
				if (file == null)
					throw new ShellException("file has no containing dir: " + path); // should not ever happen though, right?
				Runtime.getRuntime().exec(String.format("rundll32 SHELL32.DLL,ShellExec_RunDLL  \"%s\"", file.getCanonicalPath()));
				}break;
			case MAC:{
				File file = ExternalsResourceManager.getPlatformScript("reveal.applescript");
				Runtime.getRuntime().exec(new String[]{"osascript", file.getAbsolutePath(), path});
				}break;
			default:
				throw new ShellException("not supported on this platform");
	//		case NIX:
//				break;
			}
		} catch (Exception e) {
			throw new ShellException(e);
		}
	}
}
