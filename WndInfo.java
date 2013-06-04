import java.util.*;
import com.sun.jna.*;

public class WndInfo implements WndEnumProc
{
	private User32 lib = (User32) Native.loadLibrary("user32", User32.class);
	ArrayList<Window> wndList = new ArrayList<Window>();
	private byte[] title = new byte[242]; 
	private int[] pos = new int[242];
	 

	public boolean callback(int hwnd, long lParam)
	{
		lib.GetWindowRect(hwnd, pos);
		if(isValidTitle(hwnd) && lib.IsWindowVisible(hwnd))
		{
			lib.GetWindowTextA(hwnd, title, 242);
			
			 
			System.out.println(Native.toString(title) + ": " + hwnd);
			System.out.println("  coords: (" + pos[0] + "," + pos[1] + ") , (" + pos[2] + "," + pos[3] +")\n");
			
			wndList.add(new Window(Native.toString(title), hwnd));
		}	
		title = new byte[242];
		pos = new int[242];
		return true;
	}
	
	public ArrayList<Window> getWndList()
	{
		wndList.add(0, new Window("Select Window", -1));
		return markDuplicates(wndList); 
	}
	
	public boolean isValidTitle(int hwnd)
	{
		return lib.GetWindowTextA(hwnd, title, 242) != 0 && !Native.toString(title).equals("Program Manager") && !Native.toString(title).equals("Start") && !Native.toString(title).equals("Select Windows - WndResize");
	}
	
	public ArrayList<Window> markDuplicates(ArrayList<Window> wndList)
	{
		int counter = 2; 
		String title;
		
		for(int i = 0; i < wndList.size(); i++)
		{
			for(int j = i + 1; j < wndList.size(); j++)
			{
				title = wndList.get(j).getTitle();
				if(wndList.get(i).getTitle().equals(title))
				{
					wndList.get(j).setTitle(title + " (" + counter + ")" );
					counter++; 
				}
			}
			if(counter != 2)
				wndList.get(i).setTitle(wndList.get(i).getTitle() + " (1)" );
			counter = 2;	 
		}
		
		return wndList;
	}
}