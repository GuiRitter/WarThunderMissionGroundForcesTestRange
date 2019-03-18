package io.github.guiritter.war_thunder.tree_table_manager;


import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import static javax.swing.BoxLayout.Y_AXIS;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.border.EtchedBorder;

public final class Cell {

    private final JCheckBox checkBox;

    private final JTextField lowerField;

    public final JPanel panel;

    private final JTextField upperField;

//    public int y;

    public String[] getText() {
        return new String[] {upperField.getText(), lowerField.getText()};
    }

    public boolean isChecked() {
        return checkBox.isSelected();
    }

    public void setChecked(boolean checked) {
        checkBox.setSelected(checked);
    }

    public Cell(/*int y*/) {
//        this.y = y;

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, Y_AXIS));
        panel.setBorder(new EtchedBorder());
        panel.setAlignmentX(0.5f);

        checkBox = new JCheckBox();
        checkBox.setAlignmentX(0.5f);
        checkBox.setMinimumSize(new Dimension(20, 20));
        checkBox.setPreferredSize(new Dimension(20, 20));
        checkBox.setMaximumSize(new Dimension(20, 20));
        checkBox.setOpaque(false);
        panel.add(checkBox);

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
    }

    public Cell(String upper, String lower) {
        this();
        upperField.setText(upper);
        lowerField.setText(lower);
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add((new Cell(/*1*/)).panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
