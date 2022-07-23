package dev.dotspace.squidly.slash.impl.profile;

import dev.dotspace.squidly.arango.DatabaseHandler;
import dev.dotspace.squidly.request.RequestManager;
import dev.dotspace.squidly.slash.BasicSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public class ProfileSlashCommand extends BasicSlashCommand {

  public static final String NAME = "profile";
  public static final String DESC = "Retrieves generic information about a player";

  public ProfileSlashCommand() {
    super(NAME, DESC);
    addOption(new OptionData(OptionType.STRING, "player", "playername/gamertag or saved identifier"));
  }

  public static void onExecute(@NotNull SlashCommandInteractionEvent event) {
    var playername = event.getOption("player") == null ? "me" : event.getOption("player").getAsString();
    var favPlayerId = DatabaseHandler.getFavourite(event.getUser().getId(), playername);

    RequestManager.getPlayer(favPlayerId > 0 ? String.valueOf(favPlayerId) : playername).value().ifPresentOrElse(getPlayerResponse -> event.deferReply().queue(interactionHook -> {
      var embed = new ProfileEmbedFactory().createEmbed(getPlayerResponse);

      interactionHook.editOriginalEmbeds(embed).queue();
    }), () -> {
      event.reply("error").queue();
    });
  }


}
