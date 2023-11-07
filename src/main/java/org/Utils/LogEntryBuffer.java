package org.Utils;

import org.Constants.AllTheConstants;

import java.util.Observable;

public class LogEntryBuffer extends Observable {
    private String p_logMsg = "";
    public String getLogMsg(){return this.p_logMsg; }

    private String renderSeparator(){
        return AllTheConstants.consoleSepartorString.repeat(AllTheConstants.CONSOLE_WIDTH);
    }

    private String renderSubHeading(String l_toBeRendered){
        Integer l_halfLengthOfString = l_toBeRendered.length() / 2;
        Integer l_halfLengthOfAvailableSpace = AllTheConstants.CONSOLE_WIDTH / 2;
        int l_pointTillSymbolHasToBePrinted = l_halfLengthOfAvailableSpace - l_halfLengthOfString;
        StringBuilder l_stringToBeRendered = new StringBuilder();
        l_stringToBeRendered.append(System.lineSeparator());
        l_stringToBeRendered.append(AllTheConstants.consoleSepartorString.repeat(Math.max(0, l_pointTillSymbolHasToBePrinted)));
        l_stringToBeRendered.append(l_toBeRendered);
        l_stringToBeRendered.append(AllTheConstants.consoleSepartorString.repeat(Math.max(0, AllTheConstants.CONSOLE_WIDTH - l_stringToBeRendered.length())));
        l_stringToBeRendered.append(System.lineSeparator());
        return l_stringToBeRendered.toString();
    }

    private String renderCenteredString(int p_width, String p_s) {
        String l_centeredString = String.format("%-" + p_width  + "s", String.format("%" + (p_s.length() +
                (p_width - p_s.length()) / 2) + "s", p_s));
        return String.format(l_centeredString+"\n");
    }


    public void writeToBuffer(LogLevel p_level, String p_logString){
        switch (p_level){
            case BASICLOG -> p_logMsg += p_logString + System.lineSeparator();
            case HEADING -> {
                p_logMsg += (System.lineSeparator() + renderSeparator() + System.lineSeparator());
                p_logMsg += (renderCenteredString(AllTheConstants.CONSOLE_WIDTH,p_logString));
                p_logMsg += (renderSeparator() + System.lineSeparator());
            }
            case SUBHEADING -> {
                p_logMsg += renderSubHeading(p_logString);
            }

        }
        setChanged();
        notifyObservers(p_logMsg);
        this.p_logMsg = "";
    }
}
