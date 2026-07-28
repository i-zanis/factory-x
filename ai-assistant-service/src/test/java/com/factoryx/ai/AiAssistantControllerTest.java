package com.factoryx.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AiAssistantControllerTest {

    private MockMvc sut;

    @Mock
    private AiAssistantService aiAssistantService;

    @BeforeEach
    void setUp() {
        sut = MockMvcBuilders.standaloneSetup(new AiAssistantController(aiAssistantService)).build();
    }

    @Test
    void chat_Returns200AndResponseString() throws Exception {
        given(aiAssistantService.chat(any(PromptMessage.class))).willReturn("Hello from AI");

        ResultActions actual = sut.perform(get("/api/v1/ai/chat")
                .param("message", "Status check")
                .accept(MediaType.TEXT_PLAIN));

        actual.andExpect(status().isOk())
                .andExpect(content().string("Hello from AI"));
    }

    @Test
    void streamChat_Returns200AndFluxStream() throws Exception {
        given(aiAssistantService.streamChat(any(PromptMessage.class))).willReturn(Flux.just("Chunk 1", "Chunk 2"));

        ResultActions actual = sut.perform(get("/api/v1/ai/stream")
                .param("message", "Stream check")
                .accept(MediaType.APPLICATION_JSON));

        actual.andExpect(status().isOk());
    }
}
