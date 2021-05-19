package org.camunda.community.zeebe.testutils.samples;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import java.util.Map;

public class JobHandlerImpl implements JobHandler {

  @Override
  public void handle(final JobClient client, final ActivatedJob job) {

    final var scenario = Scenario.readScenario(job);

    switch (scenario) {
      case COMPLETE_JOB_NO_VARIABLES:
        client.newCompleteCommand(job.getKey()).send().join();
        break;
      case COMPLETE_JOB_WITH_VARIABLES:
        client.newCompleteCommand(job.getKey()).variables(Map.of("key", "value")).send().join();
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
      default:
        throw new IllegalStateException("Not yet implemented");
    }
  }
}