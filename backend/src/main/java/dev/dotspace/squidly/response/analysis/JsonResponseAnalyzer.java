package dev.dotspace.squidly.response.analysis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dotspace.squidly.response.AnalysisResult;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public interface JsonResponseAnalyzer {

  DateTimeFormatter RESPONSE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy h:m:s a").withZone(ZoneOffset.UTC);

  AnalysisResult<?> analyse(JsonNode jsonNode);

  static JsonNode toJsonNode(String s) {
    var mapper = new ObjectMapper();

    try {
      return mapper.readTree(s);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}

