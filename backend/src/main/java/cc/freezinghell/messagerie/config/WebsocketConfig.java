package cc.freezinghell.messagerie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import cc.freezinghell.messagerie.utils.MessagingHandler;

/*
 * configure le websocket avec un handler et une route 
 */

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(myHandler(), "/conv");
	}
	
	@Bean
	public WebSocketHandler myHandler() {
		return new MessagingHandler();
	}
}
