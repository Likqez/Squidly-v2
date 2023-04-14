package dev.dotspace.squidly.request;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.dotspace.squidly.APIEndpoint;
import dev.dotspace.squidly.response.AnalysisResult;
import dev.dotspace.squidly.response.analysis.JsonResponseAnalyzer;
import dev.dotspace.squidly.response.analysis.JsonResponseAnalyzerImpl;
import dev.dotspace.squidly.response.analysis.GetPlayerStatusResponseAnalyzer;
import dev.dotspace.squidly.response.model.DataUsageResponse;
import dev.dotspace.squidly.response.model.GetPlayerIdByNameResponse;
import dev.dotspace.squidly.response.model.GetPlayerResponse;
import dev.dotspace.squidly.response.model.GetPlayerStatusResponse;
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
        .addPath(credentials.user())
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

    final var responseAnalyser = new JsonResponseAnalyzerImpl.Builder<DataUsageResponse>()
            .expectSingleObjectArray()
            .schema("schemas/get-data-used-schema.json")
            .type(DataUsageResponse.class)
            .build();

    var response = new HttpRequestFactory(APIEndpoint.PALADINS)
        .addPath("getdatausedjson")
        .addPath(credentials.user())
        .addPath(cmdSignature)
        .addPath(sessionStore.session())
        .addPath(SignatureFactory.getTimestamp())
        .asyncGET();

    try {
      return response.thenApplyAsync(HttpResponse::body)
          .thenApplyAsync(JsonResponseAnalyzer::toJsonNode)
          .thenApplyAsync(responseAnalyser::analyse)
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

    final var responseAnalyser = new JsonResponseAnalyzerImpl.Builder<GetPlayerIdByNameResponse>()
            .expectSingleObjectArray()
            .schema("schemas/get-player-id-by-name-schema.json")
            .type(GetPlayerIdByNameResponse.class)
            .customFail(jsonNode -> {
              if(jsonNode.size() == 0)
                return new AnalysisResult<>(null,"No player found!");
              return AnalysisResult.SUCCESS;
            })
            .build();

    var response = new HttpRequestFactory(APIEndpoint.PALADINS)
        .addPath("getplayeridbynamejson")
        .addPath(credentials.user())
        .addPath(cmdSignature)
        .addPath(sessionStore.session())
        .addPath(SignatureFactory.getTimestamp())
        .addPath(name)
        .asyncGET();

    try {
      return response.thenApplyAsync(HttpResponse::body)
          .thenApplyAsync(JsonResponseAnalyzer::toJsonNode)
          .thenApplyAsync(responseAnalyser::analyse)
          .get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    return AnalysisResult.ERROR;
  }

  public static AnalysisResult<GetPlayerResponse> getPlayer(String player) {
    var ss = new SessionSupplier();
    var credentials = ss.getCredentialPair();
    var sessionStore = ss.get();
    var cmdSignature = SignatureFactory.getSignature(ss.getCredentialPair(), "getplayer");

    final var responseAnalyser = new JsonResponseAnalyzerImpl.Builder<GetPlayerResponse>()
            .expectSingleObjectArray()
            .schema("schemas/get-player-schema.json")
            .type(GetPlayerResponse.class)
            .customFail(jsonNode -> {
              if(jsonNode.size() == 0)
                return new AnalysisResult<>(null,"No player found!");
              return AnalysisResult.SUCCESS;
            })
            .jacksonModule(new JavaTimeModule())
            .build();

    var response = new HttpRequestFactory(APIEndpoint.PALADINS)
        .addPath("getplayerjson")
        .addPath(credentials.user())
        .addPath(cmdSignature)
        .addPath(sessionStore.session())
        .addPath(SignatureFactory.getTimestamp())
        .addPath(player)
        .asyncGET();

    try {
      return response.thenApplyAsync(HttpResponse::body)
          .thenApplyAsync(JsonResponseAnalyzer::toJsonNode)
          .thenApplyAsync(responseAnalyser::analyse)
          .get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    return AnalysisResult.ERROR;
  }

}
