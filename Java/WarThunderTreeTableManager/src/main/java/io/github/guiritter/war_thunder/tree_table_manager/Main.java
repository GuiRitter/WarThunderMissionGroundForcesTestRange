package io.github.guiritter.war_thunder.tree_table_manager;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.FILES_ONLY;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class Main {

    private static final JFileChooser chooser;

    private static final JFrame frame;

    private static final JScrollPane pane;

    private static final Table table;

    static {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        chooser = new JFileChooser();
        chooser.setFileSelectionMode(FILES_ONLY);

        frame = new JFrame();
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        pane = new JScrollPane();
        frame.getContentPane().add(pane);

        table = new Table();
        pane.setViewportView(table.panel);

        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel);

        JButton addCellBeforeButton = new JButton("add cell before");
        addCellBeforeButton.addActionListener((ActionEvent e) -> {
            table.addCellBefore();
            frame.revalidate();
        });
        buttonPanel.add(addCellBeforeButton);

        JButton addCellLastButton = new JButton("add cell last");
        addCellLastButton.addActionListener((ActionEvent e) -> {
            table.addCellLast();
            frame.revalidate();
        });
        buttonPanel.add(addCellLastButton);

        JButton addColumnBeforeButton = new JButton("add column before");
        addColumnBeforeButton.addActionListener((ActionEvent e) -> {
            table.addColumnBefore();
            frame.revalidate();
        });
        buttonPanel.add(addColumnBeforeButton);

        JButton addColumnLastButton = new JButton("add column last");
        addColumnLastButton.addActionListener((ActionEvent e) -> {
            table.addColumnLast(false);
            frame.revalidate();
        });
        buttonPanel.add(addColumnLastButton);

        JButton switchButton = new JButton("switch");
        switchButton.addActionListener(e -> {
            table.switchCell();
            frame.revalidate();
        });
        buttonPanel.add(switchButton);

        JButton loadButton = new JButton("load");
        loadButton.addActionListener((ActionEvent e) -> {
            if (chooser.showOpenDialog(frame) != APPROVE_OPTION) {
                return;
            }
            File file;
            if ((file = chooser.getSelectedFile()) == null) {
                return;
            }
            List<String> lineList;
            try {
                lineList = Files.readAllLines(file.toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "error reading file", "error", ERROR_MESSAGE);
                return;
            }
            table.clear();
            String upperFieldArray[];
            String lowerFieldArray[];
            int cellI;
            for (int columnI = 0; columnI < lineList.size(); columnI += 2) {
                if ((columnI >= lineList.size()) || lineList.get(columnI).trim().isEmpty() || lineList.get(columnI + 1).trim().isEmpty()) {
                    break;
                }
                table.addColumnLast(true);
                upperFieldArray = lineList.get(columnI    ).split("\t");
                lowerFieldArray = lineList.get(columnI + 1).split("\t");
                if (upperFieldArray.length > lowerFieldArray.length) {
                    String temporary[] = new String[upperFieldArray.length];
                    System.arraycopy(lowerFieldArray, 0, temporary, 0, lowerFieldArray.length);
                    temporary[lowerFieldArray.length] = "";
                    lowerFieldArray = temporary;
                } else if (upperFieldArray.length < lowerFieldArray.length) {
                    String temporary[] = new String[lowerFieldArray.length];
                    System.arraycopy(upperFieldArray, 0, temporary, 0, upperFieldArray.length);
                    temporary[upperFieldArray.length] = "";
                    upperFieldArray = temporary;
                }
                for (cellI = 0; cellI < upperFieldArray.length; cellI++) {
                    table.addCellLastColumnLast(upperFieldArray[cellI], lowerFieldArray[cellI]);
                }
            }
            frame.revalidate();
        });
        buttonPanel.add(loadButton);

        JButton saveButton = new JButton("save");
        saveButton.addActionListener((ActionEvent e) -> {
            if (chooser.showSaveDialog(frame) != APPROVE_OPTION) {
                return;
            }
            File file;
            if ((file = chooser.getSelectedFile()) == null) {
                return;
            }
            BufferedWriter writer;
            try {
                writer = Files.newBufferedWriter(file.toPath(), CREATE, TRUNCATE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "error writing to file", "error", ERROR_MESSAGE);
                return;
            }
            String columnArray[][][] = table.getText();
            String cellArray[][];
            int columnI, cellI;
            int fieldArray[] = {0, 1};
            for (columnI = 0; columnI < columnArray.length; columnI++) {
                cellArray = columnArray[columnI];
                for (int fieldI : fieldArray) {
                    for (cellI = 0; cellI < cellArray.length; cellI++) {
                        if (cellI != 0) {
                            try {
                                writer.write("\t");
                            } catch (IOException ex) {}
                        }
                        try {
                            writer.write(cellArray[cellI][fieldI]);
                        } catch (IOException ex) {}
                    }
                    try {
                        writer.write("\n");
                    } catch (IOException ex) {}
                }
            }
            try {
                writer.flush();
                writer.close();
            } catch (IOException ex) {}
            JOptionPane.showMessageDialog(frame, "table written to file successfully");
        });
        buttonPanel.add(saveButton);
    }

    public static void main(String args[]) {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
