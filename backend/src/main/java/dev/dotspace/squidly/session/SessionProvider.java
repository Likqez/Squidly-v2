package dev.dotspace.squidly.session;

import java.util.ServiceLoader;

public class SessionProvider implements ServiceLoader.Provider<String> {

  @Override
  public Class<? extends String> type() {
    return String.class;
  }

  @Override
  public String get() {
    return null;
  }
}
