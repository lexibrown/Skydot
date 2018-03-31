package data;

import java.io.Serializable;

public class Payee implements Serializable {

	private static final long serialVersionUID = 1277709450286853283L;

    private int id;
    private String name;

    public Payee() {
        this.id = 0;
        this.name = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
}
