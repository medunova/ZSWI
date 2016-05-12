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

	ArrayList<Otazka> vybraneOtazky = new ArrayList<Otazka>();								//seznam vygenerovan�ch ot�zek
	ArrayList<Otazka> nevybraneOtazky;														//seznam nevybran�ch ot�zek

    /** Cesta a nazev vytvoreneho PDF souboru */
    public static String pdfNazev;

    /**
     * Konstruktor t��dy pro generov�n� a export do pDF
     * @param otazky seznam v�ech ot�zek
     * @param skola n�zev �koly
     * @param zkous n�zev zkou�ej�c�ho
     * @param body	true, pokud byl vybr�n export podle po�tu bod�
     * @param pocet po�et bod� nebo ot�zek
     * @param idTest id testu
     * @param datum datum zkou�ky
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
			alert.setHeaderText("Chyba p�i exportu dat do PDF!");
			alert.setContentText("Nepoda�ilo se vyexportovat data do PDF.");
			alert.show();
		}
		catch (Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Export dat.");
			alert.setHeaderText("Chyba p�i exportu dat do PDF!");
			alert.setContentText("Nepoda�ilo se vyexportovat data do PDF.");
			alert.show();
		}

	}

	/**
	 * Metoda pro generov�n� n�hodn�ch ot�zek
	 * @param otazky celkov� seznam ot�zek pro n�hodn� v�b�r
	 * @param body true pokud se test generuje podle po�tu bod�, jinak false
	 * @param pocet po�et bod� nebo ot�zek
	 */
	private void generujOtazky(ArrayList<Otazka> otazky, Boolean body, int pocet){
		Random rnd = new Random();
		if(body) {																			//je-li true, bylo vybr�no generov�n� podle po�tu bod�
			int aktualniPocet = 0;
			int pocetOpakovani = 0;
			while(aktualniPocet != pocet){													//cyklus bude prob�hat dokuk po�et bod� n�hodn� vygenerovan�ch ot�zek nebude po�adovan� po�et
				int nahodneCislo = rnd.nextInt(nevybraneOtazky.size());						//vygeneruje se n�hodn� ��slo (max je po�et nevybran�ch ot�zek)
				Otazka otazka = nevybraneOtazky.get(nahodneCislo);							//ze seznamu nevybran�ch ot�zek se vybere ot�zka pod n�hodn� vygenerovan�m indexem
				if((aktualniPocet + otazka.getPocetBodu()) <= pocet){						//aktu�ln� po�et bod� se se�te s po�tem bod� nov� ot�zky a porovn� se c�lov�m po�tem bod�
					vybraneOtazky.add(otazka);												//pokud po�et bod� nep�es�hne c�lov� po�et, p�id� se ot�zka do vybran�ch, odebere se z nevybran�ch a zaktualizuje se aktu�ln� po�et bod�
					nevybraneOtazky.remove(otazka);
					aktualniPocet += otazka.getPocetBodu();
				}
				pocetOpakovani++;															/*po�et opakov�n� se mus� hl�dat z d�vodu mo�n�ho zacyklen�
																							  bude-li nap�. po�et aktu�ln�ch bod� 49 a u� nebude ��dn� ot�zka s 1 bodem pro c�lov�ch 50 bod�
																							  pob�� cyklus po��d dokola - p�i 150 opakov�n� se vybran� ot�zky vyma�ou a do nevybran�ch se dopln�
																							  v�echny ot�zky a n�hodn� generov�n� bude prob�hat od za�tku
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
			for(int i = 0; i < pocet; i++){													//generov�n� ot�zek podle nastaven�ho po�tu ot�zek
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
        hlavicka.add("P��jmen� a jm�no: ....................");
        document.add(hlavicka);
        hlavicka.clear();

        hlavicka.add(predmet);
        hlavicka.add(new Chunk(rozmisteni));
        hlavicka.add("Osobn� ��slo: ....................");
        document.add(hlavicka);
        hlavicka.clear();


        hlavicka.add("Zkou�ej�c�: "+zkousejici);
        hlavicka.add(new Chunk(rozmisteni));
        hlavicka.add("T�m: ....................");
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
	 * Metoda se�ad� a p�id� do exportu jednotliv� ot�zky
	 * @param document soubor kter� se exportuje
	 * @param otazkyFont font ot�zek
	 */
	private void tiskOtazek(Document document, PdfWriter writer, Font otazkyFont) throws DocumentException{
		int prostor = 37;																							//po�et ��dk� na prvn� str�nce pro ot�zky
		boolean nalezeno, dalsiStranka = false;																					//nalezena ot�zka pro vlo�en� na str�nce
		int cislo = 0;
		Comparator comp = Comparator.comparing(Otazka::getMisto);
		Collections.sort(vybraneOtazky, Collections.reverseOrder(comp));											//se�ad� ot�zky v seznamu od nejv�t�� velikosti m�sta

		while(vybraneOtazky.size() > 0){																			//cyklus pob�� dokud nejsou na str�nce v�echny vygenerovan� ot�zky
			do{
				nalezeno = false;
				dalsiStranka = false;
				for(int i = 0; i < vybraneOtazky.size(); i++){														//cyklus projde v�echny ot�zky dokud nenaraz� na tu kter� se na str�nku vejde
					Otazka otazka = vybraneOtazky.get(i);
					int pocetRadku = otazka.getPocetRadkuText() + otazka.getPocetRadkuMisto();						//nastav� se po�et nutn�ch ��dk� pro ot�zku
					if((prostor - pocetRadku) >= 0){																//vejde-li se ot�zka do zb�vaj�c�ho prostoru, dopln� se do dokumentu
						Paragraph otazkaTisk = new Paragraph(++cislo + "." + otazka.getOtazka()+"("+otazka.getPocetBodu()+"b.)", otazkyFont);
						document.add(otazkaTisk);
						prostor -= pocetRadku;
						int cisloStranky = writer.getPageNumber();
						for(int j = 0; j < otazka.getPocetRadkuMisto(); j++){										//nastav� voln� ��dky pro odpov��
					        if(cisloStranky == writer.getPageNumber()){
					        	document.add(new Paragraph(" "));
					        }
					        else{
					        	dalsiStranka = true;
					        	break;
					        }
						}

						vybraneOtazky.remove(otazka);																//ot�zka se odebere ze seznamu
						nalezeno = true;
						break;
					}
					if(nalezeno) break;
				}
			}
			while (nalezeno && !dalsiStranka);																		/*pokud nebyla ot�zka nalezena, znamen� to bu� �e je seznam pr�zdn�
																													  nebo se na str�nku u� ��dn� z vygenerovan�ch ot�zek nevejde
																													 */
			if(!dalsiStranka)
				document.newPage();
			prostor = 43;																							//nov� velikost prostoru pro str�nky bez hlavi�ky
		}
	}



}