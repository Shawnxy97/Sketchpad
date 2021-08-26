package com.xinyingSketchpad;

import java.awt.*;

public class DrawingShapes {
    //Define methods of drawing shapes

    public void handDrawing(int x1, int y1, int x2, int y2, Graphics g){
        g.drawLine(x1, y1, x2, y2);
    }

    public void straightLine(int x1, int y1, int x2, int y2, Graphics g){
        g.drawLine(x1, y1, x2, y2);
    }

    public void rectangle(int x1, int y1, int width, int height, Graphics g){
        g.drawRect(x1, y1, width, height);
    }
}
