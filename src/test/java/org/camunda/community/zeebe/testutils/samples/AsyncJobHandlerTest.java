package org.camunda.community.zeebe.testutils.samples;

import static org.awaitility.Awaitility.await;
import static org.camunda.community.zeebe.testutils.ZeebeWorkerAssertions.assertThat;

import org.camunda.community.zeebe.testutils.stubs.ActivatedJobStub.Status;
import org.camunda.community.zeebe.testutils.stubs.JobClientStub;
import org.junit.jupiter.api.Test;

public class AsyncJobHandlerTest {

  private final AsyncJobHandler sutAsyncJobHandler = new AsyncJobHandler();

  @Test
  public void shouldCompleteJob() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();

    // when
    sutAsyncJobHandler.handle(stubJobClient, stubActivatedJob);
    await("job finished").until(() -> stubActivatedJob.getStatus() != Status.ACTIVATED);

    // then
    assertThat(stubActivatedJob).completed();
  }
}
