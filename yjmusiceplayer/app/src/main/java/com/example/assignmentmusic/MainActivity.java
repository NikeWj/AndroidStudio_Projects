package com.example.assignmentmusic;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements MusicAdapter.OnMusicListener{
    private RecyclerView listView;
    private LinearLayout cur_music;
    private TextView tv_main_title;
    private ArrayList<Music> listMusic;
    private String TAG = "MainActivityLog";
    private MyReceiver myReceiver;
    private MusicAdapter adapter;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myReceiver = new MyReceiver(new Handler());
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(MusicService.MAIN_UPDATE_UI);
        registerReceiver(myReceiver, itFilter);
        requestPermission();
     }
    private void initView() {
        listView = this.findViewById(R.id.listView1);
        listMusic = MusicList.getMusicData(getApplicationContext());
        Log.i(TAG, "listMusic.size()=="+listMusic.size());
        adapter = new MusicAdapter(this, listMusic,this);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);
        editText = findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cur_music = findViewById(R.id.cur_music);
        tv_main_title = findViewById(R.id.tv_main_title);
        if(MusicService.mlastPlayer != null){
            tv_main_title.setText(listMusic.get(MusicService.mPosition).getName());
        }
    }

    @Override
    public void OnMusicClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listMusic",listMusic);
        bundle.putInt("position", position);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(MainActivity.this, DetailsActivity.class);
        startActivity(intent);
    }

    private class MyReceiver extends BroadcastReceiver {
        private final Handler handler;
        // Handler used to execute code on the UI thread
        public MyReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            // Post the UI updating code to our Handler
            handler.post(new Runnable() {
                @Override
                public void run() {
                    initView();
                }
            });
        }
    }
    @Override
    protected void onResume() {
        initView();
        if (MusicService.mlastPlayer != null){
            cur_music.setVisibility(View.VISIBLE);
            tv_main_title = findViewById(R.id.tv_main_title);
            tv_main_title.setText(listMusic.get(MusicService.mPosition).getName());
            cur_music.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    int position = MusicService.mPosition;
                    bundle.putInt("position", position);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.this, DetailsActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            cur_music.setVisibility(View.GONE);
        }
        super.onResume();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++) {

                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED){
                            String s = permissions[i];
                            Toast.makeText(this,s+"Permission is denied",Toast.LENGTH_SHORT).show();
                        }else{
                            initView();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
    private void requestPermission(){

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else {
            initView();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search,menu);
//
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }
}
