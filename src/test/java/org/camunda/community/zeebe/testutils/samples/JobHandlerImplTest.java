package org.camunda.community.zeebe.testutils.samples;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.community.zeebe.testutils.ZeebeWorkerAssertions.assertThat;

import org.camunda.community.zeebe.testutils.stubs.JobClientStub;
import org.junit.jupiter.api.Test;

public class JobHandlerImplTest {

  private final JobHandlerImpl sutJobHandler = new JobHandlerImpl();

  @Test
  public void shouldCompleteJob() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();
    Scenario.COMPLETE_JOB_NO_VARIABLES.writeScenario(stubActivatedJob);

    // when
    sutJobHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob).completed();
  }

  @Test
  public void shouldCompleteJobWithVariables() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();
    Scenario.COMPLETE_JOB_WITH_VARIABLES.writeScenario(stubActivatedJob);

    // when
    sutJobHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob)
        .completed()
        .withOutputThat()
        .containsExactly(entry("key", "value"));

    // alternative assertion
    assertThat(stubActivatedJob.getOutputVariables()).containsExactly(entry("key", "value"));
  }

  @Test
  public void shouldFailJob() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();
    Scenario.FAIL_JOB.writeScenario(stubActivatedJob);

    // when
    sutJobHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob).failed().hasErrorMessage("job failed").hasRetries(3);
  }

  @Test
  public void shouldThrowError() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();
    Scenario.THROW_ERROR.writeScenario(stubActivatedJob);

    // when
    sutJobHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob)
        .threwError()
        .hasErrorCode("error-code")
        .hasErrorMessage("a defined error occurred");
  }
}
