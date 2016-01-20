// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: view.proto

package edu.kit.ipd.crowdcontrol.workerservice.proto;

public final class ViewOuterClass {
  private ViewOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_View_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_crowdcontrol_View_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_View_Constraint_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_crowdcontrol_View_Constraint_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_View_Picture_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_crowdcontrol_View_Picture_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_View_Calibration_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_crowdcontrol_View_Calibration_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_View_CalibrationAnswerOption_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_crowdcontrol_View_CalibrationAnswerOption_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_View_Answer_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_crowdcontrol_View_Answer_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_crowdcontrol_View_RatingOption_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_crowdcontrol_View_RatingOption_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\nview.proto\022\014crowdcontrol\"\203\006\n\004View\022\021\n\tw" +
      "orker_id\030\001 \001(\005\022%\n\004type\030\002 \001(\0162\027.crowdcont" +
      "rol.View.Type\022\r\n\005title\030\003 \001(\t\022\023\n\013descript" +
      "ion\030\004 \001(\t\022\014\n\004task\030\005 \001(\005\022*\n\007answers\030\006 \003(\013" +
      "2\031.crowdcontrol.View.Answer\0226\n\rratingOpt" +
      "ions\030\007 \003(\0132\037.crowdcontrol.View.RatingOpt" +
      "ion\0222\n\013constraints\030\010 \003(\0132\035.crowdcontrol." +
      "View.Constraint\022,\n\010pictures\030\t \003(\0132\032.crow" +
      "dcontrol.View.Picture\0224\n\014calibrations\030\n " +
      "\003(\0132\036.crowdcontrol.View.Calibration\032\032\n\nC",
      "onstraint\022\014\n\004name\030\001 \001(\t\032+\n\007Picture\022\013\n\003ur" +
      "l\030\001 \001(\t\022\023\n\013url_license\030\002 \001(\t\032o\n\013Calibrat" +
      "ion\022\020\n\010question\030\001 \001(\t\022B\n\016answer_options\030" +
      "\002 \003(\0132*.crowdcontrol.View.CalibrationAns" +
      "werOption\022\n\n\002id\030\003 \001(\005\0325\n\027CalibrationAnsw" +
      "erOption\022\016\n\006option\030\001 \001(\t\022\n\n\002id\030\002 \001(\005\032$\n\006" +
      "Answer\022\n\n\002id\030\001 \001(\005\022\016\n\006answer\030\002 \001(\t\0322\n\014Ra" +
      "tingOption\022\r\n\005value\030\001 \001(\005\022\023\n\013description" +
      "\030\002 \001(\t\"H\n\004Type\022\014\n\010FINISHED\020\000\022\n\n\006ANSWER\020\001" +
      "\022\n\n\006RATING\020\002\022\017\n\013CALIBRATION\020\003\022\t\n\005EMAIL\020\004",
      "B0\n,edu.kit.ipd.crowdcontrol.workerservi" +
      "ce.protoP\001b\006proto3"
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
    internal_static_crowdcontrol_View_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_crowdcontrol_View_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_crowdcontrol_View_descriptor,
        new java.lang.String[] { "WorkerId", "Type", "Title", "Description", "Task", "Answers", "RatingOptions", "Constraints", "Pictures", "Calibrations", });
    internal_static_crowdcontrol_View_Constraint_descriptor =
      internal_static_crowdcontrol_View_descriptor.getNestedTypes().get(0);
    internal_static_crowdcontrol_View_Constraint_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_crowdcontrol_View_Constraint_descriptor,
        new java.lang.String[] { "Name", });
    internal_static_crowdcontrol_View_Picture_descriptor =
      internal_static_crowdcontrol_View_descriptor.getNestedTypes().get(1);
    internal_static_crowdcontrol_View_Picture_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_crowdcontrol_View_Picture_descriptor,
        new java.lang.String[] { "Url", "UrlLicense", });
    internal_static_crowdcontrol_View_Calibration_descriptor =
      internal_static_crowdcontrol_View_descriptor.getNestedTypes().get(2);
    internal_static_crowdcontrol_View_Calibration_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_crowdcontrol_View_Calibration_descriptor,
        new java.lang.String[] { "Question", "AnswerOptions", "Id", });
    internal_static_crowdcontrol_View_CalibrationAnswerOption_descriptor =
      internal_static_crowdcontrol_View_descriptor.getNestedTypes().get(3);
    internal_static_crowdcontrol_View_CalibrationAnswerOption_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_crowdcontrol_View_CalibrationAnswerOption_descriptor,
        new java.lang.String[] { "Option", "Id", });
    internal_static_crowdcontrol_View_Answer_descriptor =
      internal_static_crowdcontrol_View_descriptor.getNestedTypes().get(4);
    internal_static_crowdcontrol_View_Answer_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_crowdcontrol_View_Answer_descriptor,
        new java.lang.String[] { "Id", "Answer", });
    internal_static_crowdcontrol_View_RatingOption_descriptor =
      internal_static_crowdcontrol_View_descriptor.getNestedTypes().get(5);
    internal_static_crowdcontrol_View_RatingOption_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_crowdcontrol_View_RatingOption_descriptor,
        new java.lang.String[] { "Value", "Description", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
