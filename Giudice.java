import java.util.ArrayList;

/**
 * Classe che rappresenta il giudice della gara.
 * Mantiene la lista degli atleti partecipanti, controlla il progresso
 * di ciascuno e costruisce il podio in modo sincronizzato quando un
 * atleta raggiunge minimo la lunghezza della gara {@link LUNGHEZZAGARA}.
 */
public class Giudice extends Thread {
	/**
	 * Lista degli atleti partecipanti alla gara.
	 * Ogni elemento è un {@link Atleta} che verrà avviato come thread durante
	 * {@link #avviaGara()} e controllato periodicamente tramite {@link #monitora()}
	 */
	ArrayList<Atleta> Atleti = new ArrayList<>();

	/**
	 * Lista che rappresenta l'ordine di arrivo (la classifica).
	 * Gli atleti vengono aggiunti durante {@link #monitora()} quando gli atleti
	 * raggiungono la lunghezza di gara. Alla fine della gara, viene invocato
	 * {@link #fineGara()} e la lista viene usata per mostrare la classifica
	 */
	ArrayList<Atleta> Podio = new ArrayList<>();

	/**
	 * Gestore per le operazioni di I/O su file.
	 * Istanza di supporto per salvare su file le informazioni della gara
	 */
	GestioneFile filer =	new GestioneFile();

	/**
	 * Riferimento alla classe per
	 * le anomalie agli atleti
	 */
	EventiCausali ec;

	/**
	 * Lunghezza (in caratteri) del nome più lungo tra gli atleti registrati.
	 * Utilizzato per allineare la stampa della classifica
	 */
	int lunghezzaNomePiuLungo = 0;

	/**
	 * Oggetto usato per sincronizzare le modifiche concorrenti al podio.
	 * Protegge le operazioni di aggiornamento del {@link #Podio} quando più
	 * thread segnalano il completamento simultaneamente
	 */
	private final Object lock = new Object();

	/**
	 * Lunghezza totale della gara in metri.
	 * Valore costante che indica la distanza che un atleta deve
	 * percorrere per essere considerato "arrivato" e andare nel {@link #Podio}
	 */
	final double LUNGHEZZAGARA = 500.0;

	/**
	 * Costruttore di {@link Giudice}
	 */
	public Giudice(EventiCausali e) { ec = e; }

	/**
	 * Getter per la lunghezza della gara (in metri).
	 *
	 * @return la lunghezza di gara definita in {@link #LUNGHEZZAGARA}
	 */
	public double getLunghezzaGara() { return LUNGHEZZAGARA; }

	/**
	 * Getter per la lunghezza del nome più lungo registrata finora.
	 *
	 * @return numero di caratteri del nome più lungo tra gli atleti
	 */
	public int getLunghezzaNomePiuLungo() { return lunghezzaNomePiuLungo; }

	/**
	 * Verifica se un dato atleta è presente nel podio.
	 *
	 * @param a atleta da verificare
	 * @return {@code true} se l'atleta è presente in {@link #Podio}, {@code false} altrimenti
	 */
	public boolean sonoNelPodio(Atleta a) {
		return (Podio.indexOf(a) > -1) ? true : false;
	}

	/**
	 * 
	 */
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

	/**
	 * Monitora il progresso di tutti gli atleti registrati.
	 * Chiama per ciascun atleta visualizzaProgresso() e controlla se
	 * ha raggiunto o superato LUNGHEZZAGARA, in caso, lo aggiunge
	 * al {@link #Podio} in modo sincronizzato.
	 * Quando tutti gli atleti sono presenti nel podio invoca {@link #fineGara()}
	 * e termina la sua esecuzione. Questo metodo è progettato come metodo ricorsivo
	 * che effettua una pausa di 1s tra esecuzioni
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

		System.out.printf("%s\n", "-".repeat(Atleta.numeroCaratteriRappresentativi + lunghezzaNomePiuLungo + 5 + 7 + 1 + Integer.toString(Atleti.size()).length()));

		try { Thread.sleep(1000); }
		catch (InterruptedException e) { System.err.println("Errore sleep"); }
		monitora();
	}

	/**
	 * Segna la fine della gara e stampa la classifica finale.
	 * Metodo sincronizzato: imposta {@link #Podio} uguale a {@link #Atleti},
	 * stampa la classifica (dal primo all'ultimo) e delega a
	 * {@link GestioneFile#scriviPodio(ArrayList)} la scrittura su un file
	 */
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

	/**
	 * 
	 */
	private ArrayList<Atleta> registraAtleti() throws Exception {
		ArrayList<Atleta> possibiliAtleti = filer.registraAtleti("atleti.txt", this, ec);
		
		if (possibiliAtleti.size() == 0) { throw new Exception("Nessun Atleta nel File"); }
		else if (possibiliAtleti.size() == 1) { throw new Exception("Troppi pochi Atleti nel File"); }
		else if (possibiliAtleti.size() > 999) { throw new Exception("Troppi Atleti nel File"); }
		else { return possibiliAtleti; }
	}

	/**
	 * Metodo per avviare la gara. Registra gli atleti su file tramite
	 * {@link GestioneFile#registraAtleti(String, Giudice)}, poi esegue un conto
	 * alla rovescia e avvia ogni {@link Atleta} come thread separato. Infine
	 * invoca {@link #monitora()} per iniziare il monitoraggio dei progressi.
	 */
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
