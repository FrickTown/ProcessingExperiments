import processing.core.PApplet;

import static processing.core.PApplet.cos;
import static processing.core.PConstants.PI;
import static processing.core.PConstants.TAU;

public class Node {
    public float x, y, radius, radians, initrad;

    public Node(float x, float y, float radius, float radians){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.radians = radians;
        this.initrad = radians;
    }

    public void Update(float upd){
        radians += upd;
    }
}
