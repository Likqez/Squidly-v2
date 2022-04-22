package dev.dotspace.squidly.response.data;

import com.google.gson.annotations.SerializedName;

public record DataUsageResponse(
    @SerializedName("Active_Sessions")
    int activeSessions,
    @SerializedName("Concurrent_Sessions")
    int concurrentSessions,
    @SerializedName("Request_Limit_Daily")
    int requestLimitDaily,
    @SerializedName("Session_Cap")
    int sessionCap,
    @SerializedName("Session_Time_Limit")
    int sessionTimeLimit,
    @SerializedName("Total_Requests_Today")
    int totalRequestsToday,
    @SerializedName("Total_Sessions_Today")
    int totalSessionsToday,
    @SerializedName("ret_msg")
    String msg
) {

}
