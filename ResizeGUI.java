import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.sun.jna.*;

public class ResizeGUI extends JFrame
{
	Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	protected int width = (int)screenSize.getWidth();
	protected int height = (int)screenSize.getHeight();
	
	protected User32 lib = (User32) Native.loadLibrary("user32", User32.class);
	 
	public ResizeGUI()
	{
		super("Resize Windows - WndResize");
	}
	
	public String[] getTitles(ArrayList<Window> wndList)
	{
		String[] titles = new String[wndList.size()];
		
		for(int i = 0; i < wndList.size(); i++)
		{
			titles[i] = wndList.get(i).getTitle();
		}
		
		return titles;
	}
	
	public void minAllWnds(ArrayList<Window> wndList)
	{
		for(int i = 0; i < wndList.size(); i++)
			lib.CloseWindow(wndList.get(i).getHandle());
	}
	
	public void showWnds(int[] handles)
	{
		for(int i = 0; i < handles.length; i++)
		{
			lib.ShowWindow(handles[i], 9);
		}
	}
	
	public JPanel makeBottomButtons(ArrayList<Window> list, SelectWndsGUI GUI, boolean isCustomGUI)
	{
		JPanel bottomButtons = new JPanel();
		bottomButtons.setBorder(new EmptyBorder(0, 0, 10, 10));
		
		if(isCustomGUI)
			bottomButtons.setBorder(new EmptyBorder(10, 0, 10, 10));
			
		bottomButtons.setLayout(new BoxLayout(bottomButtons, BoxLayout.X_AXIS ));
		
		JButton exitButton = new JButton("   Exit   ");
		exitButton.addActionListener(new ActionListener()
		{
 			public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
		
		JButton backButton = new JButton("  Back  ");
		backButton.addActionListener(new BackButton(list, GUI));
		
		bottomButtons.add(backButton); bottomButtons.add(new JLabel(" ")); bottomButtons.add(exitButton);
		
		return bottomButtons;
	}
	
	private class BackButton implements ActionListener
	{
		private ArrayList<Window> wndList;
		private SelectWndsGUI mainGUI;
		
		public BackButton(ArrayList<Window> list, SelectWndsGUI GUI)
		{
			wndList = list;
			mainGUI = GUI;
		}
			
		public void actionPerformed(ActionEvent event)
		{
			minAllWnds(wndList);
			mainGUI.setVisible(true);
			dispose();
		}	
	}
}