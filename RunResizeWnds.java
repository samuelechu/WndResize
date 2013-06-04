import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.sun.jna.*;

public class RunResizeWnds
{
	public static void main(String[] args)
	{
		SelectWndsGUI mainGUI = new SelectWndsGUI();
		mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainGUI.setSize(560,245);
		mainGUI.setVisible(true);
		mainGUI.setAlwaysOnTop(true);
	}
}