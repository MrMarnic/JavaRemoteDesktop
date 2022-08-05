package me.marnic.jrd.jrd.desktop;

import java.util.Map;

/**
 * Copyright (c) 02.08.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Used to describe the opened monitor/display path.
 */
public class OpenedPath {
    public String path;

    public OpenedPath(int index) {
        this.path = getComputerName() + " / MONITOR " + index;
    }

    public static String getComputerName()
    {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }
}
