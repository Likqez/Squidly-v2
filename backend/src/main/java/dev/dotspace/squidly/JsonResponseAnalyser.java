package dev.dotspace.squidly;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class JsonResponseAnalyser {

  public static final DateTimeFormatter RESPONSE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy h:m:s a").withZone(ZoneOffset.UTC);



}
