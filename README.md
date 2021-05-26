[![](https://img.shields.io/badge/Lifecycle-Proof%20of%20Concept-blueviolet)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#proof-of-concept-)
[![Community extension badge](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)

# Zeebe Worker Java Test Utilities

Utilities to test Zeebe workers implemented in Java.

This library makes the implementation of unit tests for Zeebe workers easier:

```java
public class DivisionHandlerTest {

  private final DivisionHandler sutDivisionHandler = new DivisionHandler();

  @Test
  public void shouldDivideFourByTwo() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();

    stubActivatedJob.setInputVariables(Map.of("a", 4L, "b", 2L));
    // set other fields as required
    //   stubActivatedJob.setBpmnProcessId("division_process");
    //   stubActivatedJob.setRetries(5);
    //   stubActivatedJob.setWorker("division_worker");
    //   ...

    // when
    sutDivisionHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob).completed().extractingOutput().containsOnly(entry("result", 2d));
  }

  @Test
  public void shouldThrowErrorWhenDividingByZero() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();

    stubActivatedJob.setInputVariables(Map.of("a", 4L, "b", 0L));

    // when
    sutDivisionHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob)
      .threwError()
      .hasErrorCode("division-by-zero")
      .extractingErrorMessage()
      .containsIgnoringCase("cannot divide")
      .containsIgnoringCase("by zero");
  }

  @Test
  public void shouldFailWhenInputInvalid() {
    // given
    final var stubJobClient = new JobClientStub();
    final var stubActivatedJob = stubJobClient.createActivatedJob();

    stubActivatedJob.setInputVariables(Map.of("a", 4L, "b", "INVALID INPUT"));

    // when
    sutDivisionHandler.handle(stubJobClient, stubActivatedJob);

    // then
    assertThat(stubActivatedJob).failed().hasRetries(0).hasErrorMessage("exception occurred");
  }
}
```

Look at `samples` package to see more examples.

## Current Status

* Working on a proof of concept
* Next step is to use this library for testing and refine the assertion API

## Things to Know

* calling `requestTimeout(...)` has no effect at all on any of the stubs
* Exceptions thrown by aim to be class-compatible with the original exceptions
  * They won't, however, have the exact same messages as the real ones
  * Likewise, they have a different caused-by chain very different stacktraces
