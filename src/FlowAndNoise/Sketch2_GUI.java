package FlowAndNoise;

import Sketchtools.ColorPalette;
import Sketchtools.ImGuiMenu;
import Sketchtools.ImGuiThread;
import Sketchtools.Tools;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Configuration;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import processing.core.PApplet;

import java.util.Arrays;

public class Sketch2_GUI extends ImGuiMenu<Sketch2_GUI> {
    ImGuiIO io;
    ImInt angleSeed;
    ImInt intensitySeed;
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
        intensitySeed = new ImInt();

        fetchValues();
    }
    boolean lastClicked = false;
    @Override
    public void process() {
        io = ImGui.getIO();
        io.setWantCaptureMouse(true);
        if(sketch == null)
            return;
        ImGui.begin("Main", ImGuiWindowFlags.AlwaysAutoResize);

        //Menu options to render for sketch2
        ImGui.text("Hello, World!");
        ImGui.inputInt("angleSeed", angleSeed);
        ImGui.inputInt("intensitySeed", intensitySeed);
        if(ImGui.button("Go")){
            sketch.generateFieldNoise(sketch.getField(), angleSeed.longValue(), intensitySeed.longValue());
            sketch.distort();
            sketch.fullRedraw = true;
            Tools.ResetDrawers(sketch.drawers, sketch.field);
            sketch.getPostDrawers().clear();
            sketch.redraw();
        }
        if(ImGui.collapsingHeader("Colors")){
            if(ImGui.collapsingHeader("BGColor")){
                if(ImGui.colorPicker3("BGColor", palette1.get("BGColor"))){
                    System.out.println("Color changed: " + Arrays.toString(palette1.get("BGColor")));
                }
            }
            if(ImGui.collapsingHeader("ShadeColor")){
                if(ImGui.colorPicker3("ShadeColor", palette1.get("ShadeColor"))){
                    System.out.println("Color changed: " + Arrays.toString(palette1.get("ShadeColor")));
                }
            }

            if(ImGui.collapsingHeader("LightColor")){
                if(ImGui.colorPicker3("LightColor", palette1.get("LightColor"))){
                    if(ImGui.isMouseDown(0)){
                        lastClicked = true;
                        System.out.println("Mouse pressed");
                    }
                    //Funkar inte...
                    if(!ImGui.isMouseDown(0) && lastClicked){
                        lastClicked = false;
                        System.out.println("Mouse released");
                    }
                    System.out.println("Color changed: " + Arrays.toString(palette1.get("LightColor")));
                }
            }
            if(ImGui.collapsingHeader("DarkColor")){
                if(ImGui.colorPicker3("DarkColor", palette1.get("DarkColor"))){
                    System.out.println("Color changed: " + Arrays.toString(palette1.get("DarkColor")));
                }
            }
        }

        ImGui.end();
    }

    public void fetchValues(){
        angleSeed.set((int) sketch.getAngleSeed());
        intensitySeed.set((int) sketch.getIntensitySeed());
        palette1 = sketch.getPalette1();
    }
}
