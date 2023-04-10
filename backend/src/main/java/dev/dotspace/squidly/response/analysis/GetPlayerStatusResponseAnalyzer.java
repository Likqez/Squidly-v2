package dev.dotspace.squidly.response.analysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import dev.dotspace.squidly.response.AnalysisResult;
import dev.dotspace.squidly.response.model.GetPlayerStatusResponse;

public class GetPlayerStatusResponseAnalyzer implements JsonResponseAnalyzer {

  public final JsonSchema schema;

  public GetPlayerStatusResponseAnalyzer() {
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    this.schema = factory.getSchema(getClass().getClassLoader().getResourceAsStream("schemas/get-player-status-schema.json"));
  }

  @Override
  public AnalysisResult<GetPlayerStatusResponse> analyse(JsonNode jsonNode) {
    var errors = schema.validate(jsonNode);

    if (! errors.isEmpty()) {
      System.err.printf("Could not validate against 'get-player-status' schema: %s%n", jsonNode);
      return AnalysisResult.SCHEMA_MISMATCH;
    }

    if (jsonNode.size() == 0)
      return AnalysisResult.ERROR;

    jsonNode = jsonNode.get(0);

    var objectMapper = new ObjectMapper();
    try {
      return new AnalysisResult<>(
          objectMapper.treeToValue(jsonNode, GetPlayerStatusResponse.class),
          jsonNode.get("ret_msg").asText(),
          true);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return AnalysisResult.ERROR;
  }
}
