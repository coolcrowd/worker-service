package edu.kit.ipd.crowdcontrol.workerservice;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import ratpack.handling.Context;
import ratpack.http.internal.MimeParse;
import ratpack.render.RendererSupport;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author LeanderK
 * @version 1.0
 */
public class MessageRenderer extends RendererSupport<Message> {
    private static JsonFormat.Printer PRINTER = JsonFormat.printer().includingDefaultValueFields();

    @SuppressWarnings("FieldCanBeLocal")
    private static String TYPE_JSON = "application/json";
    @SuppressWarnings("FieldCanBeLocal")
    private static String TYPE_PROTOBUF = "application/protobuf";
    private static List<String> SUPPORTED_TYPES = Collections.unmodifiableList(Arrays.asList(
            "application/protobuf",
            "application/json"
    ));

    /**
     * {@inheritDoc}
     *
     * @param context
     * @param message
     */
    @Override
    public void render(Context context, Message message) throws Exception {
        String bestMatch = MimeParse.bestMatch(SUPPORTED_TYPES, context.getRequest().getHeaders().get("accept"));

        try {
            switch (bestMatch) {
                case TYPE_JSON:
                    context.getResponse().send(TYPE_JSON, PRINTER.print(message));
                    break;
                case TYPE_PROTOBUF:
                    context.getResponse().send(TYPE_PROTOBUF, message.toByteArray());
                    break;
                default:
                    throw new NotAcceptableException(context.getRequest().getHeaders().get("accept"), TYPE_JSON, TYPE_PROTOBUF);
            }
        } catch (InvalidProtocolBufferException e) {
            // Can't happen, because we don't use any "Any" fields.
            // https://developers.google.com/protocol-buffers/docs/proto3#any
            throw new InternalServerErrorException("Attempt to transform an invalid protocol buffer into JSON.");
        }
    }
}
