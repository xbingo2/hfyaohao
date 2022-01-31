package com.cn.xbingo;

import com.alibaba.excel.util.StringUtils;
import com.cn.xbingo.handler.AbstractHandler;
import com.cn.xbingo.handler.impl.JgHandler;
import com.cn.xbingo.handler.impl.YaoHaoHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SwingFrame {

    private JFrame frame;

    private JPanel panel = new JPanel();

    private Map<String, String> labelMap = new LinkedHashMap<>();

    private Map<String, JTextField> jTextFieldMap = new LinkedHashMap<>();

    private JTextArea logTextArea = new JTextArea();

    private JScrollPane jsp = new JScrollPane(logTextArea);

    private ButtonGroup typeGroup;

    private Map<String, AbstractHandler> handlerMap = new HashMap<>();

    private int labelWidth = 100,
            labelX = 10,
            labelStartY = 20,
            height = 25,
            textWidth = 320,
            textX = 120,
            textColumns = 20,
            padding = 30;

    public SwingFrame(String title) {
        // 初始化各页面组件
        initFrame(title);
        initHandler();
        initPane();
        initLabelMap();
        initLogTextArea();
        initButton();

        OutputStream textAreaStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                logTextArea.append(String.valueOf((char)b));
                logTextArea.paintImmediately(logTextArea.getBounds());
                jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
            }
            @Override
            public void write(byte b[]) throws IOException {
                logTextArea.append(new String(b));
                logTextArea.paintImmediately(logTextArea.getBounds());
                jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
            }
            @Override
            public void write(byte b[], int off, int len) throws IOException {
                logTextArea.append(new String(b, off, len));
                logTextArea.paintImmediately(logTextArea.getBounds());
                jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
            }
        };
        PrintStream myOut = new PrintStream(textAreaStream);
        System.setOut(myOut);
        System.setErr(myOut);
    }

    public void initHandler() {
        handlerMap.put("yh", new YaoHaoHandler());
        handlerMap.put("jg", new JgHandler());
    }

    public JFrame getFrame() {
        return frame;
    }

    public void initFrame(String title) {
        frame = new JFrame(title);
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initPane() {
        //设置布局为null，通过绝对定位来控制布局
        panel.setLayout(null);
        frame.add(panel);
    }

    public void initLabelMap() {
        JLabel label = new JLabel("项目id:");
        label.setBounds(labelX, labelStartY, labelWidth, height);
        panel.add(label);

        JTextField text = new JTextField(textColumns);
        text.setBounds(textX, labelStartY, textWidth, height);
        panel.add(text);
        jTextFieldMap.put("id", text);

        JLabel type = new JLabel("抓取种类:");
        type.setBounds(labelX, labelStartY + padding, labelWidth, height);
        panel.add(type);
        JRadioButton yh = new JRadioButton("合肥摇号",true);
        yh.setBounds(textX, labelStartY + padding, labelWidth, height);
        yh.setActionCommand("yh");
        JRadioButton jg = new JRadioButton("价格公示");
        jg.setBounds(textX + labelWidth, labelStartY + padding, labelWidth, height);
        jg.setActionCommand("jg");
        ButtonGroup group = new ButtonGroup();
        group.add(yh);
        group.add(jg);
        typeGroup = group;
        panel.add(yh);
        panel.add(jg);
    }

    public void initLogTextArea() {
        //日志输出框
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(true);
        logTextArea.setFont(new Font("微软雅黑",Font.PLAIN,15));
        jsp.setBounds(labelX, labelStartY + (labelMap.size()+3) * padding, 660, 530);
        panel.add(jsp);
    }

    public void initButton() {
        // 创建全部生成按钮
        JButton allButton = new JButton("抓取");
        allButton.setBounds(labelX, labelStartY + 2 * padding, labelWidth, height);
        allButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String id = jTextFieldMap.get("id").getText();
                if (StringUtils.isBlank(id)) {
                    System.out.println("请输入项目id");
                    return;
                }
                String type = typeGroup.getSelection().getActionCommand();

                Thread thread = new Thread("handler"){
                    @Override
                    public  void run(){
                        System.out.println("抓取中");
                        handlerMap.get(type).hanlder(id);
                        System.out.println("抓取完成");
                    }
                };
                thread.start();
            }
        });
        panel.add(allButton);
    }

    public static void main(String[] args) {
        SwingFrame swingFrame = new SwingFrame("合肥摇号抓取工具");
        swingFrame.getFrame().setLocationRelativeTo(null);
        swingFrame.getFrame().setVisible(true);
    }

}
