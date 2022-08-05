package me.marnic.jrd.jrd.websocket;

/**
 * Copyright (c) 03.08.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class MouseActionMessage {

    public int index;
    public MouseActionType type;
    public float percentageX;
    public float percentageY;

    /**
     *
     * @param type MouseActionType enum
     * @param percentageX percentage of screen width specified by index (ScreenRecorder.monitors)
     * @param percentageY percentage of screen height specified by index (ScreenRecorder.monitors)
     * @param index ScreenRecorder.monitors
     */
    public MouseActionMessage(MouseActionType type, float percentageX, float percentageY, int index) {
        this.type = type;
        this.percentageX = percentageX;
        this.percentageY = percentageY;
        this.index = index;
    }

    public enum MouseActionType {
        MOUSE_DOWN,
        MOUSE_UP,
        MOVE
    }
}
