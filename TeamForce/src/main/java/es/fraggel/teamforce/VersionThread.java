package es.fraggel.teamforce;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class VersionThread extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    String urlActualizacion = "";

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        String updateContenido="";
        InputStreamReader isr = null;
        BufferedReader in = null;
        try {
            URL jsonUrl = new URL("http://www.androidteamforce.es/desarrollo/teamforceapkversion.asd");
            in = new BufferedReader(new InputStreamReader(jsonUrl.openStream()));
            result = in.readLine();
            urlActualizacion = in.readLine();

            String updateContenidoAux="";
            while( (updateContenidoAux = in.readLine()) != null)
            {
                updateContenido=updateContenido+updateContenidoAux+"----";
            }

            if (in != null) {
                in.close();
            }
        } catch (Exception ex) {
            result = "TIMEOUT";
            urlActualizacion = "";
            try {
                if (in != null) {
                    in.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (Exception e) {
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        String inicio = "";
        if (params.length > 1) {
            inicio = params[1];
        }
        result = result.replaceAll("\t", "").trim();
        return inicio + "-;-" + result + "----" + urlActualizacion.trim()+"----"+updateContenido;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
