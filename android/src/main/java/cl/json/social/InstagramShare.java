package cl.json.social;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import java.io.File;
import android.os.Environment;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by Ralf Nieuwenhuizen on 10-04-17.
 */
public class InstagramShare extends SingleShareIntent {

    private static final String PACKAGE = "com.instagram.android";
    private static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=com.instagram.android";

    public InstagramShare(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public void open(ReadableMap options) throws ActivityNotFoundException {
        super.open(options);
        try {
            // passing instagram://share as url option opens Publishing screen with camera view
            if (ShareIntent.hasValidKey("url", options) && options.getString("url").startsWith("instagram://")) {
                this.getIntent().setAction(Intent.ACTION_VIEW);
                this.getIntent().setData(Uri.parse(options.getString("url")));
            }
        } catch (Exception e) {
            Log.w("RNShare", e.getMessage());
        }

        this.intent.putExtra("bottom_background_color", "#906df4");
        this.intent.putExtra("top_background_color", "#837DF4");
        
        Boolean hasBackgroundAsset = this.hasValidKey("backgroundImage", options)
                || this.hasValidKey("backgroundVideo", options);

        if (hasBackgroundAsset) {
            String backgroundFileName = "";

            if (this.hasValidKey("backgroundImage", options)) {
                backgroundFileName = options.getString("backgroundImage");
            } else if (this.hasValidKey("backgroundVideo", options)) {
                backgroundFileName = options.getString("backgroundVideo");
            }

            ShareFile backgroundAsset = new ShareFile(backgroundFileName, "image/jpeg", "background" , this.reactContext);

            this.intent.setDataAndType(backgroundAsset.getURI(), backgroundAsset.getType());
            this.intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        this.openIntentChooser();
    }

    @Override
    protected String getPackage() {
        return PACKAGE;
    }

    @Override
    protected String getDefaultWebLink() {
        return null;
    }

    @Override
    protected String getPlayStoreLink() {
        return PLAY_STORE_LINK;
    }
}
