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

  private final List<String> headers = new ArrayList<>();

  private String uri;

  public HttpRequestFactory(String uri) {
    this.uri = uri;
    this.httpClient = DEFAULT_CLIENT;
  }

  public HttpRequestFactory(String uri, HttpClient httpClient) {
    this.uri = uri;
    this.httpClient = httpClient;
  }

  public HttpRequestFactory header(String k, String v) {
    this.headers.add(k);
    this.headers.add(v);
    return this;
  }

  public HttpRequestFactory headers(String... headers) {
    this.headers.addAll(Arrays.asList(headers));
    return this;
  }

  public HttpRequestFactory addPath(String path) {
    if (!this.uri.endsWith("/")) this.uri += "/";
    this.uri += path;
    return this;
  }

  public HttpRequestFactory setPath(String path) {
    var t = uri.split("/");
    this.uri = t[0] + "//" + t[2] + "/" + path;
    return this;
  }

  public CompletableFuture<HttpResponse<String>> asyncGET() {
    var req = HttpRequest.newBuilder(URI.create(this.uri))
        //.headers(this.headers.toArray(new String[0]))
        .build();

    return this.httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
  }
}
