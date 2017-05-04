package com.bizhawkz.d2dRoni;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class UploadPresc extends AppCompatActivity {
    ImageView upload, im;
    Button btnload, btnpres;
    TextView sk, email1, num, address, city1, pincode1, name;
    ImageView edt;
    String aaa, ddd, mail, name1, number1, address1, city, state1, pincode, image1, mail4,strJson;
    SharedPreferences settings, pwd;
    ArrayList<Chemist> actorsList;

    private ImageView buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private EditText editText;
    Intent intent;
    ActorAdapter adapter;
    SessionManager1 session;
    public static final String PREFS_NAME = "LoginPrefs";
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    Integer a = 0;

    ProgressDialog pb;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_presc);
       // btnpres.setVisibility(View.INVISIBLE);

        btnpres = (Button) findViewById(R.id.image_upload);
        btnpres.setVisibility(View.INVISIBLE);
        btnpres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadMultipart();
            }
        });

        btnload = (Button) findViewById(R.id.btn_continue);
        btnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(UploadPresc.this, FirstAid1.class);
                Bundle b = new Bundle();
                b.putString("imageapth", image1);
                it.putExtras(b);
                startActivity(it);
            }
        });

        pb = new ProgressDialog(UploadPresc.this);

        session = new SessionManager1(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        mail4 = user.get(SessionManager1.KEY_EMAIL);

        name = (TextView) findViewById(R.id.textView3);
        email1 = (TextView) findViewById(R.id.mail);
        num = (TextView) findViewById(R.id.number);
        address = (TextView) findViewById(R.id.address);
        city1 = (TextView) findViewById(R.id.city);
        pincode1 = (TextView) findViewById(R.id.pincode);

        new Logmem().execute("http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=19&email=" + mail4 + "");

        upload = (ImageView) findViewById(R.id.buttonChoose);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                btnpres.setVisibility(View.VISIBLE);
            }
        });

        sk = (TextView) findViewById(R.id.tv_skip);
        sk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(UploadPresc.this, FirstAid1.class);
                Bundle b = new Bundle();
                b.putString("name", aaa);
                b.putString("imageapth", ddd);
                it.putExtras(b);
                startActivity(it);

            }
        });

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadMultipart() {

        String path = getPath(filePath);


        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId,Constants.UPLOAD_URL)
                    .addFileToUpload(path, "fileToUpload")
                    .addParameter("name", mail)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
            String filename=path.substring(path.lastIndexOf("/")+1);
            image1="http://d2dmedicine.com/aPPmob_lie/prescription/"+filename+"";
            Log.e("image path",image1);
            session.createprescription(image1.replaceAll(" ", ""));
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getPath(Uri filePath) {
        Cursor cursor = getContentResolver().query(filePath, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                upload.setImageBitmap(getCircleBitmap(bitmap));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    private class Logmem extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UploadPresc.this);
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsono = new JSONObject(data);

                    JSONArray jarray = jsono.getJSONArray("result");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        name1 = object.getString("first_name");
                        number1 = object.getString("mobileno");
                        address1 = object.getString("address");
                        city = object.getString("city");
                        state1 = object.getString("state");
                        pincode = object.getString("pincode");

                    }
                    return true;

                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            name.setText(name1);
            email1.setText(mail4);
            num.setText(number1);
            address.setText(address1);
            city1.setText(city);
            pincode1.setText(pincode);
            if (result == false) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UploadPresc.this);
                TextView myMsg = new TextView(UploadPresc.this);
                myMsg.setText("Alert!");
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextSize(20);
                myMsg.setTextColor(Color.BLACK);
                builder.setCustomTitle(myMsg);
                builder.setMessage("Please login to proceed further.");
                builder.setPositiveButton("OK.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                session.logoutUser();
                            }
                        });
                builder.show();
            }

        }
    }
}