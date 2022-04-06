package dev.dotspace.squidly.session;

import org.apache.commons.codec.digest.DigestUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class SignatureBuilder {

  public String getSignature(String devid, String authKey, String cmd) {
    var s = devid + cmd + authKey + getTimestamp();
    return DigestUtils.md5Hex(s).toLowerCase();
  }

  private String getTimestamp() {
    var time = ZonedDateTime.now(ZoneId.of("UTC"));
    return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(time);
  }
}
