package application;

import java.io.*;
import java.util.*;
import implementations.*;
import serialization.RepoManager;
import utilities.Iterator;

public class WordTracker {
    private BSTree<Word> wordTree = new BSTree<>();
    private String reportType;
    private String outputFilePath;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f <output.txt>]");
            return;
        }

        new WordTracker().run(args);
    }

    public void run(String[] args) {
        String inputPath = args[0];
        this.reportType = args[1].toLowerCase();

        if (args.length == 4 && args[2].equalsIgnoreCase("-f")) {
            this.outputFilePath = args[3];
        } else {
            this.outputFilePath = null;
        }

        wordTree = RepoManager.Load();
        
        processFile(inputPath);

        RepoManager.save(wordTree);

        generateReport();
    }

    private void processFile(String path) {
        File file = new File(path);

        try (Scanner sc = new Scanner(file)) {
            int lineNum = 0;

            while (sc.hasNextLine()) {
                lineNum++;
                String line = sc.nextLine();

                String[] words = line
                        .replaceAll("[^a-zA-Z ]", "")
                        .toLowerCase()
                        .split("\\s+");

                for (String w : words) {
                    if (w.isEmpty()) {
                        continue;
                    }

                    Word searchWord = new Word(w, file.getName(), lineNum);

                    BSTreeNode<Word> node = wordTree.search(searchWord);

                    if (node != null) {
                        node.getElement().addOccurrence(file.getName(), lineNum);
                    } else {
                        wordTree.add(searchWord);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found at " + path);
        }
    }

    private void generateReport() {
        PrintStream out = System.out;

        try {
            if (outputFilePath != null) {
                out = new PrintStream(new FileOutputStream(outputFilePath));
            }

            
            Iterator<Word> it = wordTree.inorderIterator(); 
            
            while (it.hasNext()) {
                Word w = (Word) it.next(); 
                displayWord(w, out);
            }
            
            if (outputFilePath != null) {
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Reporting Error: " + e.getMessage());
        }
    }

    private void displayWord(Word w, PrintStream out) {
        out.print("Word: " + w.getWord());

        switch (reportType) {
            case "-pf":
                out.println(" | Files: " + w.getFileData().keySet());
                break;

            case "-pl":
                out.println(" | Occurrences: " + w.getFileData());
                break;

            case "-po":
                int totalCount = 0;

                for (ArrayList<Integer> lines : w.getFileData().values()) {
                    totalCount += lines.size();
                }

                out.println(" | Total Count: " + totalCount +
                            " | Details: " + w.getFileData());
                break;

            default:
                out.println(" | Invalid flag. Use -pf, -pl, or -po.");
                break;
        }
    }
}
