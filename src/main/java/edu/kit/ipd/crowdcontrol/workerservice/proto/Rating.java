// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: rating.proto

package edu.kit.ipd.crowdcontrol.workerservice.proto;

/**
 * Protobuf type {@code crowdcontrol.Rating}
 */
public  final class Rating extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:crowdcontrol.Rating)
    RatingOrBuilder {
  // Use Rating.newBuilder() to construct.
  private Rating(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private Rating() {
    rating_ = 0;
    task_ = 0;
    answerId_ = 0;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private Rating(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry) {
    this();
    int mutable_bitField0_ = 0;
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
          case 8: {

            rating_ = input.readInt32();
            break;
          }
          case 16: {

            task_ = input.readInt32();
            break;
          }
          case 24: {

            answerId_ = input.readInt32();
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw new RuntimeException(e.setUnfinishedMessage(this));
    } catch (java.io.IOException e) {
      throw new RuntimeException(
          new com.google.protobuf.InvalidProtocolBufferException(
              e.getMessage()).setUnfinishedMessage(this));
    } finally {
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return edu.kit.ipd.crowdcontrol.workerservice.proto.RatingOuterClass.internal_static_crowdcontrol_Rating_descriptor;
  }

  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return edu.kit.ipd.crowdcontrol.workerservice.proto.RatingOuterClass.internal_static_crowdcontrol_Rating_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            edu.kit.ipd.crowdcontrol.workerservice.proto.Rating.class, edu.kit.ipd.crowdcontrol.workerservice.proto.Rating.Builder.class);
  }

  public static final int RATING_FIELD_NUMBER = 1;
  private int rating_;
  /**
   * <code>optional int32 rating = 1;</code>
   */
  public int getRating() {
    return rating_;
  }

  public static final int TASK_FIELD_NUMBER = 2;
  private int task_;
  /**
   * <code>optional int32 task = 2;</code>
   */
  public int getTask() {
    return task_;
  }

  public static final int ANSWER_ID_FIELD_NUMBER = 3;
  private int answerId_;
  /**
   * <code>optional int32 answer_id = 3;</code>
   */
  public int getAnswerId() {
    return answerId_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (rating_ != 0) {
      output.writeInt32(1, rating_);
    }
    if (task_ != 0) {
      output.writeInt32(2, task_);
    }
    if (answerId_ != 0) {
      output.writeInt32(3, answerId_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (rating_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, rating_);
    }
    if (task_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, task_);
    }
    if (answerId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, answerId_);
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }
  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input);
  }
  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input, extensionRegistry);
  }
  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(edu.kit.ipd.crowdcontrol.workerservice.proto.Rating prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code crowdcontrol.Rating}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:crowdcontrol.Rating)
      edu.kit.ipd.crowdcontrol.workerservice.proto.RatingOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return edu.kit.ipd.crowdcontrol.workerservice.proto.RatingOuterClass.internal_static_crowdcontrol_Rating_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return edu.kit.ipd.crowdcontrol.workerservice.proto.RatingOuterClass.internal_static_crowdcontrol_Rating_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              edu.kit.ipd.crowdcontrol.workerservice.proto.Rating.class, edu.kit.ipd.crowdcontrol.workerservice.proto.Rating.Builder.class);
    }

    // Construct using edu.kit.ipd.crowdcontrol.workerservice.proto.Rating.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      rating_ = 0;

      task_ = 0;

      answerId_ = 0;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return edu.kit.ipd.crowdcontrol.workerservice.proto.RatingOuterClass.internal_static_crowdcontrol_Rating_descriptor;
    }

    public edu.kit.ipd.crowdcontrol.workerservice.proto.Rating getDefaultInstanceForType() {
      return edu.kit.ipd.crowdcontrol.workerservice.proto.Rating.getDefaultInstance();
    }

    public edu.kit.ipd.crowdcontrol.workerservice.proto.Rating build() {
      edu.kit.ipd.crowdcontrol.workerservice.proto.Rating result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public edu.kit.ipd.crowdcontrol.workerservice.proto.Rating buildPartial() {
      edu.kit.ipd.crowdcontrol.workerservice.proto.Rating result = new edu.kit.ipd.crowdcontrol.workerservice.proto.Rating(this);
      result.rating_ = rating_;
      result.task_ = task_;
      result.answerId_ = answerId_;
      onBuilt();
      return result;
    }

    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof edu.kit.ipd.crowdcontrol.workerservice.proto.Rating) {
        return mergeFrom((edu.kit.ipd.crowdcontrol.workerservice.proto.Rating)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(edu.kit.ipd.crowdcontrol.workerservice.proto.Rating other) {
      if (other == edu.kit.ipd.crowdcontrol.workerservice.proto.Rating.getDefaultInstance()) return this;
      if (other.getRating() != 0) {
        setRating(other.getRating());
      }
      if (other.getTask() != 0) {
        setTask(other.getTask());
      }
      if (other.getAnswerId() != 0) {
        setAnswerId(other.getAnswerId());
      }
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      edu.kit.ipd.crowdcontrol.workerservice.proto.Rating parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (edu.kit.ipd.crowdcontrol.workerservice.proto.Rating) e.getUnfinishedMessage();
        throw e;
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int rating_ ;
    /**
     * <code>optional int32 rating = 1;</code>
     */
    public int getRating() {
      return rating_;
    }
    /**
     * <code>optional int32 rating = 1;</code>
     */
    public Builder setRating(int value) {
      
      rating_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 rating = 1;</code>
     */
    public Builder clearRating() {
      
      rating_ = 0;
      onChanged();
      return this;
    }

    private int task_ ;
    /**
     * <code>optional int32 task = 2;</code>
     */
    public int getTask() {
      return task_;
    }
    /**
     * <code>optional int32 task = 2;</code>
     */
    public Builder setTask(int value) {
      
      task_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 task = 2;</code>
     */
    public Builder clearTask() {
      
      task_ = 0;
      onChanged();
      return this;
    }

    private int answerId_ ;
    /**
     * <code>optional int32 answer_id = 3;</code>
     */
    public int getAnswerId() {
      return answerId_;
    }
    /**
     * <code>optional int32 answer_id = 3;</code>
     */
    public Builder setAnswerId(int value) {
      
      answerId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 answer_id = 3;</code>
     */
    public Builder clearAnswerId() {
      
      answerId_ = 0;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:crowdcontrol.Rating)
  }

  // @@protoc_insertion_point(class_scope:crowdcontrol.Rating)
  private static final edu.kit.ipd.crowdcontrol.workerservice.proto.Rating DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new edu.kit.ipd.crowdcontrol.workerservice.proto.Rating();
  }

  public static edu.kit.ipd.crowdcontrol.workerservice.proto.Rating getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Rating>
      PARSER = new com.google.protobuf.AbstractParser<Rating>() {
    public Rating parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      try {
        return new Rating(input, extensionRegistry);
      } catch (RuntimeException e) {
        if (e.getCause() instanceof
            com.google.protobuf.InvalidProtocolBufferException) {
          throw (com.google.protobuf.InvalidProtocolBufferException)
              e.getCause();
        }
        throw e;
      }
    }
  };

  public static com.google.protobuf.Parser<Rating> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Rating> getParserForType() {
    return PARSER;
  }

  public edu.kit.ipd.crowdcontrol.workerservice.proto.Rating getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

