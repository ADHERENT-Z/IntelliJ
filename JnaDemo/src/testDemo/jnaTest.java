package testDemo;

import com.dmt.smutil.SMUTIL;
import com.sam.sdticreader.WltDec;
import com.sun.jna.ptr.IntByReference;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class jnaTest {

    private byte[] pucCHMsg = new byte[256];
    private IntByReference puiCHMsgLen = new IntByReference();
    private byte[] pucPHMsg = new byte[1024];
    private IntByReference puiPHMsgLen = new IntByReference();
    private byte[] pucManaInfo = new byte[4];
    private byte[] pucManaMsg = new byte[8];

    private int portnum = 1001;
    private static jnaTest jnaTestDemo;
    private JFrame mainFrame;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JTextArea commentTextArea;
    private JScrollPane scrollPane;

    private baseInfoMsg basemsg = new baseInfoMsg();
    private static WltDec decoder;

    public jnaTest() {
        prepareGui();
    }

    public static void main(String[] args) {
//        decoder = new WltDec();

        jnaTestDemo = new jnaTest();
        jnaTestDemo.showButtonDemo();
    }

    private void prepareGui() {
        mainFrame = new JFrame("身份证阅读软件示例");
        mainFrame.setSize(500, 400);
        mainFrame.setLayout(new GridLayout(3, 1));
//        mainFrame.setResizable(false);//设置窗口不可放大缩小
//        mainFrame.setLocation(); // 设置窗口的位置（相对于屏幕左上角）
        /**
         * void setLocationRelativeTo(Component comp)
         * 设置窗口的相对位置。
         * 如果 comp 整个显示区域在屏幕内, 则将窗口放置到 comp 的中心;
         * 如果 comp 显示区域有部分不在屏幕内, 则将该窗口放置在最接近 comp 中心的一侧;
         * comp 为 null, 表示将窗口放置到屏幕中心。
         */
        mainFrame.setLocationRelativeTo(null);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        statusLabel = new JLabel("", JLabel.CENTER);
//        statusLabel.setSize(200,100);

        controlPanel = new JPanel();
//        controlPanel.setSize(300,100);
        controlPanel.setLayout(new FlowLayout());

        commentTextArea = new JTextArea("",10,10);
        commentTextArea.setEditable(false); // 设置文本框不可编辑
        scrollPane = new JScrollPane(commentTextArea);

        mainFrame.add(statusLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(scrollPane);
        mainFrame.setVisible(true);
    }

    private void showButtonDemo(){

//        ImageIcon icon = createImageIcon("zp.jpg","Java");
//        JLabel commentlabel = new JLabel("", icon, JLabel.CENTER);

        JButton btn_open = new JButton("打开设备");
        JButton btn_close = new JButton("关闭设备");
        JButton btn_read = new JButton("读卡");

        btn_open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (portnum = 1001; portnum <= 1016 ; portnum++) {
                    int ret = stdapi.sdtDll.SDT_OpenPort(portnum);
                    if (ret == 0x90) {
                        statusLabel.setText("设备打卡成功");
                        break;
                    }
                }
            }
        });

        btn_close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int ret = stdapi.sdtDll.SDT_ClosePort(portnum);
                if (ret == 0x90) {
                    statusLabel.setText("设备关闭成功");
                } else {
                    statusLabel.setText("设备关闭失败");
                }
            }
        });

        btn_read.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

//                int ret = testDemo.stdapi.sdtDll.SDT_StartFindIDCard(portnum, pucManaInfo, 0);
//                if (ret != 0x9F) {
//                    statusLabel.setText("寻卡失败");
//                } else {
//                    statusLabel.setText("寻卡成功");
//                }
//                ret = testDemo.stdapi.sdtDll.SDT_SelectIDCard(portnum, pucManaMsg, 0);
//                if (ret != 0x90) {
//                    statusLabel.setText("选卡失败");
//                } else {
//                    statusLabel.setText("选卡成功");
//                }
//
//                ret = testDemo.stdapi.sdtDll.SDT_ReadBaseMsg(portnum, pucCHMsg, puiCHMsgLen, pucPHMsg, puiPHMsgLen, 0);
//                if (ret != 0x90) {
//                    statusLabel.setText("读卡失败");
//                } else {
//                    statusLabel.setText("读卡成功");
//                    char [] baseMsgStr = new char[128];
//
//                    try {
//                        SMUTIL.DecodeByte(pucCHMsg, baseMsgStr); // 将读取的身份证中的信息字节，解码成可阅读的文字
//                    } catch (Exception exception) {
//                        exception.printStackTrace();
//                    }
//
//                    PareseItem(baseMsgStr, basemsg);  // 将信息解析到 msg中
//
//                    String show = "姓名:"
//                            + basemsg.name + '\n'
//                            + "中文姓名:" + basemsg.cname + '\n'
//                            + "性别:" + basemsg.sex + '\n'
//                            + "民族:" + basemsg.nation + '\n'
//                            + "出生日期:"
//                            + basemsg.birth + '\n'
//                            + "住址:" + basemsg.address + '\n'
//                            + "身份证号码:" + basemsg.id + '\n'
//                            + "签发机关:" + basemsg.iss + '\n'
//                            + "有效期日期:"
//                            + basemsg.validPeriod + '\n'
//                            + "通行证号:" + basemsg.passnum + '\n'
//                            + "签发次数:" +basemsg.signum + '\n';
//
//                    commentTextArea.setText(show);

                    String src = jnaTest.class.getResource("/files/zp.wlt").getPath();
                    String des = jnaTest.class.getResource("").getPath();
//                    try {
//                        FileImageOutputStream fileImageOutputStream = new FileImageOutputStream(new File(src));
//                        fileImageOutputStream.write(pucPHMsg, 0, pucPHMsg.length);
//                        fileImageOutputStream.close();
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }

                    // USB图片接收参数是 2，串口接收参数是 1
                    int ret = testDemo.wltrs.wltrsDll.GetBmp(src, 2); // 提示没有授权文件
//                    int ret = testDemo.wltrs.wltrsDll.unpack(src, des, 1); // 提示没有授权文件
                    System.out.println("");
//                }
            }
        });

        controlPanel.add(btn_open);
        controlPanel.add(btn_close);
        controlPanel.add(btn_read);
//        controlPanel.add(commentlabel);
        mainFrame.setVisible(true);

    }

    // Returns an ImageIcon, or null if the path was invalid.
    private static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = jnaTest.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void PareseItem(char[] charArray, baseInfoMsg msg) {
        if (SMUTIL.GetCardType(charArray) == 1 || SMUTIL.GetCardType(charArray) == 3) {
            if (SMUTIL.GetCardType(charArray) == 1) {
                msg.nation = SMUTIL.GetNation(charArray);
                msg.cname = " ";
                msg.passnum = " ";
                msg.signum = " ";
            } else {
                // GAT
                msg.nation = " ";
                msg.cname = " ";
                msg.passnum = SMUTIL.GetPassNum(charArray);
                msg.signum = SMUTIL.GetSigNum(charArray);
            }
            msg.name = SMUTIL.GetName(charArray);
            msg.sex = SMUTIL.GetSex(charArray);
            msg.birth = SMUTIL.GetBirth(charArray);
            msg.address = SMUTIL.GetAddress(charArray);
            msg.id = SMUTIL.GetId(charArray);
            msg.iss = SMUTIL.GetIss(charArray);
            msg.validPeriod = SMUTIL.GetValidPeriod(charArray);

        }else {
            msg.name = SMUTIL.GetForeName(charArray);
            msg.cname = SMUTIL.GetForeCName(charArray);
            msg.sex = SMUTIL.GetForeSex(charArray);
            msg.birth = SMUTIL.GetForeBirth(charArray);
            msg.id = SMUTIL.GetForeId(charArray);
            msg.nation = SMUTIL.GetForeNation(charArray);
            msg.validPeriod = SMUTIL.GetValidPeriod(charArray);
            msg.passnum = " ";
            msg.signum = " ";
            msg.address = " ";
            msg.iss = " ";
        }
    }

    public class baseInfoMsg {
        public String name;
        public String cname;
        public String sex;
        public String nation;
        public String birth;
        public String address;
        public String id;
        public String iss;
        public String validPeriod;
        public String passnum;
        public String signum;
//        public Bitmap photo;
    }
}
