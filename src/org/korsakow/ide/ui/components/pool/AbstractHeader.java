/**
 * 
 */
package org.korsakow.ide.ui.components.pool;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.factory.IUIFactory;
import org.korsakow.ide.ui.factory.UIFactory;

public class AbstractHeader extends KCollapsiblePane.Header
{
	public AbstractHeader()
	{
		
	}
	public AbstractHeader(String label)
	{
		super();
		setText(label);
	}
}