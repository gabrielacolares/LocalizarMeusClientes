package br.com.gabrielacolares.localizarmeusclientes;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.control.glide.GlideRequest;
import com.oceanbrasil.libocean.control.glide.ImageDelegate;

import java.io.File;
import java.util.Date;

/**
 * Created by gabrielacolares on 10/11/16.
 */
public class CadastroCliente  extends Activity implements ImageDelegate.BytesListener {
    private static final int REQUEST_PERMISSION = 13;
    private static String[] PERMISSIONS_READ_WRITE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    ImageView img;
    private File caminhoImagem;
    private byte bytesDaImagem[];
    private String foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_adicionar_cliente);
        img = (ImageView) findViewById(R.id.imgCliente);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamera();
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // exibir o motivo de esta precisando da permissao
            ActivityCompat.requestPermissions(CadastroCliente.this, PERMISSIONS_READ_WRITE, REQUEST_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(CadastroCliente.this, PERMISSIONS_READ_WRITE, REQUEST_PERMISSION);
        }
    }
    /**
     * Abrir a camera verficando se existe Permissao
     */
    private void abrirCamera(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            verificaChamarPermissao();
        } else {
            // tenho permissao, chama a intent de camera
            intentAbrirCamera();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION){
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // tem
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9 && resultCode == RESULT_OK){
            if (caminhoImagem != null && caminhoImagem.exists()){
                Ocean.glide(this)
                        .load(Uri.fromFile(caminhoImagem))
                        .build(GlideRequest.BYTES)
                        .addDelegateImageBytes(this)
                        .toBytes(300, 300);
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}