package Nodethreads;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.*;

public class Vertex {
    PVector pos, vel;
    Vertex[] nearestNeighbors;
    private PApplet sketch;

    public Vertex(PApplet sketch, float x, float y){
        this.sketch = sketch;
        pos = new PVector(x, y);
        vel = new PVector(0, 0);
    }

    public Vertex(PApplet sketch){
        this.sketch = sketch;
        pos = new PVector(0,0);
        vel = new PVector(0, 0);
    }
    int tickCounter = 99;
    public void update(){
        tickCounter += 1;
        if(tickCounter >= 100){
            Random random = new Random();
            vel.set(random.nextFloat(2) - 1, random.nextFloat(2) - 1);
            tickCounter = 0;
        }
        if(pos.x + vel.x >= sketch.width || pos.x + vel.x <= 0){
            vel.x *= -1;
        }
        if(pos.y + vel.y >= sketch.height || pos.y + vel.y <= 0){
            vel.y *= -1;
        }
        pos.add(vel);
    }

    public static Vertex[] findNearestNeighbors(Vertex node, ArrayList<Vertex> nodes, int n){
        ArrayList<Vertex> clonedNodes = (ArrayList<Vertex>) nodes.clone();
        clonedNodes.remove(node);
        clonedNodes.sort(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex vertex, Vertex t1) {
                float dif1 = PVector.dist(node.pos, vertex.pos);
                float dif2 = PVector.dist(node.pos, t1.pos);
                if (dif1 > dif2)
                    return 1;
                else if (dif1 < dif2)
                    return -1;
                return 0;
            }
        });
        Vertex[] outArr = new Vertex[n];
        for(int i = 0; i < n; i++){
            outArr[i] = clonedNodes.get(i);
        }
        return outArr;
    }

    public String toString(){
        return "pos: " + pos.toString() + ", vel: " + vel.toString();
    }
}
