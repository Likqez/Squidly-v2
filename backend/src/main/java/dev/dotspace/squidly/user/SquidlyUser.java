package dev.dotspace.squidly.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.dotspace.squidly.arango.DatabaseHandler;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record SquidlyUser(
    @JsonProperty("_key") String userid,
    @JsonProperty("favourite_players") List<FavouritePlayerData> favourites,
    @JsonProperty("favourite_limit") int favouriteLimit
) {
  public SquidlyUser(String userid, List<FavouritePlayerData> favourites) {
    this(userid, favourites,5);
  }

  public SquidlyUser(String userid) {
    this(userid, Collections.emptyList(), 5);
  }

  public boolean isFavouriteLimitReached() {
      return this.favourites.size() >= this.favouriteLimit;
  }

  public boolean hasFavourites() {
    return this.favourites.size() > 0;
  }

  public Optional<FavouritePlayerData> getFavourite(String identifier) {
    for (var fav : favourites) {
      if(fav.identifier().equals(identifier))
        return Optional.of(fav);
    }
    return Optional.empty();
  }

  /**
   * Removes a FavouritePlayerData entry from this {@link SquidlyUser}
   * @param identifier the string identifier or the entry's index (<b>starting at 1</b>)
   * @return {@code true}, when an entry has been removed
   */
  public boolean removeFavourite(String identifier) {
    for (var fav : favourites) {
      if(fav.identifier().equals(identifier))
          return this.favourites.remove(fav);
    }

    try {
      var idx = Integer.parseInt(identifier);
      if (idx > 0 && idx <= favourites.size())
        return this.favourites.remove(idx-1) != null;
    } catch (NumberFormatException ignored) {}
    return false;
  }

    /**
     * Adds a FavouritePlayerData entry to this {@link SquidlyUser}
     * @param identifier the choosen identifier
     * @param playerId the HiRez playerid of the new favourite
     * @param playerName the name of the new favourite
     * @return {@code true} if this collection changed as a result of the call
     */
  public boolean addFavourite(String identifier, long playerId, String playerName) {
      if(this.isFavouriteLimitReached()) return false;

      var favouriteData = new FavouritePlayerData(identifier,playerId,playerName);
      if(this.favourites.contains(favouriteData)) return false;// TODO FIX

      return this.favourites.add(favouriteData);
  }

  public SquidlyUser update() {
    DatabaseHandler.saveUser(this);
    return SquidlyUserSupplier.update(this);
  }
}
