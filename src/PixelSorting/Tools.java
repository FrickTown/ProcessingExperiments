package PixelSorting;

import Flipper.IPoint2;
import Sketchtools.FlowField;
import Sketchtools.LineDrawer;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Tools {
    public static double easeOutExpo(double x){
        return x == 1 ? 1 : 1 - Math.pow(2, -10 * x);
    }

    public static double easeInOutExpo(double x){
        return x == 0
                ? 0
                : x == 1
                ? 1
                : x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
                : (2 - Math.pow(2, -20 * x + 10)) / 2;

    }

    public static double easeOutCirc(double x){
        return Math.sqrt(1 - Math.pow(x - 1, 2));
    }
    public static double easeInOutSine(double x){
        return -(Math.cos(Math.PI * x) - 1) / 2;

    }

    public static double easeOutQuad(double x){
        return 1 - (1 - x) * (1 - x);

    }

    public static void DrawFlowArrows(FlowField field, float intensity, int alpha){
        PApplet sketch = field.getParent();
        for(int i = 0; i < field.getField().length; i++){
            for(int j = 0; j < field.getField()[i].length; j++){
                PVector pos = field.getPointAtIndex(j, i);
                PVector val = field.getFlowAtIndex(j, i).mult(intensity);
                if(val.x == 0 && val.y == 0)
                    continue;
                sketch.stroke(0, 0, 0, alpha);
                sketch.strokeWeight(3);
                pos = field.planeToWindow(pos);
                sketch.line(pos.x, pos.y, pos.x + val.x,pos.y + val.y);
                sketch.fill(100, 140, 255, alpha);
                sketch.circle(pos.x, pos.y, 6);
                sketch.fill(255, 130, 100, alpha);
                sketch.circle(pos.x + val.x, pos.y + val.y, 3);
            }
        }
    }

    public ArrayList<IPoint2> GetCircularSelection(FlowField field, PVector pos, float radius){
        ArrayList<IPoint2> out = new ArrayList<IPoint2>();
        IPoint2 topLeft = field.getIndexAtPoint(new PVector(pos.x - radius, pos.y - radius));
        //Then, get bottomRight most index in field
        IPoint2 bottomRight = field.getIndexAtPoint(new PVector(pos.x + radius, pos.y + radius));
        for(int i = topLeft.Y; i <= bottomRight.Y; i++) {
            for (int j = topLeft.X; j <= bottomRight.X; j++) {
                PVector testPoint = field.getPointAtIndex(j, i);
                if(testPoint.dist(pos) > radius){
                    out.add(new IPoint2(j, i));
                }
            }
        }
        return out;
    }

    public static void HealDrawers(ArrayList<LineDrawer> drawers, int hp){
        for(LineDrawer x : drawers){
            x.setLifeRemaining(hp);
        }
    }
}
