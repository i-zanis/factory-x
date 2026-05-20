package com.factoryx.ai;

import com.factoryx.common.domain.Require;

public record PromptMessage(String value) {
    public PromptMessage {
        Require.text(value, "Prompt message");
    }

    public static PromptMessage of(String value) {
        return new PromptMessage(value);
    }
}
