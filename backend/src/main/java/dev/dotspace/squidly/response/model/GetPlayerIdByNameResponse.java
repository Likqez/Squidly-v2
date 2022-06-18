package dev.dotspace.squidly.response.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    String privacyFlag,
    @JsonProperty("ret_msg")
    String msg
) {
}
