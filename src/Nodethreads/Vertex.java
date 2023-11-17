package Nodethreads;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.*;

public class Vertex {
    PVector pos, vel;
    Vertex[] nearestNeighbors;
    private PApplet sketch;

    float nodeRadius = 15f;
    float nearForce = 2.5f;
    float nearLimit = nodeRadius;
    float farForce = 1.5f;
    float farLimit = 200f;
    float maxSpeed = 10f;

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
        /*if(tickCounter >= 100){
            Random random = new Random();
            vel.set(random.nextFloat(2) - 1, random.nextFloat(2) - 1);
            tickCounter = 0;
        }*/
        //Set it in motion
        if(vel.x == 0 && vel.y == 0)
            vel.set(sketch.random(-1, 1), sketch.random(-1, 1));

        PVector resultingVector = new PVector(0,0);
        int resultingIndex = 0;
        for(Vertex x : nearestNeighbors){
            //Find distance between
            float magBetween = PVector.dist(x.pos, this.pos);
            //Find vector representing the path from this node to it's neighbor
            PVector diff = PVector.sub(x.pos, this.pos);
            if(magBetween >= farLimit){
                //Normalize it
                diff = diff.normalize();
                //Scale the vector based on the distance and add it to the result
                resultingIndex++;
                resultingVector = resultingVector
                        .add(diff.mult(farForce * (magBetween / farLimit)));
            }
            if(magBetween < nearLimit){
                //Normalize it and invert it
                diff = diff.normalize().mult(-1);
                //Add the vector to current velocity and average it out, and then scale the vector based on the distance
                resultingIndex++;
                resultingVector = resultingVector
                        .add(diff.mult(nearForce * (1 - magBetween / nearLimit)));
            }
            if(resultingIndex > 0){
                this.vel.add(resultingVector.mult(1/resultingIndex));
            }
        }
        if(pos.x + vel.x >= sketch.width || pos.x + vel.x <= 0){
            vel.x *= -1;
        }
        if(pos.y + vel.y >= sketch.height || pos.y + vel.y <= 0){
            vel.y *= -1;
        }
        if(Math.abs(vel.x) / maxSpeed > 1 || Math.abs(vel.y) / maxSpeed > 1){
            vel = vel.normalize().mult(maxSpeed);
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
