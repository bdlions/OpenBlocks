package renderable;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import codeblocks.BlockConnector;

import workspace.WorkspaceWidget;

/**
 * A subclass of factory renderable block displayed in a link suggestion popup menus.
 * 
 * @author Mariusz Bernacki
 */
public class LinkFactoryRenderableBlock extends FactoryRenderableBlock {
    /** Serial version UID */
    private static final long serialVersionUID = -4940885468332655836L;
    
    private Point socketLocationOnScreen;
    
    private JPopupMenu popupMenu;
    
    private BlockConnector plug;

    
    public LinkFactoryRenderableBlock(WorkspaceWidget widget, Point socketLocationOnScreen, Long blockID, BlockConnector plug) {
        super(widget, blockID);
        this.socketLocationOnScreen = socketLocationOnScreen;
        this.plug = plug;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        //translate this e to a MouseEvent for createdRB
        MouseEvent newE = SwingUtilities.convertMouseEvent(this, e, createdRB);
        createdRB.mouseDragged(newE);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
    }
    
    @Override
    protected Point getInitialLocation() {
        Point p = getLocationOnScreen();
        p.translate(-socketLocationOnScreen.x, -socketLocationOnScreen.y);
        p.x = -p.x;
        p.y = -p.y;
        p.translate(getX(), getY());
        
        if (plugTag != null && plugTag.getSocket() == plug)
            p.translate(-plugTag.getPixelLocation().x, -plugTag.getPixelLocation().y);
        else if (beforeTag != null && beforeTag.getSocket() == plug)
            p.translate(-beforeTag.getPixelLocation().x, -beforeTag.getPixelLocation().y);
        
        return p;
    }
    
    public void mouseReleased(MouseEvent e) {
        if (createdRB != null){
            //translate this e to a MouseEvent for createdRB
            MouseEvent newE = SwingUtilities.convertMouseEvent(this, e, createdRB);
            createdRB.mouseReleased(newE);
            createdRB = null;
            if (popupMenu != null)
                popupMenu.setVisible(false);
        }
    }

    /**
     * @return the popupMenu
     */
    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    /**
     * @param popupMenu the popupMenu to set
     */
    public void setPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }
}
