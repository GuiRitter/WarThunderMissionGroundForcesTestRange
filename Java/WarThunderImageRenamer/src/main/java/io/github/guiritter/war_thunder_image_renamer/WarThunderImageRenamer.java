package io.github.guiritter.war_thunder_image_renamer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFileChooser;

public class WarThunderImageRenamer {

	public static void main(String args[]) throws IOException {
		File imageFolder;

		if (args.length > 0) {
			imageFolder = new File(args[0]);
		} else {
			final JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choose the image folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			imageFolder = chooser.getSelectedFile();
		}

		var imageList = imageFolder.listFiles();

		for (var imageFile : imageList) {

			Files.move(imageFile.toPath(), imageFile.toPath().getParent().resolve(getNewName(imageFile.getName())));
		}
	}

	public static String getNewName(String name) {
		return name
				.replace("USSR.", "0 USSR.")
				.replace("Germany.", "1 Germany.")
				.replace("US.", "2 US.")
				.replace("UK.", "3 UK.")
				.replace("Japan.", "4 Japan.")
				.replace("Italy.", "5 Italy.")
				.replace("France.", "6 France.")
				.replace("China.", "7 China.")
				.replace("Sweden.", "8 Sweden.")
				.replace("Israel.", "9 Israel.")
				.replace("Other.", "A Other.")
				.replace("Train.", "B Train.");
	}
}
