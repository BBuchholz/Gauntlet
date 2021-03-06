package com.nineworldsdeep.gauntlet.mock;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.model.*;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyUtils;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by brent on 9/1/16.
 */
public class MockUtils {

    //get a list of adjectives and nouns to generate random mock items
    //(like default router passwords used by some ISP's)

    private static ArrayList<String> mAdjectives =
            new ArrayList<>();
    private static ArrayList<String> mNouns =
            new ArrayList<>();
    private static ArrayList<String> mVerbs =
            new ArrayList<>();

    //NEED ARRAYLISTS FOR EACH MODEL ITEM TO HOLD A FEW MOCKED COMMON ITEMS
    //eg. A couple FileModelItems to add into multiple nodes to test commonality
    private static ArrayList<FileNode> mFiles = new ArrayList<>();
    private static ArrayList<HashNode> mHashes = new ArrayList<>();
    private static ArrayList<TagNode> mTags = new ArrayList<>();
    private static ArrayList<LocalConfigNode> mLocalConfig = new ArrayList<>();
    private static ArrayList<SynergyListNode> mSynergyLists = new ArrayList<>();
    private static ArrayList<SynergyItemNode> mSynergyListItems = new ArrayList<>();

    static{

        //NB: all words were randomly generated with an online app
        populateNouns();
        populateAdjectives();
        populateVerbs();

        populateFiles();
        populateLocalConfig();
        populateSynergyLists();
    }

    private static void populateSynergyListItems(SynergyListNode lst) {

        //add a random number of existing items
        int numberOfExistingToAdd = getRandomIndex(6);

        for(int i = 0; i < numberOfExistingToAdd; i++){

            if(mSynergyListItems.size() > 0) {

                int idx = getRandomIndex(mSynergyListItems.size());

                lst.add(mSynergyListItems.get(idx));
            }
        }

        //generate a random number of new items to add to shared list
        int numberOfNewSharedToAdd = getRandomIndex(4);

        for(int i = 0; i < numberOfNewSharedToAdd; i++){

            String testText = getRandomVerb() + " " +
                    getRandomAdjective() + " " +
                    getRandomNoun();

            SynergyItemNode sli =
                    new SynergyItemNode(lst, testText);

            lst.add(sli);
            mSynergyListItems.add(sli);
        }

        //generate a random number of new items to keep seperate
        int numberOfNewPrivateToAdd = getRandomIndex(4);

        for(int i = 0; i < numberOfNewPrivateToAdd; i++){

            String testText = getRandomVerb() + " " +
                    getRandomAdjective() + " " +
                    getRandomNoun();

            SynergyItemNode sli =
                    new SynergyItemNode(lst, testText);

            lst.add(sli);
        }
    }

    private static void populateTags(FileNode fmi) {

        //add a random number of existing tags
        int numberOfExistingToAdd = getRandomIndex(3);

        for(int i = 0; i < numberOfExistingToAdd; i++){

            if(mTags.size() > 0) {

                int idx = getRandomIndex(mTags.size());

                fmi.add(mTags.get(idx));
            }
        }

        //generate a random number of new tags to add to shared list
        int numberOfNewSharedToAdd = getRandomIndex(4);

        for(int i = 0; i < numberOfNewSharedToAdd; i++){

            String testTag;

            if(i%2 ==0) {

                testTag =
                        getRandomAdjective() + " " +
                                getRandomNoun();
            }else{

                testTag =
                        getRandomNoun();
            }

            TagNode tmi = new TagNode(fmi, testTag);
            fmi.add(tmi);
            mTags.add(tmi);
        }

        //generate a random number of new tags to keep seperate
        int numberOfNewPrivateToAdd = getRandomIndex(3);

        for(int i = 0; i < numberOfNewPrivateToAdd; i++){

            String testTag;

            if(i%2 ==0) {

                testTag =
                        getRandomAdjective() + " " +
                                getRandomNoun();
            }else{

                testTag =
                        getRandomNoun();
            }

            TagNode tmi = new TagNode(fmi, testTag);
            fmi.add(tmi);
        }
    }

    private static void populateHashes(FileNode fmi) {

        int numberOfExistingToAdd = getRandomIndex(2);

        for(int i = 0; i < numberOfExistingToAdd; i++){

            if(mHashes.size() > 0) {

                int idx = getRandomIndex(mHashes.size());

                fmi.add(mHashes.get(idx));
            }
        }

        //generate a random number of new hashes to add to shared list
        int numberOfNewSharedToAdd = getRandomIndex(2);

        for(int i = 0; i < numberOfNewSharedToAdd; i++){

            HashNode hmi = getRandomHashModelItem(fmi);

            fmi.add(hmi);
            mHashes.add(hmi);
        }

        //generate a random number of new hashes to keep seperate
        int numberOfNewPrivateToAdd = getRandomIndex(2);

        for(int i = 0; i < numberOfNewPrivateToAdd; i++){

            HashNode hmi = getRandomHashModelItem(fmi);

            fmi.add(hmi);
        }
    }

    private static HashNode getRandomHashModelItem(FileNode fmi) {

            return new HashNode(fmi,
                    getRandomSha1HashString(),
                    getRandomTimeStamp());
    }

    private static String getRandomTimeStamp() {

        long offset = Timestamp.valueOf("2015-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2016-09-08 00:00:00").getTime();
        long diff = end - offset + 1;
        Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));

        Date dt = new Date(rand.getTime());

        return SynergyUtils.getTimeStamp_yyyyMMddHHmmss(dt);
    }

    private static String getRandomSha1HashString() {

        //just a very random string, all concatenated
        String randomString = getRandomAdjective() + getRandomNoun() +
                getRandomVerb() + getRandomNoun() + getRandomVerb();

        String output;

        try {

            output = Utils.computeSha1HashForText(randomString);

        }catch (Exception ex){

            //create empty hash
            output = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
        }

        return output;
    }

    private static void populateSynergyLists() {

        int numberToAdd = getRandomIndex(13);

        for(int i = 0; i < numberToAdd; i++){

            SynergyListNode lst =
                    new SynergyListNode(
                            WordUtils.capitalizeFully(getRandomAdjective()) +
                                    WordUtils.capitalizeFully(getRandomNoun())
                    );

            populateSynergyListItems(lst);

            mSynergyLists.add(lst);
        }
    }

    private static void populateFiles() {

        int numberToAdd = getRandomIndex(51);

        for(int i = 0; i < numberToAdd; i++){

            String deviceDesc =
                    WordUtils.capitalizeFully(getRandomAdjective()) + " " +
                            WordUtils.capitalizeFully(getRandomNoun());

            String path =
                    "/NWD/" + getRandomNoun() + "/" + getRandomNoun() + ".test";

            FileNode fmi = new FileNode(deviceDesc, path);

            populateTags(fmi);
            populateHashes(fmi);

            mFiles.add(fmi);
        }
    }

    private static void populateLocalConfig() {

        int numberToAdd = getRandomIndex(9);

        for(int i = 0; i < numberToAdd; i++){

            String key = getRandomNoun();
            String value = getRandomAdjective() + " " + getRandomNoun();

            mLocalConfig.add(new LocalConfigNode(key, value));
        }
    }

    private static void populateVerbs() {

        mVerbs.add("bless");
        mVerbs.add("phone");
        mVerbs.add("complete");
        mVerbs.add("encourage");
        mVerbs.add("end");
        mVerbs.add("paint");
        mVerbs.add("twist");
        mVerbs.add("tumble");
        mVerbs.add("slip");
        mVerbs.add("apologise");
        mVerbs.add("wobble");
        mVerbs.add("queue");
        mVerbs.add("fire");
        mVerbs.add("supply");
        mVerbs.add("communicate");
        mVerbs.add("annoy");
        mVerbs.add("attract");
        mVerbs.add("fetch");
        mVerbs.add("matter");
        mVerbs.add("crack");
        mVerbs.add("shade");
        mVerbs.add("hover");
        mVerbs.add("warm");
        mVerbs.add("change");
        mVerbs.add("rot");
        mVerbs.add("head");
        mVerbs.add("attack");
        mVerbs.add("wash");
        mVerbs.add("x-ray");
        mVerbs.add("grab");
        mVerbs.add("suspend");
        mVerbs.add("crush");
        mVerbs.add("shiver");
        mVerbs.add("intend");
        mVerbs.add("check");
        mVerbs.add("approve");
        mVerbs.add("clap");
        mVerbs.add("trap");
        mVerbs.add("excite");
        mVerbs.add("park");
        mVerbs.add("punch");
        mVerbs.add("judge");
        mVerbs.add("add");
        mVerbs.add("flood");
        mVerbs.add("sin");
        mVerbs.add("explain");
        mVerbs.add("mug");
        mVerbs.add("scorch");
        mVerbs.add("dare");
        mVerbs.add("jog");
        mVerbs.add("deceive");
        mVerbs.add("juggle");
        mVerbs.add("avoid");
        mVerbs.add("behave");
        mVerbs.add("kick");
        mVerbs.add("whine");
        mVerbs.add("number");
        mVerbs.add("escape");
        mVerbs.add("dress");
        mVerbs.add("hammer");
        mVerbs.add("mark");
        mVerbs.add("present");
        mVerbs.add("skip");
        mVerbs.add("drop");
        mVerbs.add("tremble");
        mVerbs.add("play");
        mVerbs.add("command");
        mVerbs.add("employ");
        mVerbs.add("pour");
        mVerbs.add("claim");
        mVerbs.add("dislike");
        mVerbs.add("blush");
        mVerbs.add("delight");
        mVerbs.add("welcome");
        mVerbs.add("ski");
        mVerbs.add("serve");
        mVerbs.add("thaw");
        mVerbs.add("appear");
        mVerbs.add("pretend");
        mVerbs.add("remove");
        mVerbs.add("chop");
        mVerbs.add("want");
        mVerbs.add("form");
        mVerbs.add("coil");
        mVerbs.add("untidy");
        mVerbs.add("signal");
        mVerbs.add("spill");
        mVerbs.add("permit");
        mVerbs.add("clip");
        mVerbs.add("bolt");
        mVerbs.add("hug");
        mVerbs.add("shave");
        mVerbs.add("release");
        mVerbs.add("brush");
        mVerbs.add("fax");
        mVerbs.add("train");
        mVerbs.add("cure");
        mVerbs.add("learn");
        mVerbs.add("arrive");
        mVerbs.add("concern");
    }

    private static void populateAdjectives() {

        mAdjectives.add("spiffy");
        mAdjectives.add("bloody");
        mAdjectives.add("grateful");
        mAdjectives.add("tacit");
        mAdjectives.add("alcoholic");
        mAdjectives.add("outstanding");
        mAdjectives.add("bright");
        mAdjectives.add("defiant");
        mAdjectives.add("silly");
        mAdjectives.add("successful");
        mAdjectives.add("nappy");
        mAdjectives.add("luxuriant");
        mAdjectives.add("condemned");
        mAdjectives.add("ceaseless");
        mAdjectives.add("few");
        mAdjectives.add("arrogant");
        mAdjectives.add("vacuous");
        mAdjectives.add("elfin");
        mAdjectives.add("imported");
        mAdjectives.add("faint");
        mAdjectives.add("open");
        mAdjectives.add("wiry");
        mAdjectives.add("kaput");
        mAdjectives.add("thoughtful");
        mAdjectives.add("reflective");
        mAdjectives.add("cruel");
        mAdjectives.add("economic");
        mAdjectives.add("adaptable");
        mAdjectives.add("frantic");
        mAdjectives.add("plain");
        mAdjectives.add("daily");
        mAdjectives.add("statuesque");
        mAdjectives.add("enthusiastic");
        mAdjectives.add("gusty");
        mAdjectives.add("wasteful");
        mAdjectives.add("remarkable");
        mAdjectives.add("damaging");
        mAdjectives.add("oafish");
        mAdjectives.add("lumpy");
        mAdjectives.add("tart");
        mAdjectives.add("distinct");
        mAdjectives.add("bent");
        mAdjectives.add("sedate");
        mAdjectives.add("various");
        mAdjectives.add("burly");
        mAdjectives.add("flimsy");
        mAdjectives.add("cloudy");
        mAdjectives.add("incandescent");
        mAdjectives.add("scintillating");
        mAdjectives.add("crooked");
        mAdjectives.add("beneficial");
        mAdjectives.add("woebegone");
        mAdjectives.add("rigid");
        mAdjectives.add("feigned");
        mAdjectives.add("synonymous");
        mAdjectives.add("utter");
        mAdjectives.add("adjoining");
        mAdjectives.add("naughty");
        mAdjectives.add("mighty");
        mAdjectives.add("tacky");
        mAdjectives.add("skillful");
        mAdjectives.add("slow");
        mAdjectives.add("chilly");
        mAdjectives.add("dangerous");
        mAdjectives.add("chemical");
        mAdjectives.add("useful");
        mAdjectives.add("absent");
        mAdjectives.add("thirsty");
        mAdjectives.add("aquatic");
        mAdjectives.add("tense");
        mAdjectives.add("horrible");
        mAdjectives.add("scandalous");
        mAdjectives.add("piquant");
        mAdjectives.add("cynical");
        mAdjectives.add("gamy");
        mAdjectives.add("physical");
        mAdjectives.add("workable");
        mAdjectives.add("willing");
        mAdjectives.add("comfortable");
        mAdjectives.add("guarded");
        mAdjectives.add("quickest");
        mAdjectives.add("incompetent");
        mAdjectives.add("valuable");
        mAdjectives.add("gaping");
        mAdjectives.add("trite");
        mAdjectives.add("vast");
        mAdjectives.add("wistful");
        mAdjectives.add("lively");
        mAdjectives.add("illegal");
        mAdjectives.add("devilish");
        mAdjectives.add("gentle");
        mAdjectives.add("finicky");
        mAdjectives.add("graceful");
        mAdjectives.add("wide-eyed");
        mAdjectives.add("super");
        mAdjectives.add("actually");
        mAdjectives.add("entertaining");
        mAdjectives.add("public");
        mAdjectives.add("mundane");
        mAdjectives.add("apathetic");
    }

    private static void populateNouns() {
        mNouns.add("silver");
        mNouns.add("key");
        mNouns.add("club");
        mNouns.add("mouth");
        mNouns.add("weight");
        mNouns.add("zinc");
        mNouns.add("bells");
        mNouns.add("skirt");
        mNouns.add("loaf");
        mNouns.add("dolls");
        mNouns.add("bike");
        mNouns.add("pizzas");
        mNouns.add("unit");
        mNouns.add("vacation");
        mNouns.add("song");
        mNouns.add("tomatoes");
        mNouns.add("dress");
        mNouns.add("pear");
        mNouns.add("scarecrow");
        mNouns.add("middle");
        mNouns.add("end");
        mNouns.add("argument");
        mNouns.add("tiger");
        mNouns.add("minister");
        mNouns.add("detail");
        mNouns.add("sink");
        mNouns.add("need");
        mNouns.add("health");
        mNouns.add("nut");
        mNouns.add("effect");
        mNouns.add("potato");
        mNouns.add("note");
        mNouns.add("town");
        mNouns.add("expansion");
        mNouns.add("boy");
        mNouns.add("mice");
        mNouns.add("rake");
        mNouns.add("change");
        mNouns.add("credit");
        mNouns.add("coast");
        mNouns.add("rest");
        mNouns.add("river");
        mNouns.add("roll");
        mNouns.add("twig");
        mNouns.add("smile");
        mNouns.add("judge");
        mNouns.add("floor");
        mNouns.add("furniture");
        mNouns.add("cent");
        mNouns.add("tendency");
        mNouns.add("notebook");
        mNouns.add("observation");
        mNouns.add("birds");
        mNouns.add("church");
        mNouns.add("cream");
        mNouns.add("monkey");
        mNouns.add("degree");
        mNouns.add("verse");
        mNouns.add("star");
        mNouns.add("government");
        mNouns.add("play");
        mNouns.add("respect");
        mNouns.add("approval");
        mNouns.add("car");
        mNouns.add("spark");
        mNouns.add("zephyr");
        mNouns.add("throne");
        mNouns.add("geese");
        mNouns.add("ground");
        mNouns.add("pollution");
        mNouns.add("discussion");
        mNouns.add("destruction");
        mNouns.add("place");
        mNouns.add("hammer");
        mNouns.add("decision");
        mNouns.add("transport");
        mNouns.add("view");
        mNouns.add("fly");
        mNouns.add("crime");
        mNouns.add("railway");
        mNouns.add("fruit");
        mNouns.add("orange");
        mNouns.add("match");
        mNouns.add("hydrant");
        mNouns.add("coal");
        mNouns.add("neck");
        mNouns.add("kittens");
        mNouns.add("peace");
        mNouns.add("pleasure");
        mNouns.add("ghost");
        mNouns.add("window");
        mNouns.add("group");
        mNouns.add("cover");
        mNouns.add("babies");
        mNouns.add("lunch");
        mNouns.add("run");
        mNouns.add("blade");
        mNouns.add("sponge");
        mNouns.add("turkey");
        mNouns.add("clam");
    }

    public static String getRandomNoun(){

        int idx = getRandomIndex(mNouns.size());
        return mNouns.get(idx);
    }

    public static String getRandomAdjective(){

        int idx = getRandomIndex(mAdjectives.size());
        return mAdjectives.get(idx);
    }

    public static String getRandomVerb(){

        int idx = getRandomIndex(mVerbs.size());
        return mVerbs.get(idx);
    }

    private static int getRandomIndex(int upperBoundExclusive){

        Random r = new Random();
        return r.nextInt(upperBoundExclusive);
    }
}
