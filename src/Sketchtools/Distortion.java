package Sketchtools;

import Flipper.IPoint2;
import PixelSorting.Tools;
import processing.core.PApplet;
import processing.core.PVector;

public class Distortion {

    public static void RadialDistortion (FlowField field, PVector pos, float radius, float intensity){
        PVector adjustedPos = field.windowToPlane(pos);
        //We need the boundary of points in the field that will be affected by the distortion
        //First, get topLeft most index in field
        IPoint2 topLeft = field.getIndexAtPoint(new PVector(pos.x - radius, pos.y - radius));
        //Then, get bottomRight most index in field
        IPoint2 bottomRight = field.getIndexAtPoint(new PVector(pos.x + radius, pos.y + radius));

        //Loop through these points on the field and calculate its flow from the radial point
        for(int i = topLeft.Y; i <= bottomRight.Y; i++){
            for(int j = topLeft.X; j <= bottomRight.X; j++){
                //Get the flowpoint's position on the fields coordinate space
                PVector testPoint = field.getPointAtIndex(j, i);
                PVector between = PVector.sub(testPoint, pos).normalize();
                PVector newFlow = between;
                //See if the flowpoint lands inside the circle
                float dist = pos.dist(testPoint);
                if(dist > radius)
                    continue;
                //If it does...
                //First, determine the distance from the center (save the signum)
                float distX = testPoint.x - pos.x;
                int sigX = (int) Math.signum(distX);
                distX = Math.abs(distX);

                float distY = testPoint.y - pos.y;
                int sigY = (int) Math.signum(distY);
                distY = Math.abs(distY);

                //Then, calculate its flow vector
                float magX = PApplet.map(distX, 0, radius, 0, 1) * intensity * sigX;
                float magY = PApplet.map(distY, 0, radius, 0, 1) * intensity * sigY;

                //Then, apply easing based on proximity to edge
                float factor = PApplet.map(dist, 0, radius, 0f, 1f);
                //factor = (float) Tools.easeOutCirc(factor);
                PVector out = PVector.lerp(new PVector(magX, magY), field.getFlowAtIndex(j, i), factor);
                //Apply the flow vector
                field.setFlowAtIndex(j, i, newFlow);
            }
        }
    }
}
