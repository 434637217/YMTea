package com.ft;


import com.example.tupianfengzhuang.R;
import com.google.zxing.ui.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TwoCodeActivity extends Activity {
    public static final int SCAN_CODE = 1;
    AlertDialog.Builder alertDialog = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)this.findViewById(R.id.scan);
        button.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwoCodeActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_CODE);
            }
            
        });        
       
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView textView = (TextView)this.findViewById(R.id.mytext);
        switch (requestCode) {
        case SCAN_CODE:
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("scan_result");
                textView.setText("ɨ��ɹ���"+result);
            } else if (resultCode == RESULT_CANCELED) {
                textView.setText("ɨ��ʧ�ܣ�");
            }
            break;
        default:
            break;
        }
    }
}