package com.pinterest.secor.transformer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinterest.secor.common.SecorConfig;
import com.pinterest.secor.message.Message;

public class DotNetDateToIso8601MessageTransformer implements MessageTransformer {
    private static final Logger LOG = LoggerFactory.getLogger(DotNetDateToIso8601MessageTransformer.class);
    private final Pattern mTsPattern;
    private final SimpleDateFormat mIso8601Format;

    protected SecorConfig mConfig;
    
    /**
     * Constructor
     * 
     * @param config
     */        
    public DotNetDateToIso8601MessageTransformer(SecorConfig config) {
        mConfig = config;
        mTsPattern = Pattern.compile("\\\\/Date\\((\\d+)\\)\\\\/", Pattern.UNIX_LINES);
        mIso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        mIso8601Format.setTimeZone(tz);
    }

    @Override
    public Message transform(Message message) {

        String line = new String(message.getPayload());

        try {
            line = replaceDotNetDates(line);
            message.setPayload(line.getBytes("UTF-8"));
        } catch (Throwable e) {
            LOG.warn("failed to transform message {}", line);
        }

        return message;
    }

    private String replaceDotNetDates(String message) {
        StringBuffer result = new StringBuffer();

        Matcher matcher = mTsPattern.matcher(message);

        while (matcher.find()) {
            matcher.appendReplacement(result, convertDotNetDateToIso8601(Long.parseLong(matcher.group(1))));
        }

        matcher.appendTail(result);

        return result.toString();
    }

    private String convertDotNetDateToIso8601(long timestamp) {

        Date d = new Date(timestamp);

        return mIso8601Format.format(d);
    }

}