/**
 * 
 */
package test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.ide.util.ResourceManager.IResourceSource;

public class TestResourceSource implements IResourceSource
{
	private static final String RESOURCE_BASE_PATH = "/resources/";
	private final Class<?> clazz;
	public TestResourceSource()
	{
		this.clazz = ResourceManager.class;
	}
	public File getResourceFile(String name) throws MissingResourceException {
		File parentDir;
		final String property = System.getProperty( "korsakow.resources.dir" );
		try {
			parentDir = new File( property!=null?property:clazz.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() );
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		// TODO: why do we prefix with ./ ?
		File file = new File(parentDir, "." + File.separator  + RESOURCE_BASE_PATH + name);
		if (!file.exists()) {
			file = new File(parentDir, "." + File.separator  + name);
			if ( !file.exists() )
				throw new MissingResourceException(file.getAbsolutePath(), clazz.getCanonicalName(), name);
		}
		return file;
	}
	public InputStream getResourceStream(String name) throws MissingResourceException {
		try {
			return new FileInputStream(getResourceFile(name));
		} catch (FileNotFoundException e) {
			throw new MissingResourceException(e.getMessage(), clazz.getCanonicalName(), name);
		}
	}
}
