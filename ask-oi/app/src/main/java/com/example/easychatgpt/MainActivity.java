package com.example.easychatgpt;
import okhttp3.OkHttpClient;
import java.util.Locale;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.view.KeyEvent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button button3;
    TextView textView;
    FirebaseUser user;
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message>messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=UTF-8");
    OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);
        auth =  FirebaseAuth.getInstance();
        button3 =findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent  = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            textView.setText(user.getEmail());
        }

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent  = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        messageList = new ArrayList<>();
        messageAdapter= new MessageAdapter(messageList);

        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            addToChat(question,Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question);
            welcomeTextView.setVisibility(View.GONE);
        });

        EditText messageEditText = findViewById(R.id.message_edit_text);
        messageEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String message = messageEditText.getText().toString().trim();
                    if (!message.isEmpty()) {
                        addToChat(message, Message.SENT_BY_ME);
                        messageEditText.setText("");
                        callAPI(message);
                        welcomeTextView.setVisibility(View.GONE);
                    }
                    return true;
                }
                return false;
            }
        });


    }

    private void initializeMainActivity() {
        // Initialize MainActivity components
        Locale locale = new Locale("tr");
        Locale.setDefault(locale);


        messageList = new ArrayList<>();



        // After initializing MainActivity, set the 'is_signed_up' flag to true
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_signed_up", true);
        editor.apply();
    }

    private void sendMessage() {
        String question = messageEditText.getText().toString().trim();
        if (!question.isEmpty()) {
            Toast.makeText(this, question, Toast.LENGTH_LONG).show();
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question);
            welcomeTextView.setVisibility(View.GONE);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    void addToChat(String message, String sentBy) {
        // Encode the message with UTF-8
        byte[] encodedMessage = message.getBytes(StandardCharsets.UTF_8);
        String encodedMessageString = new String(encodedMessage, StandardCharsets.UTF_8);

        // Add the encoded message to the chat
        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        messageList.add(new Message(encodedMessageString, sentBy));

        // Notify the adapter that the dataset has changed
        runOnUiThread(() -> {
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }


    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response,Message.SENT_BY_BOT);
    }

    void callAPI(String question){
        //okhttp

        messageList.add(new Message( "Typing",Message.SENT_BY_BOT));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","gpt-3.5-turbo");
            JSONArray messageArr = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("role", "user");
            obj.put("content",question);
            messageArr.put(obj);

            jsonBody.put("messages", messageArr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", )
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to load response: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject;
                    try {
                        if (response.body() != null) {
                            String responseData = response.body().string();
                            jsonObject = new JSONObject(responseData);
                            JSONArray jsonArray = jsonObject.getJSONArray("choices");
                            String result = jsonArray.getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");
                            addResponse(result.trim());
                        } else {
                            throw new IOException("Response body is null");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> addResponse("�zg�n�m, bu soruyu yan�tlayacak kadar yeterli bilgim yok. Ba�ka bir soru sormak ister misiniz?"));
                }
            }

        });
    }
}