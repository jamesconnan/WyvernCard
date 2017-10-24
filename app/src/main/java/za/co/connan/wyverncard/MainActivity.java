package za.co.connan.wyverncard;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public static int total = 0;
    public static final String EXTRA_MESSAGE = "connan.co.za.wyverncard.MESSAGE";
    NfcAdapter nfcAdapter;


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        TextView nfcStatus = (TextView) findViewById(R.id.textView7);

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            //Toast.makeText(this, "NFC available", Toast.LENGTH_LONG).show();
            nfcStatus.setTextColor(Color.RED);
            nfcStatus.setText("Tap card to pay");
        } else {
            nfcStatus.setTextColor(Color.BLACK);
            nfcStatus.setText("NFC OFF");
            //Toast.makeText(this, "NFC NOT available", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onNewIntent(Intent nfcIntent){
        Toast.makeText(this, "NFC Intent Received", Toast.LENGTH_SHORT).show();
        super.onNewIntent(nfcIntent);

        Tag myTag = (Tag) nfcIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


        TextView transactionView = (TextView) findViewById(R.id.transactionView);
        String message = transactionView.getText().toString();

        if (message.length()> 3){

            TextView totalView = (TextView) findViewById(R.id.totalView);
            String mesg = totalView.getText().toString();

            message = message + "#" + bytesToHexString(myTag.getId()) + "#" + mesg;
            //message = message + "#" + "#" + mesg;

            // initiate a Switch
            Switch networkSwitch = (Switch) findViewById(R.id.networkSwitch);

            // check current state of a Switch (true or false).
            Boolean switchState = networkSwitch.isChecked();
            if (!switchState)
            {
                verifyStoragePermissions(this);
            }

            message = message + "#" + switchState.toString();
            //Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            Intent payIntent = new Intent(this, PayActivity.class);
            payIntent.putExtra(EXTRA_MESSAGE, message);
            startActivity(payIntent);
        }
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onResume() {
        //Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show();
        total = 0;

        Intent nfcIntent = new Intent(this, MainActivity.class);
        nfcIntent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);

        super.onResume();
    }

    @Override
    protected void onPause(){
        //Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();

        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }


    /** Called when the user taps the Send button */
    public void sendMessageR5(View view) {
        // Do something in response to button
        TextView textView = (TextView) findViewById(R.id.transactionView);
        textView.append("1 x R5\n");
        total = total + 5;
        TextView textView2 = (TextView) findViewById(R.id.totalView);
        textView2.setText(String.valueOf(total));
    }

    public void sendMessageR10(View view) {
        // Do something in response to button
        TextView textView = (TextView) findViewById(R.id.transactionView);
        textView.append("1 x R10\n");
        total = total + 10;
        TextView textView2 = (TextView) findViewById(R.id.totalView);
        textView2.setText(String.valueOf(total));
    }

    public void sendMessageR15(View view) {
        // Do something in response to button
        TextView textView = (TextView) findViewById(R.id.transactionView);
        textView.append("1 x R15\n");
        total = total + 15;
        TextView textView2 = (TextView) findViewById(R.id.totalView);
        textView2.setText(String.valueOf(total));
    }

    public void sendMessageR20(View view) {
        // Do something in response to button
        TextView textView = (TextView) findViewById(R.id.transactionView);
        textView.append("1 x R20\n");
        total = total + 20;
        TextView textView2 = (TextView) findViewById(R.id.totalView);
        textView2.setText(String.valueOf(total));
    }

    public void sendMessageR25(View view) {
        // Do something in response to button
        TextView textView = (TextView) findViewById(R.id.transactionView);
        textView.append("1 x R25\n");
        total = total + 25;
        TextView textView2 = (TextView) findViewById(R.id.totalView);
        textView2.setText(String.valueOf(total));
    }

    public void sendMessageR30(View view) {
        // Do something in response to button
        TextView textView = (TextView) findViewById(R.id.transactionView);
        textView.append("1 x R30\n");
        total = total + 30;
        TextView textView2 = (TextView) findViewById(R.id.totalView);
        textView2.setText(String.valueOf(total));
    }

    public void sendClear(View view) {
        // Do something in response to button
        TextView textView = (TextView) findViewById(R.id.transactionView);
        textView.setText("");
        total = 0;
        TextView textView2 = (TextView) findViewById(R.id.totalView);
        textView2.setText(String.valueOf(total));
    }

    /*public void sendPay(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, PayActivity.class);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        String message = textView2.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/

    /*private void readTextFromMessage (NdefMessage ndefMessage){
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            txtContent = getTextFromNdefRecord(ndefRecord);
        }else{
            Toast.makeText(this, "NFC record NOT found", Toast.LENGTH_LONG).show();
        }
    }

    public String getTextFromNdefRecord(NdefRecord ndeRecord){
        String tagContent = null;
        try {
            byte[] payLoad = ndeRecord.getPayload();
            String textEncoding = ((payLoad[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int langagueSize = payLoad[0] & 0063;
            tagContent = new String (payLoad,langagueSize+1, payLoad.length -langagueSize-1, textEncoding);

        } catch (UnsupportedEncodingException e){
            Log.e("getTextFromNdeRecord",e.getMessage(),e);
        }
        //Toast.makeText(this, tagContent, Toast.LENGTH_LONG).show();
        return tagContent;
    }*/

    /*    public void makeUsers(){
        verifyStoragePermissions(this);
        String datastr = "0x048f87b2004680#Fast Pass" + "\n"+ "0x04571c52844c80#Magic Band";


        // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DOCUMENTS + mdir);
                                //Environment.DIRECTORY_DOCUMENTS);

        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path, wfile);

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
            Toast.makeText(this, "File success", Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            //Log.e("Exception", "File write failed: " + e.toString());
        }
    }*/

    /*
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
