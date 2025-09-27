package io.github.guiritter.war_thunder_model_lister;

import static java.lang.System.out;
import static java.nio.file.Files.newBufferedWriter;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.DIRECTORIES_ONLY;
import static javax.swing.JFileChooser.FILES_ONLY;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public final class WarThunderModelLister {
	private static File modelFolder;
	private static File outputFolder;
	private static BufferedWriter outputWriter;

	static {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
	}

	public static void main(String args[]) throws IOException {
		if (args.length > 0) {
			modelFolder = new File(args[0]);
			outputFolder = new File(args[1]);
		} else {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(DIRECTORIES_ONLY);
			chooser.setDialogTitle("Choose the model resources folder");
			if (chooser.showOpenDialog(null) != APPROVE_OPTION) {
				return;
			}
			modelFolder = chooser.getSelectedFile();
			if (modelFolder == null) {
				return;
			}
        	chooser.setFileSelectionMode(FILES_ONLY);
			chooser.setDialogTitle("Choose the output file");
			if (chooser.showOpenDialog(null) != APPROVE_OPTION) {
				return;
			}
			outputFolder = chooser.getSelectedFile();
			if (outputFolder == null) {
				return;
			}
		}

		outputWriter = newBufferedWriter(outputFolder.toPath());

		Stream.of(modelFolder.listFiles()).filter(WarThunderModelLister::byFileType).forEach(WarThunderModelLister::treatModelFile);

		outputWriter.close();

		out.println("Done.");
	}

	private static final boolean byFileType(File modelFile) {
		return modelFile.getName().endsWith(".grp");
	}

	private static final String removeFileExtension(String fileName) {
		int lastDotPosition = fileName.lastIndexOf('.');
		if (lastDotPosition == -1) {
			return fileName;
		}
		return fileName.substring(0, lastDotPosition);
	}

	private static final void treatModelFile(File modelFile) {
		out.println("Processing model file: " + modelFile.getName());
		try {
			outputWriter.write("==================================================");
			outputWriter.newLine();
			outputWriter.write("String            : ");
			outputWriter.write(removeFileExtension(modelFile.getName()));
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
