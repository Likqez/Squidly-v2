package dev.dotspace.squidly.slash.impl.settings;

import dev.dotspace.squidly.user.SquidlyUser;
import dev.dotspace.squidly.util.EmbedFactory;
import dev.dotspace.squidly.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class SavesEmbedFactory implements EmbedFactory<Object> { //TODO Change type

  @Override
  public MessageEmbed createEmbed(Object data) {
    return null;
  }

  /* /settings saves add */
  public MessageEmbed createSavedAddEmbed(@NotNull SquidlyUser user) {
    var success = ! user.isFavouriteLimitReached();
    return new EmbedBuilder()
        .setColor(Color.GREEN)
        .setTitle("/settings saves add")
        .appendDescription("User successfully saved to favourites.")
        .appendDescription("\n\n**Now saved favourites:**")
        .addField("#No.", formatFavsNo(user), true)
        .addField("Ident.", formatFavsIdent(user), true)
        .addField("Username", formatFavsNames(user), true)
        .build();
  }

  private String formatFavsNo(@NotNull SquidlyUser user) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < user.favourites().size(); i++) {
      var no = StringUtils.digitToEmoji(i);
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

  /* /saves remove */

  public MessageEmbed createSavedRemoveEmbed(@NotNull SquidlyUser user) {
    if(user.hasFavourites())
        return new EmbedBuilder()
            .setColor(Color.GREEN)
            .setTitle("/settings saves remove")
            .appendDescription("Favourites successfully updated.")
            .appendDescription("\n\n**Now saved favourites:**")
            .addField("#No.", formatFavsNo(user), true)
            .addField("Ident.", formatFavsIdent(user), true)
            .addField("Username", formatFavsNames(user), true)
            .build();
    return new EmbedBuilder()
            .setColor(Color.ORANGE)
            .setTitle("Your saved favourites:")
            .setImage(NO_FRIENDS_GIF)
            .setDescription("```Seems like no one's here :P```")
            .build();
  }

  /* /saves show */

  public MessageEmbed createShowFavsEmbed(@NotNull SquidlyUser user) {
    if (user.hasFavourites())
      return new EmbedBuilder()
          .setColor(Color.GREEN)
          .setTitle("Your saved favourites:")
          .addField("#No.", formatFavsNo(user), true)
          .addField("Ident.", formatFavsIdent(user), true)
          .addField("Username", formatFavsNames(user), true)
          .build();

    return new EmbedBuilder()
        .setColor(Color.ORANGE)
        .setTitle("Your saved favourites:")
        .setImage(NO_FRIENDS_GIF)
        .setDescription("```Seems like no one's here :P```")
        .build();
  }

  /* Player not found error */
  public MessageEmbed createNotFoundEmbed(@NotNull String command, @NotNull String input) {
    return new EmbedBuilder()
        .setColor(Color.RED)
        .setTitle(command)
        .appendDescription("``%s`` could not be found. Please check for typos.".formatted(input))
        .build();
  }

  /* Saved Favourites limit reached error */
  public MessageEmbed createFavouriteLimitReachedEmbed(@NotNull String command, @NotNull SquidlyUser user) {
    return new EmbedBuilder()
            .setColor(Color.RED)
            .setTitle(command)
            .appendDescription("User couldn't be saved to favourites.\nYou cannot save more than %d players.".formatted(user.favouriteLimit()))
            .appendDescription("\n\n**Your saved favourites:**")
            .addField("#No.", formatFavsNo(user), true)
            .addField("Ident.", formatFavsIdent(user), true)
            .addField("Username", formatFavsNames(user), true)
            .build();
  }

  /* Trying to save with duplicate identifier error */
  public MessageEmbed createDuplicateIdentifierEmbed(@NotNull String command, @NotNull String input, @NotNull SquidlyUser user) {
    return new EmbedBuilder()
            .setColor(Color.RED)
            .setTitle(command)
            .appendDescription("User couldn't be saved to favourites.\nIdentifier `%s` is already in use.".formatted(input))
            .appendDescription("\n\n**Your saved favourites:**")
            .addField("#No.", formatFavsNo(user), true)
            .addField("Ident.", formatFavsIdent(user), true)
            .addField("Username", formatFavsNames(user), true)
            .build();
  }
}
