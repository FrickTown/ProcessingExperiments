public class IPoint2 {
    public int X,Y;
    public IPoint2(int x, int y){
        X = x;
        Y = y;
    }

    public static IPoint2 add(IPoint2 one, IPoint2 two){
        return new IPoint2(one.X + two.X, one.Y + two.Y);
    }

    public static IPoint2 subtract(IPoint2 one, IPoint2 two){
        return new IPoint2(one.X - two.X, one.Y - two.Y);
    }

    public static IPoint2 multiply(IPoint2 one, IPoint2 two){
        return new IPoint2(one.X * two.X, one.Y * two.Y);
    }

    public static IPoint2 multiply(IPoint2 one, int factor){
        return new IPoint2(one.X * factor, one.Y * factor);
    }

    public String toString(){
        return "{" + X + ", " + Y + "}";
    }

}
