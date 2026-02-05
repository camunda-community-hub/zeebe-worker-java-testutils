package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.client.api.CamundaFuture;
import io.camunda.client.api.command.CompleteJobCommandStep1;
import io.camunda.client.api.command.CompleteJobResult;
import io.camunda.client.api.command.FinalCommandStep;
import io.camunda.client.api.response.CompleteJobResponse;
import io.camunda.client.impl.CamundaObjectMapper;
import io.camunda.client.impl.command.CommandWithVariables;
import java.time.Duration;
import java.util.function.Function;

class CompleteJobCommandStep1Stub extends CommandWithVariables<CompleteJobCommandStep1>
    implements CompleteJobCommandStep1 {

  private static final CamundaObjectMapper JSON_MAPPER = new CamundaObjectMapper();

  private final ActivatedJobStub job;

  CompleteJobCommandStep1Stub(final ActivatedJobStub job) {
    super(JSON_MAPPER);
    this.job = job;
  }

  @Override
  protected CompleteJobCommandStep1 setVariablesInternal(final String variables) {
    if (job != null) {
      job.setOutputVariables(JSON_MAPPER.fromJsonAsMap(variables));
    }
    return this;
  }

  @Override
  public FinalCommandStep<CompleteJobResponse> requestTimeout(final Duration requestTimeout) {
    // no effect on stub
    return this;
  }

  @Override
  public CamundaFuture<CompleteJobResponse> send() {
    final CamundaFutureStub<CompleteJobResponse> result = new CamundaFutureStub<>();

    if (job != null) {
      job.setCompleted();
      result.complete(new CompleteJobResponse() {});
    } else {
      result.completeExceptionally(ExceptionBehavior.buildNotFoundException());
    }

    return result;
  }

  @Override
  public CompleteJobCommandStep1 withResult(
      Function<CompleteJobCommandJobResultStep, CompleteJobResult> consumer) {
    return null;
  }

  @Override
  public CompleteJobCommandStep1 useRest() {
    return null;
  }

  @Override
  public CompleteJobCommandStep1 useGrpc() {
    return null;
  }
}
