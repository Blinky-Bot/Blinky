package me.tsblock.Blinky.HTTPRequest.APIJSONFormat;

public class Magic {
    private String question;
    private String answer;
    private String type;

    public Magic(String question, String answer, String type) {
        this.question = question;
        this.answer = answer;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getType() {
        return type;
    }
}
