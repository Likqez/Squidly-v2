package dev.dotspace.squidly.response.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RankedTier {
  QUALIFYING,
  BRONZE_V,
  BRONZE_IV,
  BRONZE_III,
  BRONZE_II,
  BRONZE_I,
  SILVER_V,
  SILVER_IV,
  SILVER_III,
  SILVER_II,
  SILVER_I,
  GOLD_V,
  GOLD_IV,
  GOLD_III,
  GOLD_II,
  GOLD_I,
  PLATINUM_V,
  PLATINUM_IV,
  PLATINUM_III,
  PLATINUM_II,
  PLATINUM_I,
  DIAMOND_V,
  DIAMOND_IV,
  DIAMOND_III,
  DIAMOND_II,
  DIAMOND_I,
  MASTER,
  GRANDMASTER;

  private final int id;

  RankedTier() {
    this.id = ordinal();
  }

  public static RankedTier getTier(int i) {
    return RankedTier.values()[i];
  }

  @Override
  public String toString() {
    return name().replace("_", " ");
  }

  @JsonValue
  public int getId() {
    return id;
  }
}
 
