package knowledgehunters.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Subject implements Comparable<Subject>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;

	public Subject() {
		
	}

	public Subject(int id, String name) {
		this.id = id;
		this.name = name;
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

	@Override
	public int compareTo(Subject u) {
		 if (getName() == null || u.getName() == null) {
		      return 0;
		    }
		    return getName().toLowerCase().compareTo(u.getName().toLowerCase());
		  
	}
	
	
}
