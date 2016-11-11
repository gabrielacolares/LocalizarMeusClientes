package br.com.gabrielacolares.localizarmeusclientes;


import android.*;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oceanbrasil.libocean.control.glide.ImageDelegate;

import java.io.File;

/**
 * Created by gabrielacolares on 10/11/16.
 */

public class CadastroFragment extends Fragment{
    private static final int REQUEST_PERMISSION = 13;
    private static String[] PERMISSIONS_READ_WRITE = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
    ImageView img;
    private File caminhoImagem;
    private byte bytesDaImagem[];
    private String foto;


    private EditText editNome;
    private EditText editEmail;
    private EditText editTelefone;
    private EditText editRua;
    private EditText editNumero;
    private EditText editBairro;
    private EditText editCidade;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cadastrar Cliente");
        return inflater.inflate(R.layout.layout_adicionar_cliente,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img = (ImageView) view.findViewById(R.id.imgCliente);
        editNome = (EditText) view.findViewById(R.id.nome);
        editEmail = (EditText) view.findViewById(R.id.email);
        editTelefone = (EditText) view.findViewById(R.id.telefone);
        editRua= (EditText) view.findViewById(R.id.rua);
        editNumero = (EditText) view.findViewById(R.id.numero);
        editBairro = (EditText) view.findViewById(R.id.bairro);
        editCidade = (EditText) view.findViewById(R.id.cidade);


    }

    public void salvarCliente (View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final ProgressDialog progressDialog =  new ProgressDialog(getContext());
        //progressDialog.setMessage("Enviando foto");
        progressDialog.setCancelable(false);
        progressDialog.show();

                progressDialog.dismiss();
                String nome  = editNome.getText().toString();
                String email = editEmail.getText().toString();
                String telefone = editTelefone.getText().toString();
                String numero  = editNumero.getText().toString();
                String rua = editRua.getText().toString();
                String bairro = editBairro.getText().toString();
                String cidade = editCidade.getText().toString();


                Cliente cliente =  new Cliente();
                cliente.setNome(nome);
                cliente.setEmail(email);
                cliente.setNumero(numero);
                cliente.setRua(rua);
                cliente.setBairro(bairro);
                cliente.setCidade(cidade);


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("clientes");
                databaseReference.push().setValue(cliente);
                Log.d("Aula", "onSucess");
            }


}
