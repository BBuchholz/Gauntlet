package com.nineworldsdeep.gauntlet.mnemosyne.v5;

import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.nineworldsdeep.gauntlet.Utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 * Created by brent on 2/22/17.
 */

public class UtilsMnemosyneV5 {

    static String getMimeType(File file) {



        String type = null;

//        final String url = file.toString();
//        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);

        if (file == null) {

            type = "*/*"; // fallback type

        }else {

            final String extension =
                    FilenameUtils.getExtension(file.getName());

            if (extension != null &&
                    !Utils.stringIsNullOrWhitespace(extension)) {

                type = MimeTypeMap
                        .getSingleton()
                        .getMimeTypeFromExtension(extension.toLowerCase());
            }

            if (type == null) {

                type = "*/*"; // fallback type
            }
        }

        return type;
    }
}
