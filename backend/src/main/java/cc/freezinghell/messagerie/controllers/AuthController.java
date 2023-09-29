package cc.freezinghell.messagerie.controllers;

import static com.rethinkdb.RethinkDB.r;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

import cc.freezinghell.messagerie.BackApplication;
import cc.freezinghell.messagerie.entities.User;
import cc.freezinghell.messagerie.utils.JwtUtil;
import cc.freezinghell.messagerie.utils.UserService;
import jakarta.annotation.security.RolesAllowed;

/**
 * controlleur qui gère l'authentification des utiisateurs
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * connection des utilisateur.
	 * 
	 * @return ResponseEntity<ObjectNode> un json en somme
	 * 
	 * @param @RequestBody ObjectNode
	 */
	@PostMapping("/login")
	public ResponseEntity<ObjectNode> login(@RequestBody ObjectNode body) {
		String email = body.get("email").asText();
		String password = body.get("password").asText();

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		UserDetails userDetails = userService.loadUserByUsername(email);
		String token = jwtUtil.generateToken(userDetails.getUsername());

		return ResponseEntity.ok(BackApplication.MAPPER.createObjectNode().put("token", token));
	}

	/**
	 * inscription des utilisateurs qui a la fin devras n'etre accesible pas les
	 * user ayant le role ADMIN
	 * 
	 * @return ResponseEntity<ObjectNode>
	 * 
	 * @param @RequestBody ObjectNode
	 */
	@PostMapping("/register")
	@RolesAllowed({ "ADMIN" })
	public ResponseEntity<ObjectNode> register(@RequestBody ObjectNode body) {
		// get email password and token in the body
		String email = body.get("email").asText();
		String password = body.get("password").asText();

		// test if user already exist
		User user = (User) userService.loadUserByUsername(email);

		if (user != null) {
			return ResponseEntity
					.ok(BackApplication.MAPPER.createObjectNode().put("error", "ERROR: l'utilisateur existe déjà"));
		}

		// if the user not exists so register
		user = new User();
		user.setEmail(email);
		user.setRole("ROLE_USER");
		user.getAuthorities();
		user.setPassword(this.passwordEncoder.encode(password));

		r.table("user").insert(user).run(BackApplication.getConnect());
		return ResponseEntity.ok(BackApplication.MAPPER.createObjectNode()
				.put("success", "SUCCESS: l'utilisateur a bien était enregistrer"));
	}
}
