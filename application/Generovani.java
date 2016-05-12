import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Trida pro vygenerovani testu
 * @author Aneta Medunova, Tomas Zobac, Michal Vselko
 *
 */
public class Generovani {

	ArrayList<Otazka> vybraneOtazky = new ArrayList<Otazka>();								//seznam vygenerovaných otázek
	ArrayList<Otazka> nevybraneOtazky;														//seznam nevybraných otázek

    /** Cesta a nazev vytvoreneho PDF souboru */
    public static String pdfNazev;

    /**
     * Konstruktor tøídy pro generování a export do pDF
     * @param otazky seznam všech otázek
     * @param skola název školy
     * @param zkous název zkoušejícího
     * @param body	true, pokud byl vybrán export podle poètu bodù
     * @param pocet poèet bodù nebo otázek
     * @param idTest id testu
     * @param datum datum zkoušky
     */
	public Generovani(ArrayList<Otazka> otazky, String idTest, LocalDate datum, String skola, String zkous, String predmet, Boolean body, int pocet) {

		this.pdfNazev = "testy/"+idTest+".pdf";
		try {
			nevybraneOtazky = (ArrayList<Otazka>)otazky.clone();
			generujOtazky(nevybraneOtazky, body, pocet);
			vytvorPDF(skola, zkous,predmet, datum, vybraneOtazky);

		}
		catch (DocumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Export dat.");
			alert.setHeaderText("Chyba pøi exportu dat do PDF!");
			alert.setContentText("Nepodaøilo se vyexportovat data do PDF.");
			alert.show();
		}
		catch (Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Export dat.");
			alert.setHeaderText("Chyba pøi exportu dat do PDF!");
			alert.setContentText("Nepodaøilo se vyexportovat data do PDF.");
			alert.show();
		}

	}

	/**
	 * Metoda pro generování náhodných otázek
	 * @param otazky celkový seznam otázek pro náhodný výbìr
	 * @param body true pokud se test generuje podle poètu bodù, jinak false
	 * @param pocet poèet bodù nebo otázek
	 */
	private void generujOtazky(ArrayList<Otazka> otazky, Boolean body, int pocet){
		Random rnd = new Random();
		if(body) {																			//je-li true, bylo vybráno generování podle poètu bodù
			int aktualniPocet = 0;
			int pocetOpakovani = 0;
			while(aktualniPocet != pocet){													//cyklus bude probíhat dokuk poèet bodù náhodnì vygenerovaných otázek nebude požadovaný poèet
				int nahodneCislo = rnd.nextInt(nevybraneOtazky.size());						//vygeneruje se náhodné èíslo (max je poèet nevybraných otázek)
				Otazka otazka = nevybraneOtazky.get(nahodneCislo);							//ze seznamu nevybraných otázek se vybere otázka pod náhodnì vygenerovaným indexem
				if((aktualniPocet + otazka.getPocetBodu()) <= pocet){						//aktuální poèet bodù se seète s poètem bodù nové otázky a porovná se cílovým poètem bodù
					vybraneOtazky.add(otazka);												//pokud poèet bodù nepøesáhne cílový poèet, pøidá se otázka do vybraných, odebere se z nevybraných a zaktualizuje se aktuální poèet bodù
					nevybraneOtazky.remove(otazka);
					aktualniPocet += otazka.getPocetBodu();
				}
				pocetOpakovani++;															/*poèet opakování se musí hlídat z dùvodu možného zacyklení
																							  bude-li napø. poèet aktuálních bodù 49 a už nebude žádná otázka s 1 bodem pro cílových 50 bodù
																							  pobìží cyklus poøád dokola - pøi 150 opakování se vybrané otázky vymažou a do nevybraných se doplní
																							  všechny otázky a náhodné generování bude probíhat od zaètku
																							*/
				if(pocetOpakovani == 150){
					vybraneOtazky.clear();
					nevybraneOtazky = otazky;
					aktualniPocet = 0;
					pocetOpakovani = 0;
				}
			}
		}
		else{
			for(int i = 0; i < pocet; i++){													//generování otázek podle nastaveného poètu otázek
				int nahodneCislo = rnd.nextInt(nevybraneOtazky.size());
				Otazka otazka = nevybraneOtazky.get(nahodneCislo);
				vybraneOtazky.add(otazka);
				nevybraneOtazky.remove(otazka);
			}
		}
	}

	private void vytvorPDF(String skola, String zkousejici,String predmet, LocalDate datum, ArrayList<Otazka> vybraneOtazky) throws DocumentException, IOException{


    	// Nastaveni velikosti dokumentu
        Rectangle velikostStranky = new Rectangle(PageSize.A4);	//nastavi rozmery dokumentu A4
        Document document = new Document(velikostStranky, 30f, 30f, 20f, 15f);	//vytvoreni dokumentu o velikosti velikostStranky a nastaveni okraju

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfNazev));	//vytvoreni PDF

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
        Paragraph hlavicka = new Paragraph(skola,hlavickaFont);
        hlavicka.add(new Chunk(rozmisteni));
        hlavicka.add("Pøíjmení a jméno: ....................");
        document.add(hlavicka);
        hlavicka.clear();

        hlavicka.add(predmet);
        hlavicka.add(new Chunk(rozmisteni));
        hlavicka.add("Osobní èíslo: ....................");
        document.add(hlavicka);
        hlavicka.clear();


        hlavicka.add("Zkoušející: "+zkousejici);
        hlavicka.add(new Chunk(rozmisteni));
        hlavicka.add("Tým: ....................");
        document.add(hlavicka);	//zapise do souboru

        hlavicka.clear();
        hlavicka.add("Datum: "+datum);
        document.add(hlavicka);	//zapise do souboru
        document.add(new Paragraph(" ")); //odradkuje

        Font otazkyFont = new Font(bfArial, 11.0f, Font.NORMAL, BaseColor.BLACK); //nastaveni pisma otazek

        tiskOtazek(document, writer, otazkyFont);
        document.close();	//zavre soubor
	}

	/**
	 * Metoda seøadí a pøidá do exportu jednotlivé otázky
	 * @param document soubor který se exportuje
	 * @param otazkyFont font otázek
	 */
	private void tiskOtazek(Document document, PdfWriter writer, Font otazkyFont) throws DocumentException{
		int prostor = 37;																							//poèet øádkù na první stránce pro otázky
		boolean nalezeno, dalsiStranka = false;																					//nalezena otázka pro vložení na stránce
		int cislo = 0;
		Comparator comp = Comparator.comparing(Otazka::getMisto);
		Collections.sort(vybraneOtazky, Collections.reverseOrder(comp));											//seøadí otázky v seznamu od nejvìtší velikosti místa

		while(vybraneOtazky.size() > 0){																			//cyklus pobìží dokud nejsou na stránce všechny vygenerované otázky
			do{
				nalezeno = false;
				dalsiStranka = false;
				for(int i = 0; i < vybraneOtazky.size(); i++){														//cyklus projde všechny otázky dokud nenarazí na tu která se na stránku vejde
					Otazka otazka = vybraneOtazky.get(i);
					int pocetRadku = otazka.getPocetRadkuText() + otazka.getPocetRadkuMisto();						//nastaví se poèet nutných øádkù pro otázku
					if((prostor - pocetRadku) >= 0){																//vejde-li se otázka do zbývajícího prostoru, doplní se do dokumentu
						Paragraph otazkaTisk = new Paragraph(++cislo + "." + otazka.getOtazka()+"("+otazka.getPocetBodu()+"b.)", otazkyFont);
						document.add(otazkaTisk);
						prostor -= pocetRadku;
						int cisloStranky = writer.getPageNumber();
						for(int j = 0; j < otazka.getPocetRadkuMisto(); j++){										//nastaví volné øádky pro odpovìï
					        if(cisloStranky == writer.getPageNumber()){
					        	document.add(new Paragraph(" "));
					        }
					        else{
					        	dalsiStranka = true;
					        	break;
					        }
						}

						vybraneOtazky.remove(otazka);																//otázka se odebere ze seznamu
						nalezeno = true;
						break;
					}
					if(nalezeno) break;
				}
			}
			while (nalezeno && !dalsiStranka);																		/*pokud nebyla otázka nalezena, znamená to buï že je seznam prázdný
																													  nebo se na stránku už žádná z vygenerovaných otázek nevejde
																													 */
			if(!dalsiStranka)
				document.newPage();
			prostor = 43;																							//nová velikost prostoru pro stránky bez hlavièky
		}
	}



}