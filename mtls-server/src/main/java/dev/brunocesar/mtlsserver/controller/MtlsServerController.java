package dev.brunocesar.mtlsserver.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("mtls-server")
public class MtlsServerController {

    private static final Logger LOG = LoggerFactory.getLogger(MtlsServerController.class);

    @GetMapping
    public Mono<String> get() {
        LOG.info("GET com sucesso!");
        return Mono.just("Olá, GET feito com sucesso! " + OffsetDateTime.now());
    }

    @PostMapping
    public Mono<String> post(@RequestBody JsonNode jsonNode) {
        LOG.info("POST com sucesso, body: {}", jsonNode);
        return Mono.just(jsonNode.toString());
    }

}
