package dev.dotspace.squidly.arango;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDatabase;
import com.arangodb.util.MapBuilder;
import dev.dotspace.squidly.user.SquidlyUser;

import java.io.IOException;

import static dev.dotspace.squidly.arango.DatabaseCollection.USERS;

public class DatabaseHandler {

  private static final ArangoDatabase database = new DatabaseSupplier().get();

  public static void createCollections() {
    var database = new DatabaseSupplier().get();
    if (! database.exists())
      database.create();
    for (DatabaseCollection collection : DatabaseCollection.values())
      if (database.getCollections().stream().filter(collectionEntity -> collectionEntity.getName().equals(collection.colname())).findAny().isEmpty())
        database.createCollection(collection.colname());
  }

  public static SquidlyUser saveUser(SquidlyUser user) {
    try (ArangoCursor<SquidlyUser> res = database.query(
        """
            UPSERT {_key: @userid}
              INSERT @doc
              UPDATE @doc
            IN @@coll
            RETURN NEW
            """,
        new MapBuilder()
            .put("userid", user.userid())
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
    var newUser =  new SquidlyUser(userid);
    try (ArangoCursor<SquidlyUser> res = database.query(
            """
                UPSERT {_key: @userid}
                  INSERT @doc
                  UPDATE {}
                IN @@coll
                RETURN NEW
                """,
            new MapBuilder()
                    .put("userid", userid)
                    .put("@coll", USERS.colname())
                    .put("doc", newUser)
                    .get(), SquidlyUser.class)) {
      if (res.hasNext())
        return res.next();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  return null;
  }
}
