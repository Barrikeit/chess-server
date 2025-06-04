package org.barrikeit.chess;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.chess.core.config.ApplicationProperties;
import org.barrikeit.chess.security.config.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Log4j2
@SpringBootApplication
@EnableConfigurationProperties({
  ApplicationProperties.GenericProperties.class,
  ApplicationProperties.ServerProperties.class,
  ApplicationProperties.MailProperties.class,
  SecurityProperties.class
})
public class ChessApplication {
  public static void main(String[] args) {
    SpringApplication.run(ChessApplication.class, args);
  }
}
