package edu.floridapoly.polycamsportal.util;

import android.util.Log;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public final class PolyAuthenticator extends Authenticator {

    public static final String API_DOMAIN = "poly-cams-scraper.herokuapp.com";

    private static final String TAG =
        PolyAuthenticator.class.getCanonicalName();

    private static PolyAuthenticator instance = new PolyAuthenticator();

    private PasswordAuthentication apiAuth = null;

    public static PolyAuthenticator getInstance() {
        return instance;
    }

    /**
     * Set this authenticator as the default authenticator for the app.
     * Note: This should be done during app initialization so that any API
     * requests made can be authenticated.
     */
    public static void setAsDefault() {
        Authenticator.setDefault(getInstance());
    }

    /**
     * Globally sets the username and password for the scraper API
     * authentication. This must be called before any API requests are made.
     *
     * @param username Florida Poly username.
     * @param password Florida Poly password.
     */
    public static void setApiAuth(String username, String password) {
        getInstance().apiAuth = new PasswordAuthentication(
            username, password.toCharArray());
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        // Only send authentication when using HTTPS
        String protocol = getRequestingProtocol();
        if (!protocol.equals("https")) {
            Log.w(TAG,
                "Authentication requested over protocol: " + protocol + ". " +
                "Authentication will only be sent over HTTPS.");
            return null;
        }

        // Only Basic authentication is supported
        String scheme = getRequestingScheme();
        if (!scheme.equals("Basic")) {
            Log.w(TAG, "Authentication scheme not supported: " + scheme);
            return null;
        }

        // Only give the credentials to the API domain. Give other domains no
        // authentication.
//        if (host.equals(Resources.getSystem().getString(R.string.api_domain)) {
        String host = getRequestingHost();
        if (host.equals(API_DOMAIN)) {
            return apiAuth;
        } else {
            Log.w(TAG,
                "Authentication requested for unknown host: " + host);
            return null;
        }
    }

}
