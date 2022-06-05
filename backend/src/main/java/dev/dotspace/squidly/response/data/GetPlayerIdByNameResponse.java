package dev.dotspace.squidly.response.data;

import com.google.gson.annotations.SerializedName;

public record GetPlayerIdByNameResponse(
    @SerializedName("Name")
    String name,
    @SerializedName("player_id")
    int playerId,
    @SerializedName("portal")
    String portal,
    @SerializedName("portal_id")
    String portalId,
    @SerializedName("privacy_flag")
    String privacyFlag,
    @SerializedName("ret_msg")
    String msg
) {
}
