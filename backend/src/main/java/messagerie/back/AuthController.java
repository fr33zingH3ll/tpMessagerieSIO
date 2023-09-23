package messagerie.back;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.rethinkdb.RethinkDB.r;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Result;

import entities.User;
import utils.Config;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	public static final ObjectMapper MAPPER = new ObjectMapper();
	
	private Connection connect;
	private Config config;
	
	public AuthController () {
		try {
			this.config = MAPPER.readValue(new File("./config.json"), Config.class);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		};
		this.connect = r.connection().hostname(this.config.dbHost).port(this.config.dbPort).connect();
	}
	
	@PostMapping("/login")
	public Result<User> login(@RequestParam String email, @RequestParam String password) {
		return r.db("tpMessagerie").table("user").filter(r.row("email").equals(email)).run(this.connect, User.class);
	}

}
