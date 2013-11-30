package com.johnsoft.library.swing.hook;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.WinUser.POINT;


public class MSLLHOOKSTRUCT extends Structure
{
    public POINT pt;
    public int   mouseData;
    public int   flags;
    public int   time;
    public ULONG_PTR dwExtraInfo;
}