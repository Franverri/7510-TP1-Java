package ar.uba.fi.tdd.rulogic.model;

import java.util.List;

public class Fact {

	private String nombre;
	private List<String> argumentos;
	
	public Fact(String nombre, List<String> argumentos) {
		
		this.nombre = nombre;
		this.argumentos = argumentos;
		
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public List<String> getArgumentos() {
		return argumentos;
	}
}
