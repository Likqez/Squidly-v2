package dev.dotspace.squidly.listener;

import dev.dotspace.squidly.arango.DatabaseHandler;
import dev.dotspace.squidly.user.SquidlyUser;
import dev.dotspace.squidly.slash.impl.match.MatchSlashCommand;
import dev.dotspace.squidly.slash.impl.profile.ProfileSlashCommand;
import dev.dotspace.squidly.slash.impl.settings.SavesSlashCommand;
import dev.dotspace.squidly.user.SquidlyUserSupplier;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends ListenerAdapter {

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    if (event.getGuild() == null) return;

    //Creating SquildyUser Entry for every person ever interacting with the bot.
    var user = SquidlyUserSupplier.get(event.getUser().getId());

    if (event.getName().equals(ProfileSlashCommand.NAME))
      ProfileSlashCommand.onExecute(user, event);

    else if (event.getName().equals(SavesSlashCommand.NAME))
      SavesSlashCommand.onExecute(user,event);


  }
}
