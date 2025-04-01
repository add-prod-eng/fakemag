package ro.unibuc.hello.e2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/user.feature",
    tags = "@UserFeature",
    glue = {"ro.unibuc.hello.steps"}
)
public class UserE2E {
}