package messagerie.back;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.rethinkdb.RethinkDB.r;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Result;

import com.fasterxml.jackson.databind.ObjectMapper;

import entities.User;
import utils.Config;

@RestController
@RequestMapping("/user")
public class UserController {
	
	public static final ObjectMapper MAPPER = new ObjectMapper();
	
	private Connection connect;
	private Config config;
	
	public UserController() {
		try {
			this.config = MAPPER.readValue(new File("./config.json"), Config.class);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		};
		this.connect = r.connection().hostname(this.config.dbHost).port(this.config.dbPort).connect();
	}
	
	@RequestMapping("/")
	@ResponseBody
	public String index() {
		return "hello world";
	}
	
	@GetMapping("/all")
	public Result<List<User>> getAllUser() {
		return null;
	}
	
	@PostMapping("/{id}")
	public Result<User> getUserById(@RequestParam String id) {
		return null;
	}
}
