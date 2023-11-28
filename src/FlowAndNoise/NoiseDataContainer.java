package FlowAndNoise;

import imgui.ImGui;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import processing.core.PApplet;

public class NoiseDataContainer {

    public String getName() {
        return name;
    }

    public ImInt getSeed() {
        return seed;
    }

    public ImFloat getFallOff() {
        return fallOff;
    }

    public ImInt getLod() {
        return lod;
    }

    private final String name;
    private final ImInt seed;
    private final ImFloat fallOff;
    private final ImInt lod;

    public NoiseDataContainer(String name, Integer seed, Float fallOff, Integer lod){
        this.name = name;
        this.fallOff = fallOff == null ? new ImFloat(0.5f) : new ImFloat(fallOff);
        this.lod = lod == null ? new ImInt(4) : new ImInt(lod);
        this.seed = new ImInt(seed);
    }

    public void updateAll(int seed, Float fallOff, Integer lod){
        if(fallOff > 1){
            this.fallOff.set(1);
            throw new Error("fallOff higher than 1, setting value to 1");
        }
        if(fallOff < 0){
            this.fallOff.set(0);
            throw new Error("falloff lower than 0, setting value to 0");
        }
        if(lod < 1){
            this.lod.set(1);
            throw new Error("lod lower than 1, setting value to 1");
        }
        this.seed.set(seed);
    }

    public void scrambleSeed(){
        PApplet mock = new PApplet();
        seed.set((int) mock.random(-100000000, 100000000));
    }

}
