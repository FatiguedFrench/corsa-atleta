import java.util.Random;

public class Atleta implements Runnable {
	int numero;
	String nome;
	double tempo = 0;

	public Atleta(int pNumero, String pNome) {
		numero = pNumero;
		nome = pNome;
		Giudice.aggiungimi(this);
	}

	double cammina() {
		Random generatore = new Random();
		double metri = generatore.nextDouble(10);
		tempo++;
		return metri;
	}

	@Override
	public void run() {
		while(Giudice.passi(this, cammina())) {
			try { Thread.currentThread().sleep(1000); }
			catch (InterruptedException e) { System.err.println("Errore sleep"); }
		}
	}
}
