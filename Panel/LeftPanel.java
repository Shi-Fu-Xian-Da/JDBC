package 报告1.test10.Panel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;

import static 报告1.test10.MyFrame.readData;
import static 报告1.test10.MyFrame.refreshJSP;
import static 报告1.test10.Main.sample;
import static 报告1.test10.MyFrame.*;

public class LeftPanel extends Panel {
    String[] column=new String[]{"EMPNO   ","FIRSTNME","MIDINIT ","LASTNAME","  WORKDEPT","PHONENO ",
            "HIREDATE","JOB     ","EDLEVEL ","SEX     ","BIRTHDATE","SALARY  ","BONUS   ","COMM    "};
    String col="EMPNO,FIRSTNME,MIDINIT,LASTNAME,WORKDEPT,PHONENO,HIREDATE,JOB,EDLEVEL,SEX,BIRTHDATE,SALARY,BONUS,COMM";

    //插入一行
    public void createJFrame1(){
        JFrame frame = new JFrame("修改一行");
        frame.setBounds(500,250,400,600);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        JLabel[] jl=new JLabel[14];
        JTextField[] jt=new JTextField[14];
        for(int i=0;i<14;i++){
            jl[i]=new JLabel(column[i]);
        }
        for(int i=0;i<14;i++){
            jt[i]=new JTextField(15);
        }

        Box vBox=Box.createVerticalBox();
        Box[]hBox=new Box[14];
        for(int i=0;i<14;i++){
            hBox[i]=Box.createHorizontalBox();
            hBox[i].add(jl[i]);
            hBox[i].add(Box.createHorizontalStrut(10));
            hBox[i].add(jt[i]);
            vBox.add(hBox[i]);
            vBox.add(Box.createVerticalStrut(10));
        }
        frame.add(vBox);
        //按键
        Box buttonBox=Box.createHorizontalBox();
        JButton sure = new JButton("确认插入");
        JButton randomCreate = new JButton("随机产生");
        buttonBox.add(sure);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(randomCreate);
        vBox.add(buttonBox);

        frame.setVisible(true);

        sure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String sql = "INSERT INTO student.TEMPL  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement statement = sample.prepareStatement(sql);
                    statement.setString(1,jt[0].getText());
                    //如果负数
                    if(Integer.parseInt(jt[0].getText())<0){
                        throw new Exception("1");
                    }
                    statement.setString(2,jt[1].getText());
                    //如果未输入
                    if(jt[1].getText().trim().isEmpty()){
                        throw new Exception("2");
                    }
                    statement.setString(3,jt[2].getText());
                    statement.setString(4,jt[3].getText());
                    //如果未输入
                    if(jt[3].getText().trim().isEmpty()){
                        throw new Exception("2");
                    }
                    statement.setString(5,jt[4].getText());
                    statement.setInt(6,Integer.parseInt(jt[5].getText()));
                    //如果"PHONENO"负数
                    if(Integer.parseInt(jt[5].getText())<0){
                        throw new Exception("3");
                    }
                    statement.setDate(7, Date.valueOf(jt[6].getText()));
                    statement.setString(8,jt[7].getText());
                    statement.setShort(9,Short.parseShort(jt[8].getText()));
                    statement.setString(10,jt[9].getText());
                    statement.setDate(11,Date.valueOf(jt[10].getText()));
                    statement.setBigDecimal(12, BigDecimal.valueOf(Long.parseLong(jt[11].getText())));
                    statement.setBigDecimal(13,BigDecimal.valueOf(Long.parseLong(jt[12].getText())));
                    statement.setBigDecimal(14,BigDecimal.valueOf(Long.parseLong(jt[13].getText())));
                    int updateCount = statement.executeUpdate();
                    if(updateCount == 1){
                        readData();
                        refreshJSP();
                        JOptionPane.showMessageDialog(null,"插入成功","55200717石奂哲", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"插入失败！", "55200717石奂哲",JOptionPane.ERROR_MESSAGE);
                    }
                    statement.close();
                    frame.setVisible(false);
                }
                catch (SQLException x) {
                    // ERRORCODE=-803,SQLSTATE=23505 不能插入行，因为这将违反唯一索引的约束
                    //EMPNO重复
                    if(x.getSQLState().equals("23505")){
                        x.printStackTrace();
                        JOptionPane.showMessageDialog(null,"主键重复！");
                    }
                    //ERRORCODE=-4461, SQLSTATE=42815，溢出
                    //EDLEVEL>32767
                    else if(x.getSQLState().equals("42815")){
                        x.printStackTrace();
                        JOptionPane.showMessageDialog(null,"数值超出范围！");
                    }
                    //ERRORCODE=-302, SQLSTATE=22001 插入的数据时的长度和数据库中自定义的长度不匹配或超出限制
                    //MIDINIT超过两个字符
                    else if(x.getSQLState().equals("22001")){
                        x.printStackTrace();
                        JOptionPane.showMessageDialog(null,"输入的字符串过长！");
                    }
                    //ERRORCODE=-4463, SQLSTATE=42601
                    //乱输入一同
                    else if(x.getSQLState().equals("42601")){
                        x.printStackTrace();
                        JOptionPane.showMessageDialog(null,"输入的转义语法错误！");
                    }
                    //SQLCODE=-204, SQLSTATE=42704
                    //没加JLU
                    else if(x.getSQLState().equals("42704")){
                        x.printStackTrace();
                        JOptionPane.showMessageDialog(null,"EMPLOYEE不存在！");
                    }
                    else{
                        x.printStackTrace();
                    }
                    int SQLCode = x.getErrorCode();
                }
                catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null,"类型不匹配！");
                }
                catch (Exception ee){
                    System.out.println(ee.getMessage());
                    if(ee.getMessage().equalsIgnoreCase("1"))JOptionPane.showMessageDialog(null,"EMPNO不能为负数！");
                    else if(ee.getMessage().equalsIgnoreCase("2"))JOptionPane.showMessageDialog(null,"未输入姓名！");
                    else if(ee.getMessage().equalsIgnoreCase("3"))JOptionPane.showMessageDialog(null,"EDLEVE:不能为负数！");
                    else JOptionPane.showMessageDialog(null,"可能违反了表的约束！");
                }
            }
        });
        randomCreate.addActionListener(e -> {
            Random r = new Random();
            jt[0].setText(String.valueOf(r.nextInt(9999)));
            jt[5].setText(String.valueOf(r.nextInt(9999)));
            jt[8].setText(String.valueOf(r.nextInt(9999)));

            java.util.Date date = new java.util.Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            jt[6].setText(formatter.format(date));
            jt[10].setText(formatter.format(date));

            jt[11].setText(String.valueOf(r.nextInt(9999)));
            jt[12].setText(String.valueOf(r.nextInt(9999)));
            jt[13].setText(String.valueOf(r.nextInt(9999)));
        });
    }
    //插入多行
    public void createJFrame2(){
        int insertNum = 0;//插入行数
        String s = null;
        s = JOptionPane.showInputDialog("请输入要插入的行数: \n ");
        insertNum = Integer.parseInt(s);
        //TODO 插入多行窗口
        JFrame frame = new JFrame("插入多行");
        frame.setBounds(10,200,1300,(insertNum)*80);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        Box vBox=Box.createVerticalBox();
        Box [] hBoxContext=new Box[insertNum];
        Box hBoxTitle=Box.createHorizontalBox();
        for(int i=0;i<insertNum;i++){
            hBoxContext[i]=Box.createHorizontalBox();
        }
        //title
        JLabel [] attribute=new JLabel[14];
        for(int i=0;i<14;i++){
            attribute[i]=new JLabel(column[i]);
        }
        hBoxTitle.add(Box.createHorizontalStrut(80));
        for(int i=0;i<14;i++){
            hBoxTitle.add(attribute[i]);
            if(i==3)hBoxTitle.add(Box.createHorizontalStrut(8));
            else hBoxTitle.add(Box.createHorizontalStrut(30));
        }
        //context
        JLabel[] labels = new JLabel[insertNum];
        for(int i=0;i<insertNum;i++){
            labels[i] = new JLabel("请输入第"+(i+1)+"行");
        }
        JTextField[][] jt = new JTextField[insertNum][14];
        for(int i=0;i<insertNum;i++){
            hBoxContext[i].add(labels[i]);
            for(int j = 0;j<14;j++){
                jt[i][j] = new JTextField(5);
                hBoxContext[i].add(jt[i][j]);
            }
        }
        vBox.add(hBoxTitle);
        for(int i=0;i<insertNum;i++){
            vBox.add(hBoxContext[i]);
        }
        frame.add(vBox);
        //按键
        Box buttonBox=Box.createHorizontalBox();
        JButton sure = new JButton("确认插入");
        JButton randomCreate = new JButton("随机产生");
        buttonBox.add(sure);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(randomCreate);
        vBox.add(buttonBox);
        frame.setVisible(true);

        sure.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for(int i=0;i<jt.length;i++){
                    try{
                        String sql = "insert into student.TEMPL VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        PreparedStatement statement = sample.prepareStatement(sql);
                        statement.setString(1,jt[i][0].getText());
                        //如果负数
                        if(Integer.parseInt(jt[i][0].getText())<0){
                            throw new Exception("1");
                        }
                        statement.setString(2,jt[i][1].getText());
                        //如果未输入
                        if(jt[i][1].getText().trim().isEmpty()){
                            throw new Exception("2");
                        }
                        statement.setString(3,jt[i][2].getText());
                        statement.setString(4,jt[i][3].getText());
                        //如果未输入
                        if(jt[i][3].getText().trim().isEmpty()){
                            throw new Exception("2");
                        }
                        statement.setString(5,jt[i][4].getText());
                        statement.setInt(6,Integer.parseInt(jt[i][5].getText()));
                        //如果"PHONENO"负数
                        if(Integer.parseInt(jt[i][5].getText())<0){
                            throw new Exception("3");
                        }
                        statement.setDate(7, Date.valueOf(jt[i][6].getText()));
                        statement.setString(8, jt[i][7].getText());
                        statement.setShort(9, Short.parseShort(jt[i][8].getText()));
                        statement.setString(10, jt[i][9].getText());
                        statement.setDate(11, Date.valueOf(jt[i][10].getText()));
                        statement.setBigDecimal(12, BigDecimal.valueOf(Long.parseLong(jt[i][11].getText())));
                        statement.setBigDecimal(13, BigDecimal.valueOf(Long.parseLong(jt[i][12].getText())));
                        statement.setBigDecimal(14, BigDecimal.valueOf(Long.parseLong(jt[i][13].getText())));
                        int updateCount = statement.executeUpdate();
                        if(updateCount == 1){
                            readData();
                            refreshJSP();
                            JOptionPane.showMessageDialog(null,"插入成功","55200717石奂哲", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"插入失败！", "55200717石奂哲",JOptionPane.ERROR_MESSAGE);
                        }
                        statement.close();
                        frame.setVisible(false);
                    }
                    catch (SQLException x) {
                        // ERRORCODE=-803,SQLSTATE=23505 不能插入行，因为这将违反唯一索引的约束
                        //EMPNO重复
                        if(x.getSQLState().equals("23505")){
                            x.printStackTrace();
                            JOptionPane.showMessageDialog(null,"主键重复！");
                        }
                        //ERRORCODE=-4461, SQLSTATE=42815，溢出
                        //EDLEVEL>32767
                        else if(x.getSQLState().equals("42815")){
                            x.printStackTrace();
                            JOptionPane.showMessageDialog(null,"数值超出范围！");
                        }
                        //ERRORCODE=-302, SQLSTATE=22001 插入的数据时的长度和数据库中自定义的长度不匹配或超出限制
                        //MIDINIT超过两个字符
                        else if(x.getSQLState().equals("22001")){
                            x.printStackTrace();
                            JOptionPane.showMessageDialog(null,"输入的字符串过长！");
                        }
                        //ERRORCODE=-4463, SQLSTATE=42601
                        //乱输入一同
                        else if(x.getSQLState().equals("42601")){
                            x.printStackTrace();
                            JOptionPane.showMessageDialog(null,"输入的转义语法错误！");
                        }
                        //SQLCODE=-204, SQLSTATE=42704
                        //没加JLU
                        else if(x.getSQLState().equals("42704")){
                            x.printStackTrace();
                            JOptionPane.showMessageDialog(null,"EMPLOYEE不存在！");
                        }
                        else{
                            x.printStackTrace();
                        }
                        int SQLCode = x.getErrorCode();
                    }
                    catch (NumberFormatException ne) {
                        JOptionPane.showMessageDialog(null,"类型不匹配！");
                    }
                    catch (Exception ee){
                        System.out.println(ee.getMessage());
                        if(ee.getMessage().equalsIgnoreCase("1"))JOptionPane.showMessageDialog(null,"EMPNO不能为负数！");
                        else if(ee.getMessage().equalsIgnoreCase("2"))JOptionPane.showMessageDialog(null,"未输入姓名！");
                        else if(ee.getMessage().equalsIgnoreCase("3"))JOptionPane.showMessageDialog(null,"EDLEVE:不能为负数！");
                        else JOptionPane.showMessageDialog(null,"可能违反了表的约束！");
                    }
                }
            }
        });
        randomCreate.addActionListener(e -> {
            Random r = new Random();
            java.util.Date date = new java.util.Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for(int i=0;i<jt.length;i++){
                jt[i][0].setText(String.valueOf(r.nextInt(9999)));
                jt[i][5].setText(String.valueOf(r.nextInt(9999)));
                jt[i][8].setText(String.valueOf(r.nextInt(9999)));

                jt[i][6].setText(formatter.format(date));
                jt[i][10].setText(formatter.format(date));

                jt[i][11].setText(String.valueOf(r.nextInt(9999)));
                jt[i][12].setText(String.valueOf(r.nextInt(9999)));
                jt[i][13].setText(String.valueOf(r.nextInt(9999)));
            }

        });
    }
    //通过子查询插入
    //select * from JLU.employee where empno=00020
    public void createJFrame3(){
        Statement st = null;
        ResultSet res = null;
        String s = JOptionPane.showInputDialog("请输入子查询语句:");

        try {
            st = sample.createStatement();
            st.executeUpdate("insert into STUDENT.TEMPL " +
                    "(EMPNO,FIRSTNME,MIDINIT,LASTNAME,WORKDEPT,PHONENO,HIREDATE,JOB,EDLEVEL,SEX,BIRTHDATE,SALARY,BONUS,COMM)"
                    + s);
        } catch (SQLException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog( null ,"子查询语句有误！","名字 学号", JOptionPane.ERROR_MESSAGE);
            return;
        }
        readData();
        refreshJSP();
        return;
    }
    //修改某一行
    public void createJFrame4(){
        PreparedStatement statement1 = null;
        ResultSet res = null;
        String inputNo = JOptionPane.showInputDialog("请输入要修改行的EMPNO值:");
        //显示修改界面
        JFrame tjf = new JFrame("修改一行");
        tjf.setBounds(500,250,400,600);
        JLabel[] jl=new JLabel[14];
        JTextField[] jt=new JTextField[14];
        try {
            String sql = "select "+col+" from student.TEMPL where EMPNO=?";
            statement1 = sample.prepareStatement(sql);
            statement1.setString(1,inputNo);
            //结果集
            ResultSet rs=statement1.executeQuery();
            rs.next();
            for(int i=0;i<14;i++){
                jl[i]=new JLabel(column[i]);
            }
            for(int i=0;i<14;i++){
                jt[i]=new JTextField(rs.getString(i+1),15);
            }
            Box vBox=Box.createVerticalBox();
            Box[]hBox=new Box[14];
            for(int i=0;i<14;i++){
                hBox[i]=Box.createHorizontalBox();
                hBox[i].add(jl[i]);
                hBox[i].add(Box.createHorizontalStrut(10));
                hBox[i].add(jt[i]);
                vBox.add(hBox[i]);
                vBox.add(Box.createVerticalStrut(10));
            }
            JButton sure = new JButton("确认修改");
            vBox.add(sure);
            tjf.add(vBox);

            sure.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try{
                        String col2="";
                        String sql="update student.TEMPL set " +
                                "EMPNO=?,FIRSTNME=?,MIDINIT=?,LASTNAME=?,WORKDEPT=?,PHONENO=?,HIREDATE=?,JOB=?,EDLEVEL=?,SEX=?,BIRTHDATE=?,SALARY=?,BONUS=?,COMM=? " +
                                "where EMPNO=?";
                        PreparedStatement statement1 = null;
                        statement1 = sample.prepareStatement(sql);
                        for(int i=0;i<6;i++){
                            statement1.setString(i+1,jt[i].getText());
                        }
                        statement1.setDate(7, Date.valueOf(jt[6].getText()));
                        statement1.setString(8,jt[7].getText());
                        statement1.setInt(9,Integer.parseInt(jt[8].getText()));
                        statement1.setString(10,jt[9].getText());
                        statement1.setDate(11,Date.valueOf(jt[10].getText()));
                        statement1.setDouble(12,Double.valueOf(jt[11].getText()));
                        statement1.setDouble(13,Double.valueOf(jt[12].getText()));
                        statement1.setDouble(14,Double.valueOf(jt[13].getText()));

                        statement1.setString(15,inputNo);
                        int updateCount=statement1.executeUpdate();
                        if(updateCount==1){
                            JOptionPane.showMessageDialog( null ,"更新成功","55200717石奂哲", JOptionPane.INFORMATION_MESSAGE);
                            readData();
                            refreshJSP();
                        }
                    }
                    catch (SQLException e1){
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog( null ,"更新表出现误","55200717石奂哲", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    return;
                }
            });
        } catch (SQLException e1) {
            e1.printStackTrace();
            if(e1.getErrorCode()==-4470)
                JOptionPane.showMessageDialog( null ,"输入EMPNO有误，请重新输入！","55200717石奂哲", JOptionPane.ERROR_MESSAGE);
            //点击取消/空
            else if(e1.getSQLState().equals("42815")){
                return;
            }
            return;
        }
        tjf.setVisible(true);
    }
    //修改某一列
    public void createJFrame5(){
        JFrame tjf = new JFrame("修改一列");
        tjf.setBounds(500,250,400,150);

        JLabel[] jl=new JLabel[2];
        jl[0]=new JLabel("请输入要改变的列");
        jl[1]=new JLabel("请输入要改变的值");
        JTextField[] jt=new JTextField[2];
        for (int i = 0; i < 2; i++) {
            jt[i]=new JTextField(15);
        }
        Box vBox=Box.createVerticalBox();
        Box []hBox=new Box[2];
        for (int i = 0; i < 2; i++) {
            hBox[i]=Box.createHorizontalBox();
            hBox[i].add(jl[i]);
            hBox[i].add(Box.createHorizontalStrut(10));
            hBox[i].add(jt[i]);
            vBox.add(hBox[i]);
            vBox.add(Box.createVerticalStrut(10));
        }
        tjf.add(vBox);
        JButton sure = new JButton("确认修改");
        vBox.add(sure);
        tjf.setVisible(true);

        sure.addActionListener(e -> {
            /*String[] column=new String[]{"EMPNO","FIRSTNME","MIDINIT","LASTNAME","WORKDEPT","PHONENO",
                    "HIREDATE","JOB","EDLEVEL","SEX","BIRTHDATE","SALARY","BONUS","COMM"};*/

            //获取改变的列
            Integer col=Integer.parseInt(jt[0].getText());
            String sql="update student.TEMPL set "+column[col-1]+"=?";
            try{
                PreparedStatement statement = sample.prepareStatement(sql);
                switch (col){
                    case 7:
                    case 11:
                        statement.setDate(1,Date.valueOf(jt[1].getText()));
                        break;
                    case 9:
                        statement.setInt(1,Integer.parseInt(jt[1].getText()));
                        break;
                    case 12:
                    case 13:
                    case 14:
                        statement.setDouble(1,Double.valueOf(jt[1].getText()));
                        break;
                    default://1-6,8,10
                        statement.setString(1,jt[1].getText());
                        break;
                }
                int updateCount=statement.executeUpdate();
                System.out.println("执行成功");
                JOptionPane.showMessageDialog( null ,"修改成功，修改"+updateCount+"行","55200717石奂哲", JOptionPane.INFORMATION_MESSAGE);
                readData();
                refreshJSP();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
    //查询图片
    public void createJFrame6(){
        JFrame tjf = new JFrame("表emp_photo的的查询");
        tjf.setBounds(500,200,350,300);
        tjf.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        //标签
        JLabel[] jl=new JLabel[2];
        jl[0] = new JLabel("EMPNO:");
        jl[1] = new JLabel("PHOTO_FORMAT:");
        //输入框
        JTextField[] jt=new JTextField[2];
        jt[0] = new JTextField(15);
        jt[1] = new JTextField(15);
        Box vBox=Box.createVerticalBox();
        Box[]hBox=new Box[2];

        for(int i=0;i<2;i++){
            hBox[i]=Box.createHorizontalBox();
            hBox[i].add(jl[i]);
            hBox[i].add(Box.createHorizontalStrut(10));
            hBox[i].add(jt[i]);
            vBox.add(hBox[i]);
            vBox.add(Box.createVerticalStrut(10));
        }
        JButton tjb = new JButton("查询");
        vBox.add(tjb);
        tjf.add(vBox);
        tjf.setVisible(true);

        tjb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String sql="select picture from jlu.emp_photo where EMPNO=? and PHOTO_FORMAT=?";
                ResultSet rs = null;
                try {
                    PreparedStatement ps = sample.prepareStatement(sql);
                    for(int i=0;i<2;i++){
                        ps.setString(i+1,jt[i].getText());
                    }
                    rs=ps.executeQuery();
                    boolean flag=false;
                    while(rs.next()){
                        Blob blob= (Blob) rs.getBlob(1);
                        InputStream inputStream =blob.getBinaryStream();
                        File fileOutput = new File("D:\\myCode\\my_java\\数据库应用开发\\项目4\\src\\报告1\\test10\\image\\a.jpg");
                        FileOutputStream fo = new FileOutputStream(fileOutput);
                        BufferedOutputStream bo=new BufferedOutputStream(fo);
                        int cinPicture;
                        while ((cinPicture = inputStream.read()) != -1)
                            bo.write(cinPicture);
                        //流的关闭:
                        inputStream.close();
                        bo.close();
                        flag=true;
                    }

                    if(flag){
                        String url="D:\\myCode\\my_java\\数据库应用开发\\项目4\\src\\报告1\\test10\\image\\a.jpg";
                        RightPicPanel lp=new RightPicPanel(url);
                        jsp.setRightComponent(lp);
                    }
                    else{
                        JOptionPane.showMessageDialog( null ,"未查询到图片！","55200717石奂哲", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog( null ,"查询语句有误！","55200717石奂哲", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                return;
            }
        });
        return;
    }
    //插入图片
    public void createJFrame7(){
        JFrame tjf = new JFrame("表emp_photo的插入");
        tjf.setBounds(500,200,350,300);
        tjf.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        //标签
        JLabel[] jl=new JLabel[3];
        jl[0] = new JLabel("EMPNO:");
        jl[1] = new JLabel("PHOTO_FORMAT:");
        jl[2] = new JLabel("PICTURE:");
        //输入框
        JTextField[] jt=new JTextField[3];
        jt[0] = new JTextField(15);
        jt[1] = new JTextField(15);
        jt[2] = new JTextField(15);
        Box vBox=Box.createVerticalBox();
        Box[]hBox=new Box[3];
        for(int i=0;i<3;i++){
            hBox[i]=Box.createHorizontalBox();
            hBox[i].add(jl[i]);
            hBox[i].add(Box.createHorizontalStrut(10));
            hBox[i].add(jt[i]);
            vBox.add(hBox[i]);
            vBox.add(Box.createVerticalStrut(10));
        }
        JButton buttonInsert = new JButton("插入");
        Box hBoxButton=Box.createHorizontalBox();
        hBoxButton.add(Box.createHorizontalStrut(10));
        hBoxButton.add(buttonInsert);
        vBox.add(hBoxButton);
        tjf.add(vBox);
        tjf.setVisible(true);
        //插入
        buttonInsert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql="insert into JLU.emp_photo values(?,?,?)";
                try {
                    PreparedStatement ps = sample.prepareStatement(sql);
                    for(int i=0;i<2;i++){
                        ps.setString(i+1,jt[i].getText());
                    }
                    //显示图片
                    String url=jt[2].getText();
                    RightPicPanel lp=new RightPicPanel(url);
                    jsp.setRightComponent(lp);

                    File file=new File(jt[2].getText());
                    BufferedInputStream imageInput = new BufferedInputStream(new FileInputStream(file));
                    ps.setBinaryStream(3, imageInput,(int) file.length());
                    int updateCount=ps.executeUpdate();

                    if(updateCount==1){
                        JOptionPane.showMessageDialog( null ,"插入成功","55200717石奂哲", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog( null ,"插入失败","55200717石奂哲", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                    JOptionPane.showMessageDialog( null ,"违反表的约束/插入语句有误！","55200717石奂哲", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        });
        return;
    }

    public LeftPanel(){
        Box vBox=Box.createVerticalBox();
        //TODO JLabel
        vBox.add(Box.createVerticalStrut(10));
        JLabel jl1 = new JLabel("数据库名：TEMPL");
        JLabel jl2 = new JLabel("请使用操作来完成更新。");
        vBox.add(jl1);vBox.add(Box.createVerticalStrut(10));
        vBox.add(jl2);vBox.add(Box.createVerticalStrut(20));
        //TODO JButton
        JButton jb1 = new JButton("   插入一行  ");
        JButton jb2 = new JButton("   插入多行   ");
        JButton jb3 = new JButton("通过子查询插入");
        JButton jb4 = new JButton("   修改某一行  ");
        JButton jb5 = new JButton("   修改某一列  ");

        JButton jb6 = new JButton("   图片查询  ");
        JButton jb7 = new JButton("   图片插入  ");
        JButton jb8 = new JButton("    提交    ");
        vBox.add(jb1);vBox.add(Box.createVerticalStrut(10));
        vBox.add(jb2);vBox.add(Box.createVerticalStrut(10));
        vBox.add(jb3);vBox.add(Box.createVerticalStrut(10));
        vBox.add(jb4);vBox.add(Box.createVerticalStrut(10));
        vBox.add(jb5);vBox.add(Box.createVerticalStrut(10));
        vBox.add(jb6);vBox.add(Box.createVerticalStrut(10));
        vBox.add(jb7);vBox.add(Box.createVerticalStrut(10));
        vBox.add(jb8);vBox.add(Box.createVerticalStrut(10));

        jb1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                createJFrame1();
            }
        });
        jb2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createJFrame2();
            }
        });
        jb3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createJFrame3();
            }
        });
        jb4.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                createJFrame4();
            }
        });
        jb5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createJFrame5();
            }
        });
        jb6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createJFrame6();
            }
        });
        jb7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createJFrame7();
            }
        });

        jb8.addActionListener(new ActionListener() {//提交按钮
            public void actionPerformed(ActionEvent e) {
                try{
                    sample.commit();
                } catch (Exception ee){
                    ee.printStackTrace();
                }
            }
        });
        this.add(vBox,BorderLayout.NORTH);
    }
}
