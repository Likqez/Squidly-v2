package dev.dotspace.squidly.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class AnalysisResult<T> {

    public static final AnalysisResult SCHEMA_MISMATCH = new AnalysisResult<>(null, "Schema mismatch: Response contains unexpected information.");
    public static final AnalysisResult ERROR = new AnalysisResult<>(null, "internal error");
    public static final AnalysisResult SUCCESS = new AnalysisResult<>("success", "");
    private final T value;
    private final String msg;

    @Deprecated(forRemoval = true)
    public AnalysisResult(@Nullable T value, String msg, boolean success) {
        if (success)
            this.value = value;
        else
            this.value = null;
        this.msg = msg;
    }

    public AnalysisResult(@Nullable T value, @NotNull String msg) {
        this.value = value;
        this.msg = msg;
    }

    public Optional<T> value() {
        return Optional.ofNullable(value);
    }

    public String msg() {
        return msg;
    }

    public boolean success() {
        return this.value().isPresent();
    }

    @Override
    public String toString() {
        return "AnalysisResult{" +
                "value=" + value +
                ", msg='" + msg + '\'' +
                '}';
    }
}
