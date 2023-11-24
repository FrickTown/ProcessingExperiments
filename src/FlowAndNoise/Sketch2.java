package FlowAndNoise;

import Sketchtools.Tools;
import Sketchtools.Distortion;
import Sketchtools.FlowField;
import Sketchtools.LineDrawer;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

public class Sketch2 extends PApplet {

    FlowField field;
    ArrayList<LineDrawer> drawers;
    long angleSeed;
    long intensitySeed;
    @Override
    public void settings() {
        size(1000, 750);
    }
    @Override
    public void setup() {
        field = new FlowField(this, 0.01f, 0.5f);
        angleSeed = (long) random(-16000000, 16000000);
        intensitySeed = (long) random(-16000000, 16000000);
        PVector c = field.windowToPlane(new PVector((float) width /2, (float) height /2));
        for(int y = 0; y < field.getField().length; y++){
            for(int x = 0; x < field.getField()[y].length; x++){
                //grid[i][j] = ((float) i / rowCount) * PI;
                noiseSeed(angleSeed);
                noiseDetail(4, 0.5f);
                float angleNoise = noise((float)x * 0.005f, (float)y * 0.005f);
                field.getField()[y][x] = PVector.fromAngle(map(angleNoise, 0.0f, 1.0f, 0.0f, PI * 2.0f));
                noiseSeed(intensitySeed);
                noiseDetail(4, 0.75f);
                float intensityNoise = noise((float)x * 0.005f, (float)y * 0.005f);
                field.getField()[y][x].z = intensityNoise;
                //field.setFlowAtIndex(x, y, PVector.fromAngle(((float) y / field.getField().length) * PI));
                //field.setFlowAtIndex(x, y, new PVector(0,0));
                //field.setFlowAtIndex(x, y, PVector.fromAngle(PVector.angleBetween(field.windowToPlane(field.getPointAtIndex(x, y)), c)));
            }
        }

        Distortion.RadialDistortion(field, field.windowToPlane(new PVector((float) width /2, (float) height /2)), 200, 1, true);

        drawers = new ArrayList<LineDrawer>();
        int size = 5;
        for(int y = 0; y < size; y+=1) {
            for (int x = 0; x < size; x++) {
                PVector pos = new PVector(
                        map(x, 0, size, field.windowToPlane(new PVector(100, 100)).x, field.windowToPlane(new PVector(width, 0)).x),
                        map(y, 0, size, field.windowToPlane(new PVector(100, 100)).y, field.windowToPlane(new PVector(0, height)).y)
                );
                LineDrawer t = new LineDrawer(pos, field.getFlowAtPoint(pos), 500);
                drawers.add(t);
            }
        }
    }
    PGraphics flowArrows = null;
    PGraphics drawnLines = null;
    public void draw() {
        background(255);
        if(flowArrows == null){
            flowArrows = Tools.DrawFlowArrows(field, 50, 100, 2);

        }
        image(flowArrows, 0, 0);
        noFill();
        noStroke();
        for(LineDrawer drawer : drawers){
            while(drawer.getLifeRemaining() > 0){
                field.influenceLinedrawer(drawer);
                if(drawer.getDir() == null)
                    break;
                drawer.takeStep();
            }
            ArrayList<PVector> calcPoints = drawer.getCalculatedPoints();
            for(int i = calcPoints.size() - 1; i > 0; i--) {
                PVector x = calcPoints.get(i);
                x = field.planeToWindow(x);
                fill(200, 0, map(i, 0, calcPoints.size(), 0, 255));
                circle(x.x, x.y, map(i, 0,  calcPoints.size(), 10, 0));
                println(x);
            }
        }
        stroke(255);
        strokeWeight(4);
        noFill();
        circle((float) width /2, (float) height /2, 400);

        noLoop();
    }

    @Override
    public void mouseClicked() {
        if(mouseButton == LEFT){
            PVector pos = field.windowToPlane(new PVector(mouseX, mouseY));
            LineDrawer newDraw = new LineDrawer(pos, field.getFlowAtPoint(pos), 500);
            //Tools.HealDrawers(drawers, 500);
            drawers.add(newDraw);
            redraw();
        }
        else if(mouseButton == CENTER){
            saveFrame("./savedFrames/"+java.time.LocalDateTime.now().toString() + ".png");
        }

    }
}
