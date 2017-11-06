package ar.uba.fi.tdd.rulogic.model;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;

public class KnowledgeBaseTest {
	
	private KnowledgeBase db = new KnowledgeBase();
	private KnowledgeBase dbDesdeRutaFija = new KnowledgeBase("src/main/resources/rules.db");

	@Test
	public void testIdentificarReglaValida() {
		boolean evaluacion = db.esRegla("hijo(X, Y) :- varon(X), padre(Y, X).");
		Assert.assertTrue(evaluacion);	
	}
	
	@Test
	public void testIdentificarReglaIncorrecta() {
		boolean evaluacion = db.esRegla("padre(juan, pepe).");
		Assert.assertFalse(evaluacion);
	}
	
	@Test
	public void testSintaxisHechoCorrecta() {
		boolean evaluacion = db.sintaxisCorrecta("padre(juan,pepe).");
		Assert.assertTrue(evaluacion);
	}
	
	@Test
	public void testSintaxisHechoIncorrecta() {
		boolean evaluacion = db.sintaxisCorrecta("padre(juan,pepe)");
		Assert.assertFalse(evaluacion);
	}
	
	@Test
	public void testSintaxisReglaCorrecta() {
		boolean evaluacion = db.sintaxisCorrecta("hijo(X,Y):-varon(X),padre(Y,X).");
		Assert.assertTrue(evaluacion);
	}
	
	@Test
	public void testSintaxisReglaIncorrecta() {
		boolean evaluacion = db.sintaxisCorrecta("hijo(X,Y):--varon(X);padre(Y,X).");
		Assert.assertFalse(evaluacion);
	}
	
	@Test
	/**
	 * Ver si la consulta corresponde a un fact a partir de la base de 
	 * datos dada de ejemplo
	 */
	public void testEsFact() {
		boolean evaluacion = db.consultaEsFact("varon(juan)");
		Assert.assertTrue(evaluacion);
	}
	
	@Test
	public void testEsRegla() {
		boolean evaluacion = db.consultaEsFact("hijo(juan, pepe)");
		Assert.assertFalse(evaluacion);
	}
	
	@Test
	/**
	 * Ver si el fact esta en la base de datos de ejemplo
	 */
	public void testEvaluarFactPresenteEnBD() {
		boolean evaluacion = db.evaluarFact("varon(juan)");
		Assert.assertTrue(evaluacion);
	}
	
	@Test
	public void testEvaluarFactAusenteEnBD() {
		boolean evaluacion = db.evaluarFact("varon(franco)");
		Assert.assertFalse(evaluacion);
	}
	
	@Test
	/**
	 * Ver si los facts construidos a partir de la regla estan todos en la
	 * base de datos
	 */
	public void testEvaluarReglaPresenteEnBD() {
		boolean evaluacion = db.evaluarRegla("hijo(pepe,juan)");
		Assert.assertTrue(evaluacion);
	}

	@Test
	public void testEvaluarReglaAusenteEnBD() {
		boolean evaluacion = db.evaluarRegla("hijo(cristiano,messi)");
		Assert.assertFalse(evaluacion);
	}
	
	@Test
	/**
	 * Verificar funcionamiento del constructor de la base de datos pasándole
	 * la ruta del archivo como parámetro. Utilizo la variable dbDesdeRutaFija que
	 * abre el archivo de ejemplo y si el fact "varon(juan)" da true es porque abrio
	 * el archivo correctamente
	 */
	public void testUbicacionArchivoComoParametro() {
		boolean evaluacion = dbDesdeRutaFija.evaluarFact("varon(juan)");
		Assert.assertTrue(evaluacion);
	}
	
	@Test
	/**
	 * Ver si los facts se agregan a la lista de facts de la base de datos
	 */
	public void testAgregarFact() {
		db.agregarFact("crack(messi)");
		boolean evaluacion = db.evaluarFact("crack(messi)");
		Assert.assertTrue(evaluacion);
	}
	
	@Test
	/**
	 * Ver si las reglas se agregan a la lista de reglas de la base de datos
	 */
	public void testAgregarRegla() {
		db.agregarRule("sobrina(X,Y,Z):-mujer(X),hermano(Y,Z),padre(Y,X).");
		//Agrego los facts para que de bien la regla
		db.agregarFact("mujer(lara)");
		db.agregarFact("hermano(demian,franco)");
		db.agregarFact("padre(demian,lara)");
		boolean evaluacion = db.evaluarRegla("sobrina(lara,demian,franco)");
		Assert.assertTrue(evaluacion);
	}
	
	@Test
	/**
	 * Ver si cada linea procesada del archivo agrega un fact o rule a la 
	 * base de datos correctamente
	 */
	public void testProcesarLineaHecho() {
		db.procesarLinea("crack(messi).");
		//Si conuslto sobre ese fact debería devolver true si lo agregó bien
		boolean evaluacion = db.evaluarFact("crack(messi)");
		Assert.assertTrue(evaluacion);
	}	
	
	@Test
	public void testProcesarLineaRegla() {
		db.procesarLinea("sobrina(X,Y,Z):-mujer(X),hermano(Y,Z),padre(Y,X).");
		//Agrego los facts para poder testear si la regla se agregó correctamente
		db.procesarLinea("mujer(lara).");
		db.procesarLinea("hermano(demian,franco).");
		db.procesarLinea("padre(demian,lara).");
		//Si conuslto sobre ese fact debería devolver true si lo agregó bien
		boolean evaluacion = db.evaluarRegla("sobrina(lara,demian,franco)");
		Assert.assertTrue(evaluacion);
	}
	
	@Test
	/**
	 * Ver que si la linea procesada tiene una sintaxis invalida no se agrega
	 * a la base de datos
	 */
	public void testProcesarLineaInvalida() {
		db.procesarLinea("crack messi.");
		//Si conuslto sobre ese fact debería devolver false
		boolean evaluacion = db.evaluarFact("crack(messi)");
		Assert.assertFalse(evaluacion);
	}	
	
	@Test
	/**
	 * Ver que si la función que redirige el flujo de las consultas esta funcionando.
	 * El resto de funciones que esta utilizada ya fueron probadas previamente.
	 */
	public void testContestarConsultaHecho() {
		boolean evaluacion = db.answer("varon(juan)");
		Assert.assertTrue(evaluacion);
	}
	
	@Test
	/**
	 * Ver que si la función que redirige el flujo de las consultas esta funcionando.
	 * El resto de funciones que esta utilizada ya fueron probadas previamente.
	 */
	public void testContestarConsultaRegla() {
		boolean evaluacion = db.answer("hijo(pepe,juan)");
		Assert.assertTrue(evaluacion);
	}	
}
