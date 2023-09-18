package messagerie.back;

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

import controller.Config;
import entities.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private Connection connect;
	private Config config;
	
	public UserController(Config config, Connection connect) {
		this.config = config;
		this.connect = connect;
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
