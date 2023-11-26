package FlowAndNoise;

import Sketchtools.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import imgui.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Sketch2 extends PApplet {

    public FlowField getField() {
        return field;
    }

    public void setField(FlowField field) {
        this.field = field;
    }

    public ArrayList<LineDrawer> getDrawers() {
        return drawers;
    }

    public void setDrawers(ArrayList<LineDrawer> drawers) {
        this.drawers = drawers;
    }

    public ArrayList<LineDrawer> getPostDrawers() {
        return postDrawers;
    }

    public void setPostDrawers(ArrayList<LineDrawer> postDrawers) {
        this.postDrawers = postDrawers;
    }

    public long getAngleSeed() {
        return angleSeed;
    }

    public void setAngleSeed(long angleSeed) {
        this.angleSeed = angleSeed;
    }

    public long getIntensitySeed() {
        return intensitySeed;
    }

    public void setIntensitySeed(long intensitySeed) {
        this.intensitySeed = intensitySeed;
    }

    public Boolean getShowDrawnLines() {
        return showDrawnLines;
    }

    public void setShowDrawnLines(Boolean showDrawnLines) {
        this.showDrawnLines = showDrawnLines;
    }

    public Boolean getShowFlowArrows() {
        return showFlowArrows;
    }

    public void setShowFlowArrows(Boolean showFlowArrows) {
        this.showFlowArrows = showFlowArrows;
    }

    public Boolean getShowHeatMap() {
        return showHeatMap;
    }

    public void setShowHeatMap(Boolean showHeatMap) {
        this.showHeatMap = showHeatMap;
    }

    public static int getBGColor() {
        return BGColor;
    }

    public static void setBGColor(int BGColor) {
        Sketch2.BGColor = BGColor;
    }

    public static int getShadeColor() {
        return ShadeColor;
    }

    public static void setShadeColor(int shadeColor) {
        ShadeColor = shadeColor;
    }

    public static int getLightColor() {
        return LightColor;
    }

    public static void setLightColor(int lightColor) {
        LightColor = lightColor;
    }

    public static int getDarkColor() {
        return DarkColor;
    }

    public static void setDarkColor(int darkColor) {
        DarkColor = darkColor;
    }

    FlowField field;
    ArrayList<LineDrawer> drawers;
    ArrayList<LineDrawer> postDrawers;
    long angleSeed;
    long intensitySeed;
    PGraphics drawnLines = null;
    Boolean showDrawnLines = true;
    PGraphics flowArrows = null;
    Boolean showFlowArrows = false;
    PGraphics heatMap = null;
    Boolean showHeatMap = false;
    boolean fullRedraw = false;

    static int BGColor = Color.decode("#DDF2FD").getRGB();
    static int ShadeColor = Color.decode("#DDF2FD").getRGB();
    static int LightColor = Color.decode("#427D9D").getRGB();
    static int DarkColor = Color.decode("#164863").getRGB();

    @Override
    public void settings() {
        size(1000, 750);
    }
    public void distort(){
        float thirdX = (float) width / 3;
        float thirdY = (float) height / 3;
        Distortion.RadialDistortion(field, field.windowToPlane(new PVector(thirdX*0.5f, thirdY*0.5f)), thirdX/2.5f, 50, true);
        Distortion.RadialDistortion(field, field.windowToPlane(new PVector(thirdX*1.5f, thirdY*1.5f)), thirdX/2.5f, 50, true);
        Distortion.RadialDistortion(field, field.windowToPlane(new PVector(thirdX*2.5f, thirdY*2.5f)), thirdX/2.5f, 50, true);
    }
    @Override
    public void setup() {
        ImGuiThread imguiThread = new ImGuiThread(this, Sketch2_GUI.class);
        imguiThread.start();
        //Create a new field, generate some noise (multithreaded) and distort with the distortion function above
        field = new FlowField(this, 0.005f, 0.5f);
        generateFieldNoise(field);
        distort();

        //Initialize the list for automatically placed drawers, and the list for user defined drawers
        postDrawers = new ArrayList<LineDrawer>();
        drawers = new ArrayList<LineDrawer>();

        //Genearate automatically placed drawers (Row above and row below)
        int size = 50;
        PVector m50m50 = field.windowToPlane(new PVector(0, 0));
        for(int j = 0; j < 2; j++){
            for(int i = 0; i < size; i++){
                PVector pos = new PVector(
                        m50m50.x + (i * 25),
                        m50m50.y  - 50 + (j*(height+100))
                );
                LineDrawer t = new LineDrawer(pos, field.getFlowAtPoint(pos), 1000);
                drawers.add(t);
            }
            for(int i = 0; i < size-15; i++) {
                PVector pos = new PVector(
                        m50m50.x - 50 + (j * (width+100)),
                        m50m50.y + (i * 25)
                );
                PVector newFlow = field.getFlowAtPoint(pos);
                LineDrawer t = new LineDrawer(pos, newFlow, 1000);
                drawers.add(t);
            }
        }
    }
    public void draw() {
        if((flowArrows == null || fullRedraw) && showFlowArrows){
            thread("drawArrowsAsync");
        }

        if((heatMap == null || fullRedraw) && showHeatMap){
            thread("generateHeatmapAsync");
        }

        if((drawnLines == null || fullRedraw) && showDrawnLines){
            thread("marchAsync");
        }
        background(BGColor);

        if(heatMap != null && showHeatMap){
            image(heatMap, 0, 0);
        }
        if(flowArrows != null && showFlowArrows){
            image(flowArrows, 0, 0);
        }
        if(drawnLines != null && showDrawnLines){
            image(drawnLines, 0, 0);
        }
        noStroke();
        noFill();
        //Stuff to draw after first render
        for(LineDrawer drawer : postDrawers) {
            if(drawer.getCalculatedPoints().size() <= 1)
                drawer.march(field);
            drawLineDrawerPath(drawer, this.getGraphics());
        }
        stroke(255);
        strokeWeight(4);
        noFill();
        circle((float) width /2, (float) height /2, (float)width/3);

        fullRedraw = false;
        noLoop();
    }

    @Override
    public void mouseClicked() {
        if(mouseButton == LEFT){
            PVector pos = field.windowToPlane(new PVector(mouseX, mouseY));
            LineDrawer newDraw = new LineDrawer(pos, field.getFlowAtPoint(pos), 1000);
            postDrawers.add(newDraw);
            redraw();
        }
        else if(mouseButton == CENTER){
            saveFrame("./savedFrames/"+java.time.LocalDateTime.now().toString() + ".png");
        }
        else if(mouseButton == RIGHT){
            PVector pos = field.windowToPlane(new PVector(mouseX, mouseY));
            Distortion.RadialDistortion(field, pos, 200, 50, true);
            fullRedraw = true;
            Tools.ResetDrawers(drawers, field);
            Tools.ResetDrawers(postDrawers, field);
            redraw();
        }
    }

    @Override
    public void keyPressed() {
        if(key == ENTER){
            generateFieldNoise(field);
            distort();
            fullRedraw = true;
            Tools.ResetDrawers(drawers, field);
            postDrawers.clear();
            redraw();
        }
        if(keyCode == KeyEvent.VK_H){
            showHeatMap = !showHeatMap;
            redraw();
        }
        if(keyCode == KeyEvent.VK_F){
            showFlowArrows = !showFlowArrows;
            redraw();
        }
    }

    public void drawLineDrawerPath(LineDrawer drawer, PGraphics context){
        context.noStroke();
        ArrayList<PVector> calcPoints = drawer.getCalculatedPoints();
        for(int i = calcPoints.size() - 1; i > 0; i--) {
            PVector x = calcPoints.get(i);
            x = field.planeToWindow(x);
            float mapped = map(i, 0, calcPoints.size(), 0, 1);
            context.fill(lerpColor(DarkColor, LightColor, mapped));
            try{
                context.circle(x.x, x.y, map(i, 0,  calcPoints.size(), 10, 0));
            } catch(ArrayIndexOutOfBoundsException e){
                println(drawer.toString());
            }
        }
    }

    public void drawLineDrawerPath(LineDrawer drawer) {

    }

    public void generateFieldNoise(FlowField flowField, long aSeed, long iSeed){

        angleSeed = aSeed;
        intensitySeed = iSeed;

        //For every row in the field, start a RowNoiserThread
        ArrayList<RowNoiserThread> noiserThreads = new ArrayList<RowNoiserThread>();
        for(int y = 0; y < flowField.getField().length; y++){
            //Add them to a collection to keep track, and start them
            noiserThreads.add(new RowNoiserThread(y, new PVector[flowField.getField()[0].length], angleSeed, intensitySeed));
            noiserThreads.get(y).start();
        }

        //While there are still noiserThreads left in the collection...
        boolean notDone = true;
        while(notDone){
            //Loop through them...
            for(int i = 0; i < noiserThreads.size(); i++){
                RowNoiserThread ind = noiserThreads.get(i);
                //Check if they're done...
                if(!ind.isAlive()){
                    //If they are, inject the buffer into the field array, and remove the thread
                    flowField.getField()[ind.y] = ind.row;
                    noiserThreads.remove(ind);
                }
            }
            if(noiserThreads.isEmpty())
                notDone = false;
        }
    }

    public void generateFieldNoise(FlowField field){
        //Generate random seeds
        long aSeed = (long) random(-16000000, 16000000);
        long iSeed = (long) random(-16000000, 16000000);
        generateFieldNoise(field, aSeed, iSeed);
    }

    public void marchAsync(){
        drawnLines = createGraphics(field.getBounds().getWidth(), field.getBounds().getHeight());
        drawnLines.beginDraw();
        drawnLines.clear();
        for(LineDrawer drawer : drawers){
            drawer.marchConstant(field);
            drawLineDrawerPath(drawer, drawnLines);
        }
        drawnLines.endDraw();
        redraw();
    }

    public void drawArrowsAsync(){
        flowArrows = Tools.DrawFlowArrows(field, 50, 100, 4);
        redraw();
    }

    public void generateHeatmapAsync(){
        heatMap = Tools.GenerateHeatMapZ(field, 1);
        redraw();
    }

}
