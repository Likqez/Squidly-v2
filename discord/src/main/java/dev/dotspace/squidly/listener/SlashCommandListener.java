package dev.dotspace.squidly.listener;

import dev.dotspace.squidly.slash.impl.profile.ProfileSlashCommand;
import dev.dotspace.squidly.slash.impl.settings.SettingsSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends ListenerAdapter {

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    if (event.getGuild() == null) return;


    if (event.getName().equals(ProfileSlashCommand.NAME))
      ProfileSlashCommand.onExecute(event);

    else if (event.getName().equals(SettingsSlashCommand.NAME))
      SettingsSlashCommand.onExecute(event);


  }
}
