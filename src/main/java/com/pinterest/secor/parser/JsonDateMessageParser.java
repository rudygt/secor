package com.pinterest.secor.parser;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import com.pinterest.secor.common.SecorConfig;
import com.pinterest.secor.message.Message;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class JsonDateMessageParser extends TimestampedMessageParser {
    private final boolean m_timestampRequired;

    public JsonDateMessageParser(SecorConfig config) {
        super(config);
        m_timestampRequired = config.isMessageTimestampRequired();
    }

    @Override
    public long extractTimestampMillis(final Message message) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(message.getPayload());
        if (jsonObject != null) {
            Object fieldValue = getJsonFieldValue(jsonObject);
            if (fieldValue != null) {
                return toMillis(DatatypeConverter.parseDateTime(fieldValue.toString()).getTime().getTime());
            }
        } else if (m_timestampRequired) {
            throw new RuntimeException("Missing timestamp field for message: " + message);
        }
        return toMillis(new Date().getTime());
    }

}