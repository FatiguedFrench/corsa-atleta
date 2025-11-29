import java.util.Random;

public class EventiCausali {
	public int evento(Random random) {
		int evento = random.nextInt(1000);
		if (evento >= 0 && evento < 1) { return ritiro(); }			// 01‰ - Ritiro dalla Gara
		else if (evento >= 1 && evento < 11) { return pausa(5); }	// 10‰ - Infortunio di 5 secondi
		else if (evento >= 11 && evento < 26) { return pausa(3); }	// 15‰ - Infortunio di 3 secondi
		else if (evento >= 26 && evento < 46) { return pausa(1); }	// 20‰ - Infortunio di 1 secondo
		else { return 0; }
	}

	public int pausa(int Secondi) {
		return Secondi;
	}

	public int ritiro() {
		return -1;
	}
}
