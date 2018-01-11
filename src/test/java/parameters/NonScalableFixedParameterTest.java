package parameters;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NonScalableFixedParameterTest
{
    @Test
    public void shouldNotBeAffectedByExpand() throws Exception
    {
        // given
        int value = 15;
        NonScalableFixedParameter nonScalableFixedParameter = new NonScalableFixedParameter(value);

        // when
        nonScalableFixedParameter.expand();

        // then
        ParameterRanges range = nonScalableFixedParameter.getRanges();
        assertThat(range.getLower()).isEqualTo(value);
        assertThat(range.getUpper()).isEqualTo(value);
    }
}