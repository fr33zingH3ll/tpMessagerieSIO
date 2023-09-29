package cc.freezinghell.messagerie.controllers;

import static com.rethinkdb.RethinkDB.r;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import cc.freezinghell.messagerie.BackApplication;
import cc.freezinghell.messagerie.entities.User;
import cc.freezinghell.messagerie.utils.Views.Public;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/user")
@RolesAllowed("USER")
public class UserController {

	@GetMapping("/")
	@JsonView(Public.class)
	public ResponseEntity<List<User>> users() {
		return ResponseEntity.ok(r.table("user").run(BackApplication.getConnect(), User.class).toList());
	}
}
