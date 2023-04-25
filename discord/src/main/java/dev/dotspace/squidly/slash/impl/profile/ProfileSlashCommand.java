package dev.dotspace.squidly.slash.impl.profile;

import dev.dotspace.squidly.request.RequestManager;
import dev.dotspace.squidly.response.model.GetPlayerResponse;
import dev.dotspace.squidly.slash.BasicSlashCommand;
import dev.dotspace.squidly.user.FavouritePlayerData;
import dev.dotspace.squidly.user.SquidlyUser;
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

  public static void onExecute(@NotNull SquidlyUser squidlyUser, @NotNull SlashCommandInteractionEvent event) {
    var playername = event.getOption("player") == null ? "me" : event.getOption("player").getAsString();
    var favPlayer = squidlyUser.getFavourite(playername).map(FavouritePlayerData::playerid).map(String::valueOf);

    RequestManager.getPlayer(favPlayer.orElse(playername))
            .value()
            .ifPresentOrElse(
                    (response) -> sendProfileEmbed(response, squidlyUser, event),
                    () -> event.reply("error").queue()
            );
  }

  private static void sendProfileEmbed(GetPlayerResponse response, SquidlyUser squidlyUser, SlashCommandInteractionEvent event) {
    event.deferReply(squidlyUser.privacyMode()).queue(interactionHook -> {
      var embed = new ProfileEmbedFactory().createEmbed(response);
      interactionHook.editOriginalEmbeds(embed).queue();
    });
  }


}
