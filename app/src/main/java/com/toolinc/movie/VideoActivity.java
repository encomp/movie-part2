package com.toolinc.movie;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebChromeClient;

import com.toolinc.movie.client.model.Video;
import com.toolinc.movie.databinding.ActivityVideoBinding;

/** Defines the behavior of the {@code R.layout.activity_video} to play a video. */
public final class VideoActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video);

    ActivityVideoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_video);
    binding.webView.setWebChromeClient(new WebChromeClient());
    binding.webView.getSettings().setJavaScriptEnabled(true);
    binding.webView.getSettings().setLoadWithOverviewMode(true);
    binding.webView.getSettings().setUseWideViewPort(true);

    if (getIntent().hasExtra(Intent.EXTRA_KEY_EVENT)) {
      Video video = (Video) getIntent().getSerializableExtra(Intent.EXTRA_KEY_EVENT);
      setTitle(video.name());
      binding.webView.loadUrl(String.format(BuildConfig.VIDEO_BASE_URL, video.key()));
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
