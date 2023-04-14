package dev.dotspace.squidly.user;

import dev.dotspace.squidly.arango.DatabaseHandler;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;

import java.util.concurrent.TimeUnit;

public class SquidlyUserSupplier {

    private static final Cache<String, SquidlyUser> userCache = Cache2kBuilder
            .of(String.class, SquidlyUser.class)
            .name("userCache")
            .expireAfterWrite(1, TimeUnit.HOURS)
            .refreshAhead(true)
            .loader(SquidlyUserSupplier::retrieveSquidlyUserFromArango)         // auto populating function
            .build();

    private static SquidlyUser retrieveSquidlyUserFromArango(String userId) {
        var user = DatabaseHandler.getUser(userId); // creates entry if not found
        assert user != null;
        return user;
    }

    public static SquidlyUser get(String userId) {
        return userCache.get(userId);
    }

    public static SquidlyUser update(SquidlyUser user) {
        userCache.put(user.userid(), user);
        return user;
    }


}
