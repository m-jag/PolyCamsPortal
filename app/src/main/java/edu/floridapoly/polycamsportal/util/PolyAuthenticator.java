package edu.floridapoly.polycamsportal.util;

import android.content.res.Resources;
import android.util.Log;
import edu.floridapoly.polycamsportal.R;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public final class PolyAuthenticator extends Authenticator {

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
        String scheme = getRequestingScheme();
        String host = getRequestingHost();

        // Only send authentication when using HTTPS
        if (!scheme.equals("https")) {
            Log.w(TAG, String.format(
                "Authentication requested for host %s using scheme %s. " +
                    "Authentication is only allowed via HTTPS.",
                host, scheme));
            return null;
        }

        // Only give the credentials to the API domain. Give other domains no
        // authentication.
        if (host.equals(Resources.getSystem().getString(R.string.apiDomain))) {
            return apiAuth;
        } else {
            Log.w(TAG, String.format(
                "Authentication requested for unknown host %s.",
                host));
            return null;
        }
    }

}
