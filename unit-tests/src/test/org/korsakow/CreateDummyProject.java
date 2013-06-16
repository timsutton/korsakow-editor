package test.org.korsakow;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.xml.transform.TransformerException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.junit.Assert;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.LoadProjectCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.Main;
import org.korsakow.ide.util.FileUtil;

public class CreateDummyProject
{
	public static final Random rand = new Random();
	public static String getRand(String... s) {
		return s[rand.nextInt(s.length)];
	}
	public static void main(String[] args) throws Exception {
		Main.setupLogging();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					doFrame();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
	public static void doFrame() throws Exception {
		JFrame frame = new JFrame();
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel p;
		
		p = new JPanel(new FlowLayout());
		final JButton workDirButton = new JButton("...");
		final JTextField workDirField = new JTextField(String.format("%s/tmp/kdummy", System.getProperty("user.home")));
		workDirField.setPreferredSize(new Dimension(440, 25));
		p.add(new JLabel("Work Dir"));
		p.add(workDirField);
		p.add(workDirButton);
		frame.add(p);
		
		p = new JPanel(new FlowLayout());
		final JButton inFileButton = new JButton("...");
		final JTextField inFileField = new JTextField();
		inFileField.setPreferredSize(new Dimension(440, 25));
		p.add(new JLabel("Source File"));
		p.add(inFileField);
		p.add(inFileButton);
		frame.add(p);
		
		p = new JPanel(new FlowLayout());
		final JButton outFileButton = new JButton("...");
		final JTextField outFileField = new JTextField("/Users/d/Work/Korsakow/export/default/");
		outFileField.setPreferredSize(new Dimension(440, 25));
		p.add(new JLabel("Out Dir"));
		p.add(outFileField);
		p.add(outFileButton);		
		frame.add(p);
		
		JButton okButton = new JButton("OK");
		frame.add(okButton);
		
		inFileButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				chooser.showOpenDialog((Component)e.getSource());
				File file = chooser.getSelectedFile();
				if ( file == null )
					return;
				inFileField.setText(file.getPath());
			};
		});
		
		workDirButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				chooser.showOpenDialog((Component)e.getSource());
				File file = chooser.getSelectedFile();
				if ( file == null )
					return;
				workDirField.setText(file.getPath());
			};
		});
		
		outFileButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				chooser.showOpenDialog((Component)e.getSource());
				File file = chooser.getSelectedFile();
				if ( file == null )
					return;
				outFileField.setText(file.getPath());
			};
		});
		
		okButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				String basePath = workDirField.getText();
				String inPath = inFileField.getText();
				String outPath = outFileField.getText();
				try {
					create( basePath, inPath, new File(outPath, new File(inPath).getName()).getPath() );
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
		});
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setSize(640, 480);
		frame.setVisible(true);
	}
	public static void create(String basePath, String inPath, String outPath) throws CommandException, FileNotFoundException, IOException, SQLException, KeyNotFoundException, CreationException, MapperException, TransformerException {
		
		Request request = new Request();
		request.set("filename", inPath);
		Response response = CommandExecutor.executeCommand(LoadProjectCommand.class, request);
		IProject project = (IProject)response.get("project");
		for (IMedia media : project.getMedia()) {
			File mediaFile = new File(basePath, media.getFilename()
					.replace('\\', File.separatorChar)
					.replace('/', File.separatorChar)
					.replace(':', '_')
					);
			media.setFilename(mediaFile.getPath());
			if (!mediaFile.exists()) {
				mediaFile.getParentFile().mkdirs();
				if (media instanceof IVideo) {
					final File dummyFile = getDummyVideoFile();
					System.out.println("<" + dummyFile);
					writeMedia(mediaFile, dummyFile);
				} else
				if (media instanceof IImage) {
					final File dummyFile = getDummyImageFile();
					System.out.println("<" + dummyFile);
					writeMedia(mediaFile, dummyFile);
				} else
				if (media instanceof ISound) {
					final File dummyFile = getDummySoundFile();
					System.out.println("<" + dummyFile);
					writeMedia(mediaFile, dummyFile);
				} else {
					throw new IllegalArgumentException(String.format("Don't know how to fake %s; %s", media.getClass().getName(), media.getFilename()));
					//FileUtil.writeFileFromString(mediaFile, "");
				}
			}
			
			if (media instanceof IVideo) {
				IVideo video = (IVideo)media;
				if (video.getSubtitles() != null) {
					File subFile = new File(basePath, video.getSubtitles());
					if (!subFile.exists()) {
						subFile.getParentFile().mkdirs();
						FileUtil.writeFileFromString(subFile, "");
					}
					video.setSubtitles(subFile.getPath());
				}
			}
			UoW.getCurrent().registerDirty(media);
			System.out.println(">"+mediaFile);
		}
		UoW.getCurrent().commit();
		
		final File outFile = new File(outPath);
		outFile.getParentFile().mkdirs();
		DataRegistry.setFile(outFile);
		DataRegistry.flush();
	}
	private static void writeMedia(File mediaFile, final File dummyFile)
			throws FileNotFoundException, IOException
	{
		final FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);
		final FileChannel channel = fileOutputStream.getChannel();
		Assert.assertEquals(dummyFile.length(), new FileInputStream(dummyFile).getChannel().transferTo(0, dummyFile.length(), channel));
		channel.force(true);
		channel.close();
		fileOutputStream.flush();
		fileOutputStream.close();
	}
	private static File getDummyImageFile()
	{
		final File blankFile = new File(getRand(
				"/Users/d/Work/Korsakow/media/images/placeholder.jpg"
				));
		return blankFile;
	}
	private static File getDummyVideoFile()
	{
		final File blankFile = new File(getRand(
				"/Users/d/Work/Korsakow/media/videos/mokay.mov"
//				"/Users/d/Work/Korsakow/media/videos/IronMan4.mov",
//				"/Users/d/Work/Korsakow/media/videos/man01.mov",
//				"/Users/d/Work/Korsakow/media/videos/Hatschifanta.flv",
//				"/Users/d/Work/Korsakow/media/videos/aesthetics1.mov"
				));
		return blankFile;
	}
	private static File getDummySoundFile()
	{
		final File blankFile = new File(getRand(
				"/Users/d/Work/Korsakow/media/sounds/clips/train.wav"
				));
		return blankFile;
	}
}
