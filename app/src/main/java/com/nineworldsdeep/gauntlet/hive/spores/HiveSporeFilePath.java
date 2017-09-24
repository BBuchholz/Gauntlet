package com.nineworldsdeep.gauntlet.hive.spores;

import com.nineworldsdeep.gauntlet.hive.HiveSpore;
import com.nineworldsdeep.gauntlet.hive.HiveSporeType;
import com.nineworldsdeep.gauntlet.hive.UtilsHive;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.File;

/**
 * Created by brent on 9/24/17.
 */

public class HiveSporeFilePath extends HiveSpore {

    private File fileForSpore;
    private HiveSporeType hiveSporeType;

    public HiveSporeFilePath(File fileForSpore){

        this.name = fileForSpore.getName();
        this.fileForSpore = fileForSpore;

        hiveSporeType = UtilsHive.getSporeTypeFromFile(this.fileForSpore);
    }

    @Override
    public HiveSporeType getHiveSporeType() {

        return hiveSporeType;
    }

    public File getFile(){

        return fileForSpore;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HiveSporeFilePath that = (HiveSporeFilePath) o;

        return new EqualsBuilder()
                .append(getFile().getAbsolutePath().toLowerCase(),
                        that.getFile().getAbsolutePath().toLowerCase())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getFile().getAbsolutePath().toLowerCase())
                .toHashCode();
    }
}
