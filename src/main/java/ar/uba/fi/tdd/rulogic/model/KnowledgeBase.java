package ar.uba.fi.tdd.rulogic.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KnowledgeBase {
	
	static final String rutaArchivoFija = "src/main/resources/rules.db";
	
	private List<Fact> facts;
	private List<Rule> rules;
	
	public KnowledgeBase() {
		this.facts = new ArrayList<Fact>();
		this.rules = new ArrayList<Rule>();
		try {
			parsearArchivo(rutaArchivoFija);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public KnowledgeBase(String rutaArchivo) {
		this.facts = new ArrayList<Fact>();
		this.rules = new ArrayList<Rule>();
		try {
			parsearArchivo(rutaArchivo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
        	procesarLinea(linea.replaceAll("\\s+",""));
        }
        archivoBDD.close();
	}

	/**
	 * Función encargada de rederigir el flujo del programa según la naturaleza de la
	 * línea leida, es decir si corresponde a una regla o a un fact.
	 * @param linea: representa una única línea del archivo procesado
	 */
	void procesarLinea(String linea) {
		//Verifica si la sintaxis de esa línea en particular es correcta
		if(sintaxisCorrecta(linea)) {
			//Compruebo si está haciendo referencia a una regla o a un hecho
			if(esRegla(linea)) {
				//Agrega la regla a la base de datos
				agregarRule(linea);
			} else {
				//Agrega el hecho a la base de datos
				agregarFact(linea);
			}
		} else {
			//Informo por consola aquellas líneas que tengan una sintaxis incorrecta
			System.out.println("Linea incorrecta: "+linea);
		}		
	}

	/**
	 * Se encarga de crear una regla obteniendo los datos desde la línea obtenida del archivo
	 * @param linea: linea del archivo leido asociado a una regla
	 * @return: retorna un objeto "Rule" (Regla) ya inicializado con sus parámetros
	 * correspondientes.
	 */
	private Rule crearRule(String linea) {
		//Parseo de la linea para separar aquellos campos que son relevantes a la hora de
		//crear la regla
		String[] parts = linea.split(":-");
		String definicion = parts[0];
		String conjuntoFacts = parts[1];
		conjuntoFacts = conjuntoFacts.replaceAll("\\.", "");
		conjuntoFacts = conjuntoFacts.replaceAll("\\),", ")|");
		List<String> listaStrFacts = Arrays.asList(conjuntoFacts.split("\\|"));
		List<Fact> listaFacts = new ArrayList<Fact>();
		
		//Genero un objeto "Fact" (Hecho) para cada uno de los hechos que están incluidos en
		//la regla
		for (int i = 0; i < listaStrFacts.size(); i++) {
			//System.out.println(listaStrFacts.get(i));
			Fact fact = crearFact(listaStrFacts.get(i));
			listaFacts.add(fact);
		}
		
		String nombreRegla = definicion.substring(0, definicion.indexOf('('));
		String variablesRegla = definicion.substring(definicion.indexOf('(')+1, definicion.indexOf(')'));	
		List<String> listaVariables = Arrays.asList(variablesRegla.split(","));
		
		Rule rule = new Rule(nombreRegla, listaFacts, listaVariables);
		
		return rule;		
	}
	
	/**
	 * Agrega el objeto "Rule" a la lista de reglas de la base de datos, denominada "rules"
	 * @param linea: linea del archivo leido asociado a una regla
	 */
	void agregarRule(String linea) {
		Rule rule = crearRule(linea);
		rules.add(rule);		
	}

	/**
	 * Se encarga de crear un hecho obteniendo los datos desde la línea obtenida del archivo
	 * @param linea: linea del archivo leido asociado a un hecho
	 * @return: retorna un objeto "Fact" (Hecho) ya inicializado con sus parámetros
	 * correspondientes.
	 */
	private Fact crearFact(String linea) {
		linea = linea.replaceAll("\\).", "");
		linea = linea.replaceAll("\\)", "");
		String[] parts = linea.split("\\(");
		String nombre = parts[0];
		String argumentos = parts[1];
		List<String> listaArgumentos = Arrays.asList(argumentos.split(","));
		return (new Fact(nombre, listaArgumentos));
	}
	
	/**
	 * Agrega el objeto "Fact" a la lista de hechos de la base de datos, denominada "facts"
	 * @param linea: linea del archivo leido asociado a una regla
	 */
	void agregarFact(String linea) {
		Fact fact = crearFact(linea);
		facts.add(fact);		
	}

	/**
	 * Verifica si la linea es una regla o un hecho
	 * @param linea: linea del archivo leido
	 * @return: devuelve true en el caso de que la línea esté asociada a una regla o false
	 * en caso contrario, es decir, si se relaciona con un hecho
	 */
	boolean esRegla(String linea) {
		return (linea.contains(":-"));
	}

	/**
	 * Verifica si la sintaxis de la línea es correcta
	 * @param linea: linea del archivo leido
	 * @return: devuelve true en caso de que la sintaxis sea correcta o false en caso contrario
	 */
	boolean sintaxisCorrecta(String linea) {
		if(linea.contains(":-")) {
			//Verifica la sintaxis para aquellos casos en que la línea esté asociada a una regla
			return linea.matches("(^[a-z]*\\([A-Z,]*\\))(:-)([a-z]*\\([A-Z,]*\\),)*([a-z]*\\([A-Z,]*\\).)$");
		} else {
			//Verifica la sintaxis para aquellos casos en que la línea esté asociada a un hecho
			return linea.matches("^[a-z]*\\([a-z,]*\\).$");
		}
	}

	/**
	 * Función que se encarga de redirigir el flujo de la información ingresada por el usuario
	 * para determinar si la consulta va a contener información de la base de datos o no
	 * @param query: consulta ingresada por el usuario por consola
	 * @return: devuelve true si la evaluación de la regla o el hecho es satisfactoria, es
	 * decir si se encuentra en la base de datos
	 */
	public boolean answer(String query) {
		boolean evaluacion;
		if(consultaEsFact(query)) {
			evaluacion = evaluarFact(query);
		} else {
			evaluacion = evaluarRegla(query);
		}
		return evaluacion;
	}

	/**
	 * Verifica si los hechos que componen la regla sobre el cual el usuario está 
	 * consultando se encuentra o no en la base de datos
	 * @param query: consulta ingresada por el usuario por consola
	 * @return: devuelve true si efectivamente se encuentran todos en la base de datos y 
	 * false en caso de que alguno de ellos no esté
	 */
	boolean evaluarRegla(String query) {
		boolean evaluacion = false;
		String nombreRegla = query.substring(0, query.indexOf('('));
		String variablesRegla = query.substring(query.indexOf('(')+1, query.indexOf(')'));
		List<String> listaVariables = Arrays.asList(variablesRegla.split(","));		
		
		//Busca la regla asociada a la consulta
		boolean encontrado = false;
		int i = 0;
		while(i < rules.size() && !encontrado) {
			String nombreRuleDB = (rules.get(i).getNombre());
			//Si la encuentra construye los facts a partir de la regla junto con los datos
			//de la consulta ingresada por el usuario
			if(nombreRegla.equals(nombreRuleDB)) {
				encontrado = true;
				evaluacion = armarFactsDeRegla(rules.get(i), listaVariables);
			}
			i++;
		}
		return evaluacion;
	}

	/**
	 * Construye los facts a evaluar a partir de la regla y la consulta del usuario
	 * @param rule: regla sobre la cual se van a generar los hechos para buscar en la base de datos
	 * @param listaVariables: variables ya separadas obtenidas a partir de lo ingresado por el usuario
	 * @return: devuelve true si todos los hechos están en la base de dato o false si uno de ellos no está
	 */
	private boolean armarFactsDeRegla(Rule rule, List<String> listaVariables) {
		boolean evaluacion = true;
		for (int i = 0; i < rule.getListaFacts().size(); i++) {
			Fact fact = rule.getListaFacts().get(i);
			List<String> listaArgumentos = rule.getVariables();
			//System.out.println(fact.getNombre() + " " + fact.getArgumentos());

			String strVariables = String.join(",", fact.getArgumentos());
			//System.out.println(strVariables);
			for (int j = 0; j < listaVariables.size(); j++) {
				//System.out.println(listaVariables.get(i));
				strVariables = strVariables.replaceAll(listaArgumentos.get(j), listaVariables.get(j));
			}
			
			System.out.println(fact.getNombre() + "(" + strVariables + ")");
			String factAEvaluar = (fact.getNombre() + "(" + strVariables + ")");
			if(evaluarFact(factAEvaluar) == false) {
				evaluacion = false;
			}
		}
		
		return evaluacion;
		
	}

	/**
	 * Verifica si el hecho sobre el cual el usuario está consultando se encuentra o no en
	 * la base de datos
	 * @param query: consulta ingresada por el usuario por consola
	 * @return: devuelve true si efectivamente se encuentra en la base de datos y false en
	 * caso contrario
	 */
	boolean evaluarFact(String query) {
		Fact fact = crearFact(query);
		String nombre = fact.getNombre();
		List<String> argumentos = fact.getArgumentos();
		
		boolean encontrado = false;
		int i = 0;
		while(i < facts.size() && !encontrado) {
			String nombreFact = (facts.get(i).getNombre());
			List<String> argumentosFact = (facts.get(i).getArgumentos());
			if((nombreFact.equals(nombre)) && (argumentosFact.equals(argumentos))) {
				encontrado = true;
			}
			i++;
		}
		return encontrado;
	}

	/**
	 * Verifica si la consulta corresponde a un hecho o una regla
	 * @param query: consulta ingresada por el usuario por consola
	 * @return: devuelte true en el caso de que esté relacionada con un hecho y false si está
	 * asociada a una regla
	 */
	boolean consultaEsFact(String query) {
		String nombre = query.substring(0, query.indexOf('('));
		boolean encontrado = false;
		int i = 0;
		while(i < facts.size() && !encontrado) {
			String nombreFact = (facts.get(i).getNombre());
			if(nombreFact.equals(nombre)) {
				encontrado = true;
			}
			i++;
		}
		return encontrado;
	}

}
