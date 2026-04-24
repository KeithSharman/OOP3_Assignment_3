package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Word implements Serializable, Comparable<Word> {
    private static final long serialVersionUID = 1L;
    
    private String word;
    private Map<String, ArrayList<Integer>> fileData;

    public Word(String word, String fileName, int lineNumber) {
        this.word = word;
        this.fileData = new HashMap<>();
        addOccurrence(fileName, lineNumber);
    }

    public void addOccurrence(String fileName, int lineNumber) {
        fileData.putIfAbsent(fileName, new ArrayList<>());

        ArrayList<Integer> lines = fileData.get(fileName);

        // had to change this to prevent duplicate line numbers from being added
        // The same word was appearing twice during testing 
        if (!lines.contains(lineNumber)) {
            lines.add(lineNumber);
        }
    }

    @Override
    public int compareTo(Word other) {
        return this.word.compareTo(other.word);
    }

    public String getWord() { return word; }
    public Map<String, ArrayList<Integer>> getFileData() { return fileData; }
}