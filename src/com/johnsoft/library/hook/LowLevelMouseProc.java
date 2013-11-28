package com.johnsoft.library.hook;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;

public interface LowLevelMouseProc extends HOOKPROC
{
    public LRESULT callback(int nCode, WPARAM wParam, MSLLHOOKSTRUCT lParam);
}