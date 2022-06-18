package dev.dotspace.squidly.listener.slash;

import dev.dotspace.squidly.embed.ProfileCommandEmbedFactory;
import dev.dotspace.squidly.request.RequestManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static dev.dotspace.squidly.conf.SlashCommandConfiguration.PROFILE_COMMAND;

public class ProfileCommandListener {

  public static void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    assert event.getGuild() != null;
    assert event.getName().equals(PROFILE_COMMAND.getName());

    //TODO: Remove requireNonNull and resolve from database
    var playername = Objects.requireNonNull(event.getOption("player")).getAsString();

    RequestManager.getPlayer(playername).value().ifPresentOrElse(getPlayerResponse -> event.deferReply().queue(interactionHook -> {
      var embed = new ProfileCommandEmbedFactory().createEmbed(getPlayerResponse);

      interactionHook.editOriginalEmbeds(embed).queue();
    }), () -> {
      event.reply("error").queue();
    });
  }
}
