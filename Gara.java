/**
 * Avvia una gara atletica creando un insieme di atleti e un giudice
 * che ne gestisce lo svolgimento. La classe contiene esclusivamente
 * il metodo {@code main}, punto di ingresso dell’applicazione.
 *
 * @author PixPix
 * @version 3.1
 */
public class Gara {
	/**
	 * Punto di ingresso del programma. Inizializza il giudice, registra gli atleti
	 * e avvia la competizione.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		System.out.println("Gara Atletica");

		Giudice g = new Giudice();

		new Atleta("Auci", g);
		new Atleta("Betti", g);
		new Atleta("Capocci", g);
		new Atleta("Chionchio", g);
		new Atleta("Cutini", g);
		new Atleta("Gamboni", g);
		new Atleta("Hasani", g);
		new Atleta("Kini Chiask", g);
		new Atleta("Kini Ramsk", g);
		new Atleta("Lepicki", g);
		new Atleta("Martinoli", g);
		new Atleta("Pizzoli", g);
		new Atleta("Ragani", g);
		new Atleta("Rellini", g);
		new Atleta("Sposini", g);
		new Atleta("Orsini", g);
		new Atleta("Onofri", g);
		new Atleta("Tifi", g);
		new Atleta("Versiglioni", g);
		new Atleta("Volpi", g);

		g.avviaGara();
	}
}
