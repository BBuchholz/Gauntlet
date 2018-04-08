package com.nineworldsdeep.gauntlet.archivist.v5;

import com.nineworldsdeep.gauntlet.core.TaggingBase;

public class ArchivistSourceExcerptTagging extends TaggingBase {

    private int sourceExcerptTaggingId, sourceExcerptId, mediaTagId;
    private String mediaTagValue;

    public int getSourceExcerptTaggingId() {
        return sourceExcerptTaggingId;
    }

    public int getSourceExcerptId() {
        return sourceExcerptId;
    }

    public int getMediaTagId() {
        return mediaTagId;
    }

    public String getMediaTagValue() {
        return mediaTagValue;
    }
}
