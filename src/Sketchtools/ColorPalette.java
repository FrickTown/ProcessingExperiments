package Sketchtools;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.util.HashMap;

public class ColorPalette extends HashMap<String, float[]> {
        @SafeVarargs
        public static ColorPalette createFromStrings(Entry<String, String>... entries){
            ColorPalette out = new ColorPalette();
            for(Entry<String, String> x : entries){
                out.put(x.getKey(), Color.decode(x.getValue()).getRGBColorComponents(null));
            }
            return out;
        }
        public static ColorPalette createFromFloats(Entry<String, float[]>... entries){
            ColorPalette out = new ColorPalette();
            for(Entry<String, float[]> x : entries){
                out.put(x.getKey(), x.getValue());
            }
            return out;
        }

        public void setColorString(String name, String httpCode){
            if(this.containsKey(name)){
                this.put(name, Color.decode(httpCode).getRGBColorComponents(null));
            }
            else {
                throw new Error("Color not updated, incorrect name supplied.");
            }
        }

        public void setColorRGB(String name, float[] rgbVal){
            if(this.containsKey(name) && rgbVal.length == 3){
                this.put(name, rgbVal);
            }
            else {
                throw new Error("Color not updated, incorrect name supplied or invalid color format.");
            }
        }

        public Color asColor(String name){
            if(this.containsKey(name)){
                return new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB), this.get(name), 1);
            }
            else {
                return null;
            }
        }

        public int asRGB(String name){
            if(this.containsKey(name)){
                return new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB), this.get(name), 1).getRGB();
            }
            else {
                return 0;
            }
        }
}
