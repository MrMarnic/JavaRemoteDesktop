package me.marnic.jrd.jrd.websocket;

/**
 * Copyright (c) 02.08.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ScreenMessage {
    public String encodedMessage;

    /**
     *
     * @param encodedMessage base64 encoded image
     */
    public ScreenMessage(String encodedMessage) {
        this.encodedMessage = encodedMessage;
    }
}
