package dev.dotspace.squidly.slash.impl.settings;

import dev.dotspace.squidly.arango.DatabaseHandler;
import dev.dotspace.squidly.arango.pojo.FavouritePlayerData;
import dev.dotspace.squidly.arango.pojo.SquidlyUser;
import dev.dotspace.squidly.request.RequestManager;
import dev.dotspace.squidly.slash.AdvancedSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SettingsSlashCommand extends AdvancedSlashCommand {

  public static final String NAME = "settings";
  public static final String DESC = "squidly user settings";

  private static final SlashCommandData COMMAND_DATA = new CommandDataImpl(NAME, DESC)
      .addSubcommandGroups(
          new SubcommandGroupData("saves", "Saved player menu")
              .addSubcommands(
                  new SubcommandData("show", "Display all saved players"),
                  new SubcommandData("add", "Add player to saved players")
                      .addOption(OptionType.STRING, "player", "playername / gamertag", true)
                      .addOption(OptionType.STRING, "identifier", "symbol/emoji to identify user"),
                  new SubcommandData("remove", "Remove player from saved players")
                      .addOption(OptionType.STRING, "save", "saved identifier", true)
              )
      )
      .addSubcommands(
          new SubcommandData("hidden", "Toggle auto privacy mode")
      );

  public SettingsSlashCommand() {
    super(COMMAND_DATA);
  }

  public static void onExecute(@NotNull SlashCommandInteractionEvent event) {
    if (event.getSubcommandGroup() != null && event.getSubcommandGroup().equals("saves"))
      switch (event.getSubcommandName()) {
        case "add" -> event.deferReply(true).queue(interactionHook -> {
          var playername = event.getOption("player").getAsString();
          var identifier = event.getOption("identifier") != null ? event.getOption("identifier").getAsString() : "me";
          var userid = event.getUser().getId();

          var embedFactory = new SettingsEmbedFactory();

          RequestManager.getPlayer(playername)
              .value()
              .ifPresentOrElse(getPlayerRes -> {
                var response = DatabaseHandler.saveUser(new SquidlyUser(userid, List.of(new FavouritePlayerData(identifier, getPlayerRes.id(), playername))), new FavouritePlayerData(identifier, getPlayerRes.id(), playername));
                interactionHook.editOriginalEmbeds(embedFactory.createSavedAddEmbed(response)).queue();
              }, () -> interactionHook.editOriginalEmbeds(embedFactory.createPlayerNotFoundEmbed(event.getCommandString(), playername)).queue());
        });
        case "remove" -> {
        }
        case "show" -> {
        }


      }

  }

}
