package application;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.*;

public class OknoExportuj extends Stage {

	final ToggleGroup group = new ToggleGroup();
	private BorderPane hlPanel;
	private ToggleButton PBody, POtazky;
	private TextField pocty, institut, zkousejici;
	private Button tisk, export;
	private String datum;
	private ArrayList<Otazka> otazky;
	private ArrayList<Kategorie> kategorie;

	public OknoExportuj(ArrayList<Kategorie> kategorie, ArrayList<Otazka> otazky) {
		hlPanel = new BorderPane();

		hlPanel.setTop(hlavicka());
		hlPanel.setCenter(vyber());
		hlPanel.setBottom(tlacitka());

		Scene scene = new Scene(hlPanel, 600, 400);
		this.setScene(scene);
		scene.getStylesheets().add
		 (OknoExportuj.class.getResource("oknoExportuj.css").toExternalForm());

		this.setTitle("Exportovat do pdf");
	}

	private VBox hlavicka() {
		VBox vbox = new VBox(15);
		vbox.setPadding(new Insets(20));
		vbox.setAlignment(Pos.CENTER);
		HBox radek1 = new HBox(10);
		HBox radek2 = new HBox(10);

		institut = new TextField();
		Label text1 = new Label("Škola: ");
		text1.setId("text1");
		zkousejici = new TextField();
		Label text2 = new Label("Zkoušející: ");
		text2.setId("text2");

		String idTestu = "Vygenerované èíslo";
		Label text3 = new Label("ID testu: " + '\t' + idTestu);
		text3.setId("text3");
		DateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");
		Date date = new Date();
		datum = dateFormat.format(date);
		Label text4 = new Label('\t' + "Dnešní datum: " +'\t' + datum);
		text4.setId("text4");


		radek1.getChildren().addAll(text1,institut,text2,zkousejici);
		radek2.getChildren().addAll(text3,text4);

		vbox.getChildren().addAll(radek1, radek2);

		return vbox;
	}

	private VBox vyber() {
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(15));

		pocty = new TextField();
		Label popis = new Label("Zadejte pocet: ");
		popis.setId("popis");

		PBody = new ToggleButton("Poèet bodù");
		PBody.setToggleGroup(group);
		PBody.setSelected(true);
		PBody.setId("PBody");

		POtazky = new ToggleButton("Poèet otázek");
		POtazky.setToggleGroup(group);
		POtazky.setId("POtazky");

		Label text = new Label("Vybrat náhodnì dle: ");
		text.setId("text");

		HBox radek1 = new HBox(10);
		radek1.getChildren().addAll(text, PBody, POtazky);
		radek1.setAlignment(Pos.CENTER);

		HBox radek2 = new HBox(50);
		radek2.getChildren().addAll(popis, pocty);
		radek2.setAlignment(Pos.CENTER);

		vbox.getChildren().addAll(radek1, radek2);
		return vbox;
	}

	private HBox tlacitka() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(5,5,20,5));
		tisk = vytiskni();
		tisk.setId("tisk");
		export = exportuj();
		export.setId("export");

		hbox.getChildren().addAll(tisk, export);

		return hbox;
	}

	private Button vytiskni() {
		Button bt = new Button("Vytiskni");
		bt.setOnAction((ActionEvent e) -> {
			if(PBody.isSelected() == true) {
				String pocet = pocty.getText();
				String skola = institut.getText();
				String ucit = zkousejici.getText();

				System.out.print("Test bude tvoøen body ");
				System.out.println("a bude mít " + pocet + " bodù.");
				System.out.println("Hlavièka bude mit tvar: ");
				System.out.print("Škola: " + skola + ", ");
				System.out.print("Zkoušející: " + ucit + ", ");
				System.out.print("Dnešní datum je : " + datum);
			}

			if(POtazky.isSelected() == true) {
				String pocet = pocty.getText();
				String skola = institut.getText();
				String ucit = zkousejici.getText();

				System.out.print("Test bude tvoøen otázkami. ");
				System.out.println("a bude mít " + pocet + " otázek.");
				System.out.println("Hlavièka bude mit tvar: ");
				System.out.print("Škola: " + skola + ", ");
				System.out.print("Zkoušející: " + ucit + ", ");
				System.out.print("Dnešní datum je : " + datum);
			}
		});

		return bt;
	}

	private Button exportuj() {
		Button bt = new Button("Vytvor pdf");
		bt.setOnAction((ActionEvent e) -> {
			if(PBody.isSelected() == true) {
				String pocet = pocty.getText();
				String skola = institut.getText();
				String ucit = zkousejici.getText();

				System.out.print("PDF soubor byl vytvoren ");
				System.out.println("a bude mít " + pocet + " bodù.");
				System.out.println("Hlavièka bude mit tvar: ");
				System.out.print("Škola: " + skola + ", ");
				System.out.print("Zkoušející: " + ucit + ", ");
				System.out.print("Dnešní datum je : " + datum);
			}

			if(POtazky.isSelected() == true) {
				String pocet = pocty.getText();
				String skola = institut.getText();
				String ucit = zkousejici.getText();

				System.out.print("PDF soubor byl vytvoren. ");
				System.out.println("Test bude mít " + pocet + " otázek.");
				System.out.println("Hlavièka bude mit tvar: ");
				System.out.print("Škola: " + skola + ", ");
				System.out.print("Zkoušející: " + ucit + ", ");
				System.out.print("Dnešní datum je : " + datum);
			}
		});

		return bt;
	}


}
