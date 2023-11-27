package Sketchtools;
import FlowAndNoise.Sketch2_GUI;
import imgui.app.Application;
import processing.core.PApplet;

import java.lang.reflect.InvocationTargetException;

public class ImGuiThread extends Thread{

    PApplet mainThread;
    Class<? extends ImGuiMenu> menuClass;
    ImGuiMenu<? extends ImGuiMenu> menu;

    public ImGuiThread(PApplet mainThread, Class<? extends ImGuiMenu> menuClass) {
        this.mainThread = mainThread;
        this.menuClass = menuClass;
        try {
            this.menu = menuClass.getConstructor(mainThread.getClass()).newInstance(mainThread);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public void run(){
        Application.launch(menu);
    }

    public void requestFetch(){
        menu.fetchValues();
    }

    public ImGuiMenu<? extends ImGuiMenu> getMenu(){
        return menu;
    }
}
