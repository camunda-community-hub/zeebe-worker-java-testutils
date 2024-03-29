package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.zeebe.client.api.ZeebeFuture;
import io.camunda.zeebe.client.api.command.FailJobCommandStep1.FailJobCommandStep2;
import io.camunda.zeebe.client.api.command.FinalCommandStep;
import io.camunda.zeebe.client.api.response.FailJobResponse;
import java.time.Duration;

class FailJobCommandStep2Stub implements FailJobCommandStep2 {

  private final ActivatedJobStub job;

  FailJobCommandStep2Stub(final ActivatedJobStub job) {
    this.job = job;
  }

  @Override
  public FailJobCommandStep2 errorMessage(final String errorMessage) {
    if (job != null) {
      job.setErrorMessage(errorMessage);
    }
    return this;
  }

  @Override
  public FinalCommandStep<FailJobResponse> requestTimeout(final Duration requestTimeout) {
    // no effect on stub
    return this;
  }

  @Override
  public ZeebeFuture<FailJobResponse> send() {
    final ZeebeFutureStub<FailJobResponse> result = new ZeebeFutureStub<>();

    if (job != null) {
      job.setFailed();
      result.complete(new FailJobResponse() {});
    } else {
      result.completeExceptionally(ExceptionBehavior.buildNotFoundException());
    }

    return result;
  }

  @Override
  public FailJobCommandStep2 retryBackoff(final Duration backoffTimeout) {
    // no effect on stub
    return this;
  }
}
