package parameters;

import static org.assertj.core.api.Assertions.*;
import org.junit.*;

/**
 * Created by annterina on 19.11.17.
 */
public class ScalableFixedParameterTest {

    @Test
    public void calculatePerfectMatchPercentage() throws Exception {
        ScalableFixedParameter parameter = new ScalableFixedParameter(10);
        double matchResult = parameter.calculateMatchPercentage(new NonScalableFixedParameter(10));

        assertThat(matchResult).isEqualTo(1);
    }

    @Test
    public void calculateNoMatchPercentage() throws Exception {
        ScalableFixedParameter parameter = new ScalableFixedParameter(10);
        parameter.expandBy(5);
        double matchResult = parameter.calculateMatchPercentage(new NonScalableFixedParameter(20));

        assertThat(matchResult).isEqualTo(0);
    }

    @Test
    public void calculateMarginMatchPercentage() throws Exception {
        ScalableFixedParameter parameter = new ScalableFixedParameter(10);
        parameter.expandBy(2);
        double matchResult = parameter.calculateMatchPercentage(new NonScalableFixedParameter(12));

        assertThat(matchResult).isEqualTo(0);
    }

    @Test
    public void calculateHalfwayMatchPercentage() throws Exception {
        ScalableFixedParameter parameter = new ScalableFixedParameter(10);
        parameter.expandBy(4);
        double matchResult = parameter.calculateMatchPercentage(new NonScalableFixedParameter(12));

        assertThat(matchResult).isGreaterThan(0);
        assertThat(matchResult).isEqualTo(0.5);

    }

    @Test
    public void calculateRealNumbersMatchPercentage() throws Exception {
        ScalableFixedParameter parameter = new ScalableFixedParameter(14074.34);
        parameter.expandBy(51.76);
        double matchResult1 = parameter.calculateMatchPercentage(new NonScalableFixedParameter(14100.35));
        double matchResult2 = parameter.calculateMatchPercentage(new NonScalableFixedParameter(14120.35));

        assertThat(matchResult1).isGreaterThan(matchResult2);
    }


}