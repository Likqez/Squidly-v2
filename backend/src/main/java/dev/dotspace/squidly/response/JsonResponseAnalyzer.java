package dev.dotspace.squidly.response;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public interface JsonResponseAnalyzer {

  DateTimeFormatter RESPONSE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy h:m:s a").withZone(ZoneOffset.UTC);

  AnalysisResult<?> analyse(JsonNode jsonNode);

}

