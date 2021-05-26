package org.camunda.community.zeebe.testutils.stubs;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.ExecutionException;

public class ExceptionBehavior {

  public static ExecutionException buildNotFoundException() {
    return new ExecutionException(
        "Replacement exception message for job not found",
        new StatusRuntimeException(Status.NOT_FOUND));
  }
}
