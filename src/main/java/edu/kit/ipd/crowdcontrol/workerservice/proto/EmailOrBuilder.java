// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: email.proto

package edu.kit.ipd.crowdcontrol.workerservice.proto;

public interface EmailOrBuilder extends
    // @@protoc_insertion_point(interface_extends:crowdcontrol.Email)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional string email = 1;</code>
   */
  java.lang.String getEmail();
  /**
   * <code>optional string email = 1;</code>
   */
  com.google.protobuf.ByteString
      getEmailBytes();

  /**
   * <code>repeated .crowdcontrol.Email.PlatformParameter platform_parameters = 2;</code>
   */
  java.util.List<edu.kit.ipd.crowdcontrol.workerservice.proto.Email.PlatformParameter> 
      getPlatformParametersList();
  /**
   * <code>repeated .crowdcontrol.Email.PlatformParameter platform_parameters = 2;</code>
   */
  edu.kit.ipd.crowdcontrol.workerservice.proto.Email.PlatformParameter getPlatformParameters(int index);
  /**
   * <code>repeated .crowdcontrol.Email.PlatformParameter platform_parameters = 2;</code>
   */
  int getPlatformParametersCount();
  /**
   * <code>repeated .crowdcontrol.Email.PlatformParameter platform_parameters = 2;</code>
   */
  java.util.List<? extends edu.kit.ipd.crowdcontrol.workerservice.proto.Email.PlatformParameterOrBuilder> 
      getPlatformParametersOrBuilderList();
  /**
   * <code>repeated .crowdcontrol.Email.PlatformParameter platform_parameters = 2;</code>
   */
  edu.kit.ipd.crowdcontrol.workerservice.proto.Email.PlatformParameterOrBuilder getPlatformParametersOrBuilder(
      int index);
}
