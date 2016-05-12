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
import javafx.scene.control.ChoiceDialog;
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
 * Třída hlavního okna editace otázek a kategorií
 * @author Aneta Medunová, Tomáš Zobač, Michal Všelko
 *
 */
public class HlavniOkno extends Stage{

	private ArrayList<Otazka> otazky;				//seznam otázek
	private ArrayList<Kategorie> kategorie;			//seznam kategorií
	private ArrayList<Otazka> aktualniOtazky;		//seznam aktuálně zobrazených otázek
	private TreeView<Object> strom;					//strom kategorií - levý panel
	private TableView<Otazka> table;				//tabulka zobrazených otázek - střední panel
	private BorderPane bPane;						//panel rozvržení okna

	/**
	 * Konstruktor třídy - nastaví panely, velikost okna, titulek a akci po zavření okna
	 * @param kategorie	seznam kategorií
	 * @param otazky seznam otázek
	 */
	public HlavniOkno(ArrayList<Kategorie> kategorie, ArrayList<Otazka> otazky){
		super();
		this.kategorie = kategorie;
		this.otazky = otazky;
		this.aktualniOtazky = (ArrayList<Otazka>) otazky.clone(); 					//při spuštění se zobrazují všechny otázky

		bPane = new BorderPane();

		bPane.setLeft(getLeftPane());												//nastaví levý panel metodou getLeftPane()
		bPane.setCenter(getCenterPane());											//nastaví střední panel metodou getCenterPane()
		bPane.setRight(getRightPane());												//nastaví pravý panel metodou getRightPane()
		bPane.setTop(getTopPane());													//nastaví horní panel metodou getTopPane()
		bPane.setBottom(getBottomPane());											//nastaví dolní panel metodou getBottomPane()

		Scene scene = new Scene(bPane, 1250, 600);									//velikost okna
		this.setScene(scene);
		scene.getStylesheets().add(HlavniOkno.class.getResource("vzhled.css").toExternalForm());

		this.setTitle("Generování otázek");											//nastavení titulku
		this.show();																//zobrazení okna
		this.setOnCloseRequest(new EventHandler<WindowEvent>(){						//nastaví akci při uzavření okna

			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				ulozData();															//při zavření okna se uloží data do souborů otazky.txt a kategorie.txt
			}

		});
	}

	/**
	 * Vrací horizontální box pro nastavení horního panelu
	 * @return horizontální box (třída HBox)
	 */
	private HBox getTopPane(){
		HBox box = new HBox();

		Font fntNadpis = new Font("Arial", 20);										//font pro nadpis
		Label lblNadpis = new Label("Generování otázek");							//vytvoení nadpisu
		lblNadpis.setFont(fntNadpis);												//nastavení fontu nadpistu

		box.getChildren().add(lblNadpis);											//vložení nadpisu do boxu
		box.setPadding(new Insets(10));												//nastavení odsaení kolem boxu
		box.setAlignment(Pos.BOTTOM_CENTER);										//nastavení pozice boxu

		return box;
	}

	/**
	 * Vrací horizontální box pro nastavení dolního panelu
	 * @return horizontální box (třída HBox)
	 */
	private HBox getBottomPane(){
		HBox box = new HBox();
		Button generovat = new Button("Generovat test");								//vytvoření tlačítka pro spuštění generování testu
		generovat.setId("tisk");
		generovat.setPrefWidth(150);													//nastavení velikosti tlačítka
		generovat.setOnAction(new EventHandler<ActionEvent>(){							//nastavení akce po kliknutí na tlačítko generovat

			@Override
			public void handle(ActionEvent event) {										//spuštění generování testu
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
	 * Vrací vertikální box pro nastavení levého panelu
	 * @return vertikální box (třída VBox)
	 */
	private VBox getLeftPane(){
		VBox box = new VBox();

		nastavStrom();																	//nastavení strom levého panelu (seznam kategorií) metodou nastavStrom()

		Button pridej = new Button("Přidej kategorii");									//vytvoří tlačítko pro přidání kategorie
		Button odeber = new Button("Odeber kategorii");									//vytvoří tlačítko pro odebrání kategorie
		odeber.setId("tisk");
		odeber.setOnAction(new EventHandler<ActionEvent>(){								//akce pro odebrání kategorie

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				odeberKategorii();														//odebere se kategorie
			}

		});

		pridej.setId("tisk");
		pridej.setOnAction(new EventHandler<ActionEvent>(){								//akce pro přidání kategorií

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				pridejKategorii();														//přidá se nová kategorie
			}

		});

		HBox tlacitka = new HBox(odeber, pridej);
		tlacitka.setPadding(new Insets(15));

		box.getChildren().addAll(strom, tlacitka);
		box.setPadding(new Insets(0, 10, 10, 10));

		return box;

	};

	/**
	 * Vrací vertikální box pro nastavení pravého panelu
	 * @return vertikální box (třída VBox)
	 */
	private VBox getRightPane(){
		VBox box = new VBox();

		Button pridej = new Button("Přidej otázku");									//vytvoří tlačítko pro přídání otázky
		Button odeber = new Button("Odeber otázku");									//vytvoří tlačítko pro odebrání otázky
		Button zmenKategorii = new Button("Změň kategorii");									//vytvoří tlačítko pro odebrání otázky
		pridej.setPrefWidth(150);														//nastavení šířky tlačítek
		pridej.setId("tisk");
		odeber.setPrefWidth(150);														//vzhled z *.css souboru
		odeber.setId("tisk");
		zmenKategorii.setPrefWidth(150);														
		zmenKategorii.setId("tisk");
			
		zmenKategorii.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Otazka otazka = table.getSelectionModel().getSelectedItem();													//najde vybranou otázku
				if(otazka != null){
					ChoiceDialog<Kategorie> zmenaKategorie = new ChoiceDialog<>(kategorie.get(0), kategorie);
					zmenaKategorie.setTitle("Změna kategorie.");
					zmenaKategorie.setHeaderText("Vyberte novou kategorii");
					zmenaKategorie.setContentText("Nová kategorie:");
					Optional<Kategorie> result = zmenaKategorie.showAndWait();
					if(result.isPresent()){
						otazka.setIdKategorie(result.get().getIdKategorie());
						updateLeftPane();
					}
				}
			}
			
		});
		
		odeber.setOnAction(new EventHandler<ActionEvent>(){								//nastaví akci pro odebrání otázky

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				odeberOtazku();															//odebere otázku
			}

		});

		pridej.setOnAction(new EventHandler<ActionEvent>(){								//nastaví akci pro přidání otázky

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				pridejOtazku();															//přidání otázky
			}

		});

		box.getChildren().addAll(odeber, pridej, zmenKategorii);
		box.setSpacing(10);
		box.setPadding(new Insets(30, 10, 10, 10));
		return box;

	};

	/**
	 * Vrací tabulku se seznamem otázek pro nastavení centrálního panelu
	 * @return tabulku se seznamem otázek
	 */
	private TableView getCenterPane(){
		table = new TableView();

		TableColumn<Otazka, Integer> id = new TableColumn("Id");											//nastavení sloupců tabulky
		TableColumn<Otazka, String> nazev = new TableColumn("Otázka");
		TableColumn<Otazka, Integer> pocetBodu = new TableColumn("Počet bodů");
		TableColumn<Otazka, Integer> misto = new TableColumn("Potřebné místo");

		table.getColumns().addAll(id, nazev, pocetBodu, misto);												//vložení sloupců do tabulky
		table.setItems(FXCollections.observableList(aktualniOtazky));										//vložení dat do tabulky (zobrazují se pouze některé otázky - proto arraylist aktualniOtazky)


		id.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));					//nastavení typů buněk
		nazev.setCellFactory(TextFieldTableCell.forTableColumn());
		pocetBodu.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		misto.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		id.setCellValueFactory(new PropertyValueFactory("id"));
		nazev.setCellValueFactory(new PropertyValueFactory<>("otazka"));
		pocetBodu.setCellValueFactory(new PropertyValueFactory<>("pocetBodu"));
		misto.setCellValueFactory(new PropertyValueFactory<>("misto"));

		nazev.setOnEditCommit(event -> {																						//nastavení akce při změně hodnoty ve sloupci s textem otázky
			((Otazka)event.getTableView().getItems().get(event.getTablePosition().getRow())).setOtazka(event.getNewValue());	//nastaví se nový text v otázce funkcí setOtazka(String otazka)
		});

		pocetBodu.setOnEditCommit(event -> {																					//nastavení akce při změně hodnoty ve sloupci s počtem bodů
			((Otazka)event.getTableView().getItems().get(event.getTablePosition().getRow())).setPocetBodu(event.getNewValue());
		});

		misto.setOnEditCommit(event -> {																						//nastavení akce při změně hodnoty ve sloupci s velikostí místa
			((Otazka)event.getTableView().getItems().get(event.getTablePosition().getRow())).setMisto(event.getNewValue());
		});

		id.setEditable(false);																				//id se nesmí měnit
		nazev.setPrefWidth(547);																			//nastavení šířky textu otázky
		table.setEditable(true);																			//mimo sloupce id jsou ostatní editovatelné

		return table;
	}

	/**
	 * Nastavení stromu se seznamem kategorií v pravém panelu
	 */
	private void nastavStrom(){
		TreeItem<Object> seznamKategorii = new TreeItem<Object>("Seznam kategorií");						//vytvoření kořenového uzlu
		seznamKategorii.setExpanded(true);																	//kořen bude při spuštění otevřený
		for(int i = 0; i < kategorie.size(); i++){															//projde všechny kategorie v arraylistu a voží je do kořene
			seznamKategorii.getChildren().add(new TreeItem<Object>(kategorie.get(i)));
		}

		strom = new TreeView<Object>(seznamKategorii);														//vytvoří nový strom s vytvořeným kořenem
		strom.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);									//může se označit pouze jeden uzel v jednom okamžiku
		naplnData();																						//naplní data tabulky se seznamem otázek

		strom.setOnMouseClicked(new EventHandler<MouseEvent>(){												//nastaví akci po kliknutí myší ve stromu

			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if(event.getClickCount() == 1 || strom.getRoot().equals(strom.getSelectionModel().getSelectedItem()))
					naplnData();																			//klikne-li se jednou nebo se kliká na kořen stromu, nastaví se seznam otázek k dané kategorii
				else{
					zmenNazevKategorie();																	//klikne-li se vícekrát, přejde se k editaci názvu kategorie
				}
			}

		});
	}

	/**
	 * Naplní tabulku seznamem otázek aktuálně k zobrazení
	 */
	private void naplnData(){
		TreeItem<Object> vybranaKategorie = strom.getSelectionModel().getSelectedItem();					//zjistí aktuálně vybranou kategorii
		if(vybranaKategorie != null){
			aktualniOtazky.clear();																			//vyčistí arraylist pro aktuálně zobrazené otázky
			if(strom.getRoot().equals(vybranaKategorie)){
				aktualniOtazky = (ArrayList<Otazka>)otazky.clone();											//je-li vybraný kořen, zobrazí se všechny otázky
			}
			else{
				for(int i = 0; i < otazky.size(); i++){														//není-li vybraný kořen, projdou se všechny otázky a do arraylistu aktualniOtazky se vloží ty co mají stejnou kategorii jako je ta vybraná
					if(otazky.get(i).getIdKategorie() == ((Kategorie)vybranaKategorie.getValue()).getIdKategorie())
						aktualniOtazky.add(otazky.get(i));
				}
			}
		}
		updateCenterPane();																					//znovu se načte centrální panel pro aktualizaci dat
	}

	/**
	 * Zaktualizuje levý panel
	 */
	public void updateLeftPane(){
		bPane.setLeft(getLeftPane());
	}

	/**
	 * Zaktualizuje centrální panel
	 */
	public void updateCenterPane(){
		bPane.setCenter(getCenterPane());
	}

	/**
	 * Metoda pro odebrání kategorie
	 */
	private void odeberKategorii(){
		if(strom.getSelectionModel().getSelectedIndex() > 0){															//testuje zda se neodebírá kořen stromu
			Kategorie vybranaKategorie = (Kategorie)strom.getSelectionModel().getSelectedItem().getValue();				//zjistí vybranou kategorii

			Alert alert = new Alert(AlertType.CONFIRMATION);															//hláška zda se opravdu má kategorie odebrat
			alert.setTitle("Odebrání kategorie");
			alert.setHeaderText("Skutečně chcete odebrat kategorii " + vybranaKategorie.getNazev());

			ButtonType buttonTypeAno = new ButtonType("Ano");
			ButtonType buttonTypeNe = new ButtonType("Ne");

			alert.getButtonTypes().setAll(buttonTypeNe, buttonTypeAno);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeAno){																			//je-li vybráno Ano přejde se k odebrání kategorie
				if(aktualniOtazky.size() == 0){ 																		//kategorie nesmí mít uložené otázky
					for(int i = 0; i < kategorie.size(); i++){															//projdou se kategorie a vybraná se odstraní
						if(kategorie.get(i) == vybranaKategorie){
							kategorie.remove(i);
							break;
						}
					}
					updateLeftPane();																					//zaktualizuje se levý panel
				}
				else {
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Odebrání kategorie");
					alert.setHeaderText("Kategorie obsahuje otázky!");
					alert.setContentText("Odeberte z kategorie nejprve otázky a poté ji zkuste odebrat znovu.");
					alert.show();
				}
			}
		}
	}

	/**
	 * Přidá novou kategorii
	 */
	private void pridejKategorii(){
		TextInputDialog dialog = new TextInputDialog();																	//okno pro zadání názvu nové kategorie
		dialog.setTitle("Přidání kategorie");
		dialog.setHeaderText("Zadejte název kategorie");
		dialog.setContentText("Název kategorie: ");

		ButtonType buttonTypeAno = new ButtonType("Přidat kategorii", ButtonData.OK_DONE);
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
			if(!nalezeno){																								//pokud není nalezen stejný název v jiné kategorii, uloží se nová kategorie
				kategorie.add(new Kategorie(this.getNoveId(kategorie, false) , result.get()));
				updateLeftPane();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Přidání kategorie");
				alert.setHeaderText("Nalezen stejný název kategorie!");
				alert.setContentText("Kategorie s tímto názvem již existuje. Zadejte jiný název kategorie.");
				alert.showAndWait();
				pridejKategorii();
			}
		}
	}

	/**
	 * Editace názvu kategorie
	 */
	private void zmenNazevKategorie(){
		TextInputDialog dialog = new TextInputDialog();																	//okno pro nový název kategorie
		dialog.setTitle("Změna názvu kategorie");
		dialog.setHeaderText("Zadejte název kategorie");
		dialog.setContentText("Název kategorie: ");
		dialog.getEditor().setText(((Kategorie)strom.getSelectionModel().getSelectedItem().getValue()).getNazev());

		ButtonType buttonTypeAno = new ButtonType("Změň název", ButtonData.OK_DONE);
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
			if(!nalezeno){																								//pokud není nalezen stejný název, přejde se ke změně názvu kategorie
				((Kategorie)strom.getSelectionModel().getSelectedItem().getValue()).setNazev(result.get());
				updateLeftPane();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Změna názvu kategorie");
				alert.setHeaderText("Nalezen stejný název kategorie!");
				alert.setContentText("Kategorie s tímto názvem již existuje. Zadejte jiný název kategorie.");
				alert.showAndWait();
				zmenNazevKategorie();																					//je-li nelzen stejný název, vypíše se chybová hláška a okno se spustí znovu
			}
		}
	}

	/**
	 * Odebere otázku ze seznamu otázek
	 */
	private void odeberOtazku(){
		Otazka otazka = table.getSelectionModel().getSelectedItem();													//najde vybranou otázku
		if(otazka != null){
			Alert alert = new Alert(AlertType.CONFIRMATION);															//hláška zda chcete otázku odebrat
			alert.setTitle("Odebrání otázky");
			alert.setHeaderText("Skutečně chcete odebrat otázku " + otazka.getOtazka());

			ButtonType buttonTypeAno = new ButtonType("Ano");
			ButtonType buttonTypeNe = new ButtonType("Ne");

			alert.getButtonTypes().setAll(buttonTypeNe, buttonTypeAno);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeAno){																			//je-li Ano otázka se odebere z arraylistů otazky a aktualniOtazky
				otazky.remove(otazka);
				aktualniOtazky.remove(otazka);
				updateCenterPane();																						//zaktualizuje centrální panel
			}
		}
	}

	/**
	 * Přidání nové otázky
	 */
	private void pridejOtazku(){
		OknoPridejOtazku pridejOtazku = new OknoPridejOtazku(kategorie);												//spustí okno pro zadání údajů k nové otázce
		pridejOtazku.initModality(Modality.APPLICATION_MODAL);
		pridejOtazku.showAndWait();
		if(pridejOtazku.getPridanaOtazka()){																			//jestliže byly zadány údaje, přejde se k přidání otázky
			Otazka novaOtazka = new Otazka(this.getNoveId(otazky, true), pridejOtazku.getPopis(), pridejOtazku.getPocetBodu(), pridejOtazku.getMisto(), pridejOtazku.getIdKategorie());
			otazky.add(novaOtazka);
			aktualniOtazky.add(novaOtazka);
			this.updateCenterPane();
		}
	}

	/**
	 * Generátor nového id otázky nebo id kategorie
	 * @param seznam seznam údajů
	 * @param jeOtazka true pokud chci id otázky, jinak false (id kategorie)
	 * @return nové id
	 */
	private int getNoveId(ArrayList seznam, Boolean jeOtazka){
		int noveId = 0;

		for(int i = 0; i < seznam.size(); i++){									//projde všechny položky seznamu a najde maximální id
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
	 * Uloží data do souborů kategorie.txt a otazky.txt
	 */
	private void ulozData(){
		try{
			BufferedWriter soubor = new BufferedWriter(new FileWriter("data/kategorie.txt"));

			for(int i = 0; i < kategorie.size(); i++){												//projde kategorie a uloží je
				if(i != 0) soubor.newLine();
				Kategorie pomKat = kategorie.get(i);
				soubor.write(pomKat.getIdKategorie() + " " + pomKat.getNazev().trim());
			}
			soubor.flush();
			soubor.close();

			soubor = new BufferedWriter(new FileWriter("data/otazky.txt"));

			for(int i = 0; i < otazky.size(); i++){													//projde otázky a uloží je
				if(i != 0) soubor.newLine();
				Otazka pomOtazka = otazky.get(i);
				soubor.write(pomOtazka.getId() + " " + pomOtazka.getPocetBodu() + " " + pomOtazka.getMisto() + " " + pomOtazka.getIdKategorie() + " " + pomOtazka.getOtazka().trim());
			}
			soubor.flush();
			soubor.close();


		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
