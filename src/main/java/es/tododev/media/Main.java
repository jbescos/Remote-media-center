package es.tododev.media;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
	
	private final static Logger log = LogManager.getLogger();

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Main.class, args);
		log.debug("Applicationstarted");
	}
}
