package com.univpm.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.univpm.service.I_Stats;


/**
 * <p> <b>RestController</b> dell'applicazione <br>
 * Questa classe <b>RequestController</b> permette all'utente di visualizzare delle statistiche relative a gli eventi organizzati nel Canada,
 * attravero dei filtri che l'utente stesso puo' scegliere. </p>
 * 
 * @author Gerardo
 * @author Sara
 */

@RestController
public class RequestController {

	
	JSONObject response;
	JSONArray arr_response;
	
	@Autowired
	@Qualifier("setupModel&response")
	private I_Stats statsWithresponse;
	
	/**
	 * Questo metodo permette all'utente di visualizzare il numero di eventi che si svolgono nella nazione passata come parametro.
	 * @param countryCode indica il codice del paese
	 * @return <code> JSONObject </code> 
	 */
	@RequestMapping("/country/{countryCode}")
	public  JSONObject getEventsForCountry(@PathVariable String countryCode) {
		response=statsWithresponse.getNumberOfEvents(countryCode,"0","0","0","0"); ; 
		return response; 
	}
	
	/**
	 * Questo metodo permette all'utente di visualizzare le statistiche relative ad una nazione. 
	 * Ovvero mostra il paese relativo o i paesi relativi che hanno il numero minimo di eventi e analogamente il paese o i paesi che hanno il numero massimo di eventi,
	 * il numero degli eventi del paese e per ultimo la media degli eventi della nazione.
	 * 
	 * @param countryCode indica la nazione
	 * @param nameCat indica la classificazione o le classificazioni
	 * @return <code>JSONObject</code>
	 */
	@RequestMapping("/stats/country/{countryCode}")
	public  JSONObject getStatsEventsForCountry(@PathVariable String countryCode,String nameCat) {
		response = statsWithresponse.getstatsNumberOfEvents(countryCode, "0","0"); ; 
		return response; 
	}
	
	/**
	 * Questo metodo permette all'utente di visualizzare le statistiche relative alla classificazione o classificazioni passate come parametro nel path.
	 * In particolare mostra lo stato o gli stati che organizzano il numero massimo di eventi relativi alla classificazione,
	 * analogamente per il minimo numero di eventi, mostra il totale degli eventi relativi alla classificazione.
	 * 
	 * @param countryCode indica la nazione
	 * @param nameCat indica la classificazione o classificazioni
	 * @return <code>JSONObject</code>
	 */
	@RequestMapping("/stats/country/{countryCode}/classification/{nameCat}")
	public  JSONObject getStatsEventsForCountryForCat(@PathVariable String countryCode,@PathVariable String nameCat) {
		response=statsWithresponse.getstatsNumberOfEvents(countryCode,"0", nameCat);
		return response; 
	}

	/**
	 * Questo metodo permette all'utente di visualizzare il numero di eventi dello stato o degli stati passati come parametro.
	 * 
	 * @param countryCode indica la nazione
	 * @param stateCode indica lo stato o gli stati
	 * @return <code>JSONObject</code>
	 */
	@RequestMapping("/country/{countryCode}/state/{stateCode}")
	public  JSONObject getEventsForStateOfCountry(@PathVariable String countryCode,@PathVariable String stateCode) {
		response=statsWithresponse.getNumberOfEvents(countryCode,stateCode,"0","0","0"); ; 	
		return response; 
	}

	/**
	 * Questo metodo permette all'utente di visualizzare il numero di eventi della classificazione o delle classificazioni passate come parametro
	 * relative allo stato o a gli stati passati come parametro.
	 * 
	 * @param countryCode indica la nazione
	 * @param stateCode indica lo stato o gli stati
	 * @param nameClass indica la classificazione o le classificazioni
	 * @return <code>JSONObject</code>
	 */
	@RequestMapping("/country/{countryCode}/state/{stateCode}/classification/{nameClass}")
	public  JSONObject getNumEventsWithClass(@PathVariable String countryCode,@PathVariable String stateCode,@PathVariable String nameClass) {	
		response= statsWithresponse.getNumberOfEvents(countryCode,stateCode,nameClass,"0","0"); 	
		return response; 
	}
	
	/**
	 * Questo metodo permette all'utente di visualizzare le statistiche relative ad un certo periodo, 
	 * relative ad una o piu' classificazioni e relative ad uno o piu' stati.
	 * 
	 * @param countryCode indica la nazione
	 * @param stateCode indica lo stato o gli stati
	 * @param nameClass indica la classificazione o le classificazioni
	 * @param start indica la data di inizio del periodo
	 * @param end indica la data di fine del periodo
	 * @return <code>JSONObject</code>
	 */
	@RequestMapping("/country/{countryCode}/state/{stateCode}/classification/{nameClass}/startdate/{start}/enddate/{end}")
	public  JSONObject getNumEventsByStateClassPeriod(@PathVariable String countryCode,@PathVariable String stateCode,
			@PathVariable String nameClass,@PathVariable String start,@PathVariable String end) {	
		response= statsWithresponse.getNumberOfEvents(countryCode,stateCode,nameClass,start,end); 	
		return response;	
	}
	
	/**
	 * Questo metodo permette all'utente di visualizzare le classificazioni sulle quali si possono effettuare le statistiche.
	 * 
	 * @return <code>JSONArray</code>
	 * @throws ParseException
	 */
	@RequestMapping("/events/classification")
	public  JSONArray getClassification() throws ParseException {
		arr_response=statsWithresponse.getClassificationEvents(); 
		return arr_response; 
	}
	
	/**
	 * Questo metodo permette all'utente di visualizzare le statistiche annuali relative ad uno stato e ad un anno,
	 * entrambi passati come parametri nel path.
	 * 
	 * @param countryCode indica la nazine
	 * @param stateCode indica lo stato
	 * @param anno indica l'anno
	 * @return <code>JSONObject</code>
	 */
	@RequestMapping("/stats/country/{countryCode}/state/{stateCode}/statsmensili/{anno}")
	public JSONObject getStatsMensiliOfState(@PathVariable String countryCode, @PathVariable String stateCode, @PathVariable String anno) {
		response = statsWithresponse.getStatsAnnuali(countryCode, stateCode, anno);
		return response;
	}

}
