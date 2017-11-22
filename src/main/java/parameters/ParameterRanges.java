package parameters;

/**
 * Created by kenjik on 22.11.17.
 */
public class ParameterRanges {
    private double lower;
    private double upper;
    public ParameterRanges(double lower, double upper){
        this.lower = lower;
        this.upper = upper;
    }
    public double getLower(){
        return lower;
    }
    public double getUpper(){
        return upper;
    }
}
