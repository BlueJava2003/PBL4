/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package CLIENT.UI;

import CLIENT.BLL.QuestionBLL;
import CLIENT.BLL.ResultBLL;
import CLIENT.DTO.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author nguye
 */
public class TakeExam extends javax.swing.JPanel {

    private int DEFALUT_WIDTH;
    private ExamDTO exam;
    private UserDTO user;
    private int positionQuetion = 0;
    private int amount_Question = 0;
    private String answerChosen = "";
    private MyExamResult myresult = null;
    private QuestionBLL qsbll = new QuestionBLL();
    private ResultBLL reBll = new ResultBLL();
    private Timer timer;
    private long startTime = -1;
    private long duration = 15 * (60000);
    private long TimeExam = 0;
    private int initialPosition = 0;
    private ArrayList<MyExamResult> listEx1 = null;
    public TakeExam() {
        this.setSize(1090, 750);
        initComponents();
    }

    public TakeExam(int width) {
        DEFALUT_WIDTH = width;
        initComponents();
        this.setSize(this.DEFALUT_WIDTH - 200, 750);
        initComponents();
    }

    public TakeExam(int width, ExamDTO exam, UserDTO user) throws Exception {
        // -- New code by Nam 
        DEFALUT_WIDTH = width;
        this.exam = exam;
        this.user = user;
        ArrayList<MyExamResult> list = loadMyExamResult(this.exam.getId(),this.user.getId());
        if(list != null) {
            myresult = new MyExamResult(exam.getId(),
                                        list.get(0).getAmount_correct(),
                                        list.get(0).getAmount_incorrect(),
                                        list.get(0).getTime_doing(),
                                        list.get(0).getPoint(),
                                        list.get(0).getPosition(),
                                        list.get(0).getRank(),
                                        list.get(0).getExam_id(),
                                        list.get(0).getUser_id()
                                        );
            positionQuetion = myresult.getPosition();
            initialPosition = myresult.getPosition();
            initComponents();
            this.setSize(this.DEFALUT_WIDTH - 200, 750);
            initComponents();
            init();

            
            deleteMyExamResult(this.exam.getId(), this.user.getId());
        } else {
            myresult = new MyExamResult(exam.getId(), 0, 0, 0, 0,0, 1,this.exam.getId(), this.user.getId());
            initComponents();
            this.setSize(this.DEFALUT_WIDTH - 200, 750);
            initComponents();
            init();
        }
        
        // -- original
//        DEFALUT_WIDTH = width;
//        this.exam = exam;
//        this.user = user;
//        myresult = new MyExamResult(exam.getId(), 0, 0, 0, 0,0, 1,this.exam.getId(), this.user.getId());
//        initComponents();
//        this.setSize(this.DEFALUT_WIDTH - 200, 750);
//        initComponents();
//        init();
    }

    private void init() throws Exception {
        loadListQuestions(exam.getId());
        rbA.setActionCommand("A");
        rbB.setActionCommand("B");
        rbC.setActionCommand("C");
        rbD.setActionCommand("D");
        showQuestion(initialPosition);
        btnResult.setVisible(false);
        setpInfoExamAndCompetitor();
        lbIconHeader.setIcon(new ImageIcon("./src/main/java/CLIENT/UI/img/10207-man-student-light-skin-tone-icon-32.png"));
        lbImg.setIcon(new ImageIcon("./src/main/java/CLIENT/UI/img/iconsquizz01.png"));
        lbSerialImg.setIcon(new ImageIcon("./src/main/java/CLIENT/UI/img/icons8-quizizz-50.png"));
        lbIconClock.setIcon(new ImageIcon("./src/main/java/CLIENT/UI/img/icons8-time-50.png"));
        this.duration = exam.getTotal_time() * (60000);
        listEx1 = loadMyExamResult(exam.getId(), user.getId());
        
        //Timer
        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startTime < 0) {
                    startTime = System.currentTimeMillis();
                }
                // duration:        tổng thời gian làm bài
                // now:             mốc thời gian hiện tại
                // start:           mốc thời gian bắt đầu làm bài
                // clockTime:       thời gian đã làm bài
                // TimeExam :       thời gian còn lại
                long now = System.currentTimeMillis();
                long clockTime = now - startTime;
                if (clockTime >= duration) {
                    clockTime = duration;
                    timer.stop();
                    try {
                        shutDownAndSumbitExam();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                
                if(listEx1 == null || listEx1.isEmpty()){
                    SimpleDateFormat df = new SimpleDateFormat("mm:ss");
                    lbCounterTimer.setText(df.format(duration - clockTime));
                    TimeExam = duration - clockTime;
                } else {
                    long time_doing = (long)listEx1.get(0).getTime_doing();
                    SimpleDateFormat df = new SimpleDateFormat("mm:ss");
                    lbCounterTimer.setText(df.format(duration - clockTime - time_doing));
                    TimeExam = duration - clockTime;
                    
                }
            }
        });
        timer.setInitialDelay(0);
//            addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                   
//                }
//            });

        if (!timer.isRunning()) {
            startTime = -1;
            timer.start();

        }

    }
    public void clearTakeExam() {
        amount_Question = 0;
        positionQuetion = 0;
        myresult = null;
        answerChosen = "";
        timer.stop();
    }

    private void shutDownAndSumbitExam() throws Exception {
        JOptionPane.showMessageDialog(TakeExam.this, " Đã Hết Thời Gian Làm Bài !",
                "Cảnh Báo", JOptionPane.ERROR_MESSAGE);

        float correct_num = this.myresult.getAmount_correct();
        float incorrect_num = this.myresult.getAmount_incorrect();
        float time = this.myresult.getTime_doing();
        float point = (float) Math.round(((correct_num * 10) / this.amount_Question) * 10) / 10;
        saveResultExam();
        System.out.println("Thoi gian :" + TimeExam);
        int rank = caculateRank(new ResultDTO(null, exam.getId(), user.getId(), point));
//              JOptionPane.showMessageDialog(null,"Kết Quả","Điểm :"+point);
        btnNext.setVisible(false);
        Notify notipn = new Notify(ManageExam.getFrames()[0], false);
        notipn.setInfo("An", correct_num, incorrect_num, (exam.getTotal_time() * 60000) - TimeExam, point, rank);
        notipn.setVisible(true);
    }

    private void setpInfoExamAndCompetitor() {
        txtExamID.setText("   " + String.valueOf(exam.getId()));
        txtClassRoom.setText("   " + String.valueOf(exam.getClass_room()));
        txtQuantity.setText("   " + String.valueOf(exam.getQuantity()));
        txtSubject.setText("   " + String.valueOf(exam.getSubject()));
        txtTime.setText("   " + String.valueOf(exam.getTotal_time()));
        txtUsername.setText("   " + String.valueOf(user.getName()));
        txtUserID.setText("   " + String.valueOf(user.getId()));
    }

    private void loadListQuestions(int examID) throws Exception {
        qsbll.loadDSQuestion(examID);
        this.amount_Question = QuestionBLL.getListQuestion().size();

    }
    
    private ArrayList<MyExamResult> loadMyExamResult(int examID, int userID) throws Exception {
        ArrayList<MyExamResult> myrs = reBll.loadMyExamResult(examID, userID);
        if(myrs == null || myrs.isEmpty()){
            return null;
        }
        return myrs;
    }
    
     private void deleteMyExamResult(int examID, int userID) throws Exception {
        reBll.deleteMyExamResult(examID, userID);
    }

    private int caculateRank(ResultDTO result) throws Exception {
        ArrayList<ResultDTO> listRE = reBll.loadDSResult(exam.getId());
        listRE.add(result);
        Collections.sort(listRE, new Comparator<ResultDTO>() {
            @Override
            public int compare(ResultDTO o1, ResultDTO o2) {
                if (o1.getScore() < o2.getScore()) {
                    return 1;
                } else if (o1.getScore() > o2.getScore()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        for (int i = 0; i < listRE.size(); i++) {
            if (listRE.get(i).getId() == result.getId()) {
                System.out.println("Rank : " + i + "--" + (i + 1));
                return i+1;
            }
        }
        return 0;
    }

    private void finishExam() {
      //  saveResultExam();
        timer.stop();
        System.out.println("Thoi gian :" + TimeExam);
        JOptionPane.showMessageDialog(null, "Bạn Đã Hoàn Bài Thi. Chọn Xem Kết Quả Để Biết Điểm");
        btnNext.setVisible(false);
        btnResult.setVisible(true);
    }
    
    
    // HÀM TÍNH ĐIỂM VÀ LUU KẾT QUẢ THI
    private void saveResultExam() {
       
        try {
            float correct_num = this.myresult.getAmount_correct();
            float incorrect_num = this.myresult.getAmount_incorrect();
            float time = this.myresult.getTime_doing();
            myresult.setPoint((float) Math.round(((correct_num * 10) / this.amount_Question) * 10) / 10);
            myresult.setTime_doing((exam.getTotal_time() * 60000) - TimeExam);
            if(listEx1 != null){
                System.out.println("CO VAO DAY");
                myresult.setTime_doing((exam.getTotal_time() * 60000) - TimeExam + listEx1.get(0).getTime_doing());
            }
            ResultDTO result = new ResultDTO(null, exam.getId(), user.getId(), myresult.getPoint());
            //save Kết Quả
           int idRe=reBll.addEResult(result);
           myresult.setId(idRe);
            //tinhRank
            myresult.setRank(caculateRank(new ResultDTO(myresult.getId(), exam.getId(), user.getId(), myresult.getPoint())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    // HÀM HIỂN THI CÂU HỎI TỪ ARRAY.LIST
    private void showQuestion(int pos) {
        QuestionDTO question;
        ArrayList<QuestionDTO> res = QuestionBLL.getListQuestion();
        try {
            if (pos < res.size()) {
                question = res.get(pos);
                txtSerial.setText(" " + "      " + String.valueOf(pos + 1));
                txtQuestion.setText(question.getQuestion());
                txtA.setText(question.getA());
                txtB.setText(question.getB());
                txtC.setText(question.getC());
                txtD.setText(question.getD());
            }
            if(pos == amount_Question){
                return;
            }
            if(res.get(pos).getType() == 1){
                rbA.setVisible(true);
                rbB.setVisible(true);
                rbC.setVisible(true);
                rbD.setVisible(true);
                chkA.setVisible(false);
                chkB.setVisible(false);
                chkC.setVisible(false);
                chkD.setVisible(false);
                
            } else {
                rbA.setVisible(false);
                rbB.setVisible(false);
                rbC.setVisible(false);
                rbD.setVisible(false);
                chkA.setVisible(true);
                chkB.setVisible(true);
                chkC.setVisible(true);
                chkD.setVisible(true);
            }
        } catch (Exception e) {
            System.out.println("OUT OF BOUND");
        }
    }

//    private boolean checkAnswer(String res) {
//        return (res.equals(QuestionBLL.getListQuestion().get(positionQuetion).getAnswer()));
//    }
    
    //HÀM KIỂM TRA ĐÁP ÁN VÀ LƯU VÀO MY-RESULT
    private void checkAnswerQuestion() {
        System.out.println("vi tri hien tai:"+positionQuetion+ " So luong cau hoi: "+amount_Question);
        String ans = QuestionBLL.getListQuestion().get(positionQuetion).getAnswer();
        if (ans.contains(this.answerChosen)) {
            myresult.setAmount_correct(myresult.getAmount_correct() + 1); 
           
        } else {
            myresult.setAmount_incorrect(myresult.getAmount_incorrect() + 1);    
        }
        // luu kết quả và tính hạng
        if (positionQuetion + 1 == amount_Question) {
            // luu diem va tinh sank
            saveResultExam();
            // dừng thi và thông bao kết quả
            finishExam();
        }

    }

    private void setBackgroundAnswer(String answer, Color red, Color green, Border borderRed, Border borderGreen) {
        Color colorA = ("A".equals(answer) ? green : red);
        Border borA = ("A".equals(answer) ? borderGreen : borderRed);
        Color colorB = ("B".equals(answer) ? green : red);
        Border borB = ("B".equals(answer) ? borderGreen : borderRed);
        Color colorC = ("C".equals(answer) ? green : red);
        Border borC = ("C".equals(answer) ? borderGreen : borderRed);
        Color colorD = ("D".equals(answer) ? green : red);
        Border borD = ("D".equals(answer) ? borderGreen : borderRed);
        txtA.setBackground(colorA);
        txtB.setBackground(colorB);
        txtC.setBackground(colorC);
        txtD.setBackground(colorD);
        txtA.setBorder(borA);
        txtB.setBorder(borB);
        txtC.setBorder(borC);
        txtD.setBorder(borD);
    }

    private void clearBackgroundAnswer() {
        txtA.setBackground(Color.white);
        txtB.setBackground(Color.white);
        txtC.setBackground(Color.white);
        txtD.setBackground(Color.white);
        txtA.setBorder(null);
        txtB.setBorder(null);
        txtC.setBorder(null);
        txtD.setBorder(null);

    }

    private void clearSelectedAnswer() {
        grAnswer.clearSelection();
        rbA.setSelected(false);
        rbB.setSelected(false);
        rbC.setSelected(false);
        rbD.setSelected(false);
        chkA.setSelected(false);
        chkB.setSelected(false);
        chkC.setSelected(false);
        chkD.setSelected(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grAnswer = new javax.swing.ButtonGroup();
        HEADER = new javax.swing.JPanel();
        lbSoLuong1 = new javax.swing.JLabel();
        lbIconHeader = new javax.swing.JLabel();
        pButton = new javax.swing.JPanel();
        btnNext = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        btnResult = new javax.swing.JButton();
        lbImg = new javax.swing.JLabel();
        pInfoExam = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        HEADER1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtTime = new javax.swing.JTextField();
        txtSubject = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtExamID = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtClassRoom = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        HEADER2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtUserID = new javax.swing.JTextField();
        pInput = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtB = new javax.swing.JTextField();
        txtC = new javax.swing.JTextField();
        txtD = new javax.swing.JTextField();
        txtA = new javax.swing.JTextField();
        rbB = new javax.swing.JRadioButton();
        rbD = new javax.swing.JRadioButton();
        rbC = new javax.swing.JRadioButton();
        rbA = new javax.swing.JRadioButton();
        jLabel13 = new javax.swing.JLabel();
        txtSerial = new javax.swing.JTextField();
        pInput4 = new javax.swing.JPanel();
        lbNotifyResult = new javax.swing.JLabel();
        txtQuestion = new javax.swing.JTextField();
        lbSerialImg = new javax.swing.JLabel();
        lbCounterTimer = new javax.swing.JLabel();
        lbIconClock = new javax.swing.JLabel();
        chkA = new javax.swing.JCheckBox();
        chkB = new javax.swing.JCheckBox();
        chkC = new javax.swing.JCheckBox();
        chkD = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(255, 255, 255));

        HEADER.setBackground(new java.awt.Color(255, 204, 0));
        HEADER.setForeground(new java.awt.Color(204, 0, 51));

        lbSoLuong1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbSoLuong1.setForeground(new java.awt.Color(255, 0, 51));
        lbSoLuong1.setText("THI TRẮC NGHIỆM");

        javax.swing.GroupLayout HEADERLayout = new javax.swing.GroupLayout(HEADER);
        HEADER.setLayout(HEADERLayout);
        HEADERLayout.setHorizontalGroup(
            HEADERLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HEADERLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbIconHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbSoLuong1)
                .addGap(395, 395, 395))
        );
        HEADERLayout.setVerticalGroup(
            HEADERLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbSoLuong1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
            .addGroup(HEADERLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbIconHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pButton.setBackground(new java.awt.Color(255, 255, 255));

        btnNext.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnNext.setIcon(new ImageIcon("./src/main/java/CLIENT/UI/img/icons8-right-arrow-30.png"));
        btnNext.setText("Câu Tiếp Theo");
        btnNext.setBorder(null);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(204, 0, 51));
        btnExit.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("Kết Thúc");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        jSeparator2.setBackground(new java.awt.Color(153, 153, 153));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        btnResult.setBackground(new java.awt.Color(0, 153, 153));
        btnResult.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnResult.setForeground(new java.awt.Color(255, 255, 255));
        btnResult.setIcon(new ImageIcon("./src/main/java/CLIENT/UI/img/icons8-result-35.png"));
        btnResult.setText("Xem Kết Quả");
        btnResult.setBorder(null);
        btnResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResultActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pButtonLayout = new javax.swing.GroupLayout(pButton);
        pButton.setLayout(pButtonLayout);
        pButtonLayout.setHorizontalGroup(
            pButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pButtonLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1096, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pButtonLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbImg, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158)
                .addComponent(btnResult, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );
        pButtonLayout.setVerticalGroup(
            pButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pButtonLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnResult, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbImg, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pInfoExam.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(0, 102, 51));
        jSeparator1.setForeground(new java.awt.Color(51, 0, 255));

        HEADER1.setBackground(new java.awt.Color(255, 255, 255));
        HEADER1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 0), 2, true), "Thông Tin Đề Thi", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Phút");

        txtTime.setEditable(false);
        txtTime.setBackground(new java.awt.Color(204, 255, 255));
        txtTime.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTime.setBorder(null);

        txtSubject.setEditable(false);
        txtSubject.setBackground(new java.awt.Color(204, 255, 255));
        txtSubject.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSubject.setBorder(null);
        txtSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubjectActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Số Lượng Câu Hỏi");

        txtExamID.setEditable(false);
        txtExamID.setBackground(new java.awt.Color(204, 204, 204));
        txtExamID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtExamID.setBorder(null);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Tiêu Đề");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Mã Phòng Thi");

        txtClassRoom.setEditable(false);
        txtClassRoom.setBackground(new java.awt.Color(204, 255, 255));
        txtClassRoom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtClassRoom.setBorder(null);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Mã Đề Thi :");

        txtQuantity.setEditable(false);
        txtQuantity.setBackground(new java.awt.Color(204, 255, 255));
        txtQuantity.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Thời Gian");

        javax.swing.GroupLayout HEADER1Layout = new javax.swing.GroupLayout(HEADER1);
        HEADER1.setLayout(HEADER1Layout);
        HEADER1Layout.setHorizontalGroup(
            HEADER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HEADER1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HEADER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HEADER1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtExamID, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel9))
                    .addGroup(HEADER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(HEADER1Layout.createSequentialGroup()
                            .addComponent(txtClassRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, HEADER1Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(70, 70, 70)
                            .addComponent(jLabel11))))
                .addGroup(HEADER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HEADER1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(61, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HEADER1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(HEADER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addGroup(HEADER1Layout.createSequentialGroup()
                                .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)))
                        .addGap(81, 81, 81))))
        );
        HEADER1Layout.setVerticalGroup(
            HEADER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HEADER1Layout.createSequentialGroup()
                .addGroup(HEADER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HEADER1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(HEADER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(5, 5, 5))
                    .addGroup(HEADER1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(HEADER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(txtExamID, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(HEADER1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtClassRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
        );

        HEADER2.setBackground(new java.awt.Color(255, 255, 255));
        HEADER2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 0, 0), 2, true), "Thông Tin Thí Sinh", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16), new java.awt.Color(51, 102, 255))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Họ và Tên:");

        txtUsername.setEditable(false);
        txtUsername.setBackground(new java.awt.Color(255, 255, 204));
        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtUsername.setBorder(null);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Mã:");

        txtUserID.setEditable(false);
        txtUserID.setBackground(new java.awt.Color(255, 255, 204));
        txtUserID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtUserID.setBorder(null);

        javax.swing.GroupLayout HEADER2Layout = new javax.swing.GroupLayout(HEADER2);
        HEADER2.setLayout(HEADER2Layout);
        HEADER2Layout.setHorizontalGroup(
            HEADER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HEADER2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HEADER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HEADER2Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HEADER2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(66, 66, 66)
                        .addComponent(txtUserID, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        HEADER2Layout.setVerticalGroup(
            HEADER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HEADER2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(HEADER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtUserID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(HEADER2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pInfoExamLayout = new javax.swing.GroupLayout(pInfoExam);
        pInfoExam.setLayout(pInfoExamLayout);
        pInfoExamLayout.setHorizontalGroup(
            pInfoExamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInfoExamLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(HEADER1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(HEADER2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
            .addComponent(jSeparator1)
        );
        pInfoExamLayout.setVerticalGroup(
            pInfoExamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInfoExamLayout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addGroup(pInfoExamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(HEADER1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(HEADER2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pInput.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Chọn Đáp Án:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setText("Câu Hỏi:");

        txtB.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtB.setBorder(null);

        txtC.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtC.setBorder(null);
        txtC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCActionPerformed(evt);
            }
        });

        txtD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtD.setBorder(null);
        txtD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDActionPerformed(evt);
            }
        });

        txtA.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtA.setBorder(null);

        rbB.setBackground(new java.awt.Color(255, 255, 255));
        grAnswer.add(rbB);
        rbB.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rbB.setText("B");
        rbB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbBActionPerformed(evt);
            }
        });
        rbB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rbBKeyPressed(evt);
            }
        });

        rbD.setBackground(new java.awt.Color(255, 255, 255));
        grAnswer.add(rbD);
        rbD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rbD.setText("D");
        rbD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbDActionPerformed(evt);
            }
        });
        rbD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rbDKeyPressed(evt);
            }
        });

        rbC.setBackground(new java.awt.Color(255, 255, 255));
        grAnswer.add(rbC);
        rbC.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rbC.setText("C");
        rbC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCActionPerformed(evt);
            }
        });
        rbC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rbCKeyPressed(evt);
            }
        });

        rbA.setBackground(new java.awt.Color(255, 255, 255));
        grAnswer.add(rbA);
        rbA.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rbA.setText("A");
        rbA.setContentAreaFilled(false);
        rbA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAActionPerformed(evt);
            }
        });
        rbA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rbAKeyPressed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel13.setText("Câu Hỏi Số: ");

        txtSerial.setEditable(false);
        txtSerial.setBackground(new java.awt.Color(204, 204, 204));
        txtSerial.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtSerial.setForeground(new java.awt.Color(0, 0, 204));
        txtSerial.setBorder(null);

        pInput4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pInput4Layout = new javax.swing.GroupLayout(pInput4);
        pInput4.setLayout(pInput4Layout);
        pInput4Layout.setHorizontalGroup(
            pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 114, Short.MAX_VALUE)
        );
        pInput4Layout.setVerticalGroup(
            pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );

        lbNotifyResult.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        txtQuestion.setEditable(false);
        txtQuestion.setBackground(new java.awt.Color(255, 255, 255));
        txtQuestion.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        txtQuestion.setBorder(null);

        lbCounterTimer.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbCounterTimer.setForeground(new java.awt.Color(204, 0, 0));

        chkA.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        chkA.setText("A");
        chkA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAActionPerformed(evt);
            }
        });

        chkB.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        chkB.setText("B");
        chkB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBActionPerformed(evt);
            }
        });

        chkC.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        chkC.setText("C");
        chkC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCActionPerformed(evt);
            }
        });

        chkD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        chkD.setText("D");
        chkD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pInputLayout = new javax.swing.GroupLayout(pInput);
        pInput.setLayout(pInputLayout);
        pInputLayout.setHorizontalGroup(
            pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pInputLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pInputLayout.createSequentialGroup()
                        .addComponent(lbSerialImg, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(txtSerial, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 299, Short.MAX_VALUE)
                        .addComponent(lbIconClock, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(lbCounterTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(196, 196, 196))
                    .addGroup(pInputLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(pInputLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pInputLayout.createSequentialGroup()
                        .addComponent(rbA)
                        .addGap(18, 18, 18)
                        .addComponent(chkA))
                    .addGroup(pInputLayout.createSequentialGroup()
                        .addComponent(rbB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkB))
                    .addGroup(pInputLayout.createSequentialGroup()
                        .addComponent(rbD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkD))
                    .addGroup(pInputLayout.createSequentialGroup()
                        .addComponent(rbC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkC)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtB)
                    .addComponent(txtA)
                    .addComponent(txtC)
                    .addComponent(txtD))
                .addGap(18, 18, 18)
                .addComponent(lbNotifyResult, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(pInput4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pInputLayout.setVerticalGroup(
            pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInputLayout.createSequentialGroup()
                .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSerial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbSerialImg, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbCounterTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pInputLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbIconClock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0)
                .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pInput4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pInputLayout.createSequentialGroup()
                        .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pInputLayout.createSequentialGroup()
                                .addComponent(txtA, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(66, 66, 66)
                                .addComponent(txtC, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbNotifyResult, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pInputLayout.createSequentialGroup()
                                .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pInputLayout.createSequentialGroup()
                                        .addGap(100, 100, 100)
                                        .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(rbC)
                                            .addComponent(chkC)))
                                    .addGroup(pInputLayout.createSequentialGroup()
                                        .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(rbA)
                                            .addComponent(jLabel1)
                                            .addComponent(chkA))
                                        .addGap(20, 20, 20)
                                        .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(rbB)
                                            .addComponent(txtB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(chkB))))
                                .addGap(17, 17, 17)
                                .addGroup(pInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(rbD)
                                    .addComponent(txtD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chkD))))
                        .addGap(20, 20, 20))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(HEADER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pInfoExam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pButton, javax.swing.GroupLayout.PREFERRED_SIZE, 1056, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(pInput, javax.swing.GroupLayout.PREFERRED_SIZE, 936, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(HEADER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pInfoExam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCActionPerformed

    }//GEN-LAST:event_txtCActionPerformed

    private void rbBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbBActionPerformed
        this.answerChosen = "B";
        //this.checkAnswerQuestion();

    }//GEN-LAST:event_rbBActionPerformed

    private void rbDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbDActionPerformed
        this.answerChosen = "D";
        //this.checkAnswerQuestion();


    }//GEN-LAST:event_rbDActionPerformed

    private void rbCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCActionPerformed
        this.answerChosen = "C";
       // this.checkAnswerQuestion();

    }//GEN-LAST:event_rbCActionPerformed

    private void rbAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAActionPerformed
        this.answerChosen = "A";
        //this.checkAnswerQuestion();

    }//GEN-LAST:event_rbAActionPerformed

    private void txtSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubjectActionPerformed
    private void keyPressNextQuestion(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnNext.doClick();
        }
    }
    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        this.checkAnswerQuestion();
        if ((grAnswer.getSelection() != null) || 
                (chkA.isSelected() || chkB.isSelected() || chkC.isSelected() || chkD.isSelected())
           ) {
            this.clearBackgroundAnswer();
            this.clearSelectedAnswer();
            this.positionQuetion = this.positionQuetion + 1;
            showQuestion(positionQuetion);
        } else {
            JOptionPane.showMessageDialog(null, "\"Vui lòng chọn đáp án cho câu hỏi hiện tại !!!\"");
        }

    }//GEN-LAST:event_btnNextActionPerformed

    private void txtDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDActionPerformed

    private void rbAKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rbAKeyPressed
        keyPressNextQuestion(evt);
    }//GEN-LAST:event_rbAKeyPressed

    private void rbBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rbBKeyPressed

    }//GEN-LAST:event_rbBKeyPressed

    private void rbCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rbCKeyPressed

    }//GEN-LAST:event_rbCKeyPressed

    private void rbDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rbDKeyPressed

    }//GEN-LAST:event_rbDKeyPressed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
       //timer.stop();
        // check đã hoàn thành bài thi
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn Thực Sụ Muốn Thoát ?",
                "Cảnh Báo", JOptionPane.YES_NO_OPTION);
        if (confirm == 1) {
            clearTakeExam();
            ManageExam.openChooenExamPanel(user);
        } else {
            float correct_num = this.myresult.getAmount_correct();
            myresult.setPoint((float) Math.round(((correct_num * 10) / this.amount_Question) * 10) / 10);
            myresult.setTime_doing((exam.getTotal_time() * 60000) - TimeExam);
            if(listEx1 != null && !listEx1.isEmpty()){
                myresult.setTime_doing((exam.getTotal_time() * 60000) - TimeExam + listEx1.get(0).getTime_doing());
            }
            myresult.setPosition(positionQuetion);
            try {
                reBll.addExamResult(myresult);
            } catch (Exception e) {
                e.printStackTrace();
            }
            clearTakeExam();
            ManageExam.openChooenExamPanel(user);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResultActionPerformed
         Notify notipn = new Notify(ManageExam.getFrames()[0], false);
        notipn.setInfo("An", myresult.getAmount_correct(), myresult.getAmount_incorrect(), (long)myresult.getTime_doing(),
                myresult.getPoint(), myresult.getRank());
        notipn.setVisible(true);
    }//GEN-LAST:event_btnResultActionPerformed

    private void chkAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAActionPerformed
        if(chkA.isSelected()){
            if(this.answerChosen.equals("")){
                this.answerChosen = "A";
            } else {
                answerChosen += "A";
            }
        } else {
            if(!this.answerChosen.equals("")){
               answerChosen = answerChosen.replace("A", "");
            }
        }
        
        if(answerChosen.length() >= 2){
            char[] answerCharsArray = answerChosen.toCharArray();
            Arrays.sort(answerCharsArray);
            String newAns = "";
            for(char c : answerCharsArray){
                newAns += c;
            }
            answerChosen = newAns;
        }
        
        System.out.println("ANSWER CHOOSEN: " + answerChosen);
    }//GEN-LAST:event_chkAActionPerformed

    private void chkBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBActionPerformed
        if(chkB.isSelected()){
            if(this.answerChosen.equals("")){
                this.answerChosen = "B";
            } else {
                answerChosen += "B";
            }
        } else {
            if(!this.answerChosen.equals("")){
                answerChosen = answerChosen.replaceAll("B", "");
            }
        }
        
        if(answerChosen.length() >= 2){
            char[] answerCharsArray = answerChosen.toCharArray();
            Arrays.sort(answerCharsArray);
            String newAns = "";
            for(char c : answerCharsArray){
                newAns += c;
            }
            answerChosen = newAns;
        }
        
        System.out.println("ANSWER CHOOSEN: " + answerChosen);
    }//GEN-LAST:event_chkBActionPerformed

    private void chkCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCActionPerformed
        if(chkC.isSelected()){
            if(this.answerChosen.equals("")){
                this.answerChosen = "C";
            } else {
                answerChosen += "C";
            }
        } else {
            if(!this.answerChosen.equals("")){
                answerChosen = answerChosen.replaceAll("C", "");
            }
        }
        
        if(answerChosen.length() >= 2){
            char[] answerCharsArray = answerChosen.toCharArray();
            Arrays.sort(answerCharsArray);
            String newAns = "";
            for(char c : answerCharsArray){
                newAns += c;
            }
            answerChosen = newAns;
        }
        
        System.out.println("ANSWER CHOOSEN: " + answerChosen);
    }//GEN-LAST:event_chkCActionPerformed

    private void chkDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDActionPerformed
        if(chkD.isSelected()){
            if(this.answerChosen.equals("")){
                this.answerChosen = "D";
            } else {
                answerChosen += "D";
            }
        } else {
            if(!this.answerChosen.equals("")){
                answerChosen = answerChosen.replaceAll("D", "");
            }
        }
        
        if(answerChosen.length() >= 2){
            char[] answerCharsArray = answerChosen.toCharArray();
            Arrays.sort(answerCharsArray);
            String newAns = "";
            for(char c : answerCharsArray){
                newAns += c;
            }
            answerChosen = newAns;
        }
        
        System.out.println("ANSWER CHOOSEN: " + answerChosen);
    }//GEN-LAST:event_chkDActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel HEADER;
    private javax.swing.JPanel HEADER1;
    private javax.swing.JPanel HEADER2;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnResult;
    private javax.swing.JCheckBox chkA;
    private javax.swing.JCheckBox chkB;
    private javax.swing.JCheckBox chkC;
    private javax.swing.JCheckBox chkD;
    private javax.swing.ButtonGroup grAnswer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbCounterTimer;
    private javax.swing.JLabel lbIconClock;
    private javax.swing.JLabel lbIconHeader;
    private javax.swing.JLabel lbImg;
    private javax.swing.JLabel lbNotifyResult;
    private javax.swing.JLabel lbSerialImg;
    private javax.swing.JLabel lbSoLuong1;
    private javax.swing.JPanel pButton;
    private javax.swing.JPanel pInfoExam;
    private javax.swing.JPanel pInput;
    private javax.swing.JPanel pInput4;
    private javax.swing.JRadioButton rbA;
    private javax.swing.JRadioButton rbB;
    private javax.swing.JRadioButton rbC;
    private javax.swing.JRadioButton rbD;
    private javax.swing.JTextField txtA;
    private javax.swing.JTextField txtB;
    private javax.swing.JTextField txtC;
    private javax.swing.JTextField txtClassRoom;
    private javax.swing.JTextField txtD;
    private javax.swing.JTextField txtExamID;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtQuestion;
    private javax.swing.JTextField txtSerial;
    private javax.swing.JTextField txtSubject;
    private javax.swing.JTextField txtTime;
    private javax.swing.JTextField txtUserID;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
