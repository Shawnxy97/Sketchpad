package com.xinyingSketchpad;

import java.awt.*;

public class MyConvas extends Canvas {
    private Image image = null;

    public void setImage(Image image){
        this.image = image;
    }

    @Override
    //paint 是绘制图片到组件上
    //drawimage是将image(内存中画好的图片)一次性画到组件上
    public void paint(Graphics g) {
        g.drawImage(image, 0,0,null);

    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}





