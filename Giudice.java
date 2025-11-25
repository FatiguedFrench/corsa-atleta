import java.util.ArrayList;

/**
 * Gestisce una gara tra più atleti, monitorando i loro progressi,
 * determinando il podio e coordinando l’avvio della competizione.
 * Estende {@link Thread} per permettere eventuali estensioni concorrenti.
 */
public class Giudice extends Thread {
	/** Lista degli atleti iscritti alla gara. */
	ArrayList<Atleta> Atleti = new ArrayList<>();
	/** Avanzamento in metri per ciascun atleta, parallelo alla lista Atleti. */
	ArrayList<Double> Progresso = new ArrayList<Double>();
	/** Contatore dei turni completati. */
	int Turni = 0;
	/** Lista degli atleti nell’ordine di arrivo. */
	ArrayList<Atleta> Podio = new ArrayList<>();
	/** Lunghezza totale della gara in metri. */
	final double LUNGHEZZAGARA = 50.0;
	/** Costruisce un giudice senza parametri. */
	public Giudice() { }

	/**
	 * Registra il passo di un atleta, aggiornandone l’avanzamento.
	 * Determina se l’atleta ha terminato la gara.
	 *
	 * @param a atleta che ha effettuato il passo
	 * @param numMetri metri percorsi nel passo
	 * @return {@code true} se l’atleta deve continuare, {@code false} se ha concluso
	 */
	public boolean passi(Atleta a, double numMetri) {
		int index = Atleti.indexOf(a);
		Object lock = new Object();

		Progresso.set(index, Progresso.get(index) + numMetri);
		System.out.printf("Il numero %d ha percorso %.2f Metri\n", a.numero, Progresso.get(index));

		if (Progresso.get(index) >= LUNGHEZZAGARA) {
			synchronized (lock) { Podio.add(a); }
			if (Podio.size() == Atleti.size()) fineGara();
			return false;
		} else { return true; }
	}

	/**
	 * Registra un nuovo atleta, inizializzandone il progresso a zero.
	 *
	 * @param a atleta da aggiungere
	 */
	public synchronized void aggiungimi(Atleta a) {
		Atleti.add(a);
		Progresso.add(0.0);
	}

	/**
	 * Stampa il podio al termine della gara.
	 */
	public void fineGara() {
		System.out.println("--- --- --- --- --- ---");
		System.out.println("Gara Terminata! Ecco il Podio:");
		System.out.println("Primo in classifica: " + Podio.get(0).nome);
		System.out.println("Secondo in classifica: " + Podio.get(1).nome);
		System.out.println("Terzo in classifica: " + Podio.get(2).nome);
		System.out.println("--- --- --- --- --- ---");

		System.out.print("Classifica Finale (dal Primo all'Ultimo): ");
		for (Atleta a : Podio) {
			if (a.livelloDoping > 5) {
				System.out.printf("%s [%d] (%d), ", a.nome, a.numero, a.livelloDoping);
			} else { System.out.printf("%s [%d], ", a.nome, a.numero); }
		}
	}

	/**
	 * Esegue il conto alla rovescia e avvia ogni atleta in un proprio thread.
	 */
	public void avviaGara() {
		for (int i = 3; i > 0; i--) {
			System.out.println("Inizio in " + i);
			try { Thread.sleep(1000); }
			catch (InterruptedException e) { System.err.println("Errore sleep"); }
		}

		System.out.println("VIA!!!");

		for (Atleta a : Atleti) { (new Thread(a)).start(); }
	}
}
