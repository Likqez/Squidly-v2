package dev.dotspace.squidly.slash.impl.settings;

import dev.dotspace.squidly.slash.AdvancedSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

public class SettingsSlashCommand extends AdvancedSlashCommand {

  public static final String NAME = "settings";
  public static final String DESC = "squidly user settings";

  private static final SlashCommandData COMMAND_DATA = new CommandDataImpl(NAME, DESC)
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

  public SettingsSlashCommand() {
    super(
        COMMAND_DATA,
        null);
  }

  public static void onExecute(@NotNull SlashCommandInteractionEvent event) {
    event.reply("I can't handle that command right now :c").setEphemeral(true).queue();
  }

}
