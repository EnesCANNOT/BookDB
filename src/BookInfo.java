import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BookInfo {
    JFrame frame;

    JLabel labelTitle;
    JLabel labelAuthor;
    JLabel labelPublisher;
    JLabel labelPublicationDate;
    JLabel labelId;

    JTextField tfBookTitle;
    JTextField tfBookAuthor;
    JTextField tfBookPublisher;
    JTextField tfBookPublicationDate;
    JTextField tfBookId;

    JButton buttonSave;
    JButton buttonDelete;
    JButton buttonUpdate;
    JButton buttonSearch;

    String bookTitle;
    String bookAuthor;
    String bookPublisher;
    String bookPublicationDate;
    String bookId;

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public BookInfo(){

        Connect();

        frame = new JFrame("Book Frame");

        bookTitle = "";
        bookAuthor = "";
        bookPublisher = "";
        bookPublicationDate = "";
        bookId = "";

        labelTitle = new JLabel("Book Title : ");
        labelTitle.setHorizontalAlignment(JLabel.LEFT);
        labelTitle.setBounds(10, 30, 160, 20);

        tfBookTitle = new JTextField();
        tfBookTitle.setBounds(180, 30, 260, 20);

        labelAuthor = new JLabel("Book Author : ");
        labelAuthor.setHorizontalAlignment(JLabel.LEFT);
        labelAuthor.setBounds(10, 60, 160, 20);

        tfBookAuthor = new JTextField();
        tfBookAuthor.setBounds(180, 60, 260, 20);

        labelPublisher = new JLabel("Book Publisher : ");
        labelPublisher.setHorizontalAlignment(JLabel.LEFT);
        labelPublisher.setBounds(10, 90, 160, 20);

        tfBookPublisher = new JTextField();
        tfBookPublisher.setBounds(180, 90, 260, 20);

        labelPublicationDate = new JLabel("Book Publication Date : ");
        labelPublicationDate.setHorizontalAlignment(JLabel.LEFT);
        labelPublicationDate.setBounds(10, 120, 160, 20);

        tfBookPublicationDate = new JTextField();
        tfBookPublicationDate.setBounds(180, 120, 260, 20);

        buttonSave = new JButton("Save");
        buttonSave.setBounds(180, 160, 75, 20);

        labelId = new JLabel("Book ID : ");
        labelId.setHorizontalAlignment(JLabel.LEFT);
        labelId.setBounds(10, 220, 100, 20);

        tfBookId = new JTextField();
        tfBookId.setBounds(180, 220, 260, 20);

        buttonDelete = new JButton("Delete");
        buttonDelete.setBounds(180, 250, 80, 20);

        buttonUpdate = new JButton("Update");
        buttonUpdate.setBounds(270, 250, 80, 20);

        buttonSearch = new JButton("Search");
        buttonSearch.setBounds(360, 250, 80, 20);


        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connect();
                saveBook();
            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connect();
                deleteBook();
            }
        });

        buttonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connect();
                updateBook();
            }
        });

        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connect();
                searchBook();
            }
        });

        frame.add(labelTitle);
        frame.add(labelAuthor);
        frame.add(labelPublisher);
        frame.add(labelPublicationDate);
        frame.add(labelId);

        frame.add(tfBookTitle);
        frame.add(tfBookAuthor);
        frame.add(tfBookPublisher);
        frame.add(tfBookPublicationDate);
        frame.add(tfBookId);

        frame.add(buttonSave);
        frame.add(buttonUpdate);
        frame.add(buttonDelete);
        frame.add(buttonSearch);

        frame.setLayout(null);
        frame.setSize(600, 400);
        frame.setVisible(true);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /*
    public static void main(String[] args) {
        new BookInfo();
    }
    */

    private void Connect(){
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("Connector URL", "user", "password");
            System.out.println("Database connection successful!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveBook(){
        bookTitle = tfBookTitle.getText().trim();
        bookAuthor = tfBookAuthor.getText().trim();
        bookPublisher = tfBookPublisher.getText().trim();
        bookPublicationDate = tfBookPublicationDate.getText().trim();
        //clearTextFields();

        if (!(bookTitle.isEmpty()) && !(bookAuthor.isEmpty()) && !(bookPublisher.isEmpty()) && !(bookPublicationDate.isEmpty())){
            try {
                preparedStatement = connection.prepareStatement("ALTER TABLE bookinfo AUTO_INCREMENT=1");
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement("INSERT INTO bookinfo(bookTitle, bookAuthor, bookPublisher, bookPublicationDate) VALUES(?, ?, ?, ?)");
                preparedStatement.setString(1, bookTitle);
                preparedStatement.setString(2, bookAuthor);
                preparedStatement.setString(3, bookPublisher);
                preparedStatement.setString(4, bookPublicationDate);
                preparedStatement.executeUpdate();
                clearStrings();
                clearTextFields();
            } catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, e.getMessage());
                clearStrings();
                clearTextFields();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please fill all text fields");
        }

        tfBookTitle.requestFocus();
    }

    private void deleteBook(){
        bookId = tfBookId.getText().trim();
        clearTextFields();

        try {
            Statement statement = connection.createStatement();
            String checkBook = "SELECT * FROM bookinfo WHERE bookId='" + bookId + "'";
            resultSet = statement.executeQuery(checkBook);

            if (resultSet.next()){
                //JOptionPane.showMessageDialog(frame, "Book Found");
                bookId = resultSet.getString(1);
                bookTitle = resultSet.getString(2);
                bookAuthor = resultSet.getString(3);
                bookPublisher = resultSet.getString(4);
                bookPublicationDate = resultSet.getString(5);

                int deleteChoice;

                String deleteConfirmMessage = "Do you want to delete the Book?\n\n";
                String deleteDialogMessage =
                        "Book ID : " + bookId +
                        "\nBook Title : " + bookTitle +
                        "\nBook Author : " + bookAuthor +
                        "\nBook Publisher : " + bookPublisher +
                        "\nBook Publication Date : " + bookPublicationDate;

                deleteChoice = JOptionPane.showConfirmDialog(frame, deleteConfirmMessage + deleteDialogMessage);

                if (deleteChoice == 0){
                    try {
                        preparedStatement = connection.prepareStatement("DELETE FROM bookinfo WHERE bookId='" + bookId + "'");
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "The book deleted!");
                        clearStrings();
                        clearTextFields();
                    } catch (Exception e){
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(frame, e.getMessage());
                    }
                } else if (deleteChoice == 1){
                    JOptionPane.showMessageDialog(frame, "Book deletion not confirmed");
                    clearStrings();
                    clearTextFields();
                } else if (deleteChoice == 2){
                    JOptionPane.showMessageDialog(frame, "Book deletion cancelled");
                    clearStrings();
                    clearTextFields();
                } else{
                    JOptionPane.showMessageDialog(frame, "Something went wrong | Error : Failed to delete the book!");
                    clearStrings();
                    clearTextFields();
                }

            } else {
                JOptionPane.showMessageDialog(frame, "Book Not Found! Please Valid Book ID.");
                clearStrings();
                clearTextFields();
            }

            connection.close();
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }

        tfBookTitle.requestFocus();
    }

    private void updateBook(){
        bookId = tfBookId.getText();

        try {
            Statement statement = connection.createStatement();
            String checkBook = "SELECT * FROM bookinfo WHERE bookId='" + bookId + "'";
            resultSet = statement.executeQuery(checkBook);

            if (resultSet.next()){
                //JOptionPane.showMessageDialog(frame, "Book Found");
                bookId = resultSet.getString(1);
                bookTitle = resultSet.getString(2);
                bookAuthor = resultSet.getString(3);
                bookPublisher = resultSet.getString(4);
                bookPublicationDate = resultSet.getString(5);;

                int updateChoice;

                String updateConfirmMessage = "Do you want to update the Book?\n\n";
                String updateDialogMessage =
                        "Book ID : " + bookId +
                        "\nBook Title : " + bookTitle +
                        "\nBook Author : " + bookAuthor +
                        "\nBook Publisher : " + bookPublisher +
                        "\nBook Publication Date : " + bookPublicationDate;

                updateChoice = JOptionPane.showConfirmDialog(frame, updateConfirmMessage + updateDialogMessage);

                if (updateChoice == 0){
                    String updatedBookTitle = JOptionPane.showInputDialog(frame, "Enter updated book title : ");
                    String updatedBookAuthor = JOptionPane.showInputDialog(frame, "Enter updated book author : ");
                    String updatedBookPublisher = JOptionPane.showInputDialog(frame, "Enter updated book publisher : ");
                    String updatedBookPublicationDate = JOptionPane.showInputDialog(frame, "Enter updated book publication date");

                    try {
                        String updateRow = "UPDATE bookinfo SET bookTitle='" + updatedBookTitle + "', bookAuthor='" + updatedBookAuthor +
                                "', bookPublisher='" + updatedBookPublisher + "', bookPublicationDate='" + updatedBookPublicationDate + "' WHERE bookId='" + bookId + "'";

                        preparedStatement = connection.prepareStatement(updateRow);
                        preparedStatement.executeUpdate();

                        clearTextFields();

                        String beforeUpdateInfoMessage =
                                "Beforo Update" +
                                "\nBook ID : " + bookId +
                                "\nBook Title : " + bookTitle +
                                "\nBook Author : " + bookAuthor +
                                "\nBook Publisher : " + bookPublisher +
                                "\nBook Publication Date : " + bookPublicationDate;

                        String afterUpdateInfoMessage =
                                "\n\nAfter Update" +
                                "\nBook ID : " + bookId +
                                "\nBook Title : " + updatedBookTitle +
                                "\nBook Author : " + updatedBookAuthor +
                                "\nBook Publisher : " + updatedBookPublisher +
                                "\nBook Publication Date : " + updatedBookPublicationDate;

                        JOptionPane.showMessageDialog(frame, "The book updated!");
                        JOptionPane.showMessageDialog(frame, beforeUpdateInfoMessage + afterUpdateInfoMessage);
                        clearStrings();
                        clearTextFields();
                    } catch (Exception e){
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(frame, e.getMessage());
                    }
                } else if(updateChoice == 1){
                    JOptionPane.showMessageDialog(frame, "Book update not confirmed");
                    clearStrings();
                    clearTextFields();
                } else if (updateChoice == 2){
                    JOptionPane.showMessageDialog(frame, "Book update cancelled");
                    clearStrings();
                    clearTextFields();
                } else{
                    JOptionPane.showMessageDialog(frame, "Something went wrong | Error : Failed to update the book!");
                    clearStrings();
                    clearTextFields();
                }

            } else {
                JOptionPane.showMessageDialog(frame, "Book Not Found! Please Valid Book ID.");
                clearStrings();
                clearTextFields();
            }

            connection.close();
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }

        tfBookTitle.requestFocus();
    }

    private void searchBook(){
        bookId = tfBookId.getText();

        try {
            Statement statement = connection.createStatement();
            String checkBook = "SELECT * FROM bookinfo WHERE bookId='" + bookId + "'";
            resultSet = statement.executeQuery(checkBook);

            if (resultSet.next()){
                bookId = resultSet.getString(1);
                bookTitle = resultSet.getString(2);
                bookAuthor = resultSet.getString(3);
                bookPublisher = resultSet.getString(4);
                bookPublicationDate = resultSet.getString(5);

                String searchDialogMessage =
                        "Book ID : " + bookId +
                        "\nBook Title : " + bookTitle +
                        "\nBook Author : " + bookAuthor +
                        "\nBook Publisher : " + bookPublisher +
                        "\nBook Publication Date : " + bookPublicationDate;

                JOptionPane.showMessageDialog(frame, searchDialogMessage);
                clearStrings();
                clearTextFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Book not found!");
                clearStrings();
                clearTextFields();
            }

        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }

        tfBookTitle.requestFocus();
    }

    private void clearStrings(){
        bookId = "";
        bookTitle = "";
        bookAuthor = "";
        bookPublisher = "";
        bookPublicationDate = "";
    }

    private void clearTextFields(){
        tfBookTitle.setText("");
        tfBookAuthor.setText("");
        tfBookPublisher.setText("");
        tfBookPublicationDate.setText("");
        tfBookId.setText("");
    }
}