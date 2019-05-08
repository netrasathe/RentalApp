import android.app.ProgressDialog;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageView;
import android.provider.MediaStore;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

public class PDFMain extends AppCompatActivity implements View.OnClickListener {


    ProgressDialog prog;
    ListView listView;
    Button select;
    ImageView imageView;

    private EditText edit;
    private Button choice;
    private Button uploading;
    private int CHOOSEPDF = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;

    ArrayList<Pdf> pdfList = new ArrayList<Pdf>();
    PdfAdapter pdfAdapter;

    public static final String UPLOAD = "http://internetfaqs.net/AndroidPdfUpload/upload.php";
    public static final String DOWNLOAD = "http://internetfaqs.net/AndroidPdfUpload/getPdfs.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestStoragePermission();
        choice = (Button) findViewById(R.id.choice);
        uploading = (Button) findViewById(R.id.uploading);

        edit = (EditText) findViewById(R.id.editTextName);
        listView = (ListView) findViewById(R.id.listView);
        select = (Button) findViewById(R.id.buttonFetchPdf);

        prog = new ProgressDialog(this);
        choice.setOnClickListener(this);
        uploading.setOnClickListener(this);
        select.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Pdf pdf = (Pdf) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(pdf.getUrl()));
                startActivity(intent);
            }
        });
    }


    private void PDFup() {

        prog.setMessage("Fetching Pdfs... Please Wait");
        prog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWNLOAD,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        prog.dismiss();
                        pdfList.clear();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray = obj.getJSONArray("pdfs");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Pdf pdf = new Pdf();
                                String pdfName = jsonObject.getString("name");
                                String pdfUrl = jsonObject.getString("url");
                                pdf.setName(pdfName);
                                pdf.setUrl(pdfUrl);
                                pdfList.add(pdf);
                            }

                            pdfAdapter = new PdfAdapter(MainActivity.this, R.layout.list_layout, pdfList);

                            listView.setAdapter(pdfAdapter);

                            pdfAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);

    }


    @Override
    public void onClick(View v) {
        if (v == choice) {
            showFileChooser();
        }
        if (v == uploading) {
            uploadMultipart();
        }

        if (v == select) {
            PDFup();
        }
    }
}
