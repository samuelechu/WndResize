import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.sun.jna.*;

public class Resize4WndsGUI extends ResizeGUI
{
	private SelectWndsGUI mainGUI;
	private JPanel choiceBoxes, configButtons, bottomButtons;
	private JComboBox box, box2, box3, box4;
	
	private int prevHandle, handle, handle2, handle3, handle4;
	private ArrayList<Window> wndList;
	private String[] titles;
	
	public Resize4WndsGUI(SelectWndsGUI GUI, ArrayList<Window> list)
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
			JPanel Box4 = new JPanel();
		
		box = new JComboBox(titles);
		box2 = new JComboBox(titles); box2.setSelectedItem(titles[1]);
		box3 = new JComboBox(titles); box3.setSelectedItem(titles[2]);
		box4 = new JComboBox(titles); box4.setSelectedItem(titles[3]);
		
		Box.add(new JLabel("1")); Box.add(box);
    	Box2.add(new JLabel("2")); Box2.add(box2);
    	Box3.add(new JLabel("3")); Box3.add(box3);
    	Box4.add(new JLabel("4")); Box4.add(box4);
    	
    	choiceBoxes.add(Box); choiceBoxes.add(Box2); choiceBoxes.add(Box3); choiceBoxes.add(Box4);
		
		
		JButton equal = new JButton(new ImageIcon("4_equal.png"));
    	HandlerClass handler = new HandlerClass(0);
		equal.addActionListener(handler);
		
		configButtons = new JPanel();
		configButtons.add(choiceBoxes); configButtons.add(equal); 
		
		
		bottomButtons = new JPanel();
		bottomButtons.setLayout(new BorderLayout());
		bottomButtons.add(makeBottomButtons(wndList, mainGUI, false), BorderLayout.EAST);
		
		
    	add(configButtons, BorderLayout.CENTER);
    	add(bottomButtons, BorderLayout.SOUTH);
	}
	
	public void resizeWnds()
	{
		int[] handles = {handle, handle2, handle3, handle4};
		showWnds(handles);
		
		lib.MoveWindow(handle, 0, 0, width/2, height/2, true);
		lib.MoveWindow(handle2, width/2, 0, width/2, height/2, true);
		lib.MoveWindow(handle3, 0, height/2, width/2, height/2, true);
		lib.MoveWindow(handle4, width/2, height/2, width/2, height/2, true);	
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
			handle4 = wndList.get(box4.getSelectedIndex()).getHandle();
			resizeWnds(); 
		}
	}
}