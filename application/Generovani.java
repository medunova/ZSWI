import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.ToggleButton;
/**
 * Tøída pro vygenerovani testu
 * @author Aneta Medunová, Tomáš Zobaè, Michal Všelko
 *
 */
public class Generovani {
	/**
	 *
	 * @param kategorie - kategorie otazek
	 * @param otazky - otazky
	 * @param pocetB - boolean pro tlacitko Body
	 * @param pocetO - boolean pro tlacitko otazky
	 * @param skola - hlavicka, nazev univezity
	 * @param zkous - hlavicka, jmeno zkousejiciho
	 * @param x - pocet bodu nebo pocet otazek
	 * @param datum - hlavicka, zadane datum (zkousky)
	 */
	private BufferedWriter writer;
	private ArrayList<Otazka> otazky;
	private ArrayList<Kategorie> kategorie;
	private Random rnd;


	public Generovani(ArrayList<Kategorie> kategorie, ArrayList<Otazka> otazky,
			ToggleButton pocetB, ToggleButton pocetO, String skola, String zkous,
			String pocet, String idTest, String datum) throws IOException {
		this.otazky = otazky;
		this.kategorie = kategorie;
		rnd = new Random();

		int x = Integer.parseInt(pocet);
		writer = new BufferedWriter(new FileWriter("Test.txt", false));
		writer.write("Škola: " + skola + ", ");
		writer.write("Zkoušející: " + zkous + ", ");
		writer.newLine();
		writer.write(idTest + ", ");
		writer.write("Datum testu : " + datum);
		writer.newLine();

		if(pocetO.isSelected() == true) {
			int vyber = 11; //promenna pro vyber otazek

			for (int i = 0; i<x;i++) {
				writer.write(otazky.get(vyber).getOtazka()); //vybere otazku a vytiskne
				writer.newLine();
				vyber = rnd.nextInt(otazky.size()); //zvoli jiny index
				//vytvorPDF();
				}
			}

		if(pocetB.isSelected() == true) {
			int body = 0; // nastavy pocatecni hodnotu bodu
			int vyber = 7; //prommenna pro vyber otazek
			boolean pokracovat = true; //jestli sestavil test nebo ne

			while (pokracovat == true) {
				body = body + otazky.get(vyber).getPocetBodu(); //pricte pocet bodu otazky
				if (body < x) {
					writer.write(otazky.get(vyber).getOtazka()); // vytiskne otazku
					writer.newLine();
					vyber = rnd.nextInt(otazky.size()); //zvoli jiny index
					pokracovat = true; //rika aby cyklus pokracoval
				}
				else if (body > x) {
					body = body - otazky.get(vyber).getPocetBodu(); //odecte body otazky
					vyber = (vyber + 7)%100; //zvoli jiny index
					pokracovat = true; //hleda v hodnou otazku do poctu
				}
				else if (body == x) {
					writer.write(otazky.get(vyber).getOtazka()); // vytiskne otazku
					writer.newLine();
					pokracovat = false; //ukonci cyklus
				}
			}
		}
		writer.close();
	}

	/**private void vytvorPDF() {

	}*/
}