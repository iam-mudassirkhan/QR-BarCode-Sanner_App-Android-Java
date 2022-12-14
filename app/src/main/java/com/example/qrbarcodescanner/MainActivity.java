package com.example.qrbarcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener{

    public Button scanBtn;
    public ImageView webSearch, copyIcon, shareIcon;
    public static TextView scanTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = findViewById(R.id.scanBtn);
        scanTxt = findViewById(R.id.scanTxt);
        webSearch = findViewById(R.id.webSearch);
        copyIcon = findViewById(R.id.copyIcon);
        shareIcon = findViewById(R.id.shareIcon);

        scanBtn.setOnClickListener(this);
        scanCode();


        webSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scanTxt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String ShowUrl = scanTxt.getText().toString();
                    Toast.makeText(MainActivity.this, ShowUrl, Toast.LENGTH_SHORT).show();
                    searchWeb(ShowUrl);
                }

//                Uri webpage = Uri.parse(ShowUrl);
//                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                }
            }
        });

        copyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scanTxt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Nothing to Copy", Toast.LENGTH_SHORT).show();
                }
                else {
                    String ScanCode = scanTxt.getText().toString();
                    copyToClipBoard(ScanCode);
                }
            }
        });

        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (scanTxt.getText().toString().isEmpty()){
                   Toast.makeText(MainActivity.this, "Nothing to Share", Toast.LENGTH_SHORT).show();
               }
               else {
                   Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                   sharingIntent.setType("text/plain");
                   String SharingText = scanTxt.getText().toString();
                   sharingIntent.putExtra(Intent.EXTRA_TEXT, SharingText);
                   startActivity(Intent.createChooser(sharingIntent,"Share via"));
               }
            }
        });

//

    }

    @Override
    public void onClick(View view) {
//       scanCode();
    }

    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(true);
//        these lines are added
        integrator.setTorchEnabled(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);

        integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
        //*********************************
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage(intentResult.getContents());
//                builder.setTitle("Scanned Value");
//                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        scanCode();
//                    }
//                }).setNegativeButton("finish", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();

                scanBtn.setVisibility(View.GONE);
                scanTxt.setText(intentResult.getContents());


          }
            else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void searchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void copyToClipBoard(String text){
        ClipboardManager clipboardManager = (ClipboardManager) getApplication().getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copied data", text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getApplication(), "Copied To Clipboard", Toast.LENGTH_SHORT).show();

    }

}