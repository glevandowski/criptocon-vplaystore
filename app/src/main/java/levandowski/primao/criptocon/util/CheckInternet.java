package levandowski.primao.criptocon.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;

public class CheckInternet {
        private Context context;

        public CheckInternet(Context context){
            this.context = context;
        }

        public void dialogIfOffline() {
            new AlertDialog.Builder(context)
                    .setTitle("Problema de conexão")
                    .setMessage("Para acessar o app é preciso estar conectado, verifique sua conexão.")
                    .setNegativeButton("Ajustar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                        }
                    }).show();
        }

        public static boolean isConnected(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            return info != null && info.isConnectedOrConnecting();
        }

}
