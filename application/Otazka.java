/**
 * T��da pro ot�zku
 * @author Aneta Medunov�, Tom� Zoba�, Michal V�elko
 *
 */
public class Otazka {


	private int id;					//jednozna�n� identifik�tor ot�zky
	private String otazka;			//text ot�zky
	private int pocetBodu;			//po�et mo�n�ch bod�
	private int misto;				//velikost m�sta
	private int idKategorie;		//identifik�tor kategorie do kter� ot�zka spad�

	/**
	 * Konstruktor ot�zky
	 * @param id jednozna�n� identifik�tor ot�zky
	 * @param otazka text ot�zky
	 * @param pocetBodu po�et mo�n�ch z�skan�ch bod� za odpov��
	 * @param misto velikost m�sta
	 * @param idKategorie identifik�tor kategorie do kter� ot�zka spad�
	 */
	public Otazka(int id, String otazka, int pocetBodu, int misto, int idKategorie){
		this.id = id;
		this.otazka = otazka;
		this.pocetBodu = pocetBodu;
		this.misto = misto;
		this.idKategorie = idKategorie;
	}

	/**
	 * Vrac� id ot�zky
	 * @return id
	 */
	public int getId(){
		return this.id;
	}

	/**
	 * Vrac� text ot�zky
	 * @return text ot�zky
	 */
	public String getOtazka(){
		return this.otazka;
	}

	/**
	 * Vrac� po�et bod� za spr�vnou odpov��
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
	 * @return identifik�tor kategorie
	 */
	public int getIdKategorie(){
		return this.idKategorie;
	}
	
	/**
	 * Vrac� po�et ��dk� pot�ebn�ch pro text ot�zky
	 * @return po�et ��dk� pro text
	 */
	public int getPocetRadkuText(){
		int mistoText = (this.otazka.length() / 100) + 1;
		return mistoText;
	}

	/**
	 * Vrac� po�et ��dk� pot�ebn�ch pro odpov��
	 * @return po�et ��dk� pro odpov��
	 */
	public int getPocetRadkuMisto(){
		int mistoOdpoved = (this.misto * 10) / 6;
		return mistoOdpoved;
	}

	/**
	 * Nastav� text ot�zky
	 * @param otazka nov� text ot�zky
	 */
	public void setOtazka(String otazka){
		this.otazka = otazka;
	}

	/**
	 * Nastav� po�et bod�
	 * @param pocetBodu nov� po�et bod�
	 */
	public void setPocetBodu(int pocetBodu){
		this.pocetBodu = pocetBodu;
	}

	/**
	 * Nastav� velikost ot�zky
	 * @param misto nov� velikost ot�zky
 	 */
	public void setMisto(int misto){
		this.misto = misto;
	}

	/**
	 * Nastav� novou kategorii ot�zky
	 * @param idKategorie nov� id kategorie
	 */
	public void setIdKategorie(int idKategorie){
		this.idKategorie = idKategorie;
	}
}
