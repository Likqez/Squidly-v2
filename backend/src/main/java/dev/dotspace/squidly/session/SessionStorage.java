package dev.dotspace.squidly.session;

import java.util.Optional;

class SessionStorage {

  private static SessionStore activeSession;

  public static Optional<SessionStore> getActiveSession() {
    return Optional.ofNullable(activeSession);
  }

  public static void setActiveSession(SessionStore activeSession) {
    SessionStorage.activeSession = activeSession;
  }
}
