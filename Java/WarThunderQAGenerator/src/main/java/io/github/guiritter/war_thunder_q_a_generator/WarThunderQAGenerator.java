package io.github.guiritter.war_thunder_q_a_generator;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

import javax.swing.JFileChooser;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WarThunderQAGenerator {

	public static final List<TechTree> techTreeList = new LinkedList<>();

	public static ObjectMapper jsonMapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false).enable(INDENT_OUTPUT);

	static {
		techTreeList.add(new TechTree("China", "cn"));
		techTreeList.add(new TechTree("France", "fr"));
		techTreeList.add(new TechTree("Germany", "germ"));
		techTreeList.add(new TechTree("Israel", "il"));
		techTreeList.add(new TechTree("Italy", "it"));
		techTreeList.add(new TechTree("Japan", "jp"));
		techTreeList.add(new TechTree("Sweden", "sw"));
		techTreeList.add(new TechTree("UK", "uk"));
		techTreeList.add(new TechTree("US", "us"));
		techTreeList.add(new TechTree("USSR", "ussr"));
	}

	public static void main(String args[]) throws IOException {
		File listDiffFile;
		File dataFolder;

		if (args.length > 1) {
			listDiffFile = new File(args[0]);
			dataFolder = new File(args[1]);
		} else {
			final JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choose the list diff file");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			listDiffFile = chooser.getSelectedFile();

			chooser.setDialogTitle("Choose the data folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			dataFolder = chooser.getSelectedFile();
		}

		String line = "";
		var reader = Files.newBufferedReader(listDiffFile.toPath());
		boolean isAfterLinesOnlyInNew = false;
		var map = new HashMap<String, List<String>>();
		String treeBlk;

		for (TechTree techTree : techTreeList) {
			map.put(techTree.blk, new LinkedList<>());
		}

		while ((line = reader.readLine()) != null) {
			if (isAfterLinesOnlyInNew) {
				if (!line.trim().isEmpty()) {
					treeBlk = getTreeBlk(line);
					map.get(treeBlk).add(line);
				}
			} else if (line.compareToIgnoreCase("lines only in new:") == 0) {
				isAfterLinesOnlyInNew = true;
			}
		}

		reader.close();

		out.println(map);

		List<String> blkList;
		Table table;
		Column column;
		List<Cell> cellList;
		File outputFile;

		for (TechTree techTree : techTreeList) {
			blkList = map.get(techTree.blk);
			if (blkList.isEmpty()) {
				continue;
			}

			cellList = blkList.stream().map((String blk) -> new Cell(blk, blk)).collect(toList());
			column = new Column(cellList);
			table = new Table(column);

			outputFile = dataFolder.toPath().resolve(techTree.jsonName + "_qa.json").toFile();
			jsonMapper.writeValue(outputFile, table);
		}
	}

	private static String getTreeBlk(String blk) {
		return blk.substring(0, blk.indexOf('_'));
	}
}
