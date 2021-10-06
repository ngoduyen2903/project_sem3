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
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class ProductManagement extends javax.swing.JFrame {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet resultset = null;
    private String sqlProduct = "SELECT * FROM Product";
    private boolean Add = false, Change = false;
    private Detail detail;

    public ProductManagement(Detail d) {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        connectionSQLServer();
        detail = new Detail(d);
        disabled();
        Load(sqlProduct);
        txtTrangThai.setForeground(Color.red);
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

    private void Load(String sql) {
        tableProduct.removeAll();
        try {
            String[] data = {"Mã Sản Phẩm", "Tên Sản Phẩm", "Danh mục Sản Phẩm", "Đơn Vị", "Giá", "Số Lượng Còn"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("ProductID").trim());
                vector.add(resultset.getString("ProductName").trim());
                vector.add(resultset.getString("Product_PortfolioID").trim());
                vector.add(resultset.getString("Unit").trim());
                vector.add(resultset.getString("Price").trim());
                vector.add(resultset.getInt("QuantityRemaining"));
                tbModel.addRow(vector);
            }
            tableProduct.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadProductPortfolio() {
        txTLoaiSanPham.removeAllItems();
        String sql = "SELECT * FROM ProductPortfolio";
        try {
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                this.txTLoaiSanPham.addItem(resultset.getString("Product_Portfolio_Name").trim());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void enabled() {
        txtMaSanPham.setEnabled(true);
        txtTenSanPham.setEnabled(true);
        txtUnit.setEnabled(true);
        txtPrice.setEnabled(true);
        txtQuantityRemaining.setEnabled(true);
        txTLoaiSanPham.setEnabled(true);
        txTLoaiSanPham.setEnabled(true);
        txtTrangThai.setText("Status");
    }

    public void disabled() {
        txtMaSanPham.setEnabled(false);
        txtTenSanPham.setEnabled(false);
        txtUnit.setEnabled(false);
        txtPrice.setEnabled(false);
        txtQuantityRemaining.setEnabled(false);
        txTLoaiSanPham.setEnabled(false);

    }

    public void refresh() {
        txtMaSanPham.setText("");
        txtTenSanPham.setText("");
        txtUnit.setText("");
        txtPrice.setText("");
        txtQuantityRemaining.setText("");
        btnThem.setEnabled(true);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnLuu.setEnabled(false);
        Add = false;
        Change = false;
        disabled();
    }

    public boolean checkEmpty() {
        boolean kq = true;
        if (String.valueOf(this.txtMaSanPham.getText()).length() == 0) {
            txtTrangThai.setText("Mã sản phẩm không được trống!");
            return false;
        }
        if (String.valueOf(this.txtTenSanPham.getText()).length() == 0) {
            txtTrangThai.setText("Tên sản phẩm không được trống!");
            return false;
        }

        if (String.valueOf(this.txtUnit.getText()).length() == 0) {
            txtTrangThai.setText("Bạn chưa nhập số lượng sản phẩm hiện có!");
            return false;
        }
        if (String.valueOf(this.txtPrice.getText()).length() == 0) {
            txtTrangThai.setText("Đơn vị không được trống!");
            return false;
        }
        if (String.valueOf(this.txtQuantityRemaining.getText()).length() == 0) {
            txtTrangThai.setText("Giá không được trống!");
            return false;
        }
        return kq;
    }

    public boolean check() {
        boolean kq = true;
        String sqlCheck = "SELECT * FROM Product";
        try {
            PreparedStatement pstCheck = conn.prepareStatement(sqlCheck);
            ResultSet rsCheck = pstCheck.executeQuery();
            while (rsCheck.next()) {
                if (this.txtMaSanPham.getText().equals(rsCheck.getString("ProductID").toString().trim())) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    private void insertProduct() {
        if (checkEmpty()) {
            String sqlInsert = "INSERT INTO Product (ProductID,ProductName,Product_PortfolioID,Unit,Price,QuantityRemaining) VALUES(?,?,?,?,?,?)";
            try {
                pst = conn.prepareStatement(sqlInsert);
                pst.setString(1, (String) txtMaSanPham.getText());
                pst.setString(2, (String) txtTenSanPham.getText());
                pst.setString(3, String.valueOf(this.txTLoaiSanPham.getSelectedItem()));

                //  pst.setString(4, String.valueOf(txtNhaSanXuat.getSelectedItem()));
                //  pst.setInt(5, Integer.parseInt(txtThoiGianBaoHanh.getText()));
                // pst.setString(6, String.valueOf(this.btnThoiGianBaoHanh.getSelectedItem()));
                // pst.setInt(4, Integer.parseInt(txtUnit.getText()));
                pst.setString(4, (String) txtUnit.getText());
                pst.setString(5, String.valueOf(this.txtPrice.getText() + " " + "VND"));
                pst.setString(6, String.valueOf(this.txtQuantityRemaining.getText()));
                pst.executeUpdate();
                txtTrangThai.setText("Đã thêm sản phẩm!");
                disabled();
                refresh();
                Load(sqlProduct);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateProduct() {
        int Click = tableProduct.getSelectedRow();
        TableModel tbModel = tableProduct.getModel();
        if (checkEmpty()) {
            String sqlChange = "UPDATE Product SET ProductID=?,ProductName=?,Product_PortfolioID=?,Unit=?,Price=?,QuantityRemaining=? "
                    + "WHERE ProductID='" + tbModel.getValueAt(Click, 0).toString().trim() + "'";
            try {
                pst = conn.prepareStatement(sqlChange);
                pst.setString(1, txtMaSanPham.getText());
                pst.setString(2, txtTenSanPham.getText());
                pst.setString(3, String.valueOf(txTLoaiSanPham.getSelectedItem()));
                pst.setString(4, txtUnit.getText());
                pst.setInt(5, Integer.parseInt(txtPrice.getText()));
                pst.setInt(6, Integer.parseInt(txtQuantityRemaining.getText()));
                pst.executeUpdate();
                txtTrangThai.setText("Lưu thay đổi thành công!");
                disabled();
                refresh();
                Load(sqlProduct);
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

        jTextField1 = new javax.swing.JTextField();
        txtTrangThai = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableProduct = new javax.swing.JTable();
        btnSearchProduct = new javax.swing.JButton();
        txtSearchProduct = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jMaSanPham = new javax.swing.JLabel();
        txtMaSanPham = new javax.swing.JTextField();
        jLoaiSanPham = new javax.swing.JLabel();
        txTLoaiSanPham = new javax.swing.JComboBox<>();
        jTenSanPham = new javax.swing.JLabel();
        txtTenSanPham = new javax.swing.JTextField();
        jSoLuongCon = new javax.swing.JLabel();
        txtUnit = new javax.swing.JTextField();
        jDonViTinh = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        jGia = new javax.swing.JLabel();
        txtQuantityRemaining = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTitle = new javax.swing.JLabel();
        btnLanguage = new javax.swing.JToggleButton();
        btnBackHome = new javax.swing.JButton();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ProductManagement");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtTrangThai.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTrangThai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTrangThai.setText("Status");
        txtTrangThai.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel1.setBackground(new java.awt.Color(80, 180, 155));

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(255, 255, 255));
        btnThem.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add_1.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(255, 255, 255));
        btnSua.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Update.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setEnabled(false);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 255, 255));
        btnXoa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete_1.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setEnabled(false);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLuu.setBackground(new java.awt.Color(255, 255, 255));
        btnLuu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.setEnabled(false);
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        tableProduct.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tableProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableProduct);

        btnSearchProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnSearchProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Search_1.png"))); // NOI18N
        btnSearchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchProductActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSearchProduct))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSearchProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLuu, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLuu))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSearchProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearchProduct))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBackground(new java.awt.Color(80, 180, 155));

        jMaSanPham.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jMaSanPham.setForeground(new java.awt.Color(255, 255, 255));
        jMaSanPham.setText("Product ID:");

        jLoaiSanPham.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLoaiSanPham.setForeground(new java.awt.Color(255, 255, 255));
        jLoaiSanPham.setText("Product Portfolio:");

        jTenSanPham.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jTenSanPham.setForeground(new java.awt.Color(255, 255, 255));
        jTenSanPham.setText("Product Name:");

        jSoLuongCon.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jSoLuongCon.setForeground(new java.awt.Color(255, 255, 255));
        jSoLuongCon.setText("Quantity Remaining:");

        txtUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUnitKeyReleased(evt);
            }
        });

        jDonViTinh.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jDonViTinh.setForeground(new java.awt.Color(255, 255, 255));
        jDonViTinh.setText("Unit:");

        jGia.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jGia.setForeground(new java.awt.Color(255, 255, 255));
        jGia.setText("Price:");

        txtQuantityRemaining.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQuantityRemainingKeyReleased(evt);
            }
        });

        jLabel1.setText(".");

        jLabel2.setText(".");

        jLabel3.setText(".");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLoaiSanPham)
                        .addComponent(jMaSanPham, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jTenSanPham))
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTenSanPham, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtMaSanPham, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txTLoaiSanPham, 0, 237, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDonViTinh, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jGia, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSoLuongCon, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtUnit)
                                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtQuantityRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(284, 284, 284))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel1)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel2)
                                .addGap(31, 31, 31))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jDonViTinh)
                                        .addComponent(txtUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jGia))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtQuantityRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jSoLuongCon))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jLabel3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jMaSanPham)
                            .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLoaiSanPham)
                                    .addComponent(txTLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTenSanPham)
                                    .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel2.setBackground(new java.awt.Color(80, 180, 155));

        jTitle.setFont(new java.awt.Font("Times New Roman", 1, 30)); // NOI18N
        jTitle.setForeground(new java.awt.Color(255, 255, 255));
        jTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTitle.setText("PRODUCT MANAGEMENT");

        btnLanguage.setBackground(new java.awt.Color(255, 255, 255));
        btnLanguage.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLanguage.setText("English");
        btnLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLanguageActionPerformed(evt);
            }
        });

        btnBackHome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBackHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Home.png"))); // NOI18N
        btnBackHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackHomeMouseClicked(evt);
            }
        });
        btnBackHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHomeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                .addComponent(jTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125)
                .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTrangThai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 936, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackHomeMouseClicked
        if (this.detail.getUser().toString().toString().equals("Admin")) {
            HomeAdmin home = new HomeAdmin(detail);
            this.setVisible(false);
            home.setVisible(true);
        } else {
            HomeAdmin home = new HomeAdmin(detail);
            this.setVisible(false);
            home.setVisible(true);
        }
    }//GEN-LAST:event_btnBackHomeMouseClicked

    private void txtQuantityRemainingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantityRemainingKeyReleased
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        txtPrice.setText(cutChar(txtPrice.getText()));
        if (txtPrice.getText().equals("")) {
            return;
        } else {
            txtPrice.setText(formatter.format(convertedToNumbers(txtPrice.getText())));
        }
    }//GEN-LAST:event_txtQuantityRemainingKeyReleased

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

    private void txtUnitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitKeyReleased
        txtPrice.setText(cutChar(txtPrice.getText()));
    }//GEN-LAST:event_txtUnitKeyReleased

    private void tableProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProductMouseClicked
        int Click = tableProduct.getSelectedRow();
        TableModel tbModel = tableProduct.getModel();

        txTLoaiSanPham.removeAllItems();
        txtMaSanPham.setText(tbModel.getValueAt(Click, 0).toString());
        txTLoaiSanPham.addItem(tbModel.getValueAt(Click, 2).toString());
        txtTenSanPham.setText(tbModel.getValueAt(Click, 1).toString());
        txtUnit.setText(tbModel.getValueAt(Click, 3).toString());
        txtQuantityRemaining.setText(tbModel.getValueAt(Click, 5).toString());
        String[] s1 = tbModel.getValueAt(Click, 4).toString().split("\\s");
        txtPrice.setText(s1[0]);
        btnSua.setEnabled(true);
        btnXoa.setEnabled(true);
    }//GEN-LAST:event_tableProductMouseClicked

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        if (Add == true) {
            if (check()) {
                insertProduct();
            } else {
                txtTrangThai.setText("Mã sản phẩm bạn nhập đã tồn tại!");
            }
        } else if (Change == true) {
            updateProduct();
        }
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa sản phẩm?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM Product WHERE ProductID=? AND ProductName=? ";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, (String) txtMaSanPham.getText());
                pst.setString(2, txtTenSanPham.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Đã xóa sản phẩm!");
                disabled();
                refresh();
                Load(sqlProduct);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        Add = false;
        Change = true;
        enabled();
        loadProductPortfolio();
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnLuu.setEnabled(true);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        refresh();
        Add = true;
        enabled();
        loadProductPortfolio();
        btnThem.setEnabled(false);
        btnLuu.setEnabled(true);
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refresh();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageActionPerformed
        String file = btnLanguage.isSelected() ? "Internationalization/English" : "Internationalization/Vietnamese";
        String languagecodes = btnLanguage.isSelected() ? "en" : "vi";
        String countrycodes = btnLanguage.isSelected() ? "US" : "VN";
        String togglebutton = btnLanguage.isSelected() ? "English" : "Vietnamese";
        btnLanguage.setText(togglebutton);
        Locale currentLocale = new Locale(languagecodes, countrycodes);
        ResourceBundle language = ResourceBundle.getBundle(file, currentLocale);
        jTitle.setText(language.getString("tieudequanlisanpham"));
        jMaSanPham.setText(language.getString("masanpham"));
        jLoaiSanPham.setText(language.getString("loaisanpham"));
        jTenSanPham.setText(language.getString("tensanpham"));
        //     jNhaSanXuat.setText(language.getString("nhasanxuat"));
        //    jThoiGianBaoHanh.setText(language.getString("thoigianbaohanh"));
        jSoLuongCon.setText(language.getString("soluongcon"));
        jDonViTinh.setText(language.getString("donvitinh"));
        jGia.setText(language.getString("gia"));
        btnThem.setText(language.getString("btnthem"));
        btnSua.setText(language.getString("btnsua"));
        btnXoa.setText(language.getString("btnxoa"));
        btnLuu.setText(language.getString("btnluu"));
        txtTrangThai.setText(language.getString("trangthai"));
    }//GEN-LAST:event_btnLanguageActionPerformed

    private void btnBackHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackHomeActionPerformed
        HomeAdmin home = new HomeAdmin(detail);
        this.setVisible(true);
        home.dispose();
    }//GEN-LAST:event_btnBackHomeActionPerformed

    private void btnSearchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchProductActionPerformed
        // TODO add your handling code here:

        String sql = "SELECT * FROM Product WHERE ProductID LIKE N'%"
                + this.txtSearchProduct.getText()
                + "%' or ProductName like N'%"
                + this.txtSearchProduct.getText() + "%'";

        Load(sql);
        txtSearchProduct.setText("");
    }//GEN-LAST:event_btnSearchProductActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProductManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProductManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProductManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProductManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail = new Detail();
                new ProductManagement(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackHome;
    private javax.swing.JToggleButton btnLanguage;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearchProduct;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jDonViTinh;
    private javax.swing.JLabel jGia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLoaiSanPham;
    private javax.swing.JLabel jMaSanPham;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jSoLuongCon;
    private javax.swing.JLabel jTenSanPham;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel jTitle;
    private javax.swing.JTable tableProduct;
    private javax.swing.JComboBox<String> txTLoaiSanPham;
    private javax.swing.JTextField txtMaSanPham;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtQuantityRemaining;
    private javax.swing.JTextField txtSearchProduct;
    private javax.swing.JTextField txtTenSanPham;
    private javax.swing.JLabel txtTrangThai;
    private javax.swing.JTextField txtUnit;
    // End of variables declaration//GEN-END:variables
}
