package algonquin.cst2335.li000793;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;



public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatMessage>> messages;

    public ChatRoomViewModel() {
        messages = new MutableLiveData<>();
    }
}


