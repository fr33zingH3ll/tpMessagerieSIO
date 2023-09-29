package cc.freezinghell.messagerie.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeSet<T> {
	@JsonProperty("old_val")
	private T oldValue;

	@JsonProperty("new_val")
	private T newValue;

	public T getOldValue() {
		return oldValue;
	}

	public void setOldValue(T oldValue) {
		this.oldValue = oldValue;
	}

	public T getNewValue() {
		return newValue;
	}

	public void setNewValue(T newValue) {
		this.newValue = newValue;
	}

	@Override
	public String toString() {
		return "ChangeSet [oldValue=" + oldValue + ", newValue=" + newValue + "]";
	}
}
