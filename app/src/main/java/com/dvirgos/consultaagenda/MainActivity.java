package com.dvirgos.consultaagenda;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dvirgos.consultaagenda.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private final int CONTACTS_PERMISION = 1;
    Button button;
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("zzzz", "onCreate");
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void explain() {
        showRationaleDialog(
                getString(R.string.title),
                getString(R.string.message),
                Manifest.permission.READ_CONTACTS,
                CONTACTS_PERMISION);
    }

    public void init () {

        button = findViewById(R.id.button);
        editText = findViewById(R.id.etphone);
        textView = findViewById(R.id.textView);

        SharedPreferences preferenciasActividad = getPreferences(Context.MODE_PRIVATE);
        String lastSearch = preferenciasActividad.getString(getString(R.string.last_search), "");
        if(!lastSearch.isEmpty()) {
            editText.setText(lastSearch);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIfPermision();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void resquestPermission() {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISION);
    }

    private void search() {
        textView.setText("a pelo ya si");
        //ContentProvider Proveedor de contenidos
        //ContentResolver Consultor de contenidos
        //url: https://ieszaidinvergeles.org/carpeta/carpeta2/pagina.html?data=1
        //uri: protocolo://direccion/ruta/recurso
        /*Cursor cursor = getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,  //The content URI of the words table
                new String[] {"projection"},       //The columns to return for each row
                "capo1 = ? and campo2 = ? or campo3 = ?",        //Selection criterio
                new String[] {"pepe", "4", "23"},   //Selection criterio
                "campo5, campo3, campo4");           //The sort order for the returned rows
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String proyeccion[] = new String[] {ContactsContract.Contacts.DISPLAY_NAME};
        String seleccion = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ? and " +
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";
        String argumentos[] = new String[]{"1","1"};
        argumentos = null;
        String orden = ContactsContract.Contacts.DISPLAY_NAME + " collate localized asc";
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);

        String[] columnas = cursor.getColumnNames();
        for (String s:columnas) {
            Log.v("zzzz", s);
        }

        String displayName;
        int columna = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        while (cursor.moveToNext()) {
            displayName = cursor.getString(columna);
            Log.v("zzzz", displayName);
        }*/
        String phone = editText.getText().toString();

        SharedPreferences preferenciasActividad = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferenciasActividad.edit();
        editor.putString("last_search", phone);
        editor.commit();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences p1 = getSharedPreferences("preferenciascompartidas", Context.MODE_PRIVATE);
        SharedPreferences p2 = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences p3 = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences p4 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor ed1 = p1.edit();
        SharedPreferences.Editor ed2 = p2.edit();
        SharedPreferences.Editor ed3 = p3.edit();
        SharedPreferences.Editor ed4 = p4.edit();

        ed1.putString("ved1", "v1");
        ed2.putString("ved2", "v2");
        ed3.putString("ved3", "v3");
        ed4.putString("ved4", "v4");

        ed1.commit();ed2.commit();ed3.commit();ed4.commit();

        //----------------------------------------------------------------------------------------

        Uri uri2 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String proyeccion2[] = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        String seleccion2 = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " like ?";
        String argumentos2[] = new String[]{editText.getText().toString()};
        String orden2 = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Cursor cursor2 = getContentResolver().query(uri2, proyeccion2, seleccion2, argumentos2, orden2);
        String[] columna2 = cursor2.getColumnNames(); //cursor2.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

        int colunmnaNombre = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int columnaNumero = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String nombre, numero;
        while (cursor2.moveToNext()) {
            nombre = cursor2.getString(colunmnaNombre);
            numero = cursor2.getString(columnaNumero);
            for (String s: columna2) {
                int pos = cursor2.getColumnIndex(s);
                String valor = cursor2.getString(pos);
                Log.v("zzzz", pos+ " " + s + " " + valor);
            }
        }
    }

    private void searchIfPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_CONTACTS) ==
                    PackageManager.PERMISSION_GRANTED) {
                //ya tengo el permiso
                search();
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                explain();
            } else {
                resquestPermission();
            }
        } else {
            //ya tengo el permiso, la version es anterior a la seis
            search();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            viewSetting();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewSetting() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void showRationaleDialog(String title, String message, String permission, int requestCode){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //nada
                    }
                })
                .setPositiveButton(R.string.okey, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //pedir los permisos
                        resquestPermission();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("zzzz", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("zzzz", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("zzzz", "onPause");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("zzzz", "OnRequestPermissionsResult");
        switch (requestCode) {
            case CONTACTS_PERMISION:
                    if (grantResults.length > 0 &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //permiso
                        search();
                    }else{
                        //sin permiso

                    }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("zzzz", "onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("zzzz", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("zzzz", "onResume");
    }

}
