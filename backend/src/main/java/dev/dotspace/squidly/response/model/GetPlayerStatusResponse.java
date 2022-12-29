package dev.dotspace.squidly.response.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.dotspace.squidly.response.json.YesNoBooleanDeserializer;

public record GetPlayerStatusResponse(
    @JsonProperty("ret_msg")
    String msg,
    @JsonProperty("status_string")
    String status,
    @JsonProperty("status")
    int statusId,
    @JsonProperty("personal_status_message")
    String personalStatusMessage,
    @JsonProperty("match_queue_id")
    int matchQueue,
    @JsonProperty("Match")
    int match,
    @JsonProperty("privacy_flag")
    @JsonDeserialize(using = YesNoBooleanDeserializer.class)
    boolean privacyFlag
) {
}
