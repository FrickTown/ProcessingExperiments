package Sketchtools;

import processing.core.PVector;

import java.util.ArrayList;

public class LineDrawer {
    private PVector pos;
    private PVector vel;
    private int lifeRemaining;
    private ArrayList<PVector> calculatedPoints;

    public LineDrawer(PVector pos, PVector vel, int lifeTime) {
        this.pos = pos;
        this.vel = vel;
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

    public PVector getVel() {
        return vel;
    }

    public void setVel(PVector vel) {
        this.vel = vel;
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
        pos.add(vel);
        calculatedPoints.add(pos.copy());
        lifeRemaining--;
    }

    public void analyzeFlow(){

    }
}
