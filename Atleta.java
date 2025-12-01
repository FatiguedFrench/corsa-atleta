import java.util.Random;

/** Rappresenta un atleta che partecipa alla gara.
 * Implementa {@link Runnable} */
public class Atleta implements Runnable {
	/** Priorità del thread */
	int efficienzaAgonistica;
	/** Progresso dell'atleta in metri */
	double progresso;
	/** Tempo trascorso dall'inizio della gara */
	double tempo;
	/** Riferimento per eventi casuali */
	EventiCausali ec;
	/** Pausa in secondi dell'atleta */
	int Attesa;
	/** Nome dell'atleta */
	String nome;
	/** ID del thread */
	int numero;
	/** Riferimento al Giudice che monitora la gara */
	Giudice g;
	/** Generatore di numeri casuali */
	private Random rand;
	/** Stile */
	static final int numeroCaratteriRappresentativi = 100;

	/** Costruttore */
	public Atleta(String pNome, Giudice pG, EventiCausali e) {
		Attesa = 0;
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

	/** Simula il movimento dell'atleta durante la gara.
	Incrementa il progresso in maniera casuale e aggiorna il tempo trascorso */
	void cammina() {
		Random generatore = new Random();
		progresso += generatore.nextDouble(10 + efficienzaAgonistica * 2);
		tempo++;
	}

	/** Visualizza il progresso dell'atleta nella gara. */
	void visualizzaProgresso() {
		double rapportoProgresso = Math.min((progresso / g.getLunghezzaGara() * numeroCaratteriRappresentativi), numeroCaratteriRappresentativi);
		String spaziatura = " ".repeat(g.getLunghezzaNomePiuLungo() - nome.length());
		String strProgresso = "=".repeat((int)(rapportoProgresso));
		String strRimasto = " ".repeat((int)(numeroCaratteriRappresentativi - rapportoProgresso));

		if (progresso >= g.getLunghezzaGara())	{ System.out.printf("FINITO [%d] %s%s [%s%s]\n", numero, nome, spaziatura, strProgresso, strRimasto); }
		else if (Attesa > 0)			{ System.out.printf("ATT. %d [%d] %s%s [%s%s ]\n", Attesa, numero, nome, spaziatura, strProgresso, strRimasto); }
		else if (Attesa == -1)			{ System.out.printf("RITIRO [%d] %s%s [%s%s ]\n", numero, nome, spaziatura, strProgresso, strRimasto); }
		else					{ System.out.printf("       [%d] %s%s [%s%s ]\n", numero, nome, spaziatura, strProgresso, strRimasto); }
	}

	/** Funzione principale del thread dell'atleta.
	La progressione avviene con pause di 1 secondo per simulare il tempo reale */
	@Override
	public void run() {
		Thread.currentThread().setPriority(efficienzaAgonistica);
		numero = (int) Thread.currentThread().threadId();

		while (!g.sonoNelPodio(this) && (Attesa >= 0)) {
			if (Attesa == 0 ) {
				cammina();
				Attesa = ec.evento(rand);
				double ritiro = progresso / g.LUNGHEZZAGARA;
				if (Attesa == -1 && rand.nextDouble() < ritiro) { Attesa = 0; }
			} else { Attesa--; }

			try { Thread.sleep(1000); }
			catch (InterruptedException e) { System.err.println("Errore sleep"); }
		}
	}
}
