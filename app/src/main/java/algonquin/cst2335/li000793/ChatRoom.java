package algonquin.cst2335.li000793;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.li000793.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.li000793.databinding.ReceiveMessageBinding;

import algonquin.cst2335.li000793.databinding.SentMessageBinding;



public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    private ChatRoomViewModel chatModel;
    private ArrayList<ChatMessage> messages;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    ChatMessageDAO mDAO ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get database:
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "MyMessagesDatabase").build();
         mDAO = db.cmDAO();



        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the ViewModel instance
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        // Retrieve the ArrayList of messages from the ViewModel
        messages = chatModel.messages.getValue();

        // If the ArrayList has never been set before, initialize it with a new instance
        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<>());
        }

        binding.sendbutton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            messages.add(new ChatMessage(binding.textInput.getText().toString(), currentDateandTime,true));


            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                ChatMessage newMessage = messages.get(messages.size() - 1);
                long id = mDAO.insertMessage(newMessage);
                newMessage.id = id;
            });




            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
        });

        binding.receivebutton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            messages.add(new ChatMessage(binding.textInput.getText().toString(),currentDateandTime, false));
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(),
                        parent, false);
                    return new MyRowHolder(binding.getRoot(), true);
                } else {

                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(),
                            parent, false);
                    return new MyRowHolder(binding.getRoot(), false);
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
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
                return messages.get(position).isSend() ? 0 : 1;
            }
        });
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView, boolean isSend) {
            super(itemView);


            itemView.setOnClickListener(click ->{

                int position = getAbsoluteAdapterPosition();
                MyRowHolder newRow = myAdapter.onCreateViewHolder(null, myAdapter.getItemViewType(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message:" + messageText.getText());
                builder.setTitle("Question:")
                        .setNegativeButton("No",(dialog, cl)->{})
                        .setPositiveButton("Yes", (dialog, cl)->{
                           ChatMessage removedMessage = messages.get(position);
                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);

                            Snackbar.make(messageText, "you deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {
                                        messages.add(position, removedMessage);
                                        myAdapter.notifyItemRemoved(position);


                                    })


                                    .show();
                        })
                        .create().show();

            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}
