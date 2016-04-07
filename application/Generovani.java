import java.awt.Dialog;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.ParagraphView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Font.FontStyle;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.sun.javafx.font.FontFactory;

import javafx.scene.control.ToggleButton;
/**
 * Trida pro vygenerovani testu
 * @author Aneta Medunova, Tomas Zobac, Michal Vselko
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

    /** Cesta a nazev vytvoreneho PDF souboru */
    public static final String pdfNazev = "./ZSWI.pdf";


	public Generovani(ArrayList<Kategorie> kategorie, ArrayList<Otazka> otazky,
			ToggleButton pocetB, ToggleButton pocetO, String skola, String zkous,
			String pocet, String idTest, String datum) throws IOException {

		this.otazky = otazky;
		this.kategorie = kategorie;
		rnd = new Random();
		ArrayList<String> vybraneOtazky = new ArrayList<String>();
		String nahodnaOtazka;

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
				nahodnaOtazka = otazky.get(vyber).getOtazka();
				writer.write(nahodnaOtazka); //vybere otazku a vytiskne
				vybraneOtazky.add(nahodnaOtazka);

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
					nahodnaOtazka = otazky.get(vyber).getOtazka();
					writer.write(nahodnaOtazka); // vytiskne otazku
					writer.newLine();
					vybraneOtazky.add(nahodnaOtazka);
					vyber = rnd.nextInt(otazky.size()); //zvoli jiny index
					pokracovat = true; //rika aby cyklus pokracoval
				}
				else if (body > x) {
					body = body - otazky.get(vyber).getPocetBodu(); //odecte body otazky
					vyber = (vyber + 7)%100; //zvoli jiny index
					pokracovat = true; //hleda v hodnou otazku do poctu
				}
				else if (body == x) {
					nahodnaOtazka = otazky.get(vyber).getOtazka();
					writer.write(nahodnaOtazka); // vytiskne otazku
					writer.newLine();
					vybraneOtazky.add(nahodnaOtazka);

					pokracovat = false; //ukonci cyklus
				}
			}

			try {
				vytvorPDF(skola, zkous, datum, vybraneOtazky);
			} catch (DocumentException e) {
				System.err.print("Chyba pri exportu do PDF");
			}
		}
		writer.close();
	}

	private void vytvorPDF(String skola, String zkousejici, String datum, ArrayList<String> vybraneOtazky) throws DocumentException, IOException{


    	// Nastaveni velikosti dokumentu
        Rectangle velikostStranky = new Rectangle(PageSize.A4);	//nastavi rozmery dokumentu A4
        Document document = new Document(velikostStranky, 30f, 30f, 20f, 15f);	//vytvoreni dokumentu o velikosti velikostStranky a nastaveni okraju

        PdfWriter.getInstance(document, new FileOutputStream(pdfNazev));	//vytvoreni PDF

        document.open();	//otevre souboru

        //Pridani ceskeho fontu
        BaseFont bfArial = BaseFont.createFont
        		(
        		"c:\\windows\\fonts\\arial.ttf",
        		BaseFont.IDENTITY_H,
        		BaseFont.EMBEDDED
        		);



        document.addTitle("Vygenerovany test");	//nastavi titulek dokumentu

        Font hlavickaFont = new Font(bfArial, 14.0f, Font.NORMAL, BaseColor.BLACK); //nastaveni pisma hlavicky

        //Vypise hlavicku
        Chunk rozmisteni = new Chunk(new VerticalPositionMark());
        Paragraph hlavicka = new Paragraph("Škola: "+skola,hlavickaFont);
        hlavicka.add(new Chunk(rozmisteni));
        hlavicka.add("Pøedmìt: ");
        hlavicka.add(new Chunk(rozmisteni));
        hlavicka.add("Pøíjmení a jméno: ....................");
        document.add(hlavicka);
        hlavicka.clear();

        hlavicka.add("Zkoušející: "+zkousejici);
        hlavicka.add(new Chunk(rozmisteni));
        hlavicka.add("Osobní èíslo: ....................");
        document.add(hlavicka);
        hlavicka.clear();

        hlavicka.add("Datum: "+datum);
        hlavicka.add(new Chunk(rozmisteni));
        hlavicka.add("Pracovní skupina: ....................");
        document.add(hlavicka);	//zapise do souboru

        document.add(new Paragraph(" ")); //odradkuje

        //Vypis otazek
        int i = 0;
        Paragraph otazkaTisk;
        Font otazkyFont = new Font(bfArial, 11.0f, Font.NORMAL, BaseColor.BLACK); //nastaveni pisma otazek

        while(i < vybraneOtazky.size()){	//cyklus projde seznam vybranych otazek a vytiskne je do pdf

    	  String otazka = vybraneOtazky.get(i);
    	  otazkaTisk = new Paragraph((i+1)+". "+otazka, otazkyFont);
    	  document.add(otazkaTisk);

    	  i++;
    	  }

        document.close();	//zavre soubor
	}
}