package dev.dotspace.squidly;

public enum APIEndpoint {
  PALADINS("https://api.paladins.com/paladinsapi.svc")
  ;

  private final String url;

  APIEndpoint(String url) {
    this.url = url;
  }

  public String url() {
    return url;
  }
}
