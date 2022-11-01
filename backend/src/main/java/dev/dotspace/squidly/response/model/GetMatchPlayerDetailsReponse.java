package dev.dotspace.squidly.response.model;

import java.util.List;

public record GetMatchPlayerDetailsReponse(List<MatchPlayerDetail> playerDetails) {

  public int matchId() {
    return playerDetails.get(0).matchId();
  }

  public String queue() {
    return playerDetails.get(0).queue();
  }

  public String map() {
    return playerDetails.get(0).map();
  }
}

