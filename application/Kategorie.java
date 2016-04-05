package application;

/**
 * T��da pro kategorie
 * @author Aneta Medunov�, Tom� Zoba�, Michal V�elko
 *
 */
public class Kategorie {
	
	private int idKategorie;	//jednozna�n� identifik�tor kategorie
	private String nazev;		//n�zev kategorie
	
	/**
	 * Konstruktor t��dy Kategorie, nastav� idKategorie a nazev
	 * @param idKategorie id kategorie
	 * @param nazev n�zev kategorie
	 */
	public Kategorie(int idKategorie, String nazev){
		this.idKategorie = idKategorie;
		this.nazev = nazev;
	}
	
	/**
	 * Vrac� jednozna�n� identifik�tor kategorie
	 * @return id kategorie
	 */
	public int getIdKategorie(){
		return this.idKategorie;
	}
	
	/**
	 * Vrac� n�zev kategorie
	 * @return nazev kategorie
	 */
	public String getNazev(){
		return this.nazev;
	}
	
	/**
	 * Nastav� nov� n�zev kategorie
	 * @param nazev nov� n�zev kategorie
	 */
	public void setNazev(String nazev){
		this.nazev = nazev;
	}
	
	/**
	 * p�evede instanci na textov� �et�zec
	 */
	public String toString(){
		return this.nazev;
	}
}
