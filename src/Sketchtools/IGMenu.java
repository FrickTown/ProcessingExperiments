package Sketchtools;

import java.util.Map;

import imgui.app.Application;
import imgui.app.Configuration;
import processing.core.PApplet;

public abstract class IGMenu<T> extends Application {
    protected PApplet mainThread;
    protected ImGuiThread thread;
    public abstract Map<String, ?> fetchValueMap();
    @Override
    protected abstract void configure(Configuration config);
    @Override
    public abstract void process();
    public abstract void fetchValues();
    
    public T getThis(){
        return (T) this;
    }
    
    public IGMenu(PApplet mainThread){
        this.mainThread = mainThread;
    }
    
}