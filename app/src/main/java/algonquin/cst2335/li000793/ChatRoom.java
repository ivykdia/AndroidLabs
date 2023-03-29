package algonquin.cst2335.li000793;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;
    private ChatMessageDAO mDAO; //uses the ChatMessageDAO class for querying data
    private int myRowHolderPosition;

    private TextView myRowHolderMessageText;


    //to load a Menu layout file.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_1:
                //String messageText = chatModel.selectedMessage.getValue().getMessage();

                // find the selected chat message and post that value to the selectedMessage variable of type MutableLiveData<ChatMessage> just created
                ChatMessage selected = messages.get(myRowHolderPosition);
                //chatModel.selectedMessage.postValue(selected);
                //the alert dialog to ask if you want to delete
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                //To set the message on the alert window
                builder.setMessage("Do you want to delete the message:" + selected.getMessage())
                        //To set the title of the alert dialog
                        .setTitle("Warning:")
                        .setPositiveButton("OK", (dialog, cl) -> {
                            ChatMessage m = messages.get(myRowHolderPosition);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                mDAO.deleteMessage(m);//delete the clicked message from database

                                runOnUiThread(() -> {
                                    messages.remove(myRowHolderPosition); //delete from the arraylist
                                    myAdapter.notifyItemRemoved(myRowHolderPosition);//update the RecycleView
                                });
                                Snackbar.make(myRowHolderMessageText, "You deleted message #" + myRowHolderPosition, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", clk -> {
                                            Executors.newSingleThreadExecutor().execute(() -> {
                                                mDAO.insertMessage(m);//delete the clicked message from database


                                                runOnUiThread(() -> {
                                                    messages.add(myRowHolderPosition, m);
                                                    myAdapter.notifyItemInserted(myRowHolderPosition);
                                                });
                                            });
                                        })
                                        .show();
                            });
                        })
                        // Clicking on the No shouldn't delete anything so just leave that lambda function empty.
                        .setNegativeButton("CANCEL", (dialog, cl) -> {
                        })
                        .create().show();
                break;
            case R.id.item_2:
                Toast.makeText(this, "Version 1.0, created by Lee.", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    // to maintain variables for what you want to set on each row in your list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "MyMessageDatabase").build();
        mDAO = db.cmDAO();

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue(); //survives rotation changes
        // to register as a listener to the MutableLiveData object
        chatModel.selectedMessage.observe(this, (newMessageValue) -> {

            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);  //newValue is the newly set ChatMessage
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLocation, chatFragment).addToBackStack("").commit();

        });

        //first load old messages
        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll(mDAO.getALLMessages()); //Once you get the data from database

                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter)); //You can then load the RecyclerView
            });
        }

        binding.sendbutton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, true);
            messages.add(chatMessage);
            //no more crashes
            Executor thread = Executors.newSingleThreadExecutor();
            //insert into database
            thread.execute(() -> {
                long id = mDAO.insertMessage(chatMessage);
                chatMessage.id = id; //database is saying what the id is
                // this is on main thread
                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
            });


            // tells the Adapter which row has to be redrawn
            myAdapter.notifyItemInserted(messages.size() - 1);
            // clear the previous text:
            binding.textInput.setText("");
        });

        binding.receivebutton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, false);
            messages.add(chatMessage);

            //no more crashes
            Executor thread = Executors.newSingleThreadExecutor();
            //insert into database
            thread.execute(() -> {
                long id = mDAO.insertMessage(chatMessage);
                chatMessage.id = id; //database is saying what the id is
            });


            // tells the Adapter which row has to be redrawn
            myAdapter.notifyItemInserted(messages.size() - 1);
            // clear the previous text:
            binding.textInput.setText("");
        });

        // initialize the myAdapter variable
        // only call this after loading all messages
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            // This function creates a ViewHolder object which represents a single row in the list
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            // This initializes a ViewHolder to go at the row specified by the position parameter
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                String obj = messages.get(position).getMessage();
                String time = messages.get(position).getTimeSent();
                holder.messageText.setText(obj);
                holder.timeText.setText(time);
            }

            @Override
            // This function just returns an int specifying how many items to draw
            public int getItemCount() {
                // since we want to show whatever is in our ArrayList, the number of rows will be just the size of the list
                return messages.size();
            }

            public int getItemViewType(int position) {
                if (messages.get(position).isSentButton()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyRowHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message);
            myRowHolderMessageText = messageText;
            timeText = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(click -> {

                // to tell you which row (position) this row is currently in the adapter object
                int position = getAbsoluteAdapterPosition();
                myRowHolderPosition = position;
                // find the selected chat message and post that value to the selectedMessage variable of type MutableLiveData<ChatMessage> just created
                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);
                /*
                //the alert dialog to ask if you want to delete
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                //To set the message on the alert window
                builder.setMessage("Do you want to delete the message:" + messageText.getText())
                        //To set the title of the alert dialog
                        .setTitle("Warning:")
                        // Clicking on the No shouldn't delete anything so just leave that lambda function empty.
                        .setPositiveButton("OK", (dialog, cl) -> {
                            Executors.newSingleThreadExecutor().execute(() -> {
                                mDAO.deleteMessage(selected);//delete the clicked message from database
                                messages.remove(position); //delete from the arraylist
                                runOnUiThread(() -> {
                                    myAdapter.notifyItemRemoved(position);//update the RecycleView
                                });
                                Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", clk -> {
                                            Executors.newSingleThreadExecutor().execute(() -> {
                                                mDAO.insertMessage(selected);//delete the clicked message from database
                                                messages.add(position, selected);
                                                runOnUiThread(() -> {
                                                    myAdapter.notifyItemInserted(position);
                                                });
                                            });
                                        })
                                        .show();
                            });
                        })
                        .setNegativeButton("CANCEL", (dialog, cl) -> {
                        })
                        .create().show();*/
            });
        }
    }
}
