package Sketchtools;

import processing.core.PVector;

import java.util.ArrayList;

public class LineDrawer {
    private final PVector originalPos;
    private final PVector originalDir;
    private final int originalHP;
    private PVector pos;
    private PVector dir;
    private int lifeRemaining;
    private ArrayList<PVector> calculatedPoints;

    public LineDrawer(PVector pos, PVector dir, int lifeTime) {
        originalPos = pos.copy();
        this.pos = pos;
        originalDir = dir.copy();
        this.dir = dir;
        originalHP = lifeTime;
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
        System.out.println("["+lifeRemaining+"] - I'm at: " + pos + ", my dir is: " + dir);
        calculatedPoints.add(pos.copy());
        lifeRemaining--;
    }

    public void takeConstantStep(){
        pos.x += (dir.x);
        pos.y += (dir.y);
        System.out.println("["+lifeRemaining+"] - I'm at: " + pos + ", my dir is: " + dir);
        calculatedPoints.add(pos.copy());
        lifeRemaining--;
    }

    public void analyzeFlow(){

    }

    public void march(FlowField field){
        while(lifeRemaining > 0){
            field.influenceLinedrawerDynamic(this);
            if(dir == null)
                break;
            takeStep();
        }
    }

    public void marchConstant(FlowField field){
        while(lifeRemaining > 0){
            field.influenceLinedrawerDynamic(this);
            if(dir == null)
                break;
            takeConstantStep();
        }
    }

    public PVector getOriginalDir() {
        return originalDir;
    }

    public PVector getOriginalPos() {
        return originalPos;
    }

    public int getOriginalHP() {
        return originalHP;
    }

    public void resetDrawer(){

    }
}
