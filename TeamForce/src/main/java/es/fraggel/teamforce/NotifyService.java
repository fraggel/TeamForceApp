package es.fraggel.teamforce;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Fraggel on 10/08/13.
 */
public class NotifyService extends Service implements AsyncResponse {
    /*String newversion="";
    String urlActualizacion="";
    String nversion = "";
    static NotificationManager mNotificationManagerUpdate=null;
    static NotificationManager mNotificationManagerNews=null;
    private int SIMPLE_NOTFICATION_UPDATE=8888;
    private int SIMPLE_NOTFICATION_NEWS=8889;
    SharedPreferences ajustes=null;
    String modelo=null;
    @Override*/
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       /* try {
            nversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            ajustes=getSharedPreferences("JiayuesAjustes",Context.MODE_PRIVATE);
            modelo=ajustes.getString("modelo","");
            VersionThread asyncTask = new VersionThread();
            asyncTask.delegate = this;
            asyncTask.execute(nversion);

        } catch (Exception e) {
        }*/
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();


    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void processFinish(String output) {
        /*try {
            if (output != null && !"TIMEOUT----".equals(output)) {

                String inicio = output.split("-;-")[0];
                output = output.split("-;-")[1];
                String[] split = output.split("----");
                newversion = split[0].split(" ")[1];
                urlActualizacion = split[1];
                boolean notificaciones=ajustes.getBoolean("notificaciones",true);
                if(notificaciones){
                    if (!"".equals(urlActualizacion) && !nversion.equals(newversion) && (Float.parseFloat(nversion.replaceAll("Jiayu.es ", "")) < Float.parseFloat(newversion.replaceAll("Jiayu.es ", "")))) {
                        Resources res = this.getResources();
                        mNotificationManagerUpdate = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        final Notification notifyDetails = new Notification(R.drawable.ic_launcher,getBaseContext().getResources().getString(R.string.ntfNuevaVersionTxt),System.currentTimeMillis());
                        CharSequence contentTitle = getBaseContext().getResources().getString(R.string.ntfTituloNVTxt);
                        CharSequence contentText = getBaseContext().getResources().getString(R.string.ntfDetallesNVTxt)+newversion;
                        Intent launch_intent = new Intent();
                        launch_intent.setComponent(new ComponentName("es.jiayu.jiayuid", "es.jiayu.jiayuid.App"));
                        PendingIntent intent2;

                        intent2 = PendingIntent.getActivity(getBaseContext(), 0,
                                launch_intent, Intent.FLAG_ACTIVITY_NEW_TASK);
                        notifyDetails.setLatestEventInfo(getBaseContext(), contentTitle, contentText, intent2);
                        mNotificationManagerUpdate.notify(SIMPLE_NOTFICATION_UPDATE, notifyDetails);
                    }
                }*/
                /*if((split.length-2)>0){
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
                    if(modeloEncontrado){
                            String fechaAcceso=ajustes.getString("fechaUltimoAccesoDescargas","");
                            if(!"".equals(fechaAcceso)){

                                int[] ints = descomponerFecha(fechaAcceso);
                                Calendar calAcceso=Calendar.getInstance();
                                calAcceso.set(Calendar.DAY_OF_MONTH,ints[0]);
                                calAcceso.set(Calendar.MONTH,ints[1]);
                                calAcceso.set(Calendar.YEAR,ints[2]);
                                calAcceso.set(Calendar.HOUR,0);
                                calAcceso.set(Calendar.MINUTE,0);
                                calAcceso.set(Calendar.SECOND,0);
                                calAcceso.set(Calendar.MILLISECOND,0);
                                int[] ints1 = descomponerFecha(fecha);
                                Calendar calModificacion=Calendar.getInstance();
                                calModificacion.set(Calendar.DAY_OF_MONTH,ints1[0]);
                                calModificacion.set(Calendar.MONTH,ints1[1]);
                                calModificacion.set(Calendar.YEAR,ints1[2]);
                                calModificacion.set(Calendar.HOUR,0);
                                calModificacion.set(Calendar.MINUTE,0);
                                calModificacion.set(Calendar.SECOND,0);
                                calModificacion.set(Calendar.MILLISECOND,0);
                                if(calModificacion.after(calAcceso)){
                                    mNotificationManagerNews = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                    final Notification notifyDetails = new Notification(R.drawable.ic_launcher,getBaseContext().getResources().getString(R.string.ntfMinTxt),System.currentTimeMillis());
                                    CharSequence contentTitle = getBaseContext().getResources().getString(R.string.ntfTituloTxt);
                                    CharSequence contentText = getBaseContext().getResources().getString(R.string.ntfDetallesTxt);
                                    Intent launch_intent = new Intent();
                                    launch_intent.setComponent(new ComponentName("es.jiayu.jiayuid", "es.jiayu.jiayuid.BrowserActivity"));
                                    launch_intent.putExtra("modelo", modelo);
                                    launch_intent.putExtra("tipo", "downloads");
                                    PendingIntent intent2;
                                    intent2 = PendingIntent.getActivity(getBaseContext(), 0,
                                            launch_intent, Intent.FLAG_ACTIVITY_NEW_TASK);

                                    notifyDetails.setLatestEventInfo(getBaseContext(), contentTitle, contentText, intent2);
                                    mNotificationManagerNews.notify(SIMPLE_NOTFICATION_NEWS, notifyDetails);
                                }
                            }
                        }
                }*//*
            }
        } catch (Exception e) {

        }*/
    }
    private int[] descomponerFecha(String fechaPasada) {
        int day=Integer.parseInt(fechaPasada.trim().split("/")[0]);
        int month=Integer.parseInt(fechaPasada.trim().split("/")[1])-1;
        int year=Integer.parseInt(fechaPasada.trim().split("/")[2]);
        int fecha[]=new int[3];
        fecha[0]=day;
        fecha[1]=month;
        fecha[2]=year;
        return fecha;
    }
}
