package FlowAndNoise;

import Sketchtools.ColorPalette;
import Sketchtools.ImGuiMenu;
import Sketchtools.ImGuiThread;
import Sketchtools.Tools;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Configuration;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import processing.core.PApplet;

import java.util.Arrays;

public class Sketch2_GUI extends ImGuiMenu<Sketch2_GUI> {
    ImGuiIO io;
    NoiseDataContainer angleNoiseData;
    NoiseDataContainer intensityNoiseData;
    ColorPalette palette1;

    float[] BGColor_RGB;
    float[] ShadeColor_RGB;
    float[] LightColor_RGB;
    float[] DarkColor_RGB;

    Sketch2 sketch = null;
    public Sketch2_GUI(Sketch2 mainThread) {
        super(mainThread);
    }
    @Override
    protected void configure(Configuration config) {
        if(mainThread instanceof Sketch2 x) {
            sketch = x;
        }
        else{
            throw new Error("Main process is not of type Sketch2");
        }
        config.setTitle("Dear ImGui is Awesome!");
        config.setWidth(mainThread.displayWidth/2);
        config.setHeight(mainThread.displayHeight/2);

        fetchValues();
    }
    @Override
    public void process() {
        io = ImGui.getIO();
        io.setWantCaptureMouse(true);
        if(sketch == null)
            return;
        ImGui.begin("Main", ImGuiWindowFlags.AlwaysAutoResize);

        //Menu options to render for sketch2
        ImGui.text("Hello, World!");
        generateNoiseDataPanel(angleNoiseData, regenerateField());
        generateNoiseDataPanel(intensityNoiseData, regenerateField());
        generateColorPalettePicker(palette1);



        ImGui.end();
    }

    public void fetchValues(){
        angleNoiseData = sketch.getAngleNoiseData();
        intensityNoiseData = sketch.getIntensityNoiseData();

        palette1 = sketch.getPalette1();
    }

    Runnable regenerateField(){
        return () -> {
            sketch.generateFieldNoise(sketch.getField());
            sketch.distort();
            sketch.fullRedraw = true;
            Tools.ResetDrawers(sketch.drawers, sketch.field);
            sketch.getPostDrawers().clear();
            sketch.redraw();

        };

    }

}
