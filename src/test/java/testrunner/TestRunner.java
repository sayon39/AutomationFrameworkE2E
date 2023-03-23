package testrunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features ="src/test/java/features", glue={"stepDefinitions"},

                    monochrome =true, plugin = {"pretty", "html:target/cucumber-reports",
                      "json:target/cucumber.json","junit:target/cucumber.xml"})
public class TestRunner extends AbstractTestNGCucumberTests{
}
