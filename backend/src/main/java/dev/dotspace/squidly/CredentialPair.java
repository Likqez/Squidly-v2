package dev.dotspace.squidly;

public record CredentialPair(String user, String auth) {

  public static CredentialPair load() {
    var devId = System.getenv().getOrDefault("squidly_devid", "001");
    var authKey = System.getenv().getOrDefault("squidly_authkey", "");

    //TODO Read arguments

    return new CredentialPair(devId, authKey);
  }

}
