package com.example.kinmel.khalti;

import android.net.Uri;

import com.khalti.checkout.data.Environment;

public class KhaltiConfig {


        private String publicKey;
        private String pidx;
        private Uri returnUrl;
        private Environment environment;

        public KhaltiConfig(String publicKey, String pidx, String returnUrl, Environment environment) {
            this.publicKey = publicKey;
            this.pidx = pidx;
            this.returnUrl = Uri.parse(returnUrl);
            this.environment = environment;
        }

        // Getter methods for accessing properties if needed (optional)

        public String getPublicKey() {
            return publicKey;
        }

        // ... other getters (if needed)

}
