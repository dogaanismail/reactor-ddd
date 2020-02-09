package com.mz.reactor.ddd.reactorddd.transaction.api;

import com.mz.reactor.ddd.common.components.http.HttpHandler;
import com.mz.reactor.ddd.reactorddd.transaction.api.model.CreateTransactionRequest;
import com.mz.reactor.ddd.reactorddd.transaction.api.model.CreateTransactionResponse;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionState;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Component
public class TransactionHandler implements HttpHandler {

  private final TransactionApplicationService service;

  private final TransactionQuery query;

  public TransactionHandler(TransactionApplicationService transactionApplicationService, TransactionQuery query) {
    this.service = Objects.requireNonNull(transactionApplicationService);
    this.query = Objects.requireNonNull(query);
  }

  public Mono<ServerResponse> getAll(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(query.getAll(), TransactionState.class);
  }

  public Mono<ServerResponse> getById(ServerRequest request) {
    return query.findById(request.pathVariable("id"))
        .flatMap(this::mapToResponse);
  }

  public Mono<ServerResponse> createTransaction(ServerRequest request) {
    return bodyToMono(request, CreateTransactionRequest.class)
        .map(CreateTransactionRequest::payload)
        .flatMap(service::execute)
        .map(CreateTransactionResponse::from)
        .flatMap(this::mapToResponse);
  }

  @Override
  public RouterFunction<ServerResponse> route() {
      var route = RouterFunctions
          .route(POST("").and(accept(MediaType.APPLICATION_JSON_UTF8)), this::createTransaction)
      .andRoute(GET("/").and(accept(MediaType.APPLICATION_JSON_UTF8)), this::getAll)
      .andRoute(GET("/{id}").and(accept(MediaType.APPLICATION_JSON_UTF8)), this::getById);

      return RouterFunctions.route()
          .nest(path("/transactions"), () -> route)
          .build();
  }
}
