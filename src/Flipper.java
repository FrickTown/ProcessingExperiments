import processing.core.PApplet;

import java.lang.reflect.InvocationTargetException;

public class Flipper extends PApplet {
    Tile[][] Tiles;
    int xTiles = 40;
    int yTiles = 20;
    FPoint2 tileSize;

    boolean mouseDown = false;

    public void settings(){
        size(displayWidth/2, displayHeight/2 - 40, P3D);
    }

    public void setup(){
        windowMove(displayWidth/2 - 10, displayHeight/2 - 20);
        tileSize = new FPoint2((float) width /xTiles, (float) height /yTiles);
        Tiles = new Tile[yTiles][xTiles];
        for(int y = 0; y < Tiles.length; y++){
            for (int x = 0; x < Tiles[y].length; x++) {
                Tiles[y][x] = new Tile(x, y, this);
            }
        }
        for(int y = 0; y < Tiles.length; y++){
            for (int x = 0; x < Tiles[y].length; x++) {
                Tiles[y][x].DirectNeighbors = Tile.findNeighbors(x, y, Tiles);
            }
        }
    }

    public void draw(){
        IPoint2 mousedTile = screenToGrid();
        if(Tiles[mousedTile.Y][mousedTile.X].idle) {
            try {
                if(mouseDown && mouseButton == LEFT)
                    Tiles[mousedTile.Y][mousedTile.X].initYawAnim();
                else if(mouseDown && mouseButton == RIGHT)
                    Tiles[mousedTile.Y][mousedTile.X].initRadialYawAnim();

            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        background(15, 2, 32);
        //translate(-tileSize.X/2, -tileSize.Y/2);
        for(Tile[] y : Tiles){
            pushMatrix();
            translate(0, y[0].y + tileSize.Y/2);
            for(Tile x : y){
                try {
                    x.Update();
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                pushMatrix();
                stroke(210, 222, 50);
                fill(97 + x.yaw/2f, 163, 186 - x.yaw/4f);
                translate(x.x + tileSize.X/2, 0);
                rotateX(radians(x.pitch));
                rotateY(radians(x.yaw));
                rotateZ(radians(x.roll));
                rectMode(CENTER);
                rect(0, 0, x.width, x.height);
                popMatrix();
            }
            popMatrix();
        }
    }

    public void mousePressed(){
        IPoint2 mousedTile = screenToGrid();
        println(mousedTile + ", " + Tiles[mousedTile.Y][mousedTile.X].yaw);
        mouseDown = true;
    }

    public void mouseReleased(){
        mouseDown = false;
    }

    IPoint2 screenToGrid(){
        float postModX = mouseX % tileSize.X;
        float preModX = mouseX - postModX;
        int coordX = (int) (preModX / tileSize.X);

        float postModY = mouseY % tileSize.Y;
        float preModY = mouseY - postModY;
        int coordY = (int) (preModY / tileSize.Y);

        return new IPoint2(coordX, coordY);
    }

}
