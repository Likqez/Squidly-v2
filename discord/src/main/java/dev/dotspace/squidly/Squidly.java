package dev.dotspace.squidly;

import dev.dotspace.squidly.arango.DatabaseCollection;
import dev.dotspace.squidly.arango.DatabaseHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.List;

public class Squidly {

  public static void main(String[] args) {

    var token = System.getenv("squidly_bot_token");

    var jdaBuilder = JDABuilder.createDefault(token)
        .disableCache(List.of(
            CacheFlag.VOICE_STATE,
            CacheFlag.ONLINE_STATUS,
            CacheFlag.ROLE_TAGS,
            CacheFlag.STICKER,
            CacheFlag.FORUM_TAGS,
            CacheFlag.SCHEDULED_EVENTS,
            CacheFlag.EMOJI,
            CacheFlag.ACTIVITY,
            CacheFlag.CLIENT_STATUS,
            CacheFlag.MEMBER_OVERRIDES)
        )
        .setChunkingFilter(ChunkingFilter.NONE)
        .setMemberCachePolicy(MemberCachePolicy.NONE)
        .disableIntents(List.of(
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_WEBHOOKS,
            GatewayIntent.GUILD_MESSAGE_TYPING,
            GatewayIntent.DIRECT_MESSAGE_TYPING)
        )
        .setActivity(Activity.watching("the magistrate"))
        .setAutoReconnect(true);

    DatabaseHandler.createCollections();

    new SquildyBot(jdaBuilder);


  }
}
