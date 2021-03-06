package Contoller;

import DataAccess.DataAccess;
import DataAccess.DataAccessFacade;
import Entity.*;
import ui.AddBookCopyUIForm;
import ui.CheckOutUIForm;
import ui.CheckoutRecordPrintUI;
import ui.LoginUI;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SystemController {

    private static SystemController instance;
    private DataAccess dataAccess = new DataAccessFacade();
    private List<Author> authorsList;

    private SystemController() {
    }

    public static SystemController getInstance() {
        if (instance == null) instance = new SystemController();
        return instance;
    }

    public void addMember(String memberNo, String firstName, String lastName, String phoneNumber,
                          String state, String city, String street, int zip) {

        Address address = addAddress(state, city, street, zip);
        LibraryMember libraryMember = new LibraryMember(memberNo, firstName, lastName, phoneNumber, address);

        dataAccess.saveNewMember(libraryMember);


    }


    public void checkOutBook(String memberId, String isbn, CheckOutUIForm checkOutUIForm) {

        Book book = dataAccess.searchBook(isbn);

        if (dataAccess.searchMember(memberId) && book != null) {

            BookCopy availableBookCopy = dataAccess.nextAvailableBookCopy(isbn);

            if (availableBookCopy == null) {
                checkOutUIForm.displayBookUnavailable();
            } else {

                LocalDate todaysDate = LocalDate.now();
                int checkOutLength = dataAccess.getMaximumCheckoutLength(isbn);
                LocalDate dueDate = todaysDate.plusDays(checkOutLength);

                CheckOutRecordEntry checkoutRecordEntry = new CheckOutRecordEntry(todaysDate, dueDate, availableBookCopy);
                availableBookCopy.setAvailability(false);

                dataAccess.saveMemberCheckoutRecord(memberId, checkoutRecordEntry);
                checkOutUIForm.displayCheckoutSuccess();

            }

        } else if (!dataAccess.searchMember(memberId)) {
            checkOutUIForm.displayMemberUnavailable();

        } else if (dataAccess.searchBook(isbn) == null) {
            checkOutUIForm.displayBookUnavailable();
        }

    }


    public void addBookCopy(String isbn, int copyNumber, AddBookCopyUIForm bookCopyUIForm) {

        Book book = dataAccess.searchBook(isbn);
        if (book != null) {

            BookCopy bookCopy = new BookCopy(copyNumber);

            book.addBookCopy(bookCopy);
            dataAccess.saveNewBookCopy(bookCopy);
            bookCopyUIForm.displayBookAddedUI();

        } else {
            bookCopyUIForm.displayBookNotFoundUI();
        }

    }

    public void searchCheckOutRecords(String memberId, CheckoutRecordPrintUI checkoutRecordPrintUI) {

        boolean found = dataAccess.searchMember(memberId);

        if(found) {

            List<CheckOutRecordEntry> recordEntries = dataAccess.getCheckOutRecord(memberId);

            if(recordEntries == null) {
                checkoutRecordPrintUI.displayNoRecordsFound();
            }else {
               checkoutRecordPrintUI.showRecords(recordEntries);
            }


        }else {
            checkoutRecordPrintUI.displayUserNotFound();
        }


    }

    public void addBook(String title, String isbn, List<Author> authors) {
        Book book = new Book(title, isbn, 10, authors);
        dataAccess.saveNewBook(book);
    }

    public List<Author> getAuthorsList() {
        if (authorsList == null) return new ArrayList<>();
        return authorsList;
    }

    public void addAuthors(Author author) {
        if (authorsList == null) authorsList = new ArrayList<>();
        authorsList.add(author);
    }

    public void login(int id, String password, LoginUI loginUI) {
        Role role = dataAccess.verifyUser(id, password);
        if (role == null)
            loginUI.displayLoginError();
        else {
            loginUI.displayUI(role);
        }
    }

    private Address addAddress(String state, String city, String street, int zip) {
        return new Address(state, city, street, zip);
    }
}
