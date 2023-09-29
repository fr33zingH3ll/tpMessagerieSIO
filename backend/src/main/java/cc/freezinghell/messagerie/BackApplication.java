package cc.freezinghell.messagerie;

import static com.rethinkdb.RethinkDB.r;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.rethinkdb.net.Connection;

import cc.freezinghell.messagerie.config.Config;
import cc.freezinghell.messagerie.utils.DbInitializer;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * recupération de la config et lancement de l'application
 */

@SpringBootApplication
public class BackApplication {

	public static final ObjectMapper MAPPER = new ObjectMapper();

	private static Config config;
	private static Connection connect;

	public static void main(String[] args) {
		BackApplication main = new BackApplication();
		main.start(args);
	}

	public BackApplication() {
		try {
			config = MAPPER.readValue(new File("./config.json"), Config.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		connect = r.connection(config.dbUrl).connect();
		DbInitializer dbInitializer = new DbInitializer(config, connect);
		dbInitializer.tablesInit();
	}

	public static Config getConfig() {
		return config;
	}

	public static Connection getConnect() {
		return connect;
	}

	public void start(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}
}
