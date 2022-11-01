package dev.dotspace.squidly.response.analysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import dev.dotspace.squidly.response.AnalysisResult;
import dev.dotspace.squidly.response.JsonResponseAnalyzer;
import dev.dotspace.squidly.response.model.GetMatchPlayerDetailsReponse;
import dev.dotspace.squidly.response.model.MatchPlayerDetail;

import java.util.Arrays;

public class GetMatchPlayerDetailsResponseAnalyzer implements JsonResponseAnalyzer {

  private final JsonSchema schema;

  public GetMatchPlayerDetailsResponseAnalyzer() {
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    this.schema = factory.getSchema(getClass().getClassLoader().getResourceAsStream("schemas/get-match-player-details-schema.json"));
  }

  @Override
  public AnalysisResult<GetMatchPlayerDetailsReponse> analyse(JsonNode jsonNode) {
    var errors = schema.validate(jsonNode);

    if (! errors.isEmpty()) {
      System.err.printf("Could not validate against 'get-match-player-details' schema: %s%n", jsonNode);
      return AnalysisResult.INVALID;
    }

    if (jsonNode.size() == 0)
      return new AnalysisResult<>(null, "No data found", false);

    var objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    try {
      var playerDetails = Arrays.asList(objectMapper.treeToValue(jsonNode, MatchPlayerDetail[].class));
      return new AnalysisResult<>(
          new GetMatchPlayerDetailsReponse(playerDetails),
          jsonNode.get(0).get("ret_msg").asText(),
          true);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return AnalysisResult.ERROR;
  }
}
