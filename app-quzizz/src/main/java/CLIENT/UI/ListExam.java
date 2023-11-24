/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package CLIENT.UI;

import CLIENT.BLL.ExamBLL;
import CLIENT.BLL.QuestionBLL;
import CLIENT.BLL.ValidatorBLL;
import CLIENT.DTO.ExamDTO;
import CLIENT.DTO.QuestionDTO;
import CLIENT.DTO.ResponseDTO;
import CLIENT.DTO.UserDTO;
import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author nguye
 */
public class ListExam extends javax.swing.JPanel {

    private int DEFALUT_WIDTH;
    private DefaultTableModel modelExam;
    private DefaultTableModel modelQuestion;
    private UserDTO user = null;
    private QuestionDTO questionEdit = new QuestionDTO();
    private ExamDTO exam = new ExamDTO();
    private ExamBLL bllEx = new ExamBLL();
    private QuestionBLL bllQs = new QuestionBLL();
    public static Gson gson = new Gson();

    public ListExam() {
        this.setSize(1090, 750);
        initComponents();
        init();
    }

    public ListExam(int width, UserDTO user) {
        DEFALUT_WIDTH = width;
        this.user = user;
        initComponents();
        this.setSize(this.DEFALUT_WIDTH - 200, 750);
        init();
    }

    private void init() {
        loadDataExam();
        txtUserId.setText(user.getName());
        btnOpenPanelCreateQuestion.setVisible(false);
    }

    private void insertHeaderExam() {
        Vector header = new Vector();
        header.add("ID");
        header.add("Tiêu Đề");
        header.add("Phòng");
        header.add("Lượng Câu Hỏi");
        header.add("Thời gian");
        header.add("Người tạo");
        //if (model.getRowCount()==0)
        modelExam = new DefaultTableModel(header, 0);
    }

    private void insertHeaderQuestion() {
        Vector header = new Vector();
        header.add("ID");
        header.add("Câu hỏi");
        header.add("A");
        header.add("B");
        header.add("C");
        header.add("D");
        header.add("Ans");
        //if (model.getRowCount()==0)
        modelQuestion = new DefaultTableModel(header, 0);
    }

    private void outModelExam(DefaultTableModel model, ArrayList<ExamDTO> courseinstructor) // Xuất ra Table từ ArrayList
    {
        Vector data;
        model.setRowCount(0);
        for (ExamDTO ex : courseinstructor) {
            data = new Vector();
            data.add(ex.getId());
            data.add(ex.getSubject());
            data.add(ex.getClass_room());
            data.add(ex.getQuantity());
            data.add(ex.getTotal_time());

            model.addRow(data);
        }
        tbExam.setModel(model);
    }

    private void outModelQuestion(DefaultTableModel model, ArrayList<QuestionDTO> listQuestion) // Xuất ra Table từ ArrayList
    {
        Vector data;
        model.setRowCount(0);
        for (QuestionDTO qs : listQuestion) {
            data = new Vector();
            data.add(qs.getId());
            data.add(qs.getQuestion());
            data.add(qs.getA());
            data.add(qs.getB());
            data.add(qs.getC());
            data.add(qs.getD());
            data.add(qs.getAnswer());
            model.addRow(data);
        }
        tbQuestion.setModel(model);
    }

    private boolean validateForm() {
        StringBuilder sb = new StringBuilder();
        ValidatorBLL.checkEmpty(txID, sb, "Mã đề còn trống");
        ValidatorBLL.checkEmpty(txtClassRoom, sb, "Mã phòng còn trống");
        ValidatorBLL.checkEmpty(txtSubject, sb, "Tiêu đề còn trống");
       //dblinerman1@cpanel.net ValidatorBLL.checkEmpty(txtUserId, sb, "Người tạo còn trống");
        ValidatorBLL.checkEmpty(cbQuantity, sb, "Số lượng câu hỏi còn trống");
        ValidatorBLL.checkEmpty(cbTotal_Time, sb, "Thời gian còn trống");
        ValidatorBLL.checkEmpty(cbStatus, sb, "Trạng thái còn trống");
        if (sb.length() > 0) {
            JOptionPane.showMessageDialog(this, sb.toString(), "Thông Báo Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadDataExam() {
        try {
            bllEx.loadDSUserExam(String.valueOf(user.getId()));
            ArrayList<CLIENT.DTO.ExamDTO> res = bllEx.getListUserExam();
            insertHeaderExam();
            outModelExam(modelExam, ExamBLL.getListUserExam());
        } catch (Exception e) {
            System.out.println("out");
            JOptionPane.showMessageDialog(this, "Không Thể Lấy thông Tin Đề Thi ",
                    "Thông Báo Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void loadDataQuestion(int examID) {
        try {
            bllQs.loadDSQuestion(examID);
            insertHeaderQuestion();
            outModelQuestion(modelQuestion, bllQs.getListQuestion());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không tồn tại câu hỏi ",
                    "Thông Báo Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void clearInput() {
        txtSubject.setText("");
        cbQuantity.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        cbTotal_Time.setSelectedIndex(0);
        txtClassRoom.setText("");
    }

    private boolean checkUserTakeExam(String examID) {
        return bllEx.checkTakenExam(examID);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        HEADER = new javax.swing.JPanel();
        lbSoLuong1 = new javax.swing.JLabel();
        pInput4 = new javax.swing.JPanel();
        txtSubject = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtClassRoom = new javax.swing.JTextField();
        txtUserId = new javax.swing.JTextField();
        cbQuantity = new javax.swing.JComboBox<>();
        cbTotal_Time = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        btnCreateExam = new javax.swing.JButton();
        btnUpdateExam = new javax.swing.JButton();
        btnDeleteExam = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txID = new javax.swing.JTextField();
        pInput5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbExam = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        btnOpenPanelCreateQuestion = new javax.swing.JButton();
        pInput6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbQuestion = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        btnUpdateQuestion = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(255, 255, 255));

        HEADER.setBackground(new java.awt.Color(51, 102, 255));

        lbSoLuong1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbSoLuong1.setForeground(new java.awt.Color(255, 255, 255));
        lbSoLuong1.setText("DANH SÁCH ĐỀ THI");

        javax.swing.GroupLayout HEADERLayout = new javax.swing.GroupLayout(HEADER);
        HEADER.setLayout(HEADERLayout);
        HEADERLayout.setHorizontalGroup(
            HEADERLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HEADERLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbSoLuong1)
                .addGap(467, 467, 467))
        );
        HEADERLayout.setVerticalGroup(
            HEADERLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbSoLuong1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        pInput4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Môn học");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Mã Phòng Thi");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Số Lượng Câu Hỏi");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Thời Gian");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Trạng Thái");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Người Tạo");

        txtClassRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClassRoomActionPerformed(evt);
            }
        });

        txtUserId.setEditable(false);
        txtUserId.setBackground(new java.awt.Color(102, 102, 102));
        txtUserId.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtUserId.setForeground(new java.awt.Color(255, 255, 255));
        txtUserId.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));

        cbQuantity.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "3", "10", "15", "20", "25", "40", "50" }));
        cbQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbQuantityActionPerformed(evt);
            }
        });

        cbTotal_Time.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "3", "15", "20", "30", "45", "60", "90" }));
        cbTotal_Time.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTotal_TimeActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("phút");

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "active", "disabled" }));
        cbStatus.setName("cbStatus"); // NOI18N
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnCreateExam.setBackground(new java.awt.Color(0, 153, 0));
        btnCreateExam.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCreateExam.setForeground(new java.awt.Color(255, 255, 255));
        btnCreateExam.setText("TẠO ĐỀ THI");
        btnCreateExam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateExamActionPerformed(evt);
            }
        });

        btnUpdateExam.setBackground(new java.awt.Color(255, 204, 0));
        btnUpdateExam.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUpdateExam.setText("CẬP NHẬT");
        btnUpdateExam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateExamActionPerformed(evt);
            }
        });

        btnDeleteExam.setBackground(new java.awt.Color(255, 51, 51));
        btnDeleteExam.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDeleteExam.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteExam.setText("XOÁ");
        btnDeleteExam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteExamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDeleteExam, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateExam, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCreateExam))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnCreateExam, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdateExam, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDeleteExam, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Mã Đề");

        txID.setEditable(false);
        txID.setBackground(new java.awt.Color(204, 204, 204));
        txID.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        javax.swing.GroupLayout pInput4Layout = new javax.swing.GroupLayout(pInput4);
        pInput4.setLayout(pInput4Layout);
        pInput4Layout.setHorizontalGroup(
            pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInput4Layout.createSequentialGroup()
                .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pInput4Layout.createSequentialGroup()
                        .addContainerGap(55, Short.MAX_VALUE)
                        .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(cbQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(114, 114, 114)
                        .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(137, 137, 137))
                    .addGroup(pInput4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txID, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(28, 28, 28)
                        .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtClassRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(43, 43, 43)
                        .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(pInput4Layout.createSequentialGroup()
                                .addComponent(cbTotal_Time, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)))
                        .addGap(111, 111, 111)))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        pInput4Layout.setVerticalGroup(
            pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInput4Layout.createSequentialGroup()
                .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pInput4Layout.createSequentialGroup()
                        .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pInput4Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtClassRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbTotal_Time, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(pInput4Layout.createSequentialGroup()
                        .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pInput4Layout.createSequentialGroup()
                                .addGroup(pInput4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pInput4Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(cbQuantity)
                                .addContainerGap())))))
            .addGroup(pInput4Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pInput5.setBackground(new java.awt.Color(255, 255, 255));

        tbExam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbExam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbExamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbExam);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setText("Danh Sách Bài Thi");

        btnOpenPanelCreateQuestion.setBackground(new java.awt.Color(0, 153, 0));
        btnOpenPanelCreateQuestion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnOpenPanelCreateQuestion.setForeground(new java.awt.Color(255, 255, 255));
        btnOpenPanelCreateQuestion.setText("Tạo Bộ Câu Hỏi");
        btnOpenPanelCreateQuestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenPanelCreateQuestionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pInput5Layout = new javax.swing.GroupLayout(pInput5);
        pInput5.setLayout(pInput5Layout);
        pInput5Layout.setHorizontalGroup(
            pInput5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInput5Layout.createSequentialGroup()
                .addGroup(pInput5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pInput5Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnOpenPanelCreateQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pInput5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 6, Short.MAX_VALUE))
        );
        pInput5Layout.setVerticalGroup(
            pInput5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInput5Layout.createSequentialGroup()
                .addGroup(pInput5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(btnOpenPanelCreateQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        pInput6.setBackground(new java.awt.Color(255, 255, 255));

        tbQuestion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbQuestion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbQuestionMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tbQuestionMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbQuestionMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tbQuestion);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setText("Danh Sách Câu Hỏi");

        btnUpdateQuestion.setBackground(new java.awt.Color(0, 102, 255));
        btnUpdateQuestion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUpdateQuestion.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateQuestion.setText("Cập Nhật Câu Hỏi");
        btnUpdateQuestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateQuestionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pInput6Layout = new javax.swing.GroupLayout(pInput6);
        pInput6.setLayout(pInput6Layout);
        pInput6Layout.setHorizontalGroup(
            pInput6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInput6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pInput6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                    .addGroup(pInput6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpdateQuestion)))
                .addContainerGap())
        );
        pInput6Layout.setVerticalGroup(
            pInput6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pInput6Layout.createSequentialGroup()
                .addGroup(pInput6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSeparator1.setBackground(new java.awt.Color(102, 102, 102));
        jSeparator1.setForeground(new java.awt.Color(0, 153, 51));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(HEADER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pInput4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pInput5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pInput6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(HEADER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pInput4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pInput5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pInput6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateExamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateExamActionPerformed
        // check exam wasn't taken
       // System.out.println("update");checkUserTakeExam(1) == false && 
       String quantity=cbQuantity.getSelectedItem().toString();
        System.out.println(quantity);
        boolean check =!checkUserTakeExam(txID.getText().trim());
        if (validateForm() && check) {
            // update
            ExamDTO ex = new ExamDTO();
            ex.setSubject(txtSubject.getText());
            
            ex.setQuantity(Integer.valueOf( cbQuantity.getSelectedItem().toString()));
            ex.setClass_room(txtClassRoom.getText());
            ex.setTotal_time(Integer.valueOf((String) cbTotal_Time.getSelectedItem().toString()));
            ex.setUserID(user.getId());
            ex.setId(Integer.valueOf(txID.getText().trim()));
            ex.setStatus((String.valueOf(cbStatus.getSelectedItem())).equals("active") ? 1 : 0);
            System.out.println("exam: " + ex);
            try {
               String mess= bllEx.updateExamDTO(ex);
                insertHeaderExam();
            outModelExam(modelExam, ExamBLL.getListUserExam());
                JOptionPane.showMessageDialog(this,mess,"Thông Báo",JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex1) {
                JOptionPane.showMessageDialog(this, "Không Thể Cập Nhật thông Tin Đề Thi ",
                        "Thông Báo Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không được phép cập nhật đề thi này.",
                    "Thông Báo Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnUpdateExamActionPerformed

    private void btnOpenPanelCreateQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenPanelCreateQuestionActionPerformed
        if (exam == null) {
            JOptionPane.showMessageDialog(this, "Vui Lòng Chọn Đề Thi",
                    "Thông Báo Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            ManageExam.openCreateExamPanel(exam, user);
        }

    }//GEN-LAST:event_btnOpenPanelCreateQuestionActionPerformed

    private void btnCreateExamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateExamActionPerformed
        txID.setText("AuTo");
        if (validateForm()) {
            try {

                ExamDTO ex = new ExamDTO();
                ex.setSubject(txtSubject.getText());
                ex.setQuantity(Integer.parseInt((String) cbQuantity.getSelectedItem()));
                ex.setClass_room(txtClassRoom.getText());
                ex.setStatus(cbStatus.getSelectedIndex());
                ex.setTotal_time(Integer.valueOf((String) cbTotal_Time.getSelectedItem()));
                ex.setUserID(user.getId());
                ex.setStatus(1);
                ResponseDTO res=bllEx.addExamDTO(ex);
             
                clearInput();
                if(res.getStatus()==200){
                    ExamDTO examResult = gson.fromJson(res.getResult(), ExamDTO.class);
                     ManageExam.openCreateExamPanel(examResult, user);
                }
                    
                else JOptionPane.showMessageDialog(this, "Thêm thất bại", "Thông báo", HEIGHT);
//                insertHeaderExam();
//                outModelExam(modelExam, ExamBLL.getListUserExam());

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Không thể tạo đề thi ",
                        "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnCreateExamActionPerformed

    private void tbExamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbExamMouseClicked
        int i = tbExam.getSelectedRow();
        int id = -1;
        if (i >= 0) {

//            if (bllQs.getListQuestion() == null) {
            id = Integer.parseInt(tbExam.getModel().getValueAt(i, 0).toString());
            String subject = tbExam.getModel().getValueAt(i, 1).toString();
            String classroom = tbExam.getModel().getValueAt(i, 2).toString();
            cbQuantity.getModel().setSelectedItem(tbExam.getModel().getValueAt(i, 3));
            cbTotal_Time.getModel().setSelectedItem(tbExam.getModel().getValueAt(i, 4));
            // String time = tbExam.getModel().getValueAt(i, 4).toString();
            txtSubject.setText(subject);
            txtClassRoom.setText(classroom);
            txtUserId.setText("      "+user.getName());
            txID.setText("      "+String.valueOf(id));
            questionEdit.setExamID(exam.getId());
            exam.setId(id);
            exam.setSubject(subject);
            exam.setClass_room(classroom);
            exam.setQuantity(Integer.parseInt(tbExam.getModel().getValueAt(i, 3).toString()));
            exam.setTotal_time(Integer.parseInt(tbExam.getModel().getValueAt(i, 4).toString()));
            exam.setUserID(user.getId());
            exam.setStatus(1);
             //show hide upadte
            if (checkUserTakeExam(String.valueOf(id))) {
                btnUpdateQuestion.setVisible(false);
            } else {
                btnUpdateQuestion.setVisible(true);
            }
          
            //load list Questions
            loadDataQuestion(id);

        }
    }//GEN-LAST:event_tbExamMouseClicked

    private void txtClassRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClassRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClassRoomActionPerformed

    private void cbQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbQuantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbQuantityActionPerformed

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbStatusActionPerformed

    private void btnDeleteExamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteExamActionPerformed
        if(checkUserTakeExam(txID.getText().trim())==false)
        try {
           String mess= bllEx.deleteExamDTO(Integer.parseInt(txID.getText()));
            JOptionPane.showMessageDialog(this,mess,"Thông Báo",JOptionPane.INFORMATION_MESSAGE);
           clearInput();
            insertHeaderExam();
            outModelExam(modelExam, ExamBLL.getListUserExam());

        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Không thể xóa đề thi ",
//                    "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
        }
        else   JOptionPane.showMessageDialog(this, "Không được phép xóa đề thi này.",
                    "Thông Báo Lỗi", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnDeleteExamActionPerformed

    private void cbTotal_TimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTotal_TimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTotal_TimeActionPerformed

    private void tbQuestionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbQuestionMouseClicked
        int i = tbQuestion.getSelectedRow();
        int id = -1;
        if (i >= 0) {
          
            questionEdit.setId(Integer.parseInt(tbQuestion.getModel().getValueAt(i, 0).toString()));
            questionEdit.setQuestion(tbQuestion.getModel().getValueAt(i, 1).toString());
            questionEdit.setA(tbQuestion.getModel().getValueAt(i, 2).toString());
            questionEdit.setB(tbQuestion.getModel().getValueAt(i, 3).toString());
            questionEdit.setC(tbQuestion.getModel().getValueAt(i, 4).toString());
            questionEdit.setD(tbQuestion.getModel().getValueAt(i, 5).toString());
            questionEdit.setAnswer(tbQuestion.getModel().getValueAt(i, 6).toString());
            
        }
    }//GEN-LAST:event_tbQuestionMouseClicked

    private void btnUpdateQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateQuestionActionPerformed
        boolean validate = false;
        if (exam.getId() == null) {
            JOptionPane.showMessageDialog(this, "Không thể cập nhật ! ",
                    "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            validate = true;
        }
//        if (validate == true && checkUserTakeExam(String.valueOf(exam.getId())) != true) {
//            validate = true;
//        } else {
//            JOptionPane.showMessageDialog(this, "Không được phép cập nhật ! ",
//                    "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
//            validate = false;
//            return;
//        }

        if (validate == true && questionEdit.getAnswer() != null) {
            UpdataQuestionPanel updatePanel = new UpdataQuestionPanel(ManageExam.getFrames()[0], false, questionEdit);
            updatePanel.setVisible(true);
            
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn câu hỏi ",
                    "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
        }
         insertHeaderQuestion();
         outModelQuestion(modelQuestion, bllQs.getListQuestion());

    }//GEN-LAST:event_btnUpdateQuestionActionPerformed

    private void tbQuestionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbQuestionMouseExited
        //questionEdit=null;
    }//GEN-LAST:event_tbQuestionMouseExited

    private void tbQuestionMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbQuestionMouseReleased
        //    questionEdit=null;
    }//GEN-LAST:event_tbQuestionMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel HEADER;
    private javax.swing.JButton btnCreateExam;
    private javax.swing.JButton btnDeleteExam;
    private javax.swing.JButton btnOpenPanelCreateQuestion;
    private javax.swing.JButton btnUpdateExam;
    private javax.swing.JButton btnUpdateQuestion;
    private javax.swing.JComboBox<String> cbQuantity;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JComboBox<String> cbTotal_Time;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbSoLuong1;
    private javax.swing.JPanel pInput4;
    private javax.swing.JPanel pInput5;
    private javax.swing.JPanel pInput6;
    private javax.swing.JTable tbExam;
    private javax.swing.JTable tbQuestion;
    private javax.swing.JTextField txID;
    private javax.swing.JTextField txtClassRoom;
    private javax.swing.JTextField txtSubject;
    private javax.swing.JTextField txtUserId;
    // End of variables declaration//GEN-END:variables
}
