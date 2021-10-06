package UserInterFace;

import java.awt.Color;
import java.sql.Connection;
import java.sql.Date;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class OrderManagement extends javax.swing.JFrame {

    private static Connection conn = null;
    private static PreparedStatement pst = null;
    private static ResultSet resultset = null;
    private boolean Add = false, Change = false;
    private String sqlOrder = "SELECT * FROM Order";
    private Detail detail;

    public OrderManagement(Detail d) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        connectionSQLServer();
        detail = new Detail(d);
        displayOrder(sqlOrder);
        disabled();
        txtTrangThai.setForeground(Color.red);
        loadEmployees();
        loadProduct();
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

    private void enabled() {
        txtOrderID.setEnabled(true);
        txbProductID.setEditable(true);
        txtCustomerName.setEnabled(true);
        txtAddress.setEnabled(true);
        txtPhone.setEnabled(true);
        txbEmployeeID.setEnabled(true);
        txtQuantity.setEnabled(true);
        txtDate.setEnabled(true);
        txtTotalBillAmount.setEnabled(true);
        txbShippingStatus.setEnabled(true);

        txtTrangThai.setText("Status");
    }

    private void disabled() {
        txtOrderID.setEnabled(false);
        txbProductID.setEditable(false);
        txtCustomerName.setEnabled(false);
        txtAddress.setEnabled(false);
        txtPhone.setEnabled(false);
        txbEmployeeID.setEnabled(false);
        txtQuantity.setEnabled(false);
        txtDate.setEnabled(false);
        txtTotalBillAmount.setEnabled(false);
        txbShippingStatus.setEnabled(false);

    }

    public void displayOrder(String sql) {
        tableOrder.removeAll();
        try {
            String[] data = {"Order ID", "Product ID", "Customer Name", "Address", "Phone", "Employee ID", "Quantity", "Order Creation Date", "Total Bill Amount", "Shipping Status"};
            DefaultTableModel tbModel = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("OrderID").trim());
                vector.add(resultset.getString("ProductID").trim());
                vector.add(resultset.getString("CustomerName").trim());
                vector.add(resultset.getString("Address").trim());
                vector.add(resultset.getString("Phone").trim());
                vector.add(resultset.getString("EmployeeID").trim());
                vector.add(resultset.getString("Quantity").trim());
                vector.add(new SimpleDateFormat("dd/MM/yyyy").format(resultset.getDate("OrderCreationDate")));
                vector.add(resultset.getString("TotalBillAmount").trim());
                vector.add(resultset.getString("ShippingStatus").trim());
                tbModel.addRow(vector);
            }
            tableOrder.setModel(tbModel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadProduct() {
        String sql = "SELECT * FROM Product";
        try {
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                this.txbProductID.addItem(resultset.getString("ProductID").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmployees() {
        String sql = "SELECT * FROM Employee";
        try {
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                this.txbEmployeeID.addItem(resultset.getString("EmployeeID").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadShippingStatus() {
        txbShippingStatus.removeAllItems();
        txbShippingStatus.addItem("Delivered");
        txbShippingStatus.addItem("Not Delivery");
    }

    private void refresh() {
        Add = false;
        Change = false;
        txtOrderID.setText("");
        txtCustomerName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtTotalBillAmount.setText("");
        txtQuantity.setText("");
        ((JTextField) txtDate.getDateEditor().getUiComponent()).setText("");
        txbProductID.removeAllItems();
        txbEmployeeID.removeAllItems();
        txbShippingStatus.removeAllItems();
        btnAdd.setEnabled(true);
        btnDelete.setEnabled(false);
        btnUpdate.setEnabled(false);
        btnSave.setEnabled(false);
        disabled();
    }

    private void insertOrder() {
        if (checkEmpty()) {
            String sqlInsert = "INSERT INTO Orders (OrderID,CustomerName,Address,Phone,ProductName,Amount,Price,WarrantyPeriod,IntoMoney,Date,PaymentMethods) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            try {
                pst = conn.prepareStatement(sqlInsert);
                pst.setString(1, this.txtOrderID.getText());
                pst.setString(2, String.valueOf(this.txbProductID.getSelectedItem()));
                pst.setString(3, txtCustomerName.getText());
                pst.setString(4, txtAddress.getText());
                pst.setString(5, txtPhone.getText());
                pst.setString(6, String.valueOf(this.txbEmployeeID.getSelectedItem()));
                pst.setInt(7, Integer.parseInt(this.txtQuantity.getText()));
                pst.setString(9, this.txtTotalBillAmount.getText());
                pst.setDate(8, new java.sql.Date(txtDate.getDate().getTime()));
                pst.setString(10, String.valueOf(this.txbShippingStatus.getSelectedItem()));
                pst.executeUpdate();
                txtTrangThai.setText("Added Success!");
                disabled();
                refresh();
                displayOrder(sqlOrder);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateOrder() {
        int Click = tableOrder.getSelectedRow();
        TableModel tbModel = tableOrder.getModel();
        if (checkEmpty()) {
            String sqlChange = "UPDATE [dbo].[Order] SET [OrderID]=?, [ProductID]=?, [CustomerName]=?, [Address]=?, [Phone]=?, [EmployeeID]=?,[Quantity] =?,[OrderCreationDate] =?, [TotalBillAmount]=?, [ShippingStatus]=?\n"
                    + "WHERE [OrderID]='" + tbModel.getValueAt(Click, 0).toString().trim() + "'";
            try {
                pst = conn.prepareStatement(sqlChange);
                pst.setString(1, this.txtOrderID.getText());
                pst.setString(2, String.valueOf(this.txbProductID.getSelectedItem()));
                pst.setString(3, this.txtCustomerName.getText());
                pst.setString(4, txtAddress.getText());
                pst.setString(5, txtPhone.getText());
                pst.setString(6, String.valueOf(this.txbEmployeeID.getSelectedItem()));
                pst.setInt(7, Integer.parseInt(this.txtQuantity.getText()));
                pst.setString(9, this.txtTotalBillAmount.getText());
                pst.setDate(8, new java.sql.Date(txtDate.getDate().getTime()));
                pst.setString(10, String.valueOf(this.txbShippingStatus.getSelectedItem()));
                pst.executeUpdate();
                txtTrangThai.setText("Updated Successfully!");
                disabled();
                refresh();
                displayOrder(sqlOrder);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean check() {
        boolean kq = true;
        String sqlCheck = "SELECT * FROM Order";
        try {
            pst = conn.prepareStatement(sqlCheck);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                if (this.txtOrderID.getText().equals(resultset.getString("OrderID").toString().trim())) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    public boolean checkEmpty() {
        boolean kq = true;
        if (String.valueOf(this.txtOrderID.getText()).length() == 0) {
            txtTrangThai.setText("OrderID cannot be empty. Please enter!!");
            return false;
        }
        if (String.valueOf(this.txtCustomerName.getText()).length() == 0) {
            txtTrangThai.setText("Customer Name cannot be empty. Please choose!");
            return false;
        }
        if (String.valueOf(this.txtAddress.getText()).length() == 0) {
            txtTrangThai.setText("Address cannot be empty. Please enter!");
            return false;
        }
        if (String.valueOf(this.txtPhone.getText()).length() == 0) {
            txtTrangThai.setText("Phone cannot be empty. Please enter!!");
            return false;
        }

        if (String.valueOf(this.txbProductID.getSelectedItem()).length() == 0) {
            txtTrangThai.setText("Product ID cannot be empty. Please choose!");
            return false;
        }
        if (String.valueOf(this.txbEmployeeID.getSelectedItem()).length() == 0) {
            txtTrangThai.setText("Employee ID cannot be empty. Please choose!");
            return false;
        }
        if (String.valueOf(this.txbShippingStatus.getSelectedItem()).length() == 0) {
            txtTrangThai.setText("Shipping Status cannot be empty. Please choose!");
            return false;
        }
        if (String.valueOf(this.txtQuantity.getText()).length() == 0) {
            txtTrangThai.setText("Quantity cannot be empty. Please enter!");
            return false;
        }
        if (String.valueOf(this.txtTotalBillAmount.getText()).length() == 0) {
            txtTrangThai.setText("Total Bill Amount cannot be empty. Please enter!");
            return false;
        }
        if (String.valueOf(((JTextField) this.txtDate.getDateEditor().getUiComponent()).getText()).length() == 0) {
            txtTrangThai.setText("Order creation date cannot be empty. Please choose!");
            return false;
        }
        return kq;
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

    private void loadProductt() {
        txbProductID.removeAllItems();
        String sql = "SELECT * FROM Product WHERE ProductID=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, this.txbProductID.getSelectedItem().toString());
            resultset = pst.executeQuery();
            while (resultset.next()) {
                txbProductID.addItem(resultset.getString("ProductID").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmployee() {
        txbEmployeeID.removeAllItems();
        String sql = "SELECT * FROM Employee WHERE EmployeeID=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, this.txbEmployeeID.getSelectedItem().toString());
            resultset = pst.executeQuery();
            while (resultset.next()) {
                txbEmployeeID.addItem(resultset.getString("EmployeeID").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTrangThai = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jMaDonHang = new javax.swing.JLabel();
        txtOrderID = new javax.swing.JTextField();
        jTenKhachHang = new javax.swing.JLabel();
        jDiaChi = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        jSDT = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLoaiSanPham = new javax.swing.JLabel();
        jSoLuong = new javax.swing.JLabel();
        jGia = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jThoiGianBaoHanh = new javax.swing.JLabel();
        jNgayDat = new javax.swing.JLabel();
        jThanhTien = new javax.swing.JLabel();
        txtTotalBillAmount = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txbShippingStatus = new javax.swing.JComboBox<>();
        txtDate = new com.toedter.calendar.JDateChooser();
        txbEmployeeID = new javax.swing.JComboBox<>();
        txbProductID = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableOrder = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnLanguage = new javax.swing.JToggleButton();
        btnBackHome = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("OrderManagement");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtTrangThai.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTrangThai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTrangThai.setText("Status");

        jPanel4.setBackground(new java.awt.Color(80, 180, 155));

        jMaDonHang.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jMaDonHang.setForeground(new java.awt.Color(255, 255, 255));
        jMaDonHang.setText("Order ID:");

        jTenKhachHang.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jTenKhachHang.setForeground(new java.awt.Color(255, 255, 255));
        jTenKhachHang.setText("Product ID:");

        jDiaChi.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jDiaChi.setForeground(new java.awt.Color(255, 255, 255));
        jDiaChi.setText("Customer Name:");

        jSDT.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jSDT.setForeground(new java.awt.Color(255, 255, 255));
        jSDT.setText("Address:");

        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAddressKeyReleased(evt);
            }
        });

        jLoaiSanPham.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLoaiSanPham.setForeground(new java.awt.Color(255, 255, 255));
        jLoaiSanPham.setText("Phone:");

        jSoLuong.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jSoLuong.setForeground(new java.awt.Color(255, 255, 255));
        jSoLuong.setText("Employee ID:");

        jGia.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jGia.setForeground(new java.awt.Color(255, 255, 255));
        jGia.setText("Quantity:");
        jGia.setToolTipText("");

        txtQuantity.setEnabled(false);

        jThoiGianBaoHanh.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jThoiGianBaoHanh.setForeground(new java.awt.Color(255, 255, 255));
        jThoiGianBaoHanh.setText("Order Creation Date:");

        jNgayDat.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jNgayDat.setForeground(new java.awt.Color(255, 255, 255));
        jNgayDat.setText("Total Bill Amount:");

        jThanhTien.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jThanhTien.setForeground(new java.awt.Color(255, 255, 255));
        jThanhTien.setText("Shipping Status:");

        txbShippingStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txbEmployeeID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txbProductID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jDiaChi))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jMaDonHang)
                                    .addComponent(jTenKhachHang))))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtOrderID)
                            .addComponent(txtCustomerName)
                            .addComponent(txbProductID, 0, 226, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSDT)
                            .addComponent(jLoaiSanPham))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAddress)
                            .addComponent(txtPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jGia)
                            .addComponent(jThoiGianBaoHanh)
                            .addComponent(jSoLuong))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtQuantity)
                            .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .addComponent(txbEmployeeID, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jThanhTien, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jNgayDat, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTotalBillAmount)
                            .addComponent(txbShippingStatus, 0, 221, Short.MAX_VALUE))))
                .addGap(126, 126, 126))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jMaDonHang)
                            .addComponent(txtOrderID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTenKhachHang)
                            .addComponent(txbProductID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jThoiGianBaoHanh))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDiaChi)
                                .addGap(2, 2, 2))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSoLuong)
                            .addComponent(txbEmployeeID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jGia))))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jNgayDat)
                            .addComponent(txtTotalBillAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jThanhTien)
                            .addComponent(txbShippingStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSDT))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLoaiSanPham)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(80, 180, 155));

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(255, 255, 255));
        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add_1.png"))); // NOI18N
        btnAdd.setText("Insert");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Update.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setEnabled(false);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 255, 255));
        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete_1.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setAlignmentX(0.5F);
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tableOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableOrderMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableOrder);

        btnSave.setBackground(new java.awt.Color(255, 255, 255));
        btnSave.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setAlignmentX(0.5F);
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Search_1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(172, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSearch))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(141, 141, 141))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnDelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(txtSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBackground(new java.awt.Color(80, 180, 155));

        btnLanguage.setBackground(new java.awt.Color(204, 255, 153));
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

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ORDER MANAGEMENT");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(147, 147, 147)
                .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(btnLanguage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnBackHome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTrangThai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int Click = JOptionPane.showConfirmDialog(null, "Do you want to close the application??", "Thông Báo", 2);
        if (Click == JOptionPane.OK_OPTION) {
            System.exit(0);
        } else {
            if (Click == JOptionPane.CANCEL_OPTION) {
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void txtAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyReleased
        txtAddress.setText(cutChar(txtAddress.getText()));
    }//GEN-LAST:event_txtAddressKeyReleased

    private void tableOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableOrderMouseClicked
        int Click = tableOrder.getSelectedRow();
        TableModel tbModel = tableOrder.getModel();
        txbProductID.removeAllItems();
        txbEmployeeID.removeAllItems();
        txbShippingStatus.removeAllItems();
        txtOrderID.setText(tbModel.getValueAt(Click, 0).toString());
        txbProductID.addItem(tbModel.getValueAt(Click, 1).toString());
        txtCustomerName.setText(tbModel.getValueAt(Click, 2).toString());
        txtAddress.setText(tbModel.getValueAt(Click, 3).toString());
        txtPhone.setText(tbModel.getValueAt(Click, 4).toString());
        txbEmployeeID.addItem(tbModel.getValueAt(Click, 5).toString());
        txtQuantity.setText(tbModel.getValueAt(Click, 6).toString());
        ((JTextField) txtDate.getDateEditor().getUiComponent()).setText(tbModel.getValueAt(Click, 7).toString());
        txtTotalBillAmount.setText(tbModel.getValueAt(Click, 8).toString());
        txbShippingStatus.addItem(tbModel.getValueAt(Click, 9).toString());
        loadProductt();
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);
    }//GEN-LAST:event_tableOrderMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (Add == true) {
            if (check()) {
                insertOrder();
            } else {
                txtTrangThai.setText("Order ID already exists!");
            }
        } else if (Change == true) {
            updateOrder();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Do you want to delete the order?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM Order WHERE OrderID=? ";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, txtOrderID.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Deleted Successfully!");
                disabled();
                refresh();
                displayOrder(sqlOrder);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        Change = true;
        Add = false;
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(false);
        btnUpdate.setEnabled(false);
        btnSave.setEnabled(true);
        enabled();
        loadProduct();
        loadShippingStatus();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        refresh();
        Add = true;
        btnAdd.setEnabled(false);
        btnSave.setEnabled(true);
        enabled();
        loadProduct();
        loadShippingStatus();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
        refresh();
    }//GEN-LAST:event_btnRefreshMouseClicked

    private void btnLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLanguageActionPerformed
        String file = btnLanguage.isSelected() ? "Internationalization/English" : "Internationalization/Vietnamese";
        String languagecodes = btnLanguage.isSelected() ? "en" : "vi";
        String countrycodes = btnLanguage.isSelected() ? "US" : "VN";
        String togglebutton = btnLanguage.isSelected() ? "English" : "Vietnamese";
        btnLanguage.setText(togglebutton);
        Locale currentLocale = new Locale(languagecodes, countrycodes);
        ResourceBundle language = ResourceBundle.getBundle(file, currentLocale);
        // jTitle.setText(language.getString("tieudequanlihoadon"));
        jMaDonHang.setText(language.getString("madonhang"));
        jTenKhachHang.setText(language.getString("tenkhachhang"));
        jDiaChi.setText(language.getString("diachi"));
        jSDT.setText(language.getString("sdt"));
        jLoaiSanPham.setText(language.getString("loaisanpham"));
        // jTenSanPham.setText(language.getString("tensanpham"));
        jSoLuong.setText(language.getString("soluong"));
        jGia.setText(language.getString("gia"));
        jThoiGianBaoHanh.setText(language.getString("thoigianbaohanh"));
        jNgayDat.setText(language.getString("ngaydat"));
        jThanhTien.setText(language.getString("thanhtien"));
        //  jThanhToan.setText(language.getString("thanhtoan"));
        btnAdd.setText(language.getString("btnthem"));
        btnUpdate.setText(language.getString("btnsua"));
        btnDelete.setText(language.getString("btnxoa"));
        btnSave.setText(language.getString("btnluu"));
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
                new OrderManagement(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBackHome;
    private javax.swing.JButton btnDelete;
    private javax.swing.JToggleButton btnLanguage;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jDiaChi;
    private javax.swing.JLabel jGia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLoaiSanPham;
    private javax.swing.JLabel jMaDonHang;
    private javax.swing.JLabel jNgayDat;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel jSDT;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jSoLuong;
    private javax.swing.JLabel jTenKhachHang;
    private javax.swing.JLabel jThanhTien;
    private javax.swing.JLabel jThoiGianBaoHanh;
    private javax.swing.JTable tableOrder;
    private javax.swing.JComboBox<String> txbEmployeeID;
    private javax.swing.JComboBox<String> txbProductID;
    private javax.swing.JComboBox<String> txbShippingStatus;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCustomerName;
    private com.toedter.calendar.JDateChooser txtDate;
    private javax.swing.JTextField txtOrderID;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTotalBillAmount;
    private javax.swing.JLabel txtTrangThai;
    // End of variables declaration//GEN-END:variables
}
