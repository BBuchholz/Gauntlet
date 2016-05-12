package com.nineworldsdeep.gauntlet.tapestry;

import com.nineworldsdeep.gauntlet.Utils;

/**
 * Created by brent on 5/4/16.
 */
public class MetaRegistry {

    ////TODO////////////////////////////////////////////////////////////////////
    //
    //  First order of business: if you look in your config folder on your
    //  device, you will see that NineWorldsDeep has already been generating
    //  a hash to tags index, and a timestamped version as well.
    //  We need to consume these and use them, let's start here.
    //  First let them be loaded into here, then replace instances of TagIndexFile
    //  with calls to the MetaRegistry instead.
    //
    ////////////////////////////////////////////////////////////////////////////

    ////TODO////////////////////////////////////////////////////////////////////
    //
    //  This should handle all of the old ways of using paths, tags, hashes and
    //  display names. Let's begin by porting all external logic into here,
    //  refactoring where it is useful to. In this way we maintain support
    //  for file based import and export. Then, we add in the Sqlite functionality
    //  still supporting the old way as well. Options give us flexibility.
    //
    ////////////////////////////////////////////////////////////////////////////

    ////TODO////////////////////////////////////////////////////////////////////
    //
    //  Implement an Sqlite database to hold the current (put in secure, default android
    //  location). Allow for exhale() and inhale() functions that will
    //  export internal db to a timestamped (maybe checksum hashed as well, appending
    //  to the filename, to detect tampering as these will be in public folders) sqlite db
    //  in the public config folder (maybe an export sub folder) and the reverse &c.
    //  Method .inhale() should be an "import all" and a .true(sqliteDb) for each
    //
    ////////////////////////////////////////////////////////////////////////////

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
