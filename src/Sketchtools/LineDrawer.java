package Sketchtools;

import processing.core.PVector;

import java.util.ArrayList;

public class LineDrawer {
    private PVector pos;
    private PVector dir;
    private int lifeRemaining;
    private ArrayList<PVector> calculatedPoints;

    public LineDrawer(PVector pos, PVector dir, int lifeTime) {
        this.pos = pos;
        this.dir = dir;
        this.lifeRemaining = lifeTime;
        calculatedPoints = new ArrayList<PVector>();
        calculatedPoints.add(pos);
    }

    public PVector getPos() {
        return pos;
    }

    public void setPos(PVector pos) {
        this.pos = pos;
    }

    public PVector getDir() {
        return dir;
    }

    public void setDir(PVector dir) {
        this.dir = dir;
    }

    public int getLifeRemaining() {
        return lifeRemaining;
    }

    public void setLifeRemaining(int lifeRemaining) {
        this.lifeRemaining = lifeRemaining;
    }

    public ArrayList<PVector> getCalculatedPoints() {
        return calculatedPoints;
    }

    public void takeStep(){
        pos.x += (dir.x * dir.z);
        pos.y += (dir.y * dir.z);
        calculatedPoints.add(pos.copy());
        lifeRemaining--;
    }

    public void analyzeFlow(){

    }
}
