package com.univpm.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.univpm.exception.WrongPeriodException;

public interface I_Stats {

	JSONArray getClassificationEvents() throws ParseException;

	JSONObject getstatsNumberOfEvents(String countryCode, String stateCode, String nameCat);

	JSONObject getStatsAnnuali(String countryCode, String stateCode, String anno);

	JSONObject getNumberOfEvents(String countryCode, String stateCode, String nameCat, String startDate, String endDate);
}
