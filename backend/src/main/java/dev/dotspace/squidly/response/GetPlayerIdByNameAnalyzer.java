package dev.dotspace.squidly.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.qindesign.json.schema.MalformedSchemaException;
import com.qindesign.json.schema.Validator;
import dev.dotspace.squidly.response.data.DataUsageResponse;
import dev.dotspace.squidly.response.data.GetPlayerIdByNameResponse;

import java.io.InputStreamReader;
import java.util.Objects;

import static com.qindesign.json.schema.net.URI.parseUnchecked;

public class GetPlayerIdByNameAnalyzer implements JsonResponseAnalyzer {

  private final Validator validator;

  public GetPlayerIdByNameAnalyzer() {
    try {
      validator = new Validator(JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("schemas/get-player-id-by-name-schema.json")))), parseUnchecked("https://api.paladins.com/paladinsapi.svc/getplayeridbynamejson"), null, null, null);
    } catch (MalformedSchemaException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public AnalysisResult<GetPlayerIdByNameResponse> analyse(JsonElement jsonElement) {
    try {
      var success = validator.validate(jsonElement, null, null);

      if (success) {

        if (jsonElement.getAsJsonArray().size() == 0)
          return new AnalysisResult<>(null, "No player found", false);

        var jsonObject = jsonElement.getAsJsonArray().get(0).getAsJsonObject();

        //Todo: find way to auto serialize (pos. switch to jackson)
        var data = new GetPlayerIdByNameResponse(
            jsonObject.get("Name").getAsString(),
            jsonObject.get("player_id").getAsInt(),
            jsonObject.get("portal").getAsString(),
            jsonObject.get("portal_id").getAsString(),
            jsonObject.get("privacy_flag").getAsString(),
            jsonObject.get("ret_msg").toString()
        );

        return new AnalysisResult<>(
            data,
            jsonObject.get("ret_msg").toString(),
            true);
      }

      System.err.printf("Could not validate against 'get-player-id-by-name' schema: %s%n", jsonElement);

    } catch (MalformedSchemaException e) {
      throw new RuntimeException(e);
    }

    return AnalysisResult.MALFORMED;
  }
}
