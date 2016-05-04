package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.Utils;

/**
 * Created by brent on 5/4/16.
 */
public class MetaRegistry {

    public static void fill(MetaEntry me) {

        //just a mockup, this will consider hash first, and path
        //second, to find the appropriate values (if the hash
        //exists, use that, if the hash is not in the registry,
        //add it, and populate using path (if exists))
        //
        //for tags though: combine all tag strings
        //that match path or hash

        if(Utils.stringIsNullOrWhitespace(me.getDisplayName())){

            me.setDisplayName("test display name");
        }

        if(Utils.stringIsNullOrWhitespace(me.getTags())){

            String tags ="testing " + me.getDisplayName() +
                    ", more testing " + me.getDisplayName();
            me.setTags(tags);
        }
    }
}
