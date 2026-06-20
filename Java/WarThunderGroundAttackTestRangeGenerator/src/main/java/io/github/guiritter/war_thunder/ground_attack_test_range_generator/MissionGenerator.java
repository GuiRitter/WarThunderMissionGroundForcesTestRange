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

/**
 * Generates mission *.blk files for each mission, faction and scenario.
 */
public final class MissionGenerator {

	/**
	 * Scenario where end users will play in.
	 */
	public static final String SCENARIO_RELEASE = "release";

	/**
	 * Scenario with more tightly packed vehicles, just to take screenshots. Only used in ground forces.
	 */
	public static final String SCENARIO_SCREENSHOT = "screenshot";

	/**
	 * Scenario that only contains the vehicles added last. Used to test if the newly added vehicles work as intended.
	 */
	public static final String SCENARIO_QA = "quality assurance";

	public static final String AIR = "air";
	public static final String GROUND = "ground";

	public static final String USSR = "USSR";
	public static final String GERMANY = "Germany";
	public static final String US = "US";
	public static final String UK = "UK";
	public static final String JAPAN = "Japan";
	public static final String ITALY = "Italy";
	public static final String FRANCE = "France";
	public static final String CHINA = "China";
	public static final String SWEDEN = "Sweden";
	public static final String ISRAEL = "Israel";
	public static final String OTHER = "other";
	public static final String TRAIN = "train";
	public static final String GHOST = "ghost";

	// get X distance based on faction
	public static final int getDistanceX(String faction) {
		if (faction == TRAIN) {
			return 8;
		} else {
			return 6;
		}
	}

	// get Z distance based on faction
	public static final double getDistanceZ(String faction) {
		if (faction == TRAIN) {
			return 8;
		} else {
			return 2.5;
		}
	}

	// get orientation based on faction
	public static final String getOrientation(String faction) {
		if (faction == TRAIN) {
			return "[-0.707107, 0, 0.707107] [0, 1, 0] [-0.707107, 0, -0.707107]";
		} else {
			return "[-1, 0, 0] [0, 1, 0] [0, 0, -1]";
		}
	}

	/**
	 * Returns a suffix for the name of the mission file according to the scenario.
	 * @param scenario the scenario
	 * @return the suffix
	 */
	public static final String getScenarioSuffix(String scenario) {
		return switch (scenario) {
			case SCENARIO_SCREENSHOT -> "_screenshot";
			case SCENARIO_QA -> "_qa";
			default -> "";
		};
	}

	/**
	 * Returns a suffix for the name of the mission file according to the scenario.
	 * @param scenario the scenario
	 * @return the suffix
	 */
	public static final String getScenarioFileNamePart(String scenario) {
		return switch (scenario) {
			case SCENARIO_SCREENSHOT -> " screenshot";
			case SCENARIO_QA -> " qa";
			default -> "";
		};
	}

	/**
	 * Generates the missions and saves the files.
	 * @param args optinal command line arguments that replace visual prompts
	 * @throws IOException probably related to file IO
	 */
	public static void main(String args[]) throws IOException {
		System.out.println(MissionGenerator.class.getName());
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		int FORCES_AIR = 0;
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
		final String forces[] = {AIR, GROUND};
		final String factions[] = {USSR, GERMANY, US, UK, JAPAN, ITALY, FRANCE, CHINA, SWEDEN, ISRAEL, OTHER, TRAIN, GHOST};
		final String scenarios[] = {SCENARIO_RELEASE, SCENARIO_SCREENSHOT, SCENARIO_QA};
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
		for (var scenario : scenarios) {
			for (String faction : factions) {
				table = (new ObjectMapper()).readValue(inputFolder.toPath().resolve(faction + ((scenario.equals(SCENARIO_QA)) ? "_qa" : "") + ".json").toFile(), Table.class);
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
					if (scenario.equals(SCENARIO_SCREENSHOT)) {
						if (force.equals(forces[FORCES_GROUND])) {
							lines.addAll(Files.readAllLines(
							 inputFolder.toPath().resolve(
							  faction + " " + force + " header 0" + getScenarioFileNamePart(scenario) + ".txt")));
							centerX = 8192;//2310;//3200
							centerZ = 8192;//1940;//200
							distanceX = getDistanceX(faction);
							distanceZ = getDistanceZ(faction);
							depth = "1";
							lines.add("    tm:m=[" + getOrientation(faction) + " ["
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
							  faction + " " + force + " header 0" + getScenarioFileNamePart(scenario) + ".txt")));
							centerX = 8192;//2310;
							centerZ = 8192;//1940;
							distanceX = getDistanceX(faction);
							distanceZ = getDistanceZ(faction);
							depth = "1";//"220";
							lines.add("    tm:m=[" + getOrientation(faction) + " ["
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
							  faction + " " + force + " header" + getScenarioFileNamePart(scenario) + ".txt")));
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
								lines.add("    tm:m=[" + getOrientation(faction) + " ["
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
					 + (force.equals(forces[FORCES_AIR]) ? "Attack" : "Forces")
					 + "_Test_Range_" + faction + getScenarioSuffix(scenario) +".blk"), lines,
					 StandardOpenOption.CREATE,
					 StandardOpenOption.TRUNCATE_EXISTING);
				}
			}
		}
	}
}
