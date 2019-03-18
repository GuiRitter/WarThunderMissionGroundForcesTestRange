package io.github.guiritter.war_thunder.tree_table_manager;

import static io.github.guiritter.war_thunder.tree_table_manager.Cell.switchContent;
import static javax.swing.BoxLayout.X_AXIS;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public final class Table {

    private final LinkedList<Column> columnList = new LinkedList<>();

    private int i;

    public final JPanel panel;

    public void addCellBefore() {
        columnList.stream().forEach((column) -> {
            column.addCellBefore();
        });
    }

    public void addCellLast() {
        columnList.stream().forEach((column) -> {
            column.addCellLast();
        });
    }

    public void addCellLastColumnLast(String upper, String lower) {
        columnList.getLast().addCellLast(upper, lower);
    }

    public void addColumnBefore() {
        for (i = columnList.size() - 1; i > -1; i--) {
            if (columnList.get(i).isChecked()) {
                columnList.add(i, new Column());
                panel.add(columnList.get(i).panel, i);
                columnList.get(i + 1).setChecked(false);
            }
        }
    }

    public void addColumnLast(boolean clear) {
        columnList.addLast(new Column());
        panel.add(columnList.getLast().panel);

        if (clear) {
            columnList.getLast().clear();
        }
    }

    public void clear() {
        columnList.clear();
        panel.removeAll();
    }

    public List<Cell> getCheckedCellList() {
        return columnList.stream().flatMap(column -> column.getCheckedCellList().stream()).collect(Collectors.toList());
    }

    public String[][][] getText() {
        String[][][] returnArray = new String[columnList.size()][][];
        for (i = 0; i < returnArray.length; i++) {
            returnArray[i] = columnList.get(i).getText();
        }
        return returnArray;
    }

    public void switchCell() {
        List<Cell> cellList = getCheckedCellList();
        if (cellList.size() != 2) {
            return;
        }
        switchContent(cellList.get(0), cellList.get(1));
        cellList.forEach(cell -> cell.setChecked(false));
    }

    public Table() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, X_AXIS));

        columnList.add(new Column());
        panel.add(columnList.getLast().panel);
    }
}
