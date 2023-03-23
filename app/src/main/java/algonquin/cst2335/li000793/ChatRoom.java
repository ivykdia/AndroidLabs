package algonquin.cst2335.li000793;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.item_1: //put your ChatMessage deletion code here. If you select this item, you should show the alert dialog
                //asking if the user wants to delete this message.
                break;
        }
        return true;
    }*/
   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch(item.getItemId()) {
           case R.id.item_1:
               // Get the index of the selected chat message


               int selectedIndex = chatModel.selectedMessage.getValue();

               // If no chat message is selected, show a Snackbar message and return
               if(selectedIndex == null) {
                   Snackbar.make(binding.getRoot(), "No chat message selected", Snackbar.LENGTH_SHORT).show();
                   return true;
               }

               // Show an alert dialog asking the user if they want to delete the selected chat message
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               builder.setMessage("Do you want to delete this message?");
               builder.setPositiveButton("Delete", (dialog, which) -> {
                   // Delete the selected chat message from the database and the ArrayList
                   ChatMessage deletedMessage = messages.remove(selectedIndex);
                   Executor thread = Executors.newSingleThreadExecutor();
                   thread.execute(() -> {
                       mDAO.deleteMessage(deletedMessage);
                   });

                   // Notify the adapter that the selected chat message has been removed
                   myAdapter.notifyItemRemoved(selectedIndex);

                   // Reset the selected chat message index to null
                   chatModel.selectedIndex.postValue(null);

                   // Show a Snackbar message confirming the deletion
                   Snackbar.make(binding.getRoot(), "Message deleted", Snackbar.LENGTH_SHORT).show();
               });
               builder.setNegativeButton("Cancel", null);
               builder.create().show();
               return true;
       }
       return super.onOptionsItemSelected(item);
   }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.my_menu,menu);
        return true;
    }

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
        if(messages == null)  {
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll(mDAO.getALLMessages()); //Once you get the data from database
                //run in main thread
                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }
            chatModel.selectedMessage.observe(this,(newMessageValue) ->{
                MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLocation, chatFragment)
                        .commit();
            });
        setSupportActionBar(binding.myToolbar);


        binding.sendbutton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            ChatMessage msg = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime,true);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {

                long id = mDAO.insertMessage(msg);
                msg.id = id;
                messages.add(msg);
                runOnUiThread(()->{
                    myAdapter.notifyItemInserted(messages.size() - 1);
                });
            });

            binding.textInput.setText("");
        });

        binding.receivebutton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            ChatMessage msg = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime,false);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {

                long id = mDAO.insertMessage(msg);
                msg.id = id;
                messages.add(msg);
                runOnUiThread(()->{
                    myAdapter.notifyItemInserted(messages.size() - 1);
                });
            });

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

                      ChatMessage selected = messages.get(position);

                chatModel.selectedMessage.postValue(selected);

                       /* AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                        builder.setMessage("Do you want to delete the message:" + messageText.getText());
                        builder.setTitle("Question:")
                                .setNegativeButton("No",(dialog, cl)->{})
                                .setPositiveButton("Yes", (dialog, cl)->{
                                    ChatMessage removedMessage = messages.get(position);
                                    Executor thread_1 = Executors.newSingleThreadExecutor();
                                    thread_1.execute(()->{
                                        mDAO.deleteMessage(removedMessage);
                                        messages.remove(position);
                                        //myAdapter.notifyItemRemoved(position);
                                        runOnUiThread(()-> {
                                            myAdapter.notifyItemRemoved(position);

                                            Snackbar.make(messageText, "you deleted message #" + position, Snackbar.LENGTH_LONG)
                                                    .setAction("Undo", clk -> {
                                                        Executor tread_2 = Executors.newSingleThreadExecutor();
                                                        tread_2.execute(() -> {
                                                            mDAO.insertMessage(removedMessage);
                                                            messages.add(position, removedMessage);
                                                            runOnUiThread(() -> {
                                                                myAdapter.notifyItemInserted(position);
                                                            });
                                                        });
                                                    }).show();

                                        });
                                    });

                                });
                        builder.setNegativeButton("Cancel",(dialog, which)->{

                        });
                        builder.create().show(); */


                    });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }


    }

}
