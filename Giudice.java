import com.sun.tools.jconsole.JConsoleContext;

import java.util.ArrayList;

public class Giudice extends Thread {
	static int numero;
	static ArrayList<Atleta> Atleti = new ArrayList<>();
	static ArrayList<Atleta> Podio = new ArrayList<>();
//	static ArrayList<Thread> threadAtleti = new ArrayList<>();

	private Giudice() { }

	public static void aggiungimi(Atleta a) {
		Atleti.add(a);
	}

	public static void finito(Atleta a) {
		Podio.add(a);
		if (Podio.size() == Atleti.size()) Giudice.fineGara();
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
