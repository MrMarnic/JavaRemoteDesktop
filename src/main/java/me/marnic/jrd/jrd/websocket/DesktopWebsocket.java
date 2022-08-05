package me.marnic.jrd.jrd.websocket;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import me.marnic.jrd.jrd.recorder.InputUtil;
import me.marnic.jrd.jrd.recorder.ScreenRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

import static com.sun.jna.platform.win32.WinUser.*;

/**
 * Copyright (c) 02.08.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Controller
public class DesktopWebsocket {

    public static HashMap<String,String> SOCKET_CONNECTED = new HashMap<>();
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/connect")
    public void connect(ConnectMessage msg, Principal p, StompHeaderAccessor headerAccessor) throws IOException {
        ScreenRecorder recorder = new ScreenRecorder();
        recorder.selectedMonitor = msg.monIndex;

        ScreenRecorder.getMonitors();
        recorder.prepare();

        while (SOCKET_CONNECTED.containsKey(p.getName()) && SOCKET_CONNECTED.get(p.getName()).equals(headerAccessor.getSessionId())) {
            BufferedImage image = recorder.makeScreenshot();

            String encoded = ScreenRecorder.imageToBase64String(image);

            simpMessagingTemplate.convertAndSendToUser(p.getName(),"/desktop/screen", new ScreenMessage(encoded));
            //simpMessagingTemplate.convertAndSend("/topic/greetings", new Greeting(encoded));
        }
    }

    /**
     * Handles all the different mouse event messages sent by client (MOUSE_UP, MOVE, MOUSE_DOWN)
     *
     * @param message MouseActionMessage sent by client
     * @param p currently loggedin user
     */
    @MessageMapping("/mouse")
    public void mouse(MouseActionMessage message, Principal p) {

        switch (message.type) {
            case MOVE -> {

                WinUser.MONITORINFO info = ScreenRecorder.monitors.get(message.index);

                int width = info.rcMonitor.right - info.rcMonitor.left;
                int height = info.rcMonitor.bottom - info.rcMonitor.top;

                long x = (long)(65535 * message.index + ((float)width/(float)User32.INSTANCE.GetSystemMetrics(SM_CXSCREEN) * message.percentageX * 65535.0));
                long y = (long) ((float)height/(float)User32.INSTANCE.GetSystemMetrics(SM_CYSCREEN) * message.percentageY * 65535.0);
                InputUtil.mouseAction(x,y,0x0001);
            }
            case MOUSE_UP -> {
                POINT point = new POINT();
                User32.INSTANCE.GetCursorPos(point);

                WinUser.MONITORINFO info = ScreenRecorder.monitors.get(message.index);

                int width = User32.INSTANCE.GetSystemMetrics(SM_CXSCREEN);
                int height = User32.INSTANCE.GetSystemMetrics(SM_CYSCREEN);

                LONG x = new LONG((point.x/width) * 65535);
                LONG y =new LONG( (point.y/height) * 65535);

                InputUtil.mouseAction((point.x/width) * 65535L,(point.y/height) * 65535L,0x0004);

            }
            case MOUSE_DOWN -> {
                WinUser.MONITORINFO info = ScreenRecorder.monitors.get(message.index);

                int width = info.rcMonitor.right - info.rcMonitor.left;
                int height = info.rcMonitor.bottom - info.rcMonitor.top;

                long x = (long) (65535 * message.index + ((float)width/(float)User32.INSTANCE.GetSystemMetrics(SM_CXSCREEN) * message.percentageX * 65535.0));
                long y = (long) ((float)height/(float)User32.INSTANCE.GetSystemMetrics(SM_CYSCREEN) * message.percentageY * 65535.0);

                InputUtil.mouseAction(x,y,0x0002);
            }
        }
    }
}
