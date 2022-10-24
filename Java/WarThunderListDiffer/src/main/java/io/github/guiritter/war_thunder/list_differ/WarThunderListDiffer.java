package io.github.guiritter.war_thunder.list_differ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.FILES_ONLY;

public final class WarThunderListDiffer {

    private static String line;

    public static final void addLine(BufferedReader reader, List<String> list) throws IOException {
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith("String")) {
                continue;
            }
            line = line.substring(20);
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
        BufferedReader readerOld = Files.newBufferedReader(file.toPath());
        chooser.setDialogTitle("Choose the current version file");
        if (chooser.showOpenDialog(null) != APPROVE_OPTION) {
            return;
        }
        file = chooser.getSelectedFile();
        if (file == null) {
            return;
        }
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
            while ((indexOld < linesOld.size()) || (indexNew < linesNew.size())) {
                if (indexOld == linesOld.size()) {
                    linesOnlyNew.add(linesNew.get(indexNew));
                    indexNew++;
                } else if (indexNew == linesNew.size()) {
                    linesOnlyOld.add(linesOld.get(indexOld));
                    indexOld++;
                } else {
                    compare = linesOld.get(indexOld).compareTo(linesNew.get(indexNew));
                    if (compare < 0) {
                        linesOnlyOld.add(linesOld.get(indexOld));
                        indexOld++;
                    } else if (compare > 0) {
                        linesOnlyNew.add(linesNew.get(indexNew));
                        indexNew++;
                    } else {
                        indexOld++;
                        indexNew++;
                    }
                }
            }
            if (linesOnlyOld.isEmpty()) {
                writer.write("no lines only in old\n\n");
            } else {
                writer.write("lines only in old:\n\n");
                for (String line : linesOnlyOld) {
                    writer.write(line);
                    writer.write("\n");
                }
                writer.write("\n");
            }
            if (linesOnlyNew.isEmpty()) {
                writer.write("no lines only in new\n\n");
            } else {
                writer.write("lines only in new:\n\n");
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
