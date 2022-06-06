package dev.dotspace.squidly.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DataUsageResponse(
    @JsonProperty("Active_Sessions")
    int activeSessions,
    @JsonProperty("Concurrent_Sessions")
    int concurrentSessions,
    @JsonProperty("Request_Limit_Daily")
    int requestLimitDaily,
    @JsonProperty("Session_Cap")
    int sessionCap,
    @JsonProperty("Session_Time_Limit")
    int sessionTimeLimit,
    @JsonProperty("Total_Requests_Today")
    int totalRequestsToday,
    @JsonProperty("Total_Sessions_Today")
    int totalSessionsToday,
    @JsonProperty("ret_msg")
    String msg
) {

}
