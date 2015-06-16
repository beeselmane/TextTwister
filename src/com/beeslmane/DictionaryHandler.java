package com.beeslmane;

import java.util.*;
import java.io.*;

public class DictionaryHandler
{
    private Map<String, List<String>> puzzleMap = new HashMap<>();

    public DictionaryHandler(String file)
    {
        for (String word : DictionaryHandler.readLines(file))
        {
            if (word.length() <= 2 || word.length() >= 7) continue;
            String key = DictionaryHandler.sortLetters(word);

            if (!this.puzzleMap.containsKey(key))
                this.puzzleMap.put(key, new ArrayList<>());

            this.puzzleMap.get(key).add(word);
        }
    }

    public List<String> search(String word)
    {
        String key = DictionaryHandler.sortLetters(word);
        return this.puzzleMap.get(key);
    }

    // Statics
    private static List<String> readLines(String filename)
    {
        try {
            Scanner scanner = new Scanner(new FileInputStream(filename));
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) lines.add(scanner.nextLine());
            scanner.close();
            return lines;
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    private static String sortLetters(String word)
    {
        char []letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }
}
