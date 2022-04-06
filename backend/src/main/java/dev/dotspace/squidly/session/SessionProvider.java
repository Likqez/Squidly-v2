package dev.dotspace.squidly.session;

import dev.dotspace.squidly.CredentialPair;

public class SessionProvider {

  private final CredentialPair credentialPair;

  public SessionProvider() {
    this.credentialPair = CredentialPair.load();
  }

  public SessionProvider(CredentialPair credentialPair) {
    this.credentialPair = credentialPair;
  }

  public String get(String command) {
    SignatureFactory.getSignature(credentialPair, command);

    return null;
  }
}
