/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pinterest.secor.parser;

import org.apache.commons.lang.StringUtils;

import java.util.Date;

import com.pinterest.secor.common.SecorConfig;
import com.pinterest.secor.message.Message;



/**
 * Offset message parser groups messages based on the offset ranges.
 *
 * @author Ahsan Nabi Dar (ahsan@wego.com)
 */
public class PartitionedMessageParser extends TimestampedMessageParser {

    public PartitionedMessageParser(SecorConfig config) {
        super(config);
    }

    @Override
    public String[] extractPartitions(Message message) throws Exception {
        String[] dailyPartition = generatePartitions(new Date().getTime(), mUsingHourly, mUsingMinutely);
        String dailyPartitionPath = StringUtils.join(dailyPartition, '/');
        String[] result = {dailyPartitionPath};
        return result;
    }

    @Override
    public long extractTimestampMillis(final Message message) {
        return new Date().getTime(); //Daily Timestamp generation
    }

}