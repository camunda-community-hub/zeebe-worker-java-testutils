package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.zeebe.client.api.command.ThrowErrorCommandStep1;

class ThrowErrorCommandStep1Stub implements ThrowErrorCommandStep1 {

  private final ActivatedJobStub job;

  ThrowErrorCommandStep1Stub(final ActivatedJobStub job) {
    this.job = job;
  }

  @Override
  public ThrowErrorCommandStep2 errorCode(final String errorCode) {
    if (job != null) {
      job.setErrorCode(errorCode);
    }
    return new ThrowErrorCommandStep2Stub(job);
  }
}
