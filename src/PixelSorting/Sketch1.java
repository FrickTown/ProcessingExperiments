package PixelSorting;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Comparator;

public class Sketch1 extends PApplet {
    //Modes: 0 - Black, 1 - Brightness, 2 - White
    int mode = 0;
    int blackThreshold = -16800000;
    int modularThreshold = blackThreshold;
    int brightThreshold = 60;
    int whiteThreshold = -13000000;
    PImage img;

    public void settings(){
        size(800, 600);
    }

    public void setup(){
        img = loadImage("images/1.jpg");
        img.resize(800, 600);
    }
    double ticker = 700;
    public void draw(){
        println("MouseX: " + mouseX + " MouseY: " + mouseY);
        ticker += 0.5;
        double modularRatio = (ticker /(width-1));
        modularThreshold = (int) (modularRatio * blackThreshold);
        println("Modular threshold: " + ticker + "/" + (width-1) + " * " + blackThreshold + " = " + modularThreshold);
        image(img, 0, 0);
        for(int i = 0; i < width; i++) {
            loadPixels();
            pixels = pixels;
            sortColumn(i);
            updatePixels();
        }
    }

    public void mousePressed(){

    }

    public void mouseReleased(){
        mode = mode == 2 ? 0 : mode + 1;
        println("New mode: " + mode);
    }

    public void sortColumn(int index){
        int[] columnPixels = new int[height];
        ArrayList<Integer> relevantPixels = new ArrayList<Integer>();
        for(int i = 0; i < height; i++){
            columnPixels[i] = index + (i * width);
            if(pixels[columnPixels[i]] < modularThreshold)
                break;
            relevantPixels.add(pixels[columnPixels[i]]);
        }
        relevantPixels.sort(Comparator.naturalOrder());
        for(int i = 0; i < relevantPixels.size(); i++){
            pixels[columnPixels[i]] = relevantPixels.get(i);
        }
    }
    public void sortRow(int index){

    }
}
