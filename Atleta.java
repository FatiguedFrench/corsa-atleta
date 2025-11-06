import java.util.Random;

public class Atleta implements Runnable {
	int numero;
	String nome;
//	double tempo = 0;
	double metri = 0;
	final double LUNGHEZZAGARA = 50.0;

	public Atleta(int pNumero, String pNome) {
		numero = pNumero;
		nome = pNome;
		Giudice.aggiungimi(this);
	}

	@Override
	public void run() {
		Random metriPercorsi = new Random();
		while(metri <= LUNGHEZZAGARA) {
			metri += metriPercorsi.nextDouble(10);
			System.out.println(nome + " Metri Percorsi: " + metri);
			try { Thread.currentThread().sleep(1000); }
			catch (InterruptedException e) { System.err.println("Errore sleep"); }
		}

		Giudice.finito(this);
	}
}
