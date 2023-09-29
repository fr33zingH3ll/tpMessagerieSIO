package cc.freezinghell.messagerie.controllers;

import static com.rethinkdb.RethinkDB.r;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rethinkdb.net.Result;

import cc.freezinghell.messagerie.BackApplication;
import cc.freezinghell.messagerie.entities.Channel;
import cc.freezinghell.messagerie.entities.Message;
import cc.freezinghell.messagerie.entities.User;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/message")
@RolesAllowed("USER")
public class MessageController {

	@PostMapping("/{channelId}")
	public ResponseEntity<Message> create(@PathVariable String channelId, @RequestBody ObjectNode body,
			Authentication authentication) {
		Channel ch = r.table("channel").get(channelId).run(BackApplication.getConnect(), Channel.class).first();

		User user = (User) authentication.getPrincipal();

		if (!ch.getMembers().contains(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		Message message = new Message();
		message.setAuthor(user.getId());
		message.setChannel(ch.getId());
		message.setDate(new Date());
		message.setText(body.get("text").asText());

		r.table("message").insert(message).run(BackApplication.getConnect()).close();

		return ResponseEntity.ok(message);
	}

	@GetMapping("/{channelId}")
	public ResponseEntity<List<Message>> history(@PathVariable String channelId, Authentication authentication) {
		Channel ch = r.table("channel").get(channelId).run(BackApplication.getConnect(), Channel.class).first();

		User user = (User) authentication.getPrincipal();

		if (!ch.getMembers().contains(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		Result<List<Message>> res = r.table("message")
				.filter(row -> row.g("channel").eq(ch.getId()))
				.orderBy("date")
				.run(BackApplication.getConnect(), new TypeReference<List<Message>>() {});

		return ResponseEntity.ok(res.first());
	}

	@DeleteMapping("/{channelId}/{messageId}")
	public ResponseEntity<Object> create(@PathVariable String channelId, @PathVariable String messageId,
			Authentication authentication) {
		Message message = r.table("message").get(messageId).run(BackApplication.getConnect(), Message.class).first();
		User user = (User) authentication.getPrincipal();

		if (message == null || !channelId.equals(message.getChannel())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		if (user.getId().equals(message.getAuthor()) || user.getRoles().contains("ROLE_ADMIN")) {
			r.table("message").get(message.getId()).delete().run(BackApplication.getConnect()).close();
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
