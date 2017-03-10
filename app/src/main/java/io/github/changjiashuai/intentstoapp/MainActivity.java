package io.github.changjiashuai.intentstoapp;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private WebView mWebView;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webview);

        //设置编码
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        //支持js
        mWebView.getSettings().setJavaScriptEnabled(true);
        //设置背景颜色 透明
        mWebView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        //设置本地调用对象及其接口
        mWebView.addJavascriptInterface(new JavaScriptObject(this), "myObj");
        //载入js
        mWebView.loadUrl("file:///android_asset/test.html");

//        //点击调用js中方法
//        mBtn1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mWebView.loadUrl("javascript:funFromjs()");
//                Toast.makeText(MainActivity.this, "调用javascript:funFromjs()", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    public void startAPP(String appPackageName) {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "startAPP: ", e);
        }
    }

    /**
     * 启动一个app
     * com -- ComponentName 对象，包含apk的包名和主Activity名
     * param -- 需要传给apk的参数
     */
    private void startApp(ComponentName com, String param) {
        if (com != null) {
            PackageInfo packageInfo;
            try {
                packageInfo = getPackageManager().getPackageInfo(com.getPackageName(), 0);
                Log.i(TAG, "startApp: " + packageInfo.activities);
            } catch (PackageManager.NameNotFoundException e) {
                packageInfo = null;
                Toast.makeText(this, "没有安装", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            try {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(com);
                if (param != null) {
                    Bundle bundle = new Bundle(); // 创建Bundle对象
                    bundle.putString("flag", param); // 装入数据
                    intent.putExtras(bundle); // 把Bundle塞入Intent里面
                }
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "startApp: ", e);
                Toast.makeText(this, "启动异常", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class JavaScriptObject {
        Context mContxt;


        public JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }

        @JavascriptInterface
        public void fun1FromAndroid(String name) {
            Toast.makeText(mContxt, name, Toast.LENGTH_LONG).show();
//            startAPP("com.facebook.katana");
//            ComponentName componentName = new ComponentName("com.facebook.katana", "com.facebook.katana.IntentUriHandler");
//            startApp(componentName, null);

            String facebookScheme = "http://www.facebook.com/groups/474514739424333";
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookScheme));
            startActivity(facebookIntent);
        }

        public void fun2(String name) {
            Toast.makeText(mContxt, "调用fun2:" + name, Toast.LENGTH_SHORT).show();
        }
    }

}
