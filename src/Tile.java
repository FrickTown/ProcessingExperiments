import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Tile {
    float x,y,width,height,pitch,yaw,roll;
    int tileX, tileY;
    public boolean idle = true;
    boolean reduceYaw = false;
    boolean allowInfect = true;
    Method activeAnim = null;
    ArrayList<Tile> DirectNeighbors;
    public Tile(int tileX, int tileY, Flipper main){
        this.tileX = tileX;
        this.tileY = tileY;
        this.width = main.tileSize.X;
        this.height = main.tileSize.Y;
        this.x = tileX*width;
        this.y = tileY*height;
        pitch = 0;
        yaw = 0;
        roll = 0;
    }

    public void Update() throws InvocationTargetException, IllegalAccessException {
        if(activeAnim != null){

            activeAnim.invoke(this, new Object[0]);
        }
    }

    public void initYawAnim() throws NoSuchMethodException {
        idle = false;
        activeAnim = Tile.class.getMethod("yawAnim", new Class[0]);
    }

    public void yawAnim(){
        yaw += 2;
        if(yaw % 180 == 0){
            activeAnim = null;
            idle = true;
            yaw = 0;
        }
    }

    public void initYawDown(){
        reduceYaw = true;
    }

    public void initPitchAnim() throws NoSuchMethodException {
        idle = false;
        activeAnim = Tile.class.getMethod("pitchAnim", new Class[0]);
    }

    public void pitchAnim() {
        pitch += 2;
        if (pitch % 180 == 0) {
            activeAnim = null;
            idle = true;
            pitch = 0;
        }
    }
    public void initRadialYawAnim() throws NoSuchMethodException {
        idle = false;
        activeAnim = Tile.class.getMethod("radialYawAnim", new Class[0]);
        allowInfect = true;
        infectTick = 0;
    }
    int infectTick = 0;

    public void radialYawAnim() throws NoSuchMethodException {
        if(!reduceYaw) {
            yaw += 4f;
            if (yaw > 361) {
                reduceYaw = true;
                yaw -= 4f;
            }
        }
        else if (reduceYaw){
            yaw -= 4;
            if (yaw < -1) {
                yaw += 4f;
                reduceYaw = false;
            }
        }
        if(infectTick++ == 10 && allowInfect){
            allowInfect = false;
            infectTick = 0;
            for(Tile x : DirectNeighbors){
                if(x.idle)
                    x.initRadialYawAnim();
            }
        }
        if(yaw % 180 == 0){
            activeAnim = null;
            idle = true;
            allowInfect = true;
        }
    }

    public static ArrayList<Tile> findNeighbors(int tileX, int tileY, Tile[][] tileMap){
        ArrayList<Tile> returnable = new ArrayList<Tile>();
        if(tileX != tileMap[0].length-1){
            returnable.add(tileMap[tileY][tileX+1]);
        }
        if(tileX != 0){
            returnable.add(tileMap[tileY][tileX-1]);
        }
        if(tileY != tileMap.length-1){
            returnable.add(tileMap[tileY+1][tileX]);
        }
        if(tileY != 0){
            returnable.add(tileMap[tileY-1][tileX]);
        }

        return returnable;
    }
}
