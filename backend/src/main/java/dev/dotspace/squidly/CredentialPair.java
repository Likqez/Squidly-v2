package dev.dotspace.squidly;

public record CredentialPair(String devId, String authKey) {

  public static CredentialPair load() {
    var devId = System.getenv().getOrDefault("squidly_devid", "001");
    var authKey = System.getenv().getOrDefault("squidly_authkey", "");

    return new CredentialPair(devId, authKey);
  }

}
