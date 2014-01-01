package com.tehbeard.areablock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Constructs an argument pack to allow building commands eaiser
 * Takes an argument string as input and provides simple methods to access
 * boolean flags (-a , -b)
 * option flags (-f "flag")
 * Concatenating strings "this will not be 6 args"
 * @author james
 *
 */
public class ArgumentPack {

    private Set<String> boolFlags;
    private Map<String,String> flagOptions;
    private List<String> strArgs;

    public ArgumentPack(String[] boolFlags,String[] flagOpts,String[] rawArguments) {
        String r = "";
        for (String s : rawArguments) {
            if (r.length() > 0) {
                r+=" ";
            }
            r+=s;
        }
        initialise(boolFlags,flagOpts,r);
    }

    public ArgumentPack(String[] boolFlags,String[] flagOpts,String rawArguments) {
        initialise(boolFlags,flagOpts,rawArguments);
    }
    
    private void initialise(String[] boolFlags,String[] flagOpts,String rawArguments) {
        //initialise
        strArgs = new ArrayList<String>();
        this.boolFlags = new HashSet<String>();
        this.flagOptions = new HashMap<String, String>();
        System.out.println(rawArguments);
        boolean inQuotes = false;
        StringBuilder token = new StringBuilder();
        List<String> tokens = new ArrayList<String>();
        for (int i = 0; i< rawArguments.length();i++) {
            char c = rawArguments.charAt(i);
            switch(c) {
            case ' ':
                if (inQuotes) {
                    token.append(c);
                } else {
                    if (token.length() > 0) {
                        l("" + token.length());
                        tokens.add(token.toString().trim());
                    }
                    l("adding token");
                    token = new StringBuilder();
                }
                ;break;
            case '"':
                inQuotes = !inQuotes;
                l("Swapping to quote mode " +( inQuotes ?"on":"off"));
                break;
            default:
                token.append(c);
                break;
            }
        }
        if (token.length() > 0) {
            tokens.add(token.toString().trim());
        }
        //parse list of tokens

        Iterator<String> it = tokens.iterator();
        while (it.hasNext()) {
            String tok = it.next();
            //check if it's a potential option (optFlag off, starts with -
            if (tok.startsWith("-")) {
                String t = tok.substring(1);
                if (inArray(boolFlags, t)) {
                    this.boolFlags.add(t);
                    continue;
                }
                if (inArray(flagOpts, t)) {
                    if (it.hasNext()) {
                        l(t);
                        this.flagOptions.put(t, it.next());
                    }
                    continue;
                }
            }
            strArgs.add(tok);
        }
    }

    private boolean inArray(String[] arr,String search) {
        for (String a : arr) {
            if (a.equalsIgnoreCase(search)) {
                return true;
            }
        }
        return false;
    }

    public boolean getFlag(String flag) {
        return boolFlags.contains(flag);
    }
    
    public String getOption(String flag) {
        return flagOptions.get(flag); 
    }
    
    public int size() {
        return strArgs.size();
    }
    
    public String get(int index) {
        return strArgs.size() < index ? strArgs.get(index) : "";
    }

    public static void main(String[] args) {
        String arg = "create";
        String[] bool = {"a"};
        String[] opt = {"type","c","d"};
        ArgumentPack pack = new ArgumentPack(bool, opt,arg);

        System.out.println(pack.strArgs.toString());
        System.out.println(pack.boolFlags.toString());
        System.out.println(pack.flagOptions.toString());
        System.out.println(pack.getOption("d"));
    }

    private static void l(String l) {
        //System.out.println(l);
    }
}
