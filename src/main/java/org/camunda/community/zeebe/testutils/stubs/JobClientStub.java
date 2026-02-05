package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.client.api.command.*;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
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

  @Override
  public ActivateJobsCommandStep1 newActivateJobsCommand() {
    return null;
  }

  @Override
  public StreamJobsCommandStep1 newStreamJobsCommand() {
    return null;
  }

  private ActivatedJobStub takeJob(final long jobKey) {
    return activatedJobs.remove(jobKey);
  }

  public ActivatedJobStub createActivatedJob() {
    final ActivatedJobStub result = new ActivatedJobStub(counter.incrementAndGet());

    activatedJobs.put(result.getKey(), result);

    return result;
  }
}
