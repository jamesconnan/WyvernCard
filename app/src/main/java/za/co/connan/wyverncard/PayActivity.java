package za.co.connan.wyverncard;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class PayActivity extends AppCompatActivity {

    //NfcAdapter nfcAdapter;
    String mdir ="/wyvern_data/";
    String wfile ="wyvernusers.txt";
    String bfile ="wyvernuserdata.txt";
    String lfile ="wyverntranslog.txt";

    String balance ="";
    String total = "";
    String userid ="";
    String trans = "";
    Boolean networked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        String[] separated = message.split("#");
        // Capture the layout's TextView and set the string as its text
        TextView amountView = (TextView) findViewById(R.id.amountView);
        amountView.setText("R " + separated[2]);
        total = separated[2];

        if (separated[3].compareTo("true") == 0){
            networked = true;
        } else {
            networked = false;
        }

        String name = "";
        userid = separated[1];
      if (networked){
            name = getNameFromServer(userid);
            balance = getBalanceFromServer(userid);
        } else {
            name = getNameFromFile(userid);
            balance = getBalanceFromFile(userid);
        }

        TextView nameView = (TextView) findViewById(R.id.nameView);
        nameView.setText(name);

        TextView transView = (TextView) findViewById(R.id.transView);
        transView.setText(separated[0]);
        trans = separated[0];


        TextView balancebox = (TextView) findViewById(R.id.balancebox);
        balancebox.setText(balance);

        Button confirmBtn = (Button) findViewById(R.id.button8);
        if((Integer.parseInt(balance) >= Integer.parseInt(total))) {
            confirmBtn.setEnabled(true);
        } else {
            confirmBtn.setEnabled(false);
        }


        /*nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
        } else {
            finish();
        }*/
    }

    public void onClickConfirm(View view){
        if (networked){
            writeBalanceToServer(userid);
            writeTransLogToServer();
        } else {
            writeBalanceToFile(userid);
            writeTransLogToFile();
        }
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PayActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public String getBalanceFromServer(String id)
    {
        return "0";
    }

    public String getBalanceFromFile(String id){
        String str ="";
        String nameList = readFromFile(bfile);
        //Toast.makeText(this, nameList, Toast.LENGTH_LONG).show();
        String[] names = nameList.split("\n");
        for (String s :names){
            if(id.compareTo(s.split("#")[0])==0){
                str = s.split("#")[1];
                break;
            }else{
                str = "000";
            }
        }
        return str;
    }

    public String setBalanceWithFile(String id){
        String str ="";
        String nameList = readFromFile(bfile);
        String[] names = nameList.split("\n");
        int newb = Integer.parseInt(balance)-Integer.parseInt(total);
        String newbs = String.valueOf(newb);
        for (String s :names){
            if(id.compareTo(s.split("#")[0])==0){
                str = str + id + "#" + newbs + "\n";
            }else{
                str = str + s + "\n";
            }
        }
        return str;
    }

    public void writeTransLogToServer (){

    }

    public void writeBalanceToFile(String id){
        verifyStoragePermissions(this);


        String datastr = setBalanceWithFile(id);
        // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                Environment.DIRECTORY_DOCUMENTS + mdir);
        //Environment.DIRECTORY_DOCUMENTS);

        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path, bfile);

        // Save your stream, don't forget to flush() it before closing it.

        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(datastr);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
            //Toast.makeText(this, "File success", Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            //Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void writeBalanceToServer(String id){

    }

    public void writeTransLogToFile(){
        verifyStoragePermissions(this);

        String datastr = userid + "#" + trans;
        // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                Environment.DIRECTORY_DOCUMENTS + mdir);
        //Environment.DIRECTORY_DOCUMENTS);

        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path, lfile);

        // Save your stream, don't forget to flush() it before closing it.

        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(datastr);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
            //Toast.makeText(this, "File success", Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            //Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String getNameFromServer(String id){
        return id;
    }

    public String getNameFromFile(String id){
        verifyStoragePermissions(this);
        String str ="";
        String nameList = readFromFile(wfile);
        String[] names = nameList.split("\n");
        for (String s :names){
            if(id.compareTo(s.split("#")[0])==0){
                str = s.split("#")[1];
                break;
            }else{
                str = id;
            }
        }
        return str;
    }
    
    private String readFromFile(String fname) {


        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
        //File sdcard = Environment.getExternalStorageDirectory();
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DOCUMENTS + mdir);
        //Environment.DIRECTORY_DOCUMENTS);

        //Get the text file
        File file = new File(path,fname);

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }


        return text.toString();
    }


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
