package algonquin.cst2335.li000793;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.li000793.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.li000793.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    private      ArrayList<String> messages;
    private  RecyclerView.Adapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());

        ChatRoomViewModel cvm = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = cvm.messages;
        setContentView(binding.getRoot());
        binding.sendbutton.setOnClickListener(click ->{
            messages.add(binding.textInput.getText().toString());
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
        });
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.recycleView.setAdapter( myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override

            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(),
                            parent, false

                    );

                    return new MyRowHolder(binding.getRoot());



            }


            @Override

                public void onBindViewHolder(@NonNull MyRowHolder holder, int position){
                String obj = messages.get(position);
                holder.messageText.setText(obj);
                holder.messageText.setText("");


                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String currentDateandTime = sdf.format(new Date());
                holder.timeText.setText(currentDateandTime);


                }


            @Override
            public int getItemCount() {
                return messages.size();

            }

            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
                /*if(position < 3)
                    return 0;
                else
                    return 1;*/
            }
        });
    }



    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }


}