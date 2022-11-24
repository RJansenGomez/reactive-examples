package org.rjansen.tests

import org.rjansen.event.MsServiceApplication
import org.rjansen.stock.MsStockApplication
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [
        MsServiceApplication::class,
        MsStockApplication::class
    ]
)
//@ContextConfiguration(
//    classes = [TestConfig::class, MetricRegistryMock::class],
//    initializers = [
//        KafkaBundleInitializer::class,
//        WiremockInitializer::class,
//        RedisInitializer::class,
//        PostgresqlContainer.PostgresqlContainerInitializer::class
//    ]
//)
class CucumberBaseApp {
}