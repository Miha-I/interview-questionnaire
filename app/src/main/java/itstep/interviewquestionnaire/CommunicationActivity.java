package itstep.interviewquestionnaire;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CommunicationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
    }

    public void onClick(View view){
        Resources resources = getResources();
        Intent intent;
        switch (view.getId()){
            // Звонок
            case R.id.id_btn_call:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, R.string.call_app_noPermission, Toast.LENGTH_SHORT).show();
                    break;
                }
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + resources.getString(R.string.phone_number)));
                if(intent.resolveActivity(getPackageManager()) == null){
                    Toast.makeText(this, R.string.call_app_notFound, Toast.LENGTH_SHORT).show();
                    break;
                }
                startActivity(intent);
                break;
            // Отправка письма
            case R.id.id_btn_sendEmail:
                intent = new Intent(Intent.ACTION_SENDTO)
                        .setData(Uri.parse("mailto:"))
                        .putExtra(Intent.EXTRA_EMAIL, new String[]{resources.getString(R.string.email_address)})
                        .putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.email_subject))
                        .putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.email_text));
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, R.string.email_app_notFound, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}