package codeblocks;

import renderable.RenderableBlock;

public class BlockLinkSuggestion {
    /** The drawer name in which the suggested block is contained. */
    private String drawerName;
    /** The suggested block. */
    private RenderableBlock block;
    /** The suggested connector. */
    private BlockConnector connector;

    
    public BlockLinkSuggestion(String drawerName, RenderableBlock block, BlockConnector connector) {
        this.drawerName = drawerName;
        this.block = block;
        this.connector = connector;
    }

    /**
     * @return the drawerName
     */
    public String getDrawerName() {
        return drawerName;
    }

    /**
     * @return the block
     */
    public RenderableBlock getBlock() {
        return block;
    }

    /**
     * @return the connector
     */
    public BlockConnector getConnector() {
        return connector;
    }
}
