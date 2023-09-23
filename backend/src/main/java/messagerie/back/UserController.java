package messagerie.back;

import java.io.IOException;
import java.util.List;

import static com.rethinkdb.RethinkDB.r;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rethinkdb.net.Result;


import entities.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	public UserController() {
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
