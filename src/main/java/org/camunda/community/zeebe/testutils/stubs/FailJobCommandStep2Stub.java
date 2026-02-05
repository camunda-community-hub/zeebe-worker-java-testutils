package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.client.api.CamundaFuture;
import io.camunda.client.api.command.FailJobCommandStep1.FailJobCommandStep2;
import io.camunda.client.api.command.FinalCommandStep;
import io.camunda.client.api.response.FailJobResponse;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;

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
  public FailJobCommandStep2 variables(String variables) {
    return this;
  }

  @Override
  public FailJobCommandStep2 variables(Object variables) {
    return this;
  }

  @Override
  public FailJobCommandStep2 variables(InputStream variables) {
    return null;
  }

  @Override
  public FailJobCommandStep2 variables(Map<String, Object> variables) {
    if (job != null) {
      job.setOutputVariables(variables);
    }
    return this;
  }

  @Override
  public FailJobCommandStep2 variable(String key, Object value) {
    if (job != null) {
      job.setOutputVariables(Map.of(key, value));
    }
    return this;
  }

  @Override
  public FinalCommandStep<FailJobResponse> requestTimeout(final Duration requestTimeout) {
    // no effect on stub
    return this;
  }

  @Override
  public CamundaFuture<FailJobResponse> send() {
    final CamundaFutureStub<FailJobResponse> result = new CamundaFutureStub<>();

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
