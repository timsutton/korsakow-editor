package org.korsakow.ide.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.filechooser.FileFilter;

import org.korsakow.ide.io.AsyncStreamPipe;
import org.korsakow.services.encoders.image.ImageFormat;
import org.korsakow.services.encoders.sound.SoundFormat;
import org.korsakow.services.encoders.video.VideoCodec;

public class FileUtil
{
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public static final Pattern VIDEO_FILE_EXTENSION_PATTERN = Pattern.compile(ResourceBundle.getBundle("MyResources").getString("video_pattern"), Pattern.CASE_INSENSITIVE);
	public static final Pattern SOUND_FILE_EXTENSION_PATTERN = Pattern.compile(ResourceBundle.getBundle("MyResources").getString("sound_pattern"), Pattern.CASE_INSENSITIVE);
	public static final Pattern IMAGE_FILE_EXTENSION_PATTERN = Pattern.compile(ResourceBundle.getBundle("MyResources").getString("image_pattern"), Pattern.CASE_INSENSITIVE);
	public static final Pattern TEXT_FILE_EXTENSION_PATTERN = Pattern.compile(ResourceBundle.getBundle("MyResources").getString("text_pattern"), Pattern.CASE_INSENSITIVE);
//	public static final String[] VIDEO_FILE_EXTENSIONS = {
//		"flv", "f4v", "mp4", "m4a", "mpg", "mov", "mp4v", "m4v", "3gp", "3g2", "avi",
//	};
//	public static final String[] SOUND_FILE_EXTENSIONS = {
//		"wav", 
//		"mp3",
//	};
//	public static final String[] IMAGE_FILE_EXTENSIONS = {
//		"jpg", "jpeg", "png", "bmp", "gif",
//	};
//	public static final String[] TEXT_FILE_EXTENSIONS = {
//		"txt",
//	};
	
	public static final FileFilter VIDEO_FILE_CHOOSER_FILTER = new FileFilter() {
	
		@Override
		public boolean accept(File file) {
			return VIDEO_FILE_EXTENSION_PATTERN.matcher(file.getName()).matches();
		}
	
		@Override
		public String getDescription() {
			return "Videos";
		}
	};
	public static final FileFilter SOUND_FILE_CHOOSER_FILTER = new FileFilter() {
		
		@Override
		public boolean accept(File file) {
			return SOUND_FILE_EXTENSION_PATTERN.matcher(file.getName()).matches();
		}
	
		@Override
		public String getDescription() {
			return "Sound";
		}
	};
	public static final FileFilter TEXT_FILE_CHOOSER_FILTER = new FileFilter() {
		@Override
		public boolean accept(File file) {
			return TEXT_FILE_EXTENSION_PATTERN.matcher(file.getName()).matches();
		}
	
		@Override
		public String getDescription() {
			return "Text";
		}
	};

	public static boolean isMediaFile(String filename)
	{
		return isVideoFile(filename) || isSoundFile(filename) || isImageFile(filename) || isTextFile(filename);
	}

	public static boolean isVideoFile(String filename)
	{
		return VIDEO_FILE_EXTENSION_PATTERN.matcher(filename).matches();
	}
	public static boolean isSoundFile(String filename)
	{
		return SOUND_FILE_EXTENSION_PATTERN.matcher(filename).matches();
	}
	public static boolean isImageFile(String filename)
	{
		return IMAGE_FILE_EXTENSION_PATTERN.matcher(filename).matches();
	}
	public static boolean isTextFile(String filename)
	{
		return TEXT_FILE_EXTENSION_PATTERN.matcher(filename).matches();
	}
	public static String getFileExtension(SoundFormat format)
	{
		return format.name().toLowerCase();
	}
	public static String getFileExtension(ImageFormat format)
	{
		return format.name().toLowerCase();
	}
	public static String getFileExtension(VideoCodec format)
	{
		return format.name().toLowerCase();
	}
	public static String setFileExtension(String filename, String ext)
	{
		return getFilenameWithoutExtension(filename) + "." + ext;
	}
	public static String getFileExtension(String filename)
	{
		int index = filename.lastIndexOf('.');
		if (index == -1)
			return "";
		return filename.substring(index+1);
	}
	// TODO: rename this to stripExtension since it leaves any leading path info intact
	public static String getFilenameWithoutExtension(String filename)
	{
		String ext = getFileExtension(filename);
		if (ext.length() == 0)
			return filename;
		int index = filename.lastIndexOf(ext);
		if (index == -1)
			return filename;
		int lastSeparator = 0;
// TODO: uncomment this section
//		lastSeparator = filename.lastIndexOf(File.separatorChar);
//		if (lastSeparator == -1)
//			lastSeparator = 0;
//		else
//			lastSeparator += 1;
		return filename.substring(lastSeparator, index-1); // -1 for "."
	}
	
	public static boolean isProbablyADirectory( File file ) {
		return isProbablyADirectory(file.getPath());
	}
	public static boolean isProbablyADirectory( String path ) {
		File file = new File( path );
		if (file.isDirectory())
			return true;
		if (file.isFile())
			return false;
		return !file.getName().contains(".");
	}
	
	public static void copyFile(String sourceFile, String destFile) throws IOException
	{
		copyFile(new File(sourceFile), new File(destFile));
	}
	public static void copyFile(File sourceFile, File destFile) throws IOException
	{
		if (destFile.getParentFile()!=null)
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
	public static void copyTree(File dir, File dest) throws IOException
	{
		if (!dir.isDirectory()) {
			copyFile(dir, dest);
			return;
		}
		for (File child : dir.listFiles())
		{
			copyTree(child, new File(dest, child.getName()));
		}
	}
	public static void setExecutable(File file, boolean executable) throws IOException
	{
		Process p;
		switch (Platform.getOS())
		{
		case MAC:
		case NIX:
			p = Runtime.getRuntime().exec(new String[] {
				"chmod",
				"u"+(executable?'+':'-')+"x",
				file.getAbsolutePath(),
			});
			try {
				AsyncStreamPipe<InputStream, ByteArrayOutputStream> errPipe = new AsyncStreamPipe<InputStream, ByteArrayOutputStream>(p.getErrorStream(), new ByteArrayOutputStream());
				int exitcode = p.waitFor();
				errPipe.join();
				if (exitcode != 0)
					throw new IOException(errPipe.getOutputStream().toString());
			} catch (InterruptedException e) {
				throw new IOException(e.getMessage());
			}
			p.destroy();
			break;
		case WIN:
			break;
		}
	}
	public static File createTempDirectory(String prefix, String suffix) throws IOException
	{
		return createTempDirectory(prefix, suffix, null);
	}
	public static File createTempDirectory(String prefix, String suffix, File directory) throws IOException
	{
		File file = File.createTempFile(prefix, suffix, directory);
		// technically this leaves a small window during which the file may be taken by some other process...
		delete(file);
		mkdirs(file);
		return file;
	}
	/**
	 * Delete may fail... yet do we care so long as the file does not exist?
	 * This method throws if delete fails AND the file still exists
	 * @param file
	 * @throws IOException
	 */
	public static void delete(File file) throws IOException
	{
		if (!file.delete())
			if (file.exists())
				throw new IOException("could not delete file");
	}
	/**
	 * Its unclear if File.mkdirs will return false if the directory could not be created but actually already exists.
	 * This method calls mkdirs and throws if at the end the dirs dont exist.
	 * @param file
	 * @throws IOException
	 */
	public static void mkdirs(File file) throws IOException
	{
		if (!file.mkdirs())
			if (!file.isDirectory())
				throw new IOException("could not make dirs: " + file.getPath());
	}
	public static String readFileAsString(String filename) throws IOException
	{
		return readFileAsString(new File(filename));
	}
	public static String readFileAsString(File file) throws IOException
	{
		byte[] buffer = new byte[(int)file.length()];
		DataInputStream reader = new DataInputStream(new FileInputStream(file));
		try {
			reader.readFully(buffer);
			return new String(buffer, "UTF-8");
		} finally {
			try { reader.close(); } catch (IOException e) {}
		}
	}
	public static List<String> readFileLines(File file) throws IOException
	{
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		try {
			while ((line=reader.readLine()) != null)
				lines.add(line);
		} finally {
			try { reader.close(); } catch (IOException e) {}
		}
		return lines;
	}
	public static void writeFileFromString(String filename, String content) throws IOException
	{
		File file = new File(filename);
		writeFileFromString(file, content);
	}
	public static void writeFileFromString(File file, String content) throws IOException
	{
		File parent = file.getParentFile();
		if (parent != null)
			parent.mkdirs();
		BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(file), "UTF-8") );
		try {
			writer.write(content);
			writer.flush();
		} finally {
			try { writer.close(); } catch (IOException e) {}
		}
	}
	/**
	 * Fully writes one stream to another
	 * @param input
	 * @param output
	 */
	public static void writeStreamFully(InputStream input, OutputStream output) throws IOException
	{
		try {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = input.read(buffer)) != -1)
			{
				output.write(buffer, 0, len);
			}
			output.flush();
		} finally {
			try { output.close(); } catch (IOException e) {}
		}
	}
	public static byte[] readBytesFully(InputStream input) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(input.available());
		writeStreamFully(input, baos);
		return baos.toByteArray();
	}
	public static String readString(InputStream input) throws IOException
	{
		return new String(readBytesFully(input), "UTF-8");
	}
	public static void unzip(ZipInputStream zip, File parentDir) throws IOException
	{
		try {
			ZipEntry entry = null;
			while ((entry = zip.getNextEntry()) != null)
			{
				File entryFile = new File(parentDir, entry.getName());
				if (entry.isDirectory()) {
					entryFile.mkdirs();
				} else {
					FileOutputStream outputStream = new FileOutputStream(entryFile);
					FileUtil.writeStreamFully(zip, outputStream);
				}
				zip.closeEntry();
			}
		} finally {
			try { zip.close(); } catch (IOException e) {}
		}
	}
	public static int executeProcess(Process process, OutputStream outBuff, OutputStream errBuff) throws InterruptedException
	{
		AsyncStreamPipe<InputStream, OutputStream> errPiper = new AsyncStreamPipe<InputStream, OutputStream>(process.getErrorStream(), errBuff);
		AsyncStreamPipe<InputStream, OutputStream> outPiper = new AsyncStreamPipe<InputStream, OutputStream>(process.getInputStream(), outBuff);
		errPiper.start();
		outPiper.start();
		// we avoid thread(process) issues by basically not doing anything until all pipers have joined.
		try {
			// it is essential that we capture stdout because sometimes if you don't, Process.waitFor won't ever return!
			// at least this has been observed on windows
			final int exitCode = process.waitFor();
			// we join&destroy here as well as in finally so that if one of them throws legitimately it can propagate and we won't swallow an exception
			errPiper.join();
			outPiper.join();
			process.destroy();
			return exitCode;
		} finally {
			try { process.destroy(); } catch (Throwable t) {} // this should be first to ensure the joins don't block (e.g if we are here from an InterruptedException) 
			try { errPiper.join(); } catch (Throwable t) {}
			try { outPiper.join(); } catch (Throwable t) {}
			try { if (!System.err.equals(outBuff) && !System.out.equals(outBuff)) outBuff.close(); } catch (Throwable t) {}
			try { if (!System.err.equals(errBuff) && !System.out.equals(errBuff)) errBuff.close(); } catch (Throwable t) {}
		}
	}
}
