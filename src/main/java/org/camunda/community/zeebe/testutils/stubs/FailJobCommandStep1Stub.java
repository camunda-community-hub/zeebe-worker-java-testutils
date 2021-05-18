package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.zeebe.client.api.command.FailJobCommandStep1;

class FailJobCommandStep1Stub implements FailJobCommandStep1 {

  private final ActivatedJobStub job;

  FailJobCommandStep1Stub(final ActivatedJobStub job) {
    this.job = job;
  }

  @Override
  public FailJobCommandStep2 retries(final int remainingRetries) {
    if (job != null) {
      job.setRemainingRetries(remainingRetries);
    }
    return new FailJobCommandStep2Stub(job);
  }
}
