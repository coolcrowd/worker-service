// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: answer.proto

package edu.kit.ipd.crowdcontrol.workerservice.proto;

public final class AnswerOuterClass {
  private AnswerOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_Answer_descriptor;
  static
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
      "\n\014answer.proto\022\014crowdcontrol\"A\n\006Answer\022\016" +
      "\n\006answer\030\001 \001(\t\022\022\n\nexperiment\030\002 \001(\005\022\023\n\013re" +
      "servation\030\003 \001(\005B0\n,edu.kit.ipd.crowdcont" +
      "rol.workerservice.protoP\001b\006proto3"
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
        new java.lang.String[] { "Answer", "Experiment", "Reservation", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
