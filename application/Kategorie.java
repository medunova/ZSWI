package application;

/**
 * Tøída pro kategorie
 * @author Aneta Medunová, Tomáš Zobaè, Michal Všelko
 *
 */
public class Kategorie {
	
	private int idKategorie;	//jednoznaèný identifikátor kategorie
	private String nazev;		//název kategorie
	
	/**
	 * Konstruktor tøídy Kategorie, nastaví idKategorie a nazev
	 * @param idKategorie id kategorie
	 * @param nazev název kategorie
	 */
	public Kategorie(int idKategorie, String nazev){
		this.idKategorie = idKategorie;
		this.nazev = nazev;
	}
	
	/**
	 * Vrací jednoznaèný identifikátor kategorie
	 * @return id kategorie
	 */
	public int getIdKategorie(){
		return this.idKategorie;
	}
	
	/**
	 * Vrací název kategorie
	 * @return nazev kategorie
	 */
	public String getNazev(){
		return this.nazev;
	}
	
	/**
	 * Nastaví nový název kategorie
	 * @param nazev nový název kategorie
	 */
	public void setNazev(String nazev){
		this.nazev = nazev;
	}
	
	/**
	 * pøevede instanci na textový øetìzec
	 */
	public String toString(){
		return this.nazev;
	}
}
