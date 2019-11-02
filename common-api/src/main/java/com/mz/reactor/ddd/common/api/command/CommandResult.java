package com.mz.reactor.ddd.common.api.command;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Value.Immutable
public interface CommandResult {

  enum StatusCode {
    OK,
    BAD_COMMAND,
    FAILED,
    NOT_MODIFIED;
  }

  String commandId();

  StatusCode statusCode();

  List<DomainEvent> events();

  Optional<RuntimeException> error();

  static ImmutableCommandResult.Builder builder() {
    return ImmutableCommandResult.builder();
  }

  static CommandResult fromError(RuntimeException error, DomainEvent event, Command command) {
    return builder()
        .commandId(Optional.ofNullable(command)
            .map(Command::commandId)
            .orElseGet(() -> UUID.randomUUID().toString()))
        .statusCode(StatusCode.BAD_COMMAND)
        .events(Optional.ofNullable(event).map(List::of).orElseGet(() -> List.of()))
        .error(error)
        .build();
  }

  static CommandResult badCommand(Command cmd) {
    return builder()
        .commandId(Optional.ofNullable(cmd)
            .map(Command::commandId)
            .orElseGet(() -> UUID.randomUUID().toString()))
        .statusCode(StatusCode.BAD_COMMAND)
        .build();
  }

  static CommandResult notModified(Command cmd) {
    return builder()
        .commandId(Optional.ofNullable(cmd)
            .map(Command::commandId)
            .orElseGet(() -> UUID.randomUUID().toString()))
        .statusCode(StatusCode.NOT_MODIFIED)
        .build();
  }
}
