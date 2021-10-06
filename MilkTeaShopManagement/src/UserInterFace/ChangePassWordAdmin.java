package UserInterFace;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChangePassWordAdmin extends javax.swing.JFrame {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet resultset = null;

    public ChangePassWordAdmin() {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        connectionSQLServer();
        loadComboBox();
        disbled();
        txtTrangThai.setForeground(Color.red);
    }

    private void clearTxt() {
        txtMatKhauHienTai.setText("");
        txtMatKhauMoi.setText("");
        txtNhapLaiMatKhauMoi.setText("");
        txtTrangThai.setText("Trạng Thái!");
    }

    private void disbled() {
        txtMatKhauHienTai.setEnabled(false);
        txtMatKhauMoi.setEnabled(false);
        txtNhapLaiMatKhauMoi.setEnabled(false);
    }

    private void enabled() {
        txtMatKhauHienTai.setEnabled(true);
        txtMatKhauMoi.setEnabled(true);
        txtNhapLaiMatKhauMoi.setEnabled(true);
    }

    private void connectionSQLServer() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;"
                    + "databaseName=MilkTeaShopManagement;user=sa;password=123456");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadComboBox() {
        String sql = "SELECT * FROM Administrator";
        try {
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                this.txtTenDangNhap.addItem(resultset.getString("Username").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean check() {
        boolean kq = false;
        String sql = "SELECT * FROM Administrator WHERE Username=? AND Password=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) txtTenDangNhap.getSelectedItem());
            pst.setString(2, String.valueOf(this.txtMatKhauHienTai.getPassword()));
            resultset = pst.executeQuery();
            if (resultset.next()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    private boolean checkEmpty() {
        boolean kq = true;
        if (String.valueOf(this.txtMatKhauHienTai.getPassword()).length() == 0) {
            txtTrangThai.setText("Current Password cannot be empty. Please enter!");
            return false;
        }
        if (String.valueOf(this.txtMatKhauMoi.getPassword()).length() == 0) {
            txtTrangThai.setText("New Password cannot be empty. Please enter!");
            return false;
        }
        if (String.valueOf(this.txtNhapLaiMatKhauMoi.getPassword()).length() == 0) {
            txtTrangThai.setText("Enter a New Password cannot be empty. Please enter!");
            return false;
        }
        return kq;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTenDangNhap = new javax.swing.JLabel();
        txtTenDangNhap = new javax.swing.JComboBox<>();
        jNhapLaiMatKhauMoi = new javax.swing.JLabel();
        jMatKhauHienTai = new javax.swing.JLabel();
        txtMatKhauHienTai = new javax.swing.JPasswordField();
        jMatKhauMoi = new javax.swing.JLabel();
        txtMatKhauMoi = new javax.swing.JPasswordField();
        txtNhapLaiMatKhauMoi = new javax.swing.JPasswordField();
        jPanel3 = new javax.swing.JPanel();
        btnDangNhap = new javax.swing.JButton();
        btnDongy = new javax.swing.JButton();
        txtTrangThai = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnLanguage = new javax.swing.JToggleButton();
        jLabel4 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ChangePassword");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(80, 180, 155));

        jTenDangNhap.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jTenDangNhap.setForeground(new java.awt.Color(255, 255, 255));
        jTenDangNhap.setText("Username:");

        txtTenDangNhap.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                txtTenDangNhapPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jNhapLaiMatKhauMoi.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jNhapLaiMatKhauMoi.setForeground(new java.awt.Color(255, 255, 255));
        jNhapLaiMatKhauMoi.setText("Enter a New Password:");

        jMatKhauHienTai.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jMatKhauHienTai.setForeground(new java.awt.Color(255, 255, 255));
        jMatKhauHienTai.setText("Current Password:");

        jMatKhauMoi.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jMatKhauMoi.setForeground(new java.awt.Color(255, 255, 255));
        jMatKhauMoi.setText("New Password:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jNhapLaiMatKhauMoi)
                    .addComponent(jTenDangNhap, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jMatKhauHienTai, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jMatKhauMoi, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTenDangNhap, 0, 210, Short.MAX_VALUE)
                    .addComponent(txtNhapLaiMatKhauMoi)
                    .addComponent(txtMatKhauMoi)
                    .addComponent(txtMatKhauHienTai))
                .addGap(141, 141, 141))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTenDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jMatKhauHienTai, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMatKhauHienTai, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jMatKhauMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMatKhauMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jNhapLaiMatKhauMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNhapLaiMatKhauMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 26, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 69, 610, 220));

        jPanel3.setBackground(new java.awt.Color(80, 180, 155));

        btnDangNhap.setBackground(new java.awt.Color(255, 255, 255));
        btnDangNhap.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnDangNhap.setText("Login");
        btnDangNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDangNhapMouseClicked(evt);
            }
        });

        btnDongy.setBackground(new java.awt.Color(255, 255, 255));
        btnDongy.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnDongy.setText("Agree");
        btnDongy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDongyMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(btnDongy, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(btnDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(116, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDangNhap, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(btnDongy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 610, -1));

        txtTrangThai.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTrangThai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTrangThai.setText("Status");
        txtTrangThai.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(txtTrangThai, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 610, 30));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, -1));

        jPanel5.setBackground(new java.awt.Color(80, 180, 155));

        btnLanguage.setBackground(new java.awt.Color(255, 255, 255));
        btnLanguage.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLanguage.setText("English");
        btnLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLanguageActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CHANGE PASSWORD(Admin)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(134, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 610, 70));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDangNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDangNhapMouseClicked
        Login dangnhap = new Login();
        this.setVisible(false);
        dangnhap.setVisible(true);
    }//GEN-LAST:event_btnDangNhapMouseClicked

    private void btnDongyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDongyMouseClicked
        if (checkEmpty()) {
            if (check()) {
                if (String.valueOf(this.txtMatKhauMoi.getPassword()).equals(String.valueOf(this.txtNhapLaiMatKhauMoi.getPassword()))) {
                    String sqlChange = "UPDATE Administrator SET Passưord=? WHERE Username=N'" + (String) txtTenDangNhap.getSelectedItem() + "'";
                    try {
                        pst = conn.prepareStatement(sqlChange);
                        pst.setString(1, String.valueOf(this.txtMatKhauMoi.getPassword()));
                        pst.executeUpdate();
                        disbled();
                        txtTrangThai.setText("Password Change Successful!");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    txtTrangThai.setText("Re-enter incorrect new password!");
                }
            } else {
                txtTrangThai.setText("The current password is incorrect!");
            }
        }
    }//GEN-LAST:event_btnDongyMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int Click = JOptionPane.showConfirmDialog(null, "Do you want to close the application?", "Notification", 2);
        if (Click == JOptionPane.OK_OPTION) {
            System.exit(0);
        } else {
            if (Click == JOptionPane.CANCEL_OPTION) {
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageActionPerformed
        String file = btnLanguage.isSelected() ? "Internationalization/English" : "Internationalization/Vietnamese";
        String languagecodes = btnLanguage.isSelected() ? "en" : "vi";
        String countrycodes = btnLanguage.isSelected() ? "US" : "VN";
        String togglebutton = btnLanguage.isSelected() ? "English" : "Vietnamese";
        btnLanguage.setText(togglebutton);
        Locale currentLocale = new Locale(languagecodes, countrycodes);
        ResourceBundle language = ResourceBundle.getBundle(file, currentLocale);
        //jTitle.setText(language.getString("tieudedoimatkhau"));
        jTenDangNhap.setText(language.getString("tendangnhap"));
        jMatKhauHienTai.setText(language.getString("matkhauhientai"));
        jMatKhauMoi.setText(language.getString("matkhaumoi"));
        jNhapLaiMatKhauMoi.setText(language.getString("nhaplaimatkhaumoi"));
        btnDongy.setText(language.getString("dongy"));
        btnDangNhap.setText(language.getString("dangnhap"));
        txtTrangThai.setText(language.getString("trangthai"));
    }//GEN-LAST:event_btnLanguageActionPerformed

    private void txtTenDangNhapPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_txtTenDangNhapPopupMenuWillBecomeInvisible
        enabled();
        clearTxt();
    }//GEN-LAST:event_txtTenDangNhapPopupMenuWillBecomeInvisible

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChangePassWordAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChangePassWordAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChangePassWordAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChangePassWordAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChangePassWordAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangNhap;
    private javax.swing.JButton btnDongy;
    private javax.swing.JToggleButton btnLanguage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jMatKhauHienTai;
    private javax.swing.JLabel jMatKhauMoi;
    private javax.swing.JLabel jNhapLaiMatKhauMoi;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel jTenDangNhap;
    private javax.swing.JPasswordField txtMatKhauHienTai;
    private javax.swing.JPasswordField txtMatKhauMoi;
    private javax.swing.JPasswordField txtNhapLaiMatKhauMoi;
    private javax.swing.JComboBox<String> txtTenDangNhap;
    private javax.swing.JLabel txtTrangThai;
    // End of variables declaration//GEN-END:variables
}
