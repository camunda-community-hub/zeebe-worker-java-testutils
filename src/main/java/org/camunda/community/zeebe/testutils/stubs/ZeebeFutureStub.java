package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.zeebe.client.api.ZeebeFuture;
import io.camunda.zeebe.client.api.command.ClientException;
import io.camunda.zeebe.client.api.command.ClientStatusException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class ZeebeFutureStub<CLIENT_RESPONSE> extends CompletableFuture<CLIENT_RESPONSE>
    implements ZeebeFuture<CLIENT_RESPONSE> {

  @Override
  public CLIENT_RESPONSE join(final long timeout, final TimeUnit unit) {
    try {
      return get(timeout, unit);
    } catch (final ExecutionException var5) {
      throw transformExecutionException(var5);
    } catch (final InterruptedException var6) {
      throw new ClientException("Unexpectedly interrupted awaiting client response", var6);
    } catch (final TimeoutException var7) {
      throw new ClientException("Timed out waiting on client response", var7);
    }
  }

  private RuntimeException transformExecutionException(final ExecutionException e) {
    final Throwable cause = e.getCause();
    if (cause instanceof StatusRuntimeException) {
      final Status status = ((StatusRuntimeException) cause).getStatus();
      throw new ClientStatusException(status, e);
    } else {
      throw new ClientException(e);
    }
  }
}
