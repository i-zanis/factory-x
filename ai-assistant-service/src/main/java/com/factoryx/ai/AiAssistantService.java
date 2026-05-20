package com.factoryx.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class AiAssistantService {

    private final ChatClient chatClient;

    public String chat(PromptMessage message) {
        return chatClient.prompt()
                .user(message.value())
                .call()
                .content();
    }

    public Flux<String> streamChat(PromptMessage message) {
        return chatClient.prompt()
                .user(message.value())
                .stream()
                .content();
    }
}
