package dev.dotspace.squidly.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.qindesign.json.schema.MalformedSchemaException;
import com.qindesign.json.schema.Validator;
import dev.dotspace.squidly.response.data.DataUsageResponse;

import java.io.InputStreamReader;
import java.util.Objects;

import static com.qindesign.json.schema.net.URI.parseUnchecked;

public class DataUsageResponseAnalyzer implements JsonResponseAnalyzer {

  private final Validator validator;

  public DataUsageResponseAnalyzer() {
    try {
      validator = new Validator(JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("schemas/get-data-used-schema.json")))), parseUnchecked("https://api.paladins.com/paladinsapi.svc/createsessionjson"), null, null, null);
    } catch (MalformedSchemaException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public AnalysisResult<DataUsageResponse> analyse(JsonElement jsonElement) {
    try {
      var success = validator.validate(jsonElement, null, null);

      if (success) {
        var jsonObject = jsonElement.getAsJsonArray().get(0).getAsJsonObject();

        //Todo: find way to auto serialize (pos. switch to jackson)
        var data = new DataUsageResponse(
            jsonObject.get("Active_Sessions").getAsInt(),
            jsonObject.get("Concurrent_Sessions").getAsInt(),
            jsonObject.get("Request_Limit_Daily").getAsInt(),
            jsonObject.get("Session_Cap").getAsInt(),
            jsonObject.get("Session_Time_Limit").getAsInt(),
            jsonObject.get("Total_Requests_Today").getAsInt(),
            jsonObject.get("Total_Sessions_Today").getAsInt(),
            jsonObject.get("ret_msg").toString()
        );

        return new AnalysisResult<>(
            data,
            jsonObject.get("ret_msg").toString(),
            true);
      }

      System.err.printf("Could not validate against 'get-data-used' schema: %s%n", jsonElement);

    } catch (MalformedSchemaException e) {
      throw new RuntimeException(e);
    }

    return new AnalysisResult<>(
        null,
        "Internal error: malformed schema OR schema validation failed.",
        false);
  }
}
