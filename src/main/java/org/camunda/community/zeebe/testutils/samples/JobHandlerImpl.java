package org.camunda.community.zeebe.testutils.samples;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import org.camunda.community.zeebe.testutils.stubs.ActivatedJobStub;

import java.util.HashMap;
import java.util.Map;

public class JobHandlerImpl implements JobHandler {

  @Override
  public void handle(final JobClient client, final ActivatedJob job) {

    final Scenario scenario = Scenario.readScenario(job);

    switch (scenario) {
      case COMPLETE_JOB_NO_VARIABLES:
        client.newCompleteCommand(job.getKey()).send().join();
        break;
      case COMPLETE_JOB_WITH_VARIABLES:
        final Map<String, Object> variables = new HashMap<>();
        variables.put("key", "value");
        client.newCompleteCommand(job.getKey()).variables(variables).send().join();
        break;
      case FAIL_JOB:
        client.newFailCommand(job.getKey()).retries(3).errorMessage("job failed").send().join();
        break;
      case THROW_ERROR:
        client
            .newThrowErrorCommand(job.getKey())
            .errorCode("error-code")
            .errorMessage("a defined error occurred")
            .send()
            .join();
        break;
      case COMMAND_REJECTED_JOB_NOT_FOUND:
        client.newCompleteCommand(Long.MIN_VALUE + job.getKey()).send().join();
      default:
        throw new IllegalStateException("Unexpected case: " + scenario.name());
    }
  }

  public enum Scenario {
    COMPLETE_JOB_NO_VARIABLES,
    COMPLETE_JOB_WITH_VARIABLES,
    FAIL_JOB,
    THROW_ERROR,
    COMMAND_REJECTED_JOB_NOT_FOUND;

    // TODO add more scenarios: timeouts, rejected commands, etc.

    public void writeScenario(final ActivatedJobStub job) {
      job.getVariablesAsMap().put(Scenario.class.getSimpleName(), name());
    }

    public static Scenario readScenario(final ActivatedJob job) {
      final Object value = job.getVariablesAsMap().get(Scenario.class.getSimpleName());

      return Scenario.valueOf((String) value);
    }
  }
}
