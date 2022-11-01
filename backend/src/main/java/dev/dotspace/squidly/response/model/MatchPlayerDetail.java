package dev.dotspace.squidly.response.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.dotspace.squidly.response.APIConstantProvider;

import java.time.ZonedDateTime;

public record MatchPlayerDetail(
    @JsonProperty("ret_msg")
    String msg,
    @JsonProperty("Account_Champions_Played")
    int accountChampionsPlayed,
    @JsonProperty("Account_Level")
    int accountLevel,
    @JsonProperty("ChampionId")
    int championId,
    @JsonProperty("ChampionLevel")
    int championLvl,
    @JsonProperty("ChampionName")
    String championName,
    @JsonProperty("Mastery_Level")
    int masteryLevel,
    @JsonProperty("Match")
    int matchId,
    @JsonProperty("Queue")
    String queue,
    @JsonProperty("Skin")
    String skin,
    @JsonProperty("SkinId")
    int skindId,
    @JsonProperty("Tier")
    int tier,
    @JsonProperty("mapGame")
    String map,
    @JsonProperty("playerCreated")
    @JsonFormat(timezone = "UTC", pattern = APIConstantProvider.ORIGINAL_DATE_TIME_FORMAT_PATTERN)
    ZonedDateTime playerCreated,
    @JsonProperty("playerId")
    int playerId,
    @JsonProperty("playerName")
    String playerName,
    @JsonProperty("playerPortalId")
    String playerPortalId,
    @JsonProperty("playerPortalUserId")
    String playerPortalUserId,
    @JsonProperty("playerRegion")
    String playerRegion,
    @JsonProperty("taskForce")
    int taskForce,
    @JsonProperty("tierLosses")
    int tierLosses,
    @JsonProperty("tierWins")
    int tierWins
) {
}
