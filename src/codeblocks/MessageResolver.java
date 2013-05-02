package codeblocks;

import java.util.ResourceBundle;

public class MessageResolver {
    /** The resource bundle currently in use. */
    private static ResourceBundle bundle;
    
    protected static ResourceBundle getResourceBundle() {
        ResourceBundle rb = bundle;
        if (rb == null)
            rb = bundle = ResourceBundle.getBundle(MessageResolver.class.getPackage().getName() + ".message");
        return rb;
    }
    
    public static String getMessage(String key) {
        ResourceBundle rb = getResourceBundle();
        return rb.containsKey(key)? rb.getString(key) : key;
    }
}
