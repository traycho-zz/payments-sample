package com.payments.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateTime {

	public static Date afterMinutes(int minutes) {
		final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		final LocalDateTime afterMinutes = now.plusMinutes(minutes);
		return toDate(afterMinutes);
	}

	public static Date toDate(final LocalDateTime dateTime) {
		if (dateTime == null) {
			return null;
		}

		Instant instant = dateTime.toInstant(ZoneOffset.UTC);
		return Date.from(instant);
	}

}
