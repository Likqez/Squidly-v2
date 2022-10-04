package dev.dotspace.squidly.util;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface EmbedFactory<T> {

  String SUCCESS_RESPONSE_FOOTER = "Data provided by Hi-Rez. \u00a9 2022 Hi-Rez Studios, Inc. All rights reserved.";
  String ERROR_RESPONSE_FOOTER = "%s - Please share this ID with Squildy maintainers";
  String DEFAULT_AVATAR_URL = "https://raw.githubusercontent.com/luissilva1044894/hirez-api-docs/master/.assets/paladins/avatar/0_1.png";
  String NO_FRIENDS_GIF = "https://raw.githubusercontent.com/luissilva1044894/hirez-api-docs/master/.assets/paladins/avatar/25161.gif";

  DateTimeFormatter DISPLAY_DATE_TIME_FORMATTER_SHORT = DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(Locale.ENGLISH);

  MessageEmbed createEmbed(T data);
}
