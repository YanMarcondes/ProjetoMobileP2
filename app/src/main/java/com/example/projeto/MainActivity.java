package com.example.projeto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.Cliente.Cliente;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btSelecionarFoto, btUploadFoto, btDownloadFoto;
    private ImageView imFotoUpload, imFotoDownload;
    StorageReference mstoregeReference;
    Uri caminhoImagem;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("imagens");
    private List<Cliente> clienteList = new ArrayList<>();
    private ArrayAdapter<Cliente> clienteArrayAdapter;
    private String nomeArquivo = null;
    private EditText edIndice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarFirebase();
        carregaWigets();
        botoes();
    }

    private void botoes() {
        btSelecionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscaFoto();

            }


        });

        btUploadFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravaDatabase();
                uploadFoto();

            }
        });

        btDownloadFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pesquisa("");
                downloadFoto(MainActivity.this);

            }
        });

    }

    private void downloadFoto(Context context) {
        Integer indice = 0;
        indice = Integer.valueOf(edIndice.getText().toString());
        pesquisa("");
        StorageReference storageReference = mstoregeReference;
        if (clienteList.isEmpty() == false) {
            storageReference.child(clienteList.get(indice).getEndereco()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(MainActivity.this).load(uri).into(imFotoDownload);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }

    private void pesquisa(String nome) {
        Query query;
        query = FirebaseDatabase.getInstance().getReference("imagens");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clienteList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.exists()) {
                        Cliente cliente = dataSnapshot1.getValue(Cliente.class);
                        clienteList.add(cliente);
                    }
                }
                clienteArrayAdapter = new ArrayAdapter<Cliente>(MainActivity.this,
                        android.R.layout.simple_list_item_1, clienteList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void uploadFoto() {
        StorageReference ref = mstoregeReference.child(nomeArquivo);
        ref.putFile(caminhoImagem).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "Sucesso!", Toast.LENGTH_SHORT).show();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Falhou!!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void buscaFoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && requestCode == RESULT_OK) ;
        caminhoImagem = data.getData();
        imFotoUpload.setImageURI(caminhoImagem);

    }

    private void carregaWigets() {
        btDownloadFoto = (Button) findViewById(R.id.btFotoDownload);
        btSelecionarFoto = (Button) findViewById(R.id.btBuscaFoto);
        btUploadFoto = (Button) findViewById(R.id.btUploadFoto);
        imFotoDownload = (ImageView) findViewById(R.id.imgFotoDownload);
        imFotoUpload = (ImageView) findViewById(R.id.imgFotoDispositivo);
        edIndice = (EditText) findViewById(R.id.edtIndice);


    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        mstoregeReference = FirebaseStorage.getInstance().getReference("imagens");

    }

    private void gravaDatabase() {
        Date dt = new Date();
        Cliente cli = new Cliente();
        cli.setId("i");
        nomeArquivo = String.valueOf(dt.getDate() + dt.getTime());
        cli.setNome(nomeArquivo);
        cli.setEndereco(cli.getNome());
        DatabaseReference clienteRef = databaseReference.child(cli.getNome());
        clienteRef.setValue(cli);

    }
}
