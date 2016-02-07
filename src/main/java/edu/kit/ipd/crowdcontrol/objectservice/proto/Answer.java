// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: answer.proto

package edu.kit.ipd.crowdcontrol.objectservice.proto;

/**
 * Protobuf type {@code crowdcontrol.Answer}
 */
public  final class Answer extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:crowdcontrol.Answer)
    AnswerOrBuilder {
  // Use Answer.newBuilder() to construct.
  private Answer(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private Answer() {
    id_ = 0;
    experimentId_ = 0;
    worker_ = 0;
    content_ = "";
    time_ = 0;
    quality_ = 0;
    ratings_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private Answer(
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

            id_ = input.readInt32();
            break;
          }
          case 16: {

            experimentId_ = input.readInt32();
            break;
          }
          case 24: {

            worker_ = input.readInt32();
            break;
          }
          case 34: {
            java.lang.String s = input.readStringRequireUtf8();

            content_ = s;
            break;
          }
          case 40: {

            time_ = input.readInt32();
            break;
          }
          case 48: {

            quality_ = input.readInt32();
            break;
          }
          case 58: {
            if (!((mutable_bitField0_ & 0x00000040) == 0x00000040)) {
              ratings_ = new java.util.ArrayList<edu.kit.ipd.crowdcontrol.objectservice.proto.Rating>();
              mutable_bitField0_ |= 0x00000040;
            }
            ratings_.add(input.readMessage(edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.parser(), extensionRegistry));
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
      if (((mutable_bitField0_ & 0x00000040) == 0x00000040)) {
        ratings_ = java.util.Collections.unmodifiableList(ratings_);
      }
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return edu.kit.ipd.crowdcontrol.objectservice.proto.AnswerOuterClass.internal_static_crowdcontrol_Answer_descriptor;
  }

  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return edu.kit.ipd.crowdcontrol.objectservice.proto.AnswerOuterClass.internal_static_crowdcontrol_Answer_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            edu.kit.ipd.crowdcontrol.objectservice.proto.Answer.class, edu.kit.ipd.crowdcontrol.objectservice.proto.Answer.Builder.class);
  }

  private int bitField0_;
  public static final int ID_FIELD_NUMBER = 1;
  private int id_;
  /**
   * <code>optional int32 id = 1;</code>
   */
  public int getId() {
    return id_;
  }

  public static final int EXPERIMENT_ID_FIELD_NUMBER = 2;
  private int experimentId_;
  /**
   * <code>optional int32 experiment_id = 2;</code>
   */
  public int getExperimentId() {
    return experimentId_;
  }

  public static final int WORKER_FIELD_NUMBER = 3;
  private int worker_;
  /**
   * <code>optional int32 worker = 3;</code>
   */
  public int getWorker() {
    return worker_;
  }

  public static final int CONTENT_FIELD_NUMBER = 4;
  private volatile java.lang.Object content_;
  /**
   * <code>optional string content = 4;</code>
   */
  public java.lang.String getContent() {
    java.lang.Object ref = content_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      content_ = s;
      return s;
    }
  }
  /**
   * <code>optional string content = 4;</code>
   */
  public com.google.protobuf.ByteString
      getContentBytes() {
    java.lang.Object ref = content_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      content_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int TIME_FIELD_NUMBER = 5;
  private int time_;
  /**
   * <code>optional int32 time = 5;</code>
   */
  public int getTime() {
    return time_;
  }

  public static final int QUALITY_FIELD_NUMBER = 6;
  private int quality_;
  /**
   * <code>optional int32 quality = 6;</code>
   */
  public int getQuality() {
    return quality_;
  }

  public static final int RATINGS_FIELD_NUMBER = 7;
  private java.util.List<edu.kit.ipd.crowdcontrol.objectservice.proto.Rating> ratings_;
  /**
   * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
   */
  public java.util.List<edu.kit.ipd.crowdcontrol.objectservice.proto.Rating> getRatingsList() {
    return ratings_;
  }
  /**
   * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
   */
  public java.util.List<? extends edu.kit.ipd.crowdcontrol.objectservice.proto.RatingOrBuilder> 
      getRatingsOrBuilderList() {
    return ratings_;
  }
  /**
   * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
   */
  public int getRatingsCount() {
    return ratings_.size();
  }
  /**
   * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
   */
  public edu.kit.ipd.crowdcontrol.objectservice.proto.Rating getRatings(int index) {
    return ratings_.get(index);
  }
  /**
   * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
   */
  public edu.kit.ipd.crowdcontrol.objectservice.proto.RatingOrBuilder getRatingsOrBuilder(
      int index) {
    return ratings_.get(index);
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
    if (id_ != 0) {
      output.writeInt32(1, id_);
    }
    if (experimentId_ != 0) {
      output.writeInt32(2, experimentId_);
    }
    if (worker_ != 0) {
      output.writeInt32(3, worker_);
    }
    if (!getContentBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessage.writeString(output, 4, content_);
    }
    if (time_ != 0) {
      output.writeInt32(5, time_);
    }
    if (quality_ != 0) {
      output.writeInt32(6, quality_);
    }
    for (int i = 0; i < ratings_.size(); i++) {
      output.writeMessage(7, ratings_.get(i));
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (id_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, id_);
    }
    if (experimentId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, experimentId_);
    }
    if (worker_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, worker_);
    }
    if (!getContentBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(4, content_);
    }
    if (time_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(5, time_);
    }
    if (quality_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(6, quality_);
    }
    for (int i = 0; i < ratings_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(7, ratings_.get(i));
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }
  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input);
  }
  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input, extensionRegistry);
  }
  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(edu.kit.ipd.crowdcontrol.objectservice.proto.Answer prototype) {
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
   * Protobuf type {@code crowdcontrol.Answer}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:crowdcontrol.Answer)
      edu.kit.ipd.crowdcontrol.objectservice.proto.AnswerOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return edu.kit.ipd.crowdcontrol.objectservice.proto.AnswerOuterClass.internal_static_crowdcontrol_Answer_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return edu.kit.ipd.crowdcontrol.objectservice.proto.AnswerOuterClass.internal_static_crowdcontrol_Answer_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              edu.kit.ipd.crowdcontrol.objectservice.proto.Answer.class, edu.kit.ipd.crowdcontrol.objectservice.proto.Answer.Builder.class);
    }

    // Construct using edu.kit.ipd.crowdcontrol.objectservice.proto.Answer.newBuilder()
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
        getRatingsFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      id_ = 0;

      experimentId_ = 0;

      worker_ = 0;

      content_ = "";

      time_ = 0;

      quality_ = 0;

      if (ratingsBuilder_ == null) {
        ratings_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000040);
      } else {
        ratingsBuilder_.clear();
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return edu.kit.ipd.crowdcontrol.objectservice.proto.AnswerOuterClass.internal_static_crowdcontrol_Answer_descriptor;
    }

    public edu.kit.ipd.crowdcontrol.objectservice.proto.Answer getDefaultInstanceForType() {
      return edu.kit.ipd.crowdcontrol.objectservice.proto.Answer.getDefaultInstance();
    }

    public edu.kit.ipd.crowdcontrol.objectservice.proto.Answer build() {
      edu.kit.ipd.crowdcontrol.objectservice.proto.Answer result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public edu.kit.ipd.crowdcontrol.objectservice.proto.Answer buildPartial() {
      edu.kit.ipd.crowdcontrol.objectservice.proto.Answer result = new edu.kit.ipd.crowdcontrol.objectservice.proto.Answer(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      result.id_ = id_;
      result.experimentId_ = experimentId_;
      result.worker_ = worker_;
      result.content_ = content_;
      result.time_ = time_;
      result.quality_ = quality_;
      if (ratingsBuilder_ == null) {
        if (((bitField0_ & 0x00000040) == 0x00000040)) {
          ratings_ = java.util.Collections.unmodifiableList(ratings_);
          bitField0_ = (bitField0_ & ~0x00000040);
        }
        result.ratings_ = ratings_;
      } else {
        result.ratings_ = ratingsBuilder_.build();
      }
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof edu.kit.ipd.crowdcontrol.objectservice.proto.Answer) {
        return mergeFrom((edu.kit.ipd.crowdcontrol.objectservice.proto.Answer)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(edu.kit.ipd.crowdcontrol.objectservice.proto.Answer other) {
      if (other == edu.kit.ipd.crowdcontrol.objectservice.proto.Answer.getDefaultInstance()) return this;
      if (other.getId() != 0) {
        setId(other.getId());
      }
      if (other.getExperimentId() != 0) {
        setExperimentId(other.getExperimentId());
      }
      if (other.getWorker() != 0) {
        setWorker(other.getWorker());
      }
      if (!other.getContent().isEmpty()) {
        content_ = other.content_;
        onChanged();
      }
      if (other.getTime() != 0) {
        setTime(other.getTime());
      }
      if (other.getQuality() != 0) {
        setQuality(other.getQuality());
      }
      if (ratingsBuilder_ == null) {
        if (!other.ratings_.isEmpty()) {
          if (ratings_.isEmpty()) {
            ratings_ = other.ratings_;
            bitField0_ = (bitField0_ & ~0x00000040);
          } else {
            ensureRatingsIsMutable();
            ratings_.addAll(other.ratings_);
          }
          onChanged();
        }
      } else {
        if (!other.ratings_.isEmpty()) {
          if (ratingsBuilder_.isEmpty()) {
            ratingsBuilder_.dispose();
            ratingsBuilder_ = null;
            ratings_ = other.ratings_;
            bitField0_ = (bitField0_ & ~0x00000040);
            ratingsBuilder_ = 
              com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                 getRatingsFieldBuilder() : null;
          } else {
            ratingsBuilder_.addAllMessages(other.ratings_);
          }
        }
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
      edu.kit.ipd.crowdcontrol.objectservice.proto.Answer parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (edu.kit.ipd.crowdcontrol.objectservice.proto.Answer) e.getUnfinishedMessage();
        throw e;
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private int id_ ;
    /**
     * <code>optional int32 id = 1;</code>
     */
    public int getId() {
      return id_;
    }
    /**
     * <code>optional int32 id = 1;</code>
     */
    public Builder setId(int value) {
      
      id_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 id = 1;</code>
     */
    public Builder clearId() {
      
      id_ = 0;
      onChanged();
      return this;
    }

    private int experimentId_ ;
    /**
     * <code>optional int32 experiment_id = 2;</code>
     */
    public int getExperimentId() {
      return experimentId_;
    }
    /**
     * <code>optional int32 experiment_id = 2;</code>
     */
    public Builder setExperimentId(int value) {
      
      experimentId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 experiment_id = 2;</code>
     */
    public Builder clearExperimentId() {
      
      experimentId_ = 0;
      onChanged();
      return this;
    }

    private int worker_ ;
    /**
     * <code>optional int32 worker = 3;</code>
     */
    public int getWorker() {
      return worker_;
    }
    /**
     * <code>optional int32 worker = 3;</code>
     */
    public Builder setWorker(int value) {
      
      worker_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 worker = 3;</code>
     */
    public Builder clearWorker() {
      
      worker_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object content_ = "";
    /**
     * <code>optional string content = 4;</code>
     */
    public java.lang.String getContent() {
      java.lang.Object ref = content_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        content_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string content = 4;</code>
     */
    public com.google.protobuf.ByteString
        getContentBytes() {
      java.lang.Object ref = content_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        content_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string content = 4;</code>
     */
    public Builder setContent(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      content_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional string content = 4;</code>
     */
    public Builder clearContent() {
      
      content_ = getDefaultInstance().getContent();
      onChanged();
      return this;
    }
    /**
     * <code>optional string content = 4;</code>
     */
    public Builder setContentBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      content_ = value;
      onChanged();
      return this;
    }

    private int time_ ;
    /**
     * <code>optional int32 time = 5;</code>
     */
    public int getTime() {
      return time_;
    }
    /**
     * <code>optional int32 time = 5;</code>
     */
    public Builder setTime(int value) {
      
      time_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 time = 5;</code>
     */
    public Builder clearTime() {
      
      time_ = 0;
      onChanged();
      return this;
    }

    private int quality_ ;
    /**
     * <code>optional int32 quality = 6;</code>
     */
    public int getQuality() {
      return quality_;
    }
    /**
     * <code>optional int32 quality = 6;</code>
     */
    public Builder setQuality(int value) {
      
      quality_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 quality = 6;</code>
     */
    public Builder clearQuality() {
      
      quality_ = 0;
      onChanged();
      return this;
    }

    private java.util.List<edu.kit.ipd.crowdcontrol.objectservice.proto.Rating> ratings_ =
      java.util.Collections.emptyList();
    private void ensureRatingsIsMutable() {
      if (!((bitField0_ & 0x00000040) == 0x00000040)) {
        ratings_ = new java.util.ArrayList<edu.kit.ipd.crowdcontrol.objectservice.proto.Rating>(ratings_);
        bitField0_ |= 0x00000040;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilder<
        edu.kit.ipd.crowdcontrol.objectservice.proto.Rating, edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.Builder, edu.kit.ipd.crowdcontrol.objectservice.proto.RatingOrBuilder> ratingsBuilder_;

    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public java.util.List<edu.kit.ipd.crowdcontrol.objectservice.proto.Rating> getRatingsList() {
      if (ratingsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(ratings_);
      } else {
        return ratingsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public int getRatingsCount() {
      if (ratingsBuilder_ == null) {
        return ratings_.size();
      } else {
        return ratingsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public edu.kit.ipd.crowdcontrol.objectservice.proto.Rating getRatings(int index) {
      if (ratingsBuilder_ == null) {
        return ratings_.get(index);
      } else {
        return ratingsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public Builder setRatings(
        int index, edu.kit.ipd.crowdcontrol.objectservice.proto.Rating value) {
      if (ratingsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRatingsIsMutable();
        ratings_.set(index, value);
        onChanged();
      } else {
        ratingsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public Builder setRatings(
        int index, edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.Builder builderForValue) {
      if (ratingsBuilder_ == null) {
        ensureRatingsIsMutable();
        ratings_.set(index, builderForValue.build());
        onChanged();
      } else {
        ratingsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public Builder addRatings(edu.kit.ipd.crowdcontrol.objectservice.proto.Rating value) {
      if (ratingsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRatingsIsMutable();
        ratings_.add(value);
        onChanged();
      } else {
        ratingsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public Builder addRatings(
        int index, edu.kit.ipd.crowdcontrol.objectservice.proto.Rating value) {
      if (ratingsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRatingsIsMutable();
        ratings_.add(index, value);
        onChanged();
      } else {
        ratingsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public Builder addRatings(
        edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.Builder builderForValue) {
      if (ratingsBuilder_ == null) {
        ensureRatingsIsMutable();
        ratings_.add(builderForValue.build());
        onChanged();
      } else {
        ratingsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public Builder addRatings(
        int index, edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.Builder builderForValue) {
      if (ratingsBuilder_ == null) {
        ensureRatingsIsMutable();
        ratings_.add(index, builderForValue.build());
        onChanged();
      } else {
        ratingsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public Builder addAllRatings(
        java.lang.Iterable<? extends edu.kit.ipd.crowdcontrol.objectservice.proto.Rating> values) {
      if (ratingsBuilder_ == null) {
        ensureRatingsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, ratings_);
        onChanged();
      } else {
        ratingsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public Builder clearRatings() {
      if (ratingsBuilder_ == null) {
        ratings_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000040);
        onChanged();
      } else {
        ratingsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public Builder removeRatings(int index) {
      if (ratingsBuilder_ == null) {
        ensureRatingsIsMutable();
        ratings_.remove(index);
        onChanged();
      } else {
        ratingsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.Builder getRatingsBuilder(
        int index) {
      return getRatingsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public edu.kit.ipd.crowdcontrol.objectservice.proto.RatingOrBuilder getRatingsOrBuilder(
        int index) {
      if (ratingsBuilder_ == null) {
        return ratings_.get(index);  } else {
        return ratingsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public java.util.List<? extends edu.kit.ipd.crowdcontrol.objectservice.proto.RatingOrBuilder> 
         getRatingsOrBuilderList() {
      if (ratingsBuilder_ != null) {
        return ratingsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(ratings_);
      }
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.Builder addRatingsBuilder() {
      return getRatingsFieldBuilder().addBuilder(
          edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.getDefaultInstance());
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.Builder addRatingsBuilder(
        int index) {
      return getRatingsFieldBuilder().addBuilder(
          index, edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.getDefaultInstance());
    }
    /**
     * <code>repeated .crowdcontrol.Rating ratings = 7;</code>
     */
    public java.util.List<edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.Builder> 
         getRatingsBuilderList() {
      return getRatingsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilder<
        edu.kit.ipd.crowdcontrol.objectservice.proto.Rating, edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.Builder, edu.kit.ipd.crowdcontrol.objectservice.proto.RatingOrBuilder> 
        getRatingsFieldBuilder() {
      if (ratingsBuilder_ == null) {
        ratingsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
            edu.kit.ipd.crowdcontrol.objectservice.proto.Rating, edu.kit.ipd.crowdcontrol.objectservice.proto.Rating.Builder, edu.kit.ipd.crowdcontrol.objectservice.proto.RatingOrBuilder>(
                ratings_,
                ((bitField0_ & 0x00000040) == 0x00000040),
                getParentForChildren(),
                isClean());
        ratings_ = null;
      }
      return ratingsBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:crowdcontrol.Answer)
  }

  // @@protoc_insertion_point(class_scope:crowdcontrol.Answer)
  private static final edu.kit.ipd.crowdcontrol.objectservice.proto.Answer DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new edu.kit.ipd.crowdcontrol.objectservice.proto.Answer();
  }

  public static edu.kit.ipd.crowdcontrol.objectservice.proto.Answer getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Answer>
      PARSER = new com.google.protobuf.AbstractParser<Answer>() {
    public Answer parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      try {
        return new Answer(input, extensionRegistry);
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

  public static com.google.protobuf.Parser<Answer> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Answer> getParserForType() {
    return PARSER;
  }

  public edu.kit.ipd.crowdcontrol.objectservice.proto.Answer getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
