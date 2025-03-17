package RainbowWorms;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Map;

import Sketchtools.IGMenu;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Configuration;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;

public class RBWMenu extends IGMenu<RBWMenu>{

    ImGuiIO io;
    RBW sketch;

    public RBWMenu(RBW mainThread){
        super(mainThread);
    }

    @Override
    protected void configure(Configuration config) {
        if(mainThread instanceof RBW x) {
            sketch = x;
        }
        else{
            throw new Error("Main process is not of type RBW");
        }
        config.setTitle("Frick's Rainbow Worms");
        config.setWidth(mainThread.displayWidth / 2);
        config.setHeight(mainThread.displayHeight / 2);
        // Find index of currently active easing function (declared before startup) and select it
        Arrays.sort(easingFunctions);
        posEasingSelection.set(Arrays.binarySearch(easingFunctions, sketch.activePosEasingFunction));
        sizeEasingSelection.set(Arrays.binarySearch(easingFunctions, sketch.activeSizeEasingFunction));
        timeEasingSelection.set(Arrays.binarySearch(easingFunctions, sketch.activeTimeEasingFunction));

        // Populate the menu
        rowCount.set(sketch.rows);
        colCount.set(sketch.columns);
        amp = new float[]{sketch.amp};
        timeScale = new float[]{sketch.timeScale};
        maxMaxRadius = new float[]{sketch.maxMaxRadius};
        minMaxRadius = new float[]{sketch.minMaxRadius};
        jointDiff = new float[]{sketch.jointDiff};
        apexRadius = new float[]{sketch.apexRadius};
        maxTimeOffset = new float[]{sketch.maxTimeOffset};
        colorOffsetInitScalar = new float[]{sketch.colorOffsetInitScalar};
        colorOffsetDiffScalar = new float[]{sketch.colorOffsetDiffScalar};
        globalTimeIncr = new float[]{sketch.globalTimeIncr};
    }
    
    // Global space
    ImInt posEasingSelection = new ImInt(0);
    ImInt sizeEasingSelection = new ImInt(0);
    ImInt timeEasingSelection = new ImInt(0);
    String[] easingFunctions = new String[]{
        "noEase", "easeOutExpo", "easeInOutExpo", "easeOutCirc", "easeInOutCirc",
        "easeInSine", "easeOutSine", "easeInOutSine", "easeOutQuad"
    };
    ImInt rowCount = new ImInt(0);
    ImInt colCount = new ImInt(0);

    // Sorting
    ImInt sortMethodSelection = new ImInt(0);
    String[] sortMethods = new String[]{"Left to Right, Top to Bottom", "Radial"};
    ImBoolean sortOrder = new ImBoolean(false);
    
    // Color
    ImInt colorMode = new ImInt(0);
    String[] colorModes = new String[]{"Rainbow", "Gradient", "Solid/Saturation"};
    // Worm space
    float[] timeScale; 
    float[] amp; // Max amp
    float[] maxMaxRadius;
    float[] minMaxRadius;
    float[] jointDiff;
    float[] apexRadius;
    float[] maxTimeOffset;
    float[] colorOffsetInitScalar;
    float[] colorOffsetDiffScalar;
    float[] globalTimeIncr;

    @Override
    public void process() {
        io = ImGui.getIO();
        //io.setWantCaptureMouse(true);
        //io.setWantCaptureKeyboard(true);
        ImGui.begin("Worldspace", ImGuiWindowFlags.AlwaysAutoResize);

            //Menu options to render for RBW
            if(ImGui.inputInt("Rows", rowCount)){
                sketch.rows = rowCount.get();
                sketch.Worms = sketch.GenerateWorms();
            }
            
            if(ImGui.inputInt("Columns",colCount)){
                sketch.columns = colCount.get();
                sketch.Worms = sketch.GenerateWorms();
            }

            if(ImGui.sliderFloat("Global time increment", globalTimeIncr, 0.001f, 1.5f)){
                sketch.globalTimeIncr = globalTimeIncr[0];
                sketch.Worms = sketch.GenerateWorms();
            }
            
            if(ImGui.sliderFloat("Maximal worm time offset", maxTimeOffset, 0.1f, 10f)){
                sketch.maxTimeOffset = (float) maxTimeOffset[0];
                sketch.Worms = sketch.GenerateWorms();
            }
            if(ImGui.sliderFloat("Maximum radius", maxMaxRadius, 0.1f, 500f)){
                sketch.maxMaxRadius = (float) maxMaxRadius[0];
                sketch.Worms = sketch.GenerateWorms();
            }

            if(ImGui.sliderFloat("Minmax radius", minMaxRadius, 0.1f, 100f)){
                sketch.minMaxRadius = (float) minMaxRadius[0];
                sketch.Worms = sketch.GenerateWorms();
            }
            ImGui.beginGroup();
                // Global easing function selections
                if(ImGui.combo("Positional easing function", posEasingSelection, easingFunctions)){
                    sketch.activePosEasingFunction = easingFunctions[posEasingSelection.get()];
                    sketch.Worms = sketch.GenerateWorms();
                }
                if(ImGui.combo("Worm size easing function", sizeEasingSelection, easingFunctions)){
                        sketch.activeSizeEasingFunction = easingFunctions[sizeEasingSelection.get()];
                        sketch.Worms = sketch.GenerateWorms();
                    }
                if(ImGui.combo("Worm time easing function", timeEasingSelection, easingFunctions)){
                    sketch.activeTimeEasingFunction = easingFunctions[sizeEasingSelection.get()];
                    sketch.Worms = sketch.GenerateWorms();
                }
            ImGui.endGroup();
            
            ImGui.begin("Sorting");
                if(ImGui.combo("Sorting method", sortMethodSelection, sortMethods)){
                    sketch.SortFlag = new SimpleEntry <Boolean, int[]>(true, new int[]{sortMethodSelection.get(), sketch.SortFlag.getValue()[1]});
                }
                ImGui.checkbox("Sort order - forward/backward", sortOrder);
                if (ImGui.button("Sort")){
                    if(sortOrder.get())
                        sketch.SortFlag = new SimpleEntry<Boolean, int[]>(true, new int[]{sketch.SortFlag.getValue()[0], -1});
                    else
                        sketch.SortFlag = new SimpleEntry<Boolean, int[]>(true, new int[]{sketch.SortFlag.getValue()[0], 1});
                }
            ImGui.end();

            ImGui.begin("Wormspace");
                if(ImGui.sliderFloat("Max amplitude", amp, 0f, 300f)) {
                    sketch.amp = (float) amp[0];
                    sketch.Worms = sketch.GenerateWorms();
                }

                if(ImGui.sliderFloat("Timescale", timeScale, 0f, 150f)) {
                    sketch.timeScale = (float) timeScale[0];
                    sketch.Worms = sketch.GenerateWorms();
                }

                if(ImGui.sliderFloat("Worm joint diff", jointDiff, 0.1f, 100f)){
                    sketch.jointDiff = (float) jointDiff[0];
                    sketch.Worms = sketch.GenerateWorms();
                }
                
                if(ImGui.sliderFloat("Worm apex joint radius", apexRadius, 0.1f, 100f)){
                    sketch.apexRadius = (float) apexRadius[0];
                    sketch.Worms = sketch.GenerateWorms();
                }
                
                if(ImGui.sliderFloat("Worm color offset init scalar", colorOffsetInitScalar, 100f, 10000f)){
                    sketch.colorOffsetInitScalar = (float) colorOffsetInitScalar[0];
                    sketch.Worms = sketch.GenerateWorms();
                }

                if(ImGui.sliderFloat("Worm color offset diff scalar", colorOffsetDiffScalar, 0.1f, 250f)){
                    sketch.colorOffsetDiffScalar = (float) colorOffsetDiffScalar[0];
                    sketch.Worms = sketch.GenerateWorms();
                }

            ImGui.end();
            ImGui.begin("Color Settings");
                if(ImGui.combo("Color Mode", colorMode, colorModes)){
                }
            
            ImGui.end();

        ImGui.end();
    }

    @Override
    public Map<String, ?> fetchValueMap() {
        return null;
    }

    @Override
    public void fetchValues() {
    }
}
