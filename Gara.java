public class Gara {
	public static void main(String[] args) {
		System.out.println("Gara Atletica");

		new Atleta(67, "Aureliano");
		new Atleta(68, "Filippo");
		new Atleta(69, "Cutini");

		Giudice.avviaGara();
		// System.out.println("Caratteristiche Main: "+ Thread.currentThread().getName()
		// +" - " + Thread.currentThread().getPriority());
	}
}