package dev.dotspace.squidly.session;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;

public record SessionStore(String session, ZonedDateTime creation, ZonedDateTime expiry) {

  public boolean expired() {
    var now = ChronoZonedDateTime.from(Instant.now());
    return expiry.isEqual(now) || expiry.isBefore(now);
  }
}
