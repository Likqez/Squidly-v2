package dev.dotspace.squidly.slash.impl.profile;

import dev.dotspace.squidly.request.RequestManager;
import dev.dotspace.squidly.slash.BasicSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ProfileSlashCommand extends BasicSlashCommand {

  public static final String NAME = "profile";
  public static final String DESC = "Retrieves generic information about a player";

  public ProfileSlashCommand() {
    super(NAME, DESC);
    addOption(new OptionData(OptionType.STRING, "player", "playername/gamertag or saved identifier"));
  }

  public static void onExecute(@NotNull SlashCommandInteractionEvent event) {
    //TODO: Remove requireNonNull and resolve from database
    var playername = Objects.requireNonNull(event.getOption("player")).getAsString();

    RequestManager.getPlayer(playername).value().ifPresentOrElse(getPlayerResponse -> event.deferReply().queue(interactionHook -> {
      var embed = new ProfileEmbedFactory().createEmbed(getPlayerResponse);

      interactionHook.editOriginalEmbeds(embed).queue();
    }), () -> {
      event.reply("error").queue();
    });
  }


}
