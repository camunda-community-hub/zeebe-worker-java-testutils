package org.camunda.community.zeebe.testutils.samples;

import org.camunda.community.zeebe.testutils.stubs.ActivatedJobStub;
import org.camunda.community.zeebe.testutils.stubs.ActivatedJobStub.Status;
import org.camunda.community.zeebe.testutils.stubs.JobClientStub;
import org.junit.jupiter.api.Test;

import static org.awaitility.Awaitility.await;
import static org.camunda.community.zeebe.testutils.ZeebeWorkerAssertions.assertThat;

public class AsyncJobHandlerTest {

  private final AsyncJobHandler sutAsyncJobHandler = new AsyncJobHandler();

  @Test
  public void shouldCompleteJob() {
    // given
    final JobClientStub stubJobClient = new JobClientStub();
    final ActivatedJobStub stubActivatedJob = stubJobClient.createActivatedJob();

    // when
    sutAsyncJobHandler.handle(stubJobClient, stubActivatedJob);
    await("job finished").until(() -> stubActivatedJob.getStatus() != Status.ACTIVATED);

    // then
    assertThat(stubActivatedJob).completed();
  }
}
