package it.artform;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ListView testCacheNotificheListView = findViewById(R.id.testCacheNotificheListView);

        cacheNotifications();
    }

    // TEST
    public void cacheNotifications() {
        boolean canReadStoreExt = false;
        boolean canWriteStoreExt = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            // Storage esterno disponibile in lettura e scrittura.
            canReadStoreExt = canWriteStoreExt = true;
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Storage esterno disponibile solo in lettura.
            canReadStoreExt = true;
            canWriteStoreExt = false;
        }
        else
            // Storage esterno non disponibile.
            canReadStoreExt = canWriteStoreExt = false;
        if(canReadStoreExt && canWriteStoreExt) {
            File dir = Environment.getExternalStorageDirectory();
            //localizzazione directory 'Download' tramite API I/O di Java
            List<String> notifications = loadFile(dir.toString());
        }
    }

    private List<String> loadFile(String filename) { //restituire lista di String da far caricare ad ArrayAdapter e alla ListView
        String line = null;
        List<String> res = null;
        try {
            InputStream in = openFileInput(filename);
            if (in != null) {
                InputStreamReader input = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(input);
                res = new ArrayList<>();
                while((line = buffreader.readLine()) != null)
                    res.add(line);
                in.close();
                //Toast.makeText(getApplicationContext(),"File Data == " + res,Toast.LENGTH_SHORT).show();
            }
            else {
                /* Do something*/
                Toast.makeText(getApplicationContext(), "No cached notifications", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.toString() + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return res;
    }

    private void saveFile(String filename, String data) {
        try {
            FileOutputStream fos = openFileOutput(filename, this.MODE_PRIVATE|this.MODE_APPEND);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e("Controller", e.getMessage() + e.getLocalizedMessage() + e.getCause());
        }
    }
}