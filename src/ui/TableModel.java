package ui;

import Entity.*;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel {
    private List<CheckOutRecordEntry> checkOutRecordEntryList;
    private String[] columnNames = {"Book title", "Book Author", "Book id", "Checkout date", "Due date"};

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

        BookCopy bookCopy = checkOutRecordEntry.getBookCopy();
        Book book = bookCopy.getBook();
        String authors = getAllAuthors(book.getAuthors());

        if (columnIndex == 0) {
            return book.getTitle();
        } else if (columnIndex == 1) {
            return authors;
        } else if (columnIndex == 2) {
            return bookCopy.getCopyNumber();
        } else if (columnIndex == 3) {
            return checkOutRecordEntry.getDateofCheckout();
        } else {
            return checkOutRecordEntry.getDueDate();
        }
    }

    private String getAllAuthors(List<Author> authors) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Author author : authors) {
            stringBuilder.append(author.getFirstName())
                    .append(" ")
                    .append(author.getLastName())
                    .append("\n");
        }

        return stringBuilder.toString();
    }
}
