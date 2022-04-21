package dev.dotspace.squidly.session;

import dev.dotspace.squidly.CredentialPair;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SignatureFactory {

  public static String getSignature(CredentialPair credentialPair, String command) {
    return SignatureFactory.getSignature(credentialPair.devId(), credentialPair.authKey(), command);
  }

  public static String getSignature(String devid, String authKey, String command) {
    var s = devid + command + authKey + getTimestamp();
    return DigestUtils.md5Hex(s).toLowerCase();
  }

  public static String getTimestamp() {
    var time = ZonedDateTime.now(ZoneId.of("UTC"));
    return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(time);
  }
}
