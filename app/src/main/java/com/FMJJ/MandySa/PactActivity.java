package com.FMJJ.MandySa;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mandysax.Lifecycle.LifecycleActivity;
import mandysax.Tools.ToastUtils;

public class PactActivity extends LifecycleActivity
{

	private WebView webview;

	private SwipeRefreshLayout sr_webview;

	private TextView no,yes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pact);
		webview = findViewById(R.id.pactWebView1);
		webview.loadUrl("http://39.106.7.220/mandysa/about.html");
		webview.setWebViewClient(new WebViewClient(){
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon)
				{
					sr_webview.setRefreshing(true);
				}

				@Override
				public void onReceivedError(WebView view, WebResourceRequest request,WebResourceError error)
				{
					ToastUtils.showToast("网络错误:"+error.getErrorCode());
				}
				@Override
				public void onPageFinished(WebView view, String url)
				{
					sr_webview.setRefreshing(false);
				}
			});
		sr_webview = findViewById(R.id.pactSwipeRefreshLayout1);
        sr_webview.setColorScheme(R.color.theme_color);
        sr_webview.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh()
				{
					webview.reload();
                }
            }
        );
		yes = findViewById(R.id.pactOK);
		yes.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					PactActivity.this.startActivity(new Intent(PactActivity.this,MainActivity.class));
					PactActivity.this.finish();
				}
				
			
		});
	}

}
