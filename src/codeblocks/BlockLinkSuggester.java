package codeblocks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import renderable.RenderableBlock;

import workspace.FactoryManager;
import workspace.Workspace;

public class BlockLinkSuggester {

    public static List<BlockLinkSuggestion> suggest(Block block, BlockConnector socket) {
        FactoryManager factory = Workspace.getInstance().getFactoryManager();
        
        List<BlockLinkSuggestion> result = new ArrayList<BlockLinkSuggestion>();

        // iterate through each static drawer
        for (String drawerName : factory.getStaticDrawers()) {
            // iterate through each static drawer block
            for (RenderableBlock r : factory.getStaticBlocks(drawerName)) {
                Block b = r.getBlock();
                
                // check if plug connector is compatible
                if (b.getPlug() != null && BlockLinkChecker.canLink(block, b, socket, b.getPlug()) != null)
                    result.add(new BlockLinkSuggestion(drawerName, r, b.getPlug()));
                else if (b.getBeforeConnector() != null && BlockLinkChecker.canLink(block, b, socket, b.getBeforeConnector()) != null)
                    result.add(new BlockLinkSuggestion(drawerName, r, b.getBeforeConnector()));
            }
        }
        return result;
    }
}
