package parameters;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NonScalableRangedParameterTest
{
    @Test
    public void shouldNotChangeRangeAfterExpand()
    {
        // given
        int lower = 10;
        int upper = 20;
        NonScalableRangedParameter nonScalableRangedParameter = new NonScalableRangedParameter(lower, upper);

        // when
        nonScalableRangedParameter.expand();

        // then
        ParameterRanges range = nonScalableRangedParameter.getRanges();
        assertThat(range.getLower()).isEqualTo(lower);
        assertThat(range.getUpper()).isEqualTo(upper);
    }
}