package com.univpm.model;

import java.util.ArrayList;

/**
 * Classe che rappresenta una nazione.
 * @author Gerardo
 *
 */
public class Country {

	private String name;
	private String countryCode;
	
	private ArrayList<Stato> stati = new ArrayList<Stato>();
	
	/**
	 * Costruttore della classe.
	 * @param countrycode indica il codice della nazione
	 * @see Stato
	 */
	public Country (String countrycode) {

		this.countryCode = countrycode;
		if(countrycode.equals("CA") || countryCode.equals("ca")) {
			Stato alberta=new Stato("Alberta","AB");
			stati.add(alberta);
			Stato british_Columbia=new Stato("British_Columbia","BC");
			stati.add(british_Columbia);
			Stato manitoba=new Stato("Manitoba","MB");
			stati.add(manitoba);
			Stato brunswick=new Stato("Brunswick,","NB");
			stati.add(brunswick);
			Stato newfoundland_and_Labrador=new Stato("Newfoundland and Labrador","NL");
			stati.add(newfoundland_and_Labrador);
			Stato northwest_Territories=new Stato("Northwest Territories","NT");
			stati.add(northwest_Territories);
			Stato nova_Scotia=new Stato("Nova Scotia","NS");
			stati.add(nova_Scotia);
			Stato nunavut=new Stato("Nunavut","NU");
			stati.add(nunavut);
			Stato ontario=new Stato("Ontario","ON");
			stati.add(ontario);
			Stato prince_Edward_Island=new Stato("Prince Edward Island","PE");
			stati.add(prince_Edward_Island);
			Stato quebec=new Stato("Quebec","QC");
			stati.add(quebec);
			Stato saskatchewan=new Stato("Saskatchewan","SK");
			stati.add(saskatchewan);
			Stato yukon=new Stato("Yukon","YT");
			stati.add(yukon);
		}
	}

	public String getName() {
		return name;
	}

	public String getCountryCode() {
		return countryCode;
	}
	
	public ArrayList<String> getDataString() {
		ArrayList<String> data_string = new ArrayList<String>();
		for (Stato s : this.stati) {
			data_string.add(s.getCode());
		}
		return data_string;
	}

	public ArrayList<Stato> getStati() {
		return stati;
	}
	
}
