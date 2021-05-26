package org.camunda.community.zeebe.testutils.samples;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AsyncJobHandler implements JobHandler {

  private static final ScheduledExecutorService THREAD_POOL = Executors.newScheduledThreadPool(5);

  @Override
  public void handle(final JobClient client, final ActivatedJob job) {
    THREAD_POOL.submit(
        () -> {
          foo();

          client.newCompleteCommand(job.getKey()).send();
        });
  }

  private void foo() {
    try {
      Thread.sleep(1000);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }
}
