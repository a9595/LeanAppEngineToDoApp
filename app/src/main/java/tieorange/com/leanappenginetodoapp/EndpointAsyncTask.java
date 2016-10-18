package tieorange.com.leanappenginetodoapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;
import com.example.tieorange.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import java.io.IOException;

/**
 * Created by tieorange on 18/10/2016.
 */

public class EndpointAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
  private static MyApi sMyApiService = null;
  private Context mContext;

  @Override protected String doInBackground(Pair<Context, String>... params) {
    initApiService();
    mContext = params[0].first;
    String name = params[0].second;

    try {
      return sMyApiService.sayHi(name).execute().getData();
    } catch (IOException e) {
      return e.getMessage();
    }
  }

  private void initApiService() {
    if (sMyApiService != null) return;
    MyApi.Builder builder =
        new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null).setRootUrl("http://10.0.2.2:8080/_ah/api/")
            .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
              @Override public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                request.setDisableGZipContent(true);
              }
            });
    sMyApiService = builder.build();
  }

  @Override protected void onPostExecute(String result) {
    Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
  }
}
