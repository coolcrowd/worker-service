// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: answer.proto

package edu.ipd.kit.crowdcontrol.workerservice.proto;

public final class AnswerOuterClass {
  private AnswerOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface AnswerOrBuilder extends
      // @@protoc_insertion_point(interface_extends:crowdcontrol.Answer)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string answer = 1;</code>
     */
    java.lang.String getAnswer();
    /**
     * <code>optional string answer = 1;</code>
     */
    com.google.protobuf.ByteString
        getAnswerBytes();

    /**
     * <code>optional int32 task = 2;</code>
     */
    int getTask();
  }
  /**
   * Protobuf type {@code crowdcontrol.Answer}
   */
  public  static final class Answer extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:crowdcontrol.Answer)
      AnswerOrBuilder {
    // Use Answer.newBuilder() to construct.
    private Answer(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
    }
    private Answer() {
      answer_ = "";
      task_ = 0;
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
            case 10: {
              String s = input.readStringRequireUtf8();

              answer_ = s;
              break;
            }
            case 16: {

              task_ = input.readInt32();
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
      return edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.internal_static_crowdcontrol_Answer_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.internal_static_crowdcontrol_Answer_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer.class, edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer.Builder.class);
    }

    public static final int ANSWER_FIELD_NUMBER = 1;
    private volatile java.lang.Object answer_;
    /**
     * <code>optional string answer = 1;</code>
     */
    public java.lang.String getAnswer() {
      java.lang.Object ref = answer_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        answer_ = s;
        return s;
      }
    }
    /**
     * <code>optional string answer = 1;</code>
     */
    public com.google.protobuf.ByteString
        getAnswerBytes() {
      java.lang.Object ref = answer_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        answer_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TASK_FIELD_NUMBER = 2;
    private int task_;
    /**
     * <code>optional int32 task = 2;</code>
     */
    public int getTask() {
      return task_;
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
      if (!getAnswerBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessage.writeString(output, 1, answer_);
      }
      if (task_ != 0) {
        output.writeInt32(2, task_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getAnswerBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessage.computeStringSize(1, answer_);
      }
      if (task_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, task_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer prototype) {
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
        edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.AnswerOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.internal_static_crowdcontrol_Answer_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.internal_static_crowdcontrol_Answer_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer.class, edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer.Builder.class);
      }

      // Construct using edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer.newBuilder()
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
        answer_ = "";

        task_ = 0;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.internal_static_crowdcontrol_Answer_descriptor;
      }

      public edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer getDefaultInstanceForType() {
        return edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer.getDefaultInstance();
      }

      public edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer build() {
        edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer buildPartial() {
        edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer result = new edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer(this);
        result.answer_ = answer_;
        result.task_ = task_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer) {
          return mergeFrom((edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer other) {
        if (other == edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer.getDefaultInstance()) return this;
        if (!other.getAnswer().isEmpty()) {
          answer_ = other.answer_;
          onChanged();
        }
        if (other.getTask() != 0) {
          setTask(other.getTask());
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
        edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private java.lang.Object answer_ = "";
      /**
       * <code>optional string answer = 1;</code>
       */
      public java.lang.String getAnswer() {
        java.lang.Object ref = answer_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          answer_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string answer = 1;</code>
       */
      public com.google.protobuf.ByteString
          getAnswerBytes() {
        java.lang.Object ref = answer_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          answer_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string answer = 1;</code>
       */
      public Builder setAnswer(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        answer_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string answer = 1;</code>
       */
      public Builder clearAnswer() {
        
        answer_ = getDefaultInstance().getAnswer();
        onChanged();
        return this;
      }
      /**
       * <code>optional string answer = 1;</code>
       */
      public Builder setAnswerBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        answer_ = value;
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
    private static final edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer();
    }

    public static edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer getDefaultInstance() {
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

    public edu.ipd.kit.crowdcontrol.workerservice.proto.AnswerOuterClass.Answer getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_Answer_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_crowdcontrol_Answer_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014answer.proto\022\014crowdcontrol\"&\n\006Answer\022\016" +
      "\n\006answer\030\001 \001(\t\022\014\n\004task\030\002 \001(\005B.\n,edu.ipd." +
      "kit.crowdcontrol.workerservice.protob\006pr" +
      "oto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_crowdcontrol_Answer_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_crowdcontrol_Answer_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_crowdcontrol_Answer_descriptor,
        new java.lang.String[] { "Answer", "Task", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}