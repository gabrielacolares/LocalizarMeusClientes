package br.com.gabrielacolares.localizarmeusclientes;


import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v13.app.FragmentCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.control.glide.GlideRequest;
import com.oceanbrasil.libocean.control.glide.ImageDelegate;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Executor;
import static android.app.Activity.RESULT_OK;

/**
 * Created by gabrielacolares on 10/11/16.
 */

public class CadastroFragment extends Fragment implements ImageDelegate.BytesListener  {
    private static final int REQUEST_PERMISSION = 13;
    private static String[] PERMISSIONS_READ_WRITE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private EditText editNome;
    private EditText editEmail;
    private EditText editDataNascimento;
    private EditText editTelefone;
    private EditText editRg;
    private EditText editCpf;
    private EditText editRua;
    private EditText editNumero;
    private EditText editBairro;
    private EditText editCidade;
    private EditText editStatus;
    private View appView;
    private LayoutInflater myInflater;
    private Cliente cliente;
    private ImageView img;
    private File caminhoImagem;
    private byte bytesDaImagem[];
    private String foto;

    public CadastroFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemMenu:
                salvar();
                break;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myInflater = inflater;
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Adicionar Cliente");
        return inflater.inflate(R.layout.layout_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        appView = view;
        editNome = (EditText) view.findViewById(R.id.nome);
        editEmail = (EditText) view.findViewById(R.id.email);
        editDataNascimento = (EditText) view.findViewById(R.id.datanasc);
        editTelefone = (EditText) view.findViewById(R.id.telefone);
        editRua = (EditText) view.findViewById(R.id.rua);
        editNumero = (EditText) view.findViewById(R.id.numero);
        editBairro = (EditText) view.findViewById(R.id.bairro);
        editCidade = (EditText) view.findViewById(R.id.cidade);
        img = (ImageView) view.findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamera();
            }
        });
    }

    public void setaDados() {
        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        Date dataNascimento = new Date(editDataNascimento.getText().toString());
        String telefone = editTelefone.getText().toString();
        String rua = editRua.getText().toString();
        String numero = editNumero.getText().toString();
        String bairro = editBairro.getText().toString();
        String cidade = editBairro.getText().toString();

        this.cliente = new Cliente();
        this.cliente.setNome(nome);
        this.cliente.setEmail(email);
        this.cliente.setDataNascimento(dataNascimento);
        this.cliente.setTelefone(telefone);
        this.cliente.setRua(rua);
        this.cliente.setNumero(numero);
        this.cliente.setBairro(bairro);
        this.cliente.setCidade(cidade);
    }

    public void salvar() {
        final ProgressDialog progressDialog = new ProgressDialog(appView.getContext());
        progressDialog.setMessage("Enviando foto");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://localizarcliente.appspot.com").child("clientesImagem").child(caminhoImagem.getName());

        storageRef.putBytes(bytesDaImagem)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Aula", "onSucess");
                    }
                }).addOnFailureListener((Executor) this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.d("Aula", "onSucess" + e.getMessage());
            }
        });
    }

    private void intentAbrirCamera() {
        String nomeFoto = DateFormat.format("yyyy-MM-dd_hhmmss",new Date()).toString()+"firebase.jpg";
        caminhoImagem = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),nomeFoto);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(caminhoImagem));
        startActivityForResult(i,9);
    }

    private void verificaChamarPermissao() {
        if (FragmentCompat.shouldShowRequestPermissionRationale(CadastroFragment.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) || FragmentCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            FragmentCompat.requestPermissions(CadastroFragment.this, PERMISSIONS_READ_WRITE, REQUEST_PERMISSION);
        } else {
            FragmentCompat.requestPermissions(CadastroFragment.this, PERMISSIONS_READ_WRITE, REQUEST_PERMISSION);
        }
    }

    private void abrirCamera(){
        if (ActivityCompat.checkSelfPermission(appView.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(appView.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            verificaChamarPermissao();
        } else {
            intentAbrirCamera();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION){
            if (PermissionUtil.verifyPermissions(grantResults)) {
                Log.d("Ale","tem permissao");
                intentAbrirCamera();
            } else {
                // nao tem a permissao
                Log.d("Ale","nao tem permissao");
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9 && resultCode == RESULT_OK){
            if (caminhoImagem != null && caminhoImagem.exists()){
                Ocean.glide(appView.getContext())
                        .load(Uri.fromFile(caminhoImagem))
                        .build(GlideRequest.BYTES)
                        .addDelegateImageBytes(this)
                        .toBytes(400, 400);
            }else{
                Log.e("Ale","FILE null");
            }
        }else{
            Log.d("Ale","nao usou a camera");
        }
    }

    @Override
    public void createdImageBytes(byte[] bytes) {
        bytesDaImagem = bytes;
        Bitmap bitmap = Ocean.byteToBitmap(bytes);
        img.setImageBitmap(bitmap);
    }
}
