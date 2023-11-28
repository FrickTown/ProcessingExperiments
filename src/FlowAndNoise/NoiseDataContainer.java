package FlowAndNoise;

import imgui.ImGui;
import processing.core.PApplet;

public class NoiseDataContainer {

    public float getFallOff() {
        return fallOff;
    }

    public void setFallOff(float fallOff) {
        if(fallOff > 1){
            fallOff = 1;
            throw new Error("Falloff value higher than 1, value set to 1.");
        }
        if(fallOff < 0){
            fallOff = 0;
            throw new Error("Falloff value lower than 0, value set to 0.");
        }
        this.fallOff = fallOff;
    }

    public int getLod() {
        return lod;
    }

    public void setLod(int lod) {
        if(lod < 1){
            lod = 1;
            throw new Error("lod value less than 1, value set to 1");
        }
        this.lod = lod;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    private long seed;
    private float fallOff;
    private int lod;
    public String getName() {
        return name;
    }

    private final String name;

    public NoiseDataContainer(String name, long seed, Float fallOff, Integer lod){
        this.name = name;
        this.fallOff = fallOff == null ? 0.5f : fallOff;
        this.lod = lod == null ? 4 : lod;
        this.seed = seed;
    }

    public void updateAll(long seed, Float fallOff, Integer lod){
        setFallOff(fallOff);
        setLod(lod);
        setSeed(seed);
    }

    public void scrambleSeed(){
        PApplet mock = new PApplet();
        seed = (long) mock.random(-100000000, 100000000);
    }

}
