import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/** Classe responsabile della gestione dei file. */
public class GestioneFile {
	/** Costruttore della classe GestioneFile. */
	public void GestoneFile() {}

	/** Legge i nomi degli atleti da un file di testo e crea gli oggetti Atleta registrandoli con il Giudice */
	public ArrayList<Atleta> registraAtleti(String fileAtleti, Giudice g, EventiCausali ec) {
		ArrayList<Atleta> returnObj = new ArrayList<>();
		
		try (BufferedReader file = new BufferedReader(new FileReader(fileAtleti))) {
			String nomeAtleta;
			while ((nomeAtleta = file.readLine()) != null) { returnObj.add(new Atleta(nomeAtleta, g, ec)); }
		} catch (IOException e) { System.out.println("Errore nella lettura del file: " + e.getMessage()); }

		return returnObj;
	}

	/** Scrive sul file "podio.txt" la classifica finale degli atleti.
	 * Ogni atleta viene elencato con il suo numero, nome e priorita' del Thread mentre era in esecuzione */
	public void scriviPodio(ArrayList<Atleta> podio) {
		int i = 0;
		try (PrintWriter writer = new PrintWriter(new FileWriter("podio.txt"))) {
			for (Atleta a : podio) {
				writer.printf("%d. [%d] %s | Priorita' Thread: %d/%d | Metri Percorsi: %.3f\n", ++i, a.numero, a.nome, a.efficienzaAgonistica, Thread.MAX_PRIORITY, a.progresso);
			}
		} catch (IOException e) { System.out.print("Errore Scrittura File"); }
	}
}
