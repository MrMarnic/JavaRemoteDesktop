package me.marnic.jrd.jrd.monitor;

import com.sun.jna.platform.win32.WinUser;
import me.marnic.jrd.jrd.desktop.OpenedPath;
import me.marnic.jrd.jrd.recorder.ScreenRecorder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 02.08.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Controller
public class MonitorController {

    @GetMapping("/monitors")
    public String monitors(Model model)  {
        ScreenRecorder recorder = new ScreenRecorder();
        ScreenRecorder.getMonitors();
        recorder.prepare();

        List<MonitorData> mons = new ArrayList<>();

        int index = 0;

        for(WinUser.MONITORINFO info : recorder.monitors) {
            recorder.selectedMonitor = index;
            recorder.prepare();
            BufferedImage image = recorder.makeScreenshot();
            mons.add(new MonitorData(index,info.rcMonitor.right-info.rcMonitor.left,info.rcMonitor.bottom-info.rcMonitor.top,ScreenRecorder.imageToBase64String(image)));
            index++;
        }

        recorder.clear();

        model.addAttribute("monitors", mons);
        model.addAttribute("pcname", OpenedPath.getComputerName());
        return "monitors";
    }
}
