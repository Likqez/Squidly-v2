package dev.dotspace.squidly.response;

import java.util.Optional;

public final class AnalysisResult<T> {

  public static final AnalysisResult ERROR = new AnalysisResult<>(null, "internal error", false);
  private final Optional<T> value;
  private final String msg;

  public AnalysisResult(T value, String msg, boolean success) {
    if (success)
      this.value = Optional.of(value);
    else
      this.value = Optional.empty();
    this.msg = msg;
  }

  public Optional<T> value() {
    return value;
  }

  public String msg() {
    return msg;
  }
}
