package dev.dotspace.squidly.session;

import dev.dotspace.squidly.APIEndpoint;
import dev.dotspace.squidly.CredentialPair;
import dev.dotspace.squidly.HttpRequestFactory;
import dev.dotspace.squidly.session.SessionStorage.SessionStore;

public class SessionProvider {

  private final CredentialPair credentialPair;
  private final APIEndpoint apiEndpoint;

  public SessionProvider() {
    this.credentialPair = CredentialPair.load();
    this.apiEndpoint = APIEndpoint.PALADINS;
  }

  public SessionProvider(APIEndpoint apiEndpoint) {
    this.credentialPair = CredentialPair.load();
    this.apiEndpoint = apiEndpoint;
  }

  public SessionStore get() {
    if (SessionStorage.getActiveSession().isEmpty()) {
      var newSession = retrieveNewSession(this.apiEndpoint);
      SessionStorage.setActiveSession(newSession);
      return newSession;
    }

    return SessionStorage.getActiveSession().get();
  }

  private SessionStore retrieveNewSession(APIEndpoint endpoint) {
    var res = new HttpRequestFactory(endpoint.url())
        .addPath("createsessionjson")
        .addPath(credentialPair.devId())
        .addPath(SignatureFactory.getSignature(credentialPair, "createsession"))
        .addPath(SignatureFactory.getTimestamp())
        .asyncGET();

    res.thenAccept(System.out::println);

    return null;
  }
}
