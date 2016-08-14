package setpdefs.helpandsupport;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {"json:target/helpsupport_cucumber.json", "html:target/cucumber/helpsupport"},
        features = "src/test/resources/features/helpandsupport",
        tags = {"@errormessagevalidationforbranch"})
public class runTest {
}
