package parameters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DeserializationTest
{
    @Test
    public void shouldDeserializeTest() throws Exception
    {
        //given
        final String json = Resources.toString(
                Resources.getResource("parameter-map.json"), StandardCharsets.UTF_8);
        final ObjectMapper objectMapper = new ObjectMapper();
        final TypeReference<Map<String, Map<String, Parameter>>> typeReference =
                new TypeReference<Map<String, Map<String, Parameter>>>() {};
        //when
        final Map<String, Map<String, Parameter>> map = objectMapper.readValue(json, typeReference);
        //then
        assertThat(map)
                .isNotNull()
                .hasSize(2)
                .containsKey("clientSelf")
                .containsKey("clientSearching");
    }

    @Test
    public void shouldDeserializeToSpecificParameterTypeTest() throws Exception
    {
        //given
        final String json = Resources.toString(
                Resources.getResource("non-scalable-fixed-parameter-map.json"), StandardCharsets.UTF_8);
        final ObjectMapper objectMapper = new ObjectMapper();
        final TypeReference<Map<String, NonScalableFixedParameter>> typeReference =
                new TypeReference<Map<String, NonScalableFixedParameter>>() {};
        //when
        final Map<String, NonScalableFixedParameter> map = objectMapper.readValue(json, typeReference);
        //then
        assertThat(map)
                .isNotNull()
                .hasSize(3)
                .containsKey("age")
                .containsKey("weight")
                .containsKey("height");
    }
}
