package algonquin.cst2335.li000793;public class ChatMessage {

    private final String message;
    private final String timeSent;
    private final boolean isSentButton;

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }

    public boolean isSend() {
        return isSentButton;
    }
}
