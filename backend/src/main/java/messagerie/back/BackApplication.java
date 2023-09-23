package messagerie.back;

import static com.rethinkdb.RethinkDB.r;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.rethinkdb.net.Connection;

import utils.Config;
import utils.DbInitializer;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class BackApplication {
	
	public static final ObjectMapper MAPPER = new ObjectMapper();
	
	private final Config config;
	private final Connection connect;
	
	public static void main(String[] args) {
		BackApplication main = new BackApplication();
		main.start(args);
	}
	
	public BackApplication() {
		try {
			this.config = MAPPER.readValue(new File("./config.json"), Config.class);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		};
		this.connect = r.connection().hostname(this.config.dbHost).port(this.config.dbPort).connect();
		DbInitializer dbInitializer = new DbInitializer(this.config, this.connect);
		
	}
	
	public void start(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}
}