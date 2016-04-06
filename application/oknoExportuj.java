import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
import javafx.stage.*;

public class oknoExportuj extends Stage {

	final ToggleGroup group = new ToggleGroup();
	private BorderPane hlPanel;
	private ToggleButton PBody, POtazky;
	private TextField pocty, institut, zkousejici, datum;
	private Button tisk;
	private ArrayList<Otazka> otazky;
	private ArrayList<Kategorie> kategorie;
	private static Scanner sc;
	private FileWriter writer;
	private Label done;

	public oknoExportuj(ArrayList<Kategorie> kategorie, ArrayList<Otazka> otazky) {
		this.kategorie = kategorie;
		this.otazky = otazky;

		hlPanel = new BorderPane();

		hlPanel.setTop(hlavicka());
		hlPanel.setCenter(vyber());
		hlPanel.setBottom(tlacitka());

		Scene scene = new Scene(hlPanel, 600, 400);
		this.setScene(scene);
		scene.getStylesheets().add
		 (oknoExportuj.class.getResource("oknoExportuj.css").toExternalForm());

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

		String idTestu = idTestu();
		Label text3 = new Label("ID testu: " + '\t' + idTestu);
		text3.setId("text3");
		datum = new TextField();
		Label text4 = new Label('\t' + "Datum testu: ");
		text4.setId("text4");


		radek1.getChildren().addAll(text1,institut,text2,zkousejici);
		radek2.getChildren().addAll(text3,text4, datum);

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

	private VBox tlacitka() {
		VBox hbox = new VBox(5);
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(5,5,20,5));
		tisk = vytiskni();
		tisk.setId("tisk");
		done = new Label("");

		hbox.getChildren().addAll(tisk, done);

		return hbox;
	}

	private Button vytiskni() {
		Button bt = new Button("Vytvoø test");
		bt.setOnAction((ActionEvent e) -> {
			if(PBody.isSelected() == true) {
				try {
					new Generovani(kategorie, otazky,PBody,POtazky,
							institut.getText(),zkousejici.getText(),pocty.getText(),
							idTestu(), datum.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			if(POtazky.isSelected() == true) {

				System.out.println("Test byl vytvoøen");
				try {
					new Generovani(kategorie, otazky,PBody,POtazky,
							institut.getText(),zkousejici.getText(),pocty.getText(),
							idTestu(), datum.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			done.setText(" Test byl vytvoren");
		});

		return bt;
	}

	private String idTestu() {
		String s;
		int p = 0;
		try {
			sc = new Scanner(new File("IDTESTU.txt"));
			s = sc.next();
			p = Integer.parseInt(s) + 1;
			
			writer = new FileWriter("IDTESTU.txt");
			writer.write(Integer.toString(p));
			writer.close();
		}
		catch (Exception e) {
			System.out.println("Soubor nenalezen");
		}
		s = "ZSWI2016" + String.format("%03d", p);
		return s;
	}
}
