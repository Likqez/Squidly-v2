package dev.dotspace.squidly.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dotspace.squidly.APIEndpoint;
import dev.dotspace.squidly.response.AnalysisResult;
import dev.dotspace.squidly.response.analysis.DataUsageResponseAnalyzer;
import dev.dotspace.squidly.response.analysis.GetPlayerIdByNameAnalyzer;
import dev.dotspace.squidly.response.data.DataUsageResponse;
import dev.dotspace.squidly.response.data.GetPlayerIdByNameResponse;
import dev.dotspace.squidly.session.SessionStore;
import dev.dotspace.squidly.session.SessionSupplier;
import dev.dotspace.squidly.session.SignatureFactory;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class RequestManager {

  public static boolean testSession(SessionStore sessionStore) {
    var ss = new SessionSupplier();
    var credentials = ss.getCredentialPair();
    var cmdSignature = SignatureFactory.getSignature(ss.getCredentialPair(), "testsession");

    var response = new HttpRequestFactory(APIEndpoint.PALADINS)
        .addPath("testsessionjson")
        .addPath(credentials.devId())
        .addPath(cmdSignature)
        .addPath(sessionStore.session())
        .addPath(SignatureFactory.getTimestamp())
        .asyncGET();

    try {
      return response
          .thenApplyAsync(HttpResponse::body)
          .thenApply(s -> s.contains("signature: %s".formatted(cmdSignature))).get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    return false;
  }

  public static AnalysisResult<DataUsageResponse> getDataUsed() {
    var ss = new SessionSupplier();
    var credentials = ss.getCredentialPair();
    var sessionStore = ss.get();
    var cmdSignature = SignatureFactory.getSignature(ss.getCredentialPair(), "getdataused");

    var response = new HttpRequestFactory(APIEndpoint.PALADINS)
        .addPath("getdatausedjson")
        .addPath(credentials.devId())
        .addPath(cmdSignature)
        .addPath(sessionStore.session())
        .addPath(SignatureFactory.getTimestamp())
        .asyncGET();

    var mapper = new ObjectMapper();

    try {
      return response.thenApplyAsync(HttpResponse::body)
          .thenApplyAsync(body -> {
            try {
              return mapper.readValue(body, JsonNode.class);
            } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
            }
          })
          .thenApplyAsync(jsonElement -> new DataUsageResponseAnalyzer().analyse(jsonElement))
          .get();

    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return AnalysisResult.ERROR;

  }

  public static AnalysisResult<GetPlayerIdByNameResponse> getPlayerIdByName(String name) {
    var ss = new SessionSupplier();
    var credentials = ss.getCredentialPair();
    var sessionStore = ss.get();
    var cmdSignature = SignatureFactory.getSignature(ss.getCredentialPair(), "getplayeridbyname");

    var response = new HttpRequestFactory(APIEndpoint.PALADINS)
        .addPath("getplayeridbynamejson")
        .addPath(credentials.devId())
        .addPath(cmdSignature)
        .addPath(sessionStore.session())
        .addPath(SignatureFactory.getTimestamp())
        .addPath(name)
        .asyncGET();

    var mapper = new ObjectMapper();

    try {
      return response.thenApplyAsync(HttpResponse::body)
          .thenApplyAsync(body -> {
            try {
              return mapper.readValue(body, JsonNode.class);
            } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
            }
          })
          .thenApplyAsync(new GetPlayerIdByNameAnalyzer()::analyse)
          .get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    return AnalysisResult.ERROR;
  }

}
