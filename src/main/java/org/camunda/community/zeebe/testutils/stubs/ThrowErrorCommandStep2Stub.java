package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.zeebe.client.api.ZeebeFuture;
import io.camunda.zeebe.client.api.command.FinalCommandStep;
import io.camunda.zeebe.client.api.command.ThrowErrorCommandStep1.ThrowErrorCommandStep2;
import java.time.Duration;

class ThrowErrorCommandStep2Stub implements ThrowErrorCommandStep2 {
  final ActivatedJobStub job;

  ThrowErrorCommandStep2Stub(final ActivatedJobStub job) {
    this.job = job;
  }

  @Override
  public ThrowErrorCommandStep2 errorMessage(final String errorMsg) {
    if (job != null) {
      job.setErrorMessage(errorMsg);
    }
    return this;
  }

  @Override
  public FinalCommandStep<Void> requestTimeout(final Duration requestTimeout) {
    throw new IllegalStateException("Not yet implemented");
  }

  @Override
  public ZeebeFuture<Void> send() {
    final var result = new ZeebeFutureStub<Void>();

    if (job != null) {
      job.setErrorThrown();
      result.complete(null);
    } else {
      result.completeExceptionally(
          new IllegalStateException(
              "Not yet implemented, and I don't know what the real exception would be"));
    }

    return result;
  }
}
