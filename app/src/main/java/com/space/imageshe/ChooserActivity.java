package com.space.imageshe;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.polites.android.GestureImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChooserActivity extends Activity {
    private Button btChoose;
    private GestureImageView iv;
    private static final int SELECT_PHOTO=0,CROP_PHOTO=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        iv= (GestureImageView) findViewById(R.id.chsimg);
        btChoose= (Button) findViewById(R.id.btChs);
        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case SELECT_PHOTO:
                if (resultCode== Activity.RESULT_OK)
                {
                    if (resultCode == Activity.RESULT_OK) {
                        Bitmap bitmap = null;
                        ContentResolver cResolver = getContentResolver();
                        Uri uri = data.getData();
                        Log.i("uri = ", uri.toString());
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(cResolver, uri);
                            //ivHead.setImageBitmap(bitmap);
                            String[] proj = { MediaStore.Images.Media.DATA };
                            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            String path = cursor.getString(column_index);
                            Glide.with(this).load(new File(path))./*transform(new GlideCircleTransform(this)).*/into(iv);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {
                            //bitmap.recycle();
                        }
                    }
                }
                break;
            case CROP_PHOTO:
                Bitmap bitmap=null;
                if (data.getData()!=null)
                {
                    bitmap = (Bitmap)data.getExtras().get("data");
                }
                else
                {
                    Uri uri=data.getData();
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                //ivHead.setImageBitmap(bitmap);
                break;
        }
    }
}
