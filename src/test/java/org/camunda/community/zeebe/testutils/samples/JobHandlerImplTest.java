package org.camunda.community.zeebe.testutils.samples;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.camunda.community.zeebe.testutils.ZeebeWorkerAssertions.assertThat;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.grpc.StatusRuntimeException;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.assertj.core.api.Assertions;
import org.camunda.community.zeebe.testutils.samples.JobHandlerImpl.Scenario;
import org.camunda.community.zeebe.testutils.stubs.ActivatedJobStub;
import org.camunda.community.zeebe.testutils.stubs.JobClientStub;
import org.junit.jupiter.api.Test;

public class JobHandlerImplTest {

  private final JobHandlerImpl sutJobHandler = new JobHandlerImpl();

  private Module createCustomMapper() {
    return new SimpleModule().addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
  }

  @Test
  public void shouldCompleteJob() {
    // given
    final JobClientStub stubJobClient = new JobClientStub();
    final ActivatedJobStub stubActivatedJob = stubJobClient.createActivatedJob();
    JobHandlerImpl.Scenario.COMPLETE_JOB_NO_VARIABLES.writeScenario(stubActivatedJob);

    // when
    sutJobHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob).completed();
  }

  @Test
  public void shouldCompleteJobWithVariables() {
    // given
    final JobClientStub stubJobClient = new JobClientStub();
    final ActivatedJobStub stubActivatedJob = stubJobClient.createActivatedJob();
    JobHandlerImpl.Scenario.COMPLETE_JOB_WITH_VARIABLES.writeScenario(stubActivatedJob);

    // when
    sutJobHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob)
        .completed()
        .extractingOutput()
        .containsExactly(entry("key", "value"));

    // alternative assertion
    assertThat(stubActivatedJob.getOutputVariables()).containsExactly(entry("key", "value"));
  }

  @Test
  void shouldCompleteJobWithVariablesAndCustomMapper() {
    // given
    final JobClientStub stubJobClient =
        new JobClientStub(JsonMapper.builder().addModule(createCustomMapper()).build());
    final ActivatedJobStub stubActivatedJob = stubJobClient.createActivatedJob();
    Scenario.COMPLETE_JOB_WITH_VARIABLES_AND_CUSTOM_MAPPER.writeScenario(stubActivatedJob);

    // when
    sutJobHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob)
        .completed()
        .extractingOutput()
        .containsExactly(entry("offsetdatetime", "20231020 064323 +02:00"));

    // alternative assertion
    assertThat(stubActivatedJob.getOutputVariables())
        .containsExactly(entry("offsetdatetime", "20231020 064323 +02:00"));
  }

  @Test
  public void shouldFailJob() {
    // given
    final JobClientStub stubJobClient = new JobClientStub();
    final ActivatedJobStub stubActivatedJob = stubJobClient.createActivatedJob();
    JobHandlerImpl.Scenario.FAIL_JOB.writeScenario(stubActivatedJob);

    // when
    sutJobHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob).failed().hasErrorMessage("job failed").hasRetries(3);
  }

  @Test
  public void shouldThrowError() {
    // given
    final JobClientStub stubJobClient = new JobClientStub();
    final ActivatedJobStub stubActivatedJob = stubJobClient.createActivatedJob();
    JobHandlerImpl.Scenario.THROW_ERROR.writeScenario(stubActivatedJob);

    // when
    sutJobHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob)
        .threwError()
        .hasErrorCode("error-code")
        .hasErrorMessage("a defined error occurred");
  }

  @Test
  public void shouldLeaveJobActivated() {
    // given
    final JobClientStub stubJobClient = new JobClientStub();
    final ActivatedJobStub stubActivatedJob = stubJobClient.createActivatedJob();
    Scenario.COMMAND_REJECTED_JOB_NOT_FOUND.writeScenario(stubActivatedJob);

    // when
    Assertions.assertThatThrownBy(() -> sutJobHandler.handle(stubJobClient, stubActivatedJob))
        .isInstanceOf(CompletionException.class)
        .getCause()
        .isInstanceOf(ExecutionException.class)
        .getCause()
        .isInstanceOf(StatusRuntimeException.class);

    // then
    assertThat(stubActivatedJob).isStillActivated();
  }
}
