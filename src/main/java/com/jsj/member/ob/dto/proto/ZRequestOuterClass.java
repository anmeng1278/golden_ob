// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ZRequest.proto

package com.jsj.member.ob.dto.proto;

public final class ZRequestOuterClass {
  private ZRequestOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface ZRequestOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.jsj.member.dto.proto.ZRequest)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional bytes ZPack = 1;</code>
     */
    boolean hasZPack();
    /**
     * <code>optional bytes ZPack = 1;</code>
     */
    com.google.protobuf.ByteString getZPack();

    /**
     * <code>optional string MethodName = 2;</code>
     */
    boolean hasMethodName();
    /**
     * <code>optional string MethodName = 2;</code>
     */
    String getMethodName();
    /**
     * <code>optional string MethodName = 2;</code>
     */
    com.google.protobuf.ByteString
        getMethodNameBytes();
  }
  /**
   * Protobuf type {@code com.jsj.member.dto.proto.ZRequest}
   */
  public static final class ZRequest extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:com.jsj.member.dto.proto.ZRequest)
      ZRequestOrBuilder {
    // Use ZRequest.newBuilder() to construct.
    private ZRequest(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private ZRequest(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final ZRequest defaultInstance;
    public static ZRequest getDefaultInstance() {
      return defaultInstance;
    }

    public ZRequest getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private ZRequest(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              zPack_ = input.readBytes();
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              methodName_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ZRequestOuterClass.internal_static_com_jsj_member_dto_proto_ZRequest_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ZRequestOuterClass.internal_static_com_jsj_member_dto_proto_ZRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ZRequest.class, Builder.class);
    }

    public static com.google.protobuf.Parser<ZRequest> PARSER =
        new com.google.protobuf.AbstractParser<ZRequest>() {
      public ZRequest parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ZRequest(input, extensionRegistry);
      }
    };

    @Override
    public com.google.protobuf.Parser<ZRequest> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ZPACK_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString zPack_;
    /**
     * <code>optional bytes ZPack = 1;</code>
     */
    public boolean hasZPack() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional bytes ZPack = 1;</code>
     */
    public com.google.protobuf.ByteString getZPack() {
      return zPack_;
    }

    public static final int METHODNAME_FIELD_NUMBER = 2;
    private Object methodName_;
    /**
     * <code>optional string MethodName = 2;</code>
     */
    public boolean hasMethodName() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string MethodName = 2;</code>
     */
    public String getMethodName() {
      Object ref = methodName_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          methodName_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string MethodName = 2;</code>
     */
    public com.google.protobuf.ByteString
        getMethodNameBytes() {
      Object ref = methodName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        methodName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      zPack_ = com.google.protobuf.ByteString.EMPTY;
      methodName_ = "";
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
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, zPack_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getMethodNameBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, zPack_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getMethodNameBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @Override
    protected Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static ZRequest parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ZRequest parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ZRequest parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ZRequest parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ZRequest parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ZRequest parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ZRequest parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ZRequest parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ZRequest parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ZRequest parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ZRequest prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.jsj.member.dto.proto.ZRequest}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.jsj.member.dto.proto.ZRequest)
        ZRequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ZRequestOuterClass.internal_static_com_jsj_member_dto_proto_ZRequest_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ZRequestOuterClass.internal_static_com_jsj_member_dto_proto_ZRequest_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                ZRequest.class, Builder.class);
      }

      // Construct using com.jsj.member.dto.proto.ZRequestOuterClass.ZRequest.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        zPack_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        methodName_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ZRequestOuterClass.internal_static_com_jsj_member_dto_proto_ZRequest_descriptor;
      }

      public ZRequest getDefaultInstanceForType() {
        return ZRequest.getDefaultInstance();
      }

      public ZRequest build() {
        ZRequest result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ZRequest buildPartial() {
        ZRequest result = new ZRequest(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.zPack_ = zPack_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.methodName_ = methodName_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ZRequest) {
          return mergeFrom((ZRequest)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ZRequest other) {
        if (other == ZRequest.getDefaultInstance()) return this;
        if (other.hasZPack()) {
          setZPack(other.getZPack());
        }
        if (other.hasMethodName()) {
          bitField0_ |= 0x00000002;
          methodName_ = other.methodName_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ZRequest parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ZRequest) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.ByteString zPack_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes ZPack = 1;</code>
       */
      public boolean hasZPack() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional bytes ZPack = 1;</code>
       */
      public com.google.protobuf.ByteString getZPack() {
        return zPack_;
      }
      /**
       * <code>optional bytes ZPack = 1;</code>
       */
      public Builder setZPack(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        zPack_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes ZPack = 1;</code>
       */
      public Builder clearZPack() {
        bitField0_ = (bitField0_ & ~0x00000001);
        zPack_ = getDefaultInstance().getZPack();
        onChanged();
        return this;
      }

      private Object methodName_ = "";
      /**
       * <code>optional string MethodName = 2;</code>
       */
      public boolean hasMethodName() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string MethodName = 2;</code>
       */
      public String getMethodName() {
        Object ref = methodName_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            methodName_ = s;
          }
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string MethodName = 2;</code>
       */
      public com.google.protobuf.ByteString
          getMethodNameBytes() {
        Object ref = methodName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          methodName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string MethodName = 2;</code>
       */
      public Builder setMethodName(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        methodName_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string MethodName = 2;</code>
       */
      public Builder clearMethodName() {
        bitField0_ = (bitField0_ & ~0x00000002);
        methodName_ = getDefaultInstance().getMethodName();
        onChanged();
        return this;
      }
      /**
       * <code>optional string MethodName = 2;</code>
       */
      public Builder setMethodNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        methodName_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:com.jsj.member.dto.proto.ZRequest)
    }

    static {
      defaultInstance = new ZRequest(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:com.jsj.member.dto.proto.ZRequest)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_jsj_member_dto_proto_ZRequest_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_com_jsj_member_dto_proto_ZRequest_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\016ZRequest.proto\022\030com.jsj.member.dto.pro" +
      "to\"-\n\010ZRequest\022\r\n\005ZPack\030\001 \001(\014\022\022\n\nMethodN" +
      "ame\030\002 \001(\t"
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
    internal_static_com_jsj_member_dto_proto_ZRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_jsj_member_dto_proto_ZRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_com_jsj_member_dto_proto_ZRequest_descriptor,
        new String[] { "ZPack", "MethodName", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}