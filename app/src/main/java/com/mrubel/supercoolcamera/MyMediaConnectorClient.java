package com.mrubel.supercoolcamera;

import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * Created by Mitu on 7/8/2018.
 */

public class MyMediaConnectorClient implements MediaScannerConnection.MediaScannerConnectionClient {

    String _fisier;
    MediaScannerConnection MEDIA_SCANNER_CONNECTION;

    public MyMediaConnectorClient(String _fisier) {
        this._fisier = _fisier;

    }

    public MyMediaConnectorClient(MediaScannerConnection MEDIA_SCANNER_CONNECTION) {
        this.MEDIA_SCANNER_CONNECTION = MEDIA_SCANNER_CONNECTION;
    }

    @Override
    public void onMediaScannerConnected() {

        MEDIA_SCANNER_CONNECTION.scanFile(_fisier, null);


    }

    @Override
    public void onScanCompleted(String s, Uri uri) {

        if(s.equals(_fisier))
            MEDIA_SCANNER_CONNECTION.disconnect();

    }
}
