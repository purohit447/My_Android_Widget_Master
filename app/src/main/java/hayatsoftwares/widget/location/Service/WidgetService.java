package hayatsoftwares.widget.location.Service;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import hayatsoftwares.widget.location.Model.Data;
import hayatsoftwares.widget.location.R;
import hayatsoftwares.widget.location.WidgetProvider;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        AndroidNetworking.initialize(getApplicationContext());




        return new WidgetItemFactory(getApplicationContext() , intent );
    }
    class WidgetItemFactory implements RemoteViewsFactory {
        private Context context;
        private int appWidgetId;
        private ArrayList<Data> list;

        WidgetItemFactory(Context context , Intent intent ){
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , AppWidgetManager.INVALID_APPWIDGET_ID);
        }


        @Override
        public void onCreate() {
            list = new ArrayList<>();
            list.removeAll(list);
            AndroidNetworking.get("http://service.sigura.ro/API/widgetAPI.php")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.e("testing " , response.toString());
                            for(int i = 0 ; i< response.length() ; i++){
                                try {
                                    JSONArray response2 = response.getJSONArray(i);
                                    String name = response2.getString(0);
                                    String lat = response2.getString(1);
                                    String longitude = response2.getString(2);
                                    //Toast.makeText(context, "Name -> " + name + " Lat -> " + lat + " Long -> " + longitude, Toast.LENGTH_SHORT).show();
                                    list.add(new Data(name , lat , longitude));
//                                    AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//                                    ComponentName cn = new ComponentName(context, WidgetProvider.class);
//                                    mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.listView);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //   Binder.restoreCallingIdentity(identityToken);

                            }
                           // SystemClock.sleep(10000);
                            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                            ComponentName cn = new ComponentName(context, WidgetProvider.class);
                            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.listView);

                        }
                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });




//            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//            ComponentName cn = new ComponentName(context, WidgetProvider.class);
//            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.listView);
//            list.add(new Data("Vodafone Bacau (detectie)","46.606495","26.928448"));

//            list.add(new Data("Vodafone Bacau (detectie)","46.606495","26.928448"));
//            list.add(new Data("Vodafone Bacau (detectie)","46.606495","26.928448"));
//            list.add(new Data("Vodafone Bacau (detectie)","46.606495","26.928448"));





        }

        @Override
        public void onDataSetChanged() {



           // final long identityToken = Binder.clearCallingIdentity();
            //list.removeAll(list);
//            AndroidNetworking.get("http://service.sigura.ro/API/widgetAPI.php")
//                    .setPriority(Priority.HIGH)
//                    .build()
//                    .getAsJSONArray(new JSONArrayRequestListener() {
//                        @Override
//                        public void onResponse(JSONArray response) {
//                            for(int i = 0 ; i< response.length() ; i++){
//                                try {
//                                    JSONArray response2 = response.getJSONArray(i);
//                                    String name = response2.getString(0);
//                                    String lat = response2.getString(1);
//                                    String longitude = response2.getString(2);
//                                    Toast.makeText(context, "Name -> " + name + " Lat -> " + lat + " Long -> " + longitude, Toast.LENGTH_SHORT).show();
//                                    list.add(new Data(name , lat , longitude));
////                                    AppWidgetManager mgr = AppWidgetManager.getInstance(context);
////                                    ComponentName cn = new ComponentName(context, WidgetProvider.class);
////                                    mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.listView);
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                             //   Binder.restoreCallingIdentity(identityToken);
//
//                            }
////                            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
////                            ComponentName cn = new ComponentName(context, WidgetProvider.class);
////                            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.listView);
//
//                        }
//                        @Override
//                        public void onError(ANError error) {
//                            // handle error
//                        }
//                    });


//            Toast.makeText(context, "Size -> " + list.size(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDestroy() {
            list.removeAll(list);

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName() , R.layout.listview_layout);
            views.setTextViewText(R.id.name_textView , list.get(position).getName());
            Intent fillInIntentGoogleMaps = new Intent();
            fillInIntentGoogleMaps.putExtra("Identifier","Google");
            fillInIntentGoogleMaps.putExtra("LAT" , list.get(position).getLatitude());
            fillInIntentGoogleMaps.putExtra("LONG",list.get(position).getLongitude());
            views.setOnClickFillInIntent(R.id.googleMapBtn, fillInIntentGoogleMaps);
            Intent fillInIntentWaze = new Intent();
            fillInIntentWaze.putExtra("Identifier","Waze");
            fillInIntentWaze.putExtra("LAT" , list.get(position).getLatitude());
            fillInIntentWaze.putExtra("LONG",list.get(position).getLongitude());
            views.setOnClickFillInIntent(R.id.wazeMapBtn, fillInIntentWaze);
            return views;

        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews( context.getPackageName(), R.layout.listview_layout_loading );
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
