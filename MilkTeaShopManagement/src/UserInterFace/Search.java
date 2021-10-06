package UserInterFace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Search extends javax.swing.JFrame {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet resultset = null;
    private Detail detail;
    private String sqlAccount = "SELECT * FROM Accounts";
    private String sqlEmployee = "SELECT * FROM Employees";
    private String sqlProduct = "SELECT * FROM Product";
    private String sqlOrders = "SELECT * FROM Orders";
    private String sqlPosition = "SELECT * FROM Position";
    private String sqlProducer = "SELECT * FROM Producer";
    private String sqlClassify = "SELECT * FROM Classify";

    public Search(Detail d) {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        detail = new Detail(d);
        connectionSQLServer();
        loadData();
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

    private void loadData() {
        displayAccount(sqlAccount);
        displayEmployees(sqlEmployee);
        displayProduct(sqlProduct);
        displayOrders(sqlOrders);
        displayProducer(sqlProducer);
        displayClassify(sqlClassify);
        displayPosition(sqlPosition);
    }

    private void displayAccount(String sql) {
        TableAccount.removeAll();
        try {
            String[] data = {"Tên Đăng Nhập", "Tên Nhân Viên", "Ngày Tạo Tài Khoản"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("Username").trim());
                vector.add(resultset.getString("EmployeeName").trim());
                vector.add(new SimpleDateFormat("dd/MM/yyyy").format(resultset.getDate("DateCreated")));
                tbModel.addRow(vector);
            }
            TableAccount.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayEmployees(String sql) {
        try {
            String[] data = {"Chức Vụ", "Mã Nhân Viên", "Họ Tên", "Bậc Lương", "Năm Sinh", "Giới Tính", "Địa Chỉ", "Điện Thoại", "Email", "Lương"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("Position").trim());
                vector.add(resultset.getString("EmployeeID").trim());
                vector.add(resultset.getString("EmployeeName").trim());
                vector.add(resultset.getInt("Level"));
                vector.add(new SimpleDateFormat("dd/MM/yyyy").format(resultset.getDate("YearofBirth")));
                vector.add(resultset.getString("Sex").trim());
                vector.add(resultset.getString("Address").trim());
                vector.add(resultset.getString("Phone").trim());
                vector.add(resultset.getString("Email").trim());
                vector.add(resultset.getString("Payroll").trim());
                tbModel.addRow(vector);
            }
            tableEmployees.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayProduct(String sql) {
        tableProduct.removeAll();
        try {
            String[] data = {"Mã Sản Phẩm", "Loại Sản Phẩm", "Tên Sản Phẩm", "Nhà Sản Xuất", "Thời Gian Bảo Hành", "Số Lượng Còn", "Đơn Vị", "Giá"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("ProductID").trim());
                vector.add(resultset.getString("Classify").trim());
                vector.add(resultset.getString("ProductName").trim());
                vector.add(resultset.getString("Producername").trim());
                vector.add(resultset.getInt("WarrantyPeriod") + " " + resultset.getString("SingleTime").trim());
                vector.add(resultset.getInt("QuantityRemaining"));
                vector.add(resultset.getString("Unit").trim());
                vector.add(resultset.getString("Price").trim());
                tbModel.addRow(vector);
            }
            tableProduct.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void displayOrders(String sql) {
        tableOrder.removeAll();
        try {
            String[] data = {"Mã Đơn Hàng", "Khách Hàng", "Địa Chỉ", "Số Điện Thoại", "Sản Phẩm", "Số Lượng", "Giá", "Bảo Hành", "Thành Tiền", "Ngày Đặt", "Thanh Toán"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("OrderID").trim());
                vector.add(resultset.getString("CustomerName").trim());
                vector.add(resultset.getString("Address").trim());
                vector.add(resultset.getString("Phone").trim());
                vector.add(resultset.getString("ProductName").trim());
                vector.add(resultset.getInt("Amount"));
                vector.add(resultset.getString("Price").trim());
                vector.add(resultset.getString("WarrantyPeriod").trim());
                vector.add(resultset.getString("IntoMoney").trim());
                vector.add(new SimpleDateFormat("dd/MM/yyyy").format(resultset.getDate("Date")));
                vector.add(resultset.getString("PaymentMethods").trim());
                tbModel.addRow(vector);
            }
            tableOrder.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayProducer(String sql) {
        tableProducer.removeAll();
        try {
            String[] data = {"Mã Nhà Sản Xuất", "Nhà Sản Xuất", "Địa Chỉ", "Điện Thoại", "Email"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("ProducerID").trim());
                vector.add(resultset.getString("ProducerName").trim());
                vector.add(resultset.getString("Address").trim());
                vector.add(resultset.getString("Phone").trim());
                vector.add(resultset.getString("Email").trim());
                tbModel.addRow(vector);
            }
            tableProducer.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayClassify(String sql) {
        tableClassify.removeAll();
        try {
            String[] data = {"Mã Loại", "Loại Sản Phẩm"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("ClassifyID").trim());
                vector.add(resultset.getString("Classify").trim());
                tbModel.addRow(vector);
            }
            tableClassify.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayPosition(String sql) {
        tablePosition.removeAll();
        try {
            String[] data = {"Mã Chức Vụ", "Chức Vụ", "Lương Cơ Bản"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("PositionID").trim());
                vector.add(resultset.getString("Position").trim());
                vector.add(resultset.getString("Payroll").trim());
                tbModel.addRow(vector);
            }
            tablePosition.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton8 = new javax.swing.JButton();
        jTitle = new javax.swing.JLabel();
        btnBackHome = new javax.swing.JButton();
        menuTaiKhoan = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTimKiemTaiKhoan = new javax.swing.JLabel();
        btnTimKiemTaiKhoan = new javax.swing.JTextField();
        btnRefreshAccount = new javax.swing.JButton();
        btnTimTaiKhoan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableAccount = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnRefreshNhanVien = new javax.swing.JButton();
        btnTimKiemNhanVien = new javax.swing.JButton();
        txtTimKiemNhanVien = new javax.swing.JTextField();
        jTimKiemNhanVien = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableEmployees = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnRefreshSanPham = new javax.swing.JButton();
        btnTimKiemSanPham = new javax.swing.JButton();
        txtTimKiemSanPham = new javax.swing.JTextField();
        jTimKiemSanPham = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableProduct = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnRefreshDonHang = new javax.swing.JButton();
        btnTimKiemDonHang = new javax.swing.JButton();
        txtTimKiemDonHang = new javax.swing.JTextField();
        jTimKiemDonHang = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableOrder = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        btnRefresHNSX = new javax.swing.JButton();
        btnTimKiemNSX = new javax.swing.JButton();
        txtTimKiemNSX = new javax.swing.JTextField();
        jTimKiemNSX = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableProducer = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        btnRefreshChucVu = new javax.swing.JButton();
        btnTimKiemChucVu = new javax.swing.JButton();
        txtTimKiemChucVu = new javax.swing.JTextField();
        jTimKiemChucVu = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablePosition = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        btnRefreshLoaiSP = new javax.swing.JButton();
        btnTimKiemLoaiSP = new javax.swing.JButton();
        txtTimKiemLoaiSP = new javax.swing.JTextField();
        jTimKiemLoaiSanPham = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableClassify = new javax.swing.JTable();
        btnLanguage = new javax.swing.JToggleButton();

        jButton8.setText("jButton8");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Search");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTitle.setFont(new java.awt.Font("Tahoma", 1, 25)); // NOI18N
        jTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitle.setText("TÌM KIẾM");

        btnBackHome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBackHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Home.png"))); // NOI18N
        btnBackHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHomeActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        jTimKiemTaiKhoan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTimKiemTaiKhoan.setText("Tìm Kiếm:");

        btnRefreshAccount.setBackground(new java.awt.Color(204, 255, 153));
        btnRefreshAccount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRefreshAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshAccountActionPerformed(evt);
            }
        });

        btnTimTaiKhoan.setBackground(new java.awt.Color(204, 255, 153));
        btnTimTaiKhoan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimTaiKhoan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Find.png"))); // NOI18N
        btnTimTaiKhoan.setText("Tìm Kiếm");
        btnTimTaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimTaiKhoanActionPerformed(evt);
            }
        });

        TableAccount.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(TableAccount);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTimKiemTaiKhoan)
                        .addGap(18, 18, 18)
                        .addComponent(btnTimKiemTaiKhoan)
                        .addGap(18, 18, 18)
                        .addComponent(btnTimTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefreshAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTimTaiKhoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTimKiemTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTimKiemTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnRefreshAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE))
        );

        menuTaiKhoan.addTab("Tài Khoản", jPanel1);

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        btnRefreshNhanVien.setBackground(new java.awt.Color(204, 255, 153));
        btnRefreshNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshNhanVienActionPerformed(evt);
            }
        });

        btnTimKiemNhanVien.setBackground(new java.awt.Color(204, 255, 153));
        btnTimKiemNhanVien.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimKiemNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Find.png"))); // NOI18N
        btnTimKiemNhanVien.setText("Tìm Kiếm");
        btnTimKiemNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemNhanVienActionPerformed(evt);
            }
        });

        jTimKiemNhanVien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTimKiemNhanVien.setText("Tìm Kiếm:");

        tableEmployees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tableEmployees);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTimKiemNhanVien)
                        .addGap(38, 38, 38)
                        .addComponent(txtTimKiemNhanVien)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTimKiemNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefreshNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRefreshNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtTimKiemNhanVien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTimKiemNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTimKiemNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE))
        );

        menuTaiKhoan.addTab("Nhân Viên", jPanel2);

        jPanel3.setBackground(new java.awt.Color(204, 255, 204));

        btnRefreshSanPham.setBackground(new java.awt.Color(204, 255, 153));
        btnRefreshSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshSanPhamActionPerformed(evt);
            }
        });

        btnTimKiemSanPham.setBackground(new java.awt.Color(204, 255, 153));
        btnTimKiemSanPham.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimKiemSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Find.png"))); // NOI18N
        btnTimKiemSanPham.setText("Tìm Kiếm");
        btnTimKiemSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemSanPhamActionPerformed(evt);
            }
        });

        jTimKiemSanPham.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTimKiemSanPham.setText("Tìm Kiếm:");

        tableProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tableProduct);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jTimKiemSanPham)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKiemSanPham)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTimKiemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefreshSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRefreshSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jTimKiemSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTimKiemSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTimKiemSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menuTaiKhoan.addTab("Sản Phẩm", jPanel3);

        jPanel4.setBackground(new java.awt.Color(204, 255, 204));

        btnRefreshDonHang.setBackground(new java.awt.Color(204, 255, 153));
        btnRefreshDonHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshDonHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshDonHangActionPerformed(evt);
            }
        });

        btnTimKiemDonHang.setBackground(new java.awt.Color(204, 255, 153));
        btnTimKiemDonHang.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimKiemDonHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Find.png"))); // NOI18N
        btnTimKiemDonHang.setText("Tìm Kiếm");
        btnTimKiemDonHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemDonHangActionPerformed(evt);
            }
        });

        jTimKiemDonHang.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTimKiemDonHang.setText("Tìm Kiếm:");

        tableOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tableOrder);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTimKiemDonHang)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKiemDonHang)
                        .addGap(18, 18, 18)
                        .addComponent(btnTimKiemDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefreshDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(16, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimKiemDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTimKiemDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRefreshDonHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTimKiemDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        menuTaiKhoan.addTab("Đơn Hàng", jPanel4);

        jPanel5.setBackground(new java.awt.Color(204, 255, 204));

        btnRefresHNSX.setBackground(new java.awt.Color(204, 255, 153));
        btnRefresHNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefresHNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresHNSXActionPerformed(evt);
            }
        });

        btnTimKiemNSX.setBackground(new java.awt.Color(204, 255, 153));
        btnTimKiemNSX.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimKiemNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Find.png"))); // NOI18N
        btnTimKiemNSX.setText("Tìm Kiếm");
        btnTimKiemNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemNSXActionPerformed(evt);
            }
        });

        txtTimKiemNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemNSXActionPerformed(evt);
            }
        });

        jTimKiemNSX.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTimKiemNSX.setText("Tìm Kiếm:");

        tableProducer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(tableProducer);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jTimKiemNSX)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimKiemNSX)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTimKiemNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefresHNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTimKiemNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTimKiemNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTimKiemNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnRefresHNSX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuTaiKhoan.addTab("Nhà Sản Xuất", jPanel5);

        jPanel7.setBackground(new java.awt.Color(204, 255, 204));

        btnRefreshChucVu.setBackground(new java.awt.Color(204, 255, 153));
        btnRefreshChucVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshChucVuActionPerformed(evt);
            }
        });

        btnTimKiemChucVu.setBackground(new java.awt.Color(204, 255, 153));
        btnTimKiemChucVu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimKiemChucVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Find.png"))); // NOI18N
        btnTimKiemChucVu.setText("Tìm Kiếm");
        btnTimKiemChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemChucVuActionPerformed(evt);
            }
        });

        txtTimKiemChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemChucVuActionPerformed(evt);
            }
        });

        jTimKiemChucVu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTimKiemChucVu.setText("Tìm Kiếm:");

        tablePosition.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(tablePosition);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jTimKiemChucVu)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKiemChucVu)
                        .addGap(18, 18, 18)
                        .addComponent(btnTimKiemChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefreshChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTimKiemChucVu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTimKiemChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTimKiemChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnRefreshChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        menuTaiKhoan.addTab("Chức Vụ", jPanel7);

        jPanel6.setBackground(new java.awt.Color(204, 255, 204));

        btnRefreshLoaiSP.setBackground(new java.awt.Color(204, 255, 153));
        btnRefreshLoaiSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshLoaiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshLoaiSPActionPerformed(evt);
            }
        });

        btnTimKiemLoaiSP.setBackground(new java.awt.Color(204, 255, 153));
        btnTimKiemLoaiSP.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimKiemLoaiSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Search_1.png"))); // NOI18N
        btnTimKiemLoaiSP.setText("Tìm Kiếm");
        btnTimKiemLoaiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemLoaiSPActionPerformed(evt);
            }
        });

        txtTimKiemLoaiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemLoaiSPActionPerformed(evt);
            }
        });

        jTimKiemLoaiSanPham.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTimKiemLoaiSanPham.setText("Tìm Kiếm:");

        tableClassify.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(tableClassify);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jTimKiemLoaiSanPham)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKiemLoaiSP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTimKiemLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefreshLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRefreshLoaiSP, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                            .addComponent(btnTimKiemLoaiSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimKiemLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTimKiemLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
        );

        menuTaiKhoan.addTab("Loại Sản Phẩm", jPanel6);

        btnLanguage.setBackground(new java.awt.Color(204, 255, 153));
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95)
                .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
            .addComponent(menuTaiKhoan)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnLanguage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(menuTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimTaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimTaiKhoanActionPerformed
        String sqlFind = "SELECT * FROM Accounts WHERE Username LIKE N'%"
                + this.btnTimKiemTaiKhoan.getText()
                + "%' or EmployeeName like N'%"
                + this.btnTimKiemTaiKhoan.getText()
                + "%' or DateCreated like N'%"
                + this.btnTimKiemTaiKhoan.getText() + "%'";
        displayAccount(sqlFind);
        btnTimKiemTaiKhoan.setText("");
    }//GEN-LAST:event_btnTimTaiKhoanActionPerformed

    private void btnRefreshAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshAccountActionPerformed
        displayAccount(sqlAccount);
    }//GEN-LAST:event_btnRefreshAccountActionPerformed

    private void btnTimKiemSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemSanPhamActionPerformed
        String sql = "SELECT * FROM Product WHERE ProductID LIKE N'%"
                + this.txtTimKiemSanPham.getText()
                + "%' or Classify like N'%"
                + this.txtTimKiemSanPham.getText()
                + "%' or ProductName like N'%"
                + this.txtTimKiemSanPham.getText()
                + "%' or ProducerName like N'%"
                + this.txtTimKiemSanPham.getText()
                + "%' or WarrantyPeriod like N'%"
                + this.txtTimKiemSanPham.getText()
                + "%' or SingleTime like N'%"
                + this.txtTimKiemSanPham.getText()
                + "%' or QuantityRemaining like N'%"
                + this.txtTimKiemSanPham.getText()
                + "%' or Unit like N'%"
                + this.txtTimKiemSanPham.getText()
                + "%' or Price like N'%"
                + this.txtTimKiemSanPham.getText() + "%'";
        displayProduct(sql);
        txtTimKiemSanPham.setText("");
    }//GEN-LAST:event_btnTimKiemSanPhamActionPerformed

    private void btnTimKiemNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemNhanVienActionPerformed
        String sql = "SELECT * FROM Employees WHERE Position LIKE N'%"
                + this.txtTimKiemNhanVien.getText()
                + "%' or EmployeeID like N'%"
                + this.txtTimKiemNhanVien.getText()
                + "%' or EmployeeName like N'%"
                + this.txtTimKiemNhanVien.getText()
                + "%' or YearofBirth like N'%"
                + this.txtTimKiemNhanVien.getText()
                + "%' or Sex like N'%"
                + this.txtTimKiemNhanVien.getText()
                + "%' or Address like N'%"
                + this.txtTimKiemNhanVien.getText()
                + "%' or Phone like N'%"
                + this.txtTimKiemNhanVien.getText()
                + "%' or Email like N'%"
                + this.txtTimKiemNhanVien.getText()
                + "%' or Level like N'%"
                + this.txtTimKiemNhanVien.getText()
                + "' or Payroll like N'%"
                + this.txtTimKiemNhanVien.getText() + "'";
        displayEmployees(sql);
        txtTimKiemNhanVien.setText("");
    }//GEN-LAST:event_btnTimKiemNhanVienActionPerformed

    private void btnBackHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackHomeActionPerformed
        if (this.detail.getUser().toString().toString().equals("Admin")) {
            HomeAdmin home = new HomeAdmin(detail);
            this.setVisible(false);
            home.setVisible(true);
        } else {
            HomeAdmin home = new HomeAdmin(detail);
            this.setVisible(false);
            home.setVisible(true);
        }
    }//GEN-LAST:event_btnBackHomeActionPerformed

    private void btnTimKiemNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemNSXActionPerformed
        String sql = "SELECT * FROM Producer WHERE ProducerID LIKE N'%"
                + this.txtTimKiemNSX.getText()
                + "%' or ProducerName like N'%"
                + this.txtTimKiemNSX.getText()
                + "%' or Address like N'%"
                + this.txtTimKiemNSX.getText()
                + "%' or Phone like N'%"
                + this.txtTimKiemNSX.getText()
                + "%' or Email like N'%"
                + this.txtTimKiemNSX.getText() + "%'";
        displayProducer(sql);
        txtTimKiemNSX.setText("");
    }//GEN-LAST:event_btnTimKiemNSXActionPerformed

    private void btnTimKiemChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemChucVuActionPerformed
        String sql = "SELECT * FROM Position WHERE PositionID LIKE N'%"
                + this.txtTimKiemChucVu.getText()
                + "%' or Position like N'%"
                + this.txtTimKiemChucVu.getText()
                + "%' or Payroll like N'%"
                + this.txtTimKiemChucVu.getText() + "%'";
        displayPosition(sql);
        txtTimKiemChucVu.setText("");
    }//GEN-LAST:event_btnTimKiemChucVuActionPerformed

    private void btnTimKiemLoaiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemLoaiSPActionPerformed
        String sql = "SELECT * FROM Classify WHERE ClassifyID LIKE N'%"
                + this.txtTimKiemLoaiSP.getText()
                + "%' or Classify like N'%"
                + this.txtTimKiemLoaiSP.getText() + "%'";
        displayClassify(sql);
        txtTimKiemLoaiSP.setText("");
    }//GEN-LAST:event_btnTimKiemLoaiSPActionPerformed

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

    private void btnRefreshNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshNhanVienActionPerformed
        displayEmployees(sqlEmployee);
    }//GEN-LAST:event_btnRefreshNhanVienActionPerformed

    private void btnRefreshSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshSanPhamActionPerformed
        displayProduct(sqlProduct);
    }//GEN-LAST:event_btnRefreshSanPhamActionPerformed

    private void btnTimKiemDonHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemDonHangActionPerformed
        String sql = "SELECT * FROM Orders where OrderID like N'%"
                + this.txtTimKiemDonHang.getText()
                + "%' or CustomerName like N'%"
                + this.txtTimKiemDonHang.getText()
                + "%' or Address like N'%"
                + this.txtTimKiemDonHang.getText()
                + "%' or Phone like N'%"
                + this.txtTimKiemDonHang.getText()
                + "%' or ProductName like N'%"
                + this.txtTimKiemDonHang.getText()
                + "%' or Amount like N'%"
                + this.txtTimKiemDonHang.getText()
                + "%' or Price like N'%"
                + this.txtTimKiemDonHang.getText()
                + "%' or WarrantyPeriod like N'%"
                + this.txtTimKiemDonHang.getText()
                + "%' or IntoMoney like N'%"
                + this.txtTimKiemDonHang.getText()
                + "' or Date like N'%"
                + this.txtTimKiemDonHang.getText()
                + "' or PaymentMethods like N'%"
                + this.txtTimKiemDonHang.getText() + "'";
        displayOrders(sql);
        txtTimKiemDonHang.setText("");
    }//GEN-LAST:event_btnTimKiemDonHangActionPerformed

    private void btnRefresHNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresHNSXActionPerformed
        displayProducer(sqlProducer);
    }//GEN-LAST:event_btnRefresHNSXActionPerformed

    private void btnRefreshChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshChucVuActionPerformed
        displayPosition(sqlPosition);
    }//GEN-LAST:event_btnRefreshChucVuActionPerformed

    private void btnRefreshLoaiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshLoaiSPActionPerformed
        displayClassify(sqlClassify);
    }//GEN-LAST:event_btnRefreshLoaiSPActionPerformed

    private void btnRefreshDonHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshDonHangActionPerformed
        displayOrders(sqlOrders);
    }//GEN-LAST:event_btnRefreshDonHangActionPerformed

    private void btnLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageActionPerformed
        String file = btnLanguage.isSelected() ? "Internationalization/English" : "Internationalization/Vietnamese";
        String languagecodes = btnLanguage.isSelected() ? "en" : "vi";
        String countrycodes = btnLanguage.isSelected() ? "US" : "VN";
        String togglebutton = btnLanguage.isSelected() ? "English" : "Vietnamese";
        btnLanguage.setText(togglebutton);
        Locale currentLocale = new Locale(languagecodes, countrycodes);
        ResourceBundle language = ResourceBundle.getBundle(file, currentLocale);
        jTitle.setText(language.getString("tieudetimkiem"));
        jTimKiemTaiKhoan.setText(language.getString("jtimkiemtaikhoan"));
        jTimKiemNhanVien.setText(language.getString("jtimkiemnhanvien"));
        jTimKiemSanPham.setText(language.getString("jtimkiemsanpham"));
        jTimKiemDonHang.setText(language.getString("jtimkiemdonhang"));
        jTimKiemNSX.setText(language.getString("jtimkiemnsx"));
        jTimKiemChucVu.setText(language.getString("jtimkiemchucvu"));
        jTimKiemLoaiSanPham.setText(language.getString("jtimkiemloaisp"));
        btnTimTaiKhoan.setText(language.getString("btntimkiemtaikhoan"));
        btnTimKiemNhanVien.setText(language.getString("btntimkiemnhanvien"));
        btnTimKiemSanPham.setText(language.getString("btntimkiemsanpham"));
        btnTimKiemDonHang.setText(language.getString("btntimkiemdonhang"));
        btnTimKiemNSX.setText(language.getString("btntimkiemnsx"));
        btnTimKiemChucVu.setText(language.getString("btntimkiemchucvu"));
        btnTimKiemLoaiSP.setText(language.getString("btntimkiemloaisp"));
    }//GEN-LAST:event_btnLanguageActionPerformed

    private void txtTimKiemNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemNSXActionPerformed
    }//GEN-LAST:event_txtTimKiemNSXActionPerformed

    private void txtTimKiemChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemChucVuActionPerformed
    }//GEN-LAST:event_txtTimKiemChucVuActionPerformed

    private void txtTimKiemLoaiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemLoaiSPActionPerformed
    }//GEN-LAST:event_txtTimKiemLoaiSPActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail = new Detail();
                new Search(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableAccount;
    private javax.swing.JButton btnBackHome;
    private javax.swing.JToggleButton btnLanguage;
    private javax.swing.JButton btnRefresHNSX;
    private javax.swing.JButton btnRefreshAccount;
    private javax.swing.JButton btnRefreshChucVu;
    private javax.swing.JButton btnRefreshDonHang;
    private javax.swing.JButton btnRefreshLoaiSP;
    private javax.swing.JButton btnRefreshNhanVien;
    private javax.swing.JButton btnRefreshSanPham;
    private javax.swing.JButton btnTimKiemChucVu;
    private javax.swing.JButton btnTimKiemDonHang;
    private javax.swing.JButton btnTimKiemLoaiSP;
    private javax.swing.JButton btnTimKiemNSX;
    private javax.swing.JButton btnTimKiemNhanVien;
    private javax.swing.JButton btnTimKiemSanPham;
    private javax.swing.JTextField btnTimKiemTaiKhoan;
    private javax.swing.JButton btnTimTaiKhoan;
    private javax.swing.JButton jButton8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel jTimKiemChucVu;
    private javax.swing.JLabel jTimKiemDonHang;
    private javax.swing.JLabel jTimKiemLoaiSanPham;
    private javax.swing.JLabel jTimKiemNSX;
    private javax.swing.JLabel jTimKiemNhanVien;
    private javax.swing.JLabel jTimKiemSanPham;
    private javax.swing.JLabel jTimKiemTaiKhoan;
    private javax.swing.JLabel jTitle;
    private javax.swing.JTabbedPane menuTaiKhoan;
    private javax.swing.JTable tableClassify;
    private javax.swing.JTable tableEmployees;
    private javax.swing.JTable tableOrder;
    private javax.swing.JTable tablePosition;
    private javax.swing.JTable tableProducer;
    private javax.swing.JTable tableProduct;
    private javax.swing.JTextField txtTimKiemChucVu;
    private javax.swing.JTextField txtTimKiemDonHang;
    private javax.swing.JTextField txtTimKiemLoaiSP;
    private javax.swing.JTextField txtTimKiemNSX;
    private javax.swing.JTextField txtTimKiemNhanVien;
    private javax.swing.JTextField txtTimKiemSanPham;
    // End of variables declaration//GEN-END:variables
}
