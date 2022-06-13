package dev.dotspace.squidly.listener.slash;

import dev.dotspace.squidly.conf.SlashCommandConfiguration;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GlobalSlashCommandListener extends ListenerAdapter {

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    if (event.getGuild() == null) return;


    if (event.getName().equals(SlashCommandConfiguration.PROFILE_COMMAND.getName()))
      ProfileCommandListener.onSlashCommandInteraction(event);

    else if (event.getName().equals(SlashCommandConfiguration.SETTINGS_COMMAND.getName()))
      SettingsCommandListener.onSlashCommandInteraction(event);


  }
}
