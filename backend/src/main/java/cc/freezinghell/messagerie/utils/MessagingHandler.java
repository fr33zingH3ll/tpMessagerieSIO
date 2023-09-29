package cc.freezinghell.messagerie.utils;

import static com.rethinkdb.RethinkDB.r;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.rethinkdb.net.Result;

import cc.freezinghell.messagerie.BackApplication;
import cc.freezinghell.messagerie.entities.ChangeSet;
import cc.freezinghell.messagerie.entities.Channel;
import cc.freezinghell.messagerie.entities.Message;
import cc.freezinghell.messagerie.entities.User;

/*
 * MyHandler est la class qui recupere tout les messages envoyé sur le websocket.
 */

public class MessagingHandler extends TextWebSocketHandler {
	private static final Logger LOG = LoggerFactory.getLogger(MessagingHandler.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	/**
	 * recupere la session créé
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	}

	/**
	 * recupere depuis la session le message et vérifie les changements en db pour
	 * les envoyé au websocket
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		if (session.getAttributes().containsKey("user")) {
			session.sendMessage(new TextMessage("déjà authentifié"));
		}

		JsonNode json = BackApplication.MAPPER.readTree(message.getPayload());

		if (!json.has("token")) {
			session.sendMessage(new TextMessage("token non présent"));
			session.close();
			return;
		}

		String token = json.get("token").asText();
		boolean tokenExpired = jwtUtil.isTokenExpired(token);

		if (tokenExpired) {
			session.sendMessage(new TextMessage("token expiré"));
			session.close();
			return;
		}

		String username = jwtUtil.extractUsername(token);
		User userDetails = (User) userService.loadUserByUsername(username);
		session.getAttributes().put("user", userDetails);

		Result<ChangeSet<Message>> changes = r.table("message")
				.changes()
				.run(BackApplication.getConnect(), new TypeReference<ChangeSet<Message>>() {});

		new Thread(() -> {
			changes.stream().forEach(change -> this.handleChange(session, change));
		}, "WebSocket-" + username).start();
	}

	private void handleChange(WebSocketSession session, ChangeSet<Message> change) {
		try {
			User user = (User) session.getAttributes().get("user");
			Channel channel = r.table("channel")
					.get(change.getNewValue().getChannel())
					.run(BackApplication.getConnect(), Channel.class)
					.first();

			if (channel.getMembers().contains(user.getId())) {
				session.sendMessage(new TextMessage(BackApplication.MAPPER.writeValueAsString(change)));
			}
		} catch (IOException e) {
			LOG.error("Error while sending WS message", e);
		}
	}
}
