import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;

/**
 * T��da hlavn�ho okna editace ot�zek a kategori�
 * @author Aneta Medunov�, Tom� Zoba�, Michal V�elko
 *
 */
public class HlavniOkno extends Stage{

	private ArrayList<Otazka> otazky;				//seznam ot�zek
	private ArrayList<Kategorie> kategorie;			//seznam kategori�
	private ArrayList<Otazka> aktualniOtazky;		//seznam aktu�ln� zobrazen�ch ot�zek
	private TreeView<Object> strom;					//strom kategori� - lev� panel
	private TableView<Otazka> table;				//tabulka zobrazen�ch ot�zek - st�edn� panel
	private BorderPane bPane;						//panel rozvr�en� okna

	/**
	 * Konstruktor t��dy - nastav� panely, velikost okna, titulek a akci po zav�en� okna
	 * @param kategorie	seznam kategori�
	 * @param otazky seznam ot�zek
	 */
	public HlavniOkno(ArrayList<Kategorie> kategorie, ArrayList<Otazka> otazky){
		super();
		this.kategorie = kategorie;
		this.otazky = otazky;
		this.aktualniOtazky = (ArrayList<Otazka>) otazky.clone(); 					//p�i spu�t�n� se zobrazuj� v�echny ot�zky

		bPane = new BorderPane();

		bPane.setLeft(getLeftPane());												//nastav� lev� panel metodou getLeftPane()
		bPane.setCenter(getCenterPane());											//nastav� st�edn� panel metodou getCenterPane()
		bPane.setRight(getRightPane());												//nastav� prav� panel metodou getRightPane()
		bPane.setTop(getTopPane());													//nastav� horn� panel metodou getTopPane()
		bPane.setBottom(getBottomPane());											//nastav� doln� panel metodou getBottomPane()

		Scene scene = new Scene(bPane, 1200, 600);									//velikost okna
		this.setScene(scene);
		scene.getStylesheets().add(HlavniOkno.class.getResource("HlavniOkno.css").toExternalForm());

		this.setTitle("Generov�n� ot�zek");											//nastaven� titulku
		this.show();																//zobrazen� okna
		this.setOnCloseRequest(new EventHandler<WindowEvent>(){						//nastav� akci p�i uzav�en� okna

			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				ulozData();															//p�i zav�en� okna se ulo�� data do soubor� otazky.txt a kategorie.txt
			}

		});
	}

	/**
	 * Vrac� horizont�ln� box pro nastaven� horn�ho panelu
	 * @return horizont�ln� box (t��da HBox)
	 */
	private HBox getTopPane(){
		HBox box = new HBox();

		Font fntNadpis = new Font("Arial", 20);										//font pro nadpis
		Label lblNadpis = new Label("Generov�n� ot�zek");							//vytvoen� nadpisu
		lblNadpis.setFont(fntNadpis);												//nastaven� fontu nadpistu

		box.getChildren().add(lblNadpis);											//vlo�en� nadpisu do boxu
		box.setPadding(new Insets(10));												//nastaven� odsaen� kolem boxu
		box.setAlignment(Pos.BOTTOM_CENTER);										//nastaven� pozice boxu

		return box;
	}

	/**
	 * Vrac� horizont�ln� box pro nastaven� doln�ho panelu
	 * @return horizont�ln� box (t��da HBox)
	 */
	private HBox getBottomPane(){
		HBox box = new HBox();
		Button generovat = new Button("Generovat test");								//vytvo�en� tla��tka pro spu�t�n� generov�n� testu
		generovat.setPrefWidth(150);													//nastaven� velikosti tla��tka
		generovat.setOnAction(new EventHandler<ActionEvent>(){							//nastaven� akce po kliknut� na tla��tko generovat

			@Override
			public void handle(ActionEvent event) {										//spu�t�n� generov�n� testu
				// TODO Auto-generated method stub
				ulozData();
				oknoExportuj export = new oknoExportuj(kategorie, otazky);
				export.initModality(Modality.APPLICATION_MODAL);
				export.showAndWait();
			}

		});

		box.setAlignment(Pos.BOTTOM_RIGHT);
		box.getChildren().add(generovat);
		box.setPadding(new Insets(10));
		return box;
	}

	/**
	 * Vrac� vertik�ln� box pro nastaven� lev�ho panelu
	 * @return vertik�ln� box (t��da VBox)
	 */
	private VBox getLeftPane(){
		VBox box = new VBox();

		nastavStrom();																	//nastaven� strom lev�ho panelu (seznam kategori�) metodou nastavStrom()

		Button pridej = new Button("P�idej kategorii");									//vytvo�� tla��tko pro p�id�n� kategorie
		Button odeber = new Button("Odeber kategorii");									//vytvo�� tla��tko pro odebr�n� kategorie
		odeber.setOnAction(new EventHandler<ActionEvent>(){								//akce pro odebr�n� kategorie

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				odeberKategorii();														//odebere se kategorie
			}

		});

		pridej.setOnAction(new EventHandler<ActionEvent>(){								//akce pro p�id�n� kategori�

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				pridejKategorii();														//p�id� se nov� kategorie
			}

		});

		HBox tlacitka = new HBox(odeber, pridej);
		tlacitka.setPadding(new Insets(15));

		box.getChildren().addAll(strom, tlacitka);
		box.setPadding(new Insets(0, 10, 10, 10));

		return box;

	};

	/**
	 * Vrac� vertik�ln� box pro nastaven� prav�ho panelu
	 * @return vertik�ln� box (t��da VBox)
	 */
	private VBox getRightPane(){
		VBox box = new VBox();

		Button pridej = new Button("P�idej ot�zku");									//vytvo�� tla��tko pro p��d�n� ot�zky
		Button odeber = new Button("Odeber ot�zku");									//vytvo�� tla��tko pro odebr�n� ot�zky
		pridej.setPrefWidth(150);														//nastaven� ���ky tla��tek
		odeber.setPrefWidth(150);

		odeber.setOnAction(new EventHandler<ActionEvent>(){								//nastav� akci pro odebr�n� ot�zky

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				odeberOtazku();															//odebere ot�zku
			}

		});

		pridej.setOnAction(new EventHandler<ActionEvent>(){								//nastav� akci pro p�id�n� ot�zky

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				pridejOtazku();															//p�id�n� ot�zky
			}

		});

		box.getChildren().addAll(odeber, pridej);
		box.setSpacing(10);
		box.setPadding(new Insets(30, 10, 10, 10));
		return box;

	};

	/**
	 * Vrac� tabulku se seznamem ot�zek pro nastaven� centr�ln�ho panelu
	 * @return tabulku se seznamem ot�zek
	 */
	private TableView getCenterPane(){
		table = new TableView();

		TableColumn<Otazka, Integer> id = new TableColumn("Id");											//nastaven� sloupc� tabulky
		TableColumn<Otazka, String> nazev = new TableColumn("Ot�zka");
		TableColumn<Otazka, Integer> pocetBodu = new TableColumn("Po�et bod�");
		TableColumn<Otazka, Integer> misto = new TableColumn("Pot�ebn� m�sto");

		table.getColumns().addAll(id, nazev, pocetBodu, misto);												//vlo�en� sloupc� do tabulky
		table.setItems(FXCollections.observableList(aktualniOtazky));										//vlo�en� dat do tabulky (zobrazuj� se pouze n�kter� ot�zky - proto arraylist aktualniOtazky)


		id.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));					//nastaven� typ� bun�k
		nazev.setCellFactory(TextFieldTableCell.forTableColumn());
		pocetBodu.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		misto.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		id.setCellValueFactory(new PropertyValueFactory("id"));
		nazev.setCellValueFactory(new PropertyValueFactory<>("otazka"));
		pocetBodu.setCellValueFactory(new PropertyValueFactory<>("pocetBodu"));
		misto.setCellValueFactory(new PropertyValueFactory<>("misto"));

		nazev.setOnEditCommit(event -> {																						//nastaven� akce p�i zm�n� hodnoty ve sloupci s textem ot�zky
			((Otazka)event.getTableView().getItems().get(event.getTablePosition().getRow())).setOtazka(event.getNewValue());	//nastav� se nov� text v ot�zce funkc� setOtazka(String otazka)
		});

		pocetBodu.setOnEditCommit(event -> {																					//nastaven� akce p�i zm�n� hodnoty ve sloupci s po�tem bod�
			((Otazka)event.getTableView().getItems().get(event.getTablePosition().getRow())).setPocetBodu(event.getNewValue());
		});

		misto.setOnEditCommit(event -> {																						//nastaven� akce p�i zm�n� hodnoty ve sloupci s velikost� m�sta
			((Otazka)event.getTableView().getItems().get(event.getTablePosition().getRow())).setMisto(event.getNewValue());
		});

		id.setEditable(false);																				//id se nesm� m�nit
		nazev.setPrefWidth(547);																			//nastaven� ���ky textu ot�zky
		table.setEditable(true);																			//mimo sloupce id jsou ostatn� editovateln�

		return table;
	}

	/**
	 * Nastaven� stromu se seznamem kategori� v prav�m panelu
	 */
	private void nastavStrom(){
		TreeItem<Object> seznamKategorii = new TreeItem<Object>("Seznam kategori�");						//vytvo�en� ko�enov�ho uzlu
		seznamKategorii.setExpanded(true);																	//ko�en bude p�i spu�t�n� otev�en�
		for(int i = 0; i < kategorie.size(); i++){															//projde v�echny kategorie v arraylistu a vo�� je do ko�ene
			seznamKategorii.getChildren().add(new TreeItem<Object>(kategorie.get(i)));
		}

		strom = new TreeView<Object>(seznamKategorii);														//vytvo�� nov� strom s vytvo�en�m ko�enem
		strom.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);									//m��e se ozna�it pouze jeden uzel v jednom okam�iku
		naplnData();																						//napln� data tabulky se seznamem ot�zek

		strom.setOnMouseClicked(new EventHandler<MouseEvent>(){												//nastav� akci po kliknut� my�� ve stromu

			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if(event.getClickCount() == 1 || strom.getRoot().equals(strom.getSelectionModel().getSelectedItem()))
					naplnData();																			//klikne-li se jednou nebo se klik� na ko�en stromu, nastav� se seznam ot�zek k dan� kategorii
				else{
					zmenNazevKategorie();																	//klikne-li se v�cekr�t, p�ejde se k editaci n�zvu kategorie
				}
			}

		});
	}

	/**
	 * Napln� tabulku seznamem ot�zek aktu�ln� k zobrazen�
	 */
	private void naplnData(){
		TreeItem<Object> vybranaKategorie = strom.getSelectionModel().getSelectedItem();					//zjist� aktu�ln� vybranou kategorii
		if(vybranaKategorie != null){
			aktualniOtazky.clear();																			//vy�ist� arraylist pro aktu�ln� zobrazen� ot�zky
			if(strom.getRoot().equals(vybranaKategorie)){
				aktualniOtazky = (ArrayList<Otazka>)otazky.clone();											//je-li vybran� ko�en, zobraz� se v�echny ot�zky
			}
			else{
				for(int i = 0; i < otazky.size(); i++){														//nen�-li vybran� ko�en, projdou se v�echny ot�zky a do arraylistu aktualniOtazky se vlo�� ty co maj� stejnou kategorii jako je ta vybran�
					if(otazky.get(i).getIdKategorie() == ((Kategorie)vybranaKategorie.getValue()).getIdKategorie())
						aktualniOtazky.add(otazky.get(i));
				}
			}
		}
		updateCenterPane();																					//znovu se na�te centr�ln� panel pro aktualizaci dat
	}

	/**
	 * Zaktualizuje lev� panel
	 */
	public void updateLeftPane(){
		bPane.setLeft(getLeftPane());
	}

	/**
	 * Zaktualizuje centr�ln� panel
	 */
	public void updateCenterPane(){
		bPane.setCenter(getCenterPane());
	}

	/**
	 * Metoda pro odebr�n� kategorie
	 */
	private void odeberKategorii(){
		if(strom.getSelectionModel().getSelectedIndex() > 0){															//testuje zda se neodeb�r� ko�en stromu
			Kategorie vybranaKategorie = (Kategorie)strom.getSelectionModel().getSelectedItem().getValue();				//zjist� vybranou kategorii

			Alert alert = new Alert(AlertType.CONFIRMATION);															//hl�ka zda se opravdu m� kategorie odebrat
			alert.setTitle("Odebr�n� kategorie");
			alert.setHeaderText("Skute�n� chcete odebrat kategorii " + vybranaKategorie.getNazev());

			ButtonType buttonTypeAno = new ButtonType("Ano");
			ButtonType buttonTypeNe = new ButtonType("Ne");

			alert.getButtonTypes().setAll(buttonTypeNe, buttonTypeAno);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeAno){																			//je-li vybr�no Ano p�ejde se k odebr�n� kategorie
				if(aktualniOtazky.size() == 0){ 																		//kategorie nesm� m�t ulo�en� ot�zky
					for(int i = 0; i < kategorie.size(); i++){															//projdou se kategorie a vybran� se odstran�
						if(kategorie.get(i) == vybranaKategorie){
							kategorie.remove(i);
							break;
						}
					}
					updateLeftPane();																					//zaktualizuje se lev� panel
				}
				else {
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Odebr�n� kategorie");
					alert.setHeaderText("Kategorie obsahuje ot�zky!");
					alert.setContentText("Odeberte z kategorie nejprve ot�zky a pot� ji zkuste odebrat znovu.");
					alert.show();
				}
			}
		}
	}

	/**
	 * P�id� novou kategorii
	 */
	private void pridejKategorii(){
		TextInputDialog dialog = new TextInputDialog();																	//okno pro zad�n� n�zvu nov� kategorie
		dialog.setTitle("P�id�n� kategorie");
		dialog.setHeaderText("Zadejte n�zev kategorie");
		dialog.setContentText("N�zev kategorie: ");

		ButtonType buttonTypeAno = new ButtonType("P�idat kategorii", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().clear();
		dialog.getDialogPane().getButtonTypes().addAll(buttonTypeAno);

		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()){
			Boolean nalezeno = false;
			for(int i = 0; i < kategorie.size(); i++){
				if(kategorie.get(i).getNazev().equals(result.get())){
					nalezeno = true;
					break;
				}
			}
			if(!nalezeno){																								//pokud nen� nalezen stejn� n�zev v jin� kategorii, ulo�� se nov� kategorie
				kategorie.add(new Kategorie(this.getNoveId(kategorie, false) , result.get()));
				updateLeftPane();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("P�id�n� kategorie");
				alert.setHeaderText("Nalezen stejn� n�zev kategorie!");
				alert.setContentText("Kategorie s t�mto n�zvem ji� existuje. Zadejte jin� n�zev kategorie.");
				alert.showAndWait();
				pridejKategorii();
			}
		}
	}

	/**
	 * Editace n�zvu kategorie
	 */
	private void zmenNazevKategorie(){
		TextInputDialog dialog = new TextInputDialog();																	//okno pro nov� n�zev kategorie
		dialog.setTitle("Zm�na n�zvu kategorie");
		dialog.setHeaderText("Zadejte n�zev kategorie");
		dialog.setContentText("N�zev kategorie: ");
		dialog.getEditor().setText(((Kategorie)strom.getSelectionModel().getSelectedItem().getValue()).getNazev());

		ButtonType buttonTypeAno = new ButtonType("Zm�� n�zev", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().clear();
		dialog.getDialogPane().getButtonTypes().addAll(buttonTypeAno);

		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()){
			Boolean nalezeno = false;
			for(int i = 0; i < kategorie.size(); i++){
				if(kategorie.get(i).getNazev().equals(result.get())){
					nalezeno = true;
					break;
				}
			}
			if(!nalezeno){																								//pokud nen� nalezen stejn� n�zev, p�ejde se ke zm�n� n�zvu kategorie
				((Kategorie)strom.getSelectionModel().getSelectedItem().getValue()).setNazev(result.get());
				updateLeftPane();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Zm�na n�zvu kategorie");
				alert.setHeaderText("Nalezen stejn� n�zev kategorie!");
				alert.setContentText("Kategorie s t�mto n�zvem ji� existuje. Zadejte jin� n�zev kategorie.");
				alert.showAndWait();
				zmenNazevKategorie();																					//je-li nelzen stejn� n�zev, vyp�e se chybov� hl�ka a okno se spust� znovu
			}
		}
	}

	/**
	 * Odebere ot�zku ze seznamu ot�zek
	 */
	private void odeberOtazku(){
		Otazka otazka = table.getSelectionModel().getSelectedItem();													//najde vybranou ot�zku
		if(otazka != null){
			Alert alert = new Alert(AlertType.CONFIRMATION);															//hl�ka zda chcete ot�zku odebrat
			alert.setTitle("Odebr�n� ot�zky");
			alert.setHeaderText("Skute�n� chcete odebrat ot�zku " + otazka.getOtazka());

			ButtonType buttonTypeAno = new ButtonType("Ano");
			ButtonType buttonTypeNe = new ButtonType("Ne");

			alert.getButtonTypes().setAll(buttonTypeNe, buttonTypeAno);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeAno){																			//je-li Ano ot�zka se odebere z arraylist� otazky a aktualniOtazky
				otazky.remove(otazka);
				aktualniOtazky.remove(otazka);
				updateCenterPane();																						//zaktualizuje centr�ln� panel
			}
		}
	}

	/**
	 * P�id�n� nov� ot�zky
	 */
	private void pridejOtazku(){
		OknoPridejOtazku pridejOtazku = new OknoPridejOtazku(kategorie);												//spust� okno pro zad�n� �daj� k nov� ot�zce
		pridejOtazku.initModality(Modality.APPLICATION_MODAL);
		pridejOtazku.showAndWait();
		if(pridejOtazku.getPridanaOtazka()){																			//jestli�e byly zad�ny �daje, p�ejde se k p�id�n� ot�zky
			Otazka novaOtazka = new Otazka(this.getNoveId(otazky, true), pridejOtazku.getPopis(), pridejOtazku.getPocetBodu(), pridejOtazku.getMisto(), pridejOtazku.getIdKategorie());
			otazky.add(novaOtazka);
			aktualniOtazky.add(novaOtazka);
			this.updateCenterPane();
		}
	}

	/**
	 * Gener�tor nov�ho id ot�zky nebo id kategorie
	 * @param seznam seznam �daj�
	 * @param jeOtazka true pokud chci id ot�zky, jinak false (id kategorie)
	 * @return nov� id
	 */
	private int getNoveId(ArrayList seznam, Boolean jeOtazka){
		int noveId = 0;

		for(int i = 0; i < seznam.size(); i++){									//projde v�echny polo�ky seznamu a najde maxim�ln� id
			int id = 0;
			if(jeOtazka)
				id = ((Otazka)seznam.get(i)).getId();
			else
				id = ((Kategorie)seznam.get(i)).getIdKategorie();

			if(id > noveId) noveId = id;
		}

		return ++noveId;
	}

	/**
	 * Ulo�� data do soubor� kategorie.txt a otazky.txt
	 */
	private void ulozData(){
		try{
			BufferedWriter soubor = new BufferedWriter(new FileWriter("kategorie.txt"));

			for(int i = 0; i < kategorie.size(); i++){												//projde kategorie a ulo�� je
				if(i != 0) soubor.newLine();
				Kategorie pomKat = kategorie.get(i);
				soubor.write(pomKat.getIdKategorie() + " " + pomKat.getNazev());
			}
			soubor.flush();
			soubor.close();

			soubor = new BufferedWriter(new FileWriter("otazky.txt"));

			for(int i = 0; i < otazky.size(); i++){													//projde ot�zky a ulo�� je
				if(i != 0) soubor.newLine();
				Otazka pomOtazka = otazky.get(i);
				soubor.write(pomOtazka.getId() + " " + pomOtazka.getPocetBodu() + " " + pomOtazka.getMisto() + " " + pomOtazka.getIdKategorie() + " " + pomOtazka.getOtazka());
			}
			soubor.flush();
			soubor.close();


		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
