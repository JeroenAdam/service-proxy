/* Copyright 2020 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.membrane.core.transport.http2.frame;

public class HeadersFrame implements HeaderBlockFragment, StreamEnd {
    public static final int FLAG_END_STREAM = 0x1;
    public static final int FLAG_END_HEADERS = 0x4;
    public static final int FLAG_PADDED = 0x8;
    public static final int FLAG_PRIORITY = 0x20;

    private final Frame frame;

    private final int padLength;
    private final boolean exclusive;
    private final int streamDependency;
    private final int weight;
    private final int headerBlockStartIndex;
    
    public HeadersFrame(Frame frame) {
        this.frame = frame;

        int p = 0;

        if (isPadded()) {
            padLength = frame.content[p++];
        } else {
            padLength = 0;
        }
        if (isPriority()) {
            exclusive = (frame.content[p] & 0x80) != 0;
            streamDependency = (frame.content[p++] & 0x7F) << 24 |
                    (frame.content[p++] & 0xFF) << 16 |
                    (frame.content[p++] & 0xFF) << 8 |
                    frame.content[p++] & 0xFF;
            weight = (frame.content[p++] & 0xFF) + 1;
        } else {
            exclusive = false;
            streamDependency = 0;
            weight = 0;
        }

        headerBlockStartIndex = p;
    }

    public boolean isEndStream() {
        return (frame.flags & FLAG_END_STREAM) != 0;
    }

    public boolean isEndHeaders() {
        return (frame.flags & FLAG_END_HEADERS) != 0;
    }

    public boolean isPadded() {
        return (frame.flags & FLAG_PADDED) != 0;
    }

    public boolean isPriority() {
        return (frame.flags & FLAG_PRIORITY) != 0;
    }

    public int getHeaderBlockStartIndex() {
        return headerBlockStartIndex;
    }

    public int getHeaderBlockLength() {
        return frame.length - padLength - headerBlockStartIndex;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Headers {\n");
        sb.append("  streamId = ");
        sb.append(frame.streamId);
        sb.append("\n  flags = ");
        if (isEndHeaders())
            sb.append("END_HEADERS ");
        if (isEndStream())
            sb.append("END_STREAM");
        sb.append("\n");
        if (isPriority()) {
            sb.append("  priority: ");
            if (exclusive)
                sb.append("exclusive, ");
            sb.append("weight = ");
            sb.append(weight);
            sb.append(", streamDependency = ");
            sb.append(streamDependency);
            sb.append("\n");
        }
        sb.append("  header block data: \n");
        frame.appendHex(sb, frame.content, getHeaderBlockStartIndex(), getHeaderBlockLength(), 2);
        sb.append("}");
        return sb.toString();
    }


    public byte[] getContent() {
        return frame.getContent();
    }

    public Frame getFrame() {
        return frame;
    }

    public int getWeight() {
        return weight;
    }

    public int getStreamDependency() {
        return streamDependency;
    }

    public boolean isExclusive() {
        return exclusive;
    }
}
