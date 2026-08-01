package com.factoryx.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AiAssistantServiceTest {

    private AiAssistantService sut;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    @BeforeEach
    void setUp() {
        sut = new AiAssistantService(chatClient);
    }

    @Test
    void chat_CallsChatClientAndReturnsContent() {
        var message = new PromptMessage("Check factory status");
        given(chatClient.prompt().user(anyString()).functions(anyString()).call().content())
                .willReturn("All systems operational");

        var actual = sut.chat(message);

        assertThat(actual).isEqualTo("All systems operational");
    }

    @Test
    void streamChat_CallsChatClientAndReturnsFlux() {
        var message = new PromptMessage("Stream status");
        given(chatClient.prompt().user(anyString()).functions(anyString()).stream().content())
                .willReturn(Flux.just("All ", "systems ", "operational"));

        var actual = sut.streamChat(message);
        var results = actual.collectList().block();

        assertThat(results).containsExactly("All ", "systems ", "operational");
    }
}
