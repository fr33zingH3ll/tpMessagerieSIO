package cc.freezinghell.messagerie.controllers;

import static com.rethinkdb.RethinkDB.r;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rethinkdb.net.Result;

import cc.freezinghell.messagerie.BackApplication;
import cc.freezinghell.messagerie.entities.Channel;
import cc.freezinghell.messagerie.entities.User;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/channel")
@RolesAllowed("USER")
public class ChannelController {

	/**
	 * Creates a group message channel
	 *
	 * @param body
	 * @param authentication
	 * @return
	 */
	@PostMapping("/")
	public ResponseEntity<Channel> createGroup(@RequestBody ObjectNode body, Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		Channel channel = new Channel();
		channel.setDirect(false);
		channel.setName(body.get("name").asText());
		channel.getMembers().add(user.getId());

		r.table("channel").insert(channel).run(BackApplication.getConnect()).close();

		return ResponseEntity.ok(channel);
	}

	/**
	 * Creates a DM channel
	 *
	 * @param body
	 * @param authentication
	 * @return
	 */
	@PostMapping("/{otherUserId}")
	public ResponseEntity<Channel> createDirect(@PathVariable String otherUserId, Authentication authentication) {
		User otherUser = r.table("user").get(otherUserId).run(BackApplication.getConnect(), User.class).first();
		User user = (User) authentication.getPrincipal();

		Result<Channel> existing = r.table("channel")
				.filter(row -> row.g("direct").eq(true).and(row.g("members").contains(otherUser.getId(), user.getId())))
				.run(BackApplication.getConnect(), Channel.class);

		if (existing.hasNext()) {
			return ResponseEntity.ok().body(existing.first());
		}

		Channel channel = new Channel();
		channel.setDirect(true);
		channel.getMembers().add(user.getId());
		channel.getMembers().add(otherUser.getId());

		r.table("channel").insert(channel).run(BackApplication.getConnect()).close();

		return ResponseEntity.status(HttpStatus.CREATED).body(channel);
	}
}
