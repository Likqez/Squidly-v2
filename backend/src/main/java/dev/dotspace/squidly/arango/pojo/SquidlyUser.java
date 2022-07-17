package dev.dotspace.squidly.arango.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SquidlyUser(
    @JsonProperty("_key") String _key,
    @JsonProperty("user_id") String userid,
    @JsonProperty("favourite_players") List<FavouritePlayerData> favourites,
    @JsonProperty("favourite_limit_reaced") boolean limitReached
) {

  public SquidlyUser(String userid, List<FavouritePlayerData> favourites) {
    this(userid, userid, favourites, false);
  }
}
