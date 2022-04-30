package dev.brunocesar.mtlsclient.controller;

import com.fasterxml.jackson.databind.JsonNode;
import dev.brunocesar.mtlsclient.controller.dto.TestPostRequestDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("mtls-client")
public class MtlsClientController {

    private final WebClient webClient;

    public MtlsClientController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("test/get")
    public Mono<String> get() {
        return webClient.get()
                .uri("https://localhost:8080/mtls-server")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(throwable ->
                        new RuntimeException("Erro: " + throwable.getLocalizedMessage())
                );
    }

    @GetMapping("test/post")
    public Mono<String> post(@RequestBody JsonNode jsonNode) {
        return webClient.post()
                .uri("https://localhost:8080/mtls-server")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(new TestPostRequestDto()), TestPostRequestDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(throwable ->
                        new RuntimeException("Erro: " + throwable.getLocalizedMessage())
                );
    }

}
