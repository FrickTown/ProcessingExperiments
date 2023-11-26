package Sketchtools;

import FlowAndNoise.Sketch2;
import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.type.ImInt;
import processing.core.PApplet;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public abstract class ImGuiMenu extends Application {
    protected PApplet mainThread;
    @Override
    protected abstract void configure(Configuration config);
    @Override
    public abstract void process();

    public ImGuiMenu(PApplet mainThread){
        this.mainThread = mainThread;
    }

    public static void main(PApplet mainThread, Class<? extends ImGuiMenu> menuType) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println(Arrays.toString(menuType.getConstructors()[0].getParameterTypes()));
        launch(menuType.getConstructor(mainThread.getClass()).newInstance(mainThread));
    }
}
