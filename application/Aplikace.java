import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
/**
 * T��da pro spu�t�n� aplikace
 * @author Aneta Medunov�, Tom� Zoba�, Michal V�elko
 *
 */
public class Aplikace  extends Application{

	private static Scanner sc;

	public void start(Stage primaryStage){
		try{
			sc = new Scanner(new File("kategorie.txt"));									//na�ten� kategori�
			ArrayList<Kategorie> kategorie = new ArrayList<Kategorie>();

			while(sc.hasNextLine()){
				int id = sc.nextInt();
				String nazev = sc.nextLine().trim();
				kategorie.add(new Kategorie(id, nazev));
			}

			sc = new Scanner(new File("otazky.txt"));										//na�ten� ot�zek
			ArrayList<Otazka> otazky = new ArrayList<Otazka>();

			while(sc.hasNextLine()){
				int id = sc.nextInt();
				int pocetBodu = sc.nextInt();
				int misto = sc.nextInt();
				int idKategorie = sc.nextInt();
				String nazev = sc.nextLine().trim();
				otazky.add(new Otazka(id, nazev, pocetBodu, misto, idKategorie));
			}

			new HlavniOkno(kategorie, otazky);
		}
		catch (FileNotFoundException e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Na�ten� dat.");
			alert.setHeaderText("Nebyly nalezeny pot�ebn� soubory!");
			alert.setContentText("Program nenalezl soubor kategorie.txt nebo otazky.txt.");
			alert.show();
		}
		catch (Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Spu�t�n� programu.");
			alert.setHeaderText("Chyba p�i psu�t�n� programu!");
			alert.setContentText("Program nebylo mo�n� spustit.");
			alert.show();
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		launch(args);
	}

}
