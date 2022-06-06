package dev.dotspace.squidly.session;

//import com.google.gson.JsonParser;

import dev.dotspace.squidly.request.RequestManager;
import dev.dotspace.squidly.response.JsonResponseAnalyzer;
import dev.dotspace.squidly.response.analysis.SessionResponseAnalyzer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionTests {

  @ParameterizedTest(name = "#{index} - with {arguments}")
  @ValueSource(strings = { "{}", """
      {"session_id": "", "ret_msg": "Invalid Signature", "timestamp": "11/28/2019 3:09:16 PM"}
      """, """
      {"session_id": " ", "ret_msg": "ok", "timestamp": ""}
      """, """
      {"session_id": "XASDDDSAD", "ret_msg": "ok", "timestamp": " "}
      """ })
  public void testWithInvalidSessionResponse(String s) {
    var obj = JsonResponseAnalyzer.toJsonNode(s);
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
  public void testWithValidSessionResponse(String s) {
    var obj = JsonResponseAnalyzer.toJsonNode(s);

    assertAll(() -> {
      var result = new SessionResponseAnalyzer().analyse(obj);
      assertTrue(result.value().isPresent());
    });
  }

  @Test
  public void testSessionRetrieval() {
    var ss = new SessionSupplier();
    assertTrue(RequestManager.testSession(ss.get()));
  }

}
