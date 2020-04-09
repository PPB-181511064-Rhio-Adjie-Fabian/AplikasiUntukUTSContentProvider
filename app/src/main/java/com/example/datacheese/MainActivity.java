package com.example.datacheese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String ProviderName = "com.example.android.contentprovidersample.provider";
    private static final Uri Content_Data = Uri.parse("content://" + ProviderName + "/cheeses");
    private static final String Data_Name = "name";
    private static final String Data_Id = "_id";
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView list = findViewById(R.id.recycler_view);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        Cursor c = getCheeseData();
        myAdapter = new MyAdapter();
        myAdapter.setCheeses(c);
        list.setAdapter(myAdapter);

    }

    private Cursor getCheeseData(){
        Uri allCheeses = Uri.parse("content://" +  ProviderName + "/cheeses");
        String[] projection = new String[]
                {Data_Id, Data_Name};
        Cursor c;
        CursorLoader cursorLoader = new CursorLoader(
                this,
                allCheeses,
                projection,
                null,
                null,
                "name" + " ASC");
        c = cursorLoader.loadInBackground();
        return c;
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private Cursor cursor;

        @Override
        @NonNull
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if(cursor.moveToPosition(position)){
                holder.textView.setText(cursor.getString(cursor.getColumnIndex(Data_Name)));
            }
        }

        @Override
        public int getItemCount() {
            return cursor == null ? 0 : cursor.getCount();
        }

        void setCheeses(Cursor cursor) {
            this.cursor = cursor;
            notifyDataSetChanged();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder{

            final TextView textView;
            MyViewHolder(ViewGroup parent){
                super(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1 ,parent, false));
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
