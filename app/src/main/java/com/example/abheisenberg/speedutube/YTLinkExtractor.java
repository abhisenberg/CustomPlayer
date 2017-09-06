package com.example.abheisenberg.speedutube;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

/**
 * Created by abheisenberg on 5/9/17.
 */

public class YTLinkExtractor {
    public static final String TAG = "YTLinkExtractor";

    private String rawUrl;
    private static String usableUrl;
    Context context;

    public YTLinkExtractor(String rawUrl, Context context){
        this.rawUrl = rawUrl;
        this.context = context;
    }

    public String getYoutubeDownloadUrl(){
        new YouTubeExtractor(context){
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if(ytFiles == null){
                    Toast.makeText(context, "No url found", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Cannot get the URL");
                    usableUrl = "";
                }

                for(int i=0, itag; i < ytFiles.size(); i++){
                    itag = ytFiles.keyAt(i);
                    YtFile ytFile = ytFiles.get(itag);

                    Log.d(TAG, "Avaialable formats: ");
                    if(ytFile.getFormat().getHeight() != -1){
                        Log.d(TAG, "Q: "+String.valueOf(ytFile.getFormat().getHeight()));
                        Log.d(TAG, "Link: "+ytFile.getUrl());

                        usableUrl = ytFile.getUrl();
                    }

                }

            }
        }.extract(rawUrl, false, false);

        return usableUrl;
    }

}
