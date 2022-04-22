package dev.dotspace.squidly.session;

import com.google.gson.JsonParser;
import dev.dotspace.squidly.response.SessionResponseAnalyzer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionTests {

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
      {"session_id": "XXXXXASDASD", "ret_msg": "Invalid Signature", "timestamp": "11/28/2019 3:09:16 PM"}
      """, """
      {"session_id": "XDASDASDADSD", "ret_msg": null, "timestamp": "11/28/2019 3:09:16 PM"}
      """, """
      {"session_id": "XDASDASDADSD", "ret_msg": "Approved", "timestamp": "11/28/1971 3:09:16 PM"}
      """ })
  public void testWithValidSessionResponse(String jsonObject) {
    var obj = JsonParser.parseString(jsonObject).getAsJsonObject();

    assertAll(() -> {
      var result = new SessionResponseAnalyzer().analyse(obj);
      assertTrue(result.value().isPresent());
    });
  }

}
