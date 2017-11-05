package ar.uba.fi.tdd.rulogic;

import org.junit.Assert;
import org.junit.Test;

public class AppTest {
	
	App app;
	
	@Test
	public void testSintaxisCorrectaConsulta() {
		boolean evaluacion = app.consultacorrecta("varon(juan)");
		Assert.assertTrue(evaluacion);
	}
	
	@Test
	public void testSintaxisIncorrectaConsulta() {
		boolean evaluacion = app.consultacorrecta("varon juan");
		Assert.assertFalse(evaluacion);
	}
	
}
