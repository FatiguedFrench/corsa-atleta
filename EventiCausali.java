import java.util.Random;

/** Classe per le anomalie agli atleti durante la gara */
public class EventiCausali {
	/** Metodo principale.
	Utilizza un numero casuale per decidere l'anomalia */
	public int evento(Random random) {
		int evento = random.nextInt(1000);
		if (evento >= 0 && evento < 1) { return -1; }		// 01‰ - Ritiro dalla Gara
		else if (evento >= 1 && evento < 11) { return 5; }	// 10‰ - Infortunio di 5 secondi
		else if (evento >= 11 && evento < 26) { return 3; }	// 15‰ - Infortunio di 3 secondi
		else if (evento >= 26 && evento < 46) { return 1; }	// 20‰ - Infortunio di 1 secondo
		else { return 0; }
	}
}
