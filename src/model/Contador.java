package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "vendimiados")
public class Contador {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "counter", unique = true, nullable = true)
	private int counter;
	
	@Column(name = "fecha")
	private String fecha;

	public int getCounter() {
		return counter;
	}
	
	public int setCounter() {
		return counter;
	}

	public String getFecha() {
		return fecha;
	}

	public String setFecha() {
		return fecha;
	}
}