package cc.freezinghell.messagerie.entities;

import java.util.Date;
import java.util.UUID;

/**
 * objet message qui represente un message (pas encore coder)
 */
public class Message {
	private String id = UUID.randomUUID().toString();
	private String author;
	private String text;
	private String channel;
	private Date date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Message [author=" + author + ", text=" + text + ", channel=" + channel + ", date=" + date + "]";
	}
}
