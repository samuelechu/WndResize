public class Window
{
	private String title; 
	private int handle;
	private int[] posInfo = new int[4]; 
	
	public Window(String wndname, int hwnd)
	{
		title = wndname;
		handle = hwnd;
	}
	
	public String getTitle()
	{
		if(title.length() > 80)
			return "..." + title.substring(title.length() - 75);
		return title;
	}
	
	public void setTitle(String newTitle)
	{
		title = newTitle;
	}
	
	public int getHandle()
	{
		return handle;
	}
	
	public int[] getPosInfo()
	{
		return posInfo;
	}
	
	//x, y, width, height
	public void setPosInfo(int[] Info)
	{
		posInfo[0] = Info[0];
		posInfo[1] = Info[1];
		posInfo[2] = Info[2];
		posInfo[3] = Info[3];
	}
	
	
}