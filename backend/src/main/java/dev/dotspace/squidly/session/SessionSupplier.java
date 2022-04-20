package dev.dotspace.squidly.session;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.dotspace.squidly.APIEndpoint;
import dev.dotspace.squidly.CredentialPair;
import dev.dotspace.squidly.HttpRequestFactory;
import dev.dotspace.squidly.response.AnalysisResult;
import dev.dotspace.squidly.response.SessionResponseAnalyzer;
import dev.dotspace.squidly.session.SessionStorage.SessionStore;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class SessionSupplier implements Supplier<SessionStore> {

  private final CredentialPair credentialPair;
  private final APIEndpoint apiEndpoint;

  public SessionSupplier() {
    this.credentialPair = CredentialPair.load();
    this.apiEndpoint = APIEndpoint.PALADINS;
  }

  public SessionSupplier(APIEndpoint apiEndpoint) {
    this.credentialPair = CredentialPair.load();
    this.apiEndpoint = apiEndpoint;
  }

  public SessionStore get() {
    if (SessionStorage.getActiveSession().isEmpty())
      retrieveNewSession(this.apiEndpoint).value().ifPresent(SessionStorage::setActiveSession);

    return SessionStorage.getActiveSession().orElse(null);
  }

  private AnalysisResult<SessionStore> retrieveNewSession(APIEndpoint endpoint) {
    var res = new HttpRequestFactory(endpoint)
        .addPath("createsessionjson")
        .addPath(credentialPair.devId())
        .addPath(SignatureFactory.getSignature(credentialPair, "createsession"))
        .addPath(SignatureFactory.getTimestamp())
        .asyncGET();

    try {
      return res.thenApplyAsync(HttpResponse::body)
          .thenApplyAsync(JsonParser::parseString)
          .thenApplyAsync(JsonElement::getAsJsonObject)
          .thenApplyAsync(jsonObject -> new SessionResponseAnalyzer().analyse(jsonObject))
          .get();

    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return new AnalysisResult<>(null, "internal error", false);
  }

  public CredentialPair getCredentialPair() {
    return credentialPair;
  }
}
