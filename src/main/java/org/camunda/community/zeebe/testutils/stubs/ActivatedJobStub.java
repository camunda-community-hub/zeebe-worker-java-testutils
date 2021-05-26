package org.camunda.community.zeebe.testutils.stubs;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.impl.ZeebeObjectMapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Stub object for {@code ActivatedJob}. It contains setters for all getters defined in the
 * interface.
 *
 * <p>Plus it contains additional getters that are only available once a job has been worked on.
 *
 * <p>{@code getStatus()}
 *
 * <ul>
 *   <li>{@code ACTIVATED} - initial state. Either the job has not been worked on yet, or it has
 *       been worked on, but no result was submitted via {@code JobClient}
 *   <li>{@code COMPLETED} - job was completed. This unlocks:
 *       <ul>
 *         <li>{@code getOutputVariables()} - the variables set on completion of the job
 *       </ul>
 *   <li>{@code FAILED} - job failed. This unlocks:
 *       <ul>
 *         <li>{@code getErrorMessage()} - error message of the job
 *         <li>{@code getRemainingRetries()} - remaining retries for the job
 *       </ul>
 *   <li>{@code ERROR_THROWN} - an error was thrown. This unlocks
 *       <ul>
 *         <li>{@code getErrorMessage()}
 *         <li>{@code getErrorCode()}
 *       </ul>
 * </ul>
 */
public class ActivatedJobStub implements ActivatedJob {

  private static final ZeebeObjectMapper JSON_MAPPER = new ZeebeObjectMapper();

  // variables available at the outset
  private Status status = Status.ACTIVATED;
  private Map<String, Object> inputVariables = new HashMap<>();
  private final long key;
  private String type = "jobType";
  private long processInstanceKey = 0;
  private String bpmnProcessId = "bpmnProcessId";
  private int processDefinitionVersion = 0;
  private int processDefinitionKey = 0;
  private String elementId = "serviceTask1";
  private long elementInstanceKey = 0;
  private Map<String, String> customHeaders = new HashMap<>();
  private String worker = "worker";
  private int retries = 1;
  private long deadline = 10_000L;

  // variables available after working on a job
  private Map<String, Object> outputVariables = new HashMap<>();
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
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  @Override
  public long getProcessInstanceKey() {
    return processInstanceKey;
  }

  public void setProcessInstanceKey(final long processInstanceKey) {
    this.processInstanceKey = processInstanceKey;
  }

  @Override
  public String getBpmnProcessId() {
    return bpmnProcessId;
  }

  public void setBpmnProcessId(final String bpmnProcessId) {
    this.bpmnProcessId = bpmnProcessId;
  }

  @Override
  public int getProcessDefinitionVersion() {
    return processDefinitionVersion;
  }

  public void setProcessDefinitionVersion(final int processDefinitionVersion) {
    this.processDefinitionVersion = processDefinitionVersion;
  }

  @Override
  public long getProcessDefinitionKey() {
    return processDefinitionKey;
  }

  public void setProcessDefinitionKey(final int processDefinitionKey) {
    this.processDefinitionKey = processDefinitionKey;
  }

  @Override
  public String getElementId() {
    return elementId;
  }

  public void setElementId(final String elementId) {
    this.elementId = elementId;
  }

  @Override
  public long getElementInstanceKey() {
    return elementInstanceKey;
  }

  public void setElementInstanceKey(final long elementInstanceKey) {
    this.elementInstanceKey = elementInstanceKey;
  }

  @Override
  public Map<String, String> getCustomHeaders() {
    return customHeaders;
  }

  public void setCustomHeaders(final Map<String, String> customHeaders) {
    this.customHeaders = customHeaders;
  }

  @Override
  public String getWorker() {
    return worker;
  }

  public void setWorker(final String worker) {
    this.worker = worker;
  }

  @Override
  public int getRetries() {
    return retries;
  }

  public void setRetries(final int retries) {
    this.retries = retries;
  }

  @Override
  public long getDeadline() {
    return deadline;
  }

  public void setDeadline(final long deadline) {
    this.deadline = deadline;
  }

  @Override
  public String getVariables() {
    return JSON_MAPPER.toJson(inputVariables);
  }

  @Override
  public Map<String, Object> getVariablesAsMap() {
    return inputVariables;
  }

  @Override
  public <T> T getVariablesAsType(final Class<T> variableType) {
    return JSON_MAPPER.fromJson(getVariables(), variableType);
  }

  @Override
  public String toJson() {
    return JSON_MAPPER.toJson(this);
  }

  @Override
  public String toString() {
    return toJson();
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
