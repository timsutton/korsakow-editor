package org.korsakow.ide.ui.interfacebuilder.panel;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.util.UIResourceManager;

public class ArrangePanel extends JPanel
{
	private JButton moveBackButton;
	private JButton moveBackwardsButton;
	private JButton moveForwardsButton;
	private JButton moveFrontButton;
	

	public ArrangePanel()
	{
		initUI();
		initListeners();
	}
	private void initUI()
	{
		add(moveBackButton = new JButton(UIResourceManager.getIcon("shape_move_back.png")));
		add(moveBackwardsButton = new JButton(UIResourceManager.getIcon("shape_move_backwards.png")));
		add(moveForwardsButton = new JButton(UIResourceManager.getIcon("shape_move_forwards.png")));
		add(moveFrontButton = new JButton(UIResourceManager.getIcon("shape_move_front.png")));
		
		moveBackButton.setToolTipText(LanguageBundle.getString("interfacebuilder.arrangepanel.moveback.tooltip"));
		moveBackwardsButton.setToolTipText(LanguageBundle.getString("interfacebuilder.arrangepanel.movebackwards.tooltip"));
		moveForwardsButton.setToolTipText(LanguageBundle.getString("interfacebuilder.arrangepanel.moveforwards.tooltip"));
		moveFrontButton.setToolTipText(LanguageBundle.getString("interfacebuilder.arrangepanel.movefront.tooltip"));
		
		setPreferredSize(new Dimension(Short.MAX_VALUE, 100));
	}
	private void initListeners()
	{
	}
	public void addMoveBackActionListener(ActionListener listener)
	{
		moveBackButton.addActionListener(listener);
	}
	public void addMoveBackwardsActionListener(ActionListener listener)
	{
		moveBackwardsButton.addActionListener(listener);
	}
	public void addMoveForwardsActionListener(ActionListener listener)
	{
		moveForwardsButton.addActionListener(listener);
	}
	public void addMoveFrontActionListener(ActionListener listener)
	{
		moveFrontButton.addActionListener(listener);
	}
}
