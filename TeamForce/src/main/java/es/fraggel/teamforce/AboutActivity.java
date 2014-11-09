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


public class AboutActivity extends Activity implements View.OnClickListener {
    Button contacto = null;
    Button visit = null;
    ImageButton imageButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.imageButtonLogo).setOnClickListener(this);
        findViewById(R.id.imageButtonFacebook).setOnClickListener(this);
        findViewById(R.id.imageButtonGoogle).setOnClickListener(this);
        findViewById(R.id.imageButtonRss).setOnClickListener(this);
        findViewById(R.id.imageButtonTwitter).setOnClickListener(this);
        findViewById(R.id.imageButtonWeb).setOnClickListener(this);
        findViewById(R.id.imageButtonYoutube).setOnClickListener(this);
        findViewById(R.id.textViewFacebook).setOnClickListener(this);
        findViewById(R.id.textViewGoogle).setOnClickListener(this);
        findViewById(R.id.textViewRss).setOnClickListener(this);
        findViewById(R.id.textViewTwitter).setOnClickListener(this);
        findViewById(R.id.textViewWeb).setOnClickListener(this);
        findViewById(R.id.textViewYoutube).setOnClickListener(this);
    }

    public void onClick(View view) {
        int id=view.getId();
        String url =null;
        Intent i = new Intent(Intent.ACTION_VIEW);
        if(id==R.id.imageButtonLogo){
            url=getResources().getString(R.string.urlWeb);
            i.setData(Uri.parse(url));
        }if(id==R.id.imageButtonFacebook || id==R.id.textViewFacebook){
            url=getResources().getString(R.string.urlFacebook);
            i.setData(Uri.parse(url));
        }if(id==R.id.imageButtonGoogle || id==R.id.textViewGoogle){
            url=getResources().getString(R.string.urlGoogle);
            i.setData(Uri.parse(url));
        }if(id==R.id.imageButtonRss || id==R.id.textViewRss){
            url=getResources().getString(R.string.urlRss);
            i.setData(Uri.parse(url));
        }if(id==R.id.imageButtonTwitter || id==R.id.textViewTwitter){
            url=getResources().getString(R.string.urlTwitter);
            i.setData(Uri.parse(url));
        }if(id==R.id.imageButtonWeb || id==R.id.textViewWeb){
            url=getResources().getString(R.string.urlWeb);
            i.setData(Uri.parse(url));
        }if(id==R.id.imageButtonYoutube || id==R.id.textViewYoutube){
            url=getResources().getString(R.string.urlYoutube);
            i.setData(Uri.parse(url));
        }
        startActivity(i);
    }
}
