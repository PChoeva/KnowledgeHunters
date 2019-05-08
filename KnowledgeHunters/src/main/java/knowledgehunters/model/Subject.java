package knowledgehunters.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="subject")
public class Subject implements Comparable<Subject>{
	
	@Id
	private String id;
	private String name;

	public Subject() {
		
	}

	public Subject(String id, String name) {
		//super();
		this.id = id;
		this.name = name;
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

	@Override
	public int compareTo(Subject u) {
		 if (getName() == null || u.getName() == null) {
		      return 0;
		    }
		    return getName().toLowerCase().compareTo(u.getName().toLowerCase());
		  
	}
	
	
}
