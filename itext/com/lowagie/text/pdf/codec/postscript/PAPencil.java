/*
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package com.lowagie.text.pdf.codec.postscript;

import java.util.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;


/**
 */
public class PAPencil extends Object {

    /**
     */
    static protected class State extends Object implements Cloneable {
        public Stroke stroke;
        public Paint paint;
        public AffineTransform at;
        public Shape clipShape;
        public Font font;
        public Composite composite;
        public GeneralPath path;

        public State(){
            this(null);
        }

        /**
         * Constructor for State.
         * @param g Graphics2D
         */
        public State(Graphics2D g){
            if(g == null){
                this.stroke = new BasicStroke();
                this.paint = Color.black;
                this.at = new AffineTransform();
                this.font = new Font("SansSerif", Font.PLAIN, 12);
                this.composite = AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1.0f);
                this.clipShape = null;
            } else {
                this.recordState(g);
            }
            this.path = new GeneralPath();
        }

        /**
         * Method recordState.
         * @param g Graphics2D
         */
        public void recordState(Graphics2D g){
            this.stroke = g.getStroke();
            this.paint = g.getPaint();
            this.at = g.getTransform();
            this.font = g.getFont();
            this.composite = g.getComposite();
            this.clipShape = g.getClip();
        }

        /**
         * Method stampState.
         * @param g Graphics2D
         * @param size Dimension
         */
        public void stampState(Graphics2D g, Dimension size){
            g.setTransform(new AffineTransform());
            g.setClip(new Rectangle(0, 0, size.width, size.height));
            g.setStroke(this.stroke);
            g.setPaint(this.paint);
            g.setTransform(this.at);
            g.setFont(this.font);
            g.setComposite(this.composite);
            if(this.clipShape != null){
                g.clip(this.clipShape);
            }
        }

        /**
         * Method clone.
         * @return Object
         */
        public Object clone(){
            try {
                State n = (State)super.clone();

                n.at = (AffineTransform) this.at.clone();
                n.path = new GeneralPath();
                n.path.append(this.path, false);
                return n;
            } catch(CloneNotSupportedException e){
                throw new InternalError();
            }
        }

    }

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    /**
     * The canvas size.
     */
    protected Dimension size;

    /**
     * The current graphics state.
     */
    protected State state;

    /**
     * The stack of graphic states.
     */
    protected Stack gStack;

    /**
     * The font hashtable with postscript names as keys
     */
    protected HashMap fonts;

    /**
     * The current graphics device
     */
    public Graphics2D graphics;

    //
    // Constructors
    //

    /**
     * Constructor for PAPencil.
     * @param component Component
     */
    public PAPencil(Component component){
	this.graphics = (Graphics2D) component.getGraphics();
	this.size = component.getSize();
	this.initgraphics();
    }

    /**
     * Constructor for PAPencil.
     * @param graphics Graphics
     * @param size Dimension
     */
    public PAPencil(Graphics graphics, Dimension size){
	this.graphics = (Graphics2D) graphics;
	this.size = size;
	this.initgraphics();
    }

    //
    // Graphics state management
    //

    public void gsave(){
        this.state.recordState(this.graphics);
	State next = (State) this.state.clone();

	this.gStack.push(this.state);
	this.state = next;
    }

    public void grestore(){
       	if(this.gStack.empty()){
	    this.initgraphics();
	} else {
	    this.state = (State) this.gStack.pop();
            this.state.stampState(this.graphics, this.size);
	}
    }

    public void grestoreall(){
	this.initgraphics();
    }

    public void initgraphics(){
       // AffineTransform at = new AffineTransform();

        // turn anti-aliasing and high-quality rendering on
	this.graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
	this.graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

        // initialize to a postscript coordinate system
//        at.translate(0, this.size.getHeight());
//        at.scale(1, -1);
//        this.graphics.setTransform(at);
        this.graphics.translate(0, this.size.getHeight());
        this.graphics.scale(1, -1);

        // state, stack and page
	this.state = new State(this.graphics);
	this.gStack = new Stack();
	this.erasepage();
    }

    //
    // Path definition
    //

    public void newpath(){
	this.state.path.reset();
    }

    /**
     * Method moveto.
     * @param x double
     * @param y double
     */
    public void moveto(double x, double y){
	this.state.path.moveTo((float) x, (float) y);
    }

    /**
     * Method moveto.
     * @param p Point2D
     */
    public void moveto(Point2D p) {
	this.moveto(p.getX(), p.getY());
    }

    /**
     * Method rmoveto.
     * @param dx double
     * @param dy double
     * @throws PainterException
     */
    public void rmoveto(double dx, double dy) throws PainterException {
        Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
	this.state.path.moveTo((float) (currentPoint.getX() + dx) , (float) (currentPoint.getY() + dy));
    }

    /**
     * Method lineto.
     * @param x double
     * @param y double
     * @throws PainterException
     */
    public void lineto(double x, double y) throws PainterException {
	Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
	this.state.path.lineTo((float) x, (float) y);
    }

    /**
     * Method lineto.
     * @param p Point2D
     * @throws PainterException
     */
    public void lineto(Point2D p) throws PainterException {
	this.lineto(p.getX(), p.getY());
    }

    /**
     * Method rlineto.
     * @param dx double
     * @param dy double
     * @throws PainterException
     */
    public void rlineto(double dx, double dy) throws PainterException {
        Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
        this.state.path.lineTo((float) (currentPoint.getX() + dx) , (float) (currentPoint.getY() + dy));
    }

    /**
     * Method arc.
     * @param cx double
     * @param cy double
     * @param r double
     * @param ang1 double
     * @param ang2 double
     */
    public void arc(double cx, double cy, double r, double ang1, double ang2){
        Arc2D.Float arc = new Arc2D.Float((float) cx, (float) cy, (float) r, (float) r,
                                          (float) ang1, (float) (ang2 - ang1), Arc2D.OPEN);
        Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
            this.state.path.append(arc, false);
        } else {
            this.state.path.append(arc, true);
        }
    }

    /**
     * Method arcn.
     * @param cx double
     * @param cy double
     * @param r double
     * @param ang1 double
     * @param ang2 double
     */
    public void arcn(double cx, double cy, double r,
    		      double ang1, double ang2) {
	Arc2D.Float arc = new Arc2D.Float((float) cx, (float) cy, (float) r, (float) r,
                                          (float) ang1, (float) (ang1 - ang2), Arc2D.OPEN);
       Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
            this.state.path.append(arc, false);
        } else {
            this.state.path.append(arc, true);
        }
    }

    /**
     * Method curveto.
     * @param x1 double
     * @param y1 double
     * @param x2 double
     * @param y2 double
     * @param x3 double
     * @param y3 double
     * @throws PainterException
     */
    public void curveto(double x1, double y1, double x2, double y2,
		         double x3, double y3) throws PainterException {
        Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
        this.state.path.curveTo((float) x1, (float) y1, (float) x2, (float) y2,
                                  (float) x3, (float) y3);
    }

    /**
     * Method rcurveto.
     * @param dx1 double
     * @param dy1 double
     * @param dx2 double
     * @param dy2 double
     * @param dx3 double
     * @param dy3 double
     * @throws PainterException
     */
    public void rcurveto(double dx1, double dy1, double dx2, double dy2,
			  double dx3, double dy3) throws PainterException {
        Point2D currentPoint = this.state.path.getCurrentPoint();

	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
	double x0 = currentPoint.getX();
	double y0 = currentPoint.getY();
	this.curveto(x0 + dx1, y0 + dy1, x0 + dx2, y0 + dy2, x0 + dx3,y0 + dy3);
    }

    public void closepath(){
        this.state.path.closePath();
    }

    // PENDING(uweh): just a placeholder for now
    public void clippath(){
        this.rectpath(0.0d, 0.0d, 800.0d, 800.0d);
    }

    public void erasepage(){
	this.graphics.clearRect(0, 0, 800, 800);
    }

    /**
     * Method charpath.
     * @param aString String
     * @param adjustForStroking boolean
     */
    public void charpath(String aString, boolean adjustForStroking){
	GlyphVector glyphVector = this.state.font.createGlyphVector(this.graphics.getFontRenderContext(), aString);

        Shape glyphShape = glyphVector.getOutline();
        this.state.path.append(glyphShape, false);
    }

    public void showpage(){

    }

    /**
     * Method show.
     * @param string String
     * @throws PainterException
     */
    public void show(String string) throws PainterException {
        Point2D currentPoint = this.state.path.getCurrentPoint();
        AffineTransform currentTransform = this.graphics.getTransform();
        Point2D tranformedPoint = currentTransform.transform(currentPoint, null);


	if(currentPoint == null){
	    throw new PainterException("no current point");
	}
        this.graphics.setTransform(new AffineTransform());
        this.graphics.drawString(string, (float) tranformedPoint.getX(), (float) tranformedPoint.getY());
        this.graphics.setTransform(currentTransform);
    }

    public void fill(){
        this.graphics.fill(this.state.path);
        this.newpath();
    }

    public void eofill(){
        this.state.path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
        this.graphics.fill(this.state.path);
        this.state.path.setWindingRule(GeneralPath.WIND_NON_ZERO);
        this.newpath();
    }

    public void stroke(){
        this.graphics.draw(this.state.path);
        this.newpath();
    }

    /**
     * Method rectfill.
     * @param x double
     * @param y double
     * @param width double
     * @param height double
     */
    public void rectfill(double x, double y, double width, double height){
	this.gsave();
	this.rectpath(x, y, width, height);
	this.fill();
	this.grestore();
    }

    /**
     * Method rectfill.
     * @param rect Rectangle2D
     */
    public void rectfill(Rectangle2D rect){
        this.rectfill(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    /**
     * Method rectstroke.
     * @param x double
     * @param y double
     * @param width double
     * @param height double
     */
    public void rectstroke(double x, double y, double width, double height){
	this.gsave();
	this.rectpath(x, y, width, height);
	this.stroke();
	this.grestore();
    }

    /**
     * Method rectstroke.
     * @param rect Rectangle2D
     */
    public void rectstroke(Rectangle2D rect){
	this.rectstroke(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    /**
     * Method rectpath.
     * @param x double
     * @param y double
     * @param width double
     * @param height double
     */
    public void rectpath(double x, double y, double width, double height){
	this.newpath();
	this.moveto(x, y);
	try {
	    this.rlineto(width, 0);
	    this.rlineto(0, height);
	    this.rlineto(-width, 0);
	} catch(PainterException e){
	}
	this.closepath();
    }

    // convenience

    // this guy tries to find an appropiate font
    // if he fails returns whatever font he wants
    /**
     * Method findFont.
     * @param fontname String
     * @return Font
     */
    public Font findFont(String fontname){
        Font result;
        StringBuffer buffer = new StringBuffer(fontname);
        int i, n;
        n = buffer.length();

        for(i = 0; i < n; i++){
            if(buffer.charAt(i) == '-'){
                buffer.setCharAt(i,' ');
            }
        }

        fontname = buffer.toString();

        if(this.fonts == null){
            // construct the fonts dictionary
            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font[] fontArray = genv.getAllFonts();
            this.fonts = new HashMap();
            for(i = 0; i < fontArray.length; i++){
                String postscriptName = fontArray[i].getPSName();
                this.fonts.put(postscriptName, fontArray[i]);
            }
        }
        result = (Font) this.fonts.get(fontname);
        if(result == null){
            result = new Font("SansSerif", Font.PLAIN, 12);
        }
        return result;
    }
}

