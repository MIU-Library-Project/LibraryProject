package ui;

import Entity.CheckOutRecord;
import Entity.CheckOutRecordEntry;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel {
    private List<CheckOutRecordEntry> checkOutRecordEntryList;
    private String[] columnNames = {"Book title", "Checkout date", "Due date"};

    TableModel(List<CheckOutRecordEntry> checkOutRecordEntryList) {
        this.checkOutRecordEntryList = checkOutRecordEntryList;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return checkOutRecordEntryList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CheckOutRecordEntry checkOutRecordEntry = checkOutRecordEntryList.get(rowIndex);

        if (columnIndex == 0) {
            return checkOutRecordEntry.getBookCopy().getCopyNumber();
        } else if (columnIndex == 1) {
            return checkOutRecordEntry.getDateofCheckout();
        } else {
            return checkOutRecordEntry.getDueDate();
        }
    }
}
