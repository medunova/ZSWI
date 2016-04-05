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
 * T��da okna pro p�id�n� nov� ot�zky
 * @author Aneta Medunov�, Tom� Zoba�, Michal V�elko
 *
 */
public class OknoPridejOtazku extends Stage {

	private BorderPane bPane;						//rozvr�en� okna
	private ArrayList<Kategorie> kategorie;			//seznam kategori�
	private TextArea taPopis;						//m�sto pro zad�n� textu ot�zky
	private TextField tfPocetBodu;					//zad�n� po�tu bod�
	private TextField tfMisto;						//zad�n� velikosti m�sta
	private ComboBox cbKategorie;					//zad�n� kategorie ot�zky
	private String popis;							//text ot�zky
	private int pocetBodu;							//po�et bod�
	private int misto;								//velikost m�sta
	private int idKategorie;						//id kategorie
	private boolean pridanaOtazka = false;			//zda byla zadan� nov� ot�zka

	/**
	 * Konstruktor okna pro p�id�n� nov� ot�zky
	 * @param kategorie seznam kategori�
	 */
	public OknoPridejOtazku(ArrayList<Kategorie> kategorie){
		super();
		this.kategorie = kategorie;
		bPane = new BorderPane();
		bPane.setCenter(getCenterPane());
		bPane.setBottom(getBottomPane());

		Scene scene = new Scene(bPane, 500, 280);
		this.setScene(scene);
		this.setTitle("P�idej ot�zku");
	}

	/**
	 * Nastaven� s�edn�ho panelu
	 * @return horizont�ln� box (t��da HBox)
	 */
	private HBox getCenterPane(){
		Label lblPopis = new Label("Ot�zka: ");
		Label lblPocetBodu = new Label("Po�et bod�: ");
		Label lblMisto = new Label("Pot�ebn� m�sto: ");
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
	 * Nastaven� spodn�ho panelu
	 * @return vertik�ln� box (t��da VBox)
	 */
	private VBox getBottomPane(){
		VBox box = new VBox();
		Button pridej = new Button("P�idej ot�zku");
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
	 * Nastav� atributy zadan�mi hodnotami
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
	 * Vrac� text ot�zky
	 * @return text ot�zky
	 */
	public String getPopis(){
		return this.popis;
	}

	/**
	 * Vrac� po�et bod�
	 * @return po�et bod�
	 */
	public int getPocetBodu(){
		return this.pocetBodu;
	}

	/**
	 * Vrac� velikost m�sta
	 * @return velikost m�sta
	 */
	public int getMisto(){
		return this.misto;
	}

	/**
	 * Vrac� id kategorie
	 * @return id kategorie
	 */
	public int getIdKategorie(){
		return this.idKategorie;
	}

	/**
	 * Vrac� zda byla zad�na ot�zka
	 * @return true pokud byly zad�ny hodnoty
	 */
	public boolean getPridanaOtazka(){
		return this.pridanaOtazka;
	}
}
