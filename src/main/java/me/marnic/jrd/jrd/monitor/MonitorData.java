package me.marnic.jrd.jrd.monitor;

/**
 * Copyright (c) 02.08.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class MonitorData {
    public int index;
    public int width;
    public int height;

    public String formatedSize;

    private String base64;

    /**
     *
     * @param index index to the ScreenRecorder.monitors array
     * @param width with of the display
     * @param height height of the display
     * @param base64 base64 encoded image of the screen
     */
    public MonitorData(int index, int width, int height, String base64) {
        this.index = index;
        this.width = width;
        this.height = height;
        this.formatedSize = width + " x " + height;
        this.base64 = base64;
    }

    /**
     * Returns the base64 data used by image.src on the /monitors page
     *
     * @return img data used by html img.src element
     */
    public String base64() {
        return "data:image/jpg;base64," + base64;
    }

    public String link() {
        return "/desktop?monIndex="+this.index;
    }
}
