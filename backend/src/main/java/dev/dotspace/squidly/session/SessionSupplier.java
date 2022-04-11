package dev.dotspace.squidly.session;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.dotspace.squidly.APIEndpoint;
import dev.dotspace.squidly.CredentialPair;
import dev.dotspace.squidly.HttpRequestFactory;
import dev.dotspace.squidly.session.SessionStorage.SessionStore;

import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static dev.dotspace.squidly.JsonResponseAnalyser.RESPONSE_TIME_FORMATTER;

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
      retrieveNewSession(this.apiEndpoint).ifPresent(SessionStorage::setActiveSession);

    return SessionStorage.getActiveSession().get();
  }

  private Optional<SessionStore> retrieveNewSession(APIEndpoint endpoint) {
    var res = new HttpRequestFactory(endpoint.url())
        .addPath("createsessionjson")
        .addPath(credentialPair.devId())
        .addPath(SignatureFactory.getSignature(credentialPair, "createsession"))
        .addPath(SignatureFactory.getTimestamp())
        .asyncGET();

    try {
      return res
          //.exceptionally(JsonResponseAnalyser)
          .thenApplyAsync(HttpResponse::body)
          .thenApplyAsync(JsonParser::parseString)
          .thenApplyAsync(JsonElement::getAsJsonObject)
          .thenApplyAsync(jsonObject -> {

            var creationTime = ZonedDateTime.parse(jsonObject.get("timestamp").getAsString(), RESPONSE_TIME_FORMATTER);
            var invalidationTime = creationTime.plus(15, ChronoUnit.MINUTES);
            var session = jsonObject.get("session_id").getAsString();

            return Optional.of(new SessionStore(session, creationTime, invalidationTime));
          })
          .get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }
}
