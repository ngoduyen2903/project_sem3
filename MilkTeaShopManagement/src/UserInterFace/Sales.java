package UserInterFace;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;

class Sales extends javax.swing.JFrame implements Runnable {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet resultset = null;
    private boolean Add = false, Change = false, Pay = false;
    private String sqlBill = "SELECT * FROM Bill";
    private Thread thread;
    private Detail detail;

    public Sales(Detail d) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        detail = new Detail(d);
        txtTrangThai.setForeground(Color.red);
        setData();
        connectionSQLServer();
        pays();
        start();

        loadEvent();
        display(sqlBill);
        checkBill();
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

    /* private void loadTable() {
        txbTableID.removeAllItems();
        String sqlcbxPosition = "SELECT * FROM Table";
        try {
            pst = conn.prepareStatement(sqlcbxPosition);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                this.txbTableID.addItem(resultset.getString("TableID").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    private void loadEvent() {
        txbEventID.removeAllItems();
        String sqlcbxPosition = "SELECT * FROM Event";
        try {
            pst = conn.prepareStatement(sqlcbxPosition);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                this.txbEventID.addItem(resultset.getString("EventID").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        disabled();
        jName.setText(detail.getName());
        txtDate.setText(String.valueOf(new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())));
    }

    private void pays() {
        txtTongTienHoaDon.setText("0 VND");
        String sqlPay = "SELECT * FROM Bill";
        try {
            pst = conn.prepareStatement(sqlPay);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                String[] s1 = resultset.getString("IntoMoney").toString().trim().split("\\s");
                String[] s2 = txtTongTienHoaDon.getText().split("\\s");
                double totalMoney = convertedToNumbers(s1[0]) + convertedToNumbers(s2[0]);
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                txtTongTienHoaDon.setText(formatter.format(totalMoney) + " " + s1[1]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    private void update() {
        txtTime.setText(String.valueOf(new SimpleDateFormat("HH:mm:ss").format(new java.util.Date())));
    }

    private void enabled() {
        txtLoaiSanPham.setEnabled(true);
    }

    private void disabled() {
        txtLoaiSanPham.setEnabled(false);
        txtSoLuong.setEnabled(false);
        txtTenSanPham.setEnabled(false);
    }

    private void refresh() {
        Add = false;
        Change = false;
        Pay = false;
        txtMaSanPham.setText("");
        txtGia.setText("");
        txtSoLuong.setText("");
        txtThanhTien.setText("");
        txtTienNhanKhachHang.setText("");
        txtTienDuKhachHang.setText("0 VND");
        txtTenSanPham.removeAllItems();
        txtLoaiSanPham.removeAllItems();
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnLuu.setEnabled(false);
        btnXuatHoaDon.setEnabled(false);
        disabled();
    }

    private void checkBill() {
        if (tableBanHang.getRowCount() == 0) {
            txtTongTienHoaDon.setText("0 VND");
            txtTienNhanKhachHang.setText("");
            txtTienDuKhachHang.setText("0 VND");
            btnThanhToan.setEnabled(false);
            txtTienNhanKhachHang.setEnabled(false);
        } else {
            btnThanhToan.setEnabled(true);
            txtTienNhanKhachHang.setEnabled(true);
        }
    }

    private boolean check() {
        boolean kq = true;
        String sqlCheck = "SELECT * FROM Bill";
        try {
            PreparedStatement pstCheck = conn.prepareStatement(sqlCheck);
            ResultSet rsCheck = pstCheck.executeQuery();
            while (rsCheck.next()) {
                if (this.txtMaSanPham.getText().equals(rsCheck.getString("ID").toString().trim())) {
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
        if (String.valueOf(this.txtMaSanPham.getText()).length() == 0) {
            txtTrangThai.setText("Sản phẩm không được trống!");
            return false;
        } else if (String.valueOf(this.txtSoLuong.getText()).length() == 0) {
            txtTrangThai.setText("Số lượng không được trống!");
            return false;
        }
        return kq;
    }

    private void sucessful() {
        btnLuu.setEnabled(false);
        btnThem.setEnabled(true);
        btnHoaDonMoi.setEnabled(true);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }

    private void deleteInformation() {
        String sqlDelete = "DELETE FROM Statistic";
        try {
            pst = conn.prepareStatement(sqlDelete);
            pst.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void insertProduct() {
        if (checkEmpty()) {
            String sqlInsert = "INSERT INTO Bill (ID,ProductName,Quantity,IntoMoney) VALUES(?,?,?,?)";
            try {
                pst = conn.prepareStatement(sqlInsert);
                pst.setString(1, String.valueOf(txtMaSanPham.getText()));
                pst.setString(2, String.valueOf(txtTenSanPham.getSelectedItem()));
                pst.setInt(3, Integer.parseInt(txtSoLuong.getText()));
                pst.setString(4, txtThanhTien.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Đã thêm sản phẩm thành công!");
                disabled();
                sucessful();
                display(sqlBill);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void displayClassify() {
        String sql = "SELECT * FROM ProductPortfolio";
        try {
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                this.txtLoaiSanPham.addItem(resultset.getString("Product_Portfolio_Name").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProduct() {
        int Click = tableBanHang.getSelectedRow();
        TableModel tbModel = tableBanHang.getModel();
        String sqlChange = "UPDATE Bill SET Quantity=?,IntoMoney=? WHERE ID='" + tbModel.getValueAt(Click, 0).toString().trim() + "'";
        try {
            pst = conn.prepareStatement(sqlChange);
            pst.setInt(1, Integer.parseInt(this.txtSoLuong.getText()));
            pst.setString(2, txtThanhTien.getText());
            pst.executeUpdate();
            disabled();
            sucessful();
            txtTrangThai.setText("Lưu thay đổi thành công!");
            display(sqlBill);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void display(String sql) {
        tableBanHang.removeAll();
        try {
            String[] data = {"Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Tiền"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                //vector.add(resultset.getString("TableID").trim());
                vector.add(resultset.getString("ID").trim());
                vector.add(resultset.getString("ProductName").trim());
                vector.add(resultset.getInt("Quantity"));
                vector.add(resultset.getString("IntoMoney").trim());
                tbModel.addRow(vector);
            }
            tableBanHang.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void consistency() {
        String sqlBill = "SELECT * FROM Bill";
        try {
            PreparedStatement pstBill = conn.prepareStatement(sqlBill);
            ResultSet rsBill = pstBill.executeQuery();
            while (rsBill.next()) {
                try {
                    String sqlTemp = "SELECT * FROM Product WHERE ProductID ='" + rsBill.getString("ID") + "'";
                    PreparedStatement pstTemp = conn.prepareStatement(sqlTemp);
                    ResultSet rsTemp = pstTemp.executeQuery();
                    if (rsTemp.next()) {
                        String sqlUpdate = "UPDATE Product SET QuantityRemaining=? WHERE ProductID='" + rsBill.getString("ID").trim() + "'";
                        try {
                            pst = conn.prepareStatement(sqlUpdate);
                            pst.setInt(1, rsTemp.getInt("QuantityRemaining") - rsBill.getInt("Quantity"));
                            pst.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkProducts() {
        String sqlCheck = "SELECT QuantityRemaining FROM Product WHERE ProductID='" + txtMaSanPham.getText() + "'";
        try {
            pst = conn.prepareCall(sqlCheck);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                if (resultset.getInt("QuantityRemaining") == 0) {
                    txtTrangThai.setText("Sản phẩm này đã hết hàng!!");
                    btnLuu.setEnabled(false);
                    txtSoLuong.setEnabled(false);
                } else {
                    txtTrangThai.setText("Mặt hàng này còn " + resultset.getInt("QuantityRemaining") + " sản phẩm!!");
                    btnLuu.setEnabled(true);
                    txtSoLuong.setEnabled(true);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

    private void insertRevenue() {
        String sqlPay = "INSERT INTO Statistic (EmployeeName,Date,Time,IntoMoney,Money,Surplus) VALUES(?,?,?,?,?,?)";
        String[] s = txtTongTienHoaDon.getText().split("\\s");
        try {
            pst = conn.prepareStatement(sqlPay);
            pst.setString(1, jName.getText());
            pst.setDate(2, new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(txtDate.getText()).getTime()));
            pst.setString(3, txtTime.getText());
            pst.setString(4, txtTongTienHoaDon.getText());
            pst.setString(5, txtTienNhanKhachHang.getText() + " " + s[1]);
            pst.setString(6, txtTienDuKhachHang.getText());
            pst.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String cutChar(String arry) {
        return arry.replaceAll("\\D+", "");
    }

    private void loadPriceandClassify(String s) {
        String sql = "SELECT * FROM Product where ProductID=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, String.valueOf(s));
            resultset = pst.executeQuery();
            while (resultset.next()) {
                txtLoaiSanPham.addItem(resultset.getString("Product_PortfolioID").trim());
                txtGia.setText(resultset.getString("Price").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableBanHang = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLoaiSanPham = new javax.swing.JLabel();
        jTenSanPham = new javax.swing.JLabel();
        jSoLuong = new javax.swing.JLabel();
        jThanhTien = new javax.swing.JLabel();
        txtLoaiSanPham = new javax.swing.JComboBox<>();
        txtTenSanPham = new javax.swing.JComboBox<>();
        txtThanhTien = new javax.swing.JTextField();
        txtSoLuong = new javax.swing.JTextField();
        jMaSanPham = new javax.swing.JLabel();
        txtMaSanPham = new javax.swing.JTextField();
        jGia = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txbTableID = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txbEventID = new javax.swing.JComboBox<>();
        txtDiscount = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnHoaDonMoi = new javax.swing.JButton();
        btnXuatHoaDon = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jName = new javax.swing.JLabel();
        txtDate = new javax.swing.JLabel();
        txtTime = new javax.swing.JLabel();
        jGio = new javax.swing.JLabel();
        jNgay = new javax.swing.JLabel();
        jTongTienHoaDon = new javax.swing.JLabel();
        txtTongTienHoaDon = new javax.swing.JLabel();
        jTienNhanKhachHang = new javax.swing.JLabel();
        txtTienNhanKhachHang = new javax.swing.JTextField();
        jTienDuKhachhHang = new javax.swing.JLabel();
        txtTienDuKhachHang = new javax.swing.JLabel();
        txtTrangThai = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnLanguage = new javax.swing.JToggleButton();
        btnHome = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sell");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tableBanHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableBanHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableBanHangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableBanHang);

        jPanel1.setBackground(new java.awt.Color(80, 180, 155));

        jLoaiSanPham.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLoaiSanPham.setText("Loại Sản Phẩm:");

        jTenSanPham.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTenSanPham.setText("Tên Sản Phẩm:");

        jSoLuong.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jSoLuong.setText("Số Lượng:");

        jThanhTien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jThanhTien.setText("Thành Tiền:");

        txtLoaiSanPham.setBackground(new java.awt.Color(204, 255, 153));
        txtLoaiSanPham.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                txtLoaiSanPhamPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        txtTenSanPham.setBackground(new java.awt.Color(204, 255, 153));
        txtTenSanPham.setEnabled(false);
        txtTenSanPham.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                txtTenSanPhamPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        txtTenSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenSanPhamActionPerformed(evt);
            }
        });

        txtThanhTien.setEnabled(false);

        txtSoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSoLuongKeyReleased(evt);
            }
        });

        jMaSanPham.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMaSanPham.setText("Mã Sản Phẩm:");

        txtMaSanPham.setEnabled(false);

        jGia.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jGia.setText("Giá:");

        txtGia.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Table ID:");

        txbTableID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Event ID:");

        txbEventID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        txbEventID.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                txbEventIDPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txbEventID, 0, 120, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTenSanPham)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txbTableID, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtLoaiSanPham, 0, 200, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jMaSanPham)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jSoLuong)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jThanhTien)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jGia)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTenSanPham)
                    .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jGia)
                    .addComponent(jLabel3)
                    .addComponent(txbEventID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txbTableID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSoLuong)
                        .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jThanhTien)
                        .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLoaiSanPham)
                        .addComponent(txtLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jMaSanPham)
                        .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(80, 180, 155));

        btnThem.setBackground(new java.awt.Color(255, 255, 255));
        btnThem.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add_1.png"))); // NOI18N
        btnThem.setText("Thêm Sản Phẩm");
        btnThem.setEnabled(false);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 255, 255));
        btnXoa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete_1.png"))); // NOI18N
        btnXoa.setText("Xóa Sản Phẩm");
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

        btnHoaDonMoi.setBackground(new java.awt.Color(255, 255, 255));
        btnHoaDonMoi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHoaDonMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/HoaDonNew.png"))); // NOI18N
        btnHoaDonMoi.setText("Hóa Đơn Mới");
        btnHoaDonMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoaDonMoiActionPerformed(evt);
            }
        });

        btnXuatHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        btnXuatHoaDon.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnXuatHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Print Sale.png"))); // NOI18N
        btnXuatHoaDon.setText("Xuất Hóa Đơn");
        btnXuatHoaDon.setEnabled(false);
        btnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHoaDonActionPerformed(evt);
            }
        });

        btnThanhToan.setBackground(new java.awt.Color(255, 255, 255));
        btnThanhToan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Pay.png"))); // NOI18N
        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jName.setText("Name");

        txtDate.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtDate.setText("Date");

        txtTime.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTime.setText("Time");

        jGio.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jGio.setText("Giờ:");

        jNgay.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jNgay.setText("Ngày:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jNgay)
                            .addComponent(jGio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTime, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                            .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDate)
                    .addComponent(jNgay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTime)
                    .addComponent(jGio))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHoaDonMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLuu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHoaDonMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTongTienHoaDon.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTongTienHoaDon.setText("Tổng Tiền Hóa Đơn:");

        txtTongTienHoaDon.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtTongTienHoaDon.setText("0 VND");

        jTienNhanKhachHang.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTienNhanKhachHang.setText("Tiền Nhận Khách Hàng :");

        txtTienNhanKhachHang.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTienNhanKhachHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTienNhanKhachHangKeyReleased(evt);
            }
        });

        jTienDuKhachhHang.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTienDuKhachhHang.setText("Tiền Dư Của Khách:");

        txtTienDuKhachHang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTienDuKhachHang.setText("0 VND");

        txtTrangThai.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTrangThai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTrangThai.setText("Trạng Thái");

        jPanel4.setBackground(new java.awt.Color(80, 180, 155));

        btnLanguage.setBackground(new java.awt.Color(255, 255, 255));
        btnLanguage.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLanguage.setText("English");
        btnLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLanguageActionPerformed(evt);
            }
        });

        btnHome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Home.png"))); // NOI18N
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("SALES");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(331, 331, 331)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 357, Short.MAX_VALUE)
                .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTongTienHoaDon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTongTienHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTienNhanKhachHang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTienNhanKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTienDuKhachhHang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTienDuKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTrangThai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTongTienHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTongTienHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTienNhanKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTienDuKhachhHang)
                    .addComponent(jTienNhanKhachHang)
                    .addComponent(txtTienDuKhachHang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        if (this.detail.getUser().toString().toString().equals("Admin")) {
            HomeAdmin home = new HomeAdmin(detail);
            this.setVisible(false);
            home.setVisible(true);
        } else {
            HomeAdmin home = new HomeAdmin(detail);
            this.setVisible(false);
            home.setVisible(true);
        }
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        refresh();
        Add = true;
        btnThem.setEnabled(false);
        btnLuu.setEnabled(true);
        enabled();
        displayClassify();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnHoaDonMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoaDonMoiActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn muốn tạo hóa đơn mới không?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM Bill";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.executeUpdate();
                this.txtTrangThai.setText("Đã tạo hóa đơn mới!");
                display(sqlBill);
                checkBill();
                refresh();
                btnThem.setEnabled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnHoaDonMoiActionPerformed

    private void tableBanHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBanHangMouseClicked
        txtLoaiSanPham.removeAllItems();
        txtTenSanPham.removeAllItems();
        int Click = tableBanHang.getSelectedRow();
        TableModel tbModel = tableBanHang.getModel();
        txtMaSanPham.setText(tbModel.getValueAt(Click, 0).toString());
        txtTenSanPham.addItem(tbModel.getValueAt(Click, 1).toString());
        txtSoLuong.setText(tbModel.getValueAt(Click, 2).toString());
        txtThanhTien.setText(tbModel.getValueAt(Click, 3).toString());
        loadPriceandClassify(tbModel.getValueAt(Click, 0).toString());
        btnSua.setEnabled(true);
        btnXoa.setEnabled(true);
    }//GEN-LAST:event_tableBanHangMouseClicked

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        if (Add == true) {
            if (check()) {
                insertProduct();
            } else {
                txtTrangThai.setText("Sản phẩm đã tồn tại trong hóa đơn");
            }
        } else if (Change == true) {
            updateProduct();
        }
        checkBill();
        pays();
    }//GEN-LAST:event_btnLuuActionPerformed

    private void txtLoaiSanPhamPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_txtLoaiSanPhamPopupMenuWillBecomeInvisible
        txtTenSanPham.removeAllItems();
        String sql = "SELECT * FROM Product where Product_PortfolioID=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, this.txtLoaiSanPham.getSelectedItem().toString());
            resultset = pst.executeQuery();
            while (resultset.next()) {
                this.txtTenSanPham.addItem(resultset.getString("ProductName").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (txtTenSanPham.getItemCount() == 0) {
            txtTenSanPham.setEnabled(false);
            txtSoLuong.setEnabled(false);
            txtMaSanPham.setText("");
            txtGia.setText("");
            txtSoLuong.setText("");
            txtThanhTien.setText("");
        } else {
            txtTenSanPham.setEnabled(true);
        }
    }//GEN-LAST:event_txtLoaiSanPhamPopupMenuWillBecomeInvisible

    private void txtSoLuongKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongKeyReleased
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        txtSoLuong.setText(cutChar(txtSoLuong.getText()));
        if (txtSoLuong.getText().equals("")) {
            String[] s = txtGia.getText().split("\\s");
            txtThanhTien.setText("0" + " " + s[1]);
        } else {
            String sqlCheck = "SELECT QuantityRemaining FROM Product WHERE ProductID='" + txtMaSanPham.getText() + "'";
            try {
                pst = conn.prepareStatement(sqlCheck);
                resultset = pst.executeQuery();
                while (resultset.next()) {
                    if ((resultset.getInt("QuantityRemaining") - Integer.parseInt(txtSoLuong.getText())) < 0) {
                        String[] s = txtGia.getText().split("\\s");
                        txtThanhTien.setText("0" + " " + s[1]);
                        txtTrangThai.setText("Số lượng sản phẩm bán không được vượt quá số lượng hàng trong kho!!");
                        btnLuu.setEnabled(false);
                    } else {
                        int soluong = Integer.parseInt(txtSoLuong.getText().toString());
                        String[] s = txtGia.getText().split("\\s");
                        txtThanhTien.setText(formatter.format(convertedToNumbers(s[0]) * soluong) + " " + s[1]);
                        txtTrangThai.setText("Số lượng sản phẩm bán hợp lệ!!");
                        btnLuu.setEnabled(true);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_txtSoLuongKeyReleased

    private void txtTenSanPhamPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_txtTenSanPhamPopupMenuWillBecomeInvisible
        String sql = "SELECT * FROM Product where ProductName=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, this.txtTenSanPham.getSelectedItem().toString());
            resultset = pst.executeQuery();
            while (resultset.next()) {
                txtMaSanPham.setText(resultset.getString("ProductID").trim());
                txtGia.setText(resultset.getString("Price").trim());
                txtSoLuong.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkProducts();
    }//GEN-LAST:event_txtTenSanPhamPopupMenuWillBecomeInvisible

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        Add = false;
        Change = true;
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnLuu.setEnabled(true);
        txtSoLuong.setEnabled(true);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa sản phẩm khỏi hóa đơn?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM Bill WHERE ID = ?";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, String.valueOf(txtMaSanPham.getText()));
                pst.executeUpdate();
                this.txtTrangThai.setText("Đã xóa sản phẩm!");
                refresh();
                display(sqlBill);
                sucessful();
                checkBill();
                pays();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refresh();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        deleteInformation();
        if (Pay == true) {
            String[] s = txtTongTienHoaDon.getText().split("\\s");
            String sqlPay = "INSERT INTO BillInformation (EmployeeName,Date,Time,IntoMoney,Money,Surplus) VALUES(?,?,?,?,?,?)";
            try {
                pst = conn.prepareStatement(sqlPay);
                pst.setString(1, jName.getText());
                java.util.Date today = new java.util.Date();
                pst.setDate(2, new java.sql.Date(today.getTime()));
                pst.setString(3, txtTime.getText());
                pst.setString(4, txtTongTienHoaDon.getText());
                pst.setString(5, txtTienNhanKhachHang.getText() + " " + s[1]);
                pst.setString(6, txtTienDuKhachHang.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Đã thanh toán thành công!!");
                insertRevenue();
                disabled();
                sucessful();
                consistency();
                btnXuatHoaDon.setEnabled(true);
                btnThem.setEnabled(false);
                btnThanhToan.setEnabled(false);
                txtTienNhanKhachHang.setEnabled(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (Pay == false) {
            JOptionPane.showMessageDialog(null, "Bạn cần nhập số tiền khách hàng thanh toán !");
        }
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void txtTienNhanKhachHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienNhanKhachHangKeyReleased
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        if (txtTienNhanKhachHang.getText().equals("")) {
            String[] s = txtTongTienHoaDon.getText().split("\\s");
            txtTienDuKhachHang.setText("0" + " " + s[1]);
        } else {
            txtTienNhanKhachHang.setText(formatter.format(convertedToNumbers(txtTienNhanKhachHang.getText())));

            String s1 = txtTienNhanKhachHang.getText();
            String[] s2 = txtTongTienHoaDon.getText().split("\\s");

            if ((convertedToNumbers(s1) - convertedToNumbers(s2[0])) >= 0) {
                txtTienDuKhachHang.setText(formatter.format((convertedToNumbers(s1) - convertedToNumbers(s2[0]))) + " " + s2[1]);
                txtTrangThai.setText("Số tiền khách hàng đưa đã hợp lệ!");
                Pay = true;
            } else {

                txtTienDuKhachHang.setText(formatter.format((convertedToNumbers(s1) - convertedToNumbers(s2[0]))) + " " + s2[1]);
                txtTrangThai.setText("Số tiền khách hàng đưa nhỏ hơn tổng tiền mua hàng trong hóa đơn!");
                Pay = false;
            }
        }
    }//GEN-LAST:event_txtTienNhanKhachHangKeyReleased

    private void btnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHoaDonActionPerformed

        try {
            JasperReport report = JasperCompileManager.compileReport("D:\\JAVA-2\\Thi\\QuanLyCuaHangMyPham\\src\\UserInterFace\\Bill.jrxml");
            JasperPrint print = JasperFillManager.fillReport(report, null, conn);
            JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnXuatHoaDonActionPerformed

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

    private void btnLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageActionPerformed
        String file = btnLanguage.isSelected() ? "Internationalization/English" : "Internationalization/Vietnamese";
        String languagecodes = btnLanguage.isSelected() ? "en" : "vi";
        String countrycodes = btnLanguage.isSelected() ? "US" : "VN";
        String togglebutton = btnLanguage.isSelected() ? "English" : "Vietnamese";
        btnLanguage.setText(togglebutton);
        Locale currentLocale = new Locale(languagecodes, countrycodes);
        ResourceBundle language = ResourceBundle.getBundle(file, currentLocale);
        // jTitle.setText(language.getString("tieudebanhang"));
        jLoaiSanPham.setText(language.getString("loaisanpham"));
        jTenSanPham.setText(language.getString("tensanpham"));
        jGia.setText(language.getString("gia"));
        jMaSanPham.setText(language.getString("masanpham"));
        jSoLuong.setText(language.getString("soluong"));
        jThanhTien.setText(language.getString("thanhtien"));
        btnThem.setText(language.getString("btnthem"));
        btnSua.setText(language.getString("btnsua"));
        btnXoa.setText(language.getString("btnxoa"));
        btnLuu.setText(language.getString("btnluu"));
        btnHoaDonMoi.setText(language.getString("btnhoadonmoi"));
        btnThanhToan.setText(language.getString("thanhtoan"));
        btnXuatHoaDon.setText(language.getString("btnxuathoadon"));
        jTongTienHoaDon.setText(language.getString("tongtienhoadon"));
        jTienNhanKhachHang.setText(language.getString("tiennhankhachhang"));
        jTienDuKhachhHang.setText(language.getString("tiendukhachhang"));
        jNgay.setText(language.getString("ngay"));
        jGio.setText(language.getString("gio"));
        txtTrangThai.setText(language.getString("trangthai"));
    }//GEN-LAST:event_btnLanguageActionPerformed

    private void txtTenSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenSanPhamActionPerformed
    }//GEN-LAST:event_txtTenSanPhamActionPerformed

    private void txbEventIDPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_txbEventIDPopupMenuWillBecomeInvisible
        String sql = "SELECT * FROM Event where EventID=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, this.txbEventID.getSelectedItem().toString());
            resultset = pst.executeQuery();
            while (resultset.next()) {
                txtDiscount.setText(resultset.getString("PercentDiscount").trim());
                txtDiscount.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_txbEventIDPopupMenuWillBecomeInvisible

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail = new Detail();
                new Sales(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHoaDonMoi;
    private javax.swing.JButton btnHome;
    private javax.swing.JToggleButton btnLanguage;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXuatHoaDon;
    private javax.swing.JLabel jGia;
    private javax.swing.JLabel jGio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLoaiSanPham;
    private javax.swing.JLabel jMaSanPham;
    private javax.swing.JLabel jName;
    private javax.swing.JLabel jNgay;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jSoLuong;
    private javax.swing.JLabel jTenSanPham;
    private javax.swing.JLabel jThanhTien;
    private javax.swing.JLabel jTienDuKhachhHang;
    private javax.swing.JLabel jTienNhanKhachHang;
    private javax.swing.JLabel jTongTienHoaDon;
    private javax.swing.JTable tableBanHang;
    private javax.swing.JComboBox<String> txbEventID;
    private javax.swing.JComboBox<String> txbTableID;
    private javax.swing.JLabel txtDate;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtGia;
    private javax.swing.JComboBox<String> txtLoaiSanPham;
    private javax.swing.JTextField txtMaSanPham;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JComboBox<String> txtTenSanPham;
    private javax.swing.JTextField txtThanhTien;
    private javax.swing.JLabel txtTienDuKhachHang;
    private javax.swing.JTextField txtTienNhanKhachHang;
    private javax.swing.JLabel txtTime;
    private javax.swing.JLabel txtTongTienHoaDon;
    private javax.swing.JLabel txtTrangThai;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while (true) {
            update();
            try {
                Thread.sleep(1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
