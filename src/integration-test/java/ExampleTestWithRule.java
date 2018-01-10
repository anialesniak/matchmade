import org.junit.ClassRule;
import org.junit.Test;
import rules.ClientEnrollmentTestRule;

public class ExampleTestWithRule
{
    @ClassRule
    public static final ClientEnrollmentTestRule TEST_RULE = new ClientEnrollmentTestRule(
            "localhost", 8080
    );

    @Test
    public void shouldSendClientRequest() throws Exception
    {
        TEST_RULE.enrollClient("resources/clients/client0.json");
    }
}
