// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: calibration_list.proto

package edu.kit.ipd.crowdcontrol.objectservice.proto;

public final class CalibrationListOuterClass {
  private CalibrationListOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_CalibrationList_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_crowdcontrol_CalibrationList_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\026calibration_list.proto\022\014crowdcontrol\032\021" +
      "calibration.proto\";\n\017CalibrationList\022(\n\005" +
      "items\030\001 \003(\0132\031.crowdcontrol.CalibrationB0" +
      "\n,edu.kit.ipd.crowdcontrol.objectservice" +
      ".protoP\001b\006proto3"
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
          edu.kit.ipd.crowdcontrol.objectservice.proto.CalibrationOuterClass.getDescriptor(),
        }, assigner);
    internal_static_crowdcontrol_CalibrationList_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_crowdcontrol_CalibrationList_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_crowdcontrol_CalibrationList_descriptor,
        new java.lang.String[] { "Items", });
    edu.kit.ipd.crowdcontrol.objectservice.proto.CalibrationOuterClass.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
