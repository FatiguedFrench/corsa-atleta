import java.util.ArrayList;

/**
 * Classe del giudice della gara.
 * Registra gli atleti, per ogni atleta crea il thread associato
 * Monitora gli atleti nel loro progresso nella gara
 * Scrive il podio a fine gara su file e lo mostra su schermo
 */
public class Giudice extends Thread {
	/** Lista degli atleti partecipanti alla gara. */
	private ArrayList<Atleta> Atleti = new ArrayList<>();
	/** La classifica ordinata in base agli arrivi */
	private ArrayList<Atleta> Podio = new ArrayList<>();
	/** Istanza del gestore delle operazioni su file. */
	private GestioneFile filer = new GestioneFile();
	/** Riferimento alla classe per le anomalie agli atleti */
	private EventiCausali ec;
	/** Utilizzato per allineare la stampa della classifica */
	public int lunghezzaNomePiuLungo = 0;
	/** Oggetto per la sincronizzazione
	dell'aggiunta degli atleti al podio */
	private final Object lock = new Object();

	/** Lunghezza totale della gara in metri. */
	public final double LUNGHEZZAGARA = 500.0;

	/** Costruttore del {@link Giudice} */
	public Giudice(EventiCausali e) { ec = e; }

	/** Getter per la lunghezza della gara (in metri).
	 * @return {@link #LUNGHEZZAGARA} */
	public double getLunghezzaGara() { return LUNGHEZZAGARA; }

	/** Getter della lunghezza del nome dell'atleta più lungo.
	 * @return {@link #lunghezzaNomePiuLungo} */
	public int getLunghezzaNomePiuLungo() { return lunghezzaNomePiuLungo; }

	/**
	 * Verifica se un dato atleta è presente nel podio.
	 * @param a atleta da verificare
	 * @return {@code true} se l'atleta è presente in {@link #Podio}
	*/
	public boolean sonoNelPodio(Atleta a) {
		return (Podio.indexOf(a) > -1) ? true : false;
	}

	/** Ordina per progresso e inserisce gli atleti
	ritirati all'interno del podio per la stampa */
	private void ritiratiNelPodio() {
		ArrayList<Atleta> ritirati = new ArrayList<>();

		for (Atleta a : Atleti) {
			if (a.Attesa == -1) {
				ritirati.add(a);
			}
		}

		ArrayList<Atleta> ordinati = new ArrayList<>(ritirati);
		Atleta temp;

		for (int i = 0; i < ordinati.size() - 1; i++) {
			for (int j = 0; j < ordinati.size() - 1 - i; j++) {
				if (ordinati.get(j).progresso < ordinati.get(j + 1).progresso) {
						temp = ordinati.get(j);
						ordinati.set(j, ordinati.get(j + 1));
						ordinati.set(j + 1, temp);
				}
			}
		}

		Podio.addAll(ordinati);
	}

	/** Monitora il progresso di tutti gli atleti partecipanti.
	 * Per ogni atleta esegue {@code visualizzaProgresso()}
	 * Controlla se ha percorso i metri di {@link #LUNGHEZZAGARA}
	 * Se si, inserisce l'atleta nel podio in modo sincronizzato e
	 * controlla se tutti gli altri atleti hanno finito
	 * Se tutti gli atleti hanno finito, esegue {@link #fineGara()}
	 */
	public void monitora() {
		int i = 0;

		for (Atleta a : Atleti) {
			a.visualizzaProgresso();

			if (!Podio.contains(a) && (a.progresso >= LUNGHEZZAGARA)) {
				synchronized (lock) { Podio.add(a); }
			}

			if (a.Attesa == -1) { i++; }
			if (Podio.containsAll(Atleti)) { fineGara(); return; }
			else if (i == (Atleti.size() - Podio.size())) { ritiratiNelPodio(); fineGara(); return; }
		}

		System.out.printf("%s\n", "-".repeat(Atleta.numeroCaratteriRappresentativi + lunghezzaNomePiuLungo + Integer.toString(Atleti.size()).length()) + 13);

		try { Thread.sleep(1000); }
		catch (InterruptedException e) { System.err.println("Errore sleep"); }

		monitora();
	}

	/** Segna la fine della gara e stampa la classifica finale. */
	synchronized public void fineGara() {
		int i = 0;

		System.out.println("Classifica (dal Primo all'Ultimo): ");
		for (Atleta a : Podio) {
			String spaziatura = " ".repeat(lunghezzaNomePiuLungo - a.nome.length());
			if (a.Attesa >= 0) {
				System.out.printf("         %d. [%d] %s%s | Priorita' Thread: %d/%d\n", ++i, a.numero, a.nome, spaziatura, a.efficienzaAgonistica, Thread.MAX_PRIORITY);
			} else {
				System.out.printf("RITIRATO %d. [%d] %s%s | Priorita' Thread: %d/%d\n", ++i, a.numero, a.nome, spaziatura, a.efficienzaAgonistica, Thread.MAX_PRIORITY);
			}
		}

		filer.scriviPodio(Podio);
		Podio = Atleti;
		return;
	}

	/** Metodo con eccezione per la registrazione degli atleti alla gara
	 * Se il file contenente i nomi degli atleti e' vuoto, lancia un {@link Exception}
	 * Se il file contenente i nomi degli atleti e' unico, lancia un {@link Exception}
	 * Se il file contenente i nomi degli atleti contine piu' di 999 nomi, lancia un {@link Exception}
	 * Altrimenti @return {@link ArrayList} */
	private ArrayList<Atleta> registraAtleti() throws Exception {
		ArrayList<Atleta> possibiliAtleti = filer.registraAtleti("atleti.txt", this, ec);
		
		if (possibiliAtleti.size() == 0) { throw new Exception("Nessun Atleta nel File"); }
		else if (possibiliAtleti.size() == 1) { throw new Exception("Troppi pochi Atleti nel File"); }
		else if (possibiliAtleti.size() > 999) { throw new Exception("Troppi Atleti nel File"); }
		else { return possibiliAtleti; }
	}

	/** Metodo per avviare la gara.
	 * Conto alla rovescia, poi inizializza un thread per atleta
	 * Poi esegue il metodo recursivo {@link #monitora()} */
	public void avviaGara() {
		try { Atleti = registraAtleti(); }
		catch(Exception errore) { System.out.println("Errore! " + errore.getMessage()); return; }

		System.out.print("Inizio in ");
		for (int i = 5; i > 0; i--) {
			System.out.printf("%d ", i);
			try { Thread.sleep(1000); }
			catch (InterruptedException e) { System.err.println("Errore sleep"); }
		}
		System.out.println("VIA!!!");

		for (Atleta a : Atleti) {
			if (a.nome.length() > lunghezzaNomePiuLungo) { lunghezzaNomePiuLungo = a.nome.length(); }
			(new Thread(a)).start();
		}

		monitora();
	}
}
