package PixelSorting;

public class Tools {
    public static double easeOutExpo(double x){
        return x == 1 ? 1 : 1 - Math.pow(2, -10 * x);
    }

    public static double easeInOutExpo(double x){
        return x == 0
                ? 0
                : x == 1
                ? 1
                : x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
                : (2 - Math.pow(2, -20 * x + 10)) / 2;

    }

    public static double easeOutCirc(double x){
        return Math.sqrt(1 - Math.pow(x - 1, 2));
    }

    public static double easeInOutSine(double x){
        return -(Math.cos(Math.PI * x) - 1) / 2;

    }

    public static double easeOutQuad(double x){
        return 1 - (1 - x) * (1 - x);

    }
}
