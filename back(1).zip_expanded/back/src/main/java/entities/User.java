package entities;

public class User {
	
	private String name, firstname, mail, phone, role;
	
	public User(String name, String firstname, String mail , String phone, String role) {
		this.name = name;
		this.firstname = firstname;
		this.mail = mail;
		this.phone = phone;
		this.role = role;
	}
	
	public String hello_world(String string) {
		return string;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
}
