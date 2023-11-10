package Flipper;

import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

public class App extends PApplet {

    public void settings(){
        size(displayWidth/2, displayHeight/2 - 40);

    }
    ArrayList<Node> Nodes;
    ArrayList<Node> Purge;
    FPoint2 CENTER;

    float tick = 0.01f;

    public void setup(){
        windowMove(displayWidth/2 - 10, displayHeight/2 - 20);
        CENTER = new FPoint2((float) width /2, (float) height /2);
        Nodes = new ArrayList<Node>();
        Purge = new ArrayList<Node>();
        int nodeCap = 10;
        int radius = 60;
        for(int i = 1; i <= nodeCap; i++){
            float radians = (TAU/nodeCap) * i;
            Nodes.add(new Node(cos(radians) * radius, sin(radians) * radius, radius, radians));
        }
    }
    public void draw(){
        background(Color.BLACK.getRGB());
        for(Node x : Nodes) {
            float keep = x.radians;
            x.Update(tick);
            if(sin(x.radians) < 0 && sin(keep) > sin(x.radians)){
                Purge.add(x);
            }
            x.x = (float) (cos(x.radians + mouseX/1000f) * Math.pow(2 * mouseX / (1f + width), 3) * x.radius);
            x.y = sin(x.radians + mouseY/1000f) * (2 * mouseY / (1f + height)) * x.radius;
            fill(255 * sin(x.radians),200 * sqrt(cos(x.radians)*cos(x.radians)), 255 * -sin(x.radians));
            circle(CENTER.X + x.x, CENTER.Y + x.y, 25);
        }
        for(Node x : Purge){
            Nodes.remove(x);
            Nodes.add(0, x);
        }
        Purge.clear();

        if(millis() % 1000 <= 10){
            print("{");
            for(int i = 0; i < Nodes.size(); i++){
                print(Nodes.get(i).radians);
                if(i != Nodes.size()-1)
                    print(", ");
            }
            println("}");
        }
    }

    public void mousePressed(){
        tick = PI/50;
    }

    public void mouseReleased(){
        tick = PI/100;
    }
}
