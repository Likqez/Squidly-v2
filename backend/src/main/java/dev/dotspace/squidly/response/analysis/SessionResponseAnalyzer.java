package dev.dotspace.squidly.response.analysis;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import dev.dotspace.squidly.response.AnalysisResult;
import dev.dotspace.squidly.response.JsonResponseAnalyzer;
import dev.dotspace.squidly.session.SessionStore;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class SessionResponseAnalyzer implements JsonResponseAnalyzer {

  private final JsonSchema schema;

  public SessionResponseAnalyzer() {
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    this.schema = factory.getSchema(getClass().getClassLoader().getResourceAsStream("schemas/create-session-schema.json"));
  }

  @Override
  public AnalysisResult<SessionStore> analyse(JsonNode jsonNode) {
    var errors = schema.validate((jsonNode));

    if (!errors.isEmpty()) {
      System.err.printf("Could not validate against create-session schema: %s%n", jsonNode);
      return AnalysisResult.INVALID;
    }

    var creationTime = ZonedDateTime.parse(jsonNode.get("timestamp").asText(), RESPONSE_TIME_FORMATTER);
    var invalidationTime = creationTime.plus(15, ChronoUnit.MINUTES);
    var session = jsonNode.get("session_id").asText();

    return new AnalysisResult<>(
        new SessionStore(session, creationTime, invalidationTime),
        jsonNode.get("ret_msg").asText(),
        true);
  }
}
