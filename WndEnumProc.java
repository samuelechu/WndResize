import com.sun.jna.win32.StdCallLibrary.*;

public interface WndEnumProc extends StdCallCallback
{
	boolean callback(int hwnd, long lParam);
}