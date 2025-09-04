package com.bank.client_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class ClientServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(ClientServiceApplication.class);

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
		Locale.setDefault(Locale.US);

		SpringApplication app = new SpringApplication(ClientServiceApplication.class);
		app.setBannerMode(Banner.Mode.CONSOLE);
		app.run(args);
	}

	// initialization log
	@Bean
	public ApplicationRunner logAppInfo(Environment env) {
		return args -> {
			String name = env.getProperty("spring.application.name", "client-service");
			String port = env.getProperty("local.server.port", env.getProperty("server.port", "8080"));
			log.info("App '{}' running on port {}", name, port);
		};
	}

	// DB connection check
	@Bean
	public ApplicationRunner dbHealthcheck(DataSource dataSource) {
		return args -> {
			try (Connection conn = dataSource.getConnection()) {
				DatabaseMetaData meta = conn.getMetaData();
				log.info("DB connection check: {} {} (driver: {} {})",
						meta.getDatabaseProductName(),
						meta.getDatabaseProductVersion(),
						meta.getDriverName(),
						meta.getDriverVersion());
			} catch (SQLException e) {
				log.error("No DB connection at service start", e);
				throw e;
			}
		};
	}
}
