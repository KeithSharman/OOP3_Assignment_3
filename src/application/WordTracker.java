package application;

import java.io.*;
import java.util.Scanner;
import java.util.Map;
import java.util.ArrayList;

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

        // changed this bc original version only worked if arguments were in exact positions
        // This loops through args so "-f output.txt" can appear more safely
        this.outputFilePath = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-f") && i + 1 < args.length) {
                this.outputFilePath = args[i + 1];
            }
        }

        // Load previous BST 
        wordTree = RepoManager.load();

        processFile(inputPath);

        // Save updated BST
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

            // FIX: Required header line
            // WHY: Matches assignment/demo expectation (you lose marks without this)
            out.println("Displaying " + reportType + " format");

            Iterator<Word> it = wordTree.inorderIterator();

            while (it.hasNext()) {
                Word w = it.next();
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

        // I changed output format to match demo style closer

        switch (reportType) {

            case "-pf":
                // Print word and files only
                out.print("Key : ===" + w.getWord() + "=== found in file: ");
                out.println(String.join(", ", w.getFileData().keySet()));
                break;

            case "-pl":
                // Print word + file + line numbers
                out.print("Key : ===" + w.getWord() + "=== found in: ");

                for (Map.Entry<String, ArrayList<Integer>> entry : w.getFileData().entrySet()) {
                    out.print(entry.getKey() + " " + entry.getValue() + " ");
                }
                out.println();
                break;

            case "-po":
                // changed this to match assignment reqs closer. (line numbers + occurence frequency)
                // changed formatting to match examples.

                int totalCount = 0;

                // calculate total occurrences across all files
                for (ArrayList<Integer> lines : w.getFileData().values()) {
                    totalCount += lines.size();
                }

                out.print("Key : ===" + w.getWord() + "=== number of entries: " + totalCount + " ");

                for (Map.Entry<String, ArrayList<Integer>> entry : w.getFileData().entrySet()) {
                    String fileName = entry.getKey();
                    ArrayList<Integer> lines = entry.getValue();

                    out.print("found in file: " + fileName + " on lines: ");

                    // print line numbers with commas (matches demo format)
                    for (int i = 0; i < lines.size(); i++) {
                        out.print(lines.get(i));
                        if (i < lines.size() - 1) {
                            out.print(",");
                        }
                    }

                    out.print(" ");
                }

                out.println();
                break;

            default:
                out.println("Invalid flag. Use -pf, -pl, or -po.");
                break;
        }
    }
}