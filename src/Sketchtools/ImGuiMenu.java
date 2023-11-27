package Sketchtools;

import FlowAndNoise.Sketch2;
import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.type.ImInt;
import processing.core.PApplet;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public abstract class ImGuiMenu<T> extends Application {
    protected PApplet mainThread;
    protected ImGuiThread thread;
    @Override
    protected abstract void configure(Configuration config);
    @Override
    public abstract void process();
    public abstract void fetchValues();

    public ImGuiMenu(PApplet mainThread){
        this.mainThread = mainThread;
    }

    public T getThis(){
        return (T) this;
    }
}
