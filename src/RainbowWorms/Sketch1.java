package RainbowWorms;

import java.util.ArrayList;
import java.util.Comparator;

import Sketchtools.Tools;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Sketch1 extends PApplet{

    ArrayList<Worm> Worms;
    PVector center;
    int rows = 15;
    int columns = 15;

    public void settings() {
        size(1920, 1080, P3D);
    }

    public void setup() {
        center = new PVector(width / 2, height / 2);
        Worms = new ArrayList<Worm>();
        float minRadius = 50;
        float maxRadius = 100;
        float amp = 20;
        float maxTimeOffset = PI;
        for(int y = 0; y < height; y += height / rows){
            for(int x = 0; x < width; x += width / columns){
                PVector pos = new PVector(x, y);
                float fromCenter = pos.dist(center) + 1;
                float invFC = (maxRadius / fromCenter);
                float easedInv = (float)Tools.easeOutCirc(invFC);
                pos.mult(easedInv);
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
            translate(width/4, height/4);
            noStroke();
            background(color(0));
            for (Worm worm : Worms) {
                worm.draw(this);
            }
        pop();
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
        while(nextRad >= apexRadius){
            joints.add(new Joint(center.copy(), nextRad, amplitude, timeOffset, colorOffset));
            nextRad -= jointDiff;
            colorOffset += jointDiff * 5;
            timeOffset += (1 / jointDiff) * (timeScale / 2);
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
