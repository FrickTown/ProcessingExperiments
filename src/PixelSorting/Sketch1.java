package PixelSorting;

import Sketchtools.Tools;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.IntList;

import java.io.File;

public class Sketch1 extends PApplet {
    //Modes: 0 - Black, 1 - Brightness, 2 - White
    int mode = 0;
    int blackThreshold = -16800000;
    int modularThreshold = blackThreshold;
    int brightThreshold = 60;
    int whiteThreshold = -13000000;
    int lastThreshold = blackThreshold;
    PImage img;
    File imgFolder;
    int imgIndex = 0;
    String[] imgNames;

    public void settings(){
        size(1000, 750);
    }

    public void setup(){
        imgFolder = new File("./images/");
        imgNames = imgFolder.list();
        reloadImage();
    }
    double ticker = 200;
    double tickerMod = 1;
    public void draw(){
        //println("MouseX: " + mouseX + " MouseY: " + mouseY);
        ticker += tickerMod;
        tickerMod = ticker >= width-1 || ticker <= 200 ? tickerMod * -1 : tickerMod;
        double modularRatio = ((double) ticker /(width-1));
        modularRatio = Tools.easeOutQuad(modularRatio);
        lastThreshold = modularThreshold;
        modularThreshold = (int) (modularRatio * blackThreshold);
        //modularThreshold = blackThreshold;
        //println("Modular threshold: " + momouseXuseX + "/" + (width-1) + " * " + blackThreshold + " = " + modularThreshold);
        image(img, 0, 0);
        if(lastThreshold != modularThreshold){
            loadPixels();
            for(int i = 0; i < width; i++) {
                sortColumn(i);
            }
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
        int fromPixel = findFromPixelY(index);
        int toPixel = findToPixelY(index);
        int sortLength = toPixel - fromPixel;
        if(sortLength <= 1)
            return;
        IntList toSort = new IntList();
        for(int i = fromPixel; i <= toPixel; i++){
            toSort.append(pixels[index + (i*width)]);
        }
        toSort.sort();
        int j = fromPixel;
        for(int i = 0; i < toSort.size(); i++){
            pixels[index + (j*width)] = toSort.get(i);
            j++;
        }
    }

    int findFromPixelY(int index){
        int i = 0;
        while(i < height){
            if(pixels[index + (i * width)] > modularThreshold)
                return i;
            i++;
        }
        return height-1;
    }

    int findToPixelY(int index){
        int i = 0;
        while(i < height){
            if(pixels[index + (i * width)] < modularThreshold)
                return i;
            i++;
        }
        return height-1;
    }
    public void sortRow(int index){

    }

    public void reloadImage(){
        img = loadImage("images/"+imgNames[imgIndex]);
        img.resize(1000, 750);
    }

    public void keyReleased(){
        if(keyCode == RIGHT){
            imgIndex = imgIndex == imgNames.length-1 ? 0 : imgIndex + 1;
            reloadImage();
        }
        else if (keyCode == LEFT){
            imgIndex = imgIndex == 0 ? imgNames.length -1 : imgIndex - 1;
            reloadImage();
        }
    }
}
