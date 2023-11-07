package org.Constants;

import java.util.*;

public class AllTheConstants {

    public static final HashSet<String> allMapCommands = new HashSet<>(Arrays.asList("editcontinent","editcountry",
            "editneighbor","showmap","savemap","editmap","loadmap","validatemap","gameplayer","assigncountries","deploy"
    ,"advance","bomb","blockade","airlift","negotiate","exit"));
    public static final String defaultMapLocationAppendString = "src/main/resources/Map/";
    public static final String defaultLogLocationAppendString = "src/logs/";
    public static final String continentsSeparator = "[continents]";
    public static final String countrySeparator = "[countries]";
    public static final String bordersSeparator = "[borders]";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\u001B[47m";
    public static final String consoleSepartorString = "*";
    public static final int CONSOLE_WIDTH = 90;
    public static final List<String> COLORS = Arrays.asList(RED, GREEN, YELLOW, BLUE, PURPLE, CYAN);
    public static final List<String> CARDS = Arrays.asList("bomb", "blockade", "airlift", "negotiate");

}

