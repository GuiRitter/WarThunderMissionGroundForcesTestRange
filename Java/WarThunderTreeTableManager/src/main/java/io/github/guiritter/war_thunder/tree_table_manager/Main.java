package io.github.guiritter.war_thunder.tree_table_manager;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.FILES_ONLY;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.fasterxml.jackson.databind.ObjectMapper;

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
			ObjectMapper mapper = new ObjectMapper();
			Object object = null;
			try {
				object = mapper.readValue(
						Files.readString(file.toPath()),
						Map.class);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(frame, "error reading file", "error", ERROR_MESSAGE);
			}
			List<?> columnList = List.of();
			if (object instanceof Map) {
				Object columnListObject = ((Map<?, ?>) object).get("columnList");
				if (columnListObject instanceof List) {
					columnList = (List<?>) columnListObject;
				}
			}
			table.clear();
			columnList.forEach(columnObject -> {

				List<?> cellList = null;
				if (columnObject instanceof Map) {
					Object cellListObject = ((Map<?, ?>) columnObject).get("cellList");
					if (cellListObject instanceof List) {
						cellList = (List<?>) cellListObject;
					}
				}
				table.addColumnLast(true);

				cellList.forEach(cellObject -> {
					Map<?, ?> cell = null;
					if (cellObject instanceof Map) {
						cell = (Map<?, ?>) cellObject;
					}
					Object upperFieldObject = cell.get("upperField");
					Object lowerFieldObject = cell.get("lowerField");
					String upperField = null;
					String lowerField = null;
					if (upperFieldObject instanceof String) {
						upperField = (String) upperFieldObject;
					}
					if (lowerFieldObject instanceof String) {
						lowerField = (String) lowerFieldObject;
					}
					table.addCellLastColumnLast(upperField, lowerField);
				});
			});
			/**/
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
			try {
				writer.write((new ObjectMapper()).enable(INDENT_OUTPUT).writeValueAsString(table));
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
