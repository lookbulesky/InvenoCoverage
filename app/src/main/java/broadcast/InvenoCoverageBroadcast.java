package broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import service.InvenoCoverageService;

public class InvenoCoverageBroadcast extends BroadcastReceiver{
    String TAG = "InvenoCoverageBroadcast";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: packageName="+intent.getStringExtra("packageName"));
        String action = intent.getAction();
        if(action.equals("com.paix.invenocoverage.broadcast.InvenoCoverageBroadcast")){
            Intent intent1 = new Intent(context,InvenoCoverageService.class);
            intent1.putExtra("forIn",intent.getIntExtra("forIn",0));
            intent1.putExtra("doWhat",intent.getStringExtra("doWhat"));
            intent1.putExtra("ECfileName",intent.getStringExtra("ECfileName"));
            intent1.putExtra("packageName",intent.getStringExtra("packageName"));
            context.startService(intent1);
        }
    }
}
