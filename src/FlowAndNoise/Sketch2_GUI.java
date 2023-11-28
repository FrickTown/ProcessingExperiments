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
    ImInt intensitySeed;
    ImInt iLod;
    ImFloat iFallOff;
    ImInt angleSeed;
    ImInt aLod;
    ImFloat aFallOff;
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

        angleSeed = new ImInt();
        aLod = new ImInt();
        aFallOff = new ImFloat();

        intensitySeed = new ImInt();
        iLod = new ImInt();
        iFallOff = new ImFloat();

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

        generateColorPalettePicker(palette1);

        ImGui.end();
    }

    public void fetchValues(){
        angleNoiseData = sketch.getAngleNoiseData();
        angleSeed.set((int) angleNoiseData.getSeed());
        aFallOff.set(angleNoiseData.getFallOff());
        aLod.set(angleNoiseData.getLod());

        intensityNoiseData = sketch.getIntensityNoiseData();
        intensitySeed.set((int) intensityNoiseData.getSeed());
        iFallOff.set(intensityNoiseData.getFallOff());
        iLod.set(intensityNoiseData.getLod());

        palette1 = sketch.getPalette1();
    }

    void regenerateField(){
        angleNoiseData.updateAll(
                angleSeed.longValue(),
                aFallOff.get(),
                aLod.get()
        );
        intensityNoiseData.updateAll(
                intensitySeed.longValue(),
                iFallOff.get(),
                iLod.get()
        );
        sketch.generateFieldNoise(sketch.getField());
        sketch.distort();
        sketch.fullRedraw = true;
        Tools.ResetDrawers(sketch.drawers, sketch.field);
        sketch.getPostDrawers().clear();
        sketch.redraw();
    }

}
