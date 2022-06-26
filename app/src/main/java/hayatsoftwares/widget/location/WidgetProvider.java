package hayatsoftwares.widget.location;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Locale;

import hayatsoftwares.widget.location.Service.WidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            Intent serviceIntent = new Intent(context , WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews views = new RemoteViews(context.getPackageName() , R.layout.widget_provider);
            views.setRemoteAdapter(R.id.listView , serviceIntent);
            views.setEmptyView(R.id.listView , R.id.loadingView);

            Intent clickIntent = new Intent(context , WidgetProvider.class);
            clickIntent.setAction("Click");
            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context , 0 , clickIntent , 0);
            views.setPendingIntentTemplate(R.id.listView , clickPendingIntent);

            ComponentName cn = new ComponentName(context, WidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(cn), R.id.listView);
            appWidgetManager.updateAppWidget(appWidgetId , views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant funcrtionality for when the first widget is created
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        ComponentName cn = new ComponentName(context, WidgetProvider.class);
        mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.listView);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if("Click".equals(intent.getAction())){

            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, WidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.listView);





            if(intent.getStringExtra("Identifier").equals("Google")) {
                Toast.makeText(context, "Latitude ---> " + intent.getStringExtra("LAT") + " Longitude --> " + intent.getStringExtra("LONG"), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:" + intent.getStringExtra("LAT") + "," + intent.getStringExtra("LONG")));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Only if initiating from a Broadcast Receiver
                String mapsPackageName = "com.google.android.apps.maps";
                if (isPackageExisted(context, mapsPackageName)) {
                    i.setClassName(mapsPackageName, "com.google.android.maps.MapsActivity");
                    i.setPackage(mapsPackageName);
                } else {
                    // Toast.makeText(context, "App not found", Toast.LENGTH_SHORT).show();
                }
                context.startActivity(i);
            }else if(intent.getStringExtra("Identifier").equals("Waze")){
               // Toast.makeText(context, "Waze Btn is Clicked", Toast.LENGTH_SHORT).show();
                float lat = Float.parseFloat(intent.getStringExtra("LAT"));
                float longitiude = Float.parseFloat(intent.getStringExtra("LONG"));
                String wazeUrl = "https://www.waze.com/ul?ll="+intent.getStringExtra("LAT")+"%2C"+intent.getStringExtra("LONG")+"&navigate=yes&zoom=17";
                Uri uri = Uri.parse(wazeUrl);
                Intent intent1 = new  Intent(Intent.ACTION_VIEW, uri);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        }
        super.onReceive(context, intent);
    }
    private static boolean isPackageExisted(Context context, String targetPackage){
        PackageManager pm=context.getPackageManager();
        try {
            PackageInfo info=pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }
}