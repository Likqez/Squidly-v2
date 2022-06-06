package dev.dotspace.squidly.response;

import java.util.Optional;

public final class AnalysisResult<T> {

  public static final AnalysisResult INVALID = new AnalysisResult<>(null, "Internal error: hirez provided invalid information.", false);
  public static final AnalysisResult ERROR = new AnalysisResult<>(null, "internal error", false);
  private final T value;
  private final String msg;

  public AnalysisResult(T value, String msg, boolean success) {
    if (success)
      this.value = value;
    else
      this.value = null;
    this.msg = msg;
  }

  public Optional<T> value() {
    return Optional.ofNullable(value);
  }

  public String msg() {
    return msg;
  }

  @Override
  public String toString() {
    return "AnalysisResult{" +
           "value=" + value +
           ", msg='" + msg + '\'' +
           '}';
  }
}
