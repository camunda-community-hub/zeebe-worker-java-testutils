package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.zeebe.client.api.command.CompleteJobCommandStep1;
import io.camunda.zeebe.client.api.command.FailJobCommandStep1;
import io.camunda.zeebe.client.api.command.ThrowErrorCommandStep1;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class JobClientStub implements JobClient {

  private static final AtomicLong counter = new AtomicLong();

  private final Map<Long, ActivatedJobStub> activatedJobs = new ConcurrentHashMap<>();

  @Override
  public CompleteJobCommandStep1 newCompleteCommand(final long jobKey) {
    return new CompleteJobCommandStep1Stub(takeJob(jobKey));
  }

  @Override
  public CompleteJobCommandStep1 newCompleteCommand(final ActivatedJob job) {
    return newCompleteCommand(job.getKey());
  }

  @Override
  public FailJobCommandStep1 newFailCommand(final long jobKey) {
    return new FailJobCommandStep1Stub(takeJob(jobKey));
  }

  @Override
  public FailJobCommandStep1 newFailCommand(final ActivatedJob job) {
    return newFailCommand(job.getKey());
  }

  @Override
  public ThrowErrorCommandStep1 newThrowErrorCommand(final long jobKey) {
    return new ThrowErrorCommandStep1Stub(takeJob(jobKey));
  }

  @Override
  public ThrowErrorCommandStep1 newThrowErrorCommand(final ActivatedJob job) {
    return newThrowErrorCommand(job.getKey());
  }

  private ActivatedJobStub takeJob(final long jobKey) {
    return activatedJobs.remove(jobKey);
  }

  public ActivatedJobStub createActivatedJob() {
    final var result = new ActivatedJobStub(counter.incrementAndGet());

    activatedJobs.put(result.getKey(), result);

    return result;
  }
}
