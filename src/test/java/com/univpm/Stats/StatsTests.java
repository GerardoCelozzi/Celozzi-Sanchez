package com.univpm.Stats;

import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.univpm.service.StatsImpl;

class StatsTests {

	private StatsImpl stats;
	private long x;
	
	@BeforeEach
	void setUp() throws Exception {
		stats = new StatsImpl();
		JSONArray obj = (JSONArray) stats.getstatsNumberOfEvents("CA", "0", "music").get("stats");
		JSONObject stat = (JSONObject) obj.get(0);
		x = (long) stat.get("media eventi per stato  music");
		
//		long a = (long) ((JSONObject) ((JSONArray) stats.getstatsNumberOfEvents("CA", "0", "music").get("stats")).get(0)).get("media eventi per stato music");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test numero eventi dello stato Quebec.
	 */
	@Test
	void test() {
		assertEquals(1159, (long) stats.getNumberOfEvents("CA", "QC", "0", "0", "0").get("numero eventi"));
	}
	
	/**
	 * Test numero media eventi per gli stati del canada per la classificazione Music.
	 */
	@Test 
	void test1() {
		assertEquals(193, x);
	}
	
//	@Test
//	void test2() {
//		assertEquals(13, (long) stats.getStatsAnnuali("CA", "PE", "2022").get("TOT EVENTI"));
//	}

}
