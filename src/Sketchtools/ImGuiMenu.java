package Sketchtools;

import FlowAndNoise.NoiseDataContainer;
import FlowAndNoise.Sketch2;
import RainbowWorms.*;
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
import java.util.function.Function;

public abstract class ImGuiMenu<T> extends IGMenu<ImGuiMenu> {

    public ImGuiMenu(PApplet mainThread){
        super(mainThread);
        this.mainThread = mainThread;
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
    float[] slid = new float[3];
    protected void generateNoiseDataPanel(NoiseDataContainer data, Runnable callBack){
        ImGui.setNextWindowSize(400, 200, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX() + 100 + mainThread.random(200), ImGui.getMainViewport().getPosY() + 200 + mainThread.random(100), ImGuiCond.Once);
        ImGui.begin("Noise data - " + data.getName());
        ImGui.inputInt("Seed", data.getSeed());
        ImGui.inputInt("Octave", data.getLod());

        if(ImGui.sliderFloat("test", data.getFallOff().getData(), 0, 1))
            System.out.println(data.getFallOff().get());
        ImGui.inputFloat("Falloff", data.getFallOff(), 0.01f);
        if(ImGui.button("Regenerate Field")){
            callBack.run();
        }
        ImGui.end();
    }
}
