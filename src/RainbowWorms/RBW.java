package RainbowWorms;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import Sketchtools.IGMenu;
import Sketchtools.ImGuiThread;
import Sketchtools.Tools;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class RBW extends PApplet{

    // Worldspace
    public int rows = 30;
    public int columns = 30;
    float maxMaxRadius = 100;
    float minMaxRadius = 50;
    float maxTimeOffset = 2*PI;
    
    // Wormspace
    float timeScale = 3;
    float amp = 75;
    float jointDiff = 15;
    float apexRadius = 5;
    float colorOffsetInitScalar = 1000;
    float colorOffsetDiffScalar = 5;
    
    public double GlobalTime = 0;
    public SimpleEntry<Boolean, int[]> SortFlag = new SimpleEntry<Boolean, int[]>(false, new int[]{0, -1});
    
    PVector center;
    PVector ViewPort = new PVector(0, 0, 0);

    ArrayList<Worm> Worms = new ArrayList<Worm>();

    ArrayList<Worm> GenerateWorms() {
        ArrayList<Worm> out = new ArrayList<Worm>();
        for(float y = 0; y < height; y += height / rows){
            // Space the worms evenly with respect to screen size, then offset their positions with easing functions.
            float modY = (float)CallEasing(activePosEasingFunction, y / height) * height;
            for(float x = 0; x < width; x += width / columns){
                float modX = (float)CallEasing(activePosEasingFunction, x / width) * width;
                PVector pos = new PVector(modX, modY);
                println(pos);

                // Find easing values between 0 - 1 for the size and time difference between worms (radially)
                float fromCenter = pos.dist(center);
                float invertedFC = ((minMaxRadius) / fromCenter);
                float sizeEase = (float)CallEasing(activeSizeEasingFunction, invertedFC);
                float timeEase = (float)CallEasing(activeTimeEasingFunction, invertedFC);
                // Once easing values are established, lerp them linearly between a min and a max value
                out.add(new Worm(this, pos, fromCenter, lerp(minMaxRadius, maxMaxRadius, sizeEase), lerp(maxTimeOffset, 0, timeEase)));

            }
        }
        return out;
    }

    // Sort a list of worms based on its radial distance from the center of the screen
    public void SortWorms(int radial, int order, ArrayList<Worm> wormList){
        if(radial == 1)
            wormList.sort(new Comparator<Worm>(){
                @Override
                public int compare(Worm x, Worm y){
                    if(x.fromCenter > y.fromCenter)
                        return order;
                    else if(x.fromCenter < y.fromCenter)
                        return -order;
                    return 0;
                }
            });
        else
            wormList.sort(new Comparator<Worm>(){
                @Override
                public int compare(Worm x, Worm y){
                    if(x.center.x * x.center.y < y.center.x * y.center.y)
                        return order;
                    else if(x.center.x * x.center.y > y.center.x * y.center.y)
                        return -order;
                    return 0;
                }
            });
    }

    void UpdateWormAmp(float newAmp, ArrayList<Worm> wormList) {
        wormList.iterator().forEachRemaining(worm -> {worm.amplitude = newAmp;});
    }

    ArrayList<String> pressedKeys = new ArrayList<String>();
    String activePosEasingFunction = "noEase";
    String activeSizeEasingFunction = "easeOutExpo";
    String activeTimeEasingFunction = "easeOutExpo";

    ImGuiThread imGuiThread;

    double CallEasing(String funcName, double val){
        try {
            return (double)Tools.class.getMethod(funcName, double.class).invoke(null, val);
        } catch (Exception e) {
            println("Easing exception");
            e.printStackTrace();
            println(e.getMessage());
            return -1;
        }  
    }


    public void settings() {
        size(1920, 1080, P3D);
    }

    public void setup() {
        imGuiThread = new ImGuiThread(this, RBWMenu.class);
        imGuiThread.start();
        center = new PVector(width / 2, height / 2);
        
        Worms = GenerateWorms();
        Worms.toString();
        SortWorms(1, -1, Worms);
    }

    public void draw() {
        if(!pressedKeys.isEmpty()){
            handleInput();
        }
        push();
            translate(ViewPort.x, ViewPort.y, ViewPort.z);
            noStroke();
            background(color(0));
            for (Worm worm : Worms) {
                worm.draw(this);
            }
        pop();
        if(SortFlag.getKey().booleanValue()) {
            SortWorms(SortFlag.getValue()[0], SortFlag.getValue()[1], Worms);
            SortFlag = new SimpleEntry<Boolean, int[]>(false, SortFlag.getValue());
        }

        GlobalTime += 0.1;

    }

    
    public void keyPressed() {
        pressedKeys.add(keyCode == 0 ? Character.toString(key) : Integer.toString(keyCode));
    }

    public void keyReleased() {
        pressedKeys.remove(keyCode == 0 ? Character.toString(key) : Integer.toString(keyCode));
    }

    public void handleInput() {
        for (String keyString : pressedKeys) {
            switch (keyString) {
                case "37": //left
                    ViewPort.x += 10;
                    break;
                case "38": //up
                    ViewPort.y += 10;
                    break;
                
                case "39": //right
                    ViewPort.x -= 10;
                    break;
                
                case "40": //down
                    ViewPort.y -= 10;
                    break;
    
                case "16": //in
                    ViewPort.z += 10;
                    break;
                case "17": //out
                    ViewPort.z -= 10;
    
                default:
                    break;
            }
        }
    }
}

class Worm {
    ArrayList<Joint> joints = new ArrayList<Joint>();
    private RBW world;
    float maxRadius;
    float amplitude;
    float timeOffset;
    float timeScale;
    PVector center;
    float fromCenter;


    public Worm(RBW ctx, PVector center, float fromCenter, float maxRadius, float timeOffset) {
        this.world = ctx;
        this.amplitude = ctx.amp;
        this.timeScale = ctx.timeScale;
        this.fromCenter = fromCenter;
        this.timeOffset = timeOffset;
        this.center = center;
        this.maxRadius = maxRadius;
        
        joints = generateJoints();
        System.out.println(joints.size());
        
    }

    private ArrayList<Joint> generateJoints() {
        ArrayList<Joint> out = new ArrayList<Joint>();
        int colorOffset = (int)(world.colorOffsetInitScalar * timeOffset); 
        float nextRad = maxRadius;
        while(nextRad >= world.apexRadius) {
            out.add(new Joint(center.copy(), nextRad, amplitude, timeOffset, colorOffset));
            nextRad -= world.jointDiff;
            colorOffset += world.jointDiff * world.colorOffsetDiffScalar;
            timeOffset += (1 / world.jointDiff) * (timeScale);
        }
        return out;
    }

    public void updateConstant(String propName, float val) {
        try{
            Worm.class.getField(propName).set(this, val);
            joints = generateJoints();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void draw(RBW ctx) {
        ctx.colorMode(PConstants.HSB, 3000, 255, 255);
        for (Joint joint : joints) {
            joint.update();
            joint.draw(ctx);
        }
    }

    public String toString() {
        return "Pos: " + this.center.toString() + " | ";
    } 
}

class Joint {
    float radius;
    PVector pos;
    int[] color = new int[4];
    int colorInt;
    float amplitude;
    int colorSpeed = 10;
    float timeOffset = 0;

    public Joint(PVector pos, float radius, float amplitude, float timeOffset, int colorInt) {
        this.colorInt = (int) colorInt;
        this.amplitude = (float) amplitude;
        this.timeOffset = (float) timeOffset; 
        this.radius = (float) radius;
        this.pos = pos;
    }

    public void draw(RBW ctx) {
        ctx.fill(colorInt, 255, 255);
        ctx.circle(pos.x, pos.y + PApplet.sin((float) ctx.GlobalTime + timeOffset) * amplitude, radius);
    }

    public void update() {
        colorInt += colorSpeed;
        if(colorInt >= 3000 || colorInt <= 0){
            colorInt = 0;
        }
    }
}
