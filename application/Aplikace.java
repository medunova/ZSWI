import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
/**
 * Třída pro spuštění aplikace
 * @author Aneta Medunová, Tomáš Zobač, Michal Všelko
 *
 */
public class Aplikace  extends Application{

	private static Scanner sc;

	public void start(Stage primaryStage){
		try{
			sc = new Scanner(new File("data/kategorie.txt"));									//načtení kategorií
			ArrayList<Kategorie> kategorie = new ArrayList<Kategorie>();

			while(sc.hasNextLine()){
				int id = sc.nextInt();
				String nazev = sc.nextLine().trim();
				kategorie.add(new Kategorie(id, nazev));
			}

			sc = new Scanner(new File("data/otazky.txt"));										//načtení otázek
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
			alert.setTitle("Načítání dat.");
			alert.setHeaderText("Nebyly nalezeny potřebné soubory!");
			alert.setContentText("Program nenalezl soubor kategorie.txt nebo otazky.txt.");
			alert.show();
		}
		catch (Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Spuštění programu.");
			alert.setHeaderText("Chyba při spuštění programu!");
			alert.setContentText("Program nebylo možné spustit.");
			alert.show();
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		launch(args);
	}

}
