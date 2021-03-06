package dev.dotspace.squidly.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dotspace.squidly.APIEndpoint;
import dev.dotspace.squidly.CredentialPair;
import dev.dotspace.squidly.request.HttpRequestFactory;
import dev.dotspace.squidly.request.RequestManager;
import dev.dotspace.squidly.response.AnalysisResult;
import dev.dotspace.squidly.response.analysis.SessionResponseAnalyzer;

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
    if (SessionStorage.getActiveSession().isEmpty() || SessionStorage.getActiveSession().get().expired())
      retrieveNewSession(this.apiEndpoint).value().ifPresent(sessionStore -> {
        if (RequestManager.testSession(sessionStore))
          SessionStorage.setActiveSession(sessionStore);
        else {
          System.err.println("There was an error receiving a valid session. Trying again.. ");
          this.get();
        }
      });

    return SessionStorage.getActiveSession().get();
  }

  private AnalysisResult<SessionStore> retrieveNewSession(APIEndpoint endpoint) {
    var res = new HttpRequestFactory(endpoint)
        .addPath("createsessionjson")
        .addPath(credentialPair.user())
        .addPath(SignatureFactory.getSignature(credentialPair, "createsession"))
        .addPath(SignatureFactory.getTimestamp())
        .asyncGET();

    var mapper = new ObjectMapper();

    try {
      return res.thenApplyAsync(HttpResponse::body)
          .thenApplyAsync(body -> {
            try {
              return mapper.readValue(body, JsonNode.class);
            } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
            }
          })
          .thenApplyAsync(jsonNode -> new SessionResponseAnalyzer().analyse(jsonNode))
          .get();

    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return AnalysisResult.ERROR;
  }

  public CredentialPair getCredentialPair() {
    return credentialPair;
  }
}
