package com.factoryx.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiAssistantController {

    private final AiAssistantService aiAssistantService;

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return aiAssistantService.chat(message);
    }

    @GetMapping("/stream")
    public Flux<String> streamChat(@RequestParam String message) {
        return aiAssistantService.streamChat(message);
    }
}
