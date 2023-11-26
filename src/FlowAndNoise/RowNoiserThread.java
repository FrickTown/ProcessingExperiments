package FlowAndNoise;

import processing.core.*;

public class RowNoiserThread extends Thread{
    PVector[] row;
    PApplet mockApplet;
    long aseed;
    long iseed;
    int y;
    public RowNoiserThread(int y, PVector[] row, long aseed, long iseed){
        this.row = row;
        this.aseed = aseed;
        this.iseed = iseed;
        this.y = y;
        mockApplet = new PApplet();
    }
    public void run(){
        for(int x = 0; x < row.length; x++){
            mockApplet.noiseSeed(aseed);
            mockApplet.noiseDetail(4, 0.5f);
            float angleNoise = mockApplet.noise((float)x * 0.005f, (float)y * 0.005f);
            row[x] = PVector.fromAngle(PApplet.map(angleNoise, 0.0f, 1.0f, 0.0f, PApplet.PI * 2.0f));
            mockApplet.noiseSeed(iseed);
            mockApplet.noiseDetail(4, 0.75f);
            float intensityNoise = mockApplet.noise((float)x * 0.005f, (float)y * 0.005f);
            row[x].z = intensityNoise;
        }
    }
}
