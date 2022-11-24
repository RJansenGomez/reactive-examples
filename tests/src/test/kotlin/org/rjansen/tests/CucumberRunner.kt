package org.rjansen.tests

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    stepNotifications = true,
    features = ["src/testIntegration/resources/features"],
    glue = ["com.manomano.ms.selligent.sync.steps"],
    plugin = ["pretty"],
    tags = "not @ignored",
)
class CucumberRunner {
}