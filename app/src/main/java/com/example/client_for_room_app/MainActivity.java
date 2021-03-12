package com.example.client_for_room_app;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

/*
все работает
не добавил валидацию полей
не добавил EditText для ввода реквизитов
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 123;
    private Spinner spinnerTables;
    private Spinner spinnerActions;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerTables = findViewById(R.id.spinner_tab);
        spinnerActions = findViewById(R.id.spinner_act);
        editText = findViewById(R.id.et_main);
        textView = findViewById(R.id.tv_main);

        getSupportLoaderManager().initLoader(LOADER_ID,null,this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, Uri.parse("content://com.elegion.roomdatabase.musicprovider/album")
                ,null,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data!=null&&data.moveToFirst()){
            StringBuilder builder = new StringBuilder();
            do {
                builder.append(data.getString(data.getColumnIndex("name"))).append('\n');
            }while (data.moveToNext());
            Toast.makeText(this,builder.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public void onClick(View view) {
        String selectedTable  = spinnerTables.getSelectedItem().toString();
        String selectedAction = spinnerActions.getSelectedItem().toString();
        String stringId = editText.getText().toString();

        StringBuilder uriWithId = new StringBuilder();
        uriWithId.append("content://com.elegion.roomdatabase.musicprovider/album/").append(stringId);

        ContentValues cv = new ContentValues();

        switch (view.getId()) {
            case R.id.btn_click:
                switch (selectedAction) {
                    case "update":
                        cv.put("id", stringId);
                        cv.put("name", "album super stars");
                        cv.put("release", "yesterday");
                        getContentResolver().update(Uri.parse(uriWithId.toString()), cv, null, null);
                        break;
                    case "insert":
                        cv.put("id", stringId);
                        cv.put("name", "album super stars_1");
                        cv.put("release", "tomorrow");
                        getContentResolver().insert(Uri.parse("content://com.elegion.roomdatabase.musicprovider/album"), cv);
                        break;
                    case "delete":
                        getContentResolver().delete(Uri.parse(uriWithId.toString()) , null,null);
                        break;
                    case "query":
                        if (stringId.isEmpty()){
                            showAllAlbums();
                        }else {
                            showAlbumById(uriWithId);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case R.id.btn_pull:
                showAllAlbums();
        }
    }

    public void showAllAlbums(){
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.elegion.roomdatabase.musicprovider/album")
                ,null,null,null,null);
        if (cursor!=null&&cursor.moveToFirst()){
            StringBuilder builder = new StringBuilder();
            do {
                builder.append(cursor.getString(cursor.getColumnIndex("name"))).append('\n');
            }while (cursor.moveToNext());
            textView.setText(builder.toString());
        }
    }

    public void showAlbumById(StringBuilder stringBuilder){
        Cursor cursor = getContentResolver().query(Uri.parse(stringBuilder.toString()),null,null,null,null);
        if (cursor!=null&&cursor.moveToFirst()){
            StringBuilder builder = new StringBuilder();
            do {
                builder.append(cursor.getString(cursor.getColumnIndex("name"))).append('\n');
            }while (cursor.moveToNext());
            textView.setText(builder.toString());
        }
    }
}