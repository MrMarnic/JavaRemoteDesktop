package me.marnic.jrd.jrd.recorder;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Copyright (c) 01.08.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ScreenRecorder {

    public static ArrayList<WinUser.MONITORINFO> monitors = new ArrayList<>();
    private WinDef.HDC hdc;
    public int selectedMonitor;

    private int width,height;
    private WinDef.HDC created;

    private WinDef.HBITMAP hbDesktop;

    private WinGDI.BITMAPINFO bitmapInfo;

    private final int depth = 3;

    public ScreenRecorder() {
        this.hdc = new WinDef.HDC();
        this.selectedMonitor = 0;
    }

    /**
     * Adds all monitors to the static ScreenRecorder.monitors array
     */
    public static void getMonitors() {
        monitors.clear();
        WinDef.HDC hdc = User32.INSTANCE.GetDC(null);WinDef.LPARAM lparam = new WinDef.LPARAM();

        User32.INSTANCE.EnumDisplayMonitors(hdc,null, new WinUser.MONITORENUMPROC() {
            @Override
            public int apply(WinUser.HMONITOR hmonitor, WinDef.HDC hdc, WinDef.RECT rect, WinDef.LPARAM lparam) {

                WinUser.MONITORINFO info  = new WinUser.MONITORINFO();
                info.write();

                User32.INSTANCE.GetMonitorInfo(hmonitor,info);

                monitors.add(info);

                return 1;
            }
        },lparam);

        GDI32.INSTANCE.DeleteDC(hdc);
    }

    /**
     * Must be called before makeScreenshot(). It prepares all the necessary data reused for every screenshot
     */
    public void prepare() {
        this.hdc = User32.INSTANCE.GetDC(null);WinDef.LPARAM lparam = new WinDef.LPARAM();

        WinDef.HDC created = GDI32.INSTANCE.CreateCompatibleDC(hdc);

        WinUser.MONITORINFO info = monitors.get(this.selectedMonitor);

        int width = info.rcMonitor.right - info.rcMonitor.left;
        int height = info.rcMonitor.bottom - info.rcMonitor.top;

        WinDef.HBITMAP hbDesktop = GDI32.INSTANCE.CreateCompatibleBitmap(hdc,width,height);
        GDI32.INSTANCE.SelectObject(created,hbDesktop);

        GDI32.INSTANCE.BitBlt(created,0,0,width,height,hdc,info.rcMonitor.left,info.rcMonitor.top, GDI32.SRCCOPY);

        WinGDI.BITMAPINFOHEADER header = new WinGDI.BITMAPINFOHEADER();
        header.biWidth = width;
        header.biHeight = -height;
        header.biPlanes = 1;
        header.biBitCount = (short) (depth * 8);
        header.biCompression = 0;

        WinGDI.BITMAPINFO bitmapInfo = new WinGDI.BITMAPINFO();
        bitmapInfo.bmiHeader = header;
        header.write();
        bitmapInfo.write();

        this.created = created;
        this.bitmapInfo = bitmapInfo;
        this.hbDesktop = hbDesktop;
        this.width = width;
        this.height = height;
    }

    /**
     * Uses the native Windows Api to generate a screenshot
     * @return BufferedImage from image data
     */
    public BufferedImage makeScreenshot() {
        GDI32.INSTANCE.SelectObject(created,hbDesktop);

        WinUser.MONITORINFO info = monitors.get(this.selectedMonitor);

        GDI32.INSTANCE.BitBlt(created,0,0,width,height,hdc,info.rcMonitor.left,info.rcMonitor.top, GDI32.SRCCOPY);

        int bufSize = width * height * depth;
        byte[] buffer = new byte[bufSize];

        final Pointer lpBitsMaskPtr = new Memory(buffer.length);

        int result = GDI32.INSTANCE.GetDIBits(created,hbDesktop,0,height,lpBitsMaskPtr,bitmapInfo,0);
        lpBitsMaskPtr.read(0,buffer,0,buffer.length);

        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for(int y = 0;y<height;y++) {
            for(int x = 0;x<width;x++) {
                Color c = new Color((int)(buffer[(y*width)  * depth+ x  * depth + 2]&0xff),(int)(buffer[(y*width)  * depth + x  * depth + 1]&0xff),(int)(buffer[(y*width)  * depth + x  * depth]&0xff)/*,(int)(buffer[(y*width)  * depth + x  * depth + 3]&0xff)*/);
                image.setRGB(x,y,c.getRGB());
            }
        }

        return image;
    }

    public void clear() {
        GDI32.INSTANCE.DeleteObject(hbDesktop);
        GDI32.INSTANCE.DeleteDC(created);
        GDI32.INSTANCE.DeleteDC(hdc);
    }

    public static String imageToBase64String(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(os.toByteArray());
    }

}
