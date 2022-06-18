package dev.dotspace.squidly.response.analysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import dev.dotspace.squidly.response.AnalysisResult;
import dev.dotspace.squidly.response.JsonResponseAnalyzer;
import dev.dotspace.squidly.response.model.DataUsageResponse;

public class DataUsageResponseAnalyzer implements JsonResponseAnalyzer {

  private final JsonSchema schema;

  public DataUsageResponseAnalyzer() {
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    this.schema = factory.getSchema(getClass().getClassLoader().getResourceAsStream("schemas/get-data-used-schema.json"));
  }

  @Override
  public AnalysisResult<DataUsageResponse> analyse(JsonNode jsonNode) {
    var errors = schema.validate(jsonNode);

    if (!errors.isEmpty()) {
      System.err.printf("Could not validate against 'get-data-used' schema: %s%n", jsonNode);
      return AnalysisResult.INVALID;
    }

    jsonNode = jsonNode.get(0);

    var objectMapper = new ObjectMapper();
    try {
      return new AnalysisResult<>(
          objectMapper.treeToValue(jsonNode, DataUsageResponse.class),
          jsonNode.get("ret_msg").asText(),
          true);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return AnalysisResult.ERROR;

  }
}
