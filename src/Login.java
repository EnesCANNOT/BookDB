import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login {

    JFrame frame;
    JLabel labelUsername;
    JLabel labelPassword;
    JTextField tfUserName;
    JTextField tfPassword;
    JButton buttonLogin;
    JButton buttonRegister;
    String username;
    String password;

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public Login(){

        frame = new JFrame("Login Frame");
        username = "";
        password = "";

        labelUsername = new JLabel("User Name : ");
        labelUsername.setHorizontalAlignment(JLabel.LEFT);
        labelUsername.setBounds(10, 30, 100, 20);

        tfUserName = new JTextField();
        tfUserName.setBounds(120, 30, 200, 20);

        labelPassword = new JLabel("Password : ");
        labelPassword.setHorizontalAlignment(JLabel.LEFT);
        labelPassword.setBounds(10, 50, 100, 50);

        tfPassword = new JTextField();
        tfPassword.setBounds(120, 60, 200, 20);

        buttonLogin = new JButton("Login");
        buttonLogin.setBounds(120, 100, 75, 20);

        buttonRegister = new JButton("Register");
        buttonRegister.setBounds(200, 100, 115, 20);

        buttonRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Connect();
                register();
            }
        });

        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Connect();
                signin();
            }
        });


        frame.add(labelUsername);
        frame.add(labelPassword);
        frame.add(tfUserName);
        frame.add(tfPassword);
        frame.add(buttonLogin);
        frame.add(buttonRegister);

        frame.setLayout(null);
        frame.setSize(600, 400);
        frame.setVisible(true);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Login();
    }

    private void Connect(){
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("Connector URL", "user", "password");
            System.out.println("Database connection successful!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void register(){
        username = tfUserName.getText().trim();
        password = tfPassword.getText().trim();

        if (username.isEmpty() && password.isEmpty()){
            tfUserName.setText("");
            tfPassword.setText("");
            JOptionPane.showMessageDialog(frame, "Username and Password can not be empty!");
        } else if (username.isEmpty()){
            tfUserName.setText("");
            tfPassword.setText("");
            JOptionPane.showMessageDialog(frame, "Username can not be empty!");
        } else if (password.isEmpty()){
            tfUserName.setText("");
            tfPassword.setText("");
            JOptionPane.showMessageDialog(frame, "Password can not be empty!");
        } else {
            tfUserName.setText("");
            tfPassword.setText("");
            System.out.println("Username : " + username + "\n" + "Password : " + password);

            try {
                preparedStatement = connection.prepareStatement("ALTER TABLE bookinfo AUTO_INCREMENT=1");
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement("INSERT INTO userInfo(userName, userPassword) VALUES(?, ?)");
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();

                connection.close();
                JOptionPane.showMessageDialog(frame, "Registration successful!");
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        tfUserName.requestFocus();
    }

    private void signin(){
        username = tfUserName.getText().trim();
        password = tfPassword.getText().trim();

        try {
            Statement statement = connection.createStatement();
            String checkUser = "SELECT * FROM userinfo WHERE userName='" + username + "' and userPassword='" + password + "'";
            resultSet = statement.executeQuery(checkUser);

            if (resultSet.next()){
                //JOptionPane.showMessageDialog(frame, "User found");
                frame.dispose();
                BookInfo bookInfo = new BookInfo();

            } else {
                JOptionPane.showMessageDialog(frame, "User not found");
            }

            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}