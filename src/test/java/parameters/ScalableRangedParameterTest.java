package parameters;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

/**
 * Created by annterina on 19.11.17.
 */
public class ScalableRangedParameterTest {

    @Test
    public void calculatePerfectMatchPercentage() throws Exception {
        ScalableRangedParameter parameter = new ScalableRangedParameter(3.5, 4.5);
        double matchResult = parameter.calculateMatchPercentage(new NonScalableFixedParameter(3.75));

        assertThat(matchResult).isCloseTo(new Double(1), within(Constants.EPSILON));
    }

    @Test
    public void calculateNoMatchPercentage() throws Exception {
        ScalableRangedParameter parameter = new ScalableRangedParameter(3.5, 4.5);
        parameter.expandBy(0.5);
        double matchResult = parameter.calculateMatchPercentage(new NonScalableFixedParameter(5.75));

        assertThat(matchResult).isCloseTo(new Double(0), within(Constants.EPSILON));
    }

    @Test
    public void calculateHalfwayMatchPercentage() throws Exception {
        ScalableRangedParameter parameter = new ScalableRangedParameter(3.5, 4.5);
        parameter.expandBy(0.5);
        double matchResult = parameter.calculateMatchPercentage(new NonScalableFixedParameter(4.75));

        assertThat(matchResult).isCloseTo(new Double(0.5), within(Constants.EPSILON));
    }

    @Test
    public void calculateRealNumbersMatchPercentage() throws Exception {
        ScalableRangedParameter parameter = new ScalableRangedParameter(3.125, 4.125);
        parameter.expandBy(0.125);
        double matchResult1 = parameter.calculateMatchPercentage(new NonScalableFixedParameter(3.123));
        double matchResult2 = parameter.calculateMatchPercentage(new NonScalableFixedParameter(4.126));

        assertThat(matchResult1).isLessThan(matchResult2);
    }

}