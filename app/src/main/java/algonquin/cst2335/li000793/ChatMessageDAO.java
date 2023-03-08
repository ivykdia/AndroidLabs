package algonquin.cst2335.li000793;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ChatMessageDAO {

    @Insert
    long insertMessage(ChatMessage m);

    @Query("Select * from ChatMessage")
    List<ChatMessage> getALLMessages();

    @Delete
    Void deleteMessage(ChatMessage m);
}
