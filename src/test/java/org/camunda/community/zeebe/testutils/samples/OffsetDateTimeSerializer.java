package org.camunda.community.zeebe.testutils.samples;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd HHmmss XXX");

  @Override
  public void serialize(
      OffsetDateTime value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (value == null) {
      jsonGenerator.writeNull();
    } else {
      jsonGenerator.writeString(DATE_TIME_FORMATTER.format(value));
    }
  }
}
