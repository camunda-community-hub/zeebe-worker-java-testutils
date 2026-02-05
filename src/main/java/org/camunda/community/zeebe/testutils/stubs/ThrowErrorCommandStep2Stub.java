package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.client.api.CamundaFuture;
import io.camunda.client.api.command.FinalCommandStep;
import io.camunda.client.api.command.ThrowErrorCommandStep1.ThrowErrorCommandStep2;
import io.camunda.client.api.response.ThrowErrorResponse;
import java.io.InputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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
  public ThrowErrorCommandStep2 variables(String variables) {
    return null;
  }

  @Override
  public ThrowErrorCommandStep2 variables(Object variables) {
    return this;
  }

  @Override
  public ThrowErrorCommandStep2 variables(InputStream variables) {
    return this;
  }

  @Override
  public ThrowErrorCommandStep2 variables(Map<String, Object> variables) {
    if (job != null) {
      job.setOutputVariables(variables);
    }
    return this;
  }

  @Override
  public ThrowErrorCommandStep2 variable(String key, Object value) {
    if (job != null) {
      final HashMap<String, Object> variables = HashMap.newHashMap(1);
      variables.put(key, value);
      job.setOutputVariables(variables);
    }
    return this;
  }

  @Override
  public FinalCommandStep<ThrowErrorResponse> requestTimeout(Duration requestTimeout) {
    return null;
  }

  @Override
  public CamundaFuture<ThrowErrorResponse> send() {
    final CamundaFutureStub<ThrowErrorResponse> result =
        new CamundaFutureStub<ThrowErrorResponse>();

    if (job != null) {
      job.setErrorThrown();
      result.complete(null);
    } else {
      result.completeExceptionally(ExceptionBehavior.buildNotFoundException());
    }

    return result;
  }
}
