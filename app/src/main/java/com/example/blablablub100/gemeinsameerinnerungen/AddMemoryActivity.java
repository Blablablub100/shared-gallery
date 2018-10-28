package com.example.blablablub100.gemeinsameerinnerungen;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.Config;
import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.ExperienceWriter;
import com.example.blablablub100.gemeinsameerinnerungen.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMemoryActivity extends AppCompatActivity {

    EditText memoryName = null;
    Calendar memoryDateCalendar = Calendar.getInstance();
    TextView chooseMemoryDate = null;
    EditText memoryDescription = null;
    TextView chooseMemoryFile = null;
    Uri memoryFileUri = null;
    File memoryFile = null;
    File destFile = null;

    File currPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        memoryName = findViewById(R.id.editText_memory_name);
        memoryDescription = findViewById(R.id.editText_memory_description);
        chooseMemoryDate = findViewById(R.id.textView_choose_memory_date);
        chooseMemoryFile = findViewById(R.id.textView_choose_file);

        chooseMemoryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });

        chooseMemoryFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        Button saveButton = findViewById(R.id.button_save_memory);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chooseMemoryDate.getText().toString().equals("Datum auswählen")) {
                    Toast.makeText(getBaseContext()
                            , "Du musst erst ein Datum setzen :)"
                            , Toast.LENGTH_SHORT).show();
                } else if (memoryName.getText().toString().length() == 0) {
                    Toast.makeText(getBaseContext()
                            , "Du musst der Erinnerung einen Naaaamen geben"
                            , Toast.LENGTH_SHORT).show();
                } else if (chooseMemoryFile.getText().toString().equals("Datei auswählen")
                        && memoryDescription.getText().toString().length() == 0) {
                    Toast.makeText(getBaseContext()
                            , "Ich kann mit diesen Angaben nichts anfaaangen :o\n" +
                                    "Du musst entweder eine Datei auswählen und etwas dazu schreiben\n" +
                                    "oder du schreibst etwas und gibst keine Datei an."
                            , Toast.LENGTH_SHORT).show();
                } else if (chooseMemoryFile.getText().toString().equals("Datei auswählen")
                        && memoryDescription.getText().toString().length() > 0) {
                    ExperienceWriter.writeTextMemory(memoryName.getText().toString()
                            , memoryDateCalendar.getTime()
                            , memoryDescription.getText().toString()
                            , getIntent().getStringExtra("path"));
                    finish();
                } else {
                    copyUriToFile();
                    ExperienceWriter.writeDescription(memoryDescription.getText().toString(), destFile);
                    finish();
                }
            }
        });

    }

    public void chooseFile() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 123);

        //startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123 && resultCode==RESULT_OK) {
            memoryFileUri = data.getData(); //The uri with the location of the file
            memoryFile = new File (getPath(this, memoryFileUri));
            String filename = "_data_";
            if (memoryFile != null) filename = memoryFile.getName();
            chooseMemoryFile.setText(filename);
            Date date = FileUtil.getDateFromCamera(filename);
            if (date == null) {
                date = new Date(memoryFile.lastModified());
            }
            if (!date.before(new Date((new Long(800000000000L))))) {
                memoryDateCalendar.setTime(date);
                updateLabel(chooseMemoryDate, memoryDateCalendar);
            }

            Toast.makeText(getBaseContext(), Config.printdf.format(date), Toast.LENGTH_SHORT).show();
        }
    }







    private void copyUriToFile() {
        String path = "";
        String filename = Config.df.format(memoryDateCalendar.getTime())
                + "_"
                + memoryName.getText().toString();
        if (memoryFile != null) {
            String str = memoryFile.getAbsolutePath();
            str = str.substring(str.lastIndexOf("."));
            filename = filename + str;
        } else {
            try {
                String str = memoryFileUri.getPath();
                str = str.substring(str.lastIndexOf("."));
                filename = filename + str;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        path = getIntent().getStringExtra("path") + filename;
        destFile = new File(path);
        try (InputStream in = getContentResolver().openInputStream(memoryFileUri)) {
            destFile.createNewFile();
            try (OutputStream out = new FileOutputStream(destFile)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // From here onwards just for choosing time and date
    DatePickerDialog.OnDateSetListener setDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            memoryDateCalendar.set(Calendar.YEAR, year);
            memoryDateCalendar.set(Calendar.MONTH, monthOfYear);
            memoryDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            timePicker();
            updateLabel(chooseMemoryDate, memoryDateCalendar);
        }
    };

    private void chooseDate() {
        new DatePickerDialog(AddMemoryActivity.this, setDateListener, memoryDateCalendar
                .get(Calendar.YEAR), memoryDateCalendar.get(Calendar.MONTH),
                memoryDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel(TextView tv, Calendar cal) {
        String myFormat = "dd.MM.yyyy HH:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

        tv.setText(sdf.format(cal.getTime()));
    }


    private void timePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        memoryDateCalendar.set(Calendar.HOUR_OF_DAY , hourOfDay);
                        memoryDateCalendar.set(Calendar.MINUTE, minute);
                        memoryDateCalendar.set(Calendar.SECOND, 00);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }






    // CODE for getting path of URI
    public static String getPath(final Context context, final Uri uri) {

        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
