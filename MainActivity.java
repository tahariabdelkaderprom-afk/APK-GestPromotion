package com.gestpromo.mobile;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.ValueCallback;
public class MainActivity extends Activity {
 private WebView web;
 private ValueCallback<Uri[]> uploadCallback;
 private static final int FILE_REQUEST=1001;
 private static final String HOME="https://bejewelled-melba-8562ed.netlify.app/";
 @Override public void onCreate(Bundle state){
  super.onCreate(state);
  web=new WebView(this);setContentView(web);
  WebSettings s=web.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);
  s.setAllowFileAccess(false);s.setAllowContentAccess(true);s.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
  web.setWebViewClient(new WebViewClient(){
   @Override public boolean shouldOverrideUrlLoading(WebView v,android.webkit.WebResourceRequest req){
    Uri u=req.getUrl();String host=u.getHost();
    if("https".equalsIgnoreCase(u.getScheme()) && "bejewelled-melba-8562ed.netlify.app".equalsIgnoreCase(host))return false;
    try {startActivity(new Intent(Intent.ACTION_VIEW,u));}catch(Exception ignored){} return true;
   }
  });
  web.setWebChromeClient(new WebChromeClient(){
   @Override public boolean onShowFileChooser(WebView v,ValueCallback<Uri[]> cb,FileChooserParams p){
    if(uploadCallback!=null)uploadCallback.onReceiveValue(null);uploadCallback=cb;
    try{startActivityForResult(p.createIntent(),FILE_REQUEST);return true;}
    catch(Exception e){uploadCallback=null;return false;}
   }
  });
  if(state==null)web.loadUrl(HOME);else web.restoreState(state);
 }
 @Override protected void onSaveInstanceState(Bundle b){web.saveState(b);super.onSaveInstanceState(b);}
 @Override protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==FILE_REQUEST&&uploadCallback!=null){uploadCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(c,d));uploadCallback=null;}}
 @Override public void onBackPressed(){if(web.canGoBack())web.goBack();else super.onBackPressed();}
}
