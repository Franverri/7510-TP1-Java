package ar.uba.fi.tdd.rulogic.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeBase {
	
	static final String rutaArchivoFija = "src/main/resources/rules.db";
	
	private List<Fact> facts;
	private List<Rule> rules;

	public KnowledgeBase() throws IOException {
		this.facts = new ArrayList<Fact>();
		this.rules = new ArrayList<Rule>();
		parsearArchivo(rutaArchivoFija);
	}
	
	public KnowledgeBase(String rutaArchivo) throws IOException {
		this.facts = new ArrayList<Fact>();
		this.rules = new ArrayList<Rule>();
		parsearArchivo(rutaArchivo);
	}
	
	/**
	 * A partir de la ruta de un archivo se lo procesa linea por linea para obtener los datos
	 * que van a ser almacenados en la base de datos
	 * @param rutaArchivo: ruta del archivo con la información correspondiente
	 */
	private void parsearArchivo(String rutaArchivo) throws FileNotFoundException, IOException {
		String linea;
        FileReader archivoBase = new FileReader(rutaArchivo);
        BufferedReader archivoBDD = new BufferedReader(archivoBase);
        while((linea = archivoBDD.readLine())!=null) {
        	System.out.println(linea.replaceAll("\\s+",""));
        	//procesarLinea(linea.replaceAll("\\s+",""));
        }
        archivoBDD.close();
	}
	
	public boolean answer(String query) {
		return true;
	}

}
