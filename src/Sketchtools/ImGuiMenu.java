package Sketchtools;

import FlowAndNoise.NoiseDataContainer;
import FlowAndNoise.Sketch2;
import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiCond;
import imgui.type.ImInt;
import processing.core.PApplet;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

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

    protected void generateColorPalettePicker(ColorPalette palette){
        ImGui.setNextWindowSize(400, 200, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX() + 100, ImGui.getMainViewport().getPosY() + 100, ImGuiCond.Once);
        ImGui.begin("Color picker: " + palette.getName());
        palette.forEach((key, value) -> {
            if(ImGui.collapsingHeader(key)){
                if(ImGui.colorPicker3(key, palette.get(key))){
                    System.out.println("Color '" + key + "' changed to: " + Arrays.toString(palette.get(key)));
                }
            }
        });
        ImGui.end();
    }
}
