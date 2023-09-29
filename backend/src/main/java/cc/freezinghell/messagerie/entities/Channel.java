package cc.freezinghell.messagerie.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
	private final List<String> members = new ArrayList<>();
	private boolean direct;
	private String id = UUID.randomUUID().toString(), name;

	public boolean isDirect() {
		return direct;
	}

	public void setDirect(boolean direct) {
		this.direct = direct;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return list of member IDs
	 */
	public List<String> getMembers() {
		return members;
	}

	@Override
	public String toString() {
		return "Channel [members=" + members + ", direct=" + direct + ", id=" + id + ", name=" + name + "]";
	}
}
