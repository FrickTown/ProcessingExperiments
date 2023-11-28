package FlowAndNoise;

import processing.core.*;

public class RowNoiserThread extends Thread{
    PVector[] row;
    PApplet mockApplet;
    NoiseDataContainer aData;
    NoiseDataContainer iData;
    int y;
    public RowNoiserThread(int y, PVector[] row, NoiseDataContainer aData, NoiseDataContainer iData){
        this.row = row;
        this.aData = aData;
        this.iData = iData;
        this.y = y;
        mockApplet = new PApplet();
    }
    public void run(){
        for(int x = 0; x < row.length; x++){
            //System.out.println("Now generating with lod " + data.getLod() + ", and falloff " + data.getFallOff());
            //Set angle
            mockApplet.noiseSeed(aData.getSeed().longValue());
            mockApplet.noiseDetail(aData.getLod().get(), aData.getFallOff().get());
            float angleNoise = mockApplet.noise((float)x * 0.005f, (float)y * 0.005f);
            row[x] = PVector.fromAngle(PApplet.map(angleNoise, 0.0f, 1.0f, 0.0f, PApplet.PI * 2.0f));

            //Set intensity
            mockApplet.noiseSeed(iData.getSeed().longValue());
            mockApplet.noiseDetail(iData.getLod().get(), iData.getFallOff().get());
            float intensityNoise = mockApplet.noise((float)x * 0.005f, (float)y * 0.005f);
            row[x].z = intensityNoise;
        }
    }
}
