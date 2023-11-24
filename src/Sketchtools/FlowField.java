package Sketchtools;

import Flipper.IPoint2;
import processing.core.PApplet;
import processing.core.PVector;

public class FlowField {
    private PApplet parent;
    private PVector[][] field;
    private float resolution;
    private Boundary bounds;

    public FlowField(PApplet parent, float resolutionMod, float boundaryMod){
        this.parent = parent;
        bounds = new Boundary(
                (int)(parent.width * -(1 - boundaryMod)),
                (int)(parent.width * (1 + boundaryMod)),
                (int)(parent.height * -(1 - boundaryMod)),
                (int)(parent.height * (1 + boundaryMod))
                );
        resolution = (int) (parent.width * resolutionMod);
        int columnCount = (int) ((bounds.getWidth()) / resolution);
        int rowCount = (int) ((bounds.getHeight()) / resolution);
        field = new PVector[rowCount][columnCount];
    }

    public PApplet getParent() {
        return parent;
    }

    public PVector[][] getField() {
        return field;
    }

    public void setField(PVector[][] field) {
        this.field = field;
    }

    public Boundary getBounds() {
        return bounds;
    }

    public PVector getFlowAtPoint(float x, float y){
        IPoint2 index = getIndexAtPoint(new PVector(x,y));
        return index != null ? field[index.Y][index.X] : null;
    }

    public PVector getFlowAtPoint(PVector point){
        return getFlowAtPoint(point.x, point.y);
    }

    public PVector getFlowAtIndex(int x, int y){
        return field[y][x];
    }

    public void setFlowAtIndex(int x, int y, PVector value){
        field[y][x] = value;
    }

    public PVector windowToPlane(PVector point){
        return new PVector(point.x - bounds.left, point.y - bounds.top);
    }

    public PVector planeToWindow(PVector point){
        return new PVector(point.x + bounds.left, point.y + bounds.top);
    }

    public IPoint2 getIndexAtPoint(PVector point){
        int colIndex = (int) (point.x / resolution);
        int rowIndex = (int) (point.y / resolution);
        if (colIndex >= field[0].length || colIndex < 0 || rowIndex >= field.length || rowIndex < 0)
            return null;
        return new IPoint2(colIndex, rowIndex);
    }

    public PVector getPointAtIndex(IPoint2 index){
        if (index.X >= field[0].length || index.X < 0 || index.Y >= field.length || index.Y < 0)
            return null;
        float xPos = index.X * resolution;
        float yPos = index.Y * resolution;
        return new PVector(xPos, yPos);
    }

    public PVector getPointAtIndex(int x, int y){
        return getPointAtIndex(new IPoint2(x, y));
    }

    public void influenceLinedrawer(LineDrawer toInfluence){
        PVector flow = getFlowAtPoint(toInfluence.getPos().x, toInfluence.getPos().y);
        if(flow == null)
            return;
        toInfluence.setDir(flow);
    }

    public void influenceLinedrawerDynamic(LineDrawer toInfluence){
        PVector flow = getFlowAtPoint(toInfluence.getPos().x, toInfluence.getPos().y);
        if(flow == null)
            return;
        PVector newFlow = PVector.lerp(toInfluence.getDir(), flow, flow.z);
    }
}
