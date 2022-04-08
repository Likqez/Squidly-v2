package dev.dotspace.squidly.session;

import dev.dotspace.squidly.APIEndpoint;
import dev.dotspace.squidly.CredentialPair;
import dev.dotspace.squidly.HttpRequestFactory;

import java.net.http.HttpResponse;

public class SessionProvider {

  private final CredentialPair credentialPair;
  private String activeSession = "";
  private final APIEndpoint apiEndpoint;

  public SessionProvider() {
    this.credentialPair = CredentialPair.load();
    this.apiEndpoint = APIEndpoint.PALADINS;
  }

  public SessionProvider(APIEndpoint apiEndpoint) {
    this.credentialPair = CredentialPair.load();
    this.apiEndpoint = apiEndpoint;
  }

  public String get() {
    if (activeSession.isEmpty())
      this.activeSession = retrieveNewSession(this.apiEndpoint);

    return this.activeSession;
  }

  private String retrieveNewSession(APIEndpoint endpoint) {
    var res = new HttpRequestFactory(endpoint.url())
        .addPath("createsessionjson")
        .addPath(credentialPair.devId())
        .addPath(SignatureFactory.getSignature(credentialPair, "createsession"))
        .addPath(SignatureFactory.getTimestamp())
        .asyncGET();

    res.thenAccept((response) -> System.out.println(response));

    return "test";
  }
}
