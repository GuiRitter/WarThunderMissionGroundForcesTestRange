package io.github.guiritter.war_thunder.tree_table_manager;

import static java.util.Arrays.asList;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Cell {

	@JsonIgnore
	private final JCheckBox checkBox;

	@JsonIgnore
	public final JButton downButton;

	@JsonIgnore
	private final List<JTextField> fieldList;

	@JsonIgnore
	public final String id = UUID.randomUUID().toString();

	@JsonIgnore
	private final JTextField lowerField;

	@JsonIgnore
	private final Consumer<String> onDownPressed;

	@JsonIgnore
	private final Consumer<String> onUpPressed;

	@JsonIgnore
	public final JPanel panel;

	@JsonIgnore
	public final JPanel innerPanel;

	@JsonIgnore
	public final JButton upButton;

	@JsonIgnore
	private final JTextField upperField;

	@JsonProperty("lowerField")
	public String getLowerField() {
		return lowerField.getText();
	}

	@JsonIgnore
	public String[] getText() {
		return new String[] {upperField.getText(), lowerField.getText()};
	}

	@JsonProperty("upperField")
	public String getUpperField() {
		return upperField.getText();
	}

	@JsonIgnore
	public boolean isBlank() {
		return fieldList.stream().allMatch(field -> field.getText().isBlank());
	}

	@JsonIgnore
	public boolean isChecked() {
		return checkBox.isSelected();
	}

	public void setChecked(boolean checked) {
		checkBox.setSelected(checked);
	}

	public static final void switchContent(Cell cell0, Cell cell1) {
		switchContent(cell0, cell0.upperField.getText(), cell0.lowerField.getText(), cell1, cell1.upperField.getText(), cell1.lowerField.getText());
	}

	public static final void switchContent(Cell cell0, String upper0, String lower0, Cell cell1, String upper1, String lower1) {
		cell0.lowerField.setText(lower1);
		cell0.upperField.setText(upper1);
		cell1.lowerField.setText(lower0);
		cell1.upperField.setText(upper0);
	}

	@Override
	public String toString() {
		return String.format("{ class: Cell, upper: %s, lower: %s }", upperField.getText(), lowerField.getText());
	}

	public Cell(Consumer<String> onDownPressedListener, Consumer<String> onUpPressedListener) {

		onDownPressed = onDownPressedListener;
		onUpPressed = onUpPressedListener;

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, Y_AXIS));
		panel.setBorder(new EtchedBorder());
		panel.setAlignmentX(0.5f);

		innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, X_AXIS));
		panel.add(innerPanel);

		checkBox = new JCheckBox();
		checkBox.setAlignmentX(0.5f);
		checkBox.setMinimumSize(new Dimension(20, 20));
		checkBox.setPreferredSize(new Dimension(20, 20));
		checkBox.setMaximumSize(new Dimension(20, 20));
		checkBox.setOpaque(false);
		innerPanel.add(checkBox);

		upButton = new JButton("^");
		upButton.setAlignmentX(0.5f);
		upButton.setMargin(new Insets(0, 0, 0, 0));
		upButton.setMinimumSize(new Dimension(20, 20));
		upButton.setPreferredSize(new Dimension(20, 20));
		upButton.setMaximumSize(new Dimension(20, 20));
		upButton.setOpaque(false);
		upButton.addActionListener((event) -> onUpPressed.accept(id));
		innerPanel.add(upButton);

		downButton = new JButton("v");
		downButton.setAlignmentX(0.5f);
		downButton.setMargin(new Insets(0, 0, 0, 0));
		downButton.setMinimumSize(new Dimension(20, 20));
		downButton.setPreferredSize(new Dimension(20, 20));
		downButton.setMaximumSize(new Dimension(20, 20));
		downButton.setOpaque(false);
		downButton.addActionListener((event) -> onDownPressed.accept(id));
		innerPanel.add(downButton);

		upperField = new JTextField();
		upperField.setAlignmentX(0.5f);
		upperField.setMinimumSize(new Dimension(240, 20));
		upperField.setPreferredSize(new Dimension(240, 20));
		upperField.setMaximumSize(new Dimension(240, 20));
		upperField.setHorizontalAlignment(CENTER);
		panel.add(upperField);

		lowerField = new JTextField();
		lowerField.setAlignmentX(0.5f);
		lowerField.setMinimumSize(new Dimension(240, 20));
		lowerField.setPreferredSize(new Dimension(240, 20));
		lowerField.setMaximumSize(new Dimension(240, 20));
		lowerField.setHorizontalAlignment(CENTER);
		panel.add(lowerField);

		fieldList = asList(upperField, lowerField);
	}

	public Cell(Consumer<String> onDownPressedListener, Consumer<String> onUpPressedListener, String upper, String lower) {
		this(onDownPressedListener, onUpPressedListener);
		upperField.setText(upper);
		lowerField.setText(lower);
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.getContentPane().add((new Cell(null, null)).panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
