package data;

public class Payee {

	private String name;
	private int id;
	
	public Payee() {
		name = null;
		id = 0;
	}
	
	public Payee(int id, String name) {
		setId(id);
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
