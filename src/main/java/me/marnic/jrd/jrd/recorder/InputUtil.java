package me.marnic.jrd.jrd.recorder;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import static com.sun.jna.platform.win32.WinUser.INPUT.INPUT_MOUSE;

/**
 * Copyright (c) 05.08.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class InputUtil {
    /**
     *
     * @param x mouse pos x in normalized device coordinates (65535 = width of the primary monitor)
     * @param y mouse pos y in normalized device coordinates (65535 = height of the primary monitor)
     * @param type mouse operation type (https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-mouseinput)
     */
    public static void mouseAction(long x, long y, int type) {
        WinUser.INPUT[] inputs = new WinUser.INPUT[] {new WinUser.INPUT()};
        inputs[0].type = new WinDef.DWORD(INPUT_MOUSE);
        inputs[0].input.setType("mi");
        inputs[0].input.mi.dx = new WinDef.LONG(x);
        inputs[0].input.mi.dy = new WinDef.LONG(y);
        inputs[0].input.mi.dwFlags = new WinDef.DWORD(0x8000 | type);

        User32.INSTANCE.SendInput(new WinDef.DWORD(1),inputs,inputs[0].size());
    }
}
