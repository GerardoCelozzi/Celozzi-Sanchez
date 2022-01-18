package com.univpm.Anno;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.univpm.model.Anno;

class AnnoTests {
	
	Anno a;

	@BeforeEach
	void setUp() throws Exception {
		a = new Anno("2022");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		assertEquals("2022", a.getAnno());
	}
	
	@Test
	void test1() {
		assertAll("valori", 
				()->assertEquals("Gennaio", a.getMesi().get(1).getNome_mese()),
				()->assertEquals("Febbraio", a.getMesi().get(2).getNome_mese()),
				()->assertEquals("Dicembre", a.getMesi().get(12).getNome_mese()));
	}

}
