package dev.dotspace.squidly.arango.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SquidlyUser(
    @JsonProperty("_key") String _key,
    @JsonProperty("user_id") String userid,
    @JsonProperty("player_id") String playerid,
    @JsonProperty("favourite_players") List<FavouritePlayerData> favourites
) {
}
