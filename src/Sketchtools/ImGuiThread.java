package Sketchtools;
import FlowAndNoise.Sketch2_GUI;
import imgui.app.Application;
import processing.core.PApplet;

import java.lang.reflect.InvocationTargetException;

public class ImGuiThread extends Thread{

    PApplet mainThread;
    Class<? extends ImGuiMenu> menuClass;
    public ImGuiThread(PApplet mainThread, Class<? extends ImGuiMenu> menuClass) {
        this.mainThread = mainThread;
        this.menuClass = menuClass;
    }
    public void run(){
        try {
            ImGuiMenu.main(mainThread, menuClass);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
