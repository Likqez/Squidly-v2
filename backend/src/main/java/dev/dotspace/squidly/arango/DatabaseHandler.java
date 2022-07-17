package dev.dotspace.squidly.arango;

import com.arangodb.ArangoCursor;
import com.arangodb.util.MapBuilder;
import dev.dotspace.squidly.arango.pojo.FavouritePlayerData;
import dev.dotspace.squidly.arango.pojo.SquidlyUser;

import java.io.IOException;

import static dev.dotspace.squidly.arango.DatabaseCollection.USERS;

public class DatabaseHandler {

  public static void createCollections() {
    var database = new DatabaseSupplier().get();
    if (! database.exists())
      database.create();
    for (DatabaseCollection collection : DatabaseCollection.values())
      if (database.getCollections().stream().filter(collectionEntity -> collectionEntity.getName().equals(collection.colname())).findAny().isEmpty())
        database.createCollection(collection.colname());
  }

  public static SquidlyUser saveUser(SquidlyUser user, FavouritePlayerData fav) {
    var database = new DatabaseSupplier().get();

    try (ArangoCursor<SquidlyUser> res = database.query(
        """
            UPSERT {user_id: @userid}
              INSERT @doc
              UPDATE {
              favourite_players: LENGTH(OLD.favourite_players) < %d ? APPEND(OLD.favourite_players, @fav) : OLD.favourite_players,
              favourite_limit_reaced:  LENGTH(OLD.favourite_players) >= %d
              }
            IN @@coll
            RETURN NEW
            """.formatted(user.favouriteLimit(), user.favouriteLimit()),
        new MapBuilder()
            .put("userid", user.userid())
            .put("fav", fav)
            .put("@coll", USERS.colname())
            .put("doc", user)
            .get(), SquidlyUser.class)) {
      if (res.hasNext())
        return res.next();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static SquidlyUser getUser(String userid) {
    return new DatabaseSupplier().get().collection(USERS.colname()).getDocument(userid, SquidlyUser.class);
  }

  public static int getFavourite(String userid, String fav) {
    var database = new DatabaseSupplier().get();

    try (ArangoCursor<Integer> res = database.query(
        """
            FOR doc IN @@coll
            FILTER doc._key == @user
            FOR fav IN doc.favourite_players
            FILTER LOWER(fav.identifier) == LOWER(@inp)
            RETURN fav.playerid
            """,
        new MapBuilder()
            .put("@coll", USERS.colname())
            .put("user", userid)
            .put("inp", fav)
            .get(), Integer.TYPE)) {
      if (res.hasNext())
        return res.next();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return 0;
  }
}
