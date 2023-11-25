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
                if(!negative)
                    fromPointToCenter = fromPointToCenter.mult(-1);
                //Get the original, undistorted flow
                PVector unDistorted = field.getFlowAtIndex(j, i);
                //Get the intensity
                //float adjustedIntensity = PApplet.map(intensity, 0, 255, 1f, 10);
                //dist == radius => 0.05 => ease(0.05) => close to beginning (1)
                //dist == 0 => 0.99 => ease(1) => close to end (0);
                float calcIntensity = PApplet.map(dist, 0, radius, 0.5f, 0.05f);
                calcIntensity = (float) Tools.easeOutCirc((double) calcIntensity);
                fromPointToCenter.z = calcIntensity;
                PVector newFlow = PVector.lerp(unDistorted, fromPointToCenter, fromPointToCenter.z);
                field.setFlowAtIndex(j, i, newFlow);
            }
        }
    }
}
