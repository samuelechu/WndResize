import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.sun.jna.*;

public class SelectWndsGUI extends JFrame
{
	private User32 lib = (User32) Native.loadLibrary("user32", User32.class);
	private ArrayList<Window> wndList;
	SelectWndsGUI mainGUI = this;
	
	private JPanel topContainer, choiceBoxes, bottomContainer;
	private JComboBox box, box2, box3, box4;
	
	private String[] titles;
	private int prevHandle;
	
	public SelectWndsGUI()
	{
		super("Select Windows - WndResize");
		setLayout(new BorderLayout());
		makeWndList();
		titles = getTitles();
		minAllWnds();
		
		makeTopButtons();
		
		choiceBoxes = new JPanel();		
		choiceBoxes.setLayout(new BoxLayout(choiceBoxes, BoxLayout.Y_AXIS ));
			JPanel Box = new JPanel();
			JPanel Box2 = new JPanel();
			JPanel Box3 = new JPanel();
			JPanel Box4 = new JPanel();
		
		box = new JComboBox(titles);
		box.addItemListener(new BoxClass(box));
		
		box2 = new JComboBox(titles);
		box2.addItemListener(new BoxClass(box2));
		
		box3 = new JComboBox(titles);
		box3.addItemListener(new BoxClass(box3));
		
		box4 = new JComboBox(titles);
		box4.addItemListener(new BoxClass(box4));
		
		Box.add(new JLabel("1")); Box.add(box);
    	Box2.add(new JLabel("2")); Box2.add(box2);
    	Box3.add(new JLabel("3")); Box3.add(box3);
    	Box4.add(new JLabel("4")); Box4.add(box4);
    	
    	choiceBoxes.add(Box); choiceBoxes.add(Box2); choiceBoxes.add(Box3); choiceBoxes.add(Box4);
    	
    	makeBottomButtons();
    	
 		add(topContainer, BorderLayout.NORTH);
		add(choiceBoxes, BorderLayout.CENTER);
		add(bottomContainer, BorderLayout.SOUTH);
	}
	
	public void makeWndList()
	{
		WndInfo info = new WndInfo();
		lib.EnumWindows(info, 0);
		wndList = info.getWndList();
		
		titles = getTitles();
	}
	
	public void makeTopButtons()
	{
		JButton refreshButton = new JButton(new ImageIcon("refresh_icon.png"));
		refreshButton.setPreferredSize(new Dimension(24, 24));
		
		refreshButton.setOpaque(false);
		refreshButton.setContentAreaFilled(false);
		refreshButton.setBorderPainted(false);
		refreshButton.setToolTipText("Refresh window list");
		 
		refreshButton.addActionListener(new ActionListener()
		{
 			public void actionPerformed(ActionEvent e)
            {
                SelectWndsGUI mainGUI = new SelectWndsGUI();
				mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainGUI.setSize(560,245);
				mainGUI.setVisible(true);
				mainGUI.setAlwaysOnTop(true);
				dispose();
            }
        });
        
        topContainer = new JPanel();
		topContainer.setLayout(new BorderLayout());
		topContainer.add(refreshButton, BorderLayout.EAST);	
	}
	
	public void makeBottomButtons()
	{
		JPanel customPanel = new JPanel();
		customPanel.setBorder(new EmptyBorder(0, 5, 5, 0)); 
		
		JButton customButton = new JButton("Custom");
		customButton.addActionListener(new CustomClass());
		customButton.setToolTipText("Arrange windows manually");
		customPanel.add(customButton);
		
		JButton processButton = new JButton("    OK    ");
		processButton.addActionListener(new HandlerClass());
		processButton.setToolTipText("Select at least two windows");
		
		JButton exitButton = new JButton("   Exit   ");
		exitButton.addActionListener(new ActionListener()
		{
 			public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        
		JPanel bottomButtons = new JPanel();
		bottomButtons.setBorder(new EmptyBorder(0, 0, 5, 5));
		bottomButtons.setLayout(new BoxLayout(bottomButtons, BoxLayout.X_AXIS ));
		bottomButtons.add(processButton); bottomButtons.add(new JLabel(" ")); bottomButtons.add(exitButton);
		
		bottomContainer = new JPanel();
		bottomContainer.setLayout(new BorderLayout());
		bottomContainer.add(bottomButtons, BorderLayout.EAST);
		bottomContainer.add(customPanel, BorderLayout.WEST);
	}
		
	public void minAllWnds()
	{
		for(int i = 0; i < wndList.size(); i++)
			lib.CloseWindow(wndList.get(i).getHandle());
	}
	
	public String[] getTitles()
	{
		String[] titles = new String[wndList.size()];
		titles[0] = "Select Window";
		
		for(int i = 1; i < wndList.size(); i++)
			titles[i] = wndList.get(i).getTitle();
		
		return titles;
	}
	
	public ArrayList<Window> getJBoxInfo()
	{
		ArrayList<Window> list = new ArrayList<Window>();
		
		list.add(wndList.get(box.getSelectedIndex()));
		list.add(wndList.get(box2.getSelectedIndex()));
		list.add(wndList.get(box3.getSelectedIndex()));
		list.add(wndList.get(box4.getSelectedIndex()));
		
		for(int i = list.size() - 1; i >= 0; i--)
			if(list.get(i).getHandle() == -1)
				list.remove(i);
		
		if(list.size() != 1)
			lib.CloseWindow(prevHandle);
			
		return list;	 
	}
	
	public void configGUI(ResizeGUI GUI)
	{
		GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GUI.setVisible(true);
		GUI.setAlwaysOnTop(true);
		GUI.setResizable(false);	
	}
	
	private class BoxClass implements ItemListener
	{
		private JComboBox box;
		
		public BoxClass(JComboBox Jbox)
		{
			box = Jbox;
		}
		
		public void itemStateChanged(ItemEvent event)
		{
			if(event.getStateChange() == ItemEvent.SELECTED)
			{
				lib.CloseWindow(prevHandle);
				prevHandle = wndList.get(box.getSelectedIndex()).getHandle();
				lib.ShowWindow(prevHandle, 9);
			}					 
		}
	}
	
	private class HandlerClass implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			ArrayList<Window> selectedWnds = getJBoxInfo();
			int numWindows = selectedWnds.size();
			
			if(numWindows > 1)
				mainGUI.setVisible(false);
				
			switch(numWindows)
			{
				case 2:
					Resize2WndsGUI twoWndsGUI = new Resize2WndsGUI(mainGUI, selectedWnds);
					twoWndsGUI.setSize(530,270);
					configGUI(twoWndsGUI); break;
				
				case 3:
					Resize3WndsGUI threeWndsGUI = new Resize3WndsGUI(mainGUI, selectedWnds);
					threeWndsGUI.setSize(575,470);
					configGUI(threeWndsGUI); break;
				
				case 4:
					Resize4WndsGUI fourWndsGUI = new Resize4WndsGUI(mainGUI, selectedWnds);
					fourWndsGUI.setSize(820, 225);
					configGUI(fourWndsGUI); break;
			}	 
		}
	}
	
	private class CustomClass implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			mainGUI.setVisible(false);
			CustomResizeGUI customGUI = new CustomResizeGUI(mainGUI, wndList); 
			customGUI.setSize(530,130);
			configGUI(customGUI);
		}
	} 
}