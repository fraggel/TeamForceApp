package es.fraggel.teamforce;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class AboutActivity extends Activity {
    Button contacto = null;
    Button visit = null;
    ImageButton imageButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        addListenerOnButton();
        contacto = (Button) findViewById(R.id.button1);
        visit = (Button) findViewById(R.id.button2);
    }

    public void addListenerOnButton() {
        try {

            imageButton = (ImageButton) findViewById(R.id.imageButton1);
            imageButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {

                    Uri uri = Uri.parse("http://www.androidteamforce.es");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

            });

            visit = (Button) findViewById(R.id.button2);
            visit.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    try {

                        Uri uri = Uri.parse("http://www.androidteamforce.es/");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.msgGenericError), Toast.LENGTH_SHORT).show();
                    }
                }

            });
            contacto = (Button) findViewById(R.id.button1);
            contacto.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    Resources res2 = getResources();
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL,
                            new String[]{"jiayuteamforce@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, res2.getString(R.string.msgSubjectInfo));
                    i.putExtra(Intent.EXTRA_TEXT, "");
                    try {

                        startActivity(Intent.createChooser(i,
                                res2.getString(R.string.enviarEmailBtn)));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.msgGenericError), Toast.LENGTH_SHORT).show();
                    }
                }

            });
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
