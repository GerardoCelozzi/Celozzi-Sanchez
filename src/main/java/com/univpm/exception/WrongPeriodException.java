package com.univpm.exception;

/**
 * Classe che rappresenta l'eccezione lanciata nella costruzione dell'oggetto <b>Periodo</b>.
 * @author Sara
 * @see Periodo
 */
public class WrongPeriodException extends Exception {

	private static final long serialVersionUID = 1L;
	private String msg;
	
	public WrongPeriodException(String msg) {
		this.msg = msg;
	}
	
	public String toString() {
		return this.msg;
	}
}
