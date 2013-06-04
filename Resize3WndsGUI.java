import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.sun.jna.*;

public class Resize3WndsGUI extends ResizeGUI
{
	private SelectWndsGUI mainGUI;
	private JPanel choiceBoxes, configButtons, bottomButtons;
	private JComboBox box, box2, box3;
	private JButton vert, horiz, halfVert, halfHoriz;
	
	private int prevHandle, handle, handle2, handle3, mod, mod2;
	private ArrayList<Window> wndList;
	private String[] titles;
	
	public Resize3WndsGUI(SelectWndsGUI GUI, ArrayList<Window> list)
	{
		setLayout(new BorderLayout());
		mainGUI = GUI;
		wndList = list;
		titles = getTitles(wndList);
		
		
		choiceBoxes = new JPanel();		
		choiceBoxes.setLayout(new BoxLayout(choiceBoxes, BoxLayout.Y_AXIS));
			JPanel Box = new JPanel();
			JPanel Box2 = new JPanel();
			JPanel Box3 = new JPanel();
		
		box = new JComboBox(titles);
		box2 = new JComboBox(titles); box2.setSelectedItem(titles[1]);
		box3 = new JComboBox(titles); box3.setSelectedItem(titles[2]);
		
		Box.add(new JLabel("1")); Box.add(box);
    	Box2.add(new JLabel("2")); Box2.add(box2);
    	Box3.add(new JLabel("3")); Box3.add(box3);
    	
    	choiceBoxes.add(Box); choiceBoxes.add(Box2); choiceBoxes.add(Box3);
    	
		
    	vert = new JButton(new ImageIcon("3_vert.png"));
    	HandlerClass handler = new HandlerClass(0);
		vert.addActionListener(handler);
		
    	horiz = new JButton(new ImageIcon("3_horiz.png"));
    	HandlerClass handler2 = new HandlerClass(1);
		horiz.addActionListener(handler2);
		
    	halfVert = new JButton(new ImageIcon("3_halfVert.png"));
    	HandlerClass handler3 = new HandlerClass(2);
		halfVert.addActionListener(handler3);
		
		halfHoriz = new JButton(new ImageIcon("3_halfHoriz.png"));
		HandlerClass handler4 = new HandlerClass(3);
		halfHoriz.addActionListener(handler4);
		
		configButtons = new JPanel(new GridLayout(2, 2, 5, 5));
		configButtons.setBorder(new EmptyBorder(0, 30, 10, 30));
		configButtons.add(vert); configButtons.add(horiz); configButtons.add(halfVert); configButtons.add(halfHoriz);
		
		
		bottomButtons = new JPanel();
		bottomButtons.setLayout(new BorderLayout());
		bottomButtons.add(makeBottomButtons(wndList, mainGUI, false), BorderLayout.EAST);
		
		
    	add(choiceBoxes, BorderLayout.NORTH);
    	add(configButtons, BorderLayout.CENTER);
    	add(bottomButtons, BorderLayout.SOUTH);
	}
	
	public void resizeWnds(int config)
	{
		int[] handles = {handle, handle2, handle3};
		showWnds(handles);
		
		switch(config) 
		{
			case 0: 
				lib.MoveWindow(handle, 0, 0, width/3, height, true);
				lib.MoveWindow(handle2, width/3, 0, width/3, height, true);
				lib.MoveWindow(handle3, 2 * width/3, 0, width/3, height, true); 
				resetPics(); break;
			
			case 1:
				lib.MoveWindow(handle, 0, 0, width, height/3, true);
				lib.MoveWindow(handle2, 0, height/3, width, height/3, true);
				lib.MoveWindow(handle3, 0, 2 * height/3, width, height/3, true); 
				resetPics();  break;
			
			case 2:
				if(mod == 0)
				{
					resetPics();
					lib.MoveWindow(handle, 0, 0, width/2, height, true);
					lib.MoveWindow(handle2, width/2, 0, width/2, height/2, true);
					lib.MoveWindow(handle3, width/2, height/2, width/2, height/2, true);
					halfVert.setIcon(new ImageIcon("3_halfVert2.png"));
					mod++; return;
				}
				
				if(mod == 1)
				{
					lib.MoveWindow(handle, width/2, 0, width/2, height, true);
					lib.MoveWindow(handle2, 0, 0, width/2, height/2, true);
					lib.MoveWindow(handle3, 0, height/2, width/2, height/2, true);
					halfVert.setIcon(new ImageIcon("3_halfVert.png"));
					mod--; return;
				}break;
			
			case 3:
				if(mod2 == 0)
				{
					resetPics();
					lib.MoveWindow(handle, 0, 0, width, height/2, true);
					lib.MoveWindow(handle2, 0, height/2, width/2, height/2, true);
					lib.MoveWindow(handle3, width/2, height/2, width/2, height/2, true);
					halfHoriz.setIcon(new ImageIcon("3_halfHoriz2.png"));
					mod2++; return;
				}
				
				if(mod2 == 1)
				{
					lib.MoveWindow(handle, 0, height/2, width, height/2, true);
					lib.MoveWindow(handle2, 0, 0, width/2, height/2, true);
					lib.MoveWindow(handle3, width/2, 0, width/2, height/2, true);
					halfHoriz.setIcon(new ImageIcon("3_halfHoriz.png"));
					mod2--; return;
				}break;	 
		}
	}
	
	public void resetPics()
	{
		halfVert.setIcon(new ImageIcon("3_halfVert.png"));
		halfHoriz.setIcon(new ImageIcon("3_halfHoriz.png"));
		mod = 0; mod2 = 0; 
	}
	
	private class HandlerClass implements ActionListener
	{
		private int config;
		
		public HandlerClass(int configuration)
		{
			config = configuration;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			handle = wndList.get(box.getSelectedIndex()).getHandle();
			handle2 = wndList.get(box2.getSelectedIndex()).getHandle();
			handle3 = wndList.get(box3.getSelectedIndex()).getHandle();
			resizeWnds(config); 
		}
	}
}