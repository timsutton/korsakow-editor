package org.korsakow.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dsrg.soenea.service.Registry;
import org.korsakow.domain.k3.importer.K3ImportException;
import org.korsakow.ide.Application;
import org.korsakow.ide.Build;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.export.ExportException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class ErrorMailer {
	
	/**
	 * 
	 * TODO: Stu: Currently, we arent grabbing the particulars of a broken video because I don't want to take care of file sizes now
	 * 
	 * @param title
	 * @param message
	 * @param details
	 * @throws IOException
	 */
	public static boolean sendError(String title, String message, Throwable details) throws Exception{
		if (!Application.getInstance().showOKCancelDialog(LanguageBundle.getString("general.errormailer_confirm.title"), LanguageBundle.getString("general.errormailer_confirm.message")))
			return false;
		File logFile = new File(Application.getLogfilename());
		File projectFile = DataRegistry.getFile();
		File saveDir = logFile.getParentFile();
		File dumpDir = new File(saveDir, "MyBugReport");
		dumpDir.mkdir();
		File myProject = new File(dumpDir, "MyProject");
		myProject.mkdir();
		
		
		
		//Start dumping the thigns we always want:
		copy(logFile, new File(dumpDir, "Korsakow.log"));
		if(projectFile != null)copy(projectFile, new File(myProject, "Korsakow.krw"));
		File myExport = null;
		if(isExportException(details) != null) {
			myExport = new File(dumpDir, "MyExport");
			myExport.mkdir();
			copy(new File(isExportException(details).getProjectFile(), "project.xml"), new File(myExport, "project.xml"));
		}
		File myImport = null;
		if(isImportException(details) != null) {
			myImport = new File(dumpDir, "MyImport");
			myImport.mkdir();
			if(isImportException(details).getDatabaseFile() != null && isImportException(details).getDatabaseFile().exists()) {
				copy(isImportException(details).getDatabaseFile(), new File(myImport, "database.txt"));
			}
			if(isImportException(details).getInterfaceFile() != null && isImportException(details).getInterfaceFile().exists()) {
				copy(isImportException(details).getInterfaceFile(), new File(myImport, "interfaces.txt"));
			}
		}
		
		zipDirectory(dumpDir, new File(saveDir, "MyBugReport.zip"));
		
//		Level oldLEvel = Logger.getRootLogger().getLevel();
//		//Disable logging while sending a message
//		Logger.getRootLogger().setLevel(Level.OFF);
		try {
			long time = System.currentTimeMillis();
			String uuid = Application.getUUID();
			String mailSubject = String.format("R%s; %s : %s", Build.getRelease(), Application.getUUID(), title);
			String mailContent = String.format("Release: %s\tVersion: %s\nFrom: %s\nTimestamp: %d\nDate: %s\nMessage: %s\n", Build.getRelease(), Build.getVersion(), uuid, time, new Date(time).toGMTString(), message);
			postErrorLog(mailSubject, mailContent, new File(saveDir, "MyBugReport.zip"));
		} finally {
//			Logger.getRootLogger().setLevel(oldLEvel);
		}
		
		if(myImport != null) {
			if(new File(myImport, "database.txt").exists())new File(myImport, "database.txt").delete();
			if(new File(myImport, "interfaces.txt").exists())new File(myImport, "interfaces.txt").delete();
			myImport.delete();
		}
		
		if(myExport != null) {
			if(new File(myExport, "project.xml").exists())new File(myExport, "project.xml").delete();
			myExport.delete();
		}
		
		if(new File(myProject, "Korsakow.krw").exists())new File(myProject, "Korsakow.krw").delete();
		if(new File(dumpDir, "Korsakow.log").exists())new File(dumpDir, "Korsakow.log").delete();
		new File(saveDir, "MyBugReport.zip").delete();
		myProject.delete();
		dumpDir.delete();
		return true;
	}
	
	public static ExportException isExportException(Throwable details) {
		if(details instanceof ExportException) return (ExportException)details;
		if(details.getCause() != null) return isExportException(details.getCause());
		return null;
	}
	
	public static K3ImportException isImportException(Throwable details) {
		if(details instanceof K3ImportException) return (K3ImportException)details;
		if(details.getCause() != null) return isImportException(details.getCause());
		return null;
	}
	
    /**
     * From:
     * http://www.exampledepot.com/egs/java.nio/File2File.html
     * 
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void copy(File src, File dst) throws IOException {
        // Create channel on the source
        FileChannel srcChannel = new FileInputStream(src.getCanonicalFile()).getChannel();
    
        // Create channel on the destination
        FileChannel dstChannel = new FileOutputStream(dst.getCanonicalFile()).getChannel();
    
        // Copy file contents from source to destination
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
    
        // Close the channels
        srcChannel.close();
        dstChannel.close();
    }
    
    /**
     * Grabbed from:
     * http://www.java2s.com/Code/Java/File-Input-Output/CompressfilesusingtheJavaZIPAPI.htm
     * 
     * with moderate changes (liek recursive processing)
     */
    /** Zip the contents of the directory, and save it in the zipfile */
    public static void zipDirectory(File d, File zipfile)
        throws IOException, IllegalArgumentException {
      // Check that the directory is a directory, and get its contents

    	String base = d.getCanonicalPath();
    	
      if (!d.isDirectory())
        throw new IllegalArgumentException("Not a directory:  "
            + d.getCanonicalPath());
      File[] entries = d.listFiles();
      byte[] buffer = new byte[4096]; // Create a buffer for copying
      int bytesRead;

      ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));

      zipDir(d, base, entries, buffer, out);
      out.close();
    }

	private static void zipDir(File d, String base, File[] entries,
			byte[] buffer, ZipOutputStream out) throws FileNotFoundException,
			IOException {
		int bytesRead;
		for (File f: entries) {
		    if (f.isDirectory()) {
		    	zipDir(d, base, f.listFiles(), buffer, out);
		    	continue;
		    }
		    FileInputStream in = new FileInputStream(f); // Stream to read file
		    ZipEntry entry = new ZipEntry(f.getPath().substring(base.length()).replace('\\', '/')); // Make a ZipEntry
		    out.putNextEntry(entry); // Store entry
		    while ((bytesRead = in.read(buffer)) != -1)
		      out.write(buffer, 0, bytesRead);
		    in.close(); 
		  }
	}
	
	public static void postErrorLog(String title, String message, File zip) 
	throws Exception {
		HttpClient client = new HttpClient();

		URL url = new URL(Registry.getProperty("errorLogURL"));
		PostMethod filePost = new PostMethod(url.toString());
		
		List<Part> parts = new Vector<Part>();
		parts.add(new StringPart(Registry.getProperty("errorLogTitleField"), title));
		parts.add(new StringPart(Registry.getProperty("errorLogMessageField"), message));
		parts.add(new FilePart(Registry.getProperty("errorLogZipField"), zip));
		
		filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
		filePost.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[] {}), filePost.getParams()));

		postRequest(client, filePost);
	}
	
	public static void postRequest(HttpClient client, PostMethod method) throws ErrorMailerException {
		try {
	        client.executeMethod(method); // we ignore the http status as its not a reliable way to check return value; instead just try to read the response
	        String response = method.getResponseBodyAsString();
	        Document document = DomUtil.parseXMLString(response);
	        int statusCode = XPathHelper.xpathAsInt(document, "/response/status/code");
	        String message = XPathHelper.xpathAsString(document, "response/status/message");
	        if (statusCode != 0) {
	        	throw new ErrorMailerException(message);
	        }
		} catch (HttpException e) {
			throw new ErrorMailerException(e);
		} catch (IOException e) {
			throw new ErrorMailerException(e);
		} catch (SAXException e) {
			throw new ErrorMailerException(e);
		} catch (ParserConfigurationException e) {
			throw new ErrorMailerException(e);
		} catch (XPathExpressionException e) {
			throw new ErrorMailerException(e);
		}
	}

	
}
