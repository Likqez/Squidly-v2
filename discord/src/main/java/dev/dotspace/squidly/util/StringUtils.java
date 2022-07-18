package dev.dotspace.squidly.util;

import java.util.Arrays;
import java.util.Objects;

public class StringUtils {

  public static String selectFirstNonNull(String first, String... strings) {
    if (first != null) return first;
    if (strings.length == 0) throw new NullPointerException("Only null values specified");

    return Arrays.stream(strings).filter(Objects::nonNull).findFirst().orElseThrow();
  }

  public static String digitToEmoji(int i) {
    return switch (i + 1) {
      case 1 -> ":one:";
      case 2 -> ":two:";
      case 3 -> ":three:";
      case 4 -> ":four:";
      case 5 -> ":five:";
      case 6 -> ":six:";
      case 7 -> ":seven:";
      case 8 -> ":eight:";
      case 9 -> ":nine:";
      default -> ":hash:";
    };
  }

}
