package dev.dotspace.squidly.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.qindesign.json.schema.MalformedSchemaException;
import com.qindesign.json.schema.Validator;
import com.qindesign.json.schema.net.URI;
import dev.dotspace.squidly.session.SessionStore;

import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.qindesign.json.schema.net.URI.parseUnchecked;

public class SessionResponseAnalyzer implements JsonResponseAnalyzer {

  private final Validator validator;

  public SessionResponseAnalyzer() {
    try {
      validator = new Validator(JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("schemas/create-session-schema.json")))), parseUnchecked("https://api.paladins.com/paladinsapi.svc/createsessionjson"), null, null, null);
    } catch (MalformedSchemaException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public AnalysisResult<SessionStore> analyse(JsonObject jsonObject) {
    try {
      var success = validator.validate(jsonObject, null, null);

      if (success) {
        var creationTime = ZonedDateTime.parse(jsonObject.get("timestamp").getAsString(), RESPONSE_TIME_FORMATTER);
        var invalidationTime = creationTime.plus(15, ChronoUnit.MINUTES);
        var session = jsonObject.get("session_id").getAsString();

        return new AnalysisResult<>(
            new SessionStore(session, creationTime, invalidationTime),
            jsonObject.get("ret_msg").toString(),
            true);
      }

      System.err.printf("Could not validate against create-session schema: %s%n", jsonObject);

    } catch (MalformedSchemaException e) {
      throw new RuntimeException(e);
    }

    return new AnalysisResult<>(
        null,
        jsonObject.has("ret_msg") ? jsonObject.get("ret_msg").toString() : "no msg found",
        false);
  }
}
