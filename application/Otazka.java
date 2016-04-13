/**
 * Tøída pro otázku
 * @author Aneta Medunová, Tomáš Zobaè, Michal Všelko
 *
 */
public class Otazka {


	private int id;					//jednoznaèný identifikátor otázky
	private String otazka;			//text otázky
	private int pocetBodu;			//poèet možných bodù
	private int misto;				//velikost místa
	private int idKategorie;		//identifikátor kategorie do které otázka spadá

	/**
	 * Konstruktor otázky
	 * @param id jednoznaèný identifikátor otázky
	 * @param otazka text otázky
	 * @param pocetBodu poèet možných získaných bodù za odpovìï
	 * @param misto velikost místa
	 * @param idKategorie identifikátor kategorie do které otázka spadá
	 */
	public Otazka(int id, String otazka, int pocetBodu, int misto, int idKategorie){
		this.id = id;
		this.otazka = otazka;
		this.pocetBodu = pocetBodu;
		this.misto = misto;
		this.idKategorie = idKategorie;
	}

	/**
	 * Vrací id otázky
	 * @return id
	 */
	public int getId(){
		return this.id;
	}

	/**
	 * Vrací text otázky
	 * @return text otázky
	 */
	public String getOtazka(){
		return this.otazka;
	}

	/**
	 * Vrací poèet bodù za správnou odpovìï
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
	 * @return identifikátor kategorie
	 */
	public int getIdKategorie(){
		return this.idKategorie;
	}
	
	/**
	 * Vrací poèet øádkù potøebných pro text otázky
	 * @return poèet øádkù pro text
	 */
	public int getPocetRadkuText(){
		int mistoText = (this.otazka.length() / 100) + 1;
		return mistoText;
	}

	/**
	 * Vrací poèet øádkù potøebných pro odpovìï
	 * @return poèet øádkù pro odpovìï
	 */
	public int getPocetRadkuMisto(){
		int mistoOdpoved = (this.misto * 10) / 6;
		return mistoOdpoved;
	}

	/**
	 * Nastaví text otázky
	 * @param otazka nový text otázky
	 */
	public void setOtazka(String otazka){
		this.otazka = otazka;
	}

	/**
	 * Nastaví poèet bodù
	 * @param pocetBodu nový poèet bodù
	 */
	public void setPocetBodu(int pocetBodu){
		this.pocetBodu = pocetBodu;
	}

	/**
	 * Nastaví velikost otázky
	 * @param misto nová velikost otázky
 	 */
	public void setMisto(int misto){
		this.misto = misto;
	}

	/**
	 * Nastaví novou kategorii otázky
	 * @param idKategorie nové id kategorie
	 */
	public void setIdKategorie(int idKategorie){
		this.idKategorie = idKategorie;
	}
}
