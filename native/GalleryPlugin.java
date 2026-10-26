package com.junnie.levelup;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@CapacitorPlugin(name = "Gallery")
public class GalleryPlugin extends Plugin {
    @PluginMethod
    public void save(PluginCall call) {
        String data = call.getString("data");
        String name = call.getString("name", "levelup.png");
        String mime = call.getString("mime", "image/png");
        if (data == null) {
            call.reject("No image data");
            return;
        }
        try {
            byte[] bytes = Base64.decode(data, Base64.DEFAULT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver cr = getContext().getContentResolver();
                ContentValues v = new ContentValues();
                v.put(MediaStore.Images.Media.DISPLAY_NAME, name);
                v.put(MediaStore.Images.Media.MIME_TYPE, mime);
                v.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Level UP");
                v.put(MediaStore.Images.Media.IS_PENDING, 1);
                Uri uri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, v);
                if (uri == null) {
                    call.reject("Could not create the image");
                    return;
                }
                try (OutputStream os = cr.openOutputStream(uri)) {
                    os.write(bytes);
                }
                v.clear();
                v.put(MediaStore.Images.Media.IS_PENDING, 0);
                cr.update(uri, v, null, null);
            } else {
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Level UP");
                dir.mkdirs();
                File f = new File(dir, name);
                try (FileOutputStream os = new FileOutputStream(f)) {
                    os.write(bytes);
                }
                MediaScannerConnection.scanFile(getContext(), new String[]{f.getAbsolutePath()}, new String[]{mime}, null);
            }
            call.resolve(new JSObject());
        } catch (Exception e) {
            call.reject(e.getMessage());
        }
    }
}
