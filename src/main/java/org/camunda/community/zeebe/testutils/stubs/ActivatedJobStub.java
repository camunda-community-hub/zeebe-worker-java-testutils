package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import java.util.HashMap;
import java.util.Map;

// TODO implement all the getters and corresponding setters
public class ActivatedJobStub implements ActivatedJob {

  private final long key;
  private Map<String, Object> inputVariables = new HashMap<>();
  private Map<String, Object> outputVariables = new HashMap<>();
  private Status status = Status.ACTIVATED;
  private String errorMessage;
  private int remainingRetries;
  private String errorCode;

  public ActivatedJobStub(final long key) {
    this.key = key;
  }

  @Override
  public long getKey() {
    return key;
  }

  @Override
  public String getType() {
    return null;
  }

  @Override
  public long getProcessInstanceKey() {
    return 0;
  }

  @Override
  public String getBpmnProcessId() {
    return null;
  }

  @Override
  public int getProcessDefinitionVersion() {
    return 0;
  }

  @Override
  public long getProcessDefinitionKey() {
    return 0;
  }

  @Override
  public String getElementId() {
    return null;
  }

  @Override
  public long getElementInstanceKey() {
    return 0;
  }

  @Override
  public Map<String, String> getCustomHeaders() {
    return null;
  }

  @Override
  public String getWorker() {
    return null;
  }

  @Override
  public int getRetries() {
    return 0;
  }

  @Override
  public long getDeadline() {
    return 0;
  }

  @Override
  public String getVariables() {
    return null;
  }

  @Override
  public Map<String, Object> getVariablesAsMap() {
    return inputVariables;
  }

  @Override
  public <T> T getVariablesAsType(final Class<T> variableType) {
    return null;
  }

  @Override
  public String toJson() {
    return null;
  }

  public Status getStatus() {
    return status;
  }

  public boolean isCompleted() {
    return status == Status.COMPLETED;
  }

  protected void setCompleted() {
    status = Status.COMPLETED;
  }

  public boolean isFailed() {
    return status == Status.FAILED;
  }

  protected void setFailed() {
    status = Status.FAILED;
  }

  public boolean hasThrownError() {
    return status == Status.ERROR_THROWN;
  }

  protected void setErrorThrown() {
    status = Status.ERROR_THROWN;
  }

  public Map<String, Object> getInputVariables() {
    return getVariablesAsMap();
  }

  public void setInputVariables(final Map<String, Object> variables) {
    inputVariables = Map.copyOf(variables);
  }

  public Map<String, Object> getOutputVariables() {
    return outputVariables;
  }

  protected void setOutputVariables(final Map<String, Object> variables) {
    outputVariables = Map.copyOf(variables);
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(final String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public int getRemainingRetries() {
    return remainingRetries;
  }

  public void setRemainingRetries(final int remainingRetries) {
    this.remainingRetries = remainingRetries;
  }

  public String getErrorCode() {
    return errorCode;
  }

  protected void setErrorCode(final String errorCode) {
    this.errorCode = errorCode;
  }

  public enum Status {
    ACTIVATED,
    COMPLETED,
    FAILED,
    ERROR_THROWN
  }
}
