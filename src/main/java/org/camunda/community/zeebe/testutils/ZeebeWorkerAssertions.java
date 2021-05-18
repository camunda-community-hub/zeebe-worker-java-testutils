package org.camunda.community.zeebe.testutils;

import org.camunda.community.zeebe.testutils.stubs.ActivatedJobStub;

public class ZeebeWorkerAssertions {

  public static ActivatedJobStubAssert assertThat(final ActivatedJobStub actual) {
    return new ActivatedJobStubAssert(actual);
  }
}
