package org.korsakow.ide.ui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.korsakow.ide.Application;
import org.korsakow.ide.Build;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.factory.IUIFactory;
import org.korsakow.ide.ui.factory.UIFactory;
import org.korsakow.ide.util.ShellExec;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.ShellExec.ShellException;

public class SplashPage extends JPanel
{
	private final ImageIcon mainImage;
	private JLabel versionLabel;
	private JLabel uuidLabel;
	private JTextField uuidField;
	private JButton label;
	public SplashPage()
	{
		setBackground(UIManager.getColor("window2"));
		
		IUIFactory uifac = UIFactory.getFactory();
		
		setLayout(uifac.createLayout("splash"));
		add(label = new JButton(UIResourceManager.getIcon("about_link.png")));
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label.setBorderPainted(false);
		label.setRolloverIcon(UIResourceManager.getIcon("about_link_over.png"));
		label.setRolloverEnabled(true);
		label.setFocusable(false);
		mainImage = (ImageIcon) UIResourceManager.getLanguageIcon(UIResourceManager.ABOUT_KEY);
		add(uifac.createLabel("image", mainImage));
		label.setName("text");
		
		add(versionLabel = uifac.createLabel("version", Build.getAboutString()), 0);
		versionLabel.setHorizontalAlignment(JLabel.CENTER);
		
		add(uuidLabel = uifac.createLabel("uuidLabel", "UUID:"), 0);
		add(uuidField = uifac.createTextField("uuidField", Application.getUUID()), 0);
		uuidField.setOpaque(false);
		uuidField.setBorder(null);
		uuidField.setHorizontalAlignment(JLabel.LEFT);
		uuidField.setEditable(false);
		
		
		label.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					ShellExec.openUrl(LanguageBundle.getString("splash.link"));
				} catch (ShellException e) {
					Logger.getLogger(SplashPage.class).error("", e);
				}
			}
		});
	}
	public void setUUIDVisible(boolean visible)
	{
		uuidLabel.setVisible(visible);
		uuidField.setVisible(visible);
	}
}
