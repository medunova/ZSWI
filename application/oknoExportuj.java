import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
/**
 * Tøida pro správu hlavièky testu, vybírání kategorií a klièe pro generování testu
 * @author Aneta Medunova, Michal Všelko a Tomáš Zobaè
 *
 */
public class oknoExportuj extends Stage {

	final ToggleGroup group = new ToggleGroup();
	private MenuButton choices;
	private BorderPane hlPanel;
	private ToggleButton PBody, POtazky;
	private TextField pocty, institut, zkousejici, datum, predmet;
	private Button tisk;
	private ArrayList<Otazka> otazky, vybrane;
	private ArrayList<Kategorie> kategorie;
	private static Scanner sc;
	private FileWriter writer;
	private Label done;
/**
 * Konstruktur, který pøebirá otázky a jejich kategorie
 * @param kategorie seznam kategorii
 * @param otazky seznam otázek
 */
	public oknoExportuj(ArrayList<Kategorie> kategorie, ArrayList<Otazka> otazky) {
		this.kategorie = kategorie;
		this.otazky = otazky;
		vybrane = (ArrayList<Otazka>)otazky.clone();

		hlPanel = new BorderPane();

		hlPanel.setTop(hlavicka());
		hlPanel.setCenter(vyber());
		hlPanel.setBottom(tlacitka());

		Scene scene = new Scene(hlPanel, 600, 400);
		this.setScene(scene);
		scene.getStylesheets().add
		 (oknoExportuj.class.getResource("vzhled.css").toExternalForm());

		this.setTitle("Exportovat do pdf");
	}
	/**
	 * Vrací nabídku správy hlavièku testu
	 * @return vertikální Box
	 */

	private VBox hlavicka() {
		VBox vbox = new VBox(15);
		vbox.setPadding(new Insets(20));
		vbox.setAlignment(Pos.CENTER);
		HBox radek1 = new HBox(10);
		HBox radek2 = new HBox(10);
		HBox radek3 = new HBox(10);

		institut = new TextField();
		institut.setText("Fakulta aplikovaných vìd"); //defaultní nastavení
		Label text1 = new Label("Škola: ");
		text1.setId("text1");
		zkousejici = new TextField();
		zkousejici.setText("Roman Mouèek"); //defaultní nastavení
		Label text2 = new Label("Zkoušející: ");
		text2.setId("text1");
		predmet = new TextField();
		predmet.setText("Základy softwarového inženýrství"); //defaultní nastavení
		Label text5 = new Label("Pøedmìt: ");
		text5.setId("text1");

		String idTestu = idTestu(); //nacitani jednoznaèného ID Testu
		Label text3 = new Label("ID testu: " + '\t' + idTestu);
		text3.setId("text1");
		datum = new TextField();
		Label text4 = new Label('\t' + "Datum testu: ");
		text4.setId("text1");

		choices = vyberKat(); // výbìr kategorii

		radek1.getChildren().addAll(text1,institut,text2,zkousejici);
		radek2.getChildren().addAll(text5,predmet,text4, datum);
		radek3.getChildren().addAll(text3, choices);

		vbox.getChildren().addAll(radek1, radek2,radek3);

		return vbox;
	}
	/**
	 * Vrací nabídku pro výbìr kritérii pro tvoøení testu. Buï podle poètu otázek,
	 * nebo poètu bodù
	 * @return vertikální Box
	 */

	private VBox vyber() {
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(15));

		pocty = new TextField();
		Label popis = new Label("Zadejte pocet: ");
		popis.setId("text1"); //vzhled  z *.css souboru

		PBody = new ToggleButton("Poèet bodù");
		PBody.setToggleGroup(group);
		PBody.setSelected(true);
		PBody.setId("PBody"); //vzhled z *.css souboru

		POtazky = new ToggleButton("Poèet otázek");
		POtazky.setToggleGroup(group);
		POtazky.setId("POtazky"); //vzhled  z *.css souboru

		Label text = new Label("Vybrat náhodnì dle: ");
		text.setId("text1");

		HBox radek1 = new HBox(10);
		radek1.getChildren().addAll(text, PBody, POtazky);
		radek1.setAlignment(Pos.CENTER);

		HBox radek2 = new HBox(50);
		radek2.getChildren().addAll(popis, pocty);
		radek2.setAlignment(Pos.CENTER);

		vbox.getChildren().addAll(radek1, radek2);
		return vbox;
	}
	/**
	 * Vrací tlaèítko pro spuštìní generování testu se zadanými parametry
	 * Metoda si nabírá vybrané otázky, údaje z hlavièky (Zkoušející, škola, datum atd.)
	 * a informaci, jak má vytvoøit test (jetli podle bodù nebo otázek)
	 * @return vrací tlaèítko a hlášku, že je test hotov
	 */
	private VBox tlacitka() {
		VBox hbox = new VBox(5);
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(5,5,20,5));
		tisk = vytvor();
		tisk.setId("tisk");
		done = new Label("");

		hbox.getChildren().addAll(tisk, done);

		return hbox;
	}
/**
 * Metoda na vytváøení tlaèítka
 * @return hotové tlaèítko
 */
	private Button vytvor() {
		Button bt = new Button("Vytvoø test");
		bt.setOnAction((ActionEvent e) -> {
			//Pokud je vybrané tvoøení podle bodù
			if(PBody.isSelected() == true) {
				try {
					new Generovani(vybrane, idTestu(), datum.getText(), institut.getText(), zkousejici.getText(), predmet.getText(), true, Integer.parseInt(pocty.getText()));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			//Pokud je vybrané tvoøení podle poètu otázek
			if(POtazky.isSelected() == true) {
				try {
					new Generovani(vybrane, idTestu(), datum.getText(), institut.getText(), zkousejici.getText(), predmet.getText(), false, Integer.parseInt(pocty.getText()));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			Alert inform = new Alert(AlertType.INFORMATION);
			inform.setTitle("Hotovo");
			inform.setHeaderText("Váš test byl vytvoøen");

			inform.show(); // ukáže informaèní okno
			// poèká 3 vteøiny
			long mTime = System.currentTimeMillis();
			long end = mTime + 2000;
			while (mTime < end)
			{
			    mTime = System.currentTimeMillis();
			}
			//a pak okno zavøe
			inform.close();
		});

		return bt;
	}
/**
 * Metoda, která si naèítá informaci o ID testu. Po naètení inkrementuje poèítadlo
 * pro další naèítání.
 * @return vrací jednoznaèné ID testu.
 */
	private String idTestu() {
		String s;
		int p = 0;
		try {
			sc = new Scanner(new File("data/IDTESTU.txt"));
			s = sc.next();
			p = Integer.parseInt(s) + 1;

			writer = new FileWriter("data/IDTESTU.txt");
			writer.write(Integer.toString(p));
			writer.close();
		}
		catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Naètení ID testu");
			alert.setHeaderText("Soubor nenalezen");

			alert.showAndWait();
		}
		s = "ZSWI2016" + String.format("%03d", p); //pro správný poèet èísel
		return s;
	}
/**
 * Vrací ComboBox s chooseBoxy, ve kterých se vybírají kategorie. Oznaèením nebo odznaèením
 * dané kategorie se upraví sada otázek, ze kterých se bude následnì tisknout
 * @return ComBoBox s ChooseBoxy.
 */
	private MenuButton vyberKat() {
		MenuButton vyber = new MenuButton("Kategorie"); //ComboBox
		CheckMenuItem kat1; //Jednotlivé chooseBoxy
		CheckMenuItem kat2;
		CheckMenuItem kat3;
		CheckMenuItem kat4;
		CheckMenuItem kat5;
		CheckMenuItem kat6;
        List<CheckMenuItem> checkItems = Arrays.asList(
        	kat1 = new CheckMenuItem("Kategorie 1"),
            kat2 = new CheckMenuItem("Kategorie 2"),
            kat3 = new CheckMenuItem("Kategorie 3"),
            kat4 = new CheckMenuItem("Kategorie 4"),
            kat5 = new CheckMenuItem("Kategorie 5"),
            kat6 = new CheckMenuItem("Kategorie 6")
        );
        kat1.setSelected(true); //defaultnì jsou vybrané všechny katagorie
        kat2.setSelected(true);
        kat3.setSelected(true);
        kat4.setSelected(true);
        kat5.setSelected(true);
        kat6.setSelected(true);
        vyber.getItems().addAll(checkItems);

          // spravuje odznaèení a oznaèení kategorií
        final ListView<String> selectedItems = new ListView<>();
        for (final CheckMenuItem item : checkItems) {
            item.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> obs,
                        Boolean wasPreviouslySelected, Boolean isNowSelected) {
                    if (isNowSelected) {
                        selectedItems.getItems().add(item.getText());
                        pridejKat(checkItems.indexOf(item));
                    } else {
                        selectedItems.getItems().remove(item.getText());
                        odeberKat(checkItems.indexOf(item));
                    }
                }
            });
        }
        return vyber;
	}
	/**
	 * Metoda pro odebírání otázek ze sady pro generování
	 * @param idKat - èíslo kategorie
	 * @return vrací upravenou sadu otázek
	 */
	private ArrayList<Otazka> odeberKat(int idKat) {
	ArrayList<Otazka> pomocny = new ArrayList<Otazka>(); 						//Array pro odebirane otazky
		for (int i=0; i<vybrane.size();i++) {
			if(idKat+1 == vybrane.get(i).getIdKategorie()) {
				pomocny.add(vybrane.get(i));
			}
		}
		for (int i=0;i<pomocny.size();i++) {
			vybrane.remove(pomocny.get(i));
		}
		return vybrane;
	}
	/**
	 * Metoda pro vracení otázek do sady.
	 * @param idKat èíslo kategorie
	 * @return vrací upravenou sadu otázek
	 */
	private ArrayList<Otazka> pridejKat(int idKat) {
		for (int i=0; i<otazky.size();i++) {
			if(idKat+1 == otazky.get(i).getIdKategorie()) {
				vybrane.add(otazky.get(i));
			}
		}
		return vybrane;
	}
}
