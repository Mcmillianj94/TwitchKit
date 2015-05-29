package mp.joshua.com.twitchkit.Services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;


public class ImageCoverterService extends IntentService {

    ParseSingleton mParseSingleton;
    public ImageCoverterService(){
        super("ImageConverterService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mParseSingleton = ParseSingleton.getInstance(ImageCoverterService.this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Uri photoUri = intent.getData();

        String actionId = intent.getAction();
        if (actionId.equals(ConstantsLibrary.ACTION_SETTING_COVERT_PHOTO)){
            byte[] data = null;
            try {
                ContentResolver cr = getBaseContext().getContentResolver();
                InputStream inputStream = cr.openInputStream(photoUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                data = baos.toByteArray();
                mParseSingleton.editProfilePicture(ParseUser.getCurrentUser(), data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
