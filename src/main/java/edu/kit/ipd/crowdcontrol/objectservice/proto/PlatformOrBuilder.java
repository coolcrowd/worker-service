// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: platform.proto

package edu.kit.ipd.crowdcontrol.objectservice.proto;

public interface PlatformOrBuilder extends
    // @@protoc_insertion_point(interface_extends:crowdcontrol.Platform)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional string id = 1;</code>
   */
  java.lang.String getId();
  /**
   * <code>optional string id = 1;</code>
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <code>optional string name = 2;</code>
   */
  java.lang.String getName();
  /**
   * <code>optional string name = 2;</code>
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>optional bool has_calibrations = 3;</code>
   */
  boolean getHasCalibrations();

  /**
   * <code>optional bool is_inactive = 4;</code>
   */
  boolean getIsInactive();
}
