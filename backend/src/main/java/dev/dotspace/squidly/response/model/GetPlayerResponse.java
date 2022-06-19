package dev.dotspace.squidly.response.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.dotspace.squidly.response.APIConstantProvider;

import java.time.ZonedDateTime;

public record GetPlayerResponse(
    @JsonProperty("ActivePlayerId")
    int activePlayerId,
    @JsonProperty("AvatarId")
    int avatarId,
    @JsonProperty("AvatarURL")
    String avatarUrl,
    @JsonProperty("Created_Datetime")
    @JsonFormat(timezone = "UTC", pattern = APIConstantProvider.ORIGINAL_DATE_TIME_FORMAT_PATTERN)
    ZonedDateTime createdDateTime,
    @JsonProperty("HoursPlayed")
    int hoursPlayed,
    @JsonProperty("Id")
    int id,
    @JsonProperty("Last_Login_Datetime")
    @JsonFormat(timezone = "UTC", pattern = APIConstantProvider.ORIGINAL_DATE_TIME_FORMAT_PATTERN)
    ZonedDateTime lastLoginDateTime,
    @JsonProperty("Leaves")
    int leaves,
    @JsonProperty("Level")
    int level,
    @JsonProperty("LoadingFrame")
    String loadingFrame,
    @JsonProperty("Losses")
    int losses,
    @JsonProperty("MasteryLevel")
    int masteryLevel,
    @JsonProperty("MergedPlayers")
    Object mergedPlayers,
    @JsonProperty("MinutesPlayed")
    int minutesPlayed,
    @JsonProperty("Name")
    String name,
    @JsonProperty("Personal_Status_Message")
    String personalStatusMessage,
    @JsonProperty("Platform")
    String platform,
    @JsonProperty("RankedConquest")
    RankedContainer rankedConquest,
    @JsonProperty("RankedController")
    RankedContainer rankedController,
    @JsonProperty("RankedKBM")
    RankedContainer rankedKBM,
    @JsonProperty("Region")
    String region,
    @JsonProperty("TeamId")
    int teamId,
    @JsonProperty("Team_Name")
    String teamName,
    @JsonProperty("Tier_Conquest")
    int tierConquest,
    @JsonProperty("Tier_RankedController")
    int tierRankedController,
    @JsonProperty("Tier_RankedKBM")
    int tierRandkedKBM,
    @JsonProperty("Title")
    String title,
    @JsonProperty("Total_Achievements")
    int totalAchievements,
    @JsonProperty("Total_Worshippers")
    int totalWorshippers,
    @JsonProperty("Total_XP")
    int totalXP,
    @JsonProperty("Wins")
    int wins,
    @JsonProperty("hz_gamer_tag")
    String hzGamerTag,
    @JsonProperty("hz_player_name")
    String hzPlayerName,
    @JsonProperty("ret_msg")
    String msg
) {
}

