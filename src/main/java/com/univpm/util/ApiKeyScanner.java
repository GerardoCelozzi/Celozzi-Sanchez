package com.univpm.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Classe <b>ApiKeyScanner</b> che rappresenta lo scanner del file txt contenente i dati per poter eseguire le chiamate all'endpoint.
 * @author Gerardo
 * @author Sara
 *
 */
public class ApiKeyScanner {

	private final String pathApiKey = "src/main/resources/apikey.txt";
	private String endpoint;
	private String apikey;
	
	public ApiKeyScanner(String endpoint) {
		this.endpoint = endpoint;
	}
	
	/**
	 * Questo metodo esegue lo scan del file contenente le informazioni e ritorna la chiave che serve per fare le chiamate all'endopoint.
	 * Se la chiave non è presente ritorna una stringa vuota.
	 * @return apikey chiave per fare le request all'end point principale
	 */
	public String readApiKey() {
		String msg = "";
		try {
			Scanner reader = new Scanner(new BufferedReader(new FileReader(pathApiKey)));
			while (reader.hasNextLine()) {
				msg = reader.nextLine();
				String[] dato = msg.split("-");
				String[] endpoint = dato[0].split("=");
				if(endpoint[1].equals(this.endpoint)) {
					String[] apikey = dato[1].split("=");
					this.apikey = apikey[1];
				}
			}
		} 
		catch (Exception e) {
			this.apikey = "";
		}
		return this.apikey;
	}
}
