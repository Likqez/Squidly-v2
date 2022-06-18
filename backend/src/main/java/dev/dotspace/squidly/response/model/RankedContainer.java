package dev.dotspace.squidly.response.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RankedContainer(
    @JsonProperty("Leaves")
    int leaves,
    @JsonProperty("Losses")
    int losses,
    @JsonProperty("Name")
    String name,
    @JsonProperty("Points")
    int points,
    @JsonProperty("PrevRank")
    int prevRank,
    @JsonProperty("Rank")
    int rank,
    @JsonProperty("Season")
    int season,
    @JsonProperty("Tier")
    int tier,
    @JsonProperty("Trend")
    int trend,
    @JsonProperty("Wins")
    int wins,
    @JsonProperty("player_id")
    int playerId,
    @JsonProperty("ret_msg")
    String msg
) {

}
