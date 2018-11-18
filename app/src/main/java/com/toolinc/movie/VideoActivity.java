package com.toolinc.movie;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;

import com.toolinc.movie.databinding.ActivityVideoBinding;

public class VideoActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video);

    ActivityVideoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_video);
    binding.webView.setWebChromeClient(new WebChromeClient());
    binding.webView.getSettings().setJavaScriptEnabled(true);
    binding.webView.getSettings().setLoadWithOverviewMode(true);
    binding.webView.getSettings().setUseWideViewPort(true);
    binding.webView.loadUrl(String.format(BuildConfig.VIDEO_BASE_URL, "u9Mv98Gr5pY"));
  }
}
