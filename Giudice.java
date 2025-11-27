import java.nio.Buffer;
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
	/** Motivi Stilistici */
	int lunghNomePiuLungo = 0;
	ArrayList<Atleta> Buffer = new ArrayList<>();
	/** Classe per la scrittura e lettura dei file */
	GestioneFile filer =  new GestioneFile();
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
	synchronized public boolean passi(Atleta a, double numMetri) {
		int index = Atleti.indexOf(a);
		Object lock = new Object();

		Progresso.set(index, Progresso.get(index) + numMetri);
		visualizzaProgresso(a.nome, a.numero, Progresso.get(index), false);

		if ((index + 1) == (Atleti.size() - Podio.size())) {
			for (Atleta p : Podio) {
				visualizzaProgresso(p.nome, p.numero, LUNGHEZZAGARA, true);
			}
			System.out.println("--- --- --- --- --- --- --- --- --- --- ---");
			svuotaBufferArrivati();
		}

		if (Progresso.get(index) >= LUNGHEZZAGARA) {
			synchronized (lock) { Buffer.add(a); }
			if (Podio.size() + Buffer.size() == Atleti.size()) fineGara();
			return false;
		} else { return true; }
	}

	private void svuotaBufferArrivati() {
		Podio.addAll(Buffer);
		Buffer.clear();
	}

	private void visualizzaProgresso(String Nome, int Numero, double passiEffettuati, boolean finito) {
		double rappProgresso = (passiEffettuati / LUNGHEZZAGARA) * 30;

		if (finito) { System.out.print("FINITO "); }
		else { System.out.print("       "); }

		System.out.printf("[%d] %s", Numero, Nome);

		for (int i = 0; i <= (lunghNomePiuLungo - Nome.length()); i++) { System.out.print(" "); }
		System.out.print("[");

		for (int i = 0; i < 30; i++) {
			if (i < rappProgresso) { System.out.print("="); }
			else { System.out.print(" "); }
		}
		System.out.println("]");
	}

	/**
	 * Registra un nuovo atleta, inizializzandone il progresso a zero.
	 *
	 * @param a atleta da aggiungere
	 */
	public synchronized void aggiungimi(Atleta a) {
		Atleti.add(a);
		Progresso.add(0.0);
		if (a.nome.length() > lunghNomePiuLungo) { lunghNomePiuLungo = a.nome.length(); }
	}

	/**
	 * Stampa il podio al termine della gara.
	 */
	public void fineGara() {
		int i = 1;

		System.out.println("--- --- --- --- --- ---");
		System.out.println("Gara Terminata! Ecco il Podio:");
		System.out.println("Primo in classifica: " + Podio.get(0).nome);
		System.out.println("Secondo in classifica: " + Podio.get(1).nome);
		System.out.println("Terzo in classifica: " + Podio.get(2).nome);
		System.out.println("--- --- --- --- --- ---");

		System.out.println("Classifica Finale (dal Primo all'Ultimo): ");
		for (Atleta a : Podio) {
			System.out.print(i++);
			System.out.print(". [");
			System.out.print(a.numero);
			System.out.print("] ");
			System.out.print(a.nome);
			System.out.print(" | Efficienza Agonistica: ");
			System.out.print(a.livelloDoping);
			System.out.println("/10");
		}
	}

	/**
	 * Esegue il conto alla rovescia e avvia ogni atleta in un proprio thread.
	 */
	public void avviaGara() {
		filer.registraAtleti("atleti.txt", this);

		for (int i = 3; i > 0; i--) {
			System.out.println("Inizio in " + i);
			try { Thread.sleep(1000); }
			catch (InterruptedException e) { System.err.println("Errore sleep"); }
		}

		System.out.println("VIA!!!");

		for (Atleta a : Atleti) { (new Thread(a)).start(); }
	}
}
