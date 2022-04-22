package dev.dotspace.squidly;

import dev.dotspace.squidly.request.HttpRequestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class HttpRequestFactoryTests {

  @ParameterizedTest(name = "#{index} - {arguments}")
  @ValueSource(strings = { "https://api.paladins.com/paladinsapi.svc", "https://api.paladins.com/", "https://api.paladins.com/paladinsapi.svc/" })
  public void checkURICreation(String uri) {
    final var v = "1126";
    final var t = "XXXXXXX";
    final var x = "abcd";
    final var path = v + "/" + t + "/" + x;

    assertAll(() -> {
      var factory = new HttpRequestFactory(uri);
      assertEquals(uri, factory.getFullUri());

      factory.addPath(v).addPath(t);
      assertEquals(uri.concat("/%s/%s".formatted(v, t)), factory.getFullUri());

      factory.setPath(path);
      assertEquals(uri.concat("/%s/%s/%s".formatted(v, t, x)), factory.getFullUri());
    });


  }
}
