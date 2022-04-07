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
    // no effect on stub
    return this;
  }

  @Override
  public ZeebeFuture<Void> send() {
    final ZeebeFutureStub<Void> result = new ZeebeFutureStub<>();

    if (job != null) {
      job.setErrorThrown();
      result.complete(null);
    } else {
      result.completeExceptionally(ExceptionBehavior.buildNotFoundException());
    }

    return result;
  }
}
