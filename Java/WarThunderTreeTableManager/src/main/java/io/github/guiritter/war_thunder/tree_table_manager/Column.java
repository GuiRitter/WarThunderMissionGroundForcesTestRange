package io.github.guiritter.war_thunder.tree_table_manager;

import static javax.swing.BoxLayout.Y_AXIS;

import java.awt.FlowLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Column {

	@JsonProperty
	private final LinkedList<Cell> cellList = new LinkedList<>();

	@JsonIgnore
	private int i;

	@JsonIgnore
	private final Consumer<String> onDownPressed = new Consumer<String>() {

		@Override
		public void accept(String id) {
			int pressedIndex;

			for (pressedIndex = 0; pressedIndex < cellList.size(); pressedIndex++) {
				if (cellList.get(pressedIndex).id.compareToIgnoreCase(id) == 0) {
					break;
				}
			}

			if (pressedIndex == (cellList.size() - 1)) {
				return;
			}

			var pressedCell = cellList.get(pressedIndex);
			var otherCell = cellList.get(pressedIndex + 1);

			Cell.switchContent(pressedCell, otherCell);
		}
	};

	@JsonIgnore
	private final Consumer<String> onUpPressed = new Consumer<String>() {

		@Override
		public void accept(String id) {
			int pressedIndex;

			for (pressedIndex = 0; pressedIndex < cellList.size(); pressedIndex++) {
				if (cellList.get(pressedIndex).id.compareToIgnoreCase(id) == 0) {
					break;
				}
			}

			if (pressedIndex == 0) {
				return;
			}

			var pressedCell = cellList.get(pressedIndex);
			var otherCell = cellList.get(pressedIndex - 1);

			Cell.switchContent(pressedCell, otherCell);
		}
	};

	@JsonIgnore
	public final JPanel panel;

	public void addCellBefore() {
		for (i = cellList.size() - 1; i > -1; i--) {
			if (cellList.get(i).isChecked()) {
				cellList.add(i, new Cell(onDownPressed, onUpPressed));
				panel.add(cellList.get(i).panel, i);
				cellList.get(i + 1).setChecked(false);
			}
		}
	}

	public void addCellLast() {
		for (Cell cell : cellList) {
			if (cell.isChecked()) {
				cellList.addLast(new Cell(onDownPressed, onUpPressed));
				panel.add(cellList.getLast().panel);
				setChecked(false);
				return;
			}
		}
	}

	public void addCellLast(String upper, String lower) {
		cellList.addLast(new Cell(onDownPressed, onUpPressed, upper, lower));
		panel.add(cellList.getLast().panel);
	}

	public void clear() {
		cellList.clear();
		panel.removeAll();
	}

	@JsonIgnore
	public List<Cell> getCheckedCellList() {
		return cellList.stream().filter(cell -> cell.isChecked()).collect(toList());
	}

	@JsonIgnore
	public String[][] getText() {
		String[][] returnArray = new String[cellList.size()][];
		for (i = 0; i < returnArray.length; i++) {
			returnArray[i] = cellList.get(i).getText();
		}
		return returnArray;
	}

	@JsonIgnore
	public boolean isBlank() {
		return cellList.isEmpty();
	}

	@JsonIgnore
	public boolean isChecked() {
		return cellList.stream().anyMatch((cell) -> (cell.isChecked()));
	}

	public void setChecked(boolean checked) {
		cellList.stream().forEach((cell) -> {
			cell.setChecked(checked);
		});
	}

	public void trim(){
		for (i = 0; i < cellList.size();){
			if (cellList.get(i).isBlank()) {
				cellList.remove(i);
				panel.remove(i);
			} else {
				i++;
			}
		}
		panel.validate();
	}

	public Column() {

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, Y_AXIS));
		panel.setAlignmentY(0);

		cellList.add(new Cell(onDownPressed, onUpPressed));
		panel.add(cellList.getLast().panel);
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.getContentPane().add((new Column()).panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
