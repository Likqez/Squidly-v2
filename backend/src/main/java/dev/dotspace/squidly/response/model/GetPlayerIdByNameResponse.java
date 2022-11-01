package dev.dotspace.squidly.response.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.dotspace.squidly.response.json.YesNoBooleanDeserializer;

public record GetPlayerIdByNameResponse(
    @JsonProperty("Name")
    String name,
    @JsonProperty("player_id")
    int playerId,
    @JsonProperty("portal")
    String portal,
    @JsonProperty("portal_id")
    String portalId,
    @JsonProperty("privacy_flag")
    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    boolean privacyFlag,
    @JsonProperty("ret_msg")
    String msg
) {
}
