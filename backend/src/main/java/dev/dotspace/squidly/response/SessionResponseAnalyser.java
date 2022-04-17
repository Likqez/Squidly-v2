package dev.dotspace.squidly.response;

import com.google.gson.JsonObject;
import dev.dotspace.squidly.session.SessionStorage.SessionStore;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class SessionResponseAnalyser implements JsonResponseAnalyser {

  @Override
  public AnalysisResult<SessionStore> analyse(JsonObject jsonObject) {
    var success = true;

    success = jsonObject.has("session_id");

    var creationTime = ZonedDateTime.parse(jsonObject.get("timestamp").getAsString(), RESPONSE_TIME_FORMATTER);
    var invalidationTime = creationTime.plus(15, ChronoUnit.MINUTES);
    var session = jsonObject.get("session_id").getAsString();

    if (success && (session.isEmpty() || session.isBlank())) {
      System.err.println(jsonObject.get("ret_msg").getAsString());
      success = false;
    }

    return new AnalysisResult<>(
        new SessionStore(session, creationTime, invalidationTime),
        jsonObject.has("ret_msg") ? jsonObject.get("ret_msg").getAsString() : "no msg found",
        success);
  }
}
