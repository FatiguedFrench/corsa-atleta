import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GestioneFile {
	public void GestoneFile() {}

	public void registraAtleti(String fileAtleti, Giudice g) {
		try (BufferedReader file = new BufferedReader(new FileReader(fileAtleti))) {
			String nomeAtleta;
			while ((nomeAtleta = file.readLine()) != null) {
				new Atleta(nomeAtleta, g);
			}
		} catch (IOException e) { System.out.println("Errore nella lettura del file: " + e.getMessage()); }
	}

}
