package dev.dotspace.squidly.arango;

public enum DatabaseCollection {
  USERS("users");

  private String colname;
  DatabaseCollection(String name) {
    this.colname = name;
  }

  public String colname() {
    return colname;
  }
}
