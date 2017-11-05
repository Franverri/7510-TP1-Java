package ar.uba.fi.tdd.rulogic;

import java.io.IOException;
import java.util.Scanner;

import ar.uba.fi.tdd.rulogic.model.*;

/**
 * Console application.
 *
 */
public class App
{
	private static boolean consultacorrecta(String consulta) {
		return consulta.matches("^[a-z]*\\([a-z,]*\\)");
	}
	
	public static void main(String[] args) {
		System.out.println("-------INICIO DEL INTERPRETE-------");
		System.out.println("I shall answer all your questions!");
		String consulta = "";
		Scanner scanner = new Scanner(System.in);
		do {
			System.out.println("Ingrese su consulta o 'q' para salir: ");
			consulta = scanner.nextLine().replaceAll("\\s+","");
			if(consultacorrecta(consulta)) {
				System.out.println("SI");
			} else if (!consulta.equals("q")){
				System.out.println("Sintaxis de la consulta inválida");
			}
		} while (!consulta.equals("q"));
		System.out.println("--------FIN DEL INTERPRETE--------");
		scanner.close();
    }
}
