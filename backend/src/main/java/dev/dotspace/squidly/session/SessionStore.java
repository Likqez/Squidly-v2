package dev.dotspace.squidly.session;

import java.time.ZonedDateTime;

public record SessionStore(String session, ZonedDateTime creation, ZonedDateTime expiry) {}
