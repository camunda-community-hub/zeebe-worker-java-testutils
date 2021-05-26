package org.camunda.community.zeebe.testutils;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.MapAssert;
import org.camunda.community.zeebe.testutils.stubs.ActivatedJobStub;
import org.camunda.community.zeebe.testutils.stubs.ActivatedJobStub.Status;

public class ActivatedJobStubAssert
    extends AbstractAssert<ActivatedJobStubAssert, ActivatedJobStub> {

  protected ActivatedJobStubAssert(final ActivatedJobStub actual) {
    super(actual, ActivatedJobStubAssert.class);
  }

  private void failWithMessageWrongStatus(final Status expected) {
    failWithMessage(
        "Expected job with key "
            + actual.getKey()
            + " to be "
            + expected.name()
            + " completed, but was "
            + actual.getStatus().name());
  }

  public CompletedActivatedJobStubAssert completed() {
    if (!actual.isCompleted()) {
      failWithMessageWrongStatus(Status.COMPLETED);
    }
    return new CompletedActivatedJobStubAssert(actual);
  }

  public FailedActivatedJobStubAssert failed() {
    if (!actual.isFailed()) {
      failWithMessageWrongStatus(Status.FAILED);
    }

    return new FailedActivatedJobStubAssert(actual);
  }

  public ErrorThrownActivatedJobStubAssert threwError() {
    if (!actual.hasThrownError()) {
      failWithMessageWrongStatus(Status.ERROR_THROWN);
    }

    return new ErrorThrownActivatedJobStubAssert(actual);
  }

  public void isStillActivated() {
    if (!(actual.getStatus() == Status.ACTIVATED)) {
      failWithMessageWrongStatus(Status.ACTIVATED);
    }
  }

  public static class CompletedActivatedJobStubAssert
      extends AbstractAssert<ActivatedJobStubAssert, ActivatedJobStub> {

    protected CompletedActivatedJobStubAssert(final ActivatedJobStub activatedJobStub) {
      super(activatedJobStub, CompletedActivatedJobStubAssert.class);
    }

    public MapAssert<String, Object> withOutputThat() {
      return assertThat(actual.getOutputVariables())
          .describedAs("Output variables for job " + actual.getKey());
    }
  }

  public static class FailedActivatedJobStubAssert
      extends AbstractAssert<ActivatedJobStubAssert, ActivatedJobStub> {

    protected FailedActivatedJobStubAssert(final ActivatedJobStub activatedJobStub) {
      super(activatedJobStub, FailedActivatedJobStubAssert.class);
    }

    public FailedActivatedJobStubAssert hasErrorMessage(final String expected) {

      assertThat(actual.getErrorMessage())
          .describedAs("Error message for job " + actual.getKey())
          .isEqualTo(expected);

      return this;
    }

    public FailedActivatedJobStubAssert hasRetries(final int expected) {

      assertThat(actual.getRemainingRetries())
          .describedAs("Remaining retries for job " + actual.getKey())
          .isEqualTo(expected);

      return this;
    }
  }

  public class ErrorThrownActivatedJobStubAssert
      extends AbstractAssert<ActivatedJobStubAssert, ActivatedJobStub> {

    public ErrorThrownActivatedJobStubAssert(final ActivatedJobStub actual) {
      super(actual, ErrorThrownActivatedJobStubAssert.class);
    }

    public ErrorThrownActivatedJobStubAssert hasErrorCode(final String expected) {

      assertThat(actual.getErrorCode())
          .describedAs("Error code for job " + actual.getKey())
          .isEqualTo(expected);

      return this;
    }

    public ErrorThrownActivatedJobStubAssert hasErrorMessage(final String expected) {

      assertThat(actual.getErrorMessage())
          .describedAs("Error message for job " + actual.getKey())
          .isEqualTo(expected);

      return this;
    }
  }
}
