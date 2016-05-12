import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.DatePicker;
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
 * Třida pro správu hlavičky testu, vybírání kategorií a kliče pro generování testu
 * @author Aneta Medunova, Michal Všelko a Tomáš Zobač
 *
 */
public class oknoExportuj extends Stage {

	final ToggleGroup group = new ToggleGroup();
	private DatePicker datePicker;
	private MenuButton choices;
	private BorderPane hlPanel;
	private ToggleButton PBody, POtazky;
	private TextField pocty, institut, zkousejici, predmet;
	private Button tisk;
	private ArrayList<Otazka> otazky, vybrane;
	private ArrayList<Kategorie> kategorie;
	private static Scanner sc;
	private FileWriter writer;
	private Label done;
/**
 * Konstruktur, který přebirá otázky a jejich kategorie
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

		Scene scene = new Scene(hlPanel, 621, 414);
		this.setScene(scene);
		scene.getStylesheets().add
		 (oknoExportuj.class.getResource("vzhled.css").toExternalForm());

		this.setTitle("Exportovat do pdf");
	}
	/**
	 * Vrací nabídku správy hlavičku testu
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
		zkousejici.setText("Roman Mouček"); //defaultní nastavení
		Label text2 = new Label("Zkoušející: ");
		text2.setId("text1");
		predmet = new TextField();
		predmet.setText("Základy softwarového inženýrství"); //defaultní nastavení
		Label text5 = new Label("Předmět: ");
		text5.setId("text1");

		datePicker = new DatePicker();
		 datePicker.setValue(LocalDate.now());
		String idTestu = idTestu(); //nacitani jednoznačného ID Testu
		Label text3 = new Label("ID testu: " + '\t' + idTestu);
		text3.setId("text1");
		Label text4 = new Label('\t' + "Datum testu: ");
		text4.setId("text1");

		choices = vyberKat(); // výběr kategorii

		radek1.getChildren().addAll(text1,institut,text2,zkousejici);
		radek2.getChildren().addAll(text5,predmet,text4, datePicker);
		radek3.getChildren().addAll(text3, choices);

		vbox.getChildren().addAll(radek1, radek2,radek3);

		return vbox;
	}
	/**
	 * Vrací nabídku pro výběr kritérii pro tvoření testu. Buď podle počtu otázek,
	 * nebo počtu bodů
	 * @return vertikální Box
	 */

	private VBox vyber() {
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(15));

		pocty = new TextField();
		Label popis = new Label("Zadejte počet: ");
		popis.setId("text1"); //vzhled  z *.css souboru

		PBody = new ToggleButton("Počet bodů");
		PBody.setToggleGroup(group);
		PBody.setSelected(true);
		PBody.setId("PBody"); //vzhled z *.css souboru

		POtazky = new ToggleButton("Počet otázek");
		POtazky.setToggleGroup(group);
		POtazky.setId("POtazky"); //vzhled  z *.css souboru

		Label text = new Label("Vybrat náhodně dle: ");
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
	 * Vrací tlačítko pro spuštění generování testu se zadanými parametry
	 * Metoda si nabírá vybrané otázky, údaje z hlavičky (Zkoušející, škola, datum atd.)
	 * a informaci, jak má vytvořit test (jetli podle bodů nebo otázek)
	 * @return vrací tlačítko a hlášku, že je test hotov
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
 * Metoda na vytváření tlačítka
 * @return hotové tlačítko
 */
	private Button vytvor() {
		Button bt = new Button("Vytvoř test");
		bt.setOnAction((ActionEvent e) -> {
			//Pokud je vybrané tvoření podle bodů
			if(PBody.isSelected() == true) {
				try {
					new Generovani(vybrane, idTestu(), datePicker.getValue(), institut.getText(), zkousejici.getText(), predmet.getText(), true, Integer.parseInt(pocty.getText()));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			//Pokud je vybrané tvoření podle počtu otázek
			if(POtazky.isSelected() == true) {
				try {
					new Generovani(vybrane, idTestu(), datePicker.getValue(), institut.getText(), zkousejici.getText(), predmet.getText(), false, Integer.parseInt(pocty.getText()));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			Alert inform = new Alert(AlertType.INFORMATION);
			inform.setTitle("Hotovo");
			inform.setHeaderText("Váš test byl vytvořen");

			inform.show(); // ukáže informační okno
			// počká 3 vteřiny
			long mTime = System.currentTimeMillis();
			long end = mTime + 2000;
			while (mTime < end)
			{
			    mTime = System.currentTimeMillis();
			}
			//a pak okno zavře
			inform.close();
		});

		return bt;
	}
/**
 * Metoda, která si načítá informaci o ID testu. Po načtení inkrementuje počítadlo
 * pro další načítání.
 * @return vrací jednoznačné ID testu.
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
			alert.setTitle("Načtení ID testu");
			alert.setHeaderText("Soubor nenalezen");

			alert.showAndWait();
		}
		s = "ZSWI2016" + String.format("%03d", p); //pro správný poèet èísel
		return s;
	}
/**
 * Vrací ComboBox s chooseBoxy, ve kterých se vybírají kategorie. Označením nebo odznačením
 * dané kategorie se upraví sada otázek, ze kterých se bude následně tisknout
 * @return ComBoBox s ChooseBoxy.
 */
	private MenuButton vyberKat() {
		MenuButton vyber = new MenuButton("Kategorie"); //ComboBox
		ArrayList<CheckMenuItem> checkItems = new ArrayList<CheckMenuItem>();

		for(int i = 0; i < kategorie.size(); i++){
			CheckMenuItem kat = new CheckMenuItem(kategorie.get(i).getNazev());
			kat.setSelected(true);
			checkItems.add(kat);
		}

        vyber.getItems().addAll(checkItems);

          // spravuje odznačení a označení kategorií
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
	 * @param idKat - číslo kategorie
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
	 * @param idKat číslo kategorie
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