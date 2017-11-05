package ar.uba.fi.tdd.rulogic.model;

import java.util.List;

public class Rule {

	private String nombre;
	private List<String> variables;
	private List<Fact> listaFacts;
	
	 public Rule(String nombre, List<Fact> listaFacts, List<String> variables) {
		
		 this.nombre = nombre;
		 this.listaFacts = listaFacts;
		 this.variables = variables;		 
	}
	 
	 public String getNombre() {
		return nombre;
	}
	 
	 public List<Fact> getListaFacts() {
		return listaFacts;
	}
	 
	public List<String> getVariables() {
		return variables;
	} 
	
}
