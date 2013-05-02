package codeblocks;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ExceptionRule implements LinkRule {
    
    /** The mapping between genuses source/target and their error messages. */
    private static final Map<RuleKey, String> rules = new HashMap<RuleKey, String>();
    
    /** The last link error. */
    private static String lastError;
    
    
    static class RuleKey {
        String source;
        String target;
        
        @Override
        public int hashCode() {
            return source.hashCode() ^ target.hashCode();
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof RuleKey) {
                RuleKey key = (RuleKey) obj;
                return source.equals(key.source) && target.equals(key.target);
            }
            return false;
        }
    }
    
    private static final RuleKey sharedKey = new RuleKey();
    
    /**
     * Constructs a new link exception rule with exceptions loaded
     * from the given language definition root.
     * 
     * @param root the language definition root element
     */
    public ExceptionRule(Element root) {
        NodeList nodeList = root.getElementsByTagName("LinkExceptions");
        int count = nodeList.getLength();
        for (int i = 0; i < count; i++) {
            Element node = (Element) nodeList.item(i);
            
            NodeList exceptions = node.getElementsByTagName("LinkException");
            for (int j = 0, k = exceptions.getLength(); j < k; j++) {
                Element exception = (Element) exceptions.item(j);
                RuleKey key = new RuleKey();
                key.source = exception.getAttribute("source");
                key.target = exception.getAttribute("target");
                
                // skip if link exception rule is incomplete
                if (key.source == null || key.target == null)
                    continue;
                
                String error = exception.getAttribute("error");
                if (error == null)
                    error = "";
                rules.put(key, error);
            }
        }
    }

    @Override
    public boolean canLink(Block block1, Block block2, BlockConnector socket1, BlockConnector socket2) {
        return canLink(block1.getGenusName(), block2.getGenusName());
    }
    
    protected boolean canLink(String block1, String block2) {
        String error1, error2;
        synchronized (sharedKey) {
            sharedKey.source = block1;
            sharedKey.target = block2;
            error1 = rules.get(sharedKey);
            
            sharedKey.source = block2;
            sharedKey.target = block1;
            error2 = rules.get(sharedKey);
        }
        
        if (error1 != null) {
            lastError = error1;
            return false;
        } else if (error2 != null) {
            lastError = error2;
            return false;
        }
        return true;
    }

    @Override
    public boolean isMandatory() {
        return true;
    }

    /**
     * @return the lastError
     */
    public static String getLastError() {
        return lastError;
    }

    /**
     * @param lastError the lastError to set
     */
    public static void setLastError(String lastError) {
        ExceptionRule.lastError = lastError;
    }

}
