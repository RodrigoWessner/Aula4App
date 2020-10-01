package com.example.aula4app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.UriPermission;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

public class MainActivity extends Activity {
    private final int SELECIONAR_CONTATO = 0;
    private final int CAPTURA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt1 = (Button) findViewById(R.id.contatos);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://com.android.contacts/contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                startActivityForResult(intent, SELECIONAR_CONTATO);
            }
        });

        Button bt2 = (Button) findViewById(R.id.web);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.unisc.br");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        Button bt3 = (Button) findViewById(R.id.call);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel: 996611152");
                Intent it = new Intent(Intent.ACTION_CALL, uri);
                startActivity(it);
            }
        });

        Button bt4 = (Button) findViewById(R.id.maps1);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("geo:0,0?q=Sete+de+Setembro,Curitiba");
                Intent it = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        Button bt5 = (Button) findViewById(R.id.maps2);
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String localizacao = "geo:-25.443195,-49.280977";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(localizacao)));
            }
        });
        Button bt6 = (Button) findViewById(R.id.maps3);
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String partida = "-25.443195, -49.280977";
                String destino = "-25.442207, -49.278403";
                String url = "http://maps.google.com/maps?f=d&saddr=" + partida + "&daddr=" + destino + "&h1=pt";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        Button bt7 = (Button) findViewById(R.id.piciti);
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(it, CAPTURA);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECIONAR_CONTATO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();

                Cursor c = getContentResolver().query(uri, null, null, null, null);
                c.moveToNext();
                int nameCol = c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
                int idCol = c.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
                String nome = c.getString(nameCol);
                String id = c.getString(idCol);

                c.close();

                Cursor phones = getContentResolver().query
                        (ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =  " + id, null, null);
                phones.moveToNext();
                String phoneNumber = phones.getString
                        (phones.getColumnIndexOrThrow
                                (ContactsContract.CommonDataKinds.Phone.NUMBER));

                phones.close();
                TextView tvNome = (TextView) findViewById(R.id.nometxt2);
                tvNome.setText(nome);
                TextView tvPhone = (TextView) findViewById(R.id.telefonetxt2);
                tvPhone.setText(phoneNumber);

            } else {
                Toast.makeText(this, "Nenhum contato selecionado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Bundle extras = data.getExtras();
            Bitmap img = (Bitmap) extras.get("data");
            ImageView imagem = (ImageView) findViewById(R.id.imageView);
            imagem.setImageBitmap(img);
        }

    }
}