package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.event.EventApplier;
import com.mz.reactor.ddd.common.api.valueobject.Id;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum TestFunctions {
  FN;

  public final CommandHandler<TestAggregate, TestAggregateCommand> commandHandler = (aggregate, command) -> {
    try {
      var event = aggregate.validate(command);
      return CommandResult.builder()
          .commandId(command.commandId())
          .statusCode(CommandResult.StatusCode.OK)
          .addEvents(event)
          .build();
    } catch (Exception e) {
      return CommandResult.fromError(
          new RuntimeException(e),
          null,
          command
      );
    }
  };

  public final EventApplier<TestAggregate, DomainEvent> eventApplier = (aggregate, event) -> aggregate.apply((TestAggregateEvent) event);

  public final Function<Id, TestAggregate> aggregateFactory = TestAggregate::new;

  public final BiFunction<Id, List<DomainEvent>, List<DomainEvent>> persistAll = (id, events) -> events;

  public Function<String, String> mapTest1(String val1, String val2) {
    return v -> val1 + " and " + val2 + " not " + v;
  }

  public Function<String, String> mapTest(String val2) {
    return v -> v + val2 + " test ";
  }

  public String testing() {
    return mapTest1("1", "2").andThen(mapTest("Zemo")).apply("Testuj");
  }
}
