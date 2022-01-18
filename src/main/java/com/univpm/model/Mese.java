package com.univpm.model;

/**
 * Classe <b>Mese</b> che rappresenta l'oggeto Mese.
 * @author Sara
 *
 */
public class Mese {

	private String data_inizio;
	private String data_fine;
	private String nome_mese;
	
	/**
	 * Costruttore della classe.
	 * @param data_inizio indica la data di inizio mese
	 * @param data_fine indica la data di fine mese
	 * @param nome_mese indica il nome del mese
	 */
	public Mese(String data_inizio, String data_fine, String nome_mese) {
		this.data_inizio = data_inizio;
		this.data_fine = data_fine;
		this.nome_mese = nome_mese;
	}

	public String getData_inizio() {
		return data_inizio;
	}

	public String getData_fine() {
		return data_fine;
	}
	
	public String getNome_mese() {
		return this.nome_mese;
	}
}
