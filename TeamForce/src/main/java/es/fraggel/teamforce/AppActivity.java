package es.fraggel.teamforce;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AppActivity extends Activity implements AsyncResponse {

    static long downloadREF = -1;
    static HashMap<String, String> listaDescargas = new HashMap<String, String>();
    String nversion = "";
    String version = "";
    ImageButton imglogo;
    TextView txtVersion;
    Button descargas;
    Button about;
    Button config;
    Button salir;
    String modelo = "";
    String model = "";
    String urlActualizacion = "";
    String fabricante = "";
    String compilacion = "";
    String newversion = "";
    boolean noInternet=false;
    boolean yaPase=false;
    public static boolean updatemostrado=false;
    static NotificationManager mNotificationManagerUpdate=null;
    static NotificationManager mNotificationManagerNews=null;
    private int SIMPLE_NOTFICATION_UPDATE=7777;
    private int SIMPLE_NOTFICATION_NEWS=7778;
    SharedPreferences ajustes=null;
    SharedPreferences.Editor editorAjustes=null;
    String listaIdiomas[]=null;
    protected void onResume() {
        super.onResume();
        listaIdiomas=getResources().getStringArray(R.array.languages_values);
        int i=ajustes.getInt("language",0);
        Locale locale =null;
        if(i==0){
            locale=getResources().getConfiguration().locale;
        }else{
            locale = new Locale(listaIdiomas[i]);
        }
        modificarMargins();
        updatemostrado=false;
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config,
                getApplicationContext().getResources().getDisplayMetrics());
        onCreate(null);

    }

    protected void onCreate(Bundle savedInstanceState) {
        try {
            updatemostrado=false;
            super.onCreate(savedInstanceState);
            nversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mNotificationManagerUpdate = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManagerUpdate.cancel(SIMPLE_NOTFICATION_UPDATE);
            ajustes=getSharedPreferences("TeamForceAjustes",Context.MODE_PRIVATE);
            editorAjustes=ajustes.edit();
            boolean notificacionesNews=ajustes.getBoolean("notificacionesNews",true);
            boolean notificacionesUpd=ajustes.getBoolean("notificacionesUpd",true);
            editorAjustes.putBoolean("notificacionesNews",notificacionesNews);
            editorAjustes.putBoolean("notificacionesUpd",notificacionesUpd);

            String tmpFecha="";
            tmpFecha=ajustes.getString("fechaUltimoAccesoDescargas", "");
            if("".equals(tmpFecha)){
                editorAjustes.putString("fechaUltimoAccesoDescargas", asignaFecha());
                editorAjustes.commit();
            }
            noInternet=comprobarConexion();

            comprobarVersionInicio(version);
            File f1 = new File(Environment.getExternalStorageDirectory() + "/TEAMFORCE/APP/");
            if (!f1.exists()) {
                f1.mkdirs();
            }
            File f2 = new File(Environment.getExternalStorageDirectory() + "/TEAMFORCE/ROMS/");
            if (!f2.exists()) {
                f2.mkdirs();
            }

                setContentView(R.layout.activity_main);
            modificarMargins();

            txtVersion=(TextView)findViewById(R.id.txtVersion);
            txtVersion.setText(getResources().getString(R.string.app_name)+" "+nversion);
            addListenerOnButton();
            if(noInternet){
                descargas.setEnabled(false);
            }else{
                descargas.setEnabled(true);
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.msgGenericError), Toast.LENGTH_SHORT).show();
        }
    }
    private boolean comprobarConexion() {
        boolean nohayinternet=false;
        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected()==true )
        {
            nohayinternet=false;

        }
        else
        {
            nohayinternet=true;
        }
        return nohayinternet;
    }
    private void comprobarVersionInicio(String version2) {
        try {
            VersionThread asyncTask = new VersionThread();
            asyncTask.delegate = this;
            asyncTask.execute(version2, "inicio");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgGenericError)+" 103", Toast.LENGTH_SHORT).show();
        }
    }
    public void openBrowser(View v, String tipo) {
        try {
            Intent intent = new Intent(this, BrowserActivity.class);
            intent.putExtra("modelo", modelo);
            intent.putExtra("tipo", tipo);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgGenericError)+" 112", Toast.LENGTH_SHORT).show();
        }
    }
    public void addListenerOnButton() {
        try {
            descargas = (Button) findViewById(R.id.btnDescargas);
            descargas.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    openBrowser(arg0, "downloads");
                }

            });
            about = (Button) findViewById(R.id.btnAbout);
            about.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    try {
                        /*Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(intent);*/
                        /*if(noInternet){*/
                            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                            startActivity(intent);
                        /*}else{
                            openBrowser(arg0, "about");
                        }*/
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgGenericError)+" 108", Toast.LENGTH_SHORT).show();
                    }
                }

            });


            config = (Button) findViewById(R.id.btnConfiguracion);
            config.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    try {
                        Intent intent = new Intent(getApplicationContext(), ConfigActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgGenericError)+" 121", Toast.LENGTH_SHORT).show();
                    }
                }

            });

            salir=(Button)findViewById(R.id.btnSalir);
            salir.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    finish();
                }

            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgGenericError)+" 111", Toast.LENGTH_SHORT).show();
        }

    }
    private String asignaFecha() {
        String fecha_mod=null;
        Calendar cal=Calendar.getInstance();
        String day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String month=String.valueOf((cal.get(Calendar.MONTH)+1));
        String year=String.valueOf(cal.get(Calendar.YEAR));
        if(day.length()<2){
            day="0"+day;
        }
        if(month.length()<2){
            month="0"+month;
        }
        fecha_mod=(day+"/"+month+"/"+year);
        return fecha_mod;
    }
    private void ActualizarVersion() {
        try {
            String nombreFichero = "";
            nombreFichero = urlActualizacion.split("/")[urlActualizacion.split("/").length - 1];
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlActualizacion));
            request.setDescription(nombreFichero);
            request.setTitle(nombreFichero);
            if (Build.VERSION.SDK_INT >= 11) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                if (".apk".equals(nombreFichero.substring(nombreFichero.length() - 4, nombreFichero.length()).toLowerCase())) {
                    request.setMimeType("application/vnd.android.package-archive");
                    File ffff=new File((Environment.getExternalStorageDirectory() + "/TEAMFORCE/APP/"));
                    File[] fDir=ffff.listFiles();
                    for(int x=0;x<fDir.length;x++){
                        File tmp=fDir[x];
                        if(tmp.getName().indexOf("TeamForce")!=-1){
                            tmp.delete();
                        }
                    }
                }

            }
            request.setDestinationInExternalPublicDir("/TEAMFORCE/APP/", nombreFichero);

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgIniciandoDescarga) + " " + nombreFichero, Toast.LENGTH_SHORT).show();
            downloadREF = manager.enqueue(request);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgGenericError)+" 104", Toast.LENGTH_SHORT).show();
        }
    }
    private void comprobarVersion(String version2) {
        try {
            VersionThread asyncTask = new VersionThread();
            asyncTask.delegate = this;
            asyncTask.execute(version2);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgGenericError)+" 102", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void processFinish(String output) {
        try {
            if (output != null && !"TIMEOUT----".equals(output)) {
                if(!updatemostrado){
                    String inicio = output.split("-;-")[0];
                    output = output.split("-;-")[1];
                    String[] split = output.split("----");
                    newversion = split[0].split(" ")[1];
                    urlActualizacion = split[1];
                    if (!"".equals(urlActualizacion) && !nversion.equals(newversion) && (Float.parseFloat(nversion.replaceAll("TeamForce ", "")) < Float.parseFloat(newversion.replaceAll("TeamForce ", "")))) {
                        updatemostrado=true;
                        Resources res = this.getResources();
                        AlertDialog dialog = new AlertDialog.Builder(this).create();
                        dialog.setMessage(res.getString(R.string.msgComprobarVersion) + " " + nversion + "->" + newversion + " " + res.getString(R.string.msgPreguntaVersion));
                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                                res.getString(R.string.cancelarBtn),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int witch) {
                                        AppActivity.updatemostrado=false;
                                    }
                                });
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                res.getString(R.string.aceptarBtn),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int witch) {
                                        try {
                                            AppActivity.updatemostrado=false;
                                            ActualizarVersion();
                                        } catch (Exception e) {
                                            Toast.makeText(getBaseContext(), getResources().getString(R.string.msgGenericError), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        dialog.show();
                    } else {
                        if ("".equals(inicio)) {
                            Resources res = this.getResources();
                            AlertDialog dialog = new AlertDialog.Builder(this).create();
                            dialog.setMessage(res.getString(R.string.msgLastVersion));
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                    res.getString(R.string.aceptarBtn),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int witch) {
                                        }
                                    });
                            dialog.show();
                        }
                        if((split.length-2)>0){
                            String fecha=null;
                            String model=null;
                            boolean modeloEncontrado=false;
                            for (int x =2;x<split.length;x++){
                                model=split[x].split("->")[0];
                                fecha=split[x].split("->")[1];
                                if(modelo.equals(model)){
                                    modeloEncontrado=true;
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            //noInternet=false;
        } catch (Exception e) {
            //noInternet=true;
            //Toast.makeText(getBaseContext(), getResources().getString(R.string.msgGenericError), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                comprobarVersion(nversion);
                return true;
            case R.id.action_about:
                try {
                    Intent intent = new Intent(this, AboutActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.msgGenericError), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_config:
                try {
                    Intent intent = new Intent(this, ConfigActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.msgGenericError), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_exit:
                finish();
            default:
                return super.onMenuItemSelected(featureId, item);

        }
    }
    private void modificarMargins() {
        int dpi=getResources().getDisplayMetrics().densityDpi;

        TextView t1=(TextView) findViewById(R.id.textView1);
        Button b1=(Button) findViewById(R.id.btnDescargas);
        Button b2=(Button) findViewById(R.id.button9);
        Button b3=(Button) findViewById(R.id.button10);
        Button b4=(Button) findViewById(R.id.btnAbout);
        Button b5=(Button) findViewById(R.id.button4);
        Button b6=(Button) findViewById(R.id.button3);
        Button b7=(Button) findViewById(R.id.btnConfiguracion);
        Button b8=(Button) findViewById(R.id.btnSalir);
        int orientation = getResources().getConfiguration().orientation;
        if(dpi>=160 && dpi<240){
        }else if(dpi>=240 && dpi<320) {
            if(orientation==2) {
                t1.setPadding(300, 0, 0, 0);
                b1.setPadding(150, 0, 0, 0);
                b1.setCompoundDrawablePadding(80);
                b2.setPadding(150, 0, 0, 0);
                b2.setCompoundDrawablePadding(80);
                b3.setPadding(150, 0, 0, 0);
                b3.setCompoundDrawablePadding(80);
                b4.setPadding(50, 0, 0, 0);
                b4.setCompoundDrawablePadding(10);
                b5.setPadding(40, 0, 0, 0);
                b5.setCompoundDrawablePadding(10);
                b6.setPadding(40, 0, 0, 0);
                b6.setCompoundDrawablePadding(10);
                b7.setPadding(50, 0, 0, 0);
                b7.setCompoundDrawablePadding(10);
                b8.setPadding(50, 0, 0, 0);
                b8.setCompoundDrawablePadding(10);
            }else{
                t1.setPadding(120, 0, 0, 0);
                b1.setPadding(90, 0, 0, 0);
                b1.setCompoundDrawablePadding(50);
                b2.setPadding(90, 0, 0, 0);
                b2.setCompoundDrawablePadding(50);
                b3.setPadding(90, 0, 0, 0);
                b3.setCompoundDrawablePadding(50);
                b4.setPadding(30, 0, 0, 0);
                b4.setCompoundDrawablePadding(10);
                b5.setPadding(20, 0, 0, 0);
                b5.setCompoundDrawablePadding(10);
                b6.setPadding(20, 0, 0, 0);
                b6.setCompoundDrawablePadding(10);
                b7.setPadding(30, 0, 0, 0);
                b7.setCompoundDrawablePadding(10);
                b8.setPadding(30, 0, 0, 0);
                b8.setCompoundDrawablePadding(10);
            }
        }else if(dpi>=320 && dpi<480) {
            if(orientation==2) {
                t1.setPadding(500, 0, 0, 0);
                b1.setPadding(170, 0, 0, 0);
                b1.setCompoundDrawablePadding(200);
                b2.setPadding(170, 0, 0, 0);
                b2.setCompoundDrawablePadding(200);
                b3.setPadding(170, 0, 0, 0);
                b3.setCompoundDrawablePadding(200);
                b4.setPadding(75, 0, 0, 0);
                b4.setCompoundDrawablePadding(15);
                b5.setPadding(30, 0, 0, 0);
                b5.setCompoundDrawablePadding(15);
                b6.setPadding(30, 0, 0, 0);
                b6.setCompoundDrawablePadding(15);
                b7.setPadding(75, 0, 0, 0);
                b7.setCompoundDrawablePadding(15);
                b8.setPadding(75, 0, 0, 0);
                b8.setCompoundDrawablePadding(15);
                /*b4.setPadding(250, 0, 0, 0);
                b5.setPadding(220, 0, 0, 0);
                b6.setPadding(200, 0, 0, 0);
                b7.setPadding(250, 0, 0, 0);
                b8.setPadding(205, 0, 0, 0);*/
            }else{
                t1.setPadding(200, 0, 0, 0);
                b1.setPadding(100, 0, 0, 0);
                b1.setCompoundDrawablePadding(70);
                b2.setPadding(100, 0, 0, 0);
                b2.setCompoundDrawablePadding(70);
                b3.setPadding(100, 0, 0, 0);
                b3.setCompoundDrawablePadding(70);
                b4.setPadding(40, 0, 0, 0);
                b4.setCompoundDrawablePadding(15);
                b5.setPadding(30, 0, 0, 0);
                b5.setCompoundDrawablePadding(5);
                b6.setPadding(30, 0, 0, 0);
                b6.setCompoundDrawablePadding(15);
                b7.setPadding(40, 0, 0, 0);
                b7.setCompoundDrawablePadding(15);
                b8.setPadding(40, 0, 0, 0);
                b8.setCompoundDrawablePadding(15);
                //b5.setPadding(120, 0, 0, 0);
                //b6.setPadding(115, 0, 0, 0);
                //b7.setPadding(140, 0, 0, 0);
                //b8.setPadding(125, 0, 0, 0);

            }
        }else if(dpi>=480 && dpi<680) {
            if(orientation==2) {
                t1.setPadding(550, 0, 0, 0);
                b1.setPadding(350, 0, 0, 0);
                b1.setCompoundDrawablePadding(250);
                b2.setPadding(350, 0, 0, 0);
                b2.setCompoundDrawablePadding(250);
                b3.setPadding(350, 0, 0, 0);
                b3.setCompoundDrawablePadding(250);
                b4.setPadding(105, 0, 0, 0);
                b4.setCompoundDrawablePadding(70);
                b5.setPadding(50, 0, 0, 0);
                b5.setCompoundDrawablePadding(70);
                b6.setPadding(50, 0, 0, 0);
                b6.setCompoundDrawablePadding(70);
                b7.setPadding(105, 0, 0, 0);
                b7.setCompoundDrawablePadding(70);
                b8.setPadding(75, 0, 0, 0);
                b8.setCompoundDrawablePadding(70);
            }else{
                t1.setPadding(290, 0, 0, 0);
                b1.setPadding(190, 0, 0, 0);
                b1.setCompoundDrawablePadding(100);
                b2.setPadding(190, 0, 0, 0);
                b2.setCompoundDrawablePadding(100);
                b3.setPadding(190, 0, 0, 0);
                b3.setCompoundDrawablePadding(100);
                b4.setPadding(60, 0, 0, 0);
                b4.setCompoundDrawablePadding(30);
                b5.setPadding(40, 0, 0, 0);
                b5.setCompoundDrawablePadding(15);
                b6.setPadding(40, 0, 0, 0);
                b6.setCompoundDrawablePadding(30);
                b7.setPadding(60, 0, 0, 0);
                b7.setCompoundDrawablePadding(30);
                b8.setPadding(60, 0, 0, 0);
                b8.setCompoundDrawablePadding(30);
            }
        }

    }
}
