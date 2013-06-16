package org.korsakow.diagnostics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class QTJavaHelp
{
	private static final Logger logger = Logger.getLogger( QTJavaHelp.class.getName() );
	private static boolean printDisclaimer() throws IOException {
		System.out.println( ""
				+ "contact: contact@korsakow.org"
				+ "\n"
				+ "\nQTJavaHelp is designed to diagnose installations of Java and Quicktime and"
				+ "\nto possibly fix them."
				+ "\n"
				+ "\n* This software does not send any information anywhere, to anyone and doesn't do *"
				+ "\n* anything to your system without your permission. *"
				+ "\n* Here's what it does do, if you let it:"
				+ "\n - Ask you before making any changes to your system"
				+ "\n - Look for files"
				+ "\n - Copy files from one place on your disk to another"
				+ "\n - Log everything it does to a file called qjavathelp.log"
				+ "\n - "
				+ "\n* That said, don't sue me if it all goes horribly wrong somehow. *"
				+ "\n"
				+ "\n* If you can live with that type y and press enter, anything else will quit. *"
				+ "\n"
				+ "\n" );
		return Util.okay();
	}
	public static void main( String[] args ) throws Throwable {

//		if ( !Util.isWindowsOS() ) {
//			System.out.println( "This program is only designed to be run on windows." );
//			return;
//		}
		
		try {
			System.setProperty( "log4j.configuration", "ql.log4j.properties" );
			if ( !printDisclaimer() ) {
				System.out.println( "Bye." );
				return;
			}
			QTJavaHelp qtl = new QTJavaHelp();
			qtl.execute();
		} catch ( Throwable t ) {
			logger.throwing( "", "", t );
			throw t;
		}
	}
	
	public static Set<File> getDefaultJavaRoots() {
		Set<File> files = new HashSet<File>();
		if( Util.isMacOS() ) {
			for ( String filename : Arrays.asList(
					"/System/Library/Java/"
					) ) {
				files.add( new File( filename ) );
			}
		} else
		if ( Util.isWindowsOS() ) {
			for ( String filename : Arrays.asList(
					"c:/program files",
					"c:/program files (x86)"
					) ) {
				files.add( new File ( filename ) );
			}
		}
		return files;
	}
	public static String getJavaExecutableName() {
		if ( Util.isWindowsOS() ) return "java.exe";
		return "java";
	}
	public QTJavaHelp() throws IOException {
		final FileHandler handler = new FileHandler( "qtjavahelp.log", 500 * 1024, 1 );
		handler.setFormatter( new SimpleFormatter() );
		logger.addHandler( handler );
	}
	public void execute() throws IOException {
		
		logger.info( "QTJavaHelp starting up..." );
		
		Set<File> roots = getDefaultJavaRoots();
		logger.info( "Searching recursively in: " + Util.join( roots, "," ) );
		
		logger.info( "Locating Java..." );
		final AtomicReference<Set<File>> missingExtDirs = new AtomicReference<Set<File>>( new HashSet<File>() );
		final AtomicReference<Set<File>> javaExtDirs = new AtomicReference<Set<File>>( new HashSet<File>() );
		final AtomicReference<Set<File>> javaHomeDirs = new AtomicReference<Set<File>>( new HashSet<File>() );
		final AtomicReference<Integer> counter = new AtomicReference<Integer>( 0 );
		final AtomicReference<Integer> marker = new AtomicReference<Integer>( 0 );
		for ( File root : roots )
			Util.visitRecursively( root, new FileVisitor() {
				@Override public void visit( File file ) {
					char markers[] = { '.', 'o','O','o' };
					char mark = '.';
					if ( file.getName().equals( getJavaExecutableName() ) ) {
						File parent = file.getParentFile();
						if ( parent == null ) return;
						parent = parent.getParentFile(); // bin/../
						if ( parent == null ) return;
						
						javaHomeDirs.get().add( parent );
						
						File ext = new File( parent, "lib/ext" );
						if ( ext.isDirectory() )
							javaExtDirs.get().add( ext );
						Set<File> extFiles = new HashSet<File>();
						if ( ext.listFiles() != null )
							extFiles.addAll( Arrays.asList( ext.listFiles() ) );
						Set<File> tmp = new HashSet<File>();
						for ( File extFile : extFiles )
							if ( extFile.getName().equalsIgnoreCase( "qtjava.zip" ) ) {
								tmp.add( extFile );
							}
						if ( tmp.isEmpty() )
							missingExtDirs.get().add( ext );

						if  ( counter.get() % 1 == 0 ) {
							mark = markers[ marker.get() % markers.length ];
							marker.set( marker.get() + 1 );
							System.out.print( mark );
							if ( marker.get() % ( 80 - 9 ) == 0 )
								System.out.println( " d( o.o )b" );
						}
						counter.set( counter.get() + 1 );
					}
				}
			});

		if ( javaHomeDirs.get().isEmpty() ) {
			logger.info( "Could not locate Java, it must be in a non-standard location; giving up =(" );
			return;
		}
		logger.info( "Found java here: " + Util.join( javaHomeDirs.get(), "," ) );
		if ( missingExtDirs.get().isEmpty() ) {
			logger.info( "It looks like everything is okay; giving up!" );
			return;
		}
		logger.info( "But these ones were missing qtjava: " + Util.join( missingExtDirs.get(), "," ) );

		logger.info( "Locating qtjava ( Quicktime for Java )..." );
		Set<File> qtjavas = Util.find( roots, "qtjava.zip" );

		if ( qtjavas.isEmpty() ) {
			logger.info( "Quicktime for Java was not found; giving up =(" );
			return;
		}
		logger.info( "Found qtjava.zip here: " + Util.join( qtjavas, "," ) );


		logger.info( "So it looks like your Java installation can be fixed by copying qtjava over into it. If you're okay with this type y and press enter; anything else will bail out." );
		if ( !Util.okay() )
			return;
		
		logger.info( "Copying qtjava into Java installation directories..." );
		File qtjava = qtjavas.iterator().next(); // I guess we don't care which?
		Set<IOException> exceptions = new HashSet<IOException>();
		for ( File extDir : missingExtDirs.get() ) {
			try {
				final File toFile = new File( extDir, qtjava.getName() );
				logger.info( String.format( "Copying '%s' to '%s'", qtjava.getPath(), toFile.getPath() ) );
				if ( toFile.exists() ) {
					logger.info( String.format( "'%s' already exists, skipping", toFile.getPath() ) );
					continue;
				}
				Util.copyFile( qtjava, toFile );
			} catch (IOException e) {
				exceptions.add( e );
			}
		}
		
		if ( !exceptions.isEmpty() ) {
			Set<String> msgs = new HashSet<String>();
			for ( Exception e : exceptions )
				msgs.add( e.getMessage() );
			logger.info( "Some problems happened during the copy: '" + Util.join( msgs, "', '" ) + "'" );
		} else {
			logger.info( "All done; huh, it looks like everything went okay." );
		}
	}
}

interface FileVisitor
{
	void visit( File file );
}
class Util
{
	private static final Logger logger = Logger.getLogger( Util.class.getName() );
	public static boolean okay() throws IOException {
		return okay(false);
	}
	public static boolean okay(boolean picky) throws IOException {
		Scanner scanner = new Scanner( System.in );
		do {
			System.out.print( "Okay? y/n: " );
			String s = scanner.nextLine();
			if ( s.equalsIgnoreCase( "y" )) {
				System.out.println();
				return true;
			} else if ( s.equalsIgnoreCase( "n" ) ) {
				System.out.println();
				return false;
			} else {
				System.out.println();
			}
		} while ( picky );
		return false;
	}
	
	public static void visitRecursively( File parent, FileVisitor visitor ) throws IOException {
		visitRecursively(parent, visitor, new HashSet<String>());
	}
	private static void visitRecursively( File parent, FileVisitor visitor, Set<String> visited ) throws IOException {
		String canon = parent.getCanonicalPath();
		if (visited.contains(canon))
			return;
		visited.add(canon);

		visitor.visit( parent );
		File[] children = parent.listFiles();
		if ( children != null )
			for ( File child : children )
				visitRecursively( child, visitor, visited );
	}
	public static Set<File> find( final Collection<File> roots, final String name ) throws IOException {
		final AtomicReference<Integer> counter = new AtomicReference<Integer>( 0 );
		final AtomicReference<Integer> marker = new AtomicReference<Integer>( 0 );
		final AtomicReference<Set<File>> files = new AtomicReference<Set<File>>( new HashSet<File>() );
		FileVisitor visitor = new FileVisitor() {
			@Override public void visit(File file) {
				char markers[] = { '.', 'o','O','o' };
				char mark = '.';
				if  ( counter.get() % 5000 == 0 ) {
					mark = markers[ marker.get() % markers.length ];
					marker.set( marker.get() + 1 );
					System.out.print( mark );
					if ( marker.get() % ( 80 - 9 ) == 0 )
						System.out.println( " d( o.o )b" );
				}
				counter.set( counter.get() + 1 );
				if ( file.getName().equalsIgnoreCase( name ) )
					files.get().add( file );
			}
		};
		for ( File root : roots )
			visitRecursively( root, visitor );
		System.out.println();
		logger.info( String.format( "Finished searching through %d fliles", counter.get() ) );
		return files.get();
	}
	public static String join(Collection<?> coll, String glue)
	{
		StringBuilder builder = new StringBuilder();
		for (Object o : coll) {
			builder.append(o.toString()).append(glue);
		}
		if (builder.length() > 0)
			builder.delete(builder.length() - glue.length(), builder.length());
		return builder.toString();
	}
	public static boolean isWindowsOS()
	{
		final String osName =  System.getProperty("os.name", "unknown").toLowerCase();
		return osName.startsWith("windows");
	}
	public static boolean isMacOS()
	{
		final String osName =  System.getProperty("os.name", "unknown").toLowerCase();
		return osName.startsWith("mac") || osName.contains("darwin");
	}
	public static void copyFile(File sourceFile, File destFile) throws IOException
	{
		if (destFile.getParentFile() != null)
			destFile.getParentFile().mkdirs();
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			input = new FileInputStream(sourceFile);
			output = new FileOutputStream(destFile);
			long offset = 0;
			long length = sourceFile.length();
			long count = length;
			final FileChannel destChannel = output.getChannel();
			final FileChannel srcChannel = input.getChannel();
			do {
				long written = destChannel.transferFrom(srcChannel, offset, count);
				count -= written;
				offset += written;
			} while (count > 0);
		} finally {
			if (input != null) try { input.close(); } catch (IOException e) {}
			if (output != null) try { output.close(); } catch (IOException e) {}
		}
	}
}