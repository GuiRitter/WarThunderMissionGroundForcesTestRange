package io.github.guiritter.war_thunder.list_differ;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.FILES_ONLY;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;

import static java.lang.System.out;

public final class WarThunderListDiffer {

    private static String line;

    private static String versionNew;
    private static String versionOld;

    public static final void addLine(BufferedReader reader, List<String> list) throws IOException {
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) {
                continue;
            }
            if (!list.contains(line)) {
                list.add(line);
            }
        }
    }

    public static void main(String args[]) throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(FILES_ONLY);
        chooser.setDialogTitle("Choose the penultimate version file");
        if (chooser.showOpenDialog(null) != APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        if (file == null) {
            return;
        }
        versionOld = file.getName().replaceAll("list (.+)[.]txt", "$1");
        BufferedReader readerOld = Files.newBufferedReader(file.toPath());
        chooser.setDialogTitle("Choose the current version file");
        if (chooser.showOpenDialog(null) != APPROVE_OPTION) {
            return;
        }
        file = chooser.getSelectedFile();
        if (file == null) {
            return;
        }
        versionNew = file.getName().replaceAll("list (.+)[.]txt", "$1");
        BufferedReader readerNew = Files.newBufferedReader(file.toPath());
        chooser.setDialogTitle(null);
        if (chooser.showSaveDialog(null) != APPROVE_OPTION) {
            return;
        }
        file = chooser.getSelectedFile();
        if (file == null) {
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
         file.toPath(), CREATE, TRUNCATE_EXISTING)) {
            List<String> linesOld = new LinkedList<>();
            List<String> linesNew = new LinkedList<>();
            {
                WarThunderListDiffer.addLine(readerOld, linesOld);
                readerOld.close();
                WarThunderListDiffer.addLine(readerNew, linesNew);
                readerNew.close();
            }
            Collections.sort(linesOld);
            Collections.sort(linesNew);
            int indexOld = 0;
            int indexNew = 0;
            List<String> linesOnlyOld = new LinkedList<>();
            List<String> linesOnlyNew = new LinkedList<>();
            int compare;
            String entryOld;
            String entryNew;
            while ((indexOld < linesOld.size()) || (indexNew < linesNew.size())) {
                entryOld = indexOld < linesOld.size() ? linesOld.get(indexOld) : null;
                entryNew = indexNew < linesNew.size() ? linesNew.get(indexNew) : null;

                // print a 80 characters line starting with entryOld and ending with entryNew
                // Copilot didn't do what I wanted but this is good enough
                if (entryOld != null && entryNew != null) {
                    out.println(String.format("%-80s", entryOld) + " | " + String.format("%-80s", entryNew));
                } else if (entryOld != null) {
                    out.println(String.format("%-80s", entryOld) + " | " + String.format("%-80s", ""));
                } else if (entryNew != null) {
                    out.println(String.format("%-80s", "") + " | " + String.format("%-80s", entryNew));
                }

                if (indexOld == linesOld.size()) {
                    linesOnlyNew.add(entryNew);
                    indexNew++;
                } else if (indexNew == linesNew.size()) {
                    linesOnlyOld.add(entryOld);
                    indexOld++;
                } else {
                    compare = entryOld.compareToIgnoreCase(entryNew);
                    if (compare < 0) {
                        linesOnlyOld.add(entryOld);
                        indexOld++;
                    } else if (compare > 0) {
                        linesOnlyNew.add(entryNew);
                        indexNew++;
                    } else {
                        indexOld++;
                        indexNew++;
                    }
                }
            }
            writer.write("lines only in " + versionOld + ":\n\n");

            if (!linesOnlyOld.isEmpty()) {
                for (String line : linesOnlyOld) {
                    writer.write(line);
                    writer.write("\n");
                }
                writer.write("\n");
            }

            writer.write("lines only in " + versionNew + ":\n\n");

            if (!linesOnlyNew.isEmpty()) {
                for (String line : linesOnlyNew) {
                    writer.write(line);
                    writer.write("\n");
                }
                writer.write("\n");
            }

            writer.flush();
        }
    }
}
