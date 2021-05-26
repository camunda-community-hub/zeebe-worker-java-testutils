package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.zeebe.client.api.ZeebeFuture;
import io.camunda.zeebe.client.api.command.CompleteJobCommandStep1;
import io.camunda.zeebe.client.api.command.FinalCommandStep;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;

class CompleteJobCommandStep1Stub implements CompleteJobCommandStep1 {

  private final ActivatedJobStub job;

  CompleteJobCommandStep1Stub(final ActivatedJobStub job) {
    this.job = job;
  }

  @Override
  public CompleteJobCommandStep1 variables(final InputStream variables) {
    throw new IllegalStateException("Not yet implemented");
  }

  @Override
  public CompleteJobCommandStep1 variables(final String variables) {
    throw new IllegalStateException("Not yet implemented");
  }

  @Override
  public CompleteJobCommandStep1 variables(final Map<String, Object> variables) {
    if (job != null) {
      job.setOutputVariables(variables);
    }
    return this;
  }

  @Override
  public CompleteJobCommandStep1 variables(final Object variables) {
    throw new IllegalStateException("Not yet implemented");
  }

  @Override
  public FinalCommandStep<CompleteJobResponse> requestTimeout(final Duration requestTimeout) {
    // no effect on stub
    return this;
  }

  @Override
  public ZeebeFuture<CompleteJobResponse> send() {
    final var result = new ZeebeFutureStub<CompleteJobResponse>();

    if (job != null) {
      job.setCompleted();
      result.complete(new CompleteJobResponse() {});
    } else {
      result.completeExceptionally(
          new IllegalStateException(
              "Not yet implemented, and I don't know what the real exception would be"));
    }

    return result;
  }
}
