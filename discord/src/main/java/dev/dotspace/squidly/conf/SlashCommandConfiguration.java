package dev.dotspace.squidly.conf;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Collection;
import java.util.List;

public class SlashCommandConfiguration {

  public static final SlashCommandData PROFILE_COMMAND = new CommandDataImpl("profile", "Retrieves generic information about a player")
      .addOption(OptionType.STRING, "player", "playername/gamertag or saved identifier");

  public static final SlashCommandData SETTINGS_COMMAND = new CommandDataImpl("settings", "squidly user settings")
      .addSubcommandGroups(
          new SubcommandGroupData("saves", "Saved player menu")
              .addSubcommands(
                  new SubcommandData("show", "Display all saved players"),
                  new SubcommandData("add", "Add player to saved players")
                      .addOption(OptionType.STRING, "player", "playername / gamertag", true),
                  new SubcommandData("remove", "Remove player from saved players")
                      .addOption(OptionType.STRING, "save", "saved identifier", true)
              )
      )
      .addSubcommands(
          new SubcommandData("hidden", "Toggle auto privacy mode")
      );


  public static Collection<? extends CommandData> getAllSlashCommands() {
    return List.of(PROFILE_COMMAND, SETTINGS_COMMAND);
  }
}
