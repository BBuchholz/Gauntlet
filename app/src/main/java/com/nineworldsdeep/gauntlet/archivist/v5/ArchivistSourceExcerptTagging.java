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

    public void setMediaTagId(int mediaTagId) {
        this.mediaTagId = mediaTagId;
    }

    public void setSourceExcerptTaggingId(int sourceExcerptTaggingId) {
        this.sourceExcerptTaggingId = sourceExcerptTaggingId;
    }

    public void setSourceExcerptId(int sourceExcerptId) {
        this.sourceExcerptId = sourceExcerptId;
    }

    public String getMediaTagValue() {
        return mediaTagValue;
    }

    public void setMediaTagValue(String mediaTagValue) {
        this.mediaTagValue = mediaTagValue;
    }
}
