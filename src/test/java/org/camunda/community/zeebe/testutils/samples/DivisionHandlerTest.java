package org.camunda.community.zeebe.testutils.samples;

import static java.util.Map.entry;
import static org.camunda.community.zeebe.testutils.ZeebeWorkerAssertions.assertThat;

import java.util.Map;
import org.camunda.community.zeebe.testutils.stubs.JobClientStub;
import org.junit.jupiter.api.Test;

public class DivisionHandlerTest {

  private final DivisionHandler sutDivisionHandler = new DivisionHandler();

  @Test
  public void shouldDivideFourByTwo() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();

    stubActivatedJob.setInputVariables(Map.of("a", 4L, "b", 2L));
    // set other fields as required
    //   stubActivatedJob.setBpmnProcessId("division_process");
    //   stubActivatedJob.setRetries(5);
    //   stubActivatedJob.setWorker("division_worker");
    //   ...

    // when
    sutDivisionHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob).completed().withOutputThat().containsOnly(entry("result", 2d));
  }

  @Test
  public void shouldThrowErrorWhenDividingByZero() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();

    stubActivatedJob.setInputVariables(Map.of("a", 4L, "b", 0L));

    // when
    sutDivisionHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob)
        .threwError()
        .hasErrorCode("division-by-zero")
        .hasErrorMessage("Cannot divide 4 by zero");
  }

  @Test
  public void shouldFailWhenInputInvalid() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();

    stubActivatedJob.setInputVariables(Map.of("a", 4L, "b", "INVALID INPUT"));

    // when
    sutDivisionHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob).failed().hasRetries(0).hasErrorMessage("exception occurred");
  }
}
