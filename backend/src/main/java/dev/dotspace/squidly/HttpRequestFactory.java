package dev.dotspace.squidly;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpRequestFactory {

  private static final HttpClient DEFAULT_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(15)).build();
  private final HttpClient httpClient;

  private final String uri;
  private String path = "";

  public HttpRequestFactory(APIEndpoint endpoint) {
    this.uri = endpoint.url();
    this.httpClient = DEFAULT_CLIENT;
  }

  public HttpRequestFactory(String uri) {
    this.uri = uri;
    this.httpClient = DEFAULT_CLIENT;
  }

  public HttpRequestFactory(String uri, HttpClient httpClient) {
    this.uri = uri;
    this.httpClient = httpClient;
  }

  public HttpRequestFactory addPath(String path) {
    if (!this.path.endsWith("/")) this.path += "/";
    this.path += path;
    return this;
  }

  public HttpRequestFactory setPath(String path) {
    this.path = "/" + path;
    return this;
  }

  public String getFullUri() {
    return uri.concat(path);
  }

  public CompletableFuture<HttpResponse<String>> asyncGET() {
    var req = HttpRequest.newBuilder(URI.create(this.getFullUri()))
        .build();

    return this.httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
  }
}
