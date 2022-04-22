package dev.dotspace.squidly.session;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;

public record SessionStore(String session, ZonedDateTime creation, ZonedDateTime expiry) {

  public boolean expired() {
    var now = ZonedDateTime.from(OffsetDateTime.now(ZoneId.of("UTC")));
    return expiry.isEqual(now) || expiry.isBefore(now);
  }
}
