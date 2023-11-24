package Sketchtools;

import Flipper.IPoint2;

public class Boundary {

    public final int left, right, bottom, top;

    public Boundary(int leftMost, int rightMost, int topMost, int bottomMost){
        this.left = leftMost;
        this.right = rightMost;
        this.top = topMost;
        this.bottom = bottomMost;
    }

    public int getWidth(){
        return right - left;
    }

    public int getHeight(){
        return bottom - top;
    }

    public IPoint2 getCenter(){
        return new IPoint2(left + getWidth(), top + getHeight());
    }
}
