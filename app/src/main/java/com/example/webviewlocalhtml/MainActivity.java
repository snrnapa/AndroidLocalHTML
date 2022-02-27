package com.example.webviewlocalhtml;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // リソースからファイルを生成する(/data/data/パッケージ名/files/)
    public boolean setRawResources(Context context , int resourcesID, String fileName){
        boolean result = false;

        // リソースの読み込み
        InputStream is =  context.getResources().openRawResource(resourcesID);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte [] buffer = new byte[1024];
        try{
            // 1024バイト毎、ファイルを読み込む
            while(true) {
                int len = is.read(buffer);
                if(len < 0)  break;
                baos.write(buffer, 0, len);
            }
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }

        // ファイルの生成
        File file = new File(context.getFilesDir() + "/" + fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                try{
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // リソースからファイルを生成する(/data/data/パッケージ名/files/に作成)
        setRawResources(this, R.raw.index, "index.html");
        setRawResources(this, R.raw.my, "my.js");

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView webView = findViewById(R.id.webView);

                // キャッシュクリア
                // ※開発時のみ有効にする
                webView.clearCache(true);

                // JavaScriptを有効にする
                webView.getSettings().setJavaScriptEnabled(true);

                // WebChromeClientを設定する
                // ※コレを設定しないとJSのalertは表示されない
                webView.setWebChromeClient(new WebChromeClient());

                // ファイルを読み込む
                webView.loadUrl("file:///" + MainActivity.this.getFilesDir() + "/index.html");
            }
        });

    }
}