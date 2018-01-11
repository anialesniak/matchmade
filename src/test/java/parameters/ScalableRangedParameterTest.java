package parameters;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ScalableRangedParameterTest
{
    @Test
    public void shouldExpandRangeCorrectly() throws Exception
    {
        // given
        int lower = 10;
        int upper = 20;
        int expandingStep = 5;
        ScalableRangedParameter scalableRangedParameter = new ScalableRangedParameter(lower, upper, expandingStep);

        // when
        scalableRangedParameter.expand();

        // then
        ParameterRanges range = scalableRangedParameter.getRanges();
        assertThat(range.getLower()).isEqualTo(lower - expandingStep);
        assertThat(range.getUpper()).isEqualTo(upper + expandingStep);
    }
}