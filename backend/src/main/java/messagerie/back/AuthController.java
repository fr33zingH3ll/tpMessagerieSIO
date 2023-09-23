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
	
	public AuthController () {
	}
	
	@PostMapping("/login")
	public Result<User> login(@RequestParam String email, @RequestParam String password) {
		return null;
	}

}
