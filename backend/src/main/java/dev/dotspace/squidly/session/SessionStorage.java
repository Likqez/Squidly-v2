package dev.dotspace.squidly.session;

import java.time.ZonedDateTime;
import java.util.Optional;

public class SessionStorage {

  private static SessionStore activeSession;

  public static Optional<SessionStore> getActiveSession() {
    return Optional.ofNullable(activeSession);
  }

  public static void setActiveSession(SessionStore activeSession) {
    SessionStorage.activeSession = activeSession;
  }

  public record SessionStore(String session, ZonedDateTime creation, ZonedDateTime expiry) {}
}
