package com.xinyingSketchpad;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyFrame extends JFrame {
    //two points to specify shape position
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 0;
    private int y2 = 0;

    //current selected shape
    private String state = "";

    //undo & redo attributes
    private String last_state = "";
    private ArrayList<String> progress_stack = new ArrayList<String>();
    private ArrayList<String> progress_stack_bkup = new ArrayList<String>();

    //store shape points
    private ArrayList<Integer> rec_points = new ArrayList<Integer>();
    private ArrayList<Color> rec_colors = new ArrayList<Color>();
    private ArrayList<Integer> rec_points_bkup = new ArrayList<Integer>();
    private ArrayList<Color> rec_colors_bkup = new ArrayList<Color>();

    private ArrayList<Integer> cir_points = new ArrayList<Integer>();
    private ArrayList<Color> cir_colors = new ArrayList<Color>();
    private ArrayList<Integer> cir_points_bkup = new ArrayList<Integer>();
    private ArrayList<Color> cir_colors_bkup = new ArrayList<Color>();

    private ArrayList<Integer> squ_points = new ArrayList<Integer>();
    private ArrayList<Color> squ_colors = new ArrayList<Color>();
    private ArrayList<Integer> squ_points_bkup = new ArrayList<Integer>();
    private ArrayList<Color> squ_colors_bkup = new ArrayList<Color>();

    private ArrayList<Integer> ell_points = new ArrayList<Integer>();
    private ArrayList<Color> ell_colors = new ArrayList<Color>();
    private ArrayList<Integer> ell_points_bkup = new ArrayList<Integer>();
    private ArrayList<Color> ell_colors_bkup = new ArrayList<Color>();

    private ArrayList<Integer> poly_points = new ArrayList<Integer>();
    private ArrayList<Color> poly_colors = new ArrayList<Color>();
    private ArrayList<Integer> poly_points_bkup = new ArrayList<Integer>();
    private ArrayList<Color> poly_colors_bkup = new ArrayList<Color>();

    private ArrayList<Integer> str_points = new ArrayList<Integer>();
    private ArrayList<Color> str_colors = new ArrayList<Color>();
    private ArrayList<Integer> str_points_bkup = new ArrayList<Integer>();
    private ArrayList<Color> str_colors_bkup = new ArrayList<Color>();

    private ArrayList<Integer> handdraw_points = new ArrayList<Integer>();
    private ArrayList<Color> handdraw_colors = new ArrayList<Color>();
    private ArrayList<Integer> handdraw_points_bkup = new ArrayList<Integer>();
    private ArrayList<Color> handdraw_colors_bkup = new ArrayList<Color>();

    private ArrayList<Integer> selected_object = new ArrayList<Integer>();
    private String selected_shape="";

    private ArrayList<Integer> popped_points = new ArrayList<Integer>();
    private String popped_shape = "";
    private Color popped_color;

    private int selected_element_index;





    JFrame main_frame = new JFrame();
    BufferedImage image = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_BGR);
    BufferedImage image_save = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_BGR);
//    Graphics g = image.getGraphics();
    Graphics gr = image.getGraphics();
    Graphics2D g = (Graphics2D) gr;

    MyConvas canvas = new MyConvas();
    Color pen_color = Color.BLACK;




    public MyFrame(){
        main_frame.setTitle("SketchPad");
        main_frame.setBounds(400,100,1200,800);
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BasicStroke s = new BasicStroke(3);
        g.setStroke(s);

        //design menu panel
        JPanel p_menu = new JPanel();
        p_menu.setLayout(new GridLayout(1,7,3,3));

        //design operation panel
        JPanel p_operation = new JPanel();
        p_operation.setLayout(new GridLayout(10, 1, 3,3));

        //paint the canvas area white
        g.setColor(Color.WHITE);
        g.fillRect(0,0,1200,800);
        canvas.setImage(image);

        //functional buttons on panel
        //shapes
        JButton rec = new JButton("rectangle");
        JButton str = new JButton("straight");
        JButton ell = new JButton("ellipses ");
        JButton cir = new JButton("circle");
        JButton squ = new JButton("square ");
        JButton poly = new JButton("polygons");
        JButton handdraw = new JButton("freehand");

        JButton col = new JButton("color");
        JButton clear = new JButton("clear ");
        JButton save = new JButton("save");
        JButton reload = new JButton("reload image");
        JButton load = new JButton("load history");

        JButton undo = new JButton("undo");
        JButton redo = new JButton("redo");

        JButton select = new JButton("select");
        JButton cut = new JButton("cut");
        JButton move = new JButton("move");
        JButton copy = new JButton("copy");
        JButton paste = new JButton("paste");

        JLabel instruction = new JLabel();

        instruction.setFont(new Font("", Font.BOLD, 25));




        rec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "rec";
            }
        });

        squ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "squ";
            }
        });

        str.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "str";
            }
        });

        handdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "handdraw";
            }
        });

        ell.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "ell";
            }
        });

        cir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "cir";
            }
        });

        poly.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "poly";
                x1 = -1;
                y1 = -1;

                //insert a negative number to separate shapes
                poly_points.add(-1);
//                System.out.println(poly_colors+" is poly colors stack");
//                System.out.println(progress_stack+ "+++++++++");
            }
        });

        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //open a filedialog
                FileDialog fileDialog = new FileDialog(main_frame, "reload an image", FileDialog.LOAD);
                fileDialog.setVisible(true);

                //get directory & image name
                String dir = fileDialog.getDirectory();
                String fileName = fileDialog.getFile();
                System.out.println(dir);
                System.out.println(fileName);

                try {
                    image_save = ImageIO.read(new File(dir, fileName));


                    canvas.setImage(image_save);

                    canvas.repaint();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //repaint all shapes in the load_stack
                //repaint rec
                progress_stack = progress_stack_bkup;

                rec_points = rec_points_bkup;
                rec_colors = rec_colors_bkup;

                squ_points = squ_points_bkup;
                squ_colors = squ_colors_bkup;

                ell_points = ell_points_bkup;
                ell_colors = ell_colors_bkup;

                cir_points = cir_points_bkup;
                cir_colors = cir_colors_bkup;

                str_points = str_points_bkup;
                str_colors = str_colors_bkup;

                poly_points = poly_points_bkup;
                poly_colors = poly_colors_bkup;

                handdraw_points = handdraw_points_bkup;
                handdraw_colors = handdraw_colors_bkup;


                if(rec_colors.size()>0){
                    repaint_shapes("rec", rec_colors, rec_points);
                }

                //repaint circle
                if(cir_colors.size()>0){
                    repaint_shapes("cir", cir_colors, cir_points);
                }

                //repaint square
                if(squ_colors.size()>0){
                    repaint_shapes("squ", squ_colors, squ_points);
                }

                //repaint ellipse
                if(ell_colors.size()>0){
                    repaint_shapes("ell", ell_colors, ell_points);
                }

                //repaint straight line
                if(str_colors.size()>0){
                    repaint_shapes("str", str_colors, str_points);
                }

                //repaint polygons
                if(poly_colors.size()>0){
                    //polygon repaint
                    ArrayList<Integer> separate_index = get_separate_index(poly_points);
                    ArrayList<Integer> points = new ArrayList<Integer>();

                    //other shapes
                    for (int j = 0; j < separate_index.size()-1; j++) {
                        Color polygon_color = poly_colors.get(j);
                        //each shape repaint
                        int begin_index = separate_index.get(j);
                        int end_index = separate_index.get(j+1);

                        //get polygon points arraylist
                        for (int k = (begin_index+1); k < end_index; k++) {
                            points.add(poly_points.get(k));

                        }
                        System.out.println(points);

                        //repaint one polygon
                        int line_num = points.size()/2 - 1 ;
                        for (int k = 0; k < line_num; k++) {
                            int begin_point = k*2;
                            x1 = points.get(begin_point);
                            y1 = points.get(begin_point + 1);
                            x2 = points.get(begin_point + 2);
                            y2 = points.get(begin_point + 3);

                            g.setColor(polygon_color);
                            g.drawLine(x1, y1, x2, y2);

                            canvas.repaint();
                        }
                        points.clear();
                    }

                    //the last polygon
                    //get last shape points arraylist
                    int index = separate_index.get(separate_index.size()-1);
                    for (int j = index+1; j < poly_points.size(); j++) {
                        points.add(poly_points.get(j));
                    }
                    System.out.println(points);
                    //using points to repaint polygon
                    int line_num = points.size()/2 - 1 ;
                    for (int j = 0; j < line_num; j++) {
                        int begin_point = j*2;
                        x1 = points.get(begin_point);
                        y1 = points.get(begin_point + 1);
                        x2 = points.get(begin_point + 2);
                        y2 = points.get(begin_point + 3);

                        g.setColor(poly_colors.get(poly_colors.size()-1));
                        g.drawLine(x1, y1, x2, y2);

                        canvas.repaint();

                    }
                }

                //repaint freehand lines
                if(handdraw_colors.size()>0){
                    //repaint all handdraw
                    ArrayList<Integer> separate_index = get_separate_index(handdraw_points);
                    ArrayList<Integer> points = new ArrayList<Integer>();
                    for (int i = 0; i < handdraw_colors.size(); i++) {
                        //each handdraw
                        //take all points in one draw
                        if(i == 0){
                            //first draw
                            int begin_index = 0;
                            int end_index = separate_index.get(i);
                            //get all points in first drawing
                            for (int j = begin_index; j < end_index; j++) {
                                points.add(handdraw_points.get(j));
                            }
                            //repaint first drawing
                            System.out.println(points);

                            int line_num = points.size()/2 - 1;
                            for (int j = 0; j < line_num; j++) {
                                //draw each short line
                                begin_index = j*2;

                                x1 = points.get(begin_index);
                                y1 = points.get(begin_index + 1);
                                x2 = points.get(begin_index + 2);
                                y2 = points.get(begin_index + 3);

                                g.setColor(handdraw_colors.get(i));
                                g.drawLine(x1, y1, x2, y2);

                                canvas.repaint();

                            }
                            points.clear();

                        }else{
                            //draw other handdraws
                            for (int j = 0; j < handdraw_colors.size()-1; j++) {
                                //draw each drawing

                                int begin_index = separate_index.get(j)+1;
                                int end_index = separate_index.get(j+1);
                                //get all points in one drawing
                                for (int k = begin_index; k < end_index; k++) {
                                    points.add(handdraw_points.get(k));
                                }
                                System.out.println(points);

                                //repaint drawing
                                int line_num = points.size()/2 - 1;
                                for (int k = 0; k < line_num; k++) {
                                    //each line
                                    begin_index = k*2;

                                    x1 = points.get(begin_index);
                                    y1 = points.get(begin_index + 1);
                                    x2 = points.get(begin_index + 2);
                                    y2 = points.get(begin_index + 3);

                                    g.setColor(handdraw_colors.get(i));
                                    g.drawLine(x1, y1, x2, y2);

                                    canvas.repaint();

                                }

                                points.clear();

                            }


                        }

                    }

                }

            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                instruction.setText("Save as the JPEG format eg: xxx.jpg");
                FileDialog fileDialog = new FileDialog(main_frame, "save image", FileDialog.SAVE);
                fileDialog.setVisible(true);

                //get directory & file name
                String dir = fileDialog.getDirectory();
                String fileName = fileDialog.getFile();

                System.out.println(dir);
                System.out.println(fileName);

                try {
                    File file = new File(dir, fileName);
                    ImageIO.write(image, "JPEG",file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setImage(image);
                g.setColor(Color.WHITE);
                g.fillRect(0,0,1200,800);
                canvas.repaint();

                //store & clear progress stack
                for (int i = 0; i < progress_stack.size(); i++) {
                    progress_stack_bkup.add(progress_stack.get(i));
                }
                progress_stack.clear();

                //store & clear shape data
                //rectangle
                for (int i = 0; i < rec_points.size(); i++) {
                    rec_points_bkup.add(rec_points.get(i));
                }
                rec_points.clear();
                for (int i = 0; i < rec_colors.size(); i++) {
                    rec_colors_bkup.add(rec_colors.get(i));
                }
                rec_colors.clear();

                //square
                for (int i = 0; i < squ_points.size(); i++) {
                    squ_points_bkup.add(squ_points.get(i));
                }
                squ_points.clear();
                for (int i = 0; i < squ_colors.size(); i++) {
                    squ_colors_bkup.add(squ_colors.get(i));
                }
                squ_colors.clear();

                //ellipse
                for (int i = 0; i < ell_points.size(); i++) {
                    ell_points_bkup.add(ell_points.get(i));
                }
                ell_points.clear();
                for (int i = 0; i < ell_colors.size(); i++) {
                    ell_colors_bkup.add(ell_colors.get(i));
                }
                ell_colors.clear();

                //circle
                for (int i = 0; i < cir_points.size(); i++) {
                    cir_points_bkup.add(cir_points.get(i));
                }
                cir_points.clear();
                for (int i = 0; i < cir_colors.size(); i++) {
                    cir_colors_bkup.add(cir_colors.get(i));
                }
                cir_colors.clear();

                //straight line
                for (int i = 0; i < str_points.size(); i++) {
                    str_points_bkup.add(str_points.get(i));
                }
                str_points.clear();
                for (int i = 0; i < str_colors.size(); i++) {
                    str_colors_bkup.add(str_colors.get(i));
                }
                str_colors.clear();

                //polygon
                for (int i = 0; i < poly_points.size(); i++) {
                    poly_points_bkup.add(poly_points.get(i));
                }
                poly_points.clear();
                for (int i = 0; i < poly_colors.size(); i++) {
                    poly_colors_bkup.add(poly_colors.get(i));
                }
                poly_colors.clear();

                //freehand
                for (int i = 0; i < handdraw_points.size(); i++) {
                    handdraw_points_bkup.add(handdraw_points.get(i));
                }
                handdraw_points.clear();
                for (int i = 0; i < handdraw_colors.size(); i++) {
                    handdraw_colors_bkup.add(handdraw_colors.get(i));
                }
                handdraw_colors.clear();

            }
        });

        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //repaint all shapes store in progress_stack except the last one
                //recall all points in each shape
                // 4 points as an object
                //loop and repaint
                g.setColor(Color.WHITE);
                g.fillRect(0,0,1200,800);
                canvas.repaint();

                popped_points.clear();

                if(progress_stack.size() > 0){
                    last_state = progress_stack.get(progress_stack.size()-1);
                    System.out.println(last_state+" is the last shape in progress stack");


                    if(last_state.equals("rec")){
                        //recall other point_stack
                        if(cir_colors.size()>0){
                            repaint_shapes("cir", cir_colors, cir_points);
                        }

                        if(squ_colors.size()>0){
                            //square repaint
                            repaint_shapes("squ", squ_colors, squ_points);
                        }

                        if(ell_colors.size()>0){
                            //ellipse repaint
                            repaint_shapes("ell", ell_colors, ell_points);
                        }

                        if(str_colors.size()>0){
                            //straight line repaint
                            repaint_shapes("str", str_colors, str_points);
                        }
                        if(poly_colors.size()>0){
                            //polygon repaint
                            ArrayList<Integer> separate_index = get_separate_index(poly_points);
                            ArrayList<Integer> points = new ArrayList<>();

                            //other shapes
                            for (int j = 0; j < separate_index.size()-1; j++) {
                                Color polygon_color = poly_colors.get(j);
                                //each shape repaint
                                int begin_index = separate_index.get(j);
                                int end_index = separate_index.get(j+1);

                                //get polygon points arraylist
                                for (int k = (begin_index+1); k < end_index; k++) {
                                    points.add(poly_points.get(k));

                                }
                                System.out.println(points);

                                //repaint one polygon
                                int line_num = points.size()/2 - 1 ;
                                for (int k = 0; k < line_num; k++) {
                                    int begin_point = k*2;
                                    x1 = points.get(begin_point);
                                    y1 = points.get(begin_point + 1);
                                    x2 = points.get(begin_point + 2);
                                    y2 = points.get(begin_point + 3);

                                    g.setColor(polygon_color);
                                    g.drawLine(x1, y1, x2, y2);

                                    canvas.repaint();
                                }
                                points.clear();
                            }

                            //the last polygon
                            //get last shape points arraylist
                            int index = separate_index.get(separate_index.size()-1);
                            for (int j = index+1; j < poly_points.size(); j++) {
                                points.add(poly_points.get(j));
                            }
                            System.out.println(points);
                            //using points to repaint polygon
                            int line_num = points.size()/2 - 1 ;
                            for (int j = 0; j < line_num; j++) {
                                int begin_point = j*2;
                                x1 = points.get(begin_point);
                                y1 = points.get(begin_point + 1);
                                x2 = points.get(begin_point + 2);
                                y2 = points.get(begin_point + 3);

                                g.setColor(poly_colors.get(poly_colors.size()-1));
                                g.drawLine(x1, y1, x2, y2);

                                canvas.repaint();

                            }



                        }
                        if(handdraw_colors.size()>0){
                            //repaint all handdraw
                            ArrayList<Integer> separate_index = get_separate_index(handdraw_points);
                            ArrayList<Integer> points = new ArrayList<Integer>();
                            for (int i = 0; i < handdraw_colors.size(); i++) {
                                //each handdraw
                                //take all points in one draw
                                if(i == 0){
                                    //first draw
                                    int begin_index = 0;
                                    int end_index = separate_index.get(i);
                                    //get all points in first drawing
                                    for (int j = begin_index; j < end_index; j++) {
                                        points.add(handdraw_points.get(j));
                                    }
                                    //repaint first drawing
                                    System.out.println(points);

                                    int line_num = points.size()/2 - 1;
                                    for (int j = 0; j < line_num; j++) {
                                        //draw each short line
                                        begin_index = j*2;

                                        x1 = points.get(begin_index);
                                        y1 = points.get(begin_index + 1);
                                        x2 = points.get(begin_index + 2);
                                        y2 = points.get(begin_index + 3);

                                        g.setColor(handdraw_colors.get(i));
                                        g.drawLine(x1, y1, x2, y2);

                                        canvas.repaint();

                                    }
                                    points.clear();

                                }else{
                                    //draw other handdraws
                                    for (int j = 0; j < handdraw_colors.size()-1; j++) {
                                        //draw each drawing

                                        int begin_index = separate_index.get(j)+1;
                                        int end_index = separate_index.get(j+1);
                                        //get all points in one drawing
                                        for (int k = begin_index; k < end_index; k++) {
                                            points.add(handdraw_points.get(k));
                                        }
                                        System.out.println(points);

                                        //repaint drawing
                                        int line_num = points.size()/2 - 1;
                                        for (int k = 0; k < line_num; k++) {
                                            //each line
                                            begin_index = k*2;

                                            x1 = points.get(begin_index);
                                            y1 = points.get(begin_index + 1);
                                            x2 = points.get(begin_index + 2);
                                            y2 = points.get(begin_index + 3);

                                            g.setColor(handdraw_colors.get(i));
                                            g.drawLine(x1, y1, x2, y2);

                                            canvas.repaint();

                                        }

                                        points.clear();

                                    }


                                }

                            }

                        }

                        regular_shape_undo_process("rec", rec_points, rec_colors);

                  /*  //recall rec points
                    //1. store the last rectangle data
                    //2. pop the last rectangle

                    //for loop
                    for (int i = 0; i < rec_colors.size(); i++) {
                        System.out.println(rec_colors.size());
                        int begin_index = i * 4;
                        x1 = rec_points.get(begin_index);
                        y1 = rec_points.get(begin_index + 1);
                        x2 = rec_points.get(begin_index + 2);
                        y2 = rec_points.get(begin_index + 3);
                        int[] start_points = start_point(x1, y1, x2, y2);
                        //last one
                        if(i == (rec_colors.size()-1)){
                            //store points and shape

                            popped_points.add(x1);
                            popped_points.add(y1);
                            popped_points.add(x2);
                            popped_points.add(y2);

                            popped_shape = "rec";
                            popped_color = rec_colors.get(rec_colors.size()-1);
                            System.out.println(popped_points+ " is popped rectangle");

                            //popped the last rectangle
                            System.out.println(progress_stack+" is the previous stack");
                            progress_stack.remove(progress_stack.size()-1);
                            System.out.println(progress_stack+" is the current stack");

                            //pop last 4 points from rec_points

                            if(rec_colors.size() == 1){
                                //last element in shape stack
                                rec_points.clear();
                            }
                            else{
                                //pop last 4 points from cir_points
                                rec_points.remove(rec_points.size()-1);
                                rec_points.remove(rec_points.size()-2);
                                rec_points.remove(rec_points.size()-3);
                                rec_points.remove(rec_points.size()-4);
                                System.out.println(rec_points+ " is the current rec_points");
                            }


                            //pop the last color
                            rec_colors.remove(rec_colors.size()-1);



                        }else{
                            g.setColor(rec_colors.get(i));
                            g.drawRect(start_points[0], start_points[1], Math.abs(x2-x1), Math.abs(y2-y1));

                            canvas.repaint();
                        }

                    }*/

//                    if(rec_points.size() < 5){
//                        //it is the first shape
//                        //do nothing don't paint anything on canvas
//                        //but store the
//                    }else{
//                        for (int i = 0; i < rec_colors.size()-1; i++) {
//                            System.out.println(rec_colors.size());
//                            int begin_index = i * 4;
//                            x1 = rec_points.get(begin_index);
//                            y1 = rec_points.get(begin_index + 1);
//                            x2 = rec_points.get(begin_index + 2);
//                            y2 = rec_points.get(begin_index + 3);
//
//                            int[] start_points = start_point(x1, y1, x2, y2);
//
//                            g.setColor(rec_colors.get(i));
//                            g.drawRect(start_points[0], start_points[1], Math.abs(x2-x1), Math.abs(y2-y1));
//
//                            canvas.repaint();
//                        }
//                    }


                    }

                    if(last_state.equals("cir")){
                        //recall other point_stack
                        if(rec_colors.size()>0){
                            repaint_shapes("rec", rec_colors, rec_points);
                        }

                        if(squ_colors.size()>0){
                            //square repaint
                            repaint_shapes("squ", squ_colors, squ_points);
                        }

                        if(ell_colors.size()>0){
                            //ellipse repaint
                            repaint_shapes("ell", ell_colors, ell_points);
                        }

                        if(str_colors.size()>0){
                            //straight line repaint
                            repaint_shapes("str", str_colors, str_points);
                        }
                        if(poly_colors.size()>0){
                            //polygon repaint
                            ArrayList<Integer> separate_index = get_separate_index(poly_points);
                            ArrayList<Integer> points = new ArrayList<Integer>();

                            //other shapes
                            for (int j = 0; j < separate_index.size()-1; j++) {
                                Color polygon_color = poly_colors.get(j);
                                //each shape repaint
                                int begin_index = separate_index.get(j);
                                int end_index = separate_index.get(j+1);

                                //get polygon points arraylist
                                for (int k = (begin_index+1); k < end_index; k++) {
                                    points.add(poly_points.get(k));

                                }
                                System.out.println(points);

                                //repaint one polygon
                                int line_num = points.size()/2 - 1 ;
                                for (int k = 0; k < line_num; k++) {
                                    int begin_point = k*2;
                                    x1 = points.get(begin_point);
                                    y1 = points.get(begin_point + 1);
                                    x2 = points.get(begin_point + 2);
                                    y2 = points.get(begin_point + 3);

                                    g.setColor(polygon_color);
                                    g.drawLine(x1, y1, x2, y2);

                                    canvas.repaint();
                                }
                                points.clear();
                            }

                            //the last polygon
                            //get last shape points arraylist
                            int index = separate_index.get(separate_index.size()-1);
                            for (int j = index+1; j < poly_points.size(); j++) {
                                points.add(poly_points.get(j));
                            }
                            System.out.println(points);
                            //using points to repaint polygon
                            int line_num = points.size()/2 - 1 ;
                            for (int j = 0; j < line_num; j++) {
                                int begin_point = j*2;
                                x1 = points.get(begin_point);
                                y1 = points.get(begin_point + 1);
                                x2 = points.get(begin_point + 2);
                                y2 = points.get(begin_point + 3);

                                g.setColor(poly_colors.get(poly_colors.size()-1));
                                g.drawLine(x1, y1, x2, y2);

                                canvas.repaint();

                            }

                        }
                        if(handdraw_colors.size()>0){
                            //repaint all handdraw
                            ArrayList<Integer> separate_index = get_separate_index(handdraw_points);
                            ArrayList<Integer> points = new ArrayList<Integer>();
                            for (int i = 0; i < handdraw_colors.size(); i++) {
                                //each handdraw
                                //take all points in one draw
                                if(i == 0){
                                    //first draw
                                    int begin_index = 0;
                                    int end_index = separate_index.get(i);
                                    //get all points in first drawing
                                    for (int j = begin_index; j < end_index; j++) {
                                        points.add(handdraw_points.get(j));
                                    }
                                    //repaint first drawing
                                    System.out.println(points);

                                    int line_num = points.size()/2 - 1;
                                    for (int j = 0; j < line_num; j++) {
                                        //draw each short line
                                        begin_index = j*2;

                                        x1 = points.get(begin_index);
                                        y1 = points.get(begin_index + 1);
                                        x2 = points.get(begin_index + 2);
                                        y2 = points.get(begin_index + 3);

                                        g.setColor(handdraw_colors.get(i));
                                        g.drawLine(x1, y1, x2, y2);

                                        canvas.repaint();

                                    }
                                    points.clear();

                                }else{
                                    //draw other handdraws
                                    for (int j = 0; j < handdraw_colors.size()-1; j++) {
                                        //draw each drawing

                                        int begin_index = separate_index.get(j)+1;
                                        int end_index = separate_index.get(j+1);
                                        //get all points in one drawing
                                        for (int k = begin_index; k < end_index; k++) {
                                            points.add(handdraw_points.get(k));
                                        }
                                        System.out.println(points);

                                        //repaint drawing
                                        int line_num = points.size()/2 - 1;
                                        for (int k = 0; k < line_num; k++) {
                                            //each line
                                            begin_index = k*2;

                                            x1 = points.get(begin_index);
                                            y1 = points.get(begin_index + 1);
                                            x2 = points.get(begin_index + 2);
                                            y2 = points.get(begin_index + 3);

                                            g.setColor(handdraw_colors.get(i));
                                            g.drawLine(x1, y1, x2, y2);

                                            canvas.repaint();

                                        }

                                        points.clear();

                                    }


                                }

                            }

                        }

                        regular_shape_undo_process("cir", cir_points, cir_colors);

                  /*  //recall circle points
                    //1. store the last circle data
                    //2. pop the last circle

                    //for loop
                    for (int i = 0; i < cir_colors.size(); i++) {
                        System.out.println(cir_colors.size());
                        int begin_index = i * 4;
                        x1 = cir_points.get(begin_index);
                        y1 = cir_points.get(begin_index + 1);
                        x2 = cir_points.get(begin_index + 2);
                        y2 = cir_points.get(begin_index + 3);
                        int[] start_points = start_point(x1, y1, x2, y2);
                        //last one
                        if(i == (cir_colors.size()-1)){
                            //store points and shape
                            popped_points.add(x1);
                            popped_points.add(y1);
                            popped_points.add(x2);
                            popped_points.add(y2);

                            popped_color = cir_colors.get(cir_colors.size()-1);

                            popped_shape = "cir";
                            System.out.println(popped_points+ " is popped circle");

                            //popped the last circle
                            System.out.println(progress_stack+" is the previous stack");
                            progress_stack.remove(progress_stack.size()-1);
                            System.out.println(progress_stack+" is the current stack");

                            if(cir_colors.size() == 1){
                                //last element in shape stack
                                cir_points.clear();
                            }
                            else{
                                //pop last 4 points from cir_points
                                System.out.println(cir_points.size()+ " is the previous circle points");
                                cir_points.remove(cir_points.size()-1);
                                cir_points.remove(cir_points.size()-2);
                                cir_points.remove(cir_points.size()-3);
                                cir_points.remove(cir_points.size()-4);
                                System.out.println(cir_points+ " is the current cir_points");
                            }


                            //pop the last color
                            cir_colors.remove(cir_colors.size()-1);
                            System.out.println(cir_colors+" is current color stack");


                        }else{
                            g.setColor(cir_colors.get(i));
                            g.drawOval(start_points[0], start_points[1], Math.abs(x2-x1), Math.abs(x2-x1));

                            canvas.repaint();
                        }

                    }*/

                  /*  //recall cir points
                    if(cir_points.size() < 5){
                        //it is the first shape
                        //do nothing don't paint anything on canvas
                    }else{
                        for (int i = 0; i < cir_colors.size()-1; i++) {
//                            System.out.println(cir_colors.size());
                            int begin_index = i * 4;
                            x1 = cir_points.get(begin_index);
                            y1 = cir_points.get(begin_index + 1);
                            x2 = cir_points.get(begin_index + 2);
                            y2 = cir_points.get(begin_index + 3);

                            int[] start_points = start_point(x1, y1, x2, y2);

                            g.setColor(cir_colors.get(i));
                            g.drawOval(start_points[0], start_points[1], Math.abs(x2-x1), Math.abs(x2-x1));

                            canvas.repaint();
                        }
                    }*/


                    }

                    if(last_state.equals("squ")){
                        //recall other point_stack
                        if(rec_colors.size()>0){
                            repaint_shapes("rec", rec_colors, rec_points);
                        }

                        if(cir_colors.size()>0){
                            //square repaint
                            repaint_shapes("cir", cir_colors, cir_points);
                        }

                        if(ell_colors.size()>0){
                            //ellipse repaint
                            repaint_shapes("ell", ell_colors, ell_points);
                        }

                        if(str_colors.size()>0){
                            //straight line repaint
                            repaint_shapes("str", str_colors, str_points);
                        }
                        if(poly_colors.size()>0){
                            //polygon repaint
                            ArrayList<Integer> separate_index = get_separate_index(poly_points);
                            ArrayList<Integer> points = new ArrayList<Integer>();

                            //other shapes
                            for (int j = 0; j < separate_index.size()-1; j++) {
                                Color polygon_color = poly_colors.get(j);
                                //each shape repaint
                                int begin_index = separate_index.get(j);
                                int end_index = separate_index.get(j+1);

                                //get polygon points arraylist
                                for (int k = (begin_index+1); k < end_index; k++) {
                                    points.add(poly_points.get(k));

                                }
                                System.out.println(points);

                                //repaint one polygon
                                int line_num = points.size()/2 - 1 ;
                                for (int k = 0; k < line_num; k++) {
                                    int begin_point = k*2;
                                    x1 = points.get(begin_point);
                                    y1 = points.get(begin_point + 1);
                                    x2 = points.get(begin_point + 2);
                                    y2 = points.get(begin_point + 3);

                                    g.setColor(polygon_color);
                                    g.drawLine(x1, y1, x2, y2);

                                    canvas.repaint();
                                }
                                points.clear();
                            }

                            //the last polygon
                            //get last shape points arraylist
                            int index = separate_index.get(separate_index.size()-1);
                            for (int j = index+1; j < poly_points.size(); j++) {
                                points.add(poly_points.get(j));
                            }
                            System.out.println(points);
                            //using points to repaint polygon
                            int line_num = points.size()/2 - 1 ;
                            for (int j = 0; j < line_num; j++) {
                                int begin_point = j*2;
                                x1 = points.get(begin_point);
                                y1 = points.get(begin_point + 1);
                                x2 = points.get(begin_point + 2);
                                y2 = points.get(begin_point + 3);

                                g.setColor(poly_colors.get(poly_colors.size()-1));
                                g.drawLine(x1, y1, x2, y2);

                                canvas.repaint();

                            }

                        }
                        if(handdraw_colors.size()>0){
                            //repaint all handdraw
                            ArrayList<Integer> separate_index = get_separate_index(handdraw_points);
                            ArrayList<Integer> points = new ArrayList<Integer>();
                            for (int i = 0; i < handdraw_colors.size(); i++) {
                                //each handdraw
                                //take all points in one draw
                                if(i == 0){
                                    //first draw
                                    int begin_index = 0;
                                    int end_index = separate_index.get(i);
                                    //get all points in first drawing
                                    for (int j = begin_index; j < end_index; j++) {
                                        points.add(handdraw_points.get(j));
                                    }
                                    //repaint first drawing
                                    System.out.println(points);

                                    int line_num = points.size()/2 - 1;
                                    for (int j = 0; j < line_num; j++) {
                                        //draw each short line
                                        begin_index = j*2;

                                        x1 = points.get(begin_index);
                                        y1 = points.get(begin_index + 1);
                                        x2 = points.get(begin_index + 2);
                                        y2 = points.get(begin_index + 3);

                                        g.setColor(handdraw_colors.get(i));
                                        g.drawLine(x1, y1, x2, y2);

                                        canvas.repaint();

                                    }
                                    points.clear();

                                }else{
                                    //draw other handdraws
                                    for (int j = 0; j < handdraw_colors.size()-1; j++) {
                                        //draw each drawing

                                        int begin_index = separate_index.get(j)+1;
                                        int end_index = separate_index.get(j+1);
                                        //get all points in one drawing
                                        for (int k = begin_index; k < end_index; k++) {
                                            points.add(handdraw_points.get(k));
                                        }
                                        System.out.println(points);

                                        //repaint drawing
                                        int line_num = points.size()/2 - 1;
                                        for (int k = 0; k < line_num; k++) {
                                            //each line
                                            begin_index = k*2;

                                            x1 = points.get(begin_index);
                                            y1 = points.get(begin_index + 1);
                                            x2 = points.get(begin_index + 2);
                                            y2 = points.get(begin_index + 3);

                                            g.setColor(handdraw_colors.get(i));
                                            g.drawLine(x1, y1, x2, y2);

                                            canvas.repaint();

                                        }

                                        points.clear();

                                    }


                                }

                            }
                        }

                        regular_shape_undo_process("squ", squ_points, squ_colors);
                    /*//recall squ points
                    if(squ_points.size() < 5){
                        //it is the first shape
                        //do nothing don't paint anything on canvas
                    }else{
                        for (int i = 0; i < squ_colors.size()-1; i++) {
//                            System.out.println(cir_colors.size());
                            int begin_index = i * 4;
                            x1 = squ_points.get(begin_index);
                            y1 = squ_points.get(begin_index + 1);
                            x2 = squ_points.get(begin_index + 2);
                            y2 = squ_points.get(begin_index + 3);

                            int[] start_points = start_point(x1, y1, x2, y2);

                            g.setColor(squ_colors.get(i));
                            g.drawRect(start_points[0], start_points[1], Math.abs(x2-x1), Math.abs(x2-x1));

                            canvas.repaint();
                        }
                    }*/


                    }

                    if(last_state.equals("ell")){
                        //recall other point_stack
                        if(rec_colors.size()>0){
                            repaint_shapes("rec", rec_colors, rec_points);
                        }

                        if(cir_colors.size()>0){
                            //square repaint
                            repaint_shapes("cir", cir_colors, cir_points);
                        }

                        if(squ_colors.size()>0){
                            //ellipse repaint
                            repaint_shapes("squ", squ_colors, squ_points);
                        }

                        if(str_colors.size()>0){
                            //straight line repaint
                            repaint_shapes("str", str_colors, str_points);
                        }
                        if(poly_colors.size()>0){
                            //polygon repaint
                            ArrayList<Integer> separate_index = get_separate_index(poly_points);
                            ArrayList<Integer> points = new ArrayList<Integer>();

                            //other shapes
                            for (int j = 0; j < separate_index.size()-1; j++) {
                                Color polygon_color = poly_colors.get(j);
                                //each shape repaint
                                int begin_index = separate_index.get(j);
                                int end_index = separate_index.get(j+1);

                                //get polygon points arraylist
                                for (int k = (begin_index+1); k < end_index; k++) {
                                    points.add(poly_points.get(k));

                                }
                                System.out.println(points);

                                //repaint one polygon
                                int line_num = points.size()/2 - 1 ;
                                for (int k = 0; k < line_num; k++) {
                                    int begin_point = k*2;
                                    x1 = points.get(begin_point);
                                    y1 = points.get(begin_point + 1);
                                    x2 = points.get(begin_point + 2);
                                    y2 = points.get(begin_point + 3);

                                    g.setColor(polygon_color);
                                    g.drawLine(x1, y1, x2, y2);

                                    canvas.repaint();
                                }
                                points.clear();
                            }

                            //the last polygon
                            //get last shape points arraylist
                            int index = separate_index.get(separate_index.size()-1);
                            for (int j = index+1; j < poly_points.size(); j++) {
                                points.add(poly_points.get(j));
                            }
                            System.out.println(points);
                            //using points to repaint polygon
                            int line_num = points.size()/2 - 1 ;
                            for (int j = 0; j < line_num; j++) {
                                int begin_point = j*2;
                                x1 = points.get(begin_point);
                                y1 = points.get(begin_point + 1);
                                x2 = points.get(begin_point + 2);
                                y2 = points.get(begin_point + 3);

                                g.setColor(poly_colors.get(poly_colors.size()-1));
                                g.drawLine(x1, y1, x2, y2);

                                canvas.repaint();

                            }

                        }
                        if(handdraw_colors.size()>0){
                            //repaint all handdraw
                            ArrayList<Integer> separate_index = get_separate_index(handdraw_points);
                            ArrayList<Integer> points = new ArrayList<Integer>();
                            for (int i = 0; i < handdraw_colors.size(); i++) {
                                //each handdraw
                                //take all points in one draw
                                if(i == 0){
                                    //first draw
                                    int begin_index = 0;
                                    int end_index = separate_index.get(i);
                                    //get all points in first drawing
                                    for (int j = begin_index; j < end_index; j++) {
                                        points.add(handdraw_points.get(j));
                                    }
                                    //repaint first drawing
                                    System.out.println(points);

                                    int line_num = points.size()/2 - 1;
                                    for (int j = 0; j < line_num; j++) {
                                        //draw each short line
                                        begin_index = j*2;

                                        x1 = points.get(begin_index);
                                        y1 = points.get(begin_index + 1);
                                        x2 = points.get(begin_index + 2);
                                        y2 = points.get(begin_index + 3);

                                        g.setColor(handdraw_colors.get(i));
                                        g.drawLine(x1, y1, x2, y2);

                                        canvas.repaint();

                                    }
                                    points.clear();

                                }else{
                                    //draw other handdraws
                                    for (int j = 0; j < handdraw_colors.size()-1; j++) {
                                        //draw each drawing

                                        int begin_index = separate_index.get(j)+1;
                                        int end_index = separate_index.get(j+1);
                                        //get all points in one drawing
                                        for (int k = begin_index; k < end_index; k++) {
                                            points.add(handdraw_points.get(k));
                                        }
                                        System.out.println(points);

                                        //repaint drawing
                                        int line_num = points.size()/2 - 1;
                                        for (int k = 0; k < line_num; k++) {
                                            //each line
                                            begin_index = k*2;

                                            x1 = points.get(begin_index);
                                            y1 = points.get(begin_index + 1);
                                            x2 = points.get(begin_index + 2);
                                            y2 = points.get(begin_index + 3);

                                            g.setColor(handdraw_colors.get(i));
                                            g.drawLine(x1, y1, x2, y2);

                                            canvas.repaint();

                                        }

                                        points.clear();

                                    }


                                }

                            }

                        }

                        regular_shape_undo_process("ell", ell_points, ell_colors);

                    /*//recall ellipse points
                    if(ell_points.size() < 5){
                        //it is the first shape
                        //do nothing don't paint anything on canvas
                    }else{
                        for (int i = 0; i < ell_colors.size()-1; i++) {
//                            System.out.println(cir_colors.size());
                            int begin_index = i * 4;
                            x1 = ell_points.get(begin_index);
                            y1 = ell_points.get(begin_index + 1);
                            x2 = ell_points.get(begin_index + 2);
                            y2 = ell_points.get(begin_index + 3);

                            int[] start_points = start_point(x1, y1, x2, y2);

                            g.setColor(ell_colors.get(i));
                            g.drawOval(start_points[0], start_points[1], Math.abs(x2-x1), Math.abs(y2-y1));

                            canvas.repaint();
                        }
                    }
*/

                    }

                    if(last_state.equals("str")){
                        //recall other point_stack
                        if(rec_colors.size()>0){
                            repaint_shapes("rec", rec_colors, rec_points);
                        }

                        if(cir_colors.size()>0){
                            //square repaint
                            repaint_shapes("cir", cir_colors, cir_points);
                        }

                        if(squ_colors.size()>0){
                            //ellipse repaint
                            repaint_shapes("squ", squ_colors, squ_points);
                        }

                        if(ell_colors.size()>0){
                            //straight line repaint
                            repaint_shapes("ell", ell_colors, ell_points);
                        }
                        if(poly_colors.size()>0){
                            //polygon repaint
                            ArrayList<Integer> separate_index = get_separate_index(poly_points);
                            ArrayList<Integer> points = new ArrayList<Integer>();

                            //other shapes
                            for (int j = 0; j < separate_index.size()-1; j++) {
                                Color polygon_color = poly_colors.get(j);
                                //each shape repaint
                                int begin_index = separate_index.get(j);
                                int end_index = separate_index.get(j+1);

                                //get polygon points arraylist
                                for (int k = (begin_index+1); k < end_index; k++) {
                                    points.add(poly_points.get(k));

                                }
                                System.out.println(points);

                                //repaint one polygon
                                int line_num = points.size()/2 - 1 ;
                                for (int k = 0; k < line_num; k++) {
                                    int begin_point = k*2;
                                    x1 = points.get(begin_point);
                                    y1 = points.get(begin_point + 1);
                                    x2 = points.get(begin_point + 2);
                                    y2 = points.get(begin_point + 3);

                                    g.setColor(polygon_color);
                                    g.drawLine(x1, y1, x2, y2);

                                    canvas.repaint();
                                }
                                points.clear();
                            }

                            //the last polygon
                            //get last shape points arraylist
                            int index = separate_index.get(separate_index.size()-1);
                            for (int j = index+1; j < poly_points.size(); j++) {
                                points.add(poly_points.get(j));
                            }
                            System.out.println(points);
                            //using points to repaint polygon
                            int line_num = points.size()/2 - 1 ;
                            for (int j = 0; j < line_num; j++) {
                                int begin_point = j*2;
                                x1 = points.get(begin_point);
                                y1 = points.get(begin_point + 1);
                                x2 = points.get(begin_point + 2);
                                y2 = points.get(begin_point + 3);

                                g.setColor(poly_colors.get(poly_colors.size()-1));
                                g.drawLine(x1, y1, x2, y2);

                                canvas.repaint();

                            }

                        }
                        if(handdraw_colors.size()>0){
                            //repaint all handdraw
                            ArrayList<Integer> separate_index = get_separate_index(handdraw_points);
                            ArrayList<Integer> points = new ArrayList<Integer>();
                            for (int i = 0; i < handdraw_colors.size(); i++) {
                                //each handdraw
                                //take all points in one draw
                                if(i == 0){
                                    //first draw
                                    int begin_index = 0;
                                    int end_index = separate_index.get(i);
                                    //get all points in first drawing
                                    for (int j = begin_index; j < end_index; j++) {
                                        points.add(handdraw_points.get(j));
                                    }
                                    //repaint first drawing
                                    System.out.println(points);

                                    int line_num = points.size()/2 - 1;
                                    for (int j = 0; j < line_num; j++) {
                                        //draw each short line
                                        begin_index = j*2;

                                        x1 = points.get(begin_index);
                                        y1 = points.get(begin_index + 1);
                                        x2 = points.get(begin_index + 2);
                                        y2 = points.get(begin_index + 3);

                                        g.setColor(handdraw_colors.get(i));
                                        g.drawLine(x1, y1, x2, y2);

                                        canvas.repaint();

                                    }
                                    points.clear();

                                }else{
                                    //draw other handdraws
                                    for (int j = 0; j < handdraw_colors.size()-1; j++) {
                                        //draw each drawing

                                        int begin_index = separate_index.get(j)+1;
                                        int end_index = separate_index.get(j+1);
                                        //get all points in one drawing
                                        for (int k = begin_index; k < end_index; k++) {
                                            points.add(handdraw_points.get(k));
                                        }
                                        System.out.println(points);

                                        //repaint drawing
                                        int line_num = points.size()/2 - 1;
                                        for (int k = 0; k < line_num; k++) {
                                            //each line
                                            begin_index = k*2;

                                            x1 = points.get(begin_index);
                                            y1 = points.get(begin_index + 1);
                                            x2 = points.get(begin_index + 2);
                                            y2 = points.get(begin_index + 3);

                                            g.setColor(handdraw_colors.get(i));
                                            g.drawLine(x1, y1, x2, y2);

                                            canvas.repaint();

                                        }

                                        points.clear();

                                    }


                                }

                            }

                        }

                        regular_shape_undo_process("str", str_points, str_colors);

                   /* //recall straight line points
                    if(str_points.size() < 5){
                        //it is the first shape
                        //do nothing don't paint anything on canvas
                    }else{
                        for (int i = 0; i < str_colors.size()-1; i++) {
//                            System.out.println(cir_colors.size());
                            int begin_index = i * 4;
                            x1 = str_points.get(begin_index);
                            y1 = str_points.get(begin_index + 1);
                            x2 = str_points.get(begin_index + 2);
                            y2 = str_points.get(begin_index + 3);

                            int[] start_points = start_point(x1, y1, x2, y2);

                            g.setColor(str_colors.get(i));
                            g.drawLine(x1, y1, x2, y2);

                            canvas.repaint();
                        }
                    }*/


                    }

                    if(last_state.equals("poly")){
                        //repaint polygons except the last one
                        //polygon repaint don't need to consider the last polygon
                        //not good to use polygon color cuz it can be change whithin one polygon
                        ArrayList<Integer> separate_index = get_separate_index(poly_points);
                        if(separate_index.size()>1){
                            ArrayList<Integer> points = new ArrayList<Integer>();
                            
                            for (int j = 0; j < separate_index.size(); j++) {
                                Color polygon_color = poly_colors.get(j);
                                //each shape repaint
                                int begin_index = separate_index.get(j);


                                //for the last one
                                if(j == (separate_index.size()-1)){
                                    // store all points, color, shape
                                    int end_index = poly_points.size();
                                    popped_color = poly_colors.get(poly_colors.size()-1);
                                    popped_shape = "poly";

                                    popped_points.clear();
                                    for (int k = (begin_index+1); k < end_index; k++) {
                                        popped_points.add(poly_points.get(k));
                                    }

                                    System.out.println(popped_points+" is the popped points");

                                    //popped the last one from color, points, progress stack
                                    System.out.println(poly_points + " is the previous poly points stack");
                                    for (int p = 0; p < (popped_points.size()+1); p++) {
                                        poly_points.remove(poly_points.size()-1);
                                    }
                                    System.out.println(poly_points+" is the current stack");

                                    for (int s = 0; s < popped_points.size()/2; s++) {
                                        progress_stack.remove(progress_stack.size()-1);
                                        poly_colors.remove(poly_colors.size()-1);
                                    }
                                    System.out.println(progress_stack+" is current progress_stack");
                                    System.out.println(popped_points+" is the popped points");



                                }else{
                                    //get polygon points arraylist
                                    int end_index = separate_index.get(j+1);
                                    for (int k = (begin_index+1); k < end_index; k++) {
                                        points.add(poly_points.get(k));

                                    }
                                    System.out.println(points);

                                    //repaint one polygon
                                    int line_num = points.size()/2 - 1 ;
                                    for (int k = 0; k < line_num; k++) {
                                        int begin_point = k*2;
                                        x1 = points.get(begin_point);
                                        y1 = points.get(begin_point + 1);
                                        x2 = points.get(begin_point + 2);
                                        y2 = points.get(begin_point + 3);

                                        g.setColor(polygon_color);
                                        g.drawLine(x1, y1, x2, y2);

                                        canvas.repaint();
                                    }
                                    points.clear();
                                }


                            }
                        }
                        if(separate_index.size() == 1){
                            // store the last color, points, shape
                            //popped the last one from color, points, progress stack
                            System.out.println(poly_points+" is poly points stack");
                            popped_color = poly_colors.get(poly_colors.size()-1);
                            popped_shape = "poly";
                            poly_points.remove(0);
                            popped_points.clear();
                            for (int i = 0; i < poly_points.size(); i++) {
                                popped_points.add(poly_points.get(i));
                            }
//                            int line_num = poly_points.size()-1;


                            poly_colors.clear();

                            for (int s = 0; s < poly_points.size()/2; s++) {
                                progress_stack.remove(progress_stack.size()-1);
                            }
                            System.out.println(progress_stack+" after polygon pop");
                            poly_points.clear();
                            System.out.println(popped_points+" is the popped points");



                        }

                        //repaint other shapes
                        if(rec_colors.size()>0){
                            repaint_shapes("rec", rec_colors, rec_points);
                        }

                        if(cir_colors.size()>0){
                            //circle repaint
                            repaint_shapes("cir", cir_colors, cir_points);
                        }

                        if(squ_colors.size()>0){
                            //square repaint
                            repaint_shapes("squ", squ_colors, squ_points);
                        }

                        if(ell_colors.size()>0){
                            //ellipse repaint
                            repaint_shapes("ell", ell_colors, ell_points);
                        }

                        if(str_colors.size()>0){
                            //straight line repaint
                            repaint_shapes("str", str_colors, str_points);
                        }

                        if(handdraw_colors.size()>0){
                            //repaint all handdraw
                            ArrayList<Integer> separate_index_hand = get_separate_index(handdraw_points);
                            ArrayList<Integer> points = new ArrayList<Integer>();
                            for (int i = 0; i < handdraw_colors.size(); i++) {
                                //each handdraw
                                //take all points in one draw
                                if(i == 0){
                                    //first draw
                                    int begin_index = 0;
                                    int end_index = separate_index_hand.get(i);
                                    //get all points in first drawing
                                    for (int j = begin_index; j < end_index; j++) {
                                        points.add(handdraw_points.get(j));
                                    }
                                    //repaint first drawing
                                    System.out.println(points);

                                    int line_num = points.size()/2 - 1;
                                    for (int j = 0; j < line_num; j++) {
                                        //draw each short line
                                        begin_index = j*2;

                                        x1 = points.get(begin_index);
                                        y1 = points.get(begin_index + 1);
                                        x2 = points.get(begin_index + 2);
                                        y2 = points.get(begin_index + 3);

                                        g.setColor(handdraw_colors.get(i));
                                        g.drawLine(x1, y1, x2, y2);

                                        canvas.repaint();

                                    }
                                    points.clear();

                                }else{
                                    //draw other handdraws
                                    for (int j = 0; j < handdraw_colors.size()-1; j++) {
                                        //draw each drawing

                                        int begin_index = separate_index_hand.get(j)+1;
                                        int end_index = separate_index_hand.get(j+1);
                                        //get all points in one drawing
                                        for (int k = begin_index; k < end_index; k++) {
                                            points.add(handdraw_points.get(k));
                                        }
                                        System.out.println(points);

                                        //repaint drawing
                                        int line_num = points.size()/2 - 1;
                                        for (int k = 0; k < line_num; k++) {
                                            //each line
                                            begin_index = k*2;

                                            x1 = points.get(begin_index);
                                            y1 = points.get(begin_index + 1);
                                            x2 = points.get(begin_index + 2);
                                            y2 = points.get(begin_index + 3);

                                            g.setColor(handdraw_colors.get(i));
                                            g.drawLine(x1, y1, x2, y2);

                                            canvas.repaint();

                                        }

                                        points.clear();

                                    }


                                }

                            }

                        }


                    }

                    if(last_state.equals("handdraw")){
                        //repaint shapes except the last one
                        //repaint all handdraw
                        ArrayList<Integer> separate_index = get_separate_index(handdraw_points);
                        ArrayList<Integer> points = new ArrayList<Integer>();
                        if(handdraw_colors.size() == 1){
                            // store color points shape
                            int begin_index = 0;
                            int end_index = separate_index.get(0);

                            popped_color = handdraw_colors.get(0);
                            popped_shape = "handdraw";

                            popped_points.clear();
                            for (int p = begin_index; p < end_index; p++) {
                                popped_points.add(handdraw_points.get(p));
                            }

                            //pop those data
                            handdraw_colors.clear();
                            progress_stack.remove(progress_stack.size()-1);
                            handdraw_points.clear();

                        }
                        if(handdraw_colors.size() > 1){
                            for (int i = 0; i < handdraw_colors.size(); i++) {
                                //each handdraw
                                //take all points in one draw

                                if(i == 0){
                                    //first draw
                                    int begin_index = 0;
                                    int end_index = separate_index.get(i);
                                    //get all points in first drawing
                                    for (int j = begin_index; j < end_index; j++) {
                                        points.add(handdraw_points.get(j));
                                    }
                                    //repaint first drawing
                                    System.out.println(points);

                                    int line_num = points.size()/2 - 1;
                                    for (int j = 0; j < line_num; j++) {
                                        //draw each short line
                                        begin_index = j*2;

                                        x1 = points.get(begin_index);
                                        y1 = points.get(begin_index + 1);
                                        x2 = points.get(begin_index + 2);
                                        y2 = points.get(begin_index + 3);

                                        g.setColor(handdraw_colors.get(i));
                                        g.drawLine(x1, y1, x2, y2);

                                        canvas.repaint();

                                    }
                                    points.clear();

                                }
                                if(i == handdraw_colors.size()-1){
                                    // the last freehand line
                                    //store shape color points
                                    int begin_index = separate_index.get(separate_index.size()-2)+1;
                                    int end_index = separate_index.get(separate_index.size()-1);

                                    popped_color = handdraw_colors.get(handdraw_colors.size()-1);
                                    popped_shape = "handdraw";

                                    popped_points.clear();
                                    for (int j = begin_index; j < end_index; j++) {
                                        popped_points.add(handdraw_points.get(j));
                                    }

                                    System.out.println(popped_points+"  popped freehand points");

                                    //pop those data
                                    handdraw_colors.remove(handdraw_colors.size()-1);
                                    progress_stack.remove(progress_stack.size()-1);
                                    for (int p = 0; p < (popped_points.size()+1); p++) {
                                        handdraw_points.remove(handdraw_points.size()-1);
                                    }
                                    System.out.println(handdraw_points+" after freehand pop");


                                }

                                else{
                                    //draw other handdraws
                                    for (int j = 0; j < handdraw_colors.size()-2; j++) {
                                        //draw each drawing

                                        int begin_index = separate_index.get(j)+1;
                                        int end_index = separate_index.get(j+1);
                                        //get all points in one drawing
                                        for (int k = begin_index; k < end_index; k++) {
                                            points.add(handdraw_points.get(k));
                                        }
                                        System.out.println(points);

                                        //repaint drawing
                                        int line_num = points.size()/2 - 1;
                                        for (int k = 0; k < line_num; k++) {
                                            //each line
                                            begin_index = k*2;

                                            x1 = points.get(begin_index);
                                            y1 = points.get(begin_index + 1);
                                            x2 = points.get(begin_index + 2);
                                            y2 = points.get(begin_index + 3);

                                            g.setColor(handdraw_colors.get(i));
                                            g.drawLine(x1, y1, x2, y2);

                                            canvas.repaint();

                                        }

                                        points.clear();

                                    }


                                }

                            }
                        }



                        //repaint other shapes
                        if(rec_colors.size()>0){
                            repaint_shapes("rec", rec_colors, rec_points);
                        }

                        if(cir_colors.size()>0){
                            //circle repaint
                            repaint_shapes("cir", cir_colors, cir_points);
                        }

                        if(squ_colors.size()>0){
                            //square repaint
                            repaint_shapes("squ", squ_colors, squ_points);
                        }

                        if(ell_colors.size()>0){
                            //ellipse repaint
                            repaint_shapes("ell", ell_colors, ell_points);
                        }

                        if(str_colors.size()>0){
                            //straight line repaint
                            repaint_shapes("str", str_colors, str_points);
                        }

                        if(poly_colors.size()>0){
                            //polygon repaint
                            separate_index = get_separate_index(poly_points);
                            points = new ArrayList<Integer>();

                            //other shapes
                            for (int j = 0; j < separate_index.size()-1; j++) {
                                Color polygon_color = poly_colors.get(j);
                                //each shape repaint
                                int begin_index = separate_index.get(j);
                                int end_index = separate_index.get(j+1);

                                //get polygon points arraylist
                                for (int k = (begin_index+1); k < end_index; k++) {
                                    points.add(poly_points.get(k));

                                }
                                System.out.println(points);

                                //repaint one polygon
                                int line_num = points.size()/2 - 1 ;
                                for (int k = 0; k < line_num; k++) {
                                    int begin_point = k*2;
                                    x1 = points.get(begin_point);
                                    y1 = points.get(begin_point + 1);
                                    x2 = points.get(begin_point + 2);
                                    y2 = points.get(begin_point + 3);

                                    g.setColor(polygon_color);
                                    g.drawLine(x1, y1, x2, y2);

                                    canvas.repaint();
                                }
                                points.clear();
                            }

                            //the last polygon
                            //get last shape points arraylist
                            int index = separate_index.get(separate_index.size()-1);
                            for (int j = index+1; j < poly_points.size(); j++) {
                                points.add(poly_points.get(j));
                            }
                            System.out.println(points);
                            //using points to repaint polygon
                            int line_num = points.size()/2 - 1 ;
                            for (int j = 0; j < line_num; j++) {
                                int begin_point = j*2;
                                x1 = points.get(begin_point);
                                y1 = points.get(begin_point + 1);
                                x2 = points.get(begin_point + 2);
                                y2 = points.get(begin_point + 3);

                                g.setColor(poly_colors.get(poly_colors.size()-1));
                                g.drawLine(x1, y1, x2, y2);

                                canvas.repaint();

                            }

                        }


                    }

                }


            }
        });

        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //one-step redo
                //recall popped stack and draw that shape
                //store this shape in progress stack
//                System.out.println(popped_shape+ "||  redo shape variable test");
//                System.out.println(popped_color+ " redo color variable test");
//                System.out.println(popped_points+ " redo points stack test");
                System.out.println(popped_points+ "++++++++++++++++++++++");
                int x1_r = popped_points.get(0);
                int y1_r = popped_points.get(1);
                int x2_r = popped_points.get(2);
                int y2_r = popped_points.get(3);

                switch (popped_shape){
                    case "rec":
                        g.setColor(popped_color);
                        int[] start_points = start_point(x1_r, y1_r,x2_r,y2_r);
                        g.drawRect(start_points[0], start_points[1], Math.abs(x2_r-x1_r), Math.abs(y2_r-y1_r));
                        canvas.repaint();

//                        System.out.println(rec_colors);
//                        System.out.println(rec_points);
//                        System.out.println(progress_stack);

                        //store this shape in regular stack
                        rec_colors.add(popped_color);
                        progress_stack.add(popped_shape);
                        for (int i = 0; i < popped_points.size(); i++) {
                            rec_points.add(popped_points.get(i));
                        }

//                        System.out.println(rec_colors);
                        System.out.println(rec_points+"++++++++++++++++++++++++++++++++");
//                        System.out.println(progress_stack);

                        break;
                    case "squ":
                        g.setColor(popped_color);
                        start_points = start_point(x1_r, y1_r,x2_r,y2_r);
                        g.drawRect(start_points[0], start_points[1], Math.abs(x2_r-x1_r), Math.abs(x2_r-x1_r));
                        canvas.repaint();

                        //store this shape in regular stack
                        squ_colors.add(popped_color);
                        progress_stack.add(popped_shape);
                        for (int i = 0; i < popped_points.size(); i++) {
                            squ_points.add(popped_points.get(i));
                        }

                        break;

                    case "ell":
                        g.setColor(popped_color);
                        start_points = start_point(x1_r, y1_r,x2_r,y2_r);
                        g.drawOval(start_points[0], start_points[1], Math.abs(x2_r-x1_r), Math.abs(y2_r-y1_r));
                        canvas.repaint();

                        //store this shape in regular stack
                        ell_colors.add(popped_color);
                        progress_stack.add(popped_shape);
                        for (int i = 0; i < popped_points.size(); i++) {
                            ell_points.add(popped_points.get(i));
                        }
                        break;

                    case "cir":
                        g.setColor(popped_color);
                        start_points = start_point(x1_r, y1_r,x2_r,y2_r);
                        g.drawOval(start_points[0], start_points[1], Math.abs(x2_r-x1_r), Math.abs(x2_r-x1_r));
                        canvas.repaint();

                        //store this shape in regular stack
                        cir_colors.add(popped_color);
                        progress_stack.add(popped_shape);
                        for (int i = 0; i < popped_points.size(); i++) {
                            cir_points.add(popped_points.get(i));
                        }

                        break;

                    case "str":
                        g.setColor(popped_color);
                        g.drawLine(x1_r, y1_r, x2_r, y2_r);
                        canvas.repaint();

                        //store this shape in regular stack
                        str_colors.add(popped_color);
                        progress_stack.add(popped_shape);
                        for (int i = 0; i < popped_points.size(); i++) {
                            str_points.add(popped_points.get(i));
                        }

                        break;

                    case "handdraw":
                        g.setColor(popped_color);
                        int line_num = popped_points.size()/2-1;
                        for (int i = 0; i < line_num; i++) {
                            int begin_index = i*2;
                           g.drawLine(popped_points.get(begin_index),popped_points.get(begin_index+1),popped_points.get(begin_index+2),popped_points.get(begin_index+3));
                        }
                        canvas.repaint();

                        //store this shape in regular stack
                        handdraw_colors.add(popped_color);
                        progress_stack.add(popped_shape);

                        for (int i = 0; i < popped_points.size(); i++) {
                            handdraw_points.add(popped_points.get(i));
                        }
                        handdraw_points.add(-1);

                        break;

                    case "poly":
                        g.setColor(popped_color);
                        line_num = popped_points.size()/2-1;
                        for (int i = 0; i < line_num; i++) {
                            int begin_index = i*2;
                            g.drawLine(popped_points.get(begin_index),popped_points.get(begin_index+1),popped_points.get(begin_index+2),popped_points.get(begin_index+3));
                        }
                        canvas.repaint();

                        //store this shape in regular stack
                        for (int i = 0; i < popped_points.size()/2; i++) {
                            poly_colors.add(popped_color);
                            progress_stack.add(popped_shape);
                        }

                        poly_points.add(-1);
                        for (int i = 0; i < popped_points.size(); i++) {
                            poly_points.add(popped_points.get(i));
                        }
                        break;

                }

                /*//repaint last shape in stack
                String last_shape = progress_stack.get(progress_stack.size()-1);
                if(last_shape.equals("rec")){
                    //rectangle repaint
                    repaint_shapes("rec", rec_colors, rec_points);
                }
                if(last_shape.equals("cir")){
                    //circle repaint
                    repaint_shapes("cir", cir_colors, cir_points);
                }
                if(last_shape.equals("squ")){
                    //square repaint
                    repaint_shapes("squ", squ_colors, squ_points);
                }
                if(last_shape.equals("ell")){
                    //ellipse repaint
                    repaint_shapes("ell", ell_colors, ell_points);
                }
                if(last_shape.equals("str")){
                    //straight line repaint
                    repaint_shapes("str", str_colors, str_points);
                }
                if(last_shape.equals("poly")){
                    //polygon repaint
                    ArrayList<Integer> separate_index = get_separate_index(poly_points);
                    ArrayList<Integer> points = new ArrayList<Integer>();

                    //other shapes
                    for (int j = 0; j < separate_index.size()-1; j++) {
                        Color polygon_color = poly_colors.get(j);
                        //each shape repaint
                        int begin_index = separate_index.get(j);
                        int end_index = separate_index.get(j+1);

                        //get polygon points arraylist
                        for (int k = (begin_index+1); k < end_index; k++) {
                            points.add(poly_points.get(k));

                        }
                        System.out.println(points);

                        //repaint one polygon
                        int line_num = points.size()/2 - 1 ;
                        for (int k = 0; k < line_num; k++) {
                            int begin_point = k*2;
                            x1 = points.get(begin_point);
                            y1 = points.get(begin_point + 1);
                            x2 = points.get(begin_point + 2);
                            y2 = points.get(begin_point + 3);

                            g.setColor(polygon_color);
                            g.drawLine(x1, y1, x2, y2);

                            canvas.repaint();
                        }
                        points.clear();
                    }

                    //the last polygon
                    //get last shape points arraylist
                    int index = separate_index.get(separate_index.size()-1);
                    for (int j = index+1; j < poly_points.size(); j++) {
                        points.add(poly_points.get(j));
                    }
                    System.out.println(points);
                    //using points to repaint polygon
                    int line_num = points.size()/2 - 1 ;
                    for (int j = 0; j < line_num; j++) {
                        int begin_point = j*2;
                        x1 = points.get(begin_point);
                        y1 = points.get(begin_point + 1);
                        x2 = points.get(begin_point + 2);
                        y2 = points.get(begin_point + 3);

                        g.setColor(poly_colors.get(poly_colors.size()-1));
                        g.drawLine(x1, y1, x2, y2);

                        canvas.repaint();
                    }
                }

                if(last_shape.equals("handdraw")){
                    //freehand drawing repaint
                    if(handdraw_colors.size()>0){
                        //repaint all handdraw
                        ArrayList<Integer> separate_index = get_separate_index(handdraw_points);
                        ArrayList<Integer> points = new ArrayList<Integer>();
                        for (int i = 0; i < handdraw_colors.size(); i++) {
                            //each handdraw
                            //take all points in one draw
                            if(i == 0){
                                //first draw
                                int begin_index = 0;
                                int end_index = separate_index.get(i);
                                //get all points in first drawing
                                for (int j = begin_index; j < end_index; j++) {
                                    points.add(handdraw_points.get(j));
                                }
                                //repaint first drawing
                                System.out.println(points);

                                int line_num = points.size()/2 - 1;
                                for (int j = 0; j < line_num; j++) {
                                    //draw each short line
                                    begin_index = j*2;

                                    x1 = points.get(begin_index);
                                    y1 = points.get(begin_index + 1);
                                    x2 = points.get(begin_index + 2);
                                    y2 = points.get(begin_index + 3);

                                    g.setColor(handdraw_colors.get(i));
                                    g.drawLine(x1, y1, x2, y2);

                                    canvas.repaint();

                                }
                                points.clear();

                            }else{
                                //draw other handdraws
                                for (int j = 0; j < handdraw_colors.size()-1; j++) {
                                    //draw each drawing

                                    int begin_index = separate_index.get(j)+1;
                                    int end_index = separate_index.get(j+1);
                                    //get all points in one drawing
                                    for (int k = begin_index; k < end_index; k++) {
                                        points.add(handdraw_points.get(k));
                                    }
                                    System.out.println(points);

                                    //repaint drawing
                                    int line_num = points.size()/2 - 1;
                                    for (int k = 0; k < line_num; k++) {
                                        //each line
                                        begin_index = k*2;

                                        x1 = points.get(begin_index);
                                        y1 = points.get(begin_index + 1);
                                        x2 = points.get(begin_index + 2);
                                        y2 = points.get(begin_index + 3);

                                        g.setColor(handdraw_colors.get(i));
                                        g.drawLine(x1, y1, x2, y2);

                                        canvas.repaint();

                                    }

                                    points.clear();

                                }


                            }

                        }

                    }

                }*/


            }
        });

        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //consider how to select an object which has been paint on the canvas
                state = "select";
                instruction.setText("Drag the rectangle to cover the whole shape which you want to select");

            }
        });

        move.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "move";
            }
        });

        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*System.out.println("++++++++++++");
                System.out.println(selected_shape);
                System.out.println(selected_object);
                System.out.println(selected_element_index);*/
                ArrayList<Integer> shape_index = new ArrayList<Integer>();
                if(selected_shape.length()==0){
                    instruction.setText("Please select an object before cutting operation");
                }else{
                    switch(selected_shape){
                        case "rec":
                            int begin_index = selected_object.get(0);
                            if(rec_points.size()>4){
                                for (int i = 0; i < 4; i++) {
                                    rec_points.remove(begin_index);
                                }
                            }else{
                                rec_points.clear();
                            }

                            rec_colors.remove(selected_element_index);
                            for (int i = 0; i < progress_stack.size(); i++) {
                                if(progress_stack.get(i).equals("rec")){
                                    shape_index.add(i);
                                }
                            }
                            int cut_shape_index = shape_index.get(selected_element_index);
                            progress_stack.remove(cut_shape_index);
                            break;

                        case "squ":
                            begin_index = selected_object.get(0);
                            if(squ_points.size()>4){
                                for (int i = 0; i < 4; i++) {
                                    squ_points.remove(begin_index);
                                }
                            }else{
                                squ_points.clear();
                            }
                            squ_colors.remove(selected_element_index);
                            for (int i = 0; i < progress_stack.size(); i++) {
                                if(progress_stack.get(i).equals("squ")){
                                    shape_index.add(i);
                                }
                            }
                            cut_shape_index = shape_index.get(selected_element_index);
                            progress_stack.remove(cut_shape_index);
                            break;

                        case "ell":
                            begin_index = selected_object.get(0);
                            if(ell_points.size()>4){
                                for (int i = 0; i < 4; i++) {
                                    ell_points.remove(begin_index);
                                }
                            }else{
                                ell_points.clear();
                            }
                            ell_colors.remove(selected_element_index);
                            for (int i = 0; i < progress_stack.size(); i++) {
                                if(progress_stack.get(i).equals("ell")){
                                    shape_index.add(i);
                                }
                            }
                            cut_shape_index = shape_index.get(selected_element_index);
                            progress_stack.remove(cut_shape_index);
                            break;

                        case "cir":
                            begin_index = selected_object.get(0);
                            if(cir_points.size()>4){
                                for (int i = 0; i < 4; i++) {
                                    cir_points.remove(begin_index);
                                }
                            }else{
                                cir_points.clear();
                            }
                            cir_colors.remove(selected_element_index);
                            for (int i = 0; i < progress_stack.size(); i++) {
                                if(progress_stack.get(i).equals("cir")){
                                    shape_index.add(i);
                                }
                            }
                            cut_shape_index = shape_index.get(selected_element_index);
                            progress_stack.remove(cut_shape_index);
                            break;

                        case "str":
                            begin_index = selected_object.get(0);
                            if(str_points.size()>4){
                                for (int i = 0; i < 4; i++) {
                                    str_points.remove(begin_index);
                                }
                            }else{
                                str_points.clear();
                            }
                            str_colors.remove(selected_element_index);
                            for (int i = 0; i < progress_stack.size(); i++) {
                                if(progress_stack.get(i).equals("str")){
                                    shape_index.add(i);
                                }
                            }
                            cut_shape_index = shape_index.get(selected_element_index);
                            progress_stack.remove(cut_shape_index);
                            break;

                    }
                }


                //repaint all shapes

                g.setColor(Color.WHITE);
                g.fillRect(0,0,1200, 800);
                canvas.repaint();
                System.out.println(progress_stack);
                System.out.println(rec_points);
                repaint_shapes("rec", rec_colors, rec_points);
                repaint_shapes("squ", squ_colors, squ_points);
                repaint_shapes("ell", ell_colors, ell_points);
                repaint_shapes("cir", cir_colors, cir_points);
                repaint_shapes("str", str_colors, str_points);

                canvas.repaint();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
//                System.out.println("dragged");
                if(state.equals("handdraw") || state.equals("")){
                    if(x1>0 && y1>0){
                        g.setColor(pen_color);
                        g.drawLine(x1,y1,e.getX(),e.getY());
                    }

                    x1 = e.getX();
                    y1 = e.getY();

                    handdraw_points.add(x1);
                    handdraw_points.add(y1);

                    canvas.repaint();


                }
                if(state.equals("select")){
                    Color selected_area_color = new Color(230,230,230,10);
                    g.setColor(selected_area_color);
                    g.fillRect(x1, y1, Math.abs(e.getX()-x1),Math.abs(e.getY()-y1));
                    canvas.repaint();
                }


            }
        });


        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(state.equals("poly")){
                    //多边形时，点击不会记录 点击时不会更新x1 y1

                }else{
                    //点击时获得当前的坐标值作为x1 y1
                    x1 = e.getX();
                    y1 = e.getY();
                }



            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(state.equals("rec")){
                    //draw rectangle
                    int width = e.getX()-x1;
                    int height = e.getY()-y1;
                    int[] temp = new int[4];

                    //calculate start point using start_point method
                    int[] start_point = start_point(x1, y1, e.getX(), e.getY());

                    g.setColor(pen_color);
                    g.drawRect(start_point[0], start_point[1], Math.abs(width), Math.abs(height));

                    //record paint
                    progress_stack.add("rec");
                    rec_points.add(x1);
                    rec_points.add(y1);
                    rec_points.add(e.getX());
                    rec_points.add(e.getY());


                    canvas.repaint();

                    rec_colors.add(pen_color);
                    System.out.println(rec_colors);


                }else if(state.equals("str")){
                    //draw straight line
                    g.setColor(pen_color);
                    g.drawLine(x1, y1, e.getX(),e.getY());

                    //record paint
                    progress_stack.add("str");
                    str_points.add(x1);
                    str_points.add(y1);
                    str_points.add(e.getX());
                    str_points.add(e.getY());

                    canvas.repaint();

                    str_colors.add(pen_color);


                }else if(state.equals("handdraw") || state.equals("")){
                    //draw hand line
                    g.setColor(pen_color);
                    x1 = -1;
                    y1 = -1;

                    //record paint
                    progress_stack.add("handdraw");

                    //add separate number
                    handdraw_points.add(-1);
                    System.out.println(handdraw_points);
                    handdraw_colors.add(pen_color);


//                    System.out.println(state);

                }else if(state.equals("ell")){
                    //draw ellipse
                    int width = e.getX()-x1;
                    int height = e.getY()-y1;
                    int[] start_point = start_point(x1, y1, e.getX(), e.getY());

                    g.setColor(pen_color);
                    g.drawOval(start_point[0], start_point[1], Math.abs(width), Math.abs(height));

                    //record paint
                    progress_stack.add("ell");
                    ell_points.add(x1);
                    ell_points.add(y1);
                    ell_points.add(e.getX());
                    ell_points.add(e.getY());

                    canvas.repaint();

                    ell_colors.add(pen_color);

                }else if(state.equals("cir")){
                    //draw circle
                    g.setColor(pen_color);
                    g.drawOval(x1, y1, (e.getX()-x1),(e.getX()-x1));

                    //record paint
                    progress_stack.add("cir");
                    cir_points.add(x1);
                    cir_points.add(y1);
                    cir_points.add(e.getX());
                    cir_points.add(e.getY());

                    canvas.repaint();

                    cir_colors.add(pen_color);
                }else if(state.equals("squ")){
                    //draw square
                    int width = e.getX()-x1;
                    int height = e.getY()-y1;
                    int[] start_point = start_point(x1, y1, e.getX(), e.getY());

                    g.setColor(pen_color);
                    g.drawRect(start_point[0], start_point[1], Math.abs(width), Math.abs(width));

                    //record paint
                    progress_stack.add("squ");
                    squ_points.add(x1);
                    squ_points.add(y1);
                    squ_points.add(e.getX());
                    squ_points.add(e.getY());

                    canvas.repaint();

                    squ_colors.add(pen_color);
                }else if(state.equals("poly")){
                    //draw poly
                    g.setColor(pen_color);
                    if(x1 > 0 && y1 > 0){
                        g.drawLine(x1, y1, e.getX(), e.getY());

                    }
                    //松开时的点是下次开始的x1 y1
                    x1 = e.getX();
                    y1 = e.getY();

                    //record paint
                    //only need to record x1 y1
                    progress_stack.add("poly");
                    poly_points.add(x1);
                    poly_points.add(y1);


                    canvas.repaint();



                    poly_colors.add(pen_color);
                    System.out.println(poly_points);
                    System.out.println(poly_points.indexOf(-1));


                }else if(state.equals("select")){
                    x2 = e.getX();
                    y2 = e.getY();

//                    g.fillRect(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1));
//                    System.out.println(x2);
//                    canvas.repaint();

                    //recognize selected object
                    //search each shape points stack to find some shape inside the selection rectangle

                    //search in rectangle
                    search_in_shapes("rectangle", rec_colors, rec_points, "rec");

                    //search in squares
                    search_in_shapes("square", squ_colors, squ_points, "squ");

                    //search in ellipses
                    search_in_shapes("ellipses", ell_colors, ell_points, "ell");

                    //search in circles
                    search_in_shapes("circle", cir_colors, cir_points, "cir");

                    //search in stright lines
                    search_in_shapes("straight line", str_colors, str_points, "str");

                    //search in polygons stack points
                    ArrayList<Integer> separate_index = get_separate_index(poly_points);
                    ArrayList<Integer> temp_poly_points = new ArrayList<Integer>();
                    ArrayList<Integer> x_points = new ArrayList<Integer>();
                    ArrayList<Integer> y_points = new ArrayList<Integer>();
                    System.out.println("separate index is "+separate_index);

                    for (int i = 0; i < separate_index.size(); i++) {
                        int begin_index = separate_index.get(i);
//                        System.out.println(begin_index+"begin index");
                        int end_index;
                        if(i == separate_index.size()-1){
                            //last polygon
                            end_index = poly_points.size()-1;
//                            System.out.println(end_index+" end index");

                            //store all points
                            for (int j = begin_index + 1; j < end_index+1; j++) {
                                temp_poly_points.add(poly_points.get(j));
                            }
                            System.out.println("tem_shape is:"+temp_poly_points);

                            //compare the last shape with that rectangle
                            int poly_x;
                            int poly_y;
                            int counter = 0;
                            for (int j = 0; j < temp_poly_points.size()/2; j++) {
                                poly_x = temp_poly_points.get(i*2);
                                poly_y = temp_poly_points.get(i*2+1);
                                x_points.add(poly_x);
                                y_points.add(poly_y);

                                if(poly_x>x1 && poly_y > y1 && poly_x < x2 && poly_y < y2){
                                    counter++;
                                }else{
                                    break;
                                }

                            }

                            if(counter == temp_poly_points.size()/2){
                                selected_object = temp_poly_points;
                                selected_shape = "poly";
                                System.out.println("selected object is "+selected_shape);

                                g.setColor(new Color(255,255,255,0));
                                g.fillRect(x1,y1,Math.abs(x2-x1), Math.abs(y2-y1));

                                g.setColor(new Color(0,0,15,100));

                            }



                        }

                        //other polygons
                    }

                }

                if(state.equals("move")){
                    x2 = e.getX();
                    y2 = e.getY();
                    int move_x = x2 - x1;
                    int move_y = y2 - y1;

                    int pre_x1;
                    int pre_y1;
                    int pre_x2;
                    int pre_y2;

                    int cur_x1;
                    int cur_y1;
                    int cur_x2;
                    int cur_y2;

                    switch(selected_shape){
                        case "rec":
                            pre_x1 = rec_points.get(selected_object.get(0));
                            pre_y1 = rec_points.get(selected_object.get(1));
                            pre_x2 = rec_points.get(selected_object.get(2));
                            pre_y2 = rec_points.get(selected_object.get(3));

                            System.out.println(rec_points);
                            cur_x1 = pre_x1 + move_x;
                            cur_y1 = pre_y1 + move_y;
                            cur_x2 = pre_x2 + move_x;
                            cur_y2 = pre_y2 + move_y;

                            rec_points.add(cur_x1);
                            rec_points.add(cur_y1);
                            rec_points.add(cur_x2);
                            rec_points.add(cur_y2);
                            rec_colors.add(rec_colors.get(selected_element_index));

                            System.out.println(selected_object);
                            int begin_index = selected_object.get(0);

                            for (int i = 0; i < 4; i++) {
                                rec_points.remove(begin_index);
                            }
                            rec_colors.remove(selected_element_index);

                            System.out.println(rec_points);
                            break;

                        case "squ":
                            pre_x1 = squ_points.get(selected_object.get(0));
                            pre_y1 = squ_points.get(selected_object.get(1));
                            pre_x2 = squ_points.get(selected_object.get(2));
                            pre_y2 = squ_points.get(selected_object.get(3));

                            System.out.println(squ_points);
                            cur_x1 = pre_x1 + move_x;
                            cur_y1 = pre_y1 + move_y;
                            cur_x2 = pre_x2 + move_x;
                            cur_y2 = pre_y2 + move_y;

                            squ_points.add(cur_x1);
                            squ_points.add(cur_y1);
                            squ_points.add(cur_x2);
                            squ_points.add(cur_y2);
                            squ_colors.add(squ_colors.get(selected_element_index));

                            System.out.println(selected_object);
                            begin_index = selected_object.get(0);

                            for (int i = 0; i < 4; i++) {
                                squ_points.remove(begin_index);
                            }
                            squ_colors.remove(selected_element_index);

                            System.out.println(rec_points);
                            break;

                        case "ell":
                            pre_x1 = ell_points.get(selected_object.get(0));
                            pre_y1 = ell_points.get(selected_object.get(1));
                            pre_x2 = ell_points.get(selected_object.get(2));
                            pre_y2 = ell_points.get(selected_object.get(3));

                            System.out.println(ell_points);
                            cur_x1 = pre_x1 + move_x;
                            cur_y1 = pre_y1 + move_y;
                            cur_x2 = pre_x2 + move_x;
                            cur_y2 = pre_y2 + move_y;

                            ell_points.add(cur_x1);
                            ell_points.add(cur_y1);
                            ell_points.add(cur_x2);
                            ell_points.add(cur_y2);
                            ell_colors.add(ell_colors.get(selected_element_index));

                            System.out.println(selected_object);
                            begin_index = selected_object.get(0);

                            for (int i = 0; i < 4; i++) {
                                ell_points.remove(begin_index);
                            }
                            ell_colors.remove(selected_element_index);

                            break;

                        case "cir":
                            pre_x1 = cir_points.get(selected_object.get(0));
                            pre_y1 = cir_points.get(selected_object.get(1));
                            pre_x2 = cir_points.get(selected_object.get(2));
                            pre_y2 = cir_points.get(selected_object.get(3));

                            System.out.println(cir_points);
                            cur_x1 = pre_x1 + move_x;
                            cur_y1 = pre_y1 + move_y;
                            cur_x2 = pre_x2 + move_x;
                            cur_y2 = pre_y2 + move_y;

                            cir_points.add(cur_x1);
                            cir_points.add(cur_y1);
                            cir_points.add(cur_x2);
                            cir_points.add(cur_y2);
                            cir_colors.add(cir_colors.get(selected_element_index));

                            System.out.println(selected_object);
                            begin_index = selected_object.get(0);

                            for (int i = 0; i < 4; i++) {
                                cir_points.remove(begin_index);
                            }
                            cir_colors.remove(selected_element_index);

                            break;

                        case "str":
                            pre_x1 = str_points.get(selected_object.get(0));
                            pre_y1 = str_points.get(selected_object.get(1));
                            pre_x2 = str_points.get(selected_object.get(2));
                            pre_y2 = str_points.get(selected_object.get(3));

                            System.out.println(str_points);
                            cur_x1 = pre_x1 + move_x;
                            cur_y1 = pre_y1 + move_y;
                            cur_x2 = pre_x2 + move_x;
                            cur_y2 = pre_y2 + move_y;

                            str_points.add(cur_x1);
                            str_points.add(cur_y1);
                            str_points.add(cur_x2);
                            str_points.add(cur_y2);
                            str_colors.add(str_colors.get(selected_element_index));

                            System.out.println(selected_object);
                            begin_index = selected_object.get(0);

                            for (int i = 0; i < 4; i++) {
                                str_points.remove(begin_index);
                            }
                            str_colors.remove(selected_element_index);

                            break;


                    }

                    //repaint
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, 1200, 800);
                    repaint_shapes("rec", rec_colors, rec_points);
                    repaint_shapes("squ", squ_colors, squ_points);
                    repaint_shapes("ell", ell_colors, ell_points);
                    repaint_shapes("cir", cir_colors, cir_points);
                    repaint_shapes("str", str_colors, str_points);

                    canvas.repaint();
                }


            }
        });



        col.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pen_color = JColorChooser.showDialog(main_frame, "choose a color", Color.BLACK);
                System.out.println(pen_color);
            }
        });

        p_menu.add(handdraw);
        p_menu.add(str);

        p_menu.add(rec);
        p_menu.add(squ);

        p_menu.add(ell);
        p_menu.add(cir);

        p_menu.add(poly);

        p_menu.add(load);
        p_menu.add(reload);

        p_operation.add(clear);
        p_operation.add(undo);
        p_operation.add(redo);
        p_operation.add(select);
        p_operation.add(cut);
        p_operation.add(move);
        p_operation.add(copy);
        p_operation.add(paste);
        p_operation.add(col);
        p_operation.add(save);


        p_menu.setPreferredSize(new Dimension(1200,40));
        main_frame.add(p_menu, BorderLayout.NORTH);
        main_frame.add(p_operation, BorderLayout.WEST);

        main_frame.add(canvas, BorderLayout.CENTER);
        main_frame.add(instruction, BorderLayout.SOUTH);
        main_frame.setVisible(true);
    }

    public int[] start_point(int x1, int y1, int x2, int y2){
        int[] start_point = new int[2];
        int width = x2 - x1;
        int height = y2 - y1;

        //consider 4 directions to draw a rectangle
        if(width < 0 && height < 0){
            start_point[0] = x2;
            start_point[1] = y2;

        }else if(width < 0 && height > 0){
            start_point[0] = x2;
            start_point[1] = y1;

        }else if(width > 0 && height <0){
            start_point[0] = x1;
            start_point[1] = y2;

        }else{
            start_point[0] = x1;
            start_point[1] = y1;

        }

        return start_point;

    }

    public void repaint_shapes(String shape, ArrayList<Color> shape_colors, ArrayList<Integer> shape_points){
        if(shape_colors.size()>0){
            //shape repaint
            for (int i = 0; i < shape_colors.size(); i++) {
                int begin_index = i * 4;
                x1 = shape_points.get(begin_index);
                y1 = shape_points.get(begin_index + 1);
                x2 = shape_points.get(begin_index + 2);
                y2 = shape_points.get(begin_index + 3);

                int[] start_points = start_point(x1, y1, x2, y2);

                g.setColor(shape_colors.get(i));

                if(shape.equals("rec")){
                    g.drawRect(start_points[0], start_points[1], Math.abs(x2-x1), Math.abs(y2-y1));
                }
                if(shape.equals("cir")){
                    g.drawOval(start_points[0], start_points[1], Math.abs(x2-x1), Math.abs(x2-x1));
                }
                if(shape.equals("squ")){
                    g.drawRect(start_points[0], start_points[1], Math.abs(x2-x1), Math.abs(x2-x1));
                }
                if(shape.equals("ell")){
                    g.drawOval(start_points[0], start_points[1], Math.abs(x2-x1), Math.abs(y2-y1));
                }
                if(shape.equals("poly")){

                }
                if(shape.equals("str")){
                    g.drawLine(x1, y1, x2, y2);
                }
                if(shape.equals("handdraw")){

                }


                canvas.repaint();
            }
        }
    }

    public ArrayList<Integer> get_separate_index(ArrayList<Integer> shape_points){
        ArrayList<Integer> separate_index = new ArrayList<Integer>();

        for (int i = 0; i < shape_points.size(); i++) {
            int p = shape_points.get(i);
            if(p == -1){
                separate_index.add(i);
            }
        }

        return separate_index;
    }

    public void search_in_shapes(String shape, ArrayList<Color> shape_colors, ArrayList<Integer> shape_points, String select_shape){

        for (int i = 0; i < shape_colors.size(); i++) {
            //compare each shape with the selection rectangle
            int index_x1 = i*4;
            int index_y1 = i*4 + 1;
            int index_x2 = i*4 + 2;
            int index_y2 = i*4 + 3;

            int shape_x1 = shape_points.get(index_x1);
            int shape_y1 = shape_points.get(index_y1);
            int shape_x2 = shape_points.get(index_x2);
            int shape_y2 = shape_points.get(index_y2);

            if(x1<shape_x1 && y1<shape_y1 && x2>shape_x2 && y2>shape_y2){
                g.setColor(Color.WHITE);
                g.fillRect(x1,y1,Math.abs(x2-x1), Math.abs(y2-y1));

                g.setColor(new Color(137,57,187,100));
                //repaint regular shapes
                switch (select_shape){
                    case "rec":
                        g.drawRect(shape_x1, shape_y1, Math.abs(shape_x2-shape_x1), Math.abs(shape_y2-shape_y1));
                        selected_shape = "rec";
                        selected_element_index = i;
                        break;
                    case "squ":
                        g.drawRect(shape_x1, shape_y1, Math.abs(shape_x2-shape_x1), Math.abs(shape_x2-shape_x1));
                        selected_shape = "squ";
                        selected_element_index = i;
                        break;

                    case "ell":
                        g.drawOval(shape_x1, shape_y1, Math.abs(shape_x2-shape_x1), Math.abs(shape_y2-shape_y1));
                        selected_shape = "ell";
                        selected_element_index = i;
                        break;

                    case "cir":
                        g.drawOval(shape_x1, shape_y1, Math.abs(shape_x2-shape_x1), Math.abs(shape_x2-shape_x1));
                        selected_shape = "cir";
                        selected_element_index = i;
                        break;

                    case "str":
                        g.drawLine(shape_x1, shape_y1, shape_x2, shape_y2);
                        selected_shape = "str";
                        selected_element_index = i;
                        break;
                }

                //repaint polygons


                //repaint freehand
                canvas.repaint();

                selected_object.add(index_x1);
                selected_object.add(index_y1);
                selected_object.add(index_x2);
                selected_object.add(index_y2);



                System.out.println("selected object is "+select_shape);
                System.out.println(selected_object + " object index");

            }
        }

        if(selected_object.size() > 4){
            for (int i = 0; i < selected_object.size()-4; i++) {
                selected_object.remove(0);
            }
        }
    }

    public void regular_shape_undo_process(String pop_shape, ArrayList<Integer> shape_points, ArrayList<Color> shape_colors){
        //recall rec points
        //1. store the last rectangle data
        //2. pop the last rectangle

        //for loop
        for (int i = 0; i < shape_colors.size(); i++) {
            System.out.println(shape_colors.size());
            int begin_index = i * 4;
            x1 = shape_points.get(begin_index);
            y1 = shape_points.get(begin_index + 1);
            x2 = shape_points.get(begin_index + 2);
            y2 = shape_points.get(begin_index + 3);
            int[] start_points = start_point(x1, y1, x2, y2);
            //last one
            if (i == (shape_colors.size() - 1)) {
                //store points and shape

                popped_points.add(x1);
                popped_points.add(y1);
                popped_points.add(x2);
                popped_points.add(y2);

                popped_shape = pop_shape;

                //popped the last shape
                System.out.println(progress_stack + " is the previous stack");
                progress_stack.remove(progress_stack.size() - 1);
                System.out.println(progress_stack + " is the current stack");

                //store the popped color
                popped_color = shape_colors.get(shape_colors.size() - 1);

                switch (pop_shape) {
                    case "rec":
                        //pop last 4 points from rec_points
                        if (shape_colors.size() == 1) {
                            //last element in shape stack
                            rec_points.clear();
                        } else {
                            //pop last 4 points from cir_points
//                            rec_points.remove(rec_points.size() - 1);
//                            rec_points.remove(rec_points.size() - 2);
//                            rec_points.remove(rec_points.size() - 3);
//                            rec_points.remove(rec_points.size() - 4);
                            for (int j = 0; j < 4; j++) {
                                rec_points.remove(rec_points.size()-1);
                            }
                            System.out.println(rec_points + " is the current rec_points");
                        }
                        //pop the last color
                        rec_colors.remove(rec_colors.size() - 1);

                        System.out.println(rec_points+ "----------------------");
                        System.out.println(popped_points + "----------------------");

                        break;

                    case "cir":
                        //pop last 4 points from cir_points
                        if (shape_colors.size() == 1) {
                            //last element in shape stack
                            cir_points.clear();
                        } else {
                            //pop last 4 points from cir_points
                            for (int j = 0; j < 4; j++) {
                                cir_points.remove(cir_points.size()-1);
                            }
                            System.out.println(cir_points + " is the current rec_points");
                        }
                        //pop the last color
                        cir_colors.remove(cir_colors.size() - 1);
                        break;

                    case "squ":
                        //pop last 4 points from squ_points
                        if (shape_colors.size() == 1) {
                            //last element in shape stack
                            squ_points.clear();
                        } else {
                            //pop last 4 points from squ_points
                            for (int j = 0; j < 4; j++) {
                                squ_points.remove(squ_points.size()-1);
                            }
                            System.out.println(squ_points + " is the current rec_points");
                        }
                        //pop the last color
                        squ_colors.remove(squ_colors.size() - 1);
                        break;

                    case "ell":
                        //pop last 4 points from ell_points
                        if (shape_colors.size() == 1) {
                            //last element in shape stack
                            ell_points.clear();
                        } else {
                            //pop last 4 points from squ_points
                            for (int j = 0; j < 4; j++) {
                                ell_points.remove(ell_points.size()-1);
                            }
                            System.out.println(ell_points + " is the current rec_points");
                        }
                        //pop the last color
                        ell_colors.remove(ell_colors.size() - 1);
                        break;

                    case "str":
                        //pop last 4 points from str_points
                        if (shape_colors.size() == 1) {
                            //last element in shape stack
                            str_points.clear();
                        } else {
                            //pop last 4 points from str_points
                            for (int j = 0; j < 4; j++) {
                                str_points.remove(str_points.size()-1);
                            }
                            System.out.println(str_points + " is the current rec_points");
                        }
                        //pop the last color
                        str_colors.remove(str_colors.size() - 1);
                        break;


                }


            } else {
                //repaint other elements in that shape stack
                switch (pop_shape) {
                    case "rec":
                        g.setColor(shape_colors.get(i));
                        g.drawRect(start_points[0], start_points[1], Math.abs(x2 - x1), Math.abs(y2 - y1));
                        canvas.repaint();
                        break;
                    case "cir":
                        g.setColor(shape_colors.get(i));
                        g.drawOval(start_points[0], start_points[1], Math.abs(x2 - x1), Math.abs(x2 - x1));
                        canvas.repaint();
                        break;
                    case "squ":
                        g.setColor(shape_colors.get(i));
                        g.drawRect(start_points[0], start_points[1], Math.abs(x2 - x1), Math.abs(x2 - x1));
                        canvas.repaint();
                        break;
                    case "ell":
                        g.setColor(shape_colors.get(i));
                        g.drawOval(start_points[0], start_points[1], Math.abs(x2 - x1), Math.abs(y2 - y1));
                        canvas.repaint();
                        break;
                    case "str":
                        g.setColor(shape_colors.get(i));
                        g.drawLine(x1, y1, x2, y2);
                        canvas.repaint();
                        break;
                }
            }
        }


    }



    public static void main(String[] args) {
        new MyFrame();
    }
}
