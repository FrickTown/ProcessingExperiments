package RainbowWorms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import Sketchtools.Tools;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Sketch1 extends PApplet{

    ArrayList<Worm> Worms;
    PVector center;
    int rows = 30;
    int columns = 50;

    PVector ViewPort;

    public void settings() {
        size(1920, 1080, P3D);
    }

    public void setup() {
        ViewPort = new PVector(0, 0, 0);
        center = new PVector(width / 2, height / 2);
        Worms = new ArrayList<Worm>();
        float minRadius = 50;
        float maxRadius = 100;
        float amp = 75;
        float maxTimeOffset = PI;
        for(float y = 0; y < height; y += height / rows){
            float modY = (float)Tools.easeInOutBack(y/height) * height;
            for(float x = 0; x < width; x += width / columns){
                float modX = (float)Tools.easeInOutBack(x/width) * width;
                PVector pos = new PVector(modX, modY);
                println(pos);
                float fromCenter = pos.dist(center) + 1;
                float invFC = (maxRadius / fromCenter);
                float easedInv = (float)Tools.easeOutCirc(invFC);
                Worms.add(new Worm(pos, fromCenter, lerp(minRadius, maxRadius, easedInv), lerp(maxTimeOffset, 0, easedInv), amp));

            }
        }

        Worms.sort(new Comparator<Worm>(){
            @Override
            public int compare(Worm x, Worm y){
                if(x.fromCenter > y.fromCenter)
                    return -1;
                else if(x.fromCenter < y.fromCenter)
                    return 1;
                return 0;
            }
        });
    }

    public void draw() {
        push();
            translate(ViewPort.x, ViewPort.y, ViewPort.z);
            noStroke();
            background(color(0));
            for (Worm worm : Worms) {
                worm.draw(this);
            }
        pop();
        if(keyPressed)
            handleInput();
    }

    public void handleInput(){
        println(keyCode);
        switch (keyCode) {
            case 37: //left
                    ViewPort.x -= 10;
                break;
            case 38: //up
                ViewPort.y -= 10;
                break;
            
            case 39: //right
                ViewPort.x += 10;
                break;
            
            case 40: //down
                ViewPort.y += 10;
                break;
            default:
                break;
        }
    }
}

class Worm {
    ArrayList<Joint> joints = new ArrayList<Joint>();
    float jointDiff = 15;
    float apexRadius = 5f;
    float amplitude = 1f;
    float timeOffset;
    float timeScale = 4f;
    PVector center;
    float fromCenter;


    public Worm(PVector center, float fromCenter, float maxRadius, float timeOffset, float amplitude){
        this.fromCenter = fromCenter;
        this.timeOffset = timeOffset;
        this.amplitude = amplitude;
        this.center = center;
        int colorOffset = (int)(1000 * timeOffset); 
        float nextRad = maxRadius;
        while(nextRad >= apexRadius) {
            joints.add(new Joint(center.copy(), nextRad, amplitude, timeOffset, colorOffset));
            nextRad -= jointDiff;
            colorOffset += jointDiff * 5;
            timeOffset += (1 / jointDiff) * (timeScale / 1);
        }
        System.out.println(joints.size());
        
    }

    public void draw(PApplet ctx){
        ctx.colorMode(PConstants.HSB, 3000, 255, 255);
        for (Joint joint : joints) {
            joint.update();
            joint.draw(ctx);
        }
    }

    public String toString(){
        return "Pos: " + this.center.toString() + " | ";
    } 
}

class Joint {
    float radius;
    PVector pos;
    int[] color = new int[4];
    int colorInt;
    float clock = 0;
    float amplitude = 1f;
    float speedScale = 7f;
    int colorSpeed = 10;

    public Joint(PVector pos, float radius, float amplitude, float timeOffset, int colorInt){
        this.colorInt = (int) colorInt;
        this.amplitude = (float) amplitude;
        this.clock = (float) timeOffset; 
        this.radius = (float) radius;
        this.pos = pos;
    }

    public void draw(PApplet ctx){
        ctx.fill(colorInt, 255, 255);
        ctx.circle(pos.x, pos.y + PApplet.sin(clock) * amplitude, radius);
    }

    public void update(){
        colorInt += colorSpeed;
        if(colorInt >= 3000){
            colorInt = 0;
        }
        clock += 0.01 * speedScale;
    }
}
