package es.fraggel.teamforce;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class BrowserActivity extends Activity {
    Resources res;
    PackageManager pm = null;
    String urlDestino = "";
    static NotificationManager mNotificationManagerUpdate=null;
    static NotificationManager mNotificationManagerNews=null;
    private int SIMPLE_NOTFICATION_UPDATE=8888;
    private int SIMPLE_NOTFICATION_NEWS=8889;
    SharedPreferences ajustes=null;
    SharedPreferences.Editor editorAjustes=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        res = this.getResources();
        pm = this.getPackageManager();
        isDownloadManagerAvailable(getBaseContext());
        Intent intent = getIntent();
        String modelo = intent.getExtras().getString("modelo");
        String tipo = intent.getExtras().getString("tipo");
        WebView descargas = (WebView) findViewById(R.id.webView1);
        descargas.setWebViewClient(new TeamForceWebViewClient());
        descargas.setDownloadListener(new TeamForceDownloadListener());

        if ("drivers".equals(tipo)) {
            descargas.loadUrl("http://www.androidteamforce.es/desarrollo/apptools.php");
        }else if ("downloads".equals(tipo)) {
            mNotificationManagerNews = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManagerNews.cancel(SIMPLE_NOTFICATION_NEWS);
            ajustes=getSharedPreferences("TeamForceAjustes",Context.MODE_PRIVATE);
            editorAjustes = ajustes.edit();
            editorAjustes.putString("modelo", modelo);
            editorAjustes.putString("fechaUltimoAccesoDescargas", asignaFecha());
            editorAjustes.commit();
            descargas.loadUrl("http://www.androidteamforce.es/desarrollo/appsoft.php?modelo=" + modelo);
        }

    }

    class TeamForceDownloadListener implements DownloadListener {

        public void onDownloadStart(String s, String s2, String s3, String s4, long l) {

        }

    }

    class TeamForceWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            urlDestino = url;
            if (urlDestino.lastIndexOf("/desarrollo/") != -1 && urlDestino.lastIndexOf("appsoft") == -1) {
                try {
                    String nombreFichero = "";
                    nombreFichero = urlDestino.split("/")[urlDestino.split("/").length - 1];
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlDestino));
                    request.setDescription(nombreFichero);
                    request.setTitle(nombreFichero);
                    File f1 = new File(Environment.getExternalStorageDirectory() + "/TEAMFORCE/APP/");
                    if (!f1.exists()) {
                        f1.mkdirs();
                    }
                    File f2 = new File(Environment.getExternalStorageDirectory() + "/TEAMFORCE/ROMS/");
                    if (!f2.exists()) {
                        f2.mkdirs();
                    }

                    if (Build.VERSION.SDK_INT >= 11) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        if (".apk".equals(nombreFichero.substring(nombreFichero.length() - 4, nombreFichero.length()).toLowerCase())) {
                            request.setMimeType("application/vnd.android.package-archive");
                            if (nombreFichero.indexOf("TeamForce.apk") == -1) {
                                try {
                                    new File(f1.getAbsolutePath() + "/TeamForce.apk").delete();
                                } catch (Exception e) {

                                }

                            }
                        }

                    }
                    String rutaDescarga = null;
                    if (nombreFichero.indexOf(".apk") != -1) {
                        rutaDescarga = "/TEAMFORCE/APP/";
                    } else if (nombreFichero.indexOf("signed_") != -1) {
                        rutaDescarga = "/TEAMFORCE/ROMS/";
                    }
                    request.setDestinationInExternalPublicDir(rutaDescarga, nombreFichero);

                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    //Toast.makeText(getBaseContext(), getResources().getString(R.string.msgIniciandoDescarga) + " " + nombreFichero, Toast.LENGTH_SHORT).show();
                    MainActivity.listaDescargas.put(String.valueOf(manager.enqueue(request)), nombreFichero);

                    //manager.enqueue(request);

                } catch (Exception e) {
                    //Toast.makeText(getBaseContext(), getResources().getString(R.string.msgGenericError), Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if(urlDestino.lastIndexOf("appsoft") != -1){
                return false;
            }else{
                Uri uri = Uri.parse(urlDestino);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            }
        }
    }

    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } catch (Exception e) {
            return false;
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

    @Override
    public void onBackPressed() {
        if (urlDestino.lastIndexOf("appsoftdetails") != -1) {
            WebView descargas = (WebView) findViewById(R.id.webView1);
            descargas.setWebViewClient(new TeamForceWebViewClient());
            descargas.setDownloadListener(new TeamForceDownloadListener());
            isDownloadManagerAvailable(getBaseContext());
            Intent intent = getIntent();
            String modelo = intent.getExtras().getString("modelo");
            descargas.loadUrl("http://www.androidteamforce.es/desarrollo/appsoft.php?modelo=" + modelo);
            urlDestino="http://www.androidteamforce.es/desarrollo/appsoft.php?modelo=" + modelo;
        }else{
            super.onBackPressed();
        }
    }
}
