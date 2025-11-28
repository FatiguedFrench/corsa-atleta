/**
 * Avvia una gara atletica creando un insieme di atleti e un giudice
 * che ne gestisce lo svolgimento. La classe contiene esclusivamente
 * il metodo {@code main}, punto di ingresso dell’applicazione.
 *
 * @author PixPix
 * @version 4.2
 */
public class Gara {
	/**
	 * Punto di ingresso del programma. Inizializza il giudice, registra gli atleti
	 * e avvia la competizione.
	 */
	public static void main(String[] args) {
		System.out.println("Gara Atletica");
		EventiCausali e = new EventiCausali();
		Giudice g = new Giudice(e);

		g.avviaGara();
	}
}
