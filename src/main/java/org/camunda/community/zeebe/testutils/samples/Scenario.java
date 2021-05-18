package org.camunda.community.zeebe.testutils.samples;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.camunda.community.zeebe.testutils.stubs.ActivatedJobStub;

public enum Scenario {
  COMPLETE_JOB_NO_VARIABLES,
  COMPLETE_JOB_WITH_VARIABLES,
  FAIL_JOB,
  THROW_ERROR;

  // TODO add more scenarios: timeouts, rejected commands, etc.

  public void writeScenario(final ActivatedJobStub job) {
    job.getVariablesAsMap().put(Scenario.class.getSimpleName(), name());
  }

  public static Scenario readScenario(final ActivatedJob job) {
    final var value = job.getVariablesAsMap().get(Scenario.class.getSimpleName());

    return Scenario.valueOf((String) value);
  }
}
