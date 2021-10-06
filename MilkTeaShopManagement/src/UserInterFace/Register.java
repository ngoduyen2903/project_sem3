package UserInterFace;

import java.awt.Color;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Register extends javax.swing.JFrame {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet resultset = null;
    private Boolean Add = false, Change = false;
    private String sqlAccounts = "SELECT * FROM Accounts";
    private Detail detail;

    public Register(Detail d) {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        detail = new Detail(d);
        connectionSQLServer();
        display(sqlAccounts);
        disabled();
        loadEmployees();
        txtTrangThai.setForeground(Color.red);
    }

    private void loadEmployees() {
        String sql = "SELECT * FROM Employees";
        txtTenNhanVien.removeAllItems();
        try {
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                txtTenNhanVien.addItem(resultset.getString("EmployeeName").trim());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void connectionSQLServer() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;"
                    + "databaseName=CosmeticStoreManager;user=sa;password=123456");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void display(String sql) {
        TableAccount.removeAll();
        try {
            String[] data = {"Tên Đăng Nhập", "Mật Khẩu", "Tên Nhân Viên", "Ngày Tạo"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("Username").trim());
                vector.add(resultset.getString("PassWord").trim());
                vector.add(resultset.getString("EmployeeName").trim());
                vector.add(new SimpleDateFormat("dd/MM/yyyy").format(resultset.getDate("DateCreated")));
                tbModel.addRow(vector);
            }
            TableAccount.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void enabled() {
        txtTenDangNhap.setEnabled(true);
        txtMatKhau.setEnabled(true);
        txtTenNhanVien.setEnabled(true);
        txtNgayTao.setEnabled(true);
        txtTrangThai.setText("Trạng Thái!");
    }

    private void disabled() {
        txtTenDangNhap.setEnabled(false);
        txtMatKhau.setEnabled(false);
        txtTenNhanVien.setEnabled(false);
        txtNgayTao.setEnabled(false);
    }

    private void refresh() {
        txtTenNhanVien.removeAllItems();
        Add = false;
        Change = false;
        this.txtTenDangNhap.setText("");
        this.txtMatKhau.setText("");
        loadEmployees();
        ((JTextField) this.txtNgayTao.getDateEditor().getUiComponent()).setText("");
        this.btnSua.setEnabled(false);
        this.btnThem.setEnabled(true);
        this.btnLuu.setEnabled(false);
        this.btnXoa.setEnabled(false);
    }

    private void insertAccount() {
        String sqlInsert = "INSERT INTO Accounts (Username,PassWord,EmployeeName,DateCreated) VALUES(?,?,?,?)";
        if (checkEmpty()) {
            try {
                pst = conn.prepareStatement(sqlInsert);
                pst.setString(1, this.txtTenDangNhap.getText());
                pst.setString(2, this.txtMatKhau.getText());
                pst.setString(3, this.txtTenNhanVien.getSelectedItem().toString());
                pst.setDate(4, new java.sql.Date(txtNgayTao.getDate().getTime()));
                pst.executeUpdate();
                display(sqlAccounts);
                disabled();
                refresh();
                txtTrangThai.setText("Đã thêm tài khoản!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateAccount() {
        int Click = TableAccount.getSelectedRow();
        TableModel tbModel = TableAccount.getModel();
        String sqlUpdate = "UPDATE Accounts SET Username=?,PassWord=?,EmployeeName=?,DateCreated=? "
                + "WHERE Username='" + tbModel.getValueAt(Click, 0).toString().trim() + "'";
        if (checkEmpty()) {
            try {
                pst = conn.prepareStatement(sqlUpdate);
                pst.setString(1, this.txtTenDangNhap.getText());
                pst.setString(2, this.txtMatKhau.getText());
                pst.setString(3, this.txtTenNhanVien.getSelectedItem().toString());
                pst.setDate(4, new java.sql.Date(txtNgayTao.getDate().getTime()));
                pst.executeUpdate();
                disabled();
                refresh();
                txtTrangThai.setText("Lưu thay đổi thành công!");
                display(sqlAccounts);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean check() {
        boolean kq = true;
        String sqlCheck = "SELECT * FROM Accounts";
        try {
            pst = conn.prepareStatement(sqlCheck);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                if (this.txtTenDangNhap.getText().equals(resultset.getString("Username").toString().trim())) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    private boolean checkEmpty() {
        boolean kq = true;
        if (this.txtTenDangNhap.getText().equals("")) {
            txtTrangThai.setText("Tên đăng nhập không được trống!");
            return false;
        }
        if (this.txtMatKhau.getText().equals("")) {
            txtTrangThai.setText("Mật khẩu không được trống!");
            return false;
        }
        if (((JTextField) txtNgayTao.getDateEditor().getUiComponent()).getText().equals("")) {
            txtTrangThai.setText("Ngày tạo không được trống!");
            return false;
        }
        return kq;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnHome = new javax.swing.JButton();
        jTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTenDangNhap = new javax.swing.JLabel();
        jMatKhau = new javax.swing.JLabel();
        jTenNhanVien = new javax.swing.JLabel();
        txtTenDangNhap = new javax.swing.JTextField();
        txtMatKhau = new javax.swing.JTextField();
        jNgayTao = new javax.swing.JLabel();
        txtNgayTao = new com.toedter.calendar.JDateChooser();
        txtTenNhanVien = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        btnSua = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableAccount = new javax.swing.JTable();
        txtTrangThai = new javax.swing.JLabel();
        btnLanguage = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Account");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnHome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Home.png"))); // NOI18N
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        jTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitle.setText("ĐĂNG KÍ TÀI KHOẢN");

        jPanel1.setBackground(new java.awt.Color(80, 180, 155));

        jTenDangNhap.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTenDangNhap.setText("Tên Đăng Nhập:");

        jMatKhau.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMatKhau.setText("Mật Khẩu:");

        jTenNhanVien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTenNhanVien.setText("Tên Nhân Viên:");

        jNgayTao.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jNgayTao.setText("Ngày Tạo:");

        txtNgayTao.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTenDangNhap)
                    .addComponent(jTenNhanVien, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTenDangNhap, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(txtTenNhanVien, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jMatKhau, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jNgayTao, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMatKhau)
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTenDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTenDangNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(8, 8, 8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jMatKhau)
                            .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTenNhanVien)
                        .addComponent(txtTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jNgayTao))
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(80, 180, 155));

        btnSua.setBackground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Update.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setEnabled(false);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(255, 255, 255));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add_1.png"))); // NOI18N
        btnThem.setText("Đăng ký");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 255, 255));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete_1.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setEnabled(false);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLuu.setBackground(new java.awt.Color(255, 255, 255));
        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.setEnabled(false);
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnThem)
                        .addComponent(btnSua)
                        .addComponent(btnXoa)
                        .addComponent(btnLuu)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(80, 180, 155));

        TableAccount.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TableAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableAccountMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableAccount);

        txtTrangThai.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTrangThai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTrangThai.setText("Trạng Thái");
        txtTrangThai.setFocusable(false);
        txtTrangThai.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTrangThai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        btnLanguage.setBackground(new java.awt.Color(255, 255, 255));
        btnLanguage.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLanguage.setText("English");
        btnLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLanguageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                            .addComponent(jTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(39, 39, 39)
                            .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(21, 21, 21)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLanguage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TableAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableAccountMouseClicked
        int Click = TableAccount.getSelectedRow();
        TableModel tbModel = TableAccount.getModel();
        txtTenNhanVien.removeAllItems();
        txtTenDangNhap.setText(tbModel.getValueAt(Click, 0).toString());
        txtMatKhau.setText(tbModel.getValueAt(Click, 1).toString());
        txtTenNhanVien.addItem(tbModel.getValueAt(Click, 2).toString());
        ((JTextField) txtNgayTao.getDateEditor().getUiComponent()).setText(tbModel.getValueAt(Click, 3).toString());
        btnXoa.setEnabled(true);
        btnSua.setEnabled(true);
    }//GEN-LAST:event_TableAccountMouseClicked

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        HomeAdmin home = new HomeAdmin(detail);
        this.setVisible(false);
        home.setVisible(true);
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        refresh();
        Add = true;
        enabled();
        btnThem.setEnabled(false);
        btnLuu.setEnabled(true);
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int Click = TableAccount.getSelectedRow();
        TableModel tbModel = TableAccount.getModel();
        Add = false;
        Change = true;
        enabled();
        loadEmployees();
        if (tbModel.getValueAt(Click, 0).toString().trim().equals("Admin")) {
            txtTenDangNhap.setEnabled(false);
        }
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnThem.setEnabled(false);
        btnLuu.setEnabled(true);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refresh();
        disabled();
        display(sqlAccounts);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        if (Add == true) {
            if (check()) {
                insertAccount();
            } else {
                txtTrangThai.setText("Tên đăng nhập đã tồn tại!");
            }
        } else if (Change == true) {
            updateAccount();
        }
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn muốn xóa tài khoản?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            if (this.txtTenDangNhap.getText().equals("Admin")) {
                this.txtTrangThai.setText("Không thể xóa tài khoản của Admin");
            } else {
                String sqlDelete = "DELETE FROM Accounts WHERE Username = ? AND PassWord=? AND EmployeeName=? AND DateCreated=?";
                try {
                    pst = conn.prepareStatement(sqlDelete);
                    pst.setString(1, this.txtTenDangNhap.getText());
                    pst.setString(2, this.txtMatKhau.getText());
                    pst.setString(3, this.txtTenNhanVien.getSelectedItem().toString());
                    pst.setDate(4, new java.sql.Date(txtNgayTao.getDate().getTime()));
                    pst.executeUpdate();
                    this.txtTrangThai.setText("Đã xóa tài khoản!");
                    display(sqlAccounts);
                    refresh();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int Click = JOptionPane.showConfirmDialog(null, "Bạn muốn đóng ứng dụng?", "Thông Báo", 2);
        if (Click == JOptionPane.OK_OPTION) {
            System.exit(0);
        } else {
            if (Click == JOptionPane.CANCEL_OPTION) {
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        HomeAdmin home= new HomeAdmin(detail);
        this.setVisible(false);
        home.setVisible(true);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageActionPerformed
        // TODO add your handling code here:
        String file = btnLanguage.isSelected() ? "Internationalization/English" : "Internationalization/Vietnamese";
        String languagecodes = btnLanguage.isSelected() ? "en" : "vi";
        String countrycodes = btnLanguage.isSelected() ? "US" : "VN";
        String togglebutton = btnLanguage.isSelected() ? "English" : "Vietnamese";
        btnLanguage.setText(togglebutton);
        Locale currentLocale = new Locale(languagecodes, countrycodes);
        ResourceBundle language = ResourceBundle.getBundle(file, currentLocale);
        jTitle.setText(language.getString("tieudedangky"));
        jTenDangNhap.setText(language.getString("tendangnhap"));
        jTenNhanVien.setText(language.getString("tennhanvien"));
        jMatKhau.setText(language.getString("matkhau"));
        jNgayTao.setText(language.getString("ngaytao"));
        btnThem.setText(language.getString("btnthem"));
        btnSua.setText(language.getString("btnsua"));
        btnXoa.setText(language.getString("btnxoa"));
        btnLuu.setText(language.getString("btnluu"));
        txtTrangThai.setText(language.getString("trangthai"));       
    }//GEN-LAST:event_btnLanguageActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail = new Detail();
                new Register(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableAccount;
    private javax.swing.JButton btnHome;
    private javax.swing.JToggleButton btnLanguage;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jMatKhau;
    private javax.swing.JLabel jNgayTao;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jTenDangNhap;
    private javax.swing.JLabel jTenNhanVien;
    private javax.swing.JLabel jTitle;
    private javax.swing.JTextField txtMatKhau;
    private com.toedter.calendar.JDateChooser txtNgayTao;
    private javax.swing.JTextField txtTenDangNhap;
    private javax.swing.JComboBox<String> txtTenNhanVien;
    private javax.swing.JLabel txtTrangThai;
    // End of variables declaration//GEN-END:variables
}
