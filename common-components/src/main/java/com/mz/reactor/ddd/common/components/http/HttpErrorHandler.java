package com.mz.reactor.ddd.common.components.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public enum HttpErrorHandler {
  FN;

  public <E extends Throwable> Mono<ServerResponse> onError(E e, ServerRequest req) {
    return Mono.just(ErrorMessage.builder().error(e.getMessage()).build())
        .flatMap(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(fromObject(error)));
  }
}
