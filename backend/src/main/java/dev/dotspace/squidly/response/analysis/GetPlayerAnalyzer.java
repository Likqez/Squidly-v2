package dev.dotspace.squidly.response.analysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import dev.dotspace.squidly.response.AnalysisResult;
import dev.dotspace.squidly.response.JsonResponseAnalyzer;
import dev.dotspace.squidly.response.data.GetPlayerResponse;

public class GetPlayerAnalyzer implements JsonResponseAnalyzer {

  private final JsonSchema schema;

  public GetPlayerAnalyzer() {
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    this.schema = factory.getSchema(getClass().getClassLoader().getResourceAsStream("schemas/get-player-schema.json"));
  }

  @Override
  public AnalysisResult<GetPlayerResponse> analyse(JsonNode jsonNode) {
    var errors = schema.validate(jsonNode);

    if (!errors.isEmpty()) {
      System.err.printf("Could not validate against 'get-player' schema: %s%n", jsonNode);
      return AnalysisResult.INVALID;
    }

    if (jsonNode.size() == 0)
      return new AnalysisResult<>(null, "No player found", false);

    jsonNode = jsonNode.get(0);

    var objectMapper = new ObjectMapper();
    try {
      return new AnalysisResult<>(
          objectMapper.treeToValue(jsonNode, GetPlayerResponse.class),
          jsonNode.get("ret_msg").asText(),
          true);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return AnalysisResult.ERROR;
  }
}
