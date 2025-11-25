import java.util.Random;

/**
 * Rappresenta un atleta che partecipa a una gara e avanza a intervalli regolari.
 * Implementa {@link Runnable} per consentire l’esecuzione concorrente.
 */
public class Atleta implements Runnable {
	/** Indica la priorita' del Thread dell'Atleta */
	int livelloDoping;
	/** Numero identificativo dell’atleta. */
	int numero;
	/** Nome dell’atleta. */
	String nome;
	/** Tempo impiegato dall’atleta, incrementato a ogni passo. */
	double tempo = 0;
	/** Riferimento al giudice che supervisiona la gara. */
	Giudice g;
	/** Generatore Casuale di Numeri */
	private Random rand;

	/**
	 * Costruisce un nuovo atleta e lo registra presso il giudice.
	 *
	 * @param pNome nome dell’atleta
	 * @param pG istanza del giudice che gestisce la gara
	 */
	public Atleta(String pNome, Giudice pG) {
		rand = new Random();
		nome = pNome;
		g = pG;
		g.aggiungimi(this);

		livelloDoping = rand.nextInt(20);
		if (livelloDoping > 15) {
			livelloDoping = (livelloDoping - 10);
			System.out.println("L'Atleta " + pNome + " si e' dopato! Livello di Doping: " + livelloDoping);
		} else { livelloDoping = Thread.NORM_PRIORITY; }
	}

	/**
	 * Genera l’avanzamento dell’atleta in metri e incrementa il tempo impiegato.
	 *
	 * @return il numero di metri percorsi nel passo corrente
	 */
	double cammina() {
		tempo++;
		Random generatore = new Random();
		return generatore.nextDouble(10 + livelloDoping * 2);
	}

	/**
	 * Ciclo principale di esecuzione dell’atleta. Continua ad avanzare finché il
	 * giudice lo consente, effettuando una pausa di un secondo tra un passo e l’altro.
	 */
	@Override
	public void run() {
		numero = (int) Thread.currentThread().getId();
		Thread.currentThread().setPriority(livelloDoping);

		while (g.passi(this, cammina())) {
			try { Thread.currentThread().sleep(1000); }
			catch (InterruptedException e) { System.err.println("Errore sleep"); }
		}
	}
}
