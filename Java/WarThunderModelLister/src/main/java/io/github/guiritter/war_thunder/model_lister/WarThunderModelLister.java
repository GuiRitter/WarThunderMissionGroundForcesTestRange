package io.github.guiritter.war_thunder.model_lister;

import static java.lang.System.out;
import static java.nio.file.Files.newBufferedWriter;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.FILES_ONLY;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.swing.JFileChooser;

public final class WarThunderModelLister {

	private static String UHQ_VEHICLES_TANKS_REGEX = "/uhq_vehicles_tanks/([^\\s]+)\\.dxp\\.bin";
	private static String HQ_TEX_TANKS_REGEX = "/hq_tex_tanks/([^\\s]+)\\.dxp\\.bin";
	private static String TANKS_REGEX = "/tanks/([^\\s]+)\\.grp";
	private static String TANKS_HQ_REGEX = "/tanks/([^\\s]+)-hq\\.dxp\\.bin";

	private static File warThunderBlkFile;

	private static Set<String> modelSet = new HashSet<>();

	private static File outputFile;
	private static BufferedWriter outputWriter;

	private static List<Pattern> patternList = Stream.of(
			Pattern.compile(UHQ_VEHICLES_TANKS_REGEX),
			Pattern.compile(HQ_TEX_TANKS_REGEX),
			Pattern.compile(TANKS_REGEX),
			Pattern.compile(TANKS_HQ_REGEX)).toList();

	public static void main(String args[]) throws IOException {
		out.println("main " + Arrays.toString(args));
		if (args.length > 0) {
			warThunderBlkFile = new File(args[0]);
			outputFile = new File(args[1]);
		} else {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(FILES_ONLY);
			chooser.setDialogTitle("Choose the warthunder.blk file");
			if (chooser.showOpenDialog(null) != APPROVE_OPTION) {
				return;
			}
			warThunderBlkFile = chooser.getSelectedFile();
			if (warThunderBlkFile == null) {
				return;
			}
			chooser.setDialogTitle("Choose the output file");
			if (chooser.showOpenDialog(null) != APPROVE_OPTION) {
				return;
			}
			outputFile = chooser.getSelectedFile();
			if (outputFile == null) {
				return;
			}
		}

		Files.readAllLines(warThunderBlkFile.toPath(), StandardCharsets.ISO_8859_1).stream()
					.forEach(WarThunderModelLister::treatLine);

		outputWriter = newBufferedWriter(outputFile.toPath());

		modelSet.stream().sorted().forEach(WarThunderModelLister::treatModel);
		outputWriter.close();
	}

	private static final void treatLine(String line) {
		// out.println("treatLine " + line);
		Consumer<Pattern> method = pattern -> treatMatches(line, pattern);
		patternList.forEach(method);
	}

	private static final void treatMatches(String line, Pattern pattern) {
		// out.println("treatMatches " + line + " " + pattern.toString());
		Matcher matcher = pattern.matcher(line);

		while (matcher.find()) {
			// out.println("treatMatches " + matcher.group());
			modelSet.add(matcher.group(1));
		}
	}

	private static void treatModel(String model) {
		try {
			outputWriter.write("==================================================");
			outputWriter.newLine();
			outputWriter.write("String            : ");
			outputWriter.write(model);
			outputWriter.newLine();
			outputWriter.write("Value             : 0");
			outputWriter.newLine();
			outputWriter.write("==================================================");
			outputWriter.newLine();
			outputWriter.newLine();
			outputWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
