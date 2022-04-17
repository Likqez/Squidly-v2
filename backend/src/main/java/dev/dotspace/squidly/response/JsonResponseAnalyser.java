package dev.dotspace.squidly.response;

import com.google.gson.JsonObject;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public interface JsonResponseAnalyser {

  DateTimeFormatter RESPONSE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy h:m:s a").withZone(ZoneOffset.UTC);

  AnalysisResult<?> analyse(JsonObject jsonObject);

}

