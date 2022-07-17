package dev.dotspace.squidly.slash.impl.settings;

import dev.dotspace.squidly.arango.pojo.SquidlyUser;
import dev.dotspace.squidly.util.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;

public class SettingsEmbedFactory implements EmbedFactory<Object> { //TODO Change type

  @Override
  public MessageEmbed createEmbed(Object data) {
    return null;
  }

  public MessageEmbed createSavedAddEmbed(@NotNull SquidlyUser user) {
    var success = ! user.favouriteLimitReached();
    return new EmbedBuilder()
        .setColor(success ? Color.GREEN : Color.RED)
        .setTitle("/settings saves add")
        .appendDescription(success ? "User successfully saved to favourites." : "User couldn't be saved to favourites.\nYou cannot save more than %d players".formatted(user.favouriteLimit()))
        .addField("Saved:", formatFavsNo(user), true)
        .addField("", formatFavsIdent(user), true)
        .addField("", formatFavsNames(user), true)
        .build();
  }

  private String formatFavsNo(@NotNull SquidlyUser user) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < user.favourites().size(); i++) {
      var no = switch (i + 1) {
        case 1 -> ":one:";
        case 2 -> ":two:";
        case 3 -> ":three:";
        case 4 -> ":four:";
        case 5 -> ":five:";
        default -> ":hash:";
      };
      stringBuilder.append(no).append("\n");
    }

    return stringBuilder.toString();
  }

  private String formatFavsIdent(@NotNull SquidlyUser user) {
    StringBuilder stringBuilder = new StringBuilder();
    user.favourites().forEach(fav -> stringBuilder.append("**").append(fav.identifier()).append("**\n"));
    return stringBuilder.toString();
  }

  private String formatFavsNames(@Nullable SquidlyUser user) {
    if (user == null)
      return "";
    StringBuilder stringBuilder = new StringBuilder();
    user.favourites().forEach(fav -> stringBuilder.append("`").append(fav.playername()).append("`\n"));
    return stringBuilder.toString();
  }
}
