import com.sun.jna.*;
import com.sun.jna.win32.*;

//names functions from User32.dll that are used (not methods)
public interface User32 extends StdCallLibrary
{
	boolean EnumWindows(WndEnumProc wndenumproc, long lParam);
	boolean IsWindowVisible(int hwnd);
	boolean GetWindowRect(int hwnd, int[] lpRect);
	int GetWindowTextA(int hwnd, byte[] lpString, int nMaxCount);
	 
	boolean ShowWindow(int hwnd, int nCmdShow);
	boolean SetForegroundWindow(int hwnd); 
	boolean MoveWindow(int hwnd, int X, int Y, int nWidth, int nHeight, boolean bRepaint);
	boolean CloseWindow(int hwnd);
	boolean BringWindowToTop(int hwnd); 
}