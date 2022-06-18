package dev.dotspace.squidly.embed;

import dev.dotspace.squidly.conf.ConstantProvider;
import dev.dotspace.squidly.response.model.GetPlayerResponse;
import dev.dotspace.squidly.response.model.RankedContainer;
import dev.dotspace.squidly.response.model.RankedTier;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Arrays;
import java.util.Objects;

public class ProfileCommandEmbedFactory {

  public MessageEmbed createEmbed(GetPlayerResponse response) {
    final String playername = selectFirstNonNull(response.name(), response.hzPlayerName(), response.hzGamerTag());
    final String avatar = selectFirstNonNull(response.avatarUrl(), ConstantProvider.DEFAULT_AVATAR_URL);
    final String title = response.title() != null ? response.title() : "";


    final String generalInfo = getGeneralInfo(response, playername);
    final String gameplayStats = getGameplayStats(response);
    final String kbmStats = getRankedStats(response.rankedKBM());
    final String controllerStats = getRankedStats(response.rankedController());

    return new EmbedBuilder()
        .setTitle(playername + "'s profile")
        .setThumbnail(avatar)
        .setFooter(ConstantProvider.SUCCESS_RESPONSE_FOOTER)
        .addField(title, generalInfo, false)
        .addField("Gameplay", gameplayStats, false)
        .addField("Ranked KBM", kbmStats, true)
        .addField("Ranked Controller", controllerStats, true)
        .build();
  }

  private String getRankedStats(RankedContainer rankedContainer) {
    final int gamesPlayed = rankedContainer.wins() + rankedContainer.losses() + rankedContainer.leaves();
    final double winAverage = (double) rankedContainer.wins() / (gamesPlayed < 1 ? 1D : gamesPlayed);
    final int winAverageDisplay = (int) ((winAverage * 10000D) / 100);

    return """
        ```excel
        %s (#%03d)
                
        played  = %d
        w/l     = %d/%d
        win avg = %d%%
        quits   = %d
        ```
        """.formatted(
        RankedTier.getTier(rankedContainer.tier()).name(),
        rankedContainer.prevRank(),
        gamesPlayed,
        rankedContainer.wins(),
        rankedContainer.losses(),
        winAverageDisplay,
        rankedContainer.leaves()
    );
  }

  private String getGameplayStats(GetPlayerResponse response) {
    //Leaves represents amount of quits in lobby phase. SHould be counted as loss.
    final int totalGamesPlayed = response.wins() + response.losses() + response.leaves();
    final double winAverage = (double) response.wins() / (totalGamesPlayed < 1 ? 1D : totalGamesPlayed);
    final int winAverageDisplay = (int) ((winAverage * 10000D) / 100);

    final String timePlayed = "%dh and %02dm".formatted(response.minutesPlayed() / 60, response.minutesPlayed() % 60);

    return """
        ```excel
        games played  = %d
        wins/losses   = %d/%d
        win avg.      = %02d%%
        quits         = %d
                
        played for    = %s
        ```
        """.formatted(
        totalGamesPlayed,
        response.wins(),
        response.losses(),
        winAverageDisplay,
        response.leaves(),
        timePlayed
    );
  }

  private String getGeneralInfo(GetPlayerResponse response, String playername) {
    return """
        ```excel
        identifier    = %d
        username      = %s
        playerlevel   = %d (%d xp)
        achievements  = %d
                
        created on    = %s
        last seeon on = %s
                
        platform      = %s
        region        = %s
        ```
        """.formatted(
        response.activePlayerId(),
        playername,
        response.level(),
        response.totalXP(),
        response.totalAchievements(),
        response.createdDateTime(), //TODO existing time in days
        response.lastLoginDateTime(), //TODO convert in days from now.
        response.platform(),
        response.region()
    );
  }


  private String selectFirstNonNull(String first, String... strings) {
    if (first != null) return first;
    if (strings.length == 0) throw new NullPointerException("Only null values specified");

    return Arrays.stream(strings).filter(Objects::nonNull).findFirst().orElseThrow();
  }

}
