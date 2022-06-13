package dev.dotspace.squidly.listener.slash;

import dev.dotspace.squidly.conf.ConstantProvider;
import dev.dotspace.squidly.request.RequestManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static dev.dotspace.squidly.conf.SlashCommandConfiguration.PROFILE_COMMAND;

public class ProfileCommandListener {

  public static void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    assert event.getGuild() == null;
    assert event.getName().equals(PROFILE_COMMAND.getName());

    var playername = Objects.requireNonNull(event.getOption("player")).getAsString();

    RequestManager.getPlayer(playername).value().ifPresentOrElse(getPlayerResponse -> event.deferReply().queue(interactionHook -> {
      var embedBuilder = new EmbedBuilder()
          .setTitle(playername + "'s profile")
          .setThumbnail(getPlayerResponse.avatarUrl() != null ? getPlayerResponse.avatarUrl() : ConstantProvider.DEFAULT_AVATAR_URL)
          .setFooter(ConstantProvider.SUCCESS_RESPONSE_FOOTER);

      interactionHook.editOriginalEmbeds(embedBuilder.build()).queue();
    }), () -> {
      event.reply("error").queue();
    });
  }
}
