package com.FMJJ.MandySa;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import androidx.swiperefreshlayout.widget.*;
import com.FMJJ.MandySa.*;
import mandysax.Annotation.*;
import mandysax.Lifecycle.*;
import mandysax.Tools.*;

@BindLayoutId(R.layout.pact)
public class PactActivity extends LifecycleActivity
{

	@BindView(R.id.pactWebView1)
	private WebView webview;

	@BindView(R.id.pactSwipeRefreshLayout1)
	private SwipeRefreshLayout sr_webview;

	@BindView(R.id.pactOK)
	private TextView yes;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AnnotationTool.init(this);
		webview.loadUrl("http://39.106.7.220/mandysa/about.html");
		webview.setWebViewClient(new WebViewClient(){
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon)
				{
					sr_webview.setRefreshing(true);
				}

				@Override
				public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
				{
					ToastUtils.showToast("网络错误:" + error.getErrorCode());
				}
				
				@Override
				public void onPageFinished(WebView view, String url)
				{
					sr_webview.setRefreshing(false);
				}
			});
			
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
		
		yes.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					PactActivity.this.startActivity(new Intent(PactActivity.this, MainActivity.class));
					PactActivity.this.finish();
				}


			});
	}

}
