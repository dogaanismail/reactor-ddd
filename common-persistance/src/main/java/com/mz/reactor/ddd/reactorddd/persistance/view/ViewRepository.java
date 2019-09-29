package com.mz.reactor.ddd.reactorddd.persistance.view;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

public interface ViewRepository<S> {

  Mono<S> findBy(Predicate<S> query);

  Mono<List<S>> findAllByList(Predicate<S> query);

  abstract Flux<S> findAllBy(Predicate<S> query);
}
