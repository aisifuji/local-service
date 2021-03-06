/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.herocheer.zhsq.localservice.core.device.box.YTBox.pojo;

import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class ErrorResponse extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -3276509176673275165L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ErrorResponse\",\"namespace\":\"com.herocheer.zhsq.localservice.core.device.box.YTBox.pojo\",\"fields\":[{\"name\":\"reason\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public String reason;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ErrorResponse() {}

  /**
   * All-args constructor.
   * @param reason The new value for reason
   */
  public ErrorResponse(String reason) {
    this.reason = reason;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public Object get(int field$) {
    switch (field$) {
    case 0: return reason;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, Object value$) {
    switch (field$) {
    case 0: reason = (String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'reason' field.
   * @return The value of the 'reason' field.
   */
  public String getReason() {
    return reason;
  }

  /**
   * Sets the value of the 'reason' field.
   * @param value the value to set.
   */
  public void setReason(String value) {
    this.reason = value;
  }

  /**
   * Creates a new ErrorResponse RecordBuilder.
   * @return A new ErrorResponse RecordBuilder
   */
  public static ErrorResponse.Builder newBuilder() {
    return new ErrorResponse.Builder();
  }

  /**
   * Creates a new ErrorResponse RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ErrorResponse RecordBuilder
   */
  public static ErrorResponse.Builder newBuilder(ErrorResponse.Builder other) {
    return new ErrorResponse.Builder(other);
  }

  /**
   * Creates a new ErrorResponse RecordBuilder by copying an existing ErrorResponse instance.
   * @param other The existing instance to copy.
   * @return A new ErrorResponse RecordBuilder
   */
  public static ErrorResponse.Builder newBuilder(ErrorResponse other) {
    return new ErrorResponse.Builder(other);
  }

  /**
   * RecordBuilder for ErrorResponse instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ErrorResponse>
    implements org.apache.avro.data.RecordBuilder<ErrorResponse> {

    private String reason;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(ErrorResponse.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.reason)) {
        this.reason = data().deepCopy(fields()[0].schema(), other.reason);
        fieldSetFlags()[0] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing ErrorResponse instance
     * @param other The existing instance to copy.
     */
    private Builder(ErrorResponse other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.reason)) {
        this.reason = data().deepCopy(fields()[0].schema(), other.reason);
        fieldSetFlags()[0] = true;
      }
    }

    /**
      * Gets the value of the 'reason' field.
      * @return The value.
      */
    public String getReason() {
      return reason;
    }

    /**
      * Sets the value of the 'reason' field.
      * @param value The value of 'reason'.
      * @return This builder.
      */
    public ErrorResponse.Builder setReason(String value) {
      validate(fields()[0], value);
      this.reason = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'reason' field has been set.
      * @return True if the 'reason' field has been set, false otherwise.
      */
    public boolean hasReason() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'reason' field.
      * @return This builder.
      */
    public ErrorResponse.Builder clearReason() {
      reason = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    public ErrorResponse build() {
      try {
        ErrorResponse record = new ErrorResponse();
        record.reason = fieldSetFlags()[0] ? this.reason : (String) defaultValue(fields()[0]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  private static final org.apache.avro.io.DatumWriter
    WRITER$ = new org.apache.avro.specific.SpecificDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  private static final org.apache.avro.io.DatumReader
    READER$ = new org.apache.avro.specific.SpecificDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
