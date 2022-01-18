package com.univpm.model;

/**
 * Classe <b>Stato</b> rappresenta un generico stato.
 * @author Sara
 *
 */
public class Stato {

	private String name;
	private String code;
	
	/**
	 * Costruttore della classe.
	 * @param name indica il nome dello stato
	 * @param code indica il codice dello stato
	 */
	public Stato(String name, String code) {
		 this.name = name;
		 this.code = code;
	 }

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
}
