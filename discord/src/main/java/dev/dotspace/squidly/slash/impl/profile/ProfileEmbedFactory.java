package dev.dotspace.squidly.slash.impl.profile;

import dev.dotspace.squidly.response.model.GetPlayerResponse;
import dev.dotspace.squidly.response.model.RankedContainer;
import dev.dotspace.squidly.util.EmbedFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;

import static dev.dotspace.squidly.util.StringUtils.selectFirstNonNull;

public class ProfileEmbedFactory implements EmbedFactory<GetPlayerResponse> {

  private static final NumberFormat PERCENTAGE_FORMATTER = NumberFormat.getPercentInstance(Locale.ENGLISH);
  static {
    PERCENTAGE_FORMATTER.setMaximumIntegerDigits(2);
    PERCENTAGE_FORMATTER.setMaximumFractionDigits(2);
  }

  public MessageEmbed createEmbed(GetPlayerResponse response) {
    final String playername = selectFirstNonNull(response.hzPlayerName(), response.name(), response.hzGamerTag());
    final String avatar = selectFirstNonNull(response.avatarUrl(), DEFAULT_AVATAR_URL);
    final String title = response.title() != null ? response.title() : "";


    final String generalInfo = getGeneralInfo(response, playername);
    final String gameplayStats = getGameplayStats(response);
    final String kbmStats = getRankedStats(response.rankedKBM());
    final String controllerStats = getRankedStats(response.rankedController());

    return new EmbedBuilder()
        .setTitle(playername + "'s profile")
        .setThumbnail(avatar)
        .setFooter(SUCCESS_RESPONSE_FOOTER)
        .addField(title, generalInfo, false)
        .addField("Gameplay", gameplayStats, false)
        .addField("Ranked KBM", kbmStats, true)
        .addField("Ranked Controller", controllerStats, true)
        .build();
  }

  private String getRankedStats(RankedContainer rankedContainer) {
    final int gamesPlayed = rankedContainer.wins() + rankedContainer.losses() + rankedContainer.leaves();
    final double winAverage = ((double) rankedContainer.wins() / (gamesPlayed < 1 ? 1D : gamesPlayed)*100);
    return """
        ```excel
        %s (%02dTP)
                
        played  = %d
        w/l     = %d/%d
        win avg = %s
        quits   = %d
        ```
        """.formatted(
        rankedContainer.tier().toString(),
        rankedContainer.points(),
        gamesPlayed,
        rankedContainer.wins(),
        rankedContainer.losses(),
        PERCENTAGE_FORMATTER.format(winAverage),
        rankedContainer.leaves()
    );
  }

  private String getGameplayStats(GetPlayerResponse response) {
    //Leaves represents amount of quits in lobby phase. SHould be counted as loss.
    final int totalGamesPlayed = response.wins() + response.losses() + response.leaves();
    final double winAverage = (double) response.wins() / (totalGamesPlayed < 1 ? 1D : totalGamesPlayed);

    final String timePlayed = "%sh and %02dm".formatted(DecimalFormat.getNumberInstance().format(response.minutesPlayed() / 60), response.minutesPlayed() % 60);

    return """
        ```excel
        games played  = %d
        wins/losses   = %d/%d
        win avg.      = %s
        quits         = %d
                
        played for    = %s
        ```
        """.formatted(
        totalGamesPlayed,
        response.wins(),
        response.losses(),
        PERCENTAGE_FORMATTER.format(winAverage),
        response.leaves(),
        timePlayed
    );
  }

  private String getGeneralInfo(GetPlayerResponse response, String playername) {
    var loginDiff = Duration.between(response.lastLoginDateTime(), Instant.now().atZone(ZoneId.of("UTC")));
    var loginDiffDays = loginDiff.toDays();
    var loginDiffHours = loginDiff.toHoursPart();
    var loginDiffMins = loginDiff.toMinutesPart();

    String lastLoginDuration = "%s%s%sago".formatted(
        (loginDiffDays > 0 ? loginDiffDays + "d " : ""),
        (loginDiffHours > 0 ? loginDiffHours + "h " : ""),
        (loginDiffMins > 0 ? loginDiffMins + "m " : "0m ")
    );

    var existingDiff = Duration.between(response.createdDateTime(), Instant.now().atZone(ZoneId.of("UTC")));

    return """
        ```excel
        identifier    = %d
        username      = %s
        playerlevel   = %d (%s xp)
        achievements  = %d
                
        created in    = %s (%sd ago)
        last seen     = %s
                
        platform      = %s
        region        = %s
        ```
        """.formatted(
        response.activePlayerId(),
        playername,
        response.level(),
        DecimalFormat.getNumberInstance().format(response.totalXP()),
        response.totalAchievements(),
        DISPLAY_DATE_TIME_FORMATTER_SHORT.format(response.createdDateTime()),
        DecimalFormat.getNumberInstance().format(existingDiff.toDays()),
        lastLoginDuration,
        response.platform(),
        response.region()
    );
  }

}
