package ca.yorku.group12.eecs4413.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:notifications",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.flyway.enabled=false"
})
class NotificationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
