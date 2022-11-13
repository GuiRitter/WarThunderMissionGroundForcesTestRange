package io.github.guiritter.war_thunder.ground_attack_test_range_generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class MissionGenerator {

	public static void main(String args[]) throws IOException {
		System.out.println(MissionGenerator.class.getName());
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		// int FORCES_AIR = 0;
		int FORCES_GROUND = 1;
		File inputFolder;
		if (args.length > 0) {
			inputFolder = new File(args[0]);
		} else {
			final JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			inputFolder = chooser.getSelectedFile();
		}
		final String forces[] = {"air", "ground"};
		final String factions[] = {"USSR", "Germany", "US", "UK", "Japan", "Italy", "France", "China", "Sweden", "Israel", "other"};
		final LinkedList<String> lines = new LinkedList<>();
		Cell fields[];
		int width = 0;
		int height;
		String depth; // was supposed to be height
		int centerX;   // x measured range: 1760 – 2860 // ground center: 2310 // air center: 0
		int centerZ;   // z measured range: 1780 – 2100 // ground center: 1940 // air center: 0
		int distanceX; // x distance parallel to the tracks // close as possible: 5 // ground normal: 10 // air normal: 200
		double distanceZ; // z distance perpendicular to the tracks // close as possible: 2 // ground normal: 10 // air normal: 200
		List<String> tankModel = Files.readAllLines(inputFolder.toPath().resolve("tank model.txt"));
		Table table;
		for (boolean screenshot : new boolean[]{false, true}) {
			for (String faction : factions) {
				table = (new ObjectMapper()).readValue(inputFolder.toPath().resolve(faction + ".json").toFile(), Table.class);
				/*
				width = 0;
				for (String input : inputList) {
					width = Math.max(input.split("\t").length, width);
				}
				/**/
				width = table.columnList
						.stream()
						.reduce(0, (previous, current) -> Math.max(previous, current.cellList.size()), (a, b) -> Math.max(a, b));
				/*
				height = inputList.size() / 2;
				/**/
				height = table.columnList.size();
				for (String force : forces) {
					lines.clear();
					if (screenshot) {
						if (force.equals(forces[FORCES_GROUND])) {
							lines.addAll(Files.readAllLines(
							 inputFolder.toPath().resolve(
							  faction + " " + force + " header 0 screenshot.txt")));
							centerX = 8192;//2310;//3200
							centerZ = 8192;//1940;//200
							distanceX = 6;
							distanceZ = 2.5;
							depth = "1";
							lines.add("    tm:m=[[-1, 0, 0] [0, 1, 0] [0, 0, -1] ["
							 + 7168 + ", 1, " + centerZ + "]]");
							lines.addAll(Files.readAllLines(
							 inputFolder.toPath().resolve(
							  faction + " " + force + " header 1.txt")));
						} else {
							continue;
							/*
							centerX = 0;
							centerZ = 0;
							distanceX = 6;
							distanceZ = 3;
							depth = "0.01";
							lines.addAll(Files.readAllLines(
							 inputFolder.toPath().resolve(
							  faction + " " + force + " header screenshot.txt")));
							*/
						}
					} else {
						if (force.equals(forces[FORCES_GROUND])) {
							lines.addAll(Files.readAllLines(
							 inputFolder.toPath().resolve(
							  faction + " " + force + " header 0.txt")));
							centerX = 8192;//2310;
							centerZ = 8192;//1940;
							distanceX = 10;
							distanceZ = 10;
							depth = "1";//"220";
							lines.add("    tm:m=[[-1, 0, 0] [0, 1, 0] [0, 0, -1] ["
							 + (((((-1) * 2) - width + 1) * distanceX) + centerX)
							 + ", " + depth + ", " + centerZ + "]]");
							lines.addAll(Files.readAllLines(
							 inputFolder.toPath().resolve(
							  faction + " " + force + " header 1.txt")));
						} else {
							centerX = 0;
							centerZ = 0;
							distanceX = 200;
							distanceZ = 200;
							depth = "0.01";
							lines.addAll(Files.readAllLines(
							 inputFolder.toPath().resolve(
							  faction + " " + force + " header.txt")));
						}
					}
					lines.add("");
					for (int y = 0; y < height; y++) {
						/*
						fields = inputList.get((2 * y) + 1).split("\t");
						/**/
						fields = table.columnList.get(y).cellList.toArray(new Cell[]{});
						for (int x = 0; x < fields.length; x++) {
							try {
								if (fields[x].lowerField.isBlank()) {
									continue;
								}
								lines.add("  tankModels{");
								lines.add("    name:t=\"tank_" + y + "_" + x + "\"");
								lines.add("    tm:m=[[-1, 0, 0] [0, 1, 0] [0, 0, -1] ["
								 + ((((x * 2) - width + 1) * distanceX) + centerX)
								 + ", " + depth + ", "
								 + ((((double) ((y * 2) - height + 1)) * distanceZ) + ((double) centerZ)) // TODO BigDecimal
								 + "]]");
								lines.add("    unit_class:t=\"" + fields[x].lowerField + "\"");
								lines.addAll(tankModel);
								lines.add("");
							} catch(Exception ex) {
								ex.printStackTrace();
							}
						}
					}
					lines.addAll(Files.readAllLines(
					 inputFolder.toPath().resolve(String.format("%s footer.txt", faction))));
					Files.write(inputFolder.toPath().resolve("Ground_"
					 + (force.equals("air") ? "Attack" : "Forces")
					 + "_Test_Range_" + faction + (screenshot ? "_screenshot" : "") +".blk"), lines,
					 StandardOpenOption.CREATE,
					 StandardOpenOption.TRUNCATE_EXISTING);
				}
			}
		}
	}
}
