package dev.dotspace.squidly.response;

import com.google.gson.JsonObject;
import dev.dotspace.squidly.session.SessionStorage.SessionStore;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class SessionResponseAnalyzer implements JsonResponseAnalyzer {

  @Override
  public AnalysisResult<SessionStore> analyse(JsonObject jsonObject) {
    var success = true;

    success = jsonObject.has("session_id");

    var creationTime = jsonObject.has("timestamp") ? ZonedDateTime.parse(jsonObject.get("timestamp").getAsString(), RESPONSE_TIME_FORMATTER) : null;

    var invalidationTime = jsonObject.has("timestamp") ? creationTime.plus(15, ChronoUnit.MINUTES) : null;
    var session = success ? jsonObject.get("session_id").getAsString() : "";

    if (success && (session.isEmpty() || session.isBlank())) {
      System.err.println(jsonObject);
      success = false;
    }

    return new AnalysisResult<>(
        new SessionStore(session, creationTime, invalidationTime),
        jsonObject.has("ret_msg") ? jsonObject.get("ret_msg").getAsString() : "no msg found",
        success);
  }
}
