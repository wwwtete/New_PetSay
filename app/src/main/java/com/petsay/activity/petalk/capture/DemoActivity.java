package com.petsay.activity.petalk.capture;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.petsay.constants.Constants;
import com.petsay.utile.FileUtile;
import com.petsay.vo.petalk.PetTagVo;

import java.io.File;

/**
 * Created by sam on 14-10-17.
 *
 * this is a demo
 */
public class DemoActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri=getIntent().getParcelableExtra("uri");
        int model = getIntent().getIntExtra("model",0);
        PetTagVo tagVo = (PetTagVo) getIntent().getSerializableExtra("tag");

        new Crop(uri,getIntent().getIntExtra("fromType", 0),model,tagVo)
                .output(Uri.fromFile(new File(FileUtile.getPath(getApplicationContext(), Constants.FilePath)+ "capture.jpg")))
                .withWidth(640)
                .start(this);
        finish();
    }
}