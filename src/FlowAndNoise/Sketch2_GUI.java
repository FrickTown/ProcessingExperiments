package FlowAndNoise;

import Sketchtools.ImGuiMenu;
import Sketchtools.ImGuiThread;
import Sketchtools.Tools;
import imgui.ImGui;
import imgui.app.Configuration;
import imgui.type.ImInt;
import processing.core.PApplet;

public class Sketch2_GUI extends ImGuiMenu<Sketch2_GUI> {
    ImInt angleSeed;
    ImInt intensitySeed;
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
        config.setWidth(mainThread.displayWidth/3);
        config.setHeight(mainThread.displayHeight/3);

        angleSeed = new ImInt();
        angleSeed.set((int) sketch.getAngleSeed());
        intensitySeed = new ImInt();
        intensitySeed.set((int) sketch.getIntensitySeed());
    }

    @Override
    public void process() {
        if(sketch == null)
            return;
        //Menu options to render for sketch2
        ImGui.text("Hello, World!");
        ImGui.text("Total lines = " + (sketch.getDrawers().size() + sketch.getPostDrawers().size()));
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
    }

    public void fetchValues(){
        angleSeed.set((int) sketch.getAngleSeed());
        intensitySeed.set((int) sketch.getIntensitySeed());
    }
}
