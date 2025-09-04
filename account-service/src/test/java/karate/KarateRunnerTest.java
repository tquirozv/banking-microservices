package karate;

import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
        classes = com.bank.account_service.AccountServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.web-application-type=servlet",
                "spring.main.allow-bean-definition-overriding=true"
        }
)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class KarateRunnerTest {

    @LocalServerPort
    private int port;

    @Karate.Test
    Karate testAccounts() {
        System.setProperty("karate.server.port", String.valueOf(port));
        return Karate.run("accounts/accounts")
                .relativeTo(getClass())
                .systemProperty("karate.env", "test");
    }

    @Karate.Test
    Karate testMovements() {
        System.setProperty("karate.server.port", String.valueOf(port));
        return Karate.run("movements/movements")
                .relativeTo(getClass())
                .systemProperty("karate.env", "test");
    }

//    @LocalServerPort
//    private int port;
//
//    @Karate.Test
//    Karate runClientSuite() {
//        System.setProperty("karate.server.port", String.valueOf(port));
//        return Karate.run().relativeTo(getClass());
//    }
}