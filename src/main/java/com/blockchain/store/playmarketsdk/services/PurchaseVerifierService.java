package com.blockchain.store.playmarketsdk.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.blockchain.store.playmarketsdk.utilities.Node;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by samsheff on 27/09/2017.
 */

public class PurchaseVerifierService extends IntentService {
    public PurchaseVerifierService() {
        super("PurchaseVerifierService");
    }

    public static final long PURCHASE_CHECK_DELAY = 1000;//3600000;
    public static final String NODE_URL = "https://n";
    public static final String PLAYMARKET_BASE_URL = ".playmarket.io";
    public static final String CHECK_PURCHASE_URL = "/v2/loadApp";
    public static String node = "";

    @Override
    protected void onHandleIntent(Intent workIntent) {
        if (shouldCheckIfPurchased()) {
            Log.d("PMSDK", "Will Verify Purchase");

            try {
                String nodeIp = getNearestNodes();
                node = NODE_URL + nodeIp + PLAYMARKET_BASE_URL;

                Log.d("PMSDK", node);
            } catch (IOException e) {
                e.printStackTrace();
            }

            checkPurchase();
        } else {
            Log.d("PMSDK", "Will Not Verify Purchase");

        }
    }

    private void checkPurchase() {
        try {
            URL url = new URL(makeCheckPurchaseUrl("", "", "", ""));
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.d("PMSDK", "App Unverified, Exiting");
                System.exit(0);
            } else {
                Log.d("PMSDK", "App Verified!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean shouldCheckIfPurchased() {
        long installTime = 0;
        try {
            installTime = getInstallTime();
            long time = System.currentTimeMillis();
            return time > installTime + PURCHASE_CHECK_DELAY;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }

    public long getInstallTime() throws PackageManager.NameNotFoundException {
        PackageManager pm = this.getPackageManager();

        PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
        return packageInfo.firstInstallTime;
    }

    protected String getNearestNodes() throws IOException {
        ArrayList coords = Node.getCoordinates();
        Log.d("Location", coords.get(0).toString() + "," + coords.get(1).toString());

        String[] nodes = Node.getNodesList(Node.NODES_DNS_SERVER);
        for (String node : nodes) {
            Log.d("Node", node);
        }

        String nearestNodeIP = Node.getNearestNode(nodes, (double) coords.get(0), (double) coords.get(1));
        Log.d("Node", nearestNodeIP);

        return nearestNodeIP;
    }

    public static String makeCheckPurchaseUrl(String address, String idApp, String idCat, String hashIPFS) {
        Log.d("NET", node + CHECK_PURCHASE_URL + "?address=" + address + "&idApp=" + idApp + "&idCTG=" + idCat + "&hashIpfs=" + hashIPFS);
        return node + CHECK_PURCHASE_URL + "?address=" + address + "&idApp=" + idApp + "&idCTG=" + idCat + "&hashIpfs=" + hashIPFS;
    }
}
