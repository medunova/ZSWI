import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;

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


	public Generovani(ArrayList<Kategorie> kategorie, ArrayList<Otazka> otazky,
			ToggleButton pocetB, ToggleButton pocetO, String skola, String zkous,
			String pocet, String datum) throws IOException {
		this.otazky = otazky;
		this.kategorie = kategorie;

		int x = Integer.parseInt(pocet);
		writer = new BufferedWriter(new FileWriter("Test.txt", false));
		writer.write("Škola: " + skola + ", ");
		writer.write("Zkoušející: " + zkous + ", ");
		writer.write("Dnešní datum je : " + datum);
		writer.newLine();

		if(pocetO.isSelected() == true) {
			int vyber = 11; //promenna pro vyber otazek

			for (int i = 0; i<x;i++) {
				writer.write(otazky.get(vyber).getOtazka()); //vybere otazku a vytiskne
				writer.newLine();
				vyber =(vyber + 11)%100; //zvoli jiny index
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
					vyber = (vyber + 7)%100; //zvoli jiny index
					pokracovat = true; //rika aby cyklus pokracoval
				}
				else if (body > x) {
					body = body - otazky.get(vyber).getPocetBodu(); //odecte body otazky
					vyber = (vyber + 7)%100; //zvoli jiny index
					pokracovat = true; //hleda v hodnou otazku do poctu
				}
				else if (body == x) {
					writer.write(otazky.get(vyber).getOtazka()); // vytiskne otazku
					pokracovat = false; //ukonci cyklus
				}
			}
		}
		writer.close();
	}

	private void vytvorPDF() {
		try {
		// Create a document and add a page to it
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage( page );

		// Create a new font object selecting one of the PDF base fonts
		PDFont font = PDTrueTypeFont.loadTTF(document, "Arial.ttf");

		// Start a new content stream which will "hold" the to be created content
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
		contentStream.beginText();
		contentStream.setFont( font, 12 );
		contentStream.moveTextPositionByAmount( 100, 700 );
		contentStream.drawString( "Štastný" );
		contentStream.endText();

		// Make sure that the content stream is closed:
		contentStream.close();

		// Save the results and ensure that the document is properly closed:
		document.save( "Hello World.pdf");
		document.close();
		}
		catch(Exception e) {
			System.out.println("Nekde se stala chyba");
		}

	}
}
