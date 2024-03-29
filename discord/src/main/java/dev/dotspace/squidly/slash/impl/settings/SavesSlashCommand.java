package dev.dotspace.squidly.slash.impl.settings;

import dev.dotspace.squidly.user.SquidlyUser;
import dev.dotspace.squidly.request.RequestManager;
import dev.dotspace.squidly.slash.AdvancedSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

public class SavesSlashCommand extends AdvancedSlashCommand {

  public static final String NAME = "saves";
  public static final String DESC = "Saved player menu";

  private static final SlashCommandData COMMAND_DATA = new CommandDataImpl(NAME, DESC)
      .addSubcommands(
              new SubcommandData("show", "Display all saved accounts"),
              new SubcommandData("add", "Add player to saved accounts")
                      .addOption(OptionType.STRING, "player", "playername / gamertag", true)
                      .addOption(OptionType.STRING, "identifier", "symbol/emoji to identify account"),
              new SubcommandData("remove", "Remove player from saved accounts")
                      .addOption(OptionType.STRING, "save", "No. of saved identifier/identifier", true)
      )
      .addSubcommands(

      );

  public SavesSlashCommand() {
    super(COMMAND_DATA);
  }

  private static final SavesEmbedFactory embedFactory = new SavesEmbedFactory();

  public static void onExecute(@NotNull SquidlyUser squidlyUser, @NotNull SlashCommandInteractionEvent event) {
    if (event.getSubcommandName() != null)
      switch (event.getSubcommandName()) {
        case "add" -> addSaved(squidlyUser,event);
        case "remove" -> removeSaved(squidlyUser, event);
        case "show" -> showSaves(squidlyUser, event);
      }

  }

  private static void addSaved(SquidlyUser squidlyUser, SlashCommandInteractionEvent event) {
    event.deferReply(true).queue(interactionHook -> {
        var inputName = event.getOption("player").getAsString();
        var identifier = event.getOption("identifier") == null ? "me" : event.getOption("identifier").getAsString();

        if(squidlyUser.isFavouriteLimitReached()) {
            interactionHook.editOriginalEmbeds(embedFactory.createFavouriteLimitReachedEmbed(event.getCommandString(),squidlyUser)).queue();
            return;
        }

        RequestManager.getPlayerIdByName(inputName).value().ifPresentOrElse(
                (response) -> {
                    if (squidlyUser.addFavourite(identifier, response.playerId(), inputName)) {
                        squidlyUser.update();
                        interactionHook.editOriginalEmbeds(embedFactory.createSavedAddEmbed(squidlyUser)).queue();
                    }else interactionHook.editOriginalEmbeds(embedFactory.createDuplicateIdentifierEmbed(event.getCommandString(), identifier, squidlyUser)).queue();
                },
                () -> interactionHook.editOriginalEmbeds(embedFactory.createNotFoundEmbed(event.getCommandString(),inputName)).queue()
        );
    });
  }

  private static void removeSaved(SquidlyUser squidlyUser, SlashCommandInteractionEvent event) {
    event.deferReply(true).queue(interactionHook -> {
      var identifier = event.getOption("save") == null ? "me" : event.getOption("save").getAsString();

        if (squidlyUser.removeFavourite(identifier)) {
            squidlyUser.update();
            interactionHook.editOriginalEmbeds(embedFactory.createSavedRemoveEmbed(squidlyUser)).queue();
        } else interactionHook.editOriginalEmbeds(embedFactory.createNotFoundEmbed(event.getCommandString(), identifier)).queue();
    });
  }

  private static void showSaves(SquidlyUser squidlyUser, SlashCommandInteractionEvent event) {
    event.deferReply(true).queue(interactionHook ->
        interactionHook.editOriginalEmbeds(embedFactory.createShowFavsEmbed(squidlyUser)).queue());
  }

}
