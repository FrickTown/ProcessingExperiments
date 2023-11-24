package Sketchtools;

import Flipper.IPoint2;
import processing.core.PApplet;
import processing.core.PVector;

public class Distortion {

    public static void RadialDistortion (FlowField field, PVector pos, float radius, float intensity, Boolean negative){
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
                //See if the flowpoint lands inside the circle
                float dist = pos.dist(testPoint);
                if(dist > radius)
                    continue;
                //Calculate the direction of the new flow (towards center)
                PVector fromPointToCenter = pos.copy().sub(testPoint).normalize();
                //Get the original, undistorted flow
                PVector unDistorted = field.getFlowAtIndex(j, i);
                float calcIntensity = PApplet.map(dist, radius, 0, 0, 1);
                fromPointToCenter.z = negative ? calcIntensity : -calcIntensity;
                field.setFlowAtIndex(j, i, fromPointToCenter);
            }
        }
    }
}
