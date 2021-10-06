package UserInterFace;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ProductPortfolio extends javax.swing.JFrame {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet resultset = null;
    private Detail detail;
    private boolean Add = false, Change = false;

    String sqlProductPortfolio = "SELECT * FROM ProductPortfolio";

    public ProductPortfolio(Detail d) {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        detail = new Detail(d);
        txtTrangThai.setForeground(Color.red);
        connectionSQLServer();
        //   displayMaterial(sqlPosition);
        //  displaySupplier(sqlProducer);
        displayMaterialType(sqlProductPortfolio);
        //  disabledPosition();
        //  disabledClassify();
        // disabledProducer();
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

    private void displayMaterialType(String sql) {
        tableProductPortfolio.removeAll();
        try {
            String[] data = {"Product Portfolio ID", "Product Portfolio Name"};
            DefaultTableModel tbModle = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("Product_PortfolioID").trim());
                vector.add(resultset.getString("Product_Portfolio_Name").trim());
                tbModle.addRow(vector);
            }
            tableProductPortfolio.setModel(tbModle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void backHome() {
        HomeAdmin home = new HomeAdmin(detail);
        this.setVisible(false);
        home.setVisible(true);
    }

    private void enabledClassify() {
        txtProduct_PortfolioID.setEnabled(true);
        txtProduct_Portfolio_Name.setEnabled(true);
        txtTrangThai.setText("Trạng Thái!");
    }

    private void disabledClassify() {
        txtProduct_PortfolioID.setEnabled(false);
        txtProduct_Portfolio_Name.setEnabled(false);
    }

    private void refresh() {
        Change = false;
        Add = false;
        txtMaChucVu.setText("");
        txtChucVu.setText("");
        txtLuongCoBan.setText("");
        txtMaNSX.setText("");
        txtNhaSanXuat.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtProduct_PortfolioID.setText("");
        txtProduct_Portfolio_Name.setText("");
        btnThemChucVu.setEnabled(true);
        btnSuaChucVu.setEnabled(false);
        btnXoaChucVu.setEnabled(false);
        btnLuuChucVu.setEnabled(false);
        btnThemNSX.setEnabled(true);
        btnSuaNSX.setEnabled(false);
        btnXoaNSX.setEnabled(false);
        btnLuuNSX.setEnabled(false);
        btnAddProductPortfolio.setEnabled(true);
        btnUpdateProductPortfolio.setEnabled(false);
        btnXoaProductPortfolio.setEnabled(false);
        btnLuuProductPortfolio.setEnabled(false);
        disabledClassify();
        // disabledPosition();
        // disabledProducer();
    }

    private boolean checkClassify() {
        boolean kq = true;
        String sqlCheck = "SELECT * FROM ProductPortfolio";
        try {
            pst = conn.prepareStatement(sqlCheck);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                if (this.txtProduct_PortfolioID.getText().equals(resultset.getString("Product_PortfolioID").toString().trim())) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    private boolean checkEmptyClassify() {
        boolean kq = true;
        if (String.valueOf(this.txtProduct_PortfolioID.getText()).length() == 0) {
            txtTrangThai.setText("Mã loại sản phẩm không được trống!");
            return false;
        }
        if (String.valueOf(this.txtProduct_Portfolio_Name.getText()).length() == 0) {
            txtTrangThai.setText("Loại sản phẩm không được trống!");
            return false;
        }
        return kq;
    }

    private void insertClassify() {
        if (checkEmptyClassify()) {
            String sqlInsert = "INSERT INTO ProductPortfolio (Product_PortfolioID,Product_Portfolio_Name) VALUES(?,?)";
            try {
                pst = conn.prepareStatement(sqlInsert);
                pst.setString(1, txtProduct_PortfolioID.getText());
                pst.setString(2, txtProduct_Portfolio_Name.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Đã thêm loại sản phẩm!");
                disabledClassify();
                refresh();
                displayMaterialType(sqlProductPortfolio);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateMaterialType() {
        int Click = tableProductPortfolio.getSelectedRow();
        TableModel tbModel = tableProductPortfolio.getModel();
        if (checkEmptyClassify()) {
            String sqlChange = "UPDATE ProductPortfolio SET Product_PortfolioID=?, Product_Portfolio_Name=? WHERE Product_PortfolioID='" + tbModel.getValueAt(Click, 0).toString().trim() + "'";;
            try {
                pst = conn.prepareStatement(sqlChange);
                pst.setString(1, txtProduct_PortfolioID.getText());
                pst.setString(2, txtProduct_Portfolio_Name.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Lưu thay đổi thành công!");
                disabledClassify();
                refresh();
                displayMaterialType(sqlProductPortfolio);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private double convertedToNumbers(String s) {
        String number = "";
        String[] array = s.replace(",", " ").split("\\s");
        for (String i : array) {
            number = number.concat(i);
        }
        return Double.parseDouble(number);
    }

    private String cutChar(String arry) {
        return arry.replaceAll("\\D+", "");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tableProducer = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        btnRefreshNSX = new javax.swing.JButton();
        btnThemNSX = new javax.swing.JButton();
        btnSuaNSX = new javax.swing.JButton();
        btnXoaNSX = new javax.swing.JButton();
        btnLuuNSX = new javax.swing.JButton();
        jMaNSX = new javax.swing.JLabel();
        txtMaNSX = new javax.swing.JTextField();
        jNhaSX = new javax.swing.JLabel();
        txtNhaSanXuat = new javax.swing.JTextField();
        jDiaChi = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        jSDT = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePosition = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtLuongCoBan = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLuongCoBan = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        txtChucVu = new javax.swing.JTextField();
        jChucVu = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        txtMaChucVu = new javax.swing.JTextField();
        jMaChucVu = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        btnRefreshChucVu = new javax.swing.JButton();
        btnThemChucVu = new javax.swing.JButton();
        btnSuaChucVu = new javax.swing.JButton();
        btnXoaChucVu = new javax.swing.JButton();
        btnLuuChucVu = new javax.swing.JButton();
        jMaterial_type_id = new javax.swing.JLabel();
        txtMaterial_type_id = new javax.swing.JTextField();
        txtTrangThai = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnLanguage = new javax.swing.JToggleButton();
        btnTrangChu = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableProductPortfolio = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        btnRefreshProductPortfolio = new javax.swing.JButton();
        btnAddProductPortfolio = new javax.swing.JButton();
        btnUpdateProductPortfolio = new javax.swing.JButton();
        btnXoaProductPortfolio = new javax.swing.JButton();
        btnLuuProductPortfolio = new javax.swing.JButton();
        jProduct_Portfolio_ID = new javax.swing.JLabel();
        txtProduct_PortfolioID = new javax.swing.JTextField();
        jProduct_Portfolio_Name = new javax.swing.JLabel();
        txtProduct_Portfolio_Name = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        tableProducer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableProducer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProducerMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableProducer);

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.setPreferredSize(new java.awt.Dimension(457, 457));

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));

        btnRefreshNSX.setBackground(new java.awt.Color(204, 255, 153));
        btnRefreshNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshNSX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshNSXMouseClicked(evt);
            }
        });

        btnThemNSX.setBackground(new java.awt.Color(204, 255, 153));
        btnThemNSX.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnThemNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add_1.png"))); // NOI18N
        btnThemNSX.setText("Thêm");
        btnThemNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNSXActionPerformed(evt);
            }
        });

        btnSuaNSX.setBackground(new java.awt.Color(204, 255, 153));
        btnSuaNSX.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSuaNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Update.png"))); // NOI18N
        btnSuaNSX.setText("Sửa");
        btnSuaNSX.setEnabled(false);
        btnSuaNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaNSXActionPerformed(evt);
            }
        });

        btnXoaNSX.setBackground(new java.awt.Color(255, 102, 102));
        btnXoaNSX.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnXoaNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete_1.png"))); // NOI18N
        btnXoaNSX.setText("Xóa");
        btnXoaNSX.setEnabled(false);
        btnXoaNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNSXActionPerformed(evt);
            }
        });

        btnLuuNSX.setBackground(new java.awt.Color(204, 255, 153));
        btnLuuNSX.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLuuNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnLuuNSX.setText("Lưu");
        btnLuuNSX.setEnabled(false);
        btnLuuNSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuNSXActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRefreshNSX)
                .addGap(18, 18, 18)
                .addComponent(btnThemNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSuaNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnXoaNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLuuNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(441, 441, 441))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnThemNSX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(btnSuaNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXoaNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLuuNSX, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnRefreshNSX, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMaNSX.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMaNSX.setText("Mã NSX:");

        jNhaSX.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jNhaSX.setText("Nhà Sản Xuất:");

        jDiaChi.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jDiaChi.setText("Địa Chỉ:");

        jSDT.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jSDT.setText("Số Điện Thoại:");

        txtSDT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSDTKeyReleased(evt);
            }
        });

        jEmail.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jEmail.setText("Email:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jNhaSX)
                            .addComponent(jMaNSX)
                            .addComponent(jDiaChi))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaNSX)
                            .addComponent(txtNhaSanXuat)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSDT)
                            .addComponent(jEmail))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtSDT)
                            .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaNSX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSDT)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jMaNSX))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jNhaSX)
                    .addComponent(txtNhaSanXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDiaChi)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(330, Short.MAX_VALUE))
        );

        tablePosition.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablePosition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePositionMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablePosition);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        txtLuongCoBan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLuongCoBanKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 67, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLuongCoBan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLuongCoBan.setText("Unit:");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 67, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jChucVu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jChucVu.setText("Material Name:");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 67, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jMaChucVu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMaChucVu.setText("Material ID:");

        jPanel17.setBackground(new java.awt.Color(204, 255, 204));

        btnRefreshChucVu.setBackground(new java.awt.Color(204, 255, 153));
        btnRefreshChucVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshChucVu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshChucVuMouseClicked(evt);
            }
        });
        btnRefreshChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshChucVuActionPerformed(evt);
            }
        });

        btnThemChucVu.setBackground(new java.awt.Color(204, 255, 153));
        btnThemChucVu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnThemChucVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add_1.png"))); // NOI18N
        btnThemChucVu.setText("Thêm");
        btnThemChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemChucVuActionPerformed(evt);
            }
        });

        btnSuaChucVu.setBackground(new java.awt.Color(204, 255, 153));
        btnSuaChucVu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSuaChucVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Update.png"))); // NOI18N
        btnSuaChucVu.setText("Sửa");
        btnSuaChucVu.setEnabled(false);
        btnSuaChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaChucVuActionPerformed(evt);
            }
        });

        btnXoaChucVu.setBackground(new java.awt.Color(255, 102, 102));
        btnXoaChucVu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnXoaChucVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete_1.png"))); // NOI18N
        btnXoaChucVu.setText("Xóa");
        btnXoaChucVu.setEnabled(false);
        btnXoaChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaChucVuActionPerformed(evt);
            }
        });

        btnLuuChucVu.setBackground(new java.awt.Color(204, 255, 153));
        btnLuuChucVu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLuuChucVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnLuuChucVu.setText("Lưu");
        btnLuuChucVu.setEnabled(false);
        btnLuuChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuChucVuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRefreshChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnThemChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSuaChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnXoaChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLuuChucVu, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRefreshChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(btnSuaChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXoaChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLuuChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThemChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jMaterial_type_id.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMaterial_type_id.setText("Material Type ID:");

        txtMaterial_type_id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaterial_type_idKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(230, 230, 230)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(270, 270, 270)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jMaChucVu)
                            .addComponent(jChucVu)
                            .addComponent(jLuongCoBan)
                            .addComponent(jMaterial_type_id))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtLuongCoBan)
                            .addComponent(txtChucVu, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(txtMaChucVu)
                            .addComponent(txtMaterial_type_id))))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMaChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jMaChucVu)))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jChucVu))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLuongCoBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLuongCoBan))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jMaterial_type_id)
                    .addComponent(txtMaterial_type_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(295, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("InformationManagement");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtTrangThai.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTrangThai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTrangThai.setText("Status");

        jPanel4.setBackground(new java.awt.Color(80, 180, 155));

        btnLanguage.setBackground(new java.awt.Color(255, 255, 255));
        btnLanguage.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLanguage.setText("English");
        btnLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLanguageActionPerformed(evt);
            }
        });

        btnTrangChu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnTrangChu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Home.png"))); // NOI18N
        btnTrangChu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTrangChuMouseClicked(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(80, 180, 155));

        tableProductPortfolio.setBackground(new java.awt.Color(80, 180, 155));
        tableProductPortfolio.setForeground(new java.awt.Color(255, 255, 255));
        tableProductPortfolio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableProductPortfolio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProductPortfolioMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableProductPortfolio);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 153, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel14.setBackground(new java.awt.Color(80, 180, 155));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 422, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        jPanel26.setBackground(new java.awt.Color(80, 180, 155));

        btnRefreshProductPortfolio.setBackground(new java.awt.Color(255, 255, 255));
        btnRefreshProductPortfolio.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshProductPortfolio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshProductPortfolio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshProductPortfolioMouseClicked(evt);
            }
        });

        btnAddProductPortfolio.setBackground(new java.awt.Color(255, 255, 255));
        btnAddProductPortfolio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAddProductPortfolio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add_1.png"))); // NOI18N
        btnAddProductPortfolio.setText("Thêm");
        btnAddProductPortfolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductPortfolioActionPerformed(evt);
            }
        });

        btnUpdateProductPortfolio.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdateProductPortfolio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUpdateProductPortfolio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Update.png"))); // NOI18N
        btnUpdateProductPortfolio.setText("Sửa");
        btnUpdateProductPortfolio.setEnabled(false);
        btnUpdateProductPortfolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateProductPortfolioActionPerformed(evt);
            }
        });

        btnXoaProductPortfolio.setBackground(new java.awt.Color(255, 255, 255));
        btnXoaProductPortfolio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnXoaProductPortfolio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete_1.png"))); // NOI18N
        btnXoaProductPortfolio.setText("Xóa");
        btnXoaProductPortfolio.setEnabled(false);
        btnXoaProductPortfolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaProductPortfolioActionPerformed(evt);
            }
        });

        btnLuuProductPortfolio.setBackground(new java.awt.Color(255, 255, 255));
        btnLuuProductPortfolio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLuuProductPortfolio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnLuuProductPortfolio.setText("Lưu");
        btnLuuProductPortfolio.setEnabled(false);
        btnLuuProductPortfolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuProductPortfolioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnRefreshProductPortfolio, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAddProductPortfolio, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpdateProductPortfolio, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnXoaProductPortfolio, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnLuuProductPortfolio, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLuuProductPortfolio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(btnRefreshProductPortfolio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddProductPortfolio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdateProductPortfolio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoaProductPortfolio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jProduct_Portfolio_ID.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jProduct_Portfolio_ID.setForeground(new java.awt.Color(255, 255, 255));
        jProduct_Portfolio_ID.setText("Product Portfolio ID:");

        jProduct_Portfolio_Name.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jProduct_Portfolio_Name.setForeground(new java.awt.Color(255, 255, 255));
        jProduct_Portfolio_Name.setText("Product Portfolio Name:");

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Search_1.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jProduct_Portfolio_ID)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtProduct_PortfolioID, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jProduct_Portfolio_Name)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtProduct_Portfolio_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(486, 486, 486)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jProduct_Portfolio_ID)
                            .addComponent(txtProduct_PortfolioID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProduct_Portfolio_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jProduct_Portfolio_Name))))
                .addGap(18, 18, 18)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Product Portfolio", jPanel3);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PRODUCT PORTFOLIO");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btnTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTrangThai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTrangThai)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableProductPortfolioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProductPortfolioMouseClicked
        int Click = tableProductPortfolio.getSelectedRow();
        TableModel tbModel = tableProductPortfolio.getModel();
        txtProduct_PortfolioID.setText(tbModel.getValueAt(Click, 0).toString());
        txtProduct_Portfolio_Name.setText(tbModel.getValueAt(Click, 1).toString());
        btnUpdateProductPortfolio.setEnabled(true);
        btnXoaProductPortfolio.setEnabled(true);
    }//GEN-LAST:event_tableProductPortfolioMouseClicked

    private void btnTrangChuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTrangChuMouseClicked
        backHome();
    }//GEN-LAST:event_btnTrangChuMouseClicked

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

    private void btnRefreshProductPortfolioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshProductPortfolioMouseClicked
        refresh();
    }//GEN-LAST:event_btnRefreshProductPortfolioMouseClicked

    private void btnAddProductPortfolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductPortfolioActionPerformed
        refresh();
        Add = true;
        btnAddProductPortfolio.setEnabled(false);
        btnLuuProductPortfolio.setEnabled(true);
        enabledClassify();
    }//GEN-LAST:event_btnAddProductPortfolioActionPerformed

    private void btnUpdateProductPortfolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateProductPortfolioActionPerformed
        Add = false;
        Change = true;
        btnAddProductPortfolio.setEnabled(false);
        btnUpdateProductPortfolio.setEnabled(false);
        btnXoaProductPortfolio.setEnabled(false);
        btnLuuProductPortfolio.setEnabled(true);
        enabledClassify();
    }//GEN-LAST:event_btnUpdateProductPortfolioActionPerformed

    private void btnXoaProductPortfolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaProductPortfolioActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn muốn xóa loại sản phẩm?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM ProductPortfolio WHERE Product_PortfolioID=? AND Product_Portfolio_Name=?";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, txtProduct_PortfolioID.getText());
                pst.setString(2, txtProduct_Portfolio_Name.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Xóa thành công!");
                disabledClassify();
                refresh();
                displayMaterialType(sqlProductPortfolio);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnXoaProductPortfolioActionPerformed

    private void btnLuuProductPortfolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuProductPortfolioActionPerformed
        if (Add == true) {
            if (checkClassify()) {
                insertClassify();
            } else {
                txtTrangThai.setText("Mã loại sản phẩm đã tồn tại!");
            }
        } else {
            if (Change == true) {
                updateMaterialType();
            }
        }
    }//GEN-LAST:event_btnLuuProductPortfolioActionPerformed

    private void txtSDTKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSDTKeyReleased
        txtSDT.setText(cutChar(txtSDT.getText()));
    }//GEN-LAST:event_txtSDTKeyReleased

    private void btnLuuNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuNSXActionPerformed
        /* if (Add == true) {
            if (checkProducer()) {
                insertProducer();
            } else {
                txtTrangThai.setText("Mã nhà sản xuất đã tồn tại!");
            }
        } else {
            if (Change == true) {
                updateProducer();
            }
        }*/
    }//GEN-LAST:event_btnLuuNSXActionPerformed

    private void btnXoaNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNSXActionPerformed
        /* int Click = JOptionPane.showConfirmDialog(null, "Bạn muốn xóa nhà sản xuất?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM MaterialSupplier WHERE Material_SupplierID=? AND  Material_Supplier_Name=? AND Address=? AND Phone=? AND Email=?";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, txtMaNSX.getText());
                pst.setString(2, txtNhaSanXuat.getText());
                pst.setString(3, txtDiaChi.getText());
                pst.setString(4, txtSDT.getText());
                pst.setString(5, txtEmail.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Xóa thành công!");
                disabledProducer();
                refresh();
                displaySupplier(sqlProducer);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }*/
    }//GEN-LAST:event_btnXoaNSXActionPerformed

    private void btnSuaNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaNSXActionPerformed
        Add = false;
        Change = true;
        btnThemNSX.setEnabled(false);
        btnSuaNSX.setEnabled(false);
        btnXoaNSX.setEnabled(false);
        btnLuuNSX.setEnabled(true);
        //  enabledProducer();
    }//GEN-LAST:event_btnSuaNSXActionPerformed

    private void btnThemNSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNSXActionPerformed
        refresh();
        Add = true;
        btnThemNSX.setEnabled(false);
        btnLuuNSX.setEnabled(true);
        //  enabledProducer();
    }//GEN-LAST:event_btnThemNSXActionPerformed

    private void btnRefreshNSXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshNSXMouseClicked
        refresh();
        //  displaySupplier(sqlProducer);
    }//GEN-LAST:event_btnRefreshNSXMouseClicked

    private void tableProducerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProducerMouseClicked
        int Click = tableProducer.getSelectedRow();
        TableModel tbModel = tableProducer.getModel();
        txtMaNSX.setText(tbModel.getValueAt(Click, 0).toString());
        txtNhaSanXuat.setText(tbModel.getValueAt(Click, 1).toString());
        txtDiaChi.setText(tbModel.getValueAt(Click, 2).toString());
        txtSDT.setText(tbModel.getValueAt(Click, 3).toString());
        txtEmail.setText(tbModel.getValueAt(Click, 4).toString());
        btnSuaNSX.setEnabled(true);
        btnXoaNSX.setEnabled(true);
    }//GEN-LAST:event_tableProducerMouseClicked

    private void btnLuuChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuChucVuActionPerformed
        /* if (Add == true) {
            if (checkMaterial()) {
                insertPosition();
            } else {
                txtTrangThai.setText("Mã chức vụ đã tồn tại!");
            }
        } else {
            if (Change == true) {
                updateMaterial();
            }
        }*/
    }//GEN-LAST:event_btnLuuChucVuActionPerformed

    private void btnXoaChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaChucVuActionPerformed
        /*  int Click = JOptionPane.showConfirmDialog(null, "Bạn muốn xóa chức vụ?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM Position WHERE PositionID=? AND Position=? AND Payroll=?";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, txtMaChucVu.getText());
                pst.setString(2, txtChucVu.getText());
                pst.setString(3, txtLuongCoBan.getText() + " " + "VND");
                pst.executeUpdate();
                txtTrangThai.setText("Đã xóa chức vụ!");
                disabledPosition();
                refresh();
                displayMaterial(sqlPosition);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }  */
    }//GEN-LAST:event_btnXoaChucVuActionPerformed

    private void btnSuaChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaChucVuActionPerformed
        Add = false;
        Change = true;
        btnThemChucVu.setEnabled(false);
        btnSuaChucVu.setEnabled(false);
        btnXoaChucVu.setEnabled(false);
        btnLuuChucVu.setEnabled(true);
        // enabledPosition();
    }//GEN-LAST:event_btnSuaChucVuActionPerformed

    private void btnThemChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemChucVuActionPerformed
        refresh();
        Add = true;
        btnThemChucVu.setEnabled(false);
        btnLuuChucVu.setEnabled(true);
        //  enabledPosition();
    }//GEN-LAST:event_btnThemChucVuActionPerformed

    private void btnRefreshChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshChucVuActionPerformed
    }//GEN-LAST:event_btnRefreshChucVuActionPerformed

    private void btnRefreshChucVuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshChucVuMouseClicked
        refresh();
    }//GEN-LAST:event_btnRefreshChucVuMouseClicked

    private void txtLuongCoBanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLuongCoBanKeyReleased
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        txtLuongCoBan.setText(cutChar(txtLuongCoBan.getText()));
        if (txtLuongCoBan.getText().equals("")) {
            return;
        } else {
            txtLuongCoBan.setText(formatter.format(convertedToNumbers(txtLuongCoBan.getText())));
        }
    }//GEN-LAST:event_txtLuongCoBanKeyReleased

    private void tablePositionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePositionMouseClicked
        int Click = tablePosition.getSelectedRow();
        TableModel tbModel = tablePosition.getModel();
        txtMaChucVu.setText(tbModel.getValueAt(Click, 0).toString());
        txtChucVu.setText(tbModel.getValueAt(Click, 1).toString());
        String[] s = tbModel.getValueAt(Click, 2).toString().split("\\s");
        txtLuongCoBan.setText(s[0]);
        txtMaterial_type_id.setText(tbModel.getValueAt(Click, 3).toString());
        btnSuaChucVu.setEnabled(true);
        btnXoaChucVu.setEnabled(true);
    }//GEN-LAST:event_tablePositionMouseClicked

    private void btnLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageActionPerformed
        String file = btnLanguage.isSelected() ? "Internationalization/English" : "Internationalization/Vietnamese";
        String languagecodes = btnLanguage.isSelected() ? "en" : "vi";
        String countrycodes = btnLanguage.isSelected() ? "US" : "VN";
        String togglebutton = btnLanguage.isSelected() ? "English" : "Vietnamese";
        btnLanguage.setText(togglebutton);
        Locale currentLocale = new Locale(languagecodes, countrycodes);
        ResourceBundle language = ResourceBundle.getBundle(file, currentLocale);
        // jTitle.setText(language.getString("tieudeqlthongtin"));
        jMaChucVu.setText(language.getString("machucvu"));
        jChucVu.setText(language.getString("chucvu"));
        jLuongCoBan.setText(language.getString("luongcoban"));
        btnThemChucVu.setText(language.getString("btnthemchucvu"));
        btnSuaChucVu.setText(language.getString("btnsuachucvu"));
        btnXoaChucVu.setText(language.getString("btnxoachucvu"));
        btnLuuChucVu.setText(language.getString("btnluuchucvu"));
        jMaNSX.setText(language.getString("mansx"));
        jNhaSX.setText(language.getString("nhasx"));
        jDiaChi.setText(language.getString("diachi"));
        jSDT.setText(language.getString("sdt"));
        jEmail.setText(language.getString("email"));
        btnThemNSX.setText(language.getString("btnthemnsx"));
        btnSuaNSX.setText(language.getString("btnsuansx"));
        btnXoaNSX.setText(language.getString("btnxoansx"));
        btnLuuNSX.setText(language.getString("btnluunsx"));
        jProduct_Portfolio_ID.setText(language.getString("maloai"));
        jProduct_Portfolio_Name.setText(language.getString("loaisp"));
        btnAddProductPortfolio.setText(language.getString("btnthemloaisp"));
        btnUpdateProductPortfolio.setText(language.getString("btnsualoaisp"));
        btnXoaProductPortfolio.setText(language.getString("btnxoaloaisp"));
        btnLuuProductPortfolio.setText(language.getString("btnluuloaisp"));
        txtTrangThai.setText(language.getString("trangthai"));


    }//GEN-LAST:event_btnLanguageActionPerformed

    private void txtMaterial_type_idKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaterial_type_idKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaterial_type_idKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProductPortfolio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProductPortfolio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProductPortfolio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProductPortfolio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail = new Detail();
                new ProductPortfolio(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddProductPortfolio;
    private javax.swing.JToggleButton btnLanguage;
    private javax.swing.JButton btnLuuChucVu;
    private javax.swing.JButton btnLuuNSX;
    private javax.swing.JButton btnLuuProductPortfolio;
    private javax.swing.JButton btnRefreshChucVu;
    private javax.swing.JButton btnRefreshNSX;
    private javax.swing.JButton btnRefreshProductPortfolio;
    private javax.swing.JButton btnSuaChucVu;
    private javax.swing.JButton btnSuaNSX;
    private javax.swing.JButton btnThemChucVu;
    private javax.swing.JButton btnThemNSX;
    private javax.swing.JButton btnTrangChu;
    private javax.swing.JButton btnUpdateProductPortfolio;
    private javax.swing.JButton btnXoaChucVu;
    private javax.swing.JButton btnXoaNSX;
    private javax.swing.JButton btnXoaProductPortfolio;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jChucVu;
    private javax.swing.JLabel jDiaChi;
    private javax.swing.JLabel jEmail;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLuongCoBan;
    private javax.swing.JLabel jMaChucVu;
    private javax.swing.JLabel jMaNSX;
    private javax.swing.JLabel jMaterial_type_id;
    private javax.swing.JLabel jNhaSX;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jProduct_Portfolio_ID;
    private javax.swing.JLabel jProduct_Portfolio_Name;
    private javax.swing.JLabel jSDT;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tablePosition;
    private javax.swing.JTable tableProducer;
    private javax.swing.JTable tableProductPortfolio;
    private javax.swing.JTextField txtChucVu;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtLuongCoBan;
    private javax.swing.JTextField txtMaChucVu;
    private javax.swing.JTextField txtMaNSX;
    private javax.swing.JTextField txtMaterial_type_id;
    private javax.swing.JTextField txtNhaSanXuat;
    private javax.swing.JTextField txtProduct_PortfolioID;
    private javax.swing.JTextField txtProduct_Portfolio_Name;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JLabel txtTrangThai;
    // End of variables declaration//GEN-END:variables
}
