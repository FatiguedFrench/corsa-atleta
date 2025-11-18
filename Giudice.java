import java.util.ArrayList;
import java.util.Objects;

public class Giudice extends Thread {
//	static int numero;
	static ArrayList<Atleta> Atleti = new ArrayList<>();
	static ArrayList<Double> Progresso = new ArrayList<Double>();
	static int Turni = 0;
	static ArrayList<Atleta> Podio = new ArrayList<>();
	static final double LUNGHEZZAGARA = 50.0;
//	static ArrayList<Thread> threadAtleti = new ArrayList<>();

	private Giudice() { }

	public static boolean passi(Atleta a, double numMetri) {
		int index = Atleti.indexOf(a);
		Object lock = new Object();

		Progresso.set(index, Progresso.get(index)  + numMetri);
		System.out.printf("[%s] Metri Percorsi: %f\n", a.nome, Progresso.get(index));
		Giudice.passoFatto();

		if (Progresso.get(index) >= LUNGHEZZAGARA) {
			synchronized (lock) { Podio.add(a); }
			if (Podio.size() == Atleti.size()) Giudice.fineGara();
			return false;
		} else { return true; }
	}

	public static synchronized void passoFatto() {
		if (++Turni == (Atleti.size() - Podio.size())) {
			System.out.println("--- --- --- --- ---");
			Turni = 0;
		}
	}

	public static synchronized void aggiungimi(Atleta a) {
		Atleti.add(a);
		Progresso.add(0.0);
	}

	public static void fineGara() {
		System.out.println("Gara Terminata! Ecco il Podio:");
		System.out.println("Primo in classifica: " + Podio.get(0).nome);
		System.out.println("Secondo in classifica: " + Podio.get(1).nome);
		System.out.println("Terzo in classifica: " + Podio.get(2).nome);
	}

	public static void avviaGara() {
		for (int i = 3; i > 0; i--) {
			System.out.println("Inizio in " + i);
			try { Thread.currentThread().sleep(1000); }
			catch (InterruptedException e) { System.err.println("Errore sleep"); }
		}

		System.out.println("VIA!!!");

		for (Atleta a : Atleti) {
			(new Thread(a)).start();
//			threadAtleti.add(new Thread(a));
//			threadAtleti.getLast().start();
		}
	}
}
