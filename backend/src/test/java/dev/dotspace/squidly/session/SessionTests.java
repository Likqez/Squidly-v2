package dev.dotspace.squidly.session;

import com.google.gson.JsonParser;
import dev.dotspace.squidly.response.SessionResponseAnalyzer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionTests {

  public static void main(String[] args) {

    System.out.println(new SessionSupplier().get());
    SessionStorage.getActiveSession().ifPresentOrElse(System.out::println, () -> System.out.println("not found"));

  }

  @ParameterizedTest(name = "#{index} - with {arguments}")
  @ValueSource(strings = { "{}", """
      {"session_id": "", "ret_msg": "Invalid Signature"}
      """, """
      {"session_id": " ", "ret_msg": "ok"}
      """ })
  public void testWithInvalidSessionResponse(String jsonObject) {
    var obj = JsonParser.parseString(jsonObject).getAsJsonObject();

    assertAll(() -> {
      var result = new SessionResponseAnalyzer().analyse(obj);
      assertTrue(result.value().isEmpty());
    });
  }

  @ParameterizedTest(name = "#{index} - with {arguments}")
  @ValueSource(strings = { """
      {"session_id": "XXXXXASDASD", "ret_msg": "Invalid Signature"}
      """, """
      {"session_id": "XDASDASDADSD", "ret_msg": "ok"}
      """, """
      {"session_id": "XDASDASDADSD"}
      """ })
  public void testWithValidSessionResponse(String jsonObject) {
    var obj = JsonParser.parseString(jsonObject).getAsJsonObject();

    assertAll(() -> {
      var result = new SessionResponseAnalyzer().analyse(obj);
      assertTrue(result.value().isPresent());
    });
  }

}
