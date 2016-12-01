package br.com.gabrielacolares.localizarmeusclientes;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.control.glide.GlideRequest;
import com.oceanbrasil.libocean.control.glide.ImageDelegate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

import static android.app.Activity.RESULT_OK;
import static br.com.gabrielacolares.localizarmeusclientes.R.id.tvplace;

/**
 * Created by gabrielacolares on 10/11/16.
 */

public class CadastroFragment extends Fragment implements ImageDelegate.BytesListener, GoogleApiClient.OnConnectionFailedListener {
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-3.115855, -60.011705), new LatLng(-3.005463, -59.977544));
    private int MAX_ATTACHMENT_COUNT = 1;
    private static final int REQUEST_PERMISSION = 9;
    private static String[] PERMISSIONS_READ_WRITE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private EditText editNome;
    private EditText editEmail;
    private EditText editDataNascimento;
    private EditText editTelefone;
    private AutoCompleteTextView autoCompleteTextView;
    private View appView;
    private LayoutInflater myInflater;
    private Cliente cliente;
    private ImageView img;
    private CircleImageView img2;
    private File caminhoImagem;
    private byte bytesDaImagem[];
    private String foto;
    private Place place;
    private ArrayList<String> photoPaths = new ArrayList<>();
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(), 0 , this).addApi(Places.GEO_DATA_API).build();
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
        img = (ImageView) view.findViewById(R.id.img);
        img2 = (CircleImageView) view.findViewById(R.id.profile_image);

        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(tvplace);
        PlaceAutocompleteAdapter mAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        autoCompleteTextView.setAdapter(mAdapter);

        ImageView ivLocal = (ImageView)view.findViewById(R.id.ivLocal);
        ivLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoMapa();
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamera();
                //onPickPhoto();
            }
        });

        editDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialogDataNascimento();
                InputMethodManager imm = (InputMethodManager) appView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(appView.getWindowToken(), 0);
            }
        });
    }
    /*
     *METODO PARA SALVAR CLIENTE NO FIREBASE
     */
    public void salvar() {
        final ProgressDialog progressDialog = new ProgressDialog(appView.getContext());
        progressDialog.setMessage("Enviando dados");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://localizarcliente.appspot.com").child("clientesImagem").child(caminhoImagem.getName());

        storageRef.putBytes(bytesDaImagem)
                .addOnSuccessListener((Activity) appView.getContext(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();

                        try{

                            String nome = editNome.getText().toString();
                            String email = editEmail.getText().toString();
                            String telefone = editTelefone.getText().toString();

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); // Make sure user insert date into edittext in this format.

                            Date dateObject;
                            String dob_var=(editDataNascimento.getText().toString());
                            Log.d("data",dob_var);
                            dateObject = formatter.parse(dob_var);

                            cliente = new Cliente();
                            cliente.setNome(nome);
                            cliente.setEmail(email);
                            cliente.setDataNascimento(dateObject);
                            cliente.setTelefone(telefone);
                            cliente.setUrlFoto(taskSnapshot.getDownloadUrl().toString());

                            if(null!=place.getAddress()) {
                                cliente.setEndereco(place.getAddress().toString());
                            }

                            if(null!=place.getLatLng()) {
                                cliente.setLatitude( place.getLatLng().latitude);
                                cliente.setLongitude( place.getLatLng().longitude);
                            }

                        }

                        catch (java.text.ParseException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Log.i("E11111111111", e.toString());
                        }

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("clientes");
                        databaseReference.push().setValue(cliente);

                        limparCampos();

                        Toast.makeText(appView.getContext(), "Salvo com Sucesso!", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener((Activity) appView.getContext(), new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Log.d("Falha", "Falha" + e.getMessage());
            }
        });
    }

    /*
     *METODO PARA ABRIR CAMERA
     */
    private void abrirCamera() {
        if (ActivityCompat.checkSelfPermission(appView.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(appView.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            verificaChamarPermissao();

        } else {
            onPickPhoto();
        }
    }

    /*
     *METODO PARA VERIFICAR PERMISSAO FORNECIDA PELO USUARIO
    */
    private void verificaChamarPermissao() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) appView.getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) appView.getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions((Activity) appView.getContext(), PERMISSIONS_READ_WRITE, REQUEST_PERMISSION);
        } else {
            ActivityCompat.requestPermissions((Activity) appView.getContext(), PERMISSIONS_READ_WRITE, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 11){
            for( int i = 0; i < permissions.length; i++ ){
                if( permissions[i].equalsIgnoreCase( android.Manifest.permission.ACCESS_FINE_LOCATION )
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED ){
                    obterLocal();
                }
            }
        }

        if (requestCode == REQUEST_PERMISSION) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                onPickPhoto();

            } else {
                Log.d("teste", "nao tem permissao");
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS));
                    addThemToView(photoPaths);

                }
                break;
            case 88:
                if (resultCode == RESULT_OK) {
                     place = PlacePicker.getPlace(getActivity(),data);
                    autoCompleteTextView.setText(place.getAddress());
                }
                break;

        }
        //addThemToView(photoPaths);
    }

    @Override
    public void createdImageBytes(byte[] bytes) {
        bytesDaImagem = bytes;
        Bitmap bitmap = Ocean.byteToBitmap(bytes);
        img.setImageBitmap(bitmap);
        img.setBackground(null);
        img2.setImageBitmap(bitmap);
        img2.setBackground(null);
    }

    /*
     *METODO PARA LIMPAR CAMPOS
     */
    public void limparCampos() {
        editNome.setText(null);
        editEmail.setText(null);
        editDataNascimento.setText(null);
        editTelefone.setText(null);
        img.setImageBitmap(null);
    }

    public void showDatePickerDialogDataNascimento() {
        DialogFragment newFragment = new DatePickerFragmentAgendamento();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void addThemToView(ArrayList<String> imagePaths) {
        if(imagePaths!=null)
         foto = imagePaths.get(0);
        caminhoImagem = new File(foto);

        if (foto != null) {
            Ocean.glide(appView.getContext())
                    .load(Uri.fromFile(new File(foto)))
                    .build(GlideRequest.BYTES)
                    .addDelegateImageBytes(this)
                    .resize(400,200)
                    .toBytes(400, 400);
            photoPaths = new ArrayList<String>();
        }

        else {
            Toast.makeText(appView.getContext(), "Nenhuma foto foi selecionada: ", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     *METODO PARA ABRIR GALERIA DO USUARIO
     */
    public void onPickPhoto() {
        int maxCount = MAX_ATTACHMENT_COUNT - photoPaths.size();
        if ((photoPaths.size()) == MAX_ATTACHMENT_COUNT) {
            Toast.makeText(appView.getContext(), "Não é possível selecionar mais que " + MAX_ATTACHMENT_COUNT + " itens", Toast.LENGTH_SHORT).show();

        } else {
            FilePickerBuilder.getInstance().setMaxCount(maxCount)
                    .setSelectedFiles(photoPaths)
                    .setActivityTheme(R.style.AppTheme)
                    .pickPhoto(this);
        }
    }

    public void callAccessLocation() {


        if( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            if( ActivityCompat.shouldShowRequestPermissionRationale( getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) ){
                Toast.makeText(getActivity(),"É preciso a permission ACCESS_FINE_LOCATION para apresentação dos eventos locais.",Toast.LENGTH_LONG).show();
            }
            else{
                ActivityCompat.requestPermissions( getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 11 );
            }
        }
        else{
            Log.d("Debug", "Possui permissão para acessar a localização");
            obterLocal();
        }
    }

    private void obterLocal(){

        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(getActivity()), 88);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    public void botaoMapa() {
        Log.d("Ale","botaoMapa");
        callAccessLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //tratar para erros
    }
}

