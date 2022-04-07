package org.camunda.community.zeebe.testutils.samples;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

import java.util.HashMap;
import java.util.Map;

public class DivisionHandler implements JobHandler {

  @Override
  public void handle(final JobClient client, final ActivatedJob job) {
    final Map<String, Object> variables = job.getVariablesAsMap();

    try {
      final long a = (long) (variables.get("a"));
      final long b = (long) (variables.get("b"));

      if (b == 0L) {
        client
            .newThrowErrorCommand(job.getKey())
            .errorCode("division-by-zero")
            .errorMessage("Cannot divide " + a + " by zero")
            .send();
      } else {
        final double result = a / (float) b;
        final Map<String, Object> resultVariables = new HashMap<>();
        resultVariables.put("result", result);

        client.newCompleteCommand(job.getKey()).variables(resultVariables).send();
      }

    } catch (final Exception e) {
      client.newFailCommand(job.getKey()).retries(0).errorMessage("exception occurred").send();
    }
  }
}
