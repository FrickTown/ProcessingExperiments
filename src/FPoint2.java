public class FPoint2 {
    public float X,Y;
    public FPoint2(float x, float y){
        X = x;
        Y = y;
    }

    public static FPoint2 add(FPoint2 one, FPoint2 two){
        return new FPoint2(one.X + two.X, one.Y + two.Y);
    }

    public static FPoint2 subtract(FPoint2 one, FPoint2 two){
        return new FPoint2(one.X - two.X, one.Y - two.Y);
    }

    public static FPoint2 multiply(FPoint2 one, FPoint2 two){
        return new FPoint2(one.X * two.X, one.Y * two.Y);
    }

    public static FPoint2 multiply(FPoint2 one, float factor){
        return new FPoint2(one.X * factor, one.Y * factor);
    }

    public String toString(){
        return "{" + X + ", " + Y + "}";
    }

}
