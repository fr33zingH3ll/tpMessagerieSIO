package cc.freezinghell.messagerie.utils;

import static com.rethinkdb.RethinkDB.r;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.rethinkdb.net.Result;

import cc.freezinghell.messagerie.BackApplication;
import cc.freezinghell.messagerie.entities.User;

public class MyHandler extends TextWebSocketHandler {
	
	@Autowired
	private JwtUtil jwtUil;
	
	@Autowired
	private UserService userService;

	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
	}
	
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
		boolean tokenExpired = jwtUil.isTokenExpired(token);
		if (tokenExpired) {
			session.sendMessage(new TextMessage("token expiré"));
			session.close();
			return;
		}
		String username = jwtUil.extractUsername(token);
		User userDetails = (User) userService.loadUserByUsername(username);
		session.getAttributes().put("user", userDetails);
		
		Result<Object> changes = r.table("message").changes().run(BackApplication.getConnect());
		changes.stream().forEach((change) -> {
			try {
				session.sendMessage(new TextMessage(BackApplication.MAPPER.writeValueAsString(change)));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		System.out.println("fini");
	}
}
