import java.util.Random;

/**
 * Rappresenta un atleta che partecipa allauna gara.
 * Implementa {@link Runnable} per permettere l'esecuzione di ciascun {@link Atleta} in thread separati
 */
public class Atleta implements Runnable {
	/** Priorità del thread */
	int efficienzaAgonistica;
	/** Progresso dell'atleta in metri */
	double progresso;
	/** Tempo trascorso dall'inizio della gara */
	double tempo;
	/** */
	EventiCausali ec;
	/** Nome dell'atleta */
	String nome;
	/** ID del thread */
	int numero;
	/** Riferimento al Giudice che monitora la gara */
	Giudice g;
	/** Generatore di numeri casuali, utilizzato per determinare la priorita' e i progressi */
	private Random rand;
	/** Stile */
	static final int numeroCaratteriRappresentativi = 50;

	/**
	 * Costruisce un nuovo atleta e lo registra presso il Giudice.
	 * La priorita' del futuro Thread viene assegnata casualmente tra MIN_PRIORITY e MAX_PRIORITY.
	 *
	 * @param pNome Il nome dell'atleta.
	 * @param pG	Il Giudice che gestisce la gara.
	 */
	public Atleta(String pNome, Giudice pG, EventiCausali e) {
		rand = new Random();
		progresso = 0.0;
		nome = pNome;
		tempo = 0.0;
		ec = e;
		g = pG;

		efficienzaAgonistica = rand.nextInt(Thread.MAX_PRIORITY) + Thread.MIN_PRIORITY;
		if (efficienzaAgonistica > Thread.NORM_PRIORITY) { System.out.print("L'Atleta " + pNome + " si e' dopato!"); }
		else if ( efficienzaAgonistica < Thread.NORM_PRIORITY) { System.out.print("L'Atleta " + pNome + " e' fuori forma!"); }
		else { System.out.print("L'Atleta " + pNome + " e' pronto!"); }

		System.out.printf(" Efficienza Agonistica: %d/%d\n", efficienzaAgonistica, Thread.MAX_PRIORITY);
	}

	/**
	 * Simula il movimento dell'atleta durante la gara.
	 * Incrementa il progresso in maniera casuale e aggiorna il tempo trascorso
	 */
	void cammina() {
		Random generatore = new Random();
		progresso += generatore.nextDouble(10 + efficienzaAgonistica * 2);
		tempo++;
	}

	/**
	 * Visualizza lo stato corrente dell'atleta in gara.
	 * Mostra una barra di progresso proporzionale alla distanza percorsa
	 */
	void visualizzaProgresso() {
		double rapportoProgresso = Math.min((progresso / g.getLunghezzaGara() * numeroCaratteriRappresentativi), numeroCaratteriRappresentativi);
		String spaziatura = " ".repeat(g.getLunghezzaNomePiuLungo() - nome.length());
		String strProgresso = "=".repeat((int)(rapportoProgresso));
		String strRimasto = " ".repeat((int)(numeroCaratteriRappresentativi - rapportoProgresso));

		if (progresso >= g.getLunghezzaGara()) {
			System.out.printf("FINITO [%d] %s%s [%s%s]\n", numero, nome, spaziatura, strProgresso, strRimasto);
		} else { 
			System.out.printf("       [%d] %s%s [%s%s ]\n", numero, nome, spaziatura, strProgresso, strRimasto); 
		}
	}

	/**
	 * Funzione principale del thread dell'atleta.
	 * Imposta la priorità del thread, una volta eseguito, l'atleta cammina finché non raggiunge il podio della gara.
	 * La progressione avviene con pause di 1 secondo per simulare il tempo reale
	 */
	@Override
	public void run() {
		Thread.currentThread().setPriority(efficienzaAgonistica);
		numero = (int) Thread.currentThread().threadId();

		while (!g.sonoNelPodio(this)) {
			cammina();

			try { Thread.sleep(1000); }
			catch (InterruptedException e) { System.err.println("Errore sleep"); }
		}
	}
}
