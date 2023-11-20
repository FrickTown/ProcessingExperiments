package FlowAndNoise;

import Flipper.FPoint2;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.awt.*;
import java.util.Arrays;

public class Sketch1 extends PApplet {
    int leftBounds;
    int rightBounds;
    int topBounds;
    int bottomBounds;
    int resolution;
    int columnCount;
    int rowCount;
    float[][] grid;
    FPoint2[] startingPoints;
    int startingPointCount = 3000;
    int numSteps = 25;
    float stepSize = 2f;
    float defaultAngle = PI * 0.25f;
    long lastSeed = 0;
    long newSeed = 0;
    int fromColor = 0;
    int toColor = 0;
    int fromWhite = 0;
    int toWhite = 0;
    int noiseLod = 4;
    float noiseFalloff = 0.5f;
    @Override
    public void settings(){
        super.settings();
        size(1000, 750);
    }
    void setupStartingPoints(){
        startingPoints = new FPoint2[startingPointCount];
        float startingPointSpacing = (float) (rightBounds - leftBounds) / startingPointCount;
        for(int i = 0; i < startingPointCount; i++){
            startingPoints[i] = new FPoint2(random(leftBounds, rightBounds), random(topBounds, bottomBounds));
        }
    }
    void setupGridAngles(long seed){
        noiseSeed(seed);
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                //grid[i][j] = ((float) i / rowCount) * PI;
                float noiseVal = noise((float)i * 0.005f, (float)j * 0.005f);
                grid[i][j] = map(noiseVal, 0.0f, 1.0f, 0.0f, PI * 2.0f);
            }
        }
    }

    void setupGridAngles(){
        lastSeed = newSeed;
        newSeed = (long) random(-10000, 10000);
        setupGridAngles(newSeed);
    }

    void generateBackground(){
        fromColor = color(204, 102, 0);
        toColor = color(0, 101, 153);
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                noStroke();
                float lerpVal = map(grid[i][j], 0, PI*2.0f, 0, 1);
                int lerpedColor = lerpColor(fromColor, toColor, lerpVal);
                fill(lerpedColor);
                circle(j * resolution, i * resolution, 15);
            }
        }
    }

    @Override
    public void setup() {
        leftBounds = (int) (width * -0.5);
        rightBounds = (int) (width * 1.5);
        topBounds = (int) (height * -0.5);
        bottomBounds = (int) (height * 1.5);
        resolution = (int) (width * 0.01);
        columnCount = ((rightBounds - leftBounds) / resolution);
        rowCount = ((bottomBounds - topBounds) / resolution);
        grid = new float[rowCount][columnCount];
        fromWhite = color(170,187,204);
        toWhite = color(204, 187, 170);
        setupStartingPoints();
        noiseDetail(noiseLod, noiseFalloff);
        setupGridAngles();
    }
    @Override
    public void draw() {
        println("draw");
        background(Color.WHITE.getRGB());
        generateBackground();
        strokeWeight(3);
        noFill();
        for(FPoint2 starter : startingPoints){
            float curveX = starter.X;
            float curveY = starter.Y;
            float xOffset = curveX - leftBounds;
            float yOffset = curveY - topBounds;
            int colIndex = (int) (xOffset / resolution);
            int rowIndex = (int) (yOffset / resolution);
            stroke(lerpColor(fromWhite, toWhite, map(grid[rowIndex][colIndex], 0, PI*2.0f, 0, 1)));
            beginShape();
                for(int i = 0; i < numSteps; i++) {
                    curveVertex(curveX, curveY);
                    xOffset = curveX - leftBounds;
                    yOffset = curveY - topBounds;
                    colIndex = (int) (xOffset / resolution);
                    rowIndex = (int) (yOffset / resolution);
                    if (colIndex >= grid[0].length || colIndex < 0 || rowIndex >= grid.length || rowIndex < 0)
                        break;
                    float gridAngle = grid[rowIndex][colIndex];
                    float xStep = stepSize * cos(gridAngle);
                    float yStep = stepSize * sin(gridAngle);
                    curveX += xStep;
                    curveY += yStep;
                }
            endShape();
        }
        if(doC){
            fill(Color.RED.getRGB());
            circle(400, 400, 25);
        }
        noLoop();
    }

    boolean doC = false;
    @Override
    public void keyPressed() {
        if(keyCode == RIGHT){
            println("RightPressed");
            setupGridAngles();
            doC = !doC;
            redraw();
        }
        if(keyCode == LEFT){
            println("LeftPressed");
            setupGridAngles(lastSeed);
            redraw();
        }
        if(keyCode == UP){
            noiseLod += 1;
            noiseDetail(noiseLod, noiseFalloff);
            setupGridAngles(newSeed);
            redraw();
        }
        if(keyCode == DOWN){
            noiseLod -= 1;
            noiseDetail(noiseLod, noiseFalloff);
            setupGridAngles(newSeed);
            redraw();
        }
        if(keyCode == 73){
            noiseFalloff += 0.05f;
            noiseDetail(noiseLod, noiseFalloff);
            setupGridAngles(newSeed);
            redraw();
        }
        if(keyCode == 74){
            noiseFalloff -= 0.05f;
            noiseDetail(noiseLod, noiseFalloff);
            setupGridAngles(newSeed);
            redraw();
        }
    }
}
