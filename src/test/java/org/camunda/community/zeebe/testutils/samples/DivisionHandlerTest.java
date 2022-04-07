package org.camunda.community.zeebe.testutils.samples;

import org.camunda.community.zeebe.testutils.stubs.ActivatedJobStub;
import org.camunda.community.zeebe.testutils.stubs.JobClientStub;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.entry;
import static org.camunda.community.zeebe.testutils.ZeebeWorkerAssertions.assertThat;

public class DivisionHandlerTest {

  private final DivisionHandler sutDivisionHandler = new DivisionHandler();

  @Test
  public void shouldDivideFourByTwo() {
    // given
    final JobClientStub stubJobClient = new JobClientStub();
    final ActivatedJobStub stubActivatedJob = stubJobClient.createActivatedJob();

    final Map<String, Object> variables = new HashMap<>();
    variables.put("a", 4L);
    variables.put("b", 2L);

    stubActivatedJob.setInputVariables(variables);
    // set other fields as required
    //   stubActivatedJob.setBpmnProcessId("division_process");
    //   stubActivatedJob.setRetries(5);
    //   stubActivatedJob.setWorker("division_worker");
    //   ...

    // when
    sutDivisionHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob).completed().extractingOutput().containsOnly(entry("result", 2d));
  }

  @Test
  public void shouldThrowErrorWhenDividingByZero() {
    // given
    final JobClientStub stubJobClient = new JobClientStub();
    final ActivatedJobStub stubActivatedJob = stubJobClient.createActivatedJob();

    final Map<String, Object> variables = new HashMap<>();
    variables.put("a", 4L);
    variables.put("b", 0L);

    stubActivatedJob.setInputVariables(variables);

    // when
    sutDivisionHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob)
        .threwError()
        .hasErrorCode("division-by-zero")
        .extractingErrorMessage()
        .containsIgnoringCase("cannot divide")
        .containsIgnoringCase("by zero");
  }

  @Test
  public void shouldFailWhenInputInvalid() {
    // given
    final JobClientStub stubJobClient = new JobClientStub();
    final ActivatedJobStub stubActivatedJob = stubJobClient.createActivatedJob();

    final Map<String, Object> variables = new HashMap<>();
    variables.put("a", 4L);
    variables.put("b", "INVALID INPUT");

    stubActivatedJob.setInputVariables(variables);

    // when
    sutDivisionHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob).failed().hasRetries(0).hasErrorMessage("exception occurred");
  }
}
