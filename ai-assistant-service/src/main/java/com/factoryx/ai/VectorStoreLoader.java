package com.factoryx.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VectorStoreLoader implements ApplicationRunner {

    private final VectorStore vectorStore;

    @Value("classpath:factory-docs.txt")
    private Resource factoryDocs;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting to load RAG data from {}", factoryDocs.getFilename());

        TextReader textReader = new TextReader(factoryDocs);
        textReader.get().forEach(document -> {
            log.debug("Adding document: {}", document.getContent());
        });

        vectorStore.accept(textReader.get());

        log.info("Successfully loaded RAG data into Vector Store.");
    }
}
