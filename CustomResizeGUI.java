import java.awt.Dimension;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.sun.jna.*;

public class CustomResizeGUI extends ResizeGUI
{
	private User32 lib = (User32) Native.loadLibrary("user32", User32.class);
	private ArrayList<Window> wndList; 
	private SelectWndsGUI mainGUI;
	private CustomResizeGUI resizeGUI = this;
	private JPanel topContainer, bottomButtons;
	private JComboBox box;
	
	private double[] modSize = {.25, 1/3., .50, 2/3., .75, 1.};
	private double[] modPos = {0., .25, 1/3., .5, 2/3., .75};
	private int[] posInfo;
	
	private String[] titles;
	private Window selectedWnd;
	
	public CustomResizeGUI(SelectWndsGUI GUI, ArrayList<Window> wndlist)
	{
		setLayout(new BorderLayout());
		mainGUI = GUI;
		wndList = wndlist;
		titles = getTitles(wndList);
		minAllWnds(wndList);
		
		makeTopButtons();
		
		box = new JComboBox(titles);
		box.setBorder(new EmptyBorder(10, 20, 0, 20));
		box.addItemListener(new BoxClass());
		
		bottomButtons = new JPanel();
		bottomButtons.setLayout(new BorderLayout());
		bottomButtons.add(makeBottomButtons(wndList, mainGUI, true), BorderLayout.EAST);
		
		add(topContainer, BorderLayout.NORTH);
		add(box, BorderLayout.CENTER);
		add(bottomButtons, BorderLayout.SOUTH);
		
		overrideComponentBindings(this);
	}
	
	public void overrideComponentBindings(JFrame frame)
	{
		for(Component c : getAllComponents(frame))  
		{
			try
			{
				JComponent comp = (JComponent)c;
				for(KeyBinding binding: KeyBinding.values())
				{
					comp.getInputMap().put(binding.keyStroke, binding.text);
					comp.getActionMap().put(binding.text, new overrideBind(binding.config));
				}
			}
			
			catch (ClassCastException e)
			{
			   System.out.print("");
			}
		}
	}
	
	public ArrayList<Component> getAllComponents(Container c)
	{
    	Component[] comps = c.getComponents();
    	ArrayList<Component> compList = new ArrayList<Component>();
    	
    	for (Component comp : comps)
    	{
      		compList.add(comp);
      		if(comp instanceof Container) 
      		{
        		compList.addAll(getAllComponents((Container)comp));
      		}
    	}
    	return compList;
  	}
	
	public void makeTopButtons()
	{
		JButton helpButton = new JButton(new ImageIcon("question_mark.png"));
		helpButton.setPreferredSize(new Dimension(24, 24));
		
		helpButton.setOpaque(false);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorderPainted(false);
		helpButton.setToolTipText("Help");
		  
		helpButton.addActionListener(new ActionListener()
		{
 			public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, new ImageIcon("instructions.png"), "Rearranging Windows - WndResize", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        topContainer = new JPanel();
		topContainer.setLayout(new BorderLayout());
		topContainer.add(helpButton, BorderLayout.EAST);	
	}
	
	//0 - x, 1 - y, 2 - width, 3 - height
	public void incrementMod(int config)
	{
		switch(config)
		{
			case 0: if(posInfo[3] > 0)
						posInfo[3]--; break;
			
			case 1:	if(posInfo[3] < modSize.length - 1)
						posInfo[3]++; break;
		
			case 2:	if(posInfo[2] > 0)
	       				posInfo[2]--; break;
			
			case 3:	if(posInfo[2] < modSize.length - 1)
	       				posInfo[2]++; break;
	       	
	       	case 4: if(posInfo[0] > 0)
	       				posInfo[0]--; break;
			
			case 5:	if(posInfo[0] < modPos.length - 1)
	       				posInfo[0]++; break;
			
			case 6:	if(posInfo[1] > 0)
	       				posInfo[1]--; break;
			
			case 7:	if(posInfo[1] < modPos.length - 1)
	       				posInfo[1]++; break;
		}
		
		selectedWnd.setPosInfo(posInfo); 
	}
	
	private class BoxClass implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			if(event.getStateChange() == ItemEvent.SELECTED)
			{
				selectedWnd = wndList.get(box.getSelectedIndex());
				posInfo = selectedWnd.getPosInfo();
				
				lib.SetForegroundWindow(selectedWnd.getHandle());
				lib.ShowWindow(selectedWnd.getHandle(), 1);
				lib.BringWindowToTop(selectedWnd.getHandle());
				resizeGUI.toFront();
			}					 
		}
	}
	
	private class overrideBind extends AbstractAction
   	{
      	private int config;
      	public overrideBind(int configuration) 
      	{
        	config = configuration;
      	}

      	public void actionPerformed(ActionEvent e) 
      	{
         	incrementMod(config);
         	lib.MoveWindow(selectedWnd.getHandle(), (int)(width * modPos[posInfo[0]]), (int)(height * modPos[posInfo[1]]), 
         		(int)(width * modSize[posInfo[2]]), (int)(height * modSize[posInfo[3]]), true);
      	}
   	}
	
	enum KeyBinding
	{
	    UP("verticalDecrease", 0, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0)),
	    DOWN("verticalIncrease", 1, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0)),
	    LEFT("horizontalDecrease", 2, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0)),
	    RIGHT("horizontalIncrease", 3, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0)),
	    MOVELEFT("moveLeft", 4, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)),
	    MOVERIGHT("moveRight", 5, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0)),
	    MOVEUP("moveUp", 6, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)),
	    MOVEDOWN("moveDown", 7, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
		
		private String text;
		private int config;
		private KeyStroke keyStroke;
		
		KeyBinding(String Text, int num, KeyStroke keystroke)
		{
	  		this.text = Text;
	  		this.config = num;
	  		this.keyStroke = keystroke;
		}
	}
}