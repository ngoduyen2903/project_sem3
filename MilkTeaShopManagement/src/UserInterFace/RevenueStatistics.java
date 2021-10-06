package UserInterFace;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class RevenueStatistics extends javax.swing.JFrame {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet resultset = null;
    private Detail detail;
    private String sqlRevenue = "SELECT * FROM Revenue";
    private String date = "01/10/2021";

    public RevenueStatistics(Detail d) {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        loadDate();
        detail = new Detail(d);
        connectionSQLServer();
        displayInformation(sqlRevenue);

    }

    private void loadDate() {
        try {
            jDateChooser1.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        jDateChooser2.setDate(new java.util.Date());
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

    private void displayInformation(String sql) {
        delete();
        int count = 0;
        long tongTien = 0;
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        try {
            String[] data = {"Sales Employee", "Date", "Time", "Total Bill Amount", "Money", "Surplus"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("EmployeeName").trim());
                vector.add(new SimpleDateFormat("dd/MM/yyyy").format(resultset.getDate("Date")));
                vector.add(resultset.getString("Time").trim());
                vector.add(resultset.getString("TotalMoney").trim());
                vector.add(resultset.getString("Money").trim());
                vector.add(resultset.getString("Surplus").trim());
                tbModel.addRow(vector);
                String[] s = resultset.getString("TotalMoney").trim().split("\\s");
                tongTien = convertedToNumbers(s[0]) + tongTien;
                count++;
                insertRevenue(resultset.getString("EmployeeName").trim(), resultset.getDate("Date"), resultset.getString("Time").trim(), resultset.getString("TotalMoney").trim(), resultset.getString("Money").trim(), resultset.getString("Surplus").trim());
            }
            tableThongKe.setModel(tbModel);
            txtTongSoHoaDonBanRa.setText(String.valueOf(count));
            txtTongTienHoaDon.setText(formatter.format(tongTien) + " " + "VND");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private long convertedToNumbers(String s) {
        String number = "";
        String[] array = s.replace(",", " ").split("\\s");
        for (String i : array) {
            number = number.concat(i);
        }
        return Long.parseLong(number);
    }

    private void delete() {
        String sqlDelete = "DELETE FROM Revenue";
        try {
            pst = conn.prepareStatement(sqlDelete);
            pst.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void insertRevenue(String name, Date date, String time, String tongtien, String tien, String tiendu) {
        String sqlPay = "INSERT INTO [dbo].[Statistic] ([EmployeeName],[Date],[Time],[IntoMoney],[Money],[Surplus])\n"
                + "VALUES (?, ?, ?, ?, ?, ?);";
        try {
            pst = conn.prepareStatement(sqlPay);
            pst.setString(1, name);
            pst.setDate(2, date);
            pst.setString(3, time);
            pst.setString(4, tongtien);
            pst.setString(5, tien);
            pst.setString(6, tiendu);
            pst.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBackHome = new javax.swing.JButton();
        jTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jThongKeTuNgay = new javax.swing.JLabel();
        jDenNgay = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        btnThongKe = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableThongKe = new javax.swing.JTable();
        jTongSoHoaDonBanRa = new javax.swing.JLabel();
        txtTongSoHoaDonBanRa = new javax.swing.JLabel();
        jTongTienHoaDon = new javax.swing.JLabel();
        txtTongTienHoaDon = new javax.swing.JLabel();
        btnLanguage = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RevenueStatistics");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnBackHome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBackHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Home.png"))); // NOI18N
        btnBackHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHomeActionPerformed(evt);
            }
        });

        jTitle.setFont(new java.awt.Font("Tahoma", 1, 26)); // NOI18N
        jTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitle.setText("REVENUE STATICTIS");

        jPanel1.setBackground(new java.awt.Color(80, 180, 155));

        jThongKeTuNgay.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jThongKeTuNgay.setText("Thống Kê Từ Ngày:");

        jDenNgay.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jDenNgay.setText("Đến Ngày:");

        jDateChooser1.setDateFormatString("dd/MM/yyyy");

        jDateChooser2.setDateFormatString("dd/MM/yyyy");

        btnThongKe.setBackground(new java.awt.Color(255, 255, 255));
        btnThongKe.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Revenue.png"))); // NOI18N
        btnThongKe.setText("Thống Kê");
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jThongKeTuNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDenNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThongKe)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jThongKeTuNgay))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDenNgay))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnThongKe, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        tableThongKe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tableThongKe);

        jTongSoHoaDonBanRa.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jTongSoHoaDonBanRa.setText("Tổng Số Hóa Đơn Bán Ra:");

        txtTongSoHoaDonBanRa.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTongSoHoaDonBanRa.setText("0");

        jTongTienHoaDon.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jTongTienHoaDon.setText("Tổng Tiền Hóa Đơn:");

        txtTongTienHoaDon.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTongTienHoaDon.setText("0 VND");

        btnLanguage.setBackground(new java.awt.Color(255, 255, 255));
        btnLanguage.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                        .addComponent(jTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125)
                        .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jTongSoHoaDonBanRa)
                        .addGap(18, 18, 18)
                        .addComponent(txtTongSoHoaDonBanRa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTongTienHoaDon)
                        .addGap(18, 18, 18)
                        .addComponent(txtTongTienHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBackHome)
                    .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTongSoHoaDonBanRa)
                    .addComponent(txtTongSoHoaDonBanRa)
                    .addComponent(jTongTienHoaDon)
                    .addComponent(txtTongTienHoaDon))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
        String sqlRevenue = "SELECT * FROM Revenue WHERE Date BETWEEN '" + new java.sql.Date(jDateChooser1.getDate().getTime()) + "' AND '" + new java.sql.Date(jDateChooser2.getDate().getTime()) + "'";
        displayInformation(sqlRevenue);
    }//GEN-LAST:event_btnThongKeActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        displayInformation(sqlRevenue);
        loadDate();
    }//GEN-LAST:event_btnRefreshActionPerformed

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

    private void btnBackHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackHomeActionPerformed
        HomeAdmin home = new HomeAdmin(detail);
        this.setVisible(false);
        home.setVisible(true);
    }//GEN-LAST:event_btnBackHomeActionPerformed

    private void btnLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageActionPerformed
        String file = btnLanguage.isSelected() ? "Internationalization/English" : "Internationalization/Vietnamese";
        String languagecodes = btnLanguage.isSelected() ? "en" : "vi";
        String countrycodes = btnLanguage.isSelected() ? "US" : "VN";
        String togglebutton = btnLanguage.isSelected() ? "English" : "Vietnamese";
        btnLanguage.setText(togglebutton);
        Locale currentLocale = new Locale(languagecodes, countrycodes);
        ResourceBundle language = ResourceBundle.getBundle(file, currentLocale);
        jTitle.setText(language.getString("tieudethongke"));
        jThongKeTuNgay.setText(language.getString("thongketungay"));
        jDenNgay.setText(language.getString("denngay"));
        btnThongKe.setText(language.getString("btnthongke"));
        jTongSoHoaDonBanRa.setText(language.getString("tongsohoadonbanra"));
        jTongTienHoaDon.setText(language.getString("tongtienhoadon"));
    }//GEN-LAST:event_btnLanguageActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail = new Detail();
                new RevenueStatistics(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackHome;
    private javax.swing.JToggleButton btnLanguage;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnThongKe;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jDenNgay;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jThongKeTuNgay;
    private javax.swing.JLabel jTitle;
    private javax.swing.JLabel jTongSoHoaDonBanRa;
    private javax.swing.JLabel jTongTienHoaDon;
    private javax.swing.JTable tableThongKe;
    private javax.swing.JLabel txtTongSoHoaDonBanRa;
    private javax.swing.JLabel txtTongTienHoaDon;
    // End of variables declaration//GEN-END:variables
}
