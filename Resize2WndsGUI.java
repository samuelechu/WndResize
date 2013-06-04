import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.sun.jna.*;

public class Resize2WndsGUI extends ResizeGUI
{
	private SelectWndsGUI mainGUI;
	private JPanel choiceBoxes, configButtons, bottomButtons;
	private JComboBox box, box2;
	private JButton vert, horiz;
	
	private int prevHandle, handle, handle2, mod, mod2;
	private ArrayList<Window> wndList;
	private String[] titles;
	
	public Resize2WndsGUI(SelectWndsGUI GUI, ArrayList<Window> list)
	{
		setLayout(new BorderLayout());
		mainGUI = GUI;
		wndList = list;
		titles = getTitles(wndList);
		
		
		choiceBoxes = new JPanel();		
		choiceBoxes.setLayout(new BoxLayout(choiceBoxes, BoxLayout.Y_AXIS));
			JPanel Box = new JPanel();
			JPanel Box2 = new JPanel();
		
		box = new JComboBox(titles);
		box2 = new JComboBox(titles); box2.setSelectedItem(titles[1]);
		
		Box.add(new JLabel("1")); Box.add(box);
    	Box2.add(new JLabel("2")); Box2.add(box2);
    	
    	choiceBoxes.add(Box); choiceBoxes.add(Box2);
    	
		
		vert = new JButton(new ImageIcon("2_vert.png"));
    	HandlerClass handler = new HandlerClass(0);
		vert.addActionListener(handler);
		
		horiz = new JButton(new ImageIcon("2_horiz.png"));
    	HandlerClass handler2 = new HandlerClass(1);
		horiz.addActionListener(handler2);
		
		configButtons = new JPanel();
		configButtons.add(vert); configButtons.add(horiz);
		
			
		bottomButtons = new JPanel();
		bottomButtons.setLayout(new BorderLayout());
		bottomButtons.add(makeBottomButtons(wndList, mainGUI, false), BorderLayout.EAST);
    	
    	
    	add(choiceBoxes, BorderLayout.NORTH);
    	add(configButtons, BorderLayout.CENTER);
    	add(bottomButtons, BorderLayout.SOUTH);	
	}
	
	public void resizeWnds(int config)
	{
		int[] handles = {handle, handle2};
		showWnds(handles);
		
		switch(config) 
		{
			case 0: 
				if(mod == 0)
				{
					resetPics();
					lib.MoveWindow(handle, 0, 0, width/2, height, true);
					lib.MoveWindow(handle2, width/2, 0, width/2, height, true);
					vert.setIcon(new ImageIcon("2_vert2.png"));
					mod++; return;
				}
				
				if(mod == 1)
				{
					lib.MoveWindow(handle2, 0, 0, width/2, height, true);
					lib.MoveWindow(handle, width/2, 0, width/2, height, true);
					vert.setIcon(new ImageIcon("2_vert.png"));
					mod--; return;
				}break;
					
			case 1:
				if(mod2 == 0)
				{
					resetPics();
					lib.MoveWindow(handle, 0, 0, width, height/2, true);
					lib.MoveWindow(handle2, 0, height/2, width, height/2, true);
					horiz.setIcon(new ImageIcon("2_horiz2.png"));
					mod2++; return;
				}
				
				if(mod2 == 1)
				{
					lib.MoveWindow(handle, 0, height/2, width, height/2, true);
					lib.MoveWindow(handle2, 0, 0, width, height/2, true);
					horiz.setIcon(new ImageIcon("2_horiz.png"));
					mod2--; return;
				}break;	 
		}
	}
	
	public void resetPics()
	{
		vert.setIcon(new ImageIcon("2_vert.png"));
		horiz.setIcon(new ImageIcon("2_horiz.png"));
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
			resizeWnds(config); 
		}
	}
}