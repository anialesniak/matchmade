package parameters;

public class ParameterRanges {

    private final double lower;
    private final double upper;

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
