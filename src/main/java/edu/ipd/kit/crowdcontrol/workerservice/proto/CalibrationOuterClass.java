// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: calibration.proto

package edu.ipd.kit.crowdcontrol.workerservice.proto;

public final class CalibrationOuterClass {
  private CalibrationOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface CalibrationOrBuilder extends
      // @@protoc_insertion_point(interface_extends:crowdcontrol.Calibration)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional int32 answerOption = 1;</code>
     */
    int getAnswerOption();
  }
  /**
   * Protobuf type {@code crowdcontrol.Calibration}
   */
  public  static final class Calibration extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:crowdcontrol.Calibration)
      CalibrationOrBuilder {
    // Use Calibration.newBuilder() to construct.
    private Calibration(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
    }
    private Calibration() {
      answerOption_ = 0;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private Calibration(
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

              answerOption_ = input.readInt32();
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
      return edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.internal_static_crowdcontrol_Calibration_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.internal_static_crowdcontrol_Calibration_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration.class, edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration.Builder.class);
    }

    public static final int ANSWEROPTION_FIELD_NUMBER = 1;
    private int answerOption_;
    /**
     * <code>optional int32 answerOption = 1;</code>
     */
    public int getAnswerOption() {
      return answerOption_;
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
      if (answerOption_ != 0) {
        output.writeInt32(1, answerOption_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (answerOption_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, answerOption_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration prototype) {
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
     * Protobuf type {@code crowdcontrol.Calibration}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:crowdcontrol.Calibration)
        edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.CalibrationOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.internal_static_crowdcontrol_Calibration_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.internal_static_crowdcontrol_Calibration_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration.class, edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration.Builder.class);
      }

      // Construct using edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration.newBuilder()
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
        answerOption_ = 0;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.internal_static_crowdcontrol_Calibration_descriptor;
      }

      public edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration getDefaultInstanceForType() {
        return edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration.getDefaultInstance();
      }

      public edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration build() {
        edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration buildPartial() {
        edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration result = new edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration(this);
        result.answerOption_ = answerOption_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration) {
          return mergeFrom((edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration other) {
        if (other == edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration.getDefaultInstance()) return this;
        if (other.getAnswerOption() != 0) {
          setAnswerOption(other.getAnswerOption());
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
        edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int answerOption_ ;
      /**
       * <code>optional int32 answerOption = 1;</code>
       */
      public int getAnswerOption() {
        return answerOption_;
      }
      /**
       * <code>optional int32 answerOption = 1;</code>
       */
      public Builder setAnswerOption(int value) {
        
        answerOption_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 answerOption = 1;</code>
       */
      public Builder clearAnswerOption() {
        
        answerOption_ = 0;
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


      // @@protoc_insertion_point(builder_scope:crowdcontrol.Calibration)
    }

    // @@protoc_insertion_point(class_scope:crowdcontrol.Calibration)
    private static final edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration();
    }

    public static edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Calibration>
        PARSER = new com.google.protobuf.AbstractParser<Calibration>() {
      public Calibration parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        try {
          return new Calibration(input, extensionRegistry);
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

    public static com.google.protobuf.Parser<Calibration> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Calibration> getParserForType() {
      return PARSER;
    }

    public edu.ipd.kit.crowdcontrol.workerservice.proto.CalibrationOuterClass.Calibration getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_Calibration_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_crowdcontrol_Calibration_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021calibration.proto\022\014crowdcontrol\"#\n\013Cal" +
      "ibration\022\024\n\014answerOption\030\001 \001(\005B.\n,edu.ip" +
      "d.kit.crowdcontrol.workerservice.protob\006" +
      "proto3"
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
    internal_static_crowdcontrol_Calibration_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_crowdcontrol_Calibration_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_crowdcontrol_Calibration_descriptor,
        new java.lang.String[] { "AnswerOption", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
