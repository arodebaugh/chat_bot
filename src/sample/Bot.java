package sample;

import java.util.Random;

public class Bot {
    private boolean newUser = false;
    private String name;
    private String age;

    public void transferValues(String transferName, String transferAge) {
        name = transferName;
        age = transferAge;
    }

    public String newUser(int step, String input) {

        if (step == 1) {
            name = input;
        }

        String[] newUserResponses = {
                "Hello human! What is your name?",
                "Lovely name " + name + ". How old are you?",
                "Great!"};

        newUser = true;
        return newUserResponses[step];
    }

    public String respond(String to) {
        // Remove punctuation to simplify inputs.
        String response = to.replaceAll("\\W", "");
        response = response.toLowerCase();

        System.out.println(response);

        if (response.contains("hi") || response.contains("hello") || response.contains("sup") || response.contains("hey")) {
            String[] responsesToHi = {  "Hello!", "Hi!", "Sup!", "Hello there!", "Thought you would show up! Hello!" };
            int idx = new Random().nextInt(responsesToHi.length);
            return responsesToHi[idx];
        } else if (response.contains("bye")) {
            String[] responsesToBye = {  "Bye!", "See you later!", "Come back soon!", "See ya!", "Great talking to you." };
            int idx = new Random().nextInt(responsesToBye.length);
            return responsesToBye[idx];
        } else if (response.contains("age")) {
            return "You are " + age + " years old!";
        }else if (response.contains("name")) {
            return "You are " + name + "!";
        } else if (response.equals("help")) {
            return "I am quite limited in my capabilities currently. Todo: list of commands/conversations";
        } else {
            return "I'm sorry... Not sure what you mean by that...";
        }
    }
}
