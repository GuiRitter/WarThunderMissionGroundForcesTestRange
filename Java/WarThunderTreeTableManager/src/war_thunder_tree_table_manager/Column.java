package war_thunder_tree_table_manager;

import java.awt.FlowLayout;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import static javax.swing.BoxLayout.Y_AXIS;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class Column {

    private final LinkedList<Cell> cellList = new LinkedList<>();

    private int i;

    public final JPanel panel;

//    public int x;

    public void addCellBefore() {
        for (i = cellList.size() - 1; i > -1; i--) {
            if (cellList.get(i).isChecked()) {
                cellList.add(i, new Cell());
                panel.add(cellList.get(i).panel, i);
                cellList.get(i + 1).setChecked(false);
            }
        }
    }

    public void addCellLast() {
        for (Cell cell : cellList) {
            if (cell.isChecked()) {
                cellList.addLast(new Cell());
                panel.add(cellList.getLast().panel);
                setChecked(false);
                return;
            }
        }
    }

    public void addCellLast(String upper, String lower) {
        cellList.addLast(new Cell(upper, lower));
        panel.add(cellList.getLast().panel);
    }

    public void clear() {
        cellList.clear();
        panel.removeAll();
    }

    public String[][] getText() {
        String[][] returnArray = new String[cellList.size()][];
        for (i = 0; i < returnArray.length; i++) {
            returnArray[i] = cellList.get(i).getText();
        }
        return returnArray;
    }

    public boolean isChecked() {
        return cellList.stream().anyMatch((cell) -> (cell.isChecked()));
    }

    public void setChecked(boolean checked) {
        cellList.stream().forEach((cell) -> {
            cell.setChecked(checked);
        });
    }

    public Column(/*int x*/) {
//        this.x = x;

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, Y_AXIS));
        panel.setAlignmentY(0);

        cellList.add(new Cell());
        panel.add(cellList.getLast().panel);
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add((new Column(/*5*/)).panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
