package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


// control the fragments going back and forth
public class ExportActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private static String TAGstatic = "TAG: ExportActivity" ;
    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = ExportActivity.this;
    private File internalPath;
    private File sdCardPath;

    Button csvButton, pdfButton;

    FoodLogViewModel mFoodLogViewModel;

    // variables for exporting pdf
    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;
    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();

            // do something
        } else if (item.getItemId() == R.id.action_go_home) {
            // do something
            startActivity(new Intent(thisActivity, MainActivity.class));
        } else {
            // do something
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set view
        setContentView(R.layout.activity_export);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_export);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);
        // set the paths for internal memory and external SD card
        Context context = getBaseContext();
        internalPath = context.getFilesDir();
        sdCardPath = context.getExternalFilesDir(null);

        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);

        // Button to go to ingredient's list and edit and delete and add
        csvButton = findViewById(R.id.button_csv);
        csvButton.setOnClickListener(view -> {
            exportDatabaseToSdCard(view, Util.ARGUMENT_CSV);
        });

        // setup for PDF export
        // initializing our variables.
        pdfButton = findViewById(R.id.button_pdf);
        // uncomment below 2 lines to add an image to PDF
//        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gfgimage);
//        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        pdfButton.setOnClickListener(view -> {
            exportDatabaseToSdCard(view, Util.ARGUMENT_PDF);
        });

    }

    private void exportDatabaseToSdCard(View view, String fileType) {
        String mFileName = Util.setFileName(fileType);
        Cursor cursor = mFoodLogViewModel.viewModelGetCursorAllFoodLog();
        File file = null;

        // if the cursor found a database
        if (cursor.moveToFirst()) {
            // create the file
            file = new File(sdCardPath, mFileName);
            // check if we can add to the file
            FileOutputStream stream = null;
            try {
                // true here means append to the file if it already exists
                stream = new FileOutputStream(file, true);
                try {
                    // now's the time to write to the actual file
                    // so loop through the database and append each line to the file
                    // CSV can do line by line so if the phone can't finish all in one go it's
                    // still there, PDF requires all data at once
                    if (fileType == Util.ARGUMENT_CSV) {
                        stream = exportDatabaseToCsv(cursor, mFoodLogViewModel, stream);
                    }
                    else if (fileType == Util.ARGUMENT_PDF){
                        // have to get all the data and add it to PDF at once

                        // get the data to export
                        StringBuilder stringBuilder = new StringBuilder();
                        do {
                            // get the food log
                            FoodLog mFoodLog = mFoodLogViewModel.viewModelGetFoodLogFromId(
                                    UUID.fromString(cursor.getString(0))
                            );
                            // get the info to put into the file
                            String prettyStringForPdf = processedLine(mFoodLog);
                            stringBuilder.append(prettyStringForPdf);
                        }
                        while (
                            // while there's still a database entry, go to next and repeat
                                cursor.moveToNext()
                        );

                        PdfDocument pdfDocument = makePdf(stringBuilder.toString());

                        // TODO move this to before data is processed, it would suck to wait for the database to
                        //  be converted to a string and only at the very end the pdf can't write
                        try {
                            // after creating a file name we will
                            // write our PDF file to that location.
                            pdfDocument.writeTo(stream);

                            // below line is to print toast message
                            // on completion of PDF generation.
                            Toast.makeText(ExportActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            // below line is used
                            // to handle error
                            e.printStackTrace();
                        }
                        // after storing our pdf to that
                        // location we are closing our PDF file.
                        pdfDocument.close();
                    }
                    // when we've gone through the whole database close the new file
                    stream.close();

                } catch (IOException e){
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } //end moving through database

        emailExportedDatabase(file.getPath(), mFileName);

    }

    private FileOutputStream exportDatabaseToCsv(Cursor cursor, FoodLogViewModel foodLogViewModel,
                                  FileOutputStream stream) throws IOException {

        do {
            // get the food log
            FoodLog mFoodLog = foodLogViewModel.viewModelGetFoodLogFromId(
                    UUID.fromString( cursor.getString(0) )
            );
            // get the info to put into the file
            String mFoodLogString = processedLine(mFoodLog);
            // add it to our file to write
            stream.write(mFoodLogString.getBytes());
        }
        while (
            // while there's still a database entry, go to next and repeat
                cursor.moveToNext()
        );
        Toast.makeText(thisActivity, "Successfully exported as CSV, yay!", Toast.LENGTH_SHORT).show();

        return stream;
    }


    private PdfDocument makePdf(String data) {

        PdfDocument pdfDocument = new PdfDocument();

        // for drawing shapes use "paint"
        Paint paint = new Paint();

        // pageWidth, pageHeight and number of pages
        PdfDocument.PageInfo mPageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight,
                1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page mPage = pdfDocument.startPage(mPageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = mPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        // uncomment below 2 lines to add an image in
        //Paint paintLogo = new Paint();
        //canvas.drawBitmap(scaledbmp, 56, 40, paintLogo);

        // below line is used for adding typeface

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        // below line is used for setting text size
        paint.setTextSize(15);
        // below line is sued for setting color
        paint.setColor(ContextCompat.getColor(this, R.color.green_700));

        // our text, position from start, position from top
        canvas.drawText("A log journaling symptoms and food and drinks consumed.", 209, 100, paint);
        String appNameString = getResources().getString(R.string.app_name);
        canvas.drawText(appNameString, 209, 80, paint);

        // similarly we are creating another text
        // aligning this text to center
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        paint.setColor(ContextCompat.getColor(this, R.color.black));
        paint.setTextSize(15);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(data, 396, 560, paint);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(mPage);

        return pdfDocument;
    }

    private String processedLine(FoodLog foodLog){
        StringBuilder info = new StringBuilder(foodLog.getIngredientId().toString());
        info.append("\n");

        return info.toString();
    }

    private void emailExportedDatabase(String folderName, String fileName){
        try {
            File root= Environment.getExternalStorageDirectory();
            String filelocation= root.getAbsolutePath() + folderName + "/" + fileName;
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            String message="File to be shared is " + fileName + ".";
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse( "file://"+filelocation));
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setData(Uri.parse("mailto:xyz@gmail.com"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        } catch(Exception e)  {
            System.out.println("is exception raises during sending mail"+e);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

