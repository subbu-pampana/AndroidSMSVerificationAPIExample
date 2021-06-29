package com.example.androidsmsverificationapi.autofill;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

public class HashCodeHelper extends ContextWrapper {

    public HashCodeHelper(Context context) {
        super(context);
        getAppSignaturesList();
    }

    public ArrayList<String> getAppSignaturesList() {
        ArrayList<String> appCodesList = new ArrayList<>();
        try {
            String packageName = getPackageName();
            PackageManager packageManagerInstance = getPackageManager();
            Signature[] signatures = packageManagerInstance.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES).signatures;
            for (Signature signature : signatures) {
                String hashCode = getHashValue(packageName, signature.toCharsString());
                if (hashCode != null) {
                    appCodesList.add(String.format("%s", hashCode));
                }
            }
        } catch (Exception e) {
        }
        return appCodesList;
    }

    private static String getHashValue(String packageName, String signature) {
        String applicationInfo = packageName + " " + signature;
        try {
            MessageDigest messageDigestInstance = MessageDigest.getInstance("SHA-256");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                messageDigestInstance.update(applicationInfo.getBytes(StandardCharsets.UTF_8));
            }
            byte[] hashSignature = messageDigestInstance.digest();
            hashSignature = Arrays.copyOfRange(hashSignature, 0, 9);
            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
            base64Hash = base64Hash.substring(0, 11);
            return base64Hash;
        } catch (Exception e) {
        }
        return null;
    }
}
