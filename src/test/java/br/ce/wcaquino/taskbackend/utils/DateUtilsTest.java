package br.ce.wcaquino.taskbackend.utils;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilsTest {
	
	@Test
	public void shouldReturnTrueToFutureDate() {
		LocalDate date = LocalDate.now().plusYears(10);
		Assert.assertTrue(DateUtils.isEqualOrFutureDate(date));
	}

	@Test
	public void shouldReturnFalseToPastDate() {
		LocalDate date = LocalDate.of(1996, 10, 3);
		Assert.assertFalse(DateUtils.isEqualOrFutureDate(date));
	}
	
	@Test
	public void shouldReturnTrueToCurrentDate() {
		LocalDate date = LocalDate.now();
		Assert.assertTrue(DateUtils.isEqualOrFutureDate(date));
	}

}
