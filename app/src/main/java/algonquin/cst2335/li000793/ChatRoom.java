package algonquin.cst2335.li000793;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());

        binding.recycleView.setAdapt(new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());

                return new MyRowHolder( binding.getRoot());
                ArrayList<String> messages = new ArrayList<>();
            }

            @Override

                public void onBindViewHolder(@NonNull MyRowHolder holder, int position){
                    holder.messageText.setText("");
                    holder.timeText.setText("");
                String obj = messages.get(position);
                holder.messageText.setText(obj);

                }


            @Override
            public int getItemCount() {
                return messages.size();

            }
        });


        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;
            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.messageText);
                timeText = itemView.findViewById(R.id.timeText);
            }
        }
    }
}