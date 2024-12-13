package Sketchtools;

import Flipper.IPoint2;
import Sketchtools.FlowField;
import Sketchtools.LineDrawer;
import processing.core.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

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

    public static double easeInOutCirc(double x) {
        return x < 0.5
          ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2
          : (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2;
        
    }

    public static double easeInOutSine(double x){
        return -(Math.cos(Math.PI * x) - 1) / 2;

    }

    public static double easeOutQuad(double x){
        return 1 - (1 - x) * (1 - x);

    }

    public static double easeInOutBack(double x){
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        
        return x < 0.5
          ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2
          : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2;
            
    }

    public static double easeOutBounce(double x) {
        double n1 = 7.5625;
        double d1 = 2.75;
        
        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            return n1 * (x -= 1.5 / d1) * x + 0.75;
        } else if (x < 2.5 / d1) {
            return n1 * (x -= 2.25 / d1) * x + 0.9375;
        } else {
            return n1 * (x -= 2.625 / d1) * x + 0.984375;
        }
        
    }
    
    public static double easeInOutBounce(double x) {
        return x < 0.5
          ? (1 - easeOutBounce(1 - 2 * x)) / 2
          : (1 + easeOutBounce(2 * x - 1)) / 2;
        
    }

    public static PGraphics DrawFlowArrows(FlowField field, float intensity, int alpha, int skip){
        PGraphics out = field.getParent().createGraphics(field.getBounds().getWidth(), field.getBounds().getHeight());
        out.beginDraw();
        for(int i = 0; i < field.getField().length; i += skip){
            for(int j = 0; j < field.getField()[i].length; j += skip){
                PVector pos = field.getPointAtIndex(j, i).copy();
                PVector val = field.getFlowAtIndex(j, i).copy();
                if(val.x == 0 && val.y == 0)
                    continue;
                out.stroke(0, 0, 0, alpha);
                out.strokeWeight(3);
                pos = field.planeToWindow(pos);
                PVector endPoint = new PVector(pos.x + (val.x * intensity * val.z), pos.y + (val.y * intensity * val.z));
                out.line(pos.x, pos.y, endPoint.x,endPoint.y);
                out.fill(100, 140, 255, alpha);
                out.circle(pos.x, pos.y, 6);
                out.fill(255, 130, 100, 255);
                out.circle(endPoint.x, endPoint.y, 3);
            }
        }
        out.endDraw();
        return out;
    }
    public static PGraphics GenerateHeatMapZ (FlowField field, int skip){
        PGraphics out = field.getParent().createGraphics(field.getBounds().getWidth(), field.getBounds().getHeight());
        int lowColor = Color.decode("#285bd1").getRGB();
        int highColor = Color.decode("#d1283b").getRGB();
        out.beginDraw();
        for(int i = 0; i < field.getField().length; i+=skip) {
            for (int j = 0; j < field.getField()[i].length; j+=skip) {
                PVector pos = field.getPointAtIndex(j, i).copy();
                pos = field.planeToWindow(pos);
                PVector val = field.getFlowAtIndex(j, i).copy();
                out.noStroke();
                out.fill(PGraphics.lerpColor(lowColor, highColor, val.z, PConstants.RGB));
                out.circle(pos.x, pos.y, 10);
            }
        }
        out.endDraw();
        return out;
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

    public static void ResetDrawers(ArrayList<LineDrawer> drawers, FlowField field){
        for(LineDrawer x : drawers){
            x.resetDrawer(field);
        }
    }
}
