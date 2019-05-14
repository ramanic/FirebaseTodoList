package com.ramanic.firebasetodolist;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class todo_list extends AppCompatActivity {
    String userName;
    DatabaseReference db;
    ListView mListView;
    FirebaseHelper helper;
    CustomAdapter adapter;
    EditText titleText, descText,dateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        userName = getIntent().getExtras().getString("USER");

        mListView = (ListView) findViewById(R.id.myListView);

        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db, this, mListView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.smoothScrollToPosition(4);
                displayInputDialog("0_0","","",0);
            }
        });
    }

    public class FirebaseHelper {
        DatabaseReference db;

        ArrayList<toDoListClass> todoListArray = new ArrayList<>();
        ListView mListView;
        Context c;

        public FirebaseHelper(DatabaseReference db, Context context, ListView mListView) {
            this.db = db;
            this.c = context;
            this.mListView = mListView;
            this.retrieve();

        }



        public ArrayList<toDoListClass> retrieve() {
            db.child("users").child(userName).child("todo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    todoListArray.clear();
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            toDoListClass todoList = ds.getValue(toDoListClass.class);
                            todoListArray.add(todoList);
                        }
                        adapter = new CustomAdapter(c, todoListArray);
                        mListView.setAdapter(adapter);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                mListView.smoothScrollToPosition(todoListArray.size());
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("mTAG", databaseError.getMessage());
                    Toast.makeText(c, "ERROR " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            return todoListArray;
        }
    }

    class CustomAdapter extends BaseAdapter {
        Context c;
        ArrayList<toDoListClass> todoList2;
        public CustomAdapter(Context c, ArrayList<toDoListClass> todoListArray2) {
            this.c = c;
            this.todoList2 = todoListArray2;
        }
        @Override
        public int getCount() {
            return todoList2.size();
        }
        @Override
        public Object getItem(int position) {
            return todoList2.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.list, parent, false);
            }
            TextView TitleTextView = convertView.findViewById(R.id.listTitle);
            TextView DateTextView = convertView.findViewById(R.id.list_date);
            Button updateBtn = convertView.findViewById(R.id.updateList);
            Button dltBtn = convertView.findViewById(R.id.dltList);
            TextView DescTextView = convertView.findViewById(R.id.listDescription);
            final CheckBox completedTask = convertView.findViewById(R.id.listComplete);

            final toDoListClass s = (toDoListClass) this.getItem(position);

            DateTextView.setText(s.getDate());
            
            TitleTextView.setText(s.getTitle());
            DescTextView.setText(s.getDescription());
            completedTask.setChecked(s.getCompleted());

            dltBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.child("users").child(userName).child("todo").child(s.getTitle()).removeValue();
                    Log.d("RAMANIC",""+getCount());

                    notifyDataSetChanged();
                }
            });

            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a=0;
                    if(s.getCompleted())a=1;

                    displayInputDialog(s.getTitle(),s.getDescription(),s.getDate(),a);

                    notifyDataSetChanged();
                }
            });
            completedTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  if(completedTask.isChecked()){
                      db.child("users").child(userName).child("todo").child(s.getTitle()).child("completed").setValue(1);
                      completedTask.setChecked(true);

                  }else{
                      db.child("users").child(userName).child("todo").child(s.getTitle()).child("completed").setValue(0);
                      completedTask.setChecked(false);

                    ;

                  }
                }
            });








            return convertView;
        }
    }
    private void displayInputDialog(String title, String description,String date, final int completes) {



        final Dialog d = new Dialog(this);
        d.setTitle("Todo List");
        d.setContentView(R.layout.dialog);

        dateText = d.findViewById(R.id.inputDate);
        titleText = d.findViewById(R.id.inputTitle);
        descText = d.findViewById(R.id.inputDescription);

        Button saveBtn = d.findViewById(R.id.saveBtnStd);

        if(title.contains("0_0")){
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String dates = dateText.getText().toString();
                    String titles = titleText.getText().toString();
                    String descriptions = descText.getText().toString();
                    try {


                        db.child("users").child(userName).child("todo").child(titles).child("date").setValue(dates);
                            db.child("users").child(userName).child("todo").child(titles).child("title").setValue(titles);
                            db.child("users").child(userName).child("todo").child(titles).child("description").setValue(descriptions);
                            db.child("users").child(userName).child("todo").child(titles).child("completed").setValue(completes);

                    } catch (Exception ignored) {

                    }


                }

            });


        }else {

            titleText.setText(title);
            descText.setText(description);
            final String oldTitle = titleText.getText().toString();


            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Ramanix", "Save" + oldTitle);

                    String dates = dateText.getText().toString();
                    String titles = titleText.getText().toString();
                    String descriptions = descText.getText().toString();
                    try {
                        if (titles.equals(oldTitle)) {
                            Log.d("Ramanix", "Equa" + oldTitle);

                            db.child("users").child(userName).child("todo").child(titles).child("description").setValue(descriptions);
                            db.child("users").child(userName).child("todo").child(titles).child("date").setValue(dates);

                        } else {
                            Log.d("Ramanix", oldTitle);
                            db.child("users").child(userName).child("todo").child(oldTitle).removeValue();
                            db.child("users").child(userName).child("todo").child(titles).child("date").setValue(dates);
                            db.child("users").child(userName).child("todo").child(titles).child("title").setValue(titles);
                            db.child("users").child(userName).child("todo").child(titles).child("description").setValue(descriptions);
                            db.child("users").child(userName).child("todo").child(titles).child("completed").setValue(completes);
                        }
                    } catch (Exception ignored) {

                    }


                }

            });
        }
        d.show();
    }
}
