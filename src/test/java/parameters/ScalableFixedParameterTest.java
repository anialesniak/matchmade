package parameters;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ScalableFixedParameterTest
{
    @Test
    public void shouldExpandValueCorrectly()
    {
        // given
        int value = 10;
        int expandingStep = 5;
        ScalableFixedParameter scalableFixedParameter = new ScalableFixedParameter(value, expandingStep);

        // when
        scalableFixedParameter.expand();

        // then
        ParameterRanges range = scalableFixedParameter.getRanges();
        assertThat(range.getLower()).isEqualTo(value - expandingStep);
        assertThat(range.getUpper()).isEqualTo(value + expandingStep);
    }
}