import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Tøída okna pro pøidání nové otázky
 * @author Aneta Medunová, Tomáš Zobaè, Michal Všelko
 *
 */
public class OknoPridejOtazku extends Stage {

	private BorderPane bPane;						//rozvržení okna
	private ArrayList<Kategorie> kategorie;			//seznam kategorií
	private TextArea taPopis;						//místo pro zadání textu otázky
	private TextField tfPocetBodu;					//zadání poètu bodù
	private TextField tfMisto;						//zadání velikosti místa
	private ComboBox cbKategorie;					//zadání kategorie otázky
	private String popis;							//text otázky
	private int pocetBodu;							//poèet bodù
	private int misto;								//velikost místa
	private int idKategorie;						//id kategorie
	private boolean pridanaOtazka = false;			//zda byla zadaná nová otázka

	/**
	 * Konstruktor okna pro pøidání nové otázky
	 * @param kategorie seznam kategorií
	 */
	public OknoPridejOtazku(ArrayList<Kategorie> kategorie){
		super();
		this.kategorie = kategorie;
		bPane = new BorderPane();
		bPane.setCenter(getCenterPane());
		bPane.setBottom(getBottomPane());

		Scene scene = new Scene(bPane, 500, 280);
		this.setScene(scene);
		this.setTitle("Pøidej otázku");
	}

	/**
	 * Nastavení søedního panelu
	 * @return horizontální box (tøída HBox)
	 */
	private HBox getCenterPane(){
		Label lblPopis = new Label("Otázka: ");
		Label lblPocetBodu = new Label("Poèet bodù: ");
		Label lblMisto = new Label("Potøebné místo: ");
		Label lblKategorie = new Label("Kategorie: ");
		lblPopis.setPadding(new Insets(10,10,65,10));

		taPopis = new TextArea();
		taPopis.setPrefSize(350, 100);
		tfPocetBodu = new TextField();
		tfMisto = new TextField();
		cbKategorie = new ComboBox();
		cbKategorie.getItems().addAll(kategorie);

		VBox box1 = new VBox(lblPopis, lblPocetBodu, lblMisto, lblKategorie);
		VBox box2 = new VBox(taPopis, tfPocetBodu, tfMisto, cbKategorie);
		box1.setSpacing(20);
		box2.setSpacing(10);

		HBox box = new HBox(box1, box2);
		box.setPadding(new Insets(10));

		return box;
	}

	/**
	 * Nastavení spodního panelu
	 * @return vertikální box (tøída VBox)
	 */
	private VBox getBottomPane(){
		VBox box = new VBox();
		Button pridej = new Button("Pøidej otázku");
		pridej.setPrefWidth(150);
		pridej.setOnAction(event -> {
			pridejOtazku();
		});

		box.getChildren().add(pridej);
		box.setPadding(new Insets(10));
		box.setAlignment(Pos.BOTTOM_RIGHT);

		return box;
	}

	/**
	 * Nastaví atributy zadanými hodnotami
	 */
	private void pridejOtazku(){
		popis = this.taPopis.getText();
		pocetBodu = Integer.parseInt(this.tfPocetBodu.getText());
		misto = Integer.parseInt(this.tfMisto.getText());
		idKategorie = ((Kategorie)cbKategorie.getValue()).getIdKategorie();
		pridanaOtazka = true;
		this.close();
	}

	/**
	 * Vrací text otázky
	 * @return text otázky
	 */
	public String getPopis(){
		return this.popis;
	}

	/**
	 * Vrací poèet bodù
	 * @return poèet bodù
	 */
	public int getPocetBodu(){
		return this.pocetBodu;
	}

	/**
	 * Vrací velikost místa
	 * @return velikost místa
	 */
	public int getMisto(){
		return this.misto;
	}

	/**
	 * Vrací id kategorie
	 * @return id kategorie
	 */
	public int getIdKategorie(){
		return this.idKategorie;
	}

	/**
	 * Vrací zda byla zadána otázka
	 * @return true pokud byly zadány hodnoty
	 */
	public boolean getPridanaOtazka(){
		return this.pridanaOtazka;
	}
}
