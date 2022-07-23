package dev.dotspace.squidly.slash.impl.match;

import dev.dotspace.squidly.arango.DatabaseHandler;
import dev.dotspace.squidly.slash.BasicSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public class MatchSlashCommand extends BasicSlashCommand {

  public static final String NAME = "match";
  public static final String DESC = "Retrieves information about the current match of a player";

  public MatchSlashCommand() {
    super(NAME, DESC);
    this.addOption(new OptionData(OptionType.STRING, "player", "playername/gamertag or saved identifier", false));
  }

  public static void onExecute(@NotNull SlashCommandInteractionEvent event) {
    var playername = event.getOption("player") == null ? "me" : event.getOption("player").getAsString();
    var favPlayerId = DatabaseHandler.getFavourite(event.getUser().getId(), playername);

    event.deferReply(true).queue(interactionHook -> interactionHook.editOriginal("test").queue());
  }
}
