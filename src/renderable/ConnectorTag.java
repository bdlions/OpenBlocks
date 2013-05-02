package renderable;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

import codeblocks.BlockConnector;

class ConnectorTag{
	private double zoom = 1.0;
	private SocketLabel label;
	private Point aLoc;
    private Point eLoc;
	private BlockConnector connector;
	private Dimension dimension;
	ConnectorTag(BlockConnector connector){
		aLoc = new Point(0,0);
		this.dimension = null;
		this.connector = connector;
	}
	void setZoomLevel(double newZoom){
		zoom = newZoom;
		if(label != null){
			label.setZoomLevel(newZoom);
		}
	}
	private int rescale(int x){
		return (int)(x*zoom);
	}
	BlockConnector getSocket(){
		return connector;
	}
   void setSocket(BlockConnector conn) {
        this.connector = conn;
    }
	void setLabel(SocketLabel label){
		this.label = label;
	}
	SocketLabel getLabel(){
		return this.label;
	}
	void setDimension(Dimension dimension){
		this.dimension=dimension;
	}
	Dimension getDimension(){
		if(dimension == null){
			return null;
		}else{
			return new Dimension((int)(dimension.width/zoom), (int)(dimension.height/zoom));
		}
		//return this.dimension;
	}
	void setAbstractLocation(Point2D p) {
	    setAbstractLocation(p, null);
	}
    void setAbstractLocation(Point2D p, Point2D r) {
        //we can't do aLoc=loc because then we can mutate aLoc by mutating loc
        aLoc = new Point((int)p.getX(), (int)p.getY());
        if (r != null)
            eLoc = new Point((int)r.getX(), (int)r.getY());
    }
	Point getAbstractLocation() {
		//we can't do aLoc=loc because then we can mutate aLoc by mutating loc
		return aLoc.getLocation();
	}
	Point getPixelLocation() {
		return new Point(rescale(aLoc.x), rescale(aLoc.y));
	}
    Point getErrorLocation() {
        if (eLoc != null)
            return new Point(rescale(eLoc.x), rescale(eLoc.y));
        return getPixelLocation();
    }
}
