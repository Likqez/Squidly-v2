package dev.dotspace.squidly;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class HttpRequestFactoryTests {

  @ParameterizedTest
  @ValueSource(strings = { "https://api.paladins.com/paladinsapi.svc", "https://api.paladins.com/", "https://api.paladins.com/paladinsapi.svc/" })
  @DisplayName("Checks corretness of URL's build from HttpRequestFactory")
  public void testOutputURI(String uri) {
    final var v = "1126";
    final var t = "XXXXXXX";
    final var x = "abcd";
    final var path = v + "/" + t + "/" + x;

    Assertions.assertAll(() -> {
      var factory = new HttpRequestFactory(uri);
      Assertions.assertEquals(uri, factory.getFullUri());

      factory.addPath(v).addPath(t);
      Assertions.assertEquals(uri.concat("/%s/%s".formatted(v, t)), factory.getFullUri());

      factory.setPath(path);
      Assertions.assertEquals(uri.concat("/%s/%s/%s".formatted(v, t, x)), factory.getFullUri());
    });


  }
}
