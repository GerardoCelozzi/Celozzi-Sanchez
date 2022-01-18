package com.univpm.Periodo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.univpm.exception.WrongPeriodException;
import com.univpm.model.Periodo;

class PeriodoTests {

	private Periodo p;
	
	@BeforeEach
	void setUp() throws Exception {
		p = new Periodo(1,1,2022,31,12,2022);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		assertEquals("2022-01-01T00:00:00Z",p.getStartDate());
		assertEquals("2022-12-31T00:00:00Z",p.getEndDate());
	}
	
	@Test
	void test1() {
		assertAll("valori", ()->assertEquals("2022-01-01T00:00:00Z", p.getStartDate()), 
				()->assertEquals("2022-12-31T00:00:00Z", p.getEndDate()));
	}
	
	@Test
	void test2() {
		assertThrows(WrongPeriodException.class, ()-> p.setStartMonth(30));
	}
	
	@Test
	void test3() {
		assertNotNull(p);
	}

}
