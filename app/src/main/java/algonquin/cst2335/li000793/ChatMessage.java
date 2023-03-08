package algonquin.cst2335.li000793;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate=true)

    @ColumnInfo(name="ID")
    public Long id;

    @ColumnInfo(name="message")
    public String message;
    @ColumnInfo(name="TimeSent")
    private final String timeSent;
    @ColumnInfo(name="isSentButton")
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
