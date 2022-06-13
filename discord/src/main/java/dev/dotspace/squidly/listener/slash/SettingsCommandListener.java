package dev.dotspace.squidly.listener.slash;

import dev.dotspace.squidly.conf.SlashCommandConfiguration;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class SettingsCommandListener {

  public static void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    assert event.getGuild() == null;
    assert event.getName().equals(SlashCommandConfiguration.PROFILE_COMMAND.getName());

    event.reply("I can't handle that command right now :c").setEphemeral(true).queue();
  }
}
