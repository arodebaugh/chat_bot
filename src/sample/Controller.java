package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;

import java.io.*;
import java.util.concurrent.TimeUnit;

import java.net.URL;

public class Controller {
    public Button okayButton;
    public Button clearDataButton;
    public TextField chatText;
    public ListView<String> chatList;
    public Label helloText;
    public Label userSideBar;
    public Label botSideBar;

    private Bot bot = new Bot();

    private BufferedReader input;
    private File savedText;
    private Boolean newUser;
    private int waitingFor = 0;

    private String name;
    private String age;

    private String chat;

    public void initialize() {
        chatText.requestFocus();
        readFile();
    }

    private void readFile() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();

            URL resource = classLoader.getResource("sample/dataStore.txt");

            if (resource !=  null) {
                savedText = new File(resource.getFile());
                if (savedText.exists()) {
                    input = new BufferedReader(new FileReader(savedText));
                    newUser = false;
                    if (input.readLine() == null) {
                        newUser = true;
                        newUser();
                    } else {
                        input.close();
                        input = new BufferedReader(new FileReader(savedText));
                        newUser = false;
                        name = input.readLine();
                        age = input.readLine();

                        bot.transferValues(name, age);

                        helloText.setText("Hello, " + name);
                        userSideBar.setText(name);
                        welcomeBack();
                    }
                }
            } else {
                chatList.getItems().add("Error: No file exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            chatList.getItems().add("Error: " + e);
        }
    }

    public void clearData() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();

            URL resource = classLoader.getResource("sample/dataStore.txt");
            if (resource != null) {
                File savedText = new File(resource.getFile());
                BufferedWriter writer = new BufferedWriter(new FileWriter(savedText));

                writer.write("");
                writer.close();
            } else {
                System.out.println("File does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        chatList.getItems().removeAll();
        chatList.getItems().add("Bot: Data has been cleared.");
        helloText.setText("Hello, guest.");
        userSideBar.setText("Guest");
        newUser();
    }

    private void runChat() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
            chatList.getItems().add("Error: " + e);
        }

        if (waitingFor == 0) {
            chatList.getItems().add("Bot: " + bot.respond(chat));
        } else if (waitingFor == 1) {
            name = chat;
            helloText.setText("Hello, " + name + ".");
            userSideBar.setText(name);
            chatList.getItems().add("Bot: " + bot.newUser(1, name));
            waitingFor = 2;
        } else if (waitingFor == 2) {
            age = chat;
            chatList.getItems().add("Bot: " + bot.newUser(2, age));
            bot.transferValues(name, age);
            waitingFor = 0;
        } else {
            chatList.getItems().add("Bot: Error waiting for is either < 0 or > 2");
        }
    }

    public void buttonClicked() {
        chatList.getItems().add("User: " + chatText.getText());

        chat = chatText.getText();

        chatText.setText("");

        new Thread(() -> {
            runChat();
        }).start();
    }

    public void onEnter() {
        buttonClicked();
    }

    private void newUser() {
        chatList.getItems().add("Bot: " + bot.newUser(0, ""));
        waitingFor = 1;
    }

    private void welcomeBack() {
        chatList.getItems().add("Bot: Welcome back " + name + "!");
    }

    void save() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();

            URL resource = classLoader.getResource("sample/dataStore.txt");
            if (resource != null) {
                File savedText = new File(resource.getFile());
                BufferedWriter writer = new BufferedWriter(new FileWriter(savedText));

                writer.write(name);
                writer.newLine();
                writer.write(age);
                writer.newLine();
                writer.close();
            } else {
                System.out.println("File does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
