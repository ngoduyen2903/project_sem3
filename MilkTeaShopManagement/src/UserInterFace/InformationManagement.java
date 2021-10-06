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

public class InformationManagement extends javax.swing.JFrame {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet resultset = null;
    private Detail detail;
    private boolean Add = false, Change = false;

    String sqlMaterial = "SELECT * FROM Material";
    String sqlSupplier = "SELECT * FROM MaterialSupplier";
    String sqlMaterialType = "SELECT * FROM MaterialType";

    public InformationManagement(Detail d) {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        detail = new Detail(d);
        txtTrangThai.setForeground(Color.red);
        connectionSQLServer();
        displayMaterial(sqlMaterial);
        displaySupplier(sqlSupplier);
        displayMaterialType(sqlMaterialType);
        disabledMaterial();
        disabledMateriaType();
        disabledSupplier();
        loadMaterialType();
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

    private void loadMaterialType() {
        txbMaterialTypeID.removeAllItems();
        String sqlcbxPosition = "SELECT * FROM MaterialType";
        try {
            pst = conn.prepareStatement(sqlcbxPosition);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                this.txbMaterialTypeID.addItem(resultset.getString("Material_TypeID").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayMaterial(String sql) {
        tableMaterial.removeAll();
        try {
            String[] data = {"Material ID", "Material Name", "Unit", "Material Type ID"};
            DefaultTableModel tbModle = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("MaterialID").trim());
                vector.add(resultset.getString("MaterialName").trim());
                vector.add(resultset.getString("Unit").trim());
                vector.add(resultset.getString("Material_TypeID").trim());
                tbModle.addRow(vector);
            }
            tableMaterial.setModel(tbModle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displaySupplier(String sql) {
        tableSupplier.removeAll();
        try {
            String[] data = {"Material_SupplierID", "Material_Supplier_Name", "Address", "Phone", "Email"};
            DefaultTableModel tbModle = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("Material_SupplierID").trim());
                vector.add(resultset.getString("Material_Supplier_Name").trim());
                vector.add(resultset.getString("Address").trim());
                vector.add(resultset.getString("Phone").trim());
                vector.add(resultset.getString("Email").trim());
                tbModle.addRow(vector);
            }
            tableSupplier.setModel(tbModle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayMaterialType(String sql) {
        tableMaterialType.removeAll();
        try {
            String[] data = {"Material_TypeID", "Material_Type_Name"};
            DefaultTableModel tbModle = new DefaultTableModel(data, 0);
            pst = conn.prepareStatement(sql);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                Vector vector = new Vector();
                vector.add(resultset.getString("Material_TypeID").trim());
                vector.add(resultset.getString("Material_Type_Name").trim());
                tbModle.addRow(vector);
            }
            tableMaterialType.setModel(tbModle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void backHome() {
        HomeAdmin home = new HomeAdmin(detail);
        this.setVisible(false);
        home.setVisible(true);
    }

    private void enabledMaterial() {
        txtMaterialID.setEnabled(true);
        txtMaterialName.setEnabled(true);
        txtUnit.setEnabled(true);
        txtTrangThai.setText("Status");
    }

    private void enabledSupplier() {
        txtSupplierID.setEnabled(true);
        txtSupplierName.setEnabled(true);
        txtAddress.setEnabled(true);
        txtPhone.setEnabled(true);
        txtEmail.setEnabled(true);
        txtTrangThai.setText("Status");
    }

    private void enabledMaterialType() {
        txtMaterialTypeID.setEnabled(true);
        txtMaterialTypeName.setEnabled(true);
        txtTrangThai.setText("Status");
    }

    private void disabledMaterial() {
        txtMaterialID.setEnabled(false);
        txtMaterialName.setEnabled(false);
        txtUnit.setEnabled(false);
    }

    private void disabledSupplier() {
        txtSupplierID.setEnabled(false);
        txtSupplierName.setEnabled(false);
        txtAddress.setEnabled(false);
        txtPhone.setEnabled(false);
        txtEmail.setEnabled(false);
    }

    private void disabledMateriaType() {
        txtMaterialTypeID.setEnabled(false);
        txtMaterialTypeName.setEnabled(false);
    }

    private void refresh() {
        Change = false;
        Add = false;
        txtMaterialID.setText("");
        txtMaterialName.setText("");
        txtUnit.setText("");
        txtSupplierID.setText("");
        txtSupplierName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtMaterialTypeID.setText("");
        txtMaterialTypeName.setText("");
        btnAddMaterial.setEnabled(true);
        btnUpdateMaterial.setEnabled(false);
        btnDeleteMaterial.setEnabled(false);
        btnSaveMaterial.setEnabled(false);
        btnAddSupplier.setEnabled(true);
        btnUpdateSupplier.setEnabled(false);
        btnDeleteSupplier.setEnabled(false);
        btnSaveSupplier.setEnabled(false);
        btnThemLoaisp.setEnabled(true);
        btnUpdateMaterialType.setEnabled(false);
        btnDeleteMaterialType.setEnabled(false);
        btnSaveMaterialType.setEnabled(false);
        disabledMateriaType();
        disabledMaterial();
        disabledSupplier();
    }

    private boolean checkMaterial() {
        boolean kq = true;
        String sqlCheck = "SELECT * FROM Material";
        try {
            pst = conn.prepareStatement(sqlCheck);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                if (this.txtMaterialID.getText().equals(resultset.getString("MaterialID").toString().trim())) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    private boolean checkSupplier() {
        boolean kq = true;
        String sqlCheck = "SELECT * FROM MaterialSupplier";
        try {
            pst = conn.prepareStatement(sqlCheck);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                if (this.txtSupplierID.getText().equals(resultset.getString("Material_SupplierID").toString().trim())) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    private boolean checkMaterialType() {
        boolean kq = true;
        String sqlCheck = "SELECT * FROM MaterialType";
        try {
            pst = conn.prepareStatement(sqlCheck);
            resultset = pst.executeQuery();
            while (resultset.next()) {
                if (this.txtMaterialTypeID.getText().equals(resultset.getString("Material_TypeID").toString().trim())) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kq;
    }

    private boolean checkEmptyMaterial() {
        boolean kq = true;
        if (String.valueOf(this.txtMaterialID.getText()).length() == 0) {
            txtTrangThai.setText("Material ID cannot be empty. Please re-enter!");
            return false;
        }
        if (String.valueOf(this.txtMaterialName.getText()).length() == 0) {
            txtTrangThai.setText("Material Name cannot be empty. Please re-enter!");
            return false;
        }
        if (String.valueOf(this.txtUnit.getText()).length() == 0) {
            txtTrangThai.setText("Unit cannot be empty. Please re-enter!");
            return false;
        }
        return kq;
    }

    private boolean checkEmptySupplier() {
        boolean kq = true;
        if (String.valueOf(this.txtSupplierID.getText()).length() == 0) {
            txtTrangThai.setText("Material Supplier ID cannot be empty. Please re-enter!");
            return false;
        }
        if (String.valueOf(this.txtSupplierName.getText()).length() == 0) {
            txtTrangThai.setText("Material Supplier Name cannot be empty. Please re-enter!");
            return false;
        }
        if (String.valueOf(this.txtAddress.getText()).length() == 0) {
            txtTrangThai.setText("Address cannot be empty. Please re-enter!");
            return false;
        }
        if (String.valueOf(this.txtPhone.getText()).length() == 0) {
            txtTrangThai.setText("Phone cannot be empty. Please re-enter!");
            return false;
        }
        if (String.valueOf(this.txtEmail.getText()).length() == 0) {
            txtTrangThai.setText("Email cannot be empty. Please re-enter!");
            return false;
        }
        return kq;
    }

    private boolean checkEmptyMaterialType() {
        boolean kq = true;
        if (String.valueOf(this.txtMaterialTypeID.getText()).length() == 0) {
            txtTrangThai.setText("Material Type ID cannot be empty. Please re-enter!");
            return false;
        }
        if (String.valueOf(this.txtMaterialTypeName.getText()).length() == 0) {
            txtTrangThai.setText("Material Type Name cannot be empty. Please re-enter!");
            return false;
        }
        return kq;
    }

    private void insertMaterial() {
        if (checkEmptyMaterial()) {
            String sqlInsert = "INSERT INTO Material (MaterialID,MaterialName,Unit,Material_TypeID) VALUES(?,?,?,?)";
            try {
                pst = conn.prepareStatement(sqlInsert);
                pst.setString(1, txtMaterialID.getText());
                pst.setString(2, txtMaterialName.getText());
                pst.setString(3, txtUnit.getText() + " " + "VND");
                pst.setString(4, String.valueOf(this.txbMaterialTypeID.getSelectedItem()));
                pst.executeUpdate();
                txtTrangThai.setText("Added Success!");
                disabledMaterial();
                refresh();
                displayMaterial(sqlMaterial);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void insertSupplier() {
        if (checkEmptySupplier()) {
            String sqlInsert = "INSERT INTO MaterialSupplier (Material_SupplierID,Material_Supplier_Name,Address,Phone,Email) VALUES(?,?,?,?,?)";
            try {
                pst = conn.prepareStatement(sqlInsert);
                pst.setString(1, txtSupplierID.getText());
                pst.setString(2, txtSupplierName.getText());
                pst.setString(3, txtAddress.getText());
                pst.setString(4, txtPhone.getText());
                pst.setString(5, txtEmail.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Added Success!");
                disabledSupplier();
                refresh();
                displaySupplier(sqlSupplier);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void insertMaterialType() {
        if (checkEmptyMaterialType()) {
            String sqlInsert = "INSERT INTO MaterialType (Material_TypeID,Material_Type_Name) VALUES(?,?)";
            try {
                pst = conn.prepareStatement(sqlInsert);
                pst.setString(1, txtMaterialTypeID.getText());
                pst.setString(2, txtMaterialTypeName.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Added Success!");
                disabledMateriaType();
                refresh();
                displayMaterialType(sqlMaterialType);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateMaterial() {
        int Click = tableMaterial.getSelectedRow();
        TableModel tbModel = tableMaterial.getModel();
        if (checkEmptyMaterial()) {
            String sqlChange = "UPDATE Material SET MaterialID=?, MaterialName=?,Unit=?, Material_TypeID=? WHERE MaterialID='" + tbModel.getValueAt(Click, 0).toString().trim() + "'";
            try {
                pst = conn.prepareStatement(sqlChange);
                pst.setString(1, txtMaterialID.getText());
                pst.setString(2, txtMaterialName.getText());
                pst.setString(3, txtUnit.getText());
                pst.setString(4, (String) txbMaterialTypeID.getSelectedItem());
                pst.executeUpdate();
                txtTrangThai.setText("Updated Successfully!");
                disabledMaterial();
                refresh();
                displayMaterial(sqlMaterial);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateMaterialType() {
        int Click = tableMaterialType.getSelectedRow();
        TableModel tbModel = tableMaterialType.getModel();
        if (checkEmptyMaterialType()) {
            String sqlChange = "UPDATE MaterialType SET Material_TypeID=?, Material_Type_Name=? WHERE Material_TypeID='" + tbModel.getValueAt(Click, 0).toString().trim() + "'";;
            try {
                pst = conn.prepareStatement(sqlChange);
                pst.setString(1, txtMaterialTypeID.getText());
                pst.setString(2, txtMaterialTypeName.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Updated Successfully!");
                disabledMateriaType();
                refresh();
                displayMaterialType(sqlMaterialType);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateSupplier() {
        int Click = tableSupplier.getSelectedRow();
        TableModel tbModel = tableSupplier.getModel();
        if (checkEmptySupplier()) {
            String sqlChange = "UPDATE MaterialSupplier SET Material_SupplierID=?, Material_Supplier_Name=?, Address=?, Phone=?,Email=? WHERE Material_SupplierID='" + tbModel.getValueAt(Click, 0).toString().trim() + "'";
            try {
                pst = conn.prepareStatement(sqlChange);
                pst.setString(1, txtSupplierID.getText());
                pst.setString(2, txtSupplierName.getText());
                pst.setString(3, txtAddress.getText());
                pst.setString(4, txtPhone.getText());
                pst.setString(5, txtEmail.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Updated Successfully!");
                disabledSupplier();
                refresh();
                displaySupplier(sqlSupplier);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMaterial = new javax.swing.JTable();
        txtUnit = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLuongCoBan = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        txtMaterialName = new javax.swing.JTextField();
        jChucVu = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        txtMaterialID = new javax.swing.JTextField();
        jMaChucVu = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        btnRefresHMaterial = new javax.swing.JButton();
        btnAddMaterial = new javax.swing.JButton();
        btnUpdateMaterial = new javax.swing.JButton();
        btnDeleteMaterial = new javax.swing.JButton();
        btnSaveMaterial = new javax.swing.JButton();
        txtSearchMaterial = new javax.swing.JTextField();
        btnSearchMaterial = new javax.swing.JButton();
        jMaterial_type_id = new javax.swing.JLabel();
        txbMaterialTypeID = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableSupplier = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        btnRefreshNSX = new javax.swing.JButton();
        btnAddSupplier = new javax.swing.JButton();
        btnUpdateSupplier = new javax.swing.JButton();
        btnDeleteSupplier = new javax.swing.JButton();
        btnSaveSupplier = new javax.swing.JButton();
        jMaNSX = new javax.swing.JLabel();
        txtSupplierID = new javax.swing.JTextField();
        jNhaSX = new javax.swing.JLabel();
        txtSupplierName = new javax.swing.JTextField();
        jDiaChi = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jSDT = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtSearchSupplier = new javax.swing.JTextField();
        btnSearchSupplier = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableMaterialType = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        btnAddMaterialType = new javax.swing.JPanel();
        btnRefreshClassify = new javax.swing.JButton();
        btnThemLoaisp = new javax.swing.JButton();
        btnUpdateMaterialType = new javax.swing.JButton();
        btnDeleteMaterialType = new javax.swing.JButton();
        btnSaveMaterialType = new javax.swing.JButton();
        txtSearchMaterialType = new javax.swing.JTextField();
        btnSearchMaterialType = new javax.swing.JButton();
        jMaLoai = new javax.swing.JLabel();
        txtMaterialTypeID = new javax.swing.JTextField();
        jLoaiSP = new javax.swing.JLabel();
        txtMaterialTypeName = new javax.swing.JTextField();
        txtTrangThai = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnTrangChu = new javax.swing.JButton();
        btnLanguage = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("InformationManagement");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(80, 180, 155));
        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(80, 180, 155));

        tableMaterial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMaterialMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableMaterial);

        txtUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUnitKeyReleased(evt);
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

        jLuongCoBan.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLuongCoBan.setForeground(new java.awt.Color(255, 255, 255));
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

        jChucVu.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jChucVu.setForeground(new java.awt.Color(255, 255, 255));
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

        jMaChucVu.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jMaChucVu.setForeground(new java.awt.Color(255, 255, 255));
        jMaChucVu.setText("Material ID:");

        jPanel17.setBackground(new java.awt.Color(80, 180, 155));

        btnRefresHMaterial.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresHMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefresHMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefresHMaterialMouseClicked(evt);
            }
        });
        btnRefresHMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresHMaterialActionPerformed(evt);
            }
        });

        btnAddMaterial.setBackground(new java.awt.Color(255, 255, 255));
        btnAddMaterial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAddMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add_1.png"))); // NOI18N
        btnAddMaterial.setText("Insert");
        btnAddMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMaterialActionPerformed(evt);
            }
        });

        btnUpdateMaterial.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdateMaterial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUpdateMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Update.png"))); // NOI18N
        btnUpdateMaterial.setText("Update");
        btnUpdateMaterial.setEnabled(false);
        btnUpdateMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateMaterialActionPerformed(evt);
            }
        });

        btnDeleteMaterial.setBackground(new java.awt.Color(255, 255, 255));
        btnDeleteMaterial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDeleteMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete_1.png"))); // NOI18N
        btnDeleteMaterial.setText("Delete");
        btnDeleteMaterial.setEnabled(false);
        btnDeleteMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteMaterialActionPerformed(evt);
            }
        });

        btnSaveMaterial.setBackground(new java.awt.Color(255, 255, 255));
        btnSaveMaterial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSaveMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSaveMaterial.setText("Save");
        btnSaveMaterial.setEnabled(false);
        btnSaveMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveMaterialActionPerformed(evt);
            }
        });

        btnSearchMaterial.setBackground(new java.awt.Color(255, 255, 255));
        btnSearchMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Search_1.png"))); // NOI18N
        btnSearchMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchMaterialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(btnRefresHMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdateMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSearchMaterial))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSaveMaterial, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(btnSearchMaterial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRefresHMaterial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnUpdateMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDeleteMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSaveMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAddMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(btnSearchMaterial)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtSearchMaterial))
                .addContainerGap())
        );

        jMaterial_type_id.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jMaterial_type_id.setForeground(new java.awt.Color(255, 255, 255));
        jMaterial_type_id.setText("Material Type ID:");

        txbMaterialTypeID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                            .addComponent(txtUnit)
                            .addComponent(txtMaterialName, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(txtMaterialID)
                            .addComponent(txbMaterialTypeID, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(107, Short.MAX_VALUE))
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
                        .addComponent(txtMaterialID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jMaChucVu)))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaterialName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jChucVu))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLuongCoBan))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jMaterial_type_id)
                    .addComponent(txbMaterialTypeID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Material", jPanel1);

        jPanel2.setBackground(new java.awt.Color(80, 180, 155));
        jPanel2.setPreferredSize(new java.awt.Dimension(457, 457));

        tableSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSupplierMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableSupplier);

        jPanel8.setBackground(new java.awt.Color(80, 180, 155));

        btnRefreshNSX.setBackground(new java.awt.Color(255, 255, 255));
        btnRefreshNSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshNSX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshNSXMouseClicked(evt);
            }
        });

        btnAddSupplier.setBackground(new java.awt.Color(255, 255, 255));
        btnAddSupplier.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAddSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add_1.png"))); // NOI18N
        btnAddSupplier.setText("Insert");
        btnAddSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSupplierActionPerformed(evt);
            }
        });

        btnUpdateSupplier.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdateSupplier.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUpdateSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Update.png"))); // NOI18N
        btnUpdateSupplier.setText("Update");
        btnUpdateSupplier.setEnabled(false);
        btnUpdateSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateSupplierActionPerformed(evt);
            }
        });

        btnDeleteSupplier.setBackground(new java.awt.Color(255, 255, 255));
        btnDeleteSupplier.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDeleteSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete_1.png"))); // NOI18N
        btnDeleteSupplier.setText("Delete");
        btnDeleteSupplier.setEnabled(false);
        btnDeleteSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSupplierActionPerformed(evt);
            }
        });

        btnSaveSupplier.setBackground(new java.awt.Color(255, 255, 255));
        btnSaveSupplier.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSaveSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSaveSupplier.setText("Save");
        btnSaveSupplier.setEnabled(false);
        btnSaveSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSupplierActionPerformed(evt);
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
                .addComponent(btnAddSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpdateSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSaveSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(441, 441, 441))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnAddSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(btnUpdateSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDeleteSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSaveSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnRefreshNSX, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMaNSX.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jMaNSX.setForeground(new java.awt.Color(255, 255, 255));
        jMaNSX.setText("Material Supplier ID:");

        jNhaSX.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jNhaSX.setForeground(new java.awt.Color(255, 255, 255));
        jNhaSX.setText("Material Supplier Name:");

        jDiaChi.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jDiaChi.setForeground(new java.awt.Color(255, 255, 255));
        jDiaChi.setText("Address:");

        jSDT.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jSDT.setForeground(new java.awt.Color(255, 255, 255));
        jSDT.setText("Phone:");

        txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPhoneKeyReleased(evt);
            }
        });

        jEmail.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jEmail.setForeground(new java.awt.Color(255, 255, 255));
        jEmail.setText("Email:");

        btnSearchSupplier.setBackground(new java.awt.Color(255, 255, 255));
        btnSearchSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Search_1.png"))); // NOI18N
        btnSearchSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchSupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearchSupplier)
                .addGap(18, 18, 18)
                .addComponent(btnSearchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jNhaSX)
                            .addComponent(jMaNSX)
                            .addComponent(jDiaChi))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSupplierID)
                            .addComponent(txtSupplierName)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSDT)
                            .addComponent(jEmail))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtPhone)
                            .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSDT)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jMaNSX))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jNhaSX)
                    .addComponent(txtSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDiaChi)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSearchSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearchSupplier))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Supplier", jPanel2);

        jPanel3.setBackground(new java.awt.Color(80, 180, 155));

        tableMaterialType.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableMaterialType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMaterialTypeMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableMaterialType);

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
            .addGap(0, 394, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        btnAddMaterialType.setBackground(new java.awt.Color(80, 180, 155));

        btnRefreshClassify.setBackground(new java.awt.Color(255, 255, 255));
        btnRefreshClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refest.png"))); // NOI18N
        btnRefreshClassify.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshClassifyMouseClicked(evt);
            }
        });

        btnThemLoaisp.setBackground(new java.awt.Color(255, 255, 255));
        btnThemLoaisp.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnThemLoaisp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add_1.png"))); // NOI18N
        btnThemLoaisp.setText("Update");
        btnThemLoaisp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemLoaispActionPerformed(evt);
            }
        });

        btnUpdateMaterialType.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdateMaterialType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUpdateMaterialType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Update.png"))); // NOI18N
        btnUpdateMaterialType.setText("Update");
        btnUpdateMaterialType.setEnabled(false);
        btnUpdateMaterialType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateMaterialTypeActionPerformed(evt);
            }
        });

        btnDeleteMaterialType.setBackground(new java.awt.Color(255, 255, 255));
        btnDeleteMaterialType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDeleteMaterialType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete_1.png"))); // NOI18N
        btnDeleteMaterialType.setText("Delete");
        btnDeleteMaterialType.setEnabled(false);
        btnDeleteMaterialType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteMaterialTypeActionPerformed(evt);
            }
        });

        btnSaveMaterialType.setBackground(new java.awt.Color(255, 255, 255));
        btnSaveMaterialType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSaveMaterialType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSaveMaterialType.setText("Save");
        btnSaveMaterialType.setEnabled(false);
        btnSaveMaterialType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveMaterialTypeActionPerformed(evt);
            }
        });

        btnSearchMaterialType.setBackground(new java.awt.Color(255, 255, 255));
        btnSearchMaterialType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Search_1.png"))); // NOI18N
        btnSearchMaterialType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchMaterialTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout btnAddMaterialTypeLayout = new javax.swing.GroupLayout(btnAddMaterialType);
        btnAddMaterialType.setLayout(btnAddMaterialTypeLayout);
        btnAddMaterialTypeLayout.setHorizontalGroup(
            btnAddMaterialTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnAddMaterialTypeLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(btnAddMaterialTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(btnAddMaterialTypeLayout.createSequentialGroup()
                        .addComponent(btnRefreshClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThemLoaisp, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdateMaterialType, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteMaterialType, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                    .addComponent(txtSearchMaterialType))
                .addGap(18, 18, 18)
                .addGroup(btnAddMaterialTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSaveMaterialType, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(btnSearchMaterialType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        btnAddMaterialTypeLayout.setVerticalGroup(
            btnAddMaterialTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnAddMaterialTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(btnAddMaterialTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSaveMaterialType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(btnRefreshClassify, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnThemLoaisp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdateMaterialType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDeleteMaterialType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(btnAddMaterialTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSearchMaterialType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearchMaterialType))
                .addContainerGap())
        );

        jMaLoai.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jMaLoai.setForeground(new java.awt.Color(255, 255, 255));
        jMaLoai.setText("Material Type ID:");

        jLoaiSP.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLoaiSP.setForeground(new java.awt.Color(255, 255, 255));
        jLoaiSP.setText("Material Type Name:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jMaLoai)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaterialTypeID, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLoaiSP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaterialTypeName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(486, 486, 486)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddMaterialType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
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
                            .addComponent(jMaLoai)
                            .addComponent(txtMaterialTypeID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaterialTypeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLoaiSP))))
                .addGap(18, 18, 18)
                .addComponent(btnAddMaterialType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Material Type", jPanel3);

        txtTrangThai.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTrangThai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTrangThai.setText("Status");

        jPanel4.setBackground(new java.awt.Color(80, 180, 155));

        btnTrangChu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnTrangChu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Home.png"))); // NOI18N
        btnTrangChu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTrangChuMouseClicked(evt);
            }
        });

        btnLanguage.setBackground(new java.awt.Color(255, 255, 255));
        btnLanguage.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLanguage.setText("English");
        btnLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLanguageActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("INFORMATION MANAGEMENT");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtTrangThai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableMaterialTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMaterialTypeMouseClicked
        int Click = tableMaterialType.getSelectedRow();
        TableModel tbModel = tableMaterialType.getModel();
        txtMaterialTypeID.setText(tbModel.getValueAt(Click, 0).toString());
        txtMaterialTypeName.setText(tbModel.getValueAt(Click, 1).toString());
        btnUpdateMaterialType.setEnabled(true);
        btnDeleteMaterialType.setEnabled(true);
    }//GEN-LAST:event_tableMaterialTypeMouseClicked

    private void btnTrangChuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTrangChuMouseClicked
        backHome();
    }//GEN-LAST:event_btnTrangChuMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int Click = JOptionPane.showConfirmDialog(null, "Do you want to close the application??", "Notification", 2);
        if (Click == JOptionPane.OK_OPTION) {
            System.exit(0);
        } else {
            if (Click == JOptionPane.CANCEL_OPTION) {
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnRefreshClassifyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshClassifyMouseClicked
        refresh();
    }//GEN-LAST:event_btnRefreshClassifyMouseClicked

    private void btnThemLoaispActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemLoaispActionPerformed
        refresh();
        Add = true;
        btnThemLoaisp.setEnabled(false);
        btnSaveMaterialType.setEnabled(true);
        enabledMaterialType();
    }//GEN-LAST:event_btnThemLoaispActionPerformed

    private void btnUpdateMaterialTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateMaterialTypeActionPerformed
        Add = false;
        Change = true;
        btnThemLoaisp.setEnabled(false);
        btnUpdateMaterialType.setEnabled(false);
        btnDeleteMaterialType.setEnabled(false);
        btnSaveMaterialType.setEnabled(true);
        enabledMaterialType();
    }//GEN-LAST:event_btnUpdateMaterialTypeActionPerformed

    private void btnDeleteMaterialTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteMaterialTypeActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Do you want to remove the Material Type??", "Notification", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM MaterialType WHERE Material_TypeID=? AND Material_Type_Name=?";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, txtMaterialTypeID.getText());
                pst.setString(2, txtMaterialTypeName.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Deleted Successfully!");
                disabledMateriaType();
                refresh();
                displayMaterialType(sqlMaterialType);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteMaterialTypeActionPerformed

    private void btnSaveMaterialTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveMaterialTypeActionPerformed
        if (Add == true) {
            if (checkMaterialType()) {
                insertMaterialType();
            } else {
                txtTrangThai.setText("Material Type ID already exists!");
            }
        } else {
            if (Change == true) {
                updateMaterialType();
            }
        }
    }//GEN-LAST:event_btnSaveMaterialTypeActionPerformed

    private void txtPhoneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneKeyReleased
        txtPhone.setText(cutChar(txtPhone.getText()));
    }//GEN-LAST:event_txtPhoneKeyReleased

    private void btnSaveSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSupplierActionPerformed
        if (Add == true) {
            if (checkSupplier()) {
                insertSupplier();
            } else {
                txtTrangThai.setText("Material Supplier ID already exists!");
            }
        } else {
            if (Change == true) {
                updateSupplier();
            }
        }
    }//GEN-LAST:event_btnSaveSupplierActionPerformed

    private void btnDeleteSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSupplierActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Do you want to remove Material Supplier??", "Notification", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM MaterialSupplier WHERE Material_SupplierID=? AND  Material_Supplier_Name=? AND Address=? AND Phone=? AND Email=?";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, txtSupplierID.getText());
                pst.setString(2, txtSupplierName.getText());
                pst.setString(3, txtAddress.getText());
                pst.setString(4, txtPhone.getText());
                pst.setString(5, txtEmail.getText());
                pst.executeUpdate();
                txtTrangThai.setText("Deleted Successfully!");
                disabledSupplier();
                refresh();
                displaySupplier(sqlSupplier);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteSupplierActionPerformed

    private void btnUpdateSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSupplierActionPerformed
        Add = false;
        Change = true;
        btnAddSupplier.setEnabled(false);
        btnUpdateSupplier.setEnabled(false);
        btnDeleteSupplier.setEnabled(false);
        btnSaveSupplier.setEnabled(true);
        enabledSupplier();
    }//GEN-LAST:event_btnUpdateSupplierActionPerformed

    private void btnAddSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSupplierActionPerformed
        refresh();
        Add = true;
        btnAddSupplier.setEnabled(false);
        btnSaveSupplier.setEnabled(true);
        enabledSupplier();
    }//GEN-LAST:event_btnAddSupplierActionPerformed

    private void btnRefreshNSXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshNSXMouseClicked
        refresh();
        displaySupplier(sqlSupplier);
    }//GEN-LAST:event_btnRefreshNSXMouseClicked

    private void tableSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSupplierMouseClicked
        int Click = tableSupplier.getSelectedRow();
        TableModel tbModel = tableSupplier.getModel();
        txtSupplierID.setText(tbModel.getValueAt(Click, 0).toString());
        txtSupplierName.setText(tbModel.getValueAt(Click, 1).toString());
        txtAddress.setText(tbModel.getValueAt(Click, 2).toString());
        txtPhone.setText(tbModel.getValueAt(Click, 3).toString());
        txtEmail.setText(tbModel.getValueAt(Click, 4).toString());
        btnUpdateSupplier.setEnabled(true);
        btnDeleteSupplier.setEnabled(true);
    }//GEN-LAST:event_tableSupplierMouseClicked

    private void btnSaveMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveMaterialActionPerformed
        if (Add == true) {
            if (checkMaterial()) {
                insertMaterial();
            } else {
                txtTrangThai.setText("Material ID already exists!");
            }
        } else {
            if (Change == true) {
                updateMaterial();
            }
        }
    }//GEN-LAST:event_btnSaveMaterialActionPerformed

    private void btnDeleteMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteMaterialActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Do you want to delete the Material?", "Notification", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM Position WHERE PositionID=? AND Position=? AND Payroll=?";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, txtMaterialID.getText());
                pst.setString(2, txtMaterialName.getText());
                pst.setString(3, txtUnit.getText() + " " + "VND");
                pst.executeUpdate();
                txtTrangThai.setText("Deleted Successfully!");
                disabledMaterial();
                refresh();
                displayMaterial(sqlMaterial);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteMaterialActionPerformed

    private void btnUpdateMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateMaterialActionPerformed
        Add = false;
        Change = true;
        btnAddMaterial.setEnabled(false);
        btnUpdateMaterial.setEnabled(false);
        btnDeleteMaterial.setEnabled(false);
        btnSaveMaterial.setEnabled(true);
        enabledMaterial();
    }//GEN-LAST:event_btnUpdateMaterialActionPerformed

    private void btnAddMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMaterialActionPerformed
        refresh();
        Add = true;
        btnAddMaterial.setEnabled(false);
        btnSaveMaterial.setEnabled(true);
        enabledMaterial();
    }//GEN-LAST:event_btnAddMaterialActionPerformed

    private void btnRefresHMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefresHMaterialActionPerformed
    }//GEN-LAST:event_btnRefresHMaterialActionPerformed

    private void btnRefresHMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefresHMaterialMouseClicked
        refresh();
    }//GEN-LAST:event_btnRefresHMaterialMouseClicked

    private void txtUnitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitKeyReleased
        /*  DecimalFormat formatter = new DecimalFormat("###,###,###");
        txtUnit.setText(cutChar(txtUnit.getText()));
        if (txtUnit.getText().equals("")) {
            return;
        } else {
            txtUnit.setText(formatter.format(convertedToNumbers(txtUnit.getText())));
        }*/
    }//GEN-LAST:event_txtUnitKeyReleased

    private void tableMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMaterialMouseClicked
        int Click = tableMaterial.getSelectedRow();
        TableModel tbModel = tableMaterial.getModel();
        txtMaterialID.setText(tbModel.getValueAt(Click, 0).toString());
        txtMaterialName.setText(tbModel.getValueAt(Click, 1).toString());
        txtUnit.setText(tbModel.getValueAt(Click, 2).toString());
        txbMaterialTypeID.addItem(tbModel.getValueAt(Click, 3).toString());
        btnUpdateMaterial.setEnabled(true);
        btnDeleteMaterial.setEnabled(true);
    }//GEN-LAST:event_tableMaterialMouseClicked

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
        btnAddMaterial.setText(language.getString("btnthemchucvu"));
        btnUpdateMaterial.setText(language.getString("btnsuachucvu"));
        btnDeleteMaterial.setText(language.getString("btnxoachucvu"));
        btnSaveMaterial.setText(language.getString("btnluuchucvu"));
        jMaNSX.setText(language.getString("mansx"));
        jNhaSX.setText(language.getString("nhasx"));
        jDiaChi.setText(language.getString("diachi"));
        jSDT.setText(language.getString("sdt"));
        jEmail.setText(language.getString("email"));
        btnAddSupplier.setText(language.getString("btnthemnsx"));
        btnUpdateSupplier.setText(language.getString("btnsuansx"));
        btnDeleteSupplier.setText(language.getString("btnxoansx"));
        btnSaveSupplier.setText(language.getString("btnluunsx"));
        jMaLoai.setText(language.getString("maloai"));
        jLoaiSP.setText(language.getString("loaisp"));
        btnThemLoaisp.setText(language.getString("btnthemloaisp"));
        btnUpdateMaterialType.setText(language.getString("btnsualoaisp"));
        btnDeleteMaterialType.setText(language.getString("btnxoaloaisp"));
        btnSaveMaterialType.setText(language.getString("btnluuloaisp"));
        txtTrangThai.setText(language.getString("trangthai"));


    }//GEN-LAST:event_btnLanguageActionPerformed

    private void btnSearchMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchMaterialActionPerformed

        String sql = "SELECT * FROM Material WHERE MaterialID LIKE N'%"
                + this.txtSearchMaterial.getText()
                + "%' or MaterialName like N'%"
                + this.txtSearchMaterial.getText()
                + "%' or Unit like N'%"
                + this.txtSearchMaterial.getText()
                + "%' or Material_TypeID like N'%"
                + this.txtSearchMaterial.getText() + "%'";

        displayMaterial(sql);
        txtSearchMaterial.setText("");
    }//GEN-LAST:event_btnSearchMaterialActionPerformed

    private void btnSearchSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchSupplierActionPerformed

        String sql = "SELECT * FROM MaterialSupplier WHERE Material_SupplierID LIKE N'%"
                + this.txtSearchSupplier.getText()
                + "%' or Material_Supplier_Name like N'%"
                + this.txtSearchSupplier.getText()
                + "%' or Address like N'%"
                + this.txtSearchSupplier.getText()
                + "%' or Phone like N'%"
                + this.txtSearchSupplier.getText()
                + "%' or Email like N'%"
                + this.txtSearchSupplier.getText() + "%'";

        displaySupplier(sql);
        txtSearchSupplier.setText("");

    }//GEN-LAST:event_btnSearchSupplierActionPerformed

    private void btnSearchMaterialTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchMaterialTypeActionPerformed

        String sql = "SELECT * FROM MaterialType WHERE Material_TypeID LIKE N'%"
                + this.txtSearchMaterialType.getText()
                + "%' or Material_Type_Name like N'%"
                + this.txtSearchMaterialType.getText() + "%'";

        displayMaterialType(sql);
        txtSearchMaterialType.setText("");
    }//GEN-LAST:event_btnSearchMaterialTypeActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InformationManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InformationManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InformationManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InformationManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail = new Detail();
                new InformationManagement(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddMaterial;
    private javax.swing.JPanel btnAddMaterialType;
    private javax.swing.JButton btnAddSupplier;
    private javax.swing.JButton btnDeleteMaterial;
    private javax.swing.JButton btnDeleteMaterialType;
    private javax.swing.JButton btnDeleteSupplier;
    private javax.swing.JToggleButton btnLanguage;
    private javax.swing.JButton btnRefresHMaterial;
    private javax.swing.JButton btnRefreshClassify;
    private javax.swing.JButton btnRefreshNSX;
    private javax.swing.JButton btnSaveMaterial;
    private javax.swing.JButton btnSaveMaterialType;
    private javax.swing.JButton btnSaveSupplier;
    private javax.swing.JButton btnSearchMaterial;
    private javax.swing.JButton btnSearchMaterialType;
    private javax.swing.JButton btnSearchSupplier;
    private javax.swing.JButton btnThemLoaisp;
    private javax.swing.JButton btnTrangChu;
    private javax.swing.JButton btnUpdateMaterial;
    private javax.swing.JButton btnUpdateMaterialType;
    private javax.swing.JButton btnUpdateSupplier;
    private javax.swing.JLabel jChucVu;
    private javax.swing.JLabel jDiaChi;
    private javax.swing.JLabel jEmail;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLoaiSP;
    private javax.swing.JLabel jLuongCoBan;
    private javax.swing.JLabel jMaChucVu;
    private javax.swing.JLabel jMaLoai;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jSDT;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tableMaterial;
    private javax.swing.JTable tableMaterialType;
    private javax.swing.JTable tableSupplier;
    private javax.swing.JComboBox<String> txbMaterialTypeID;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMaterialID;
    private javax.swing.JTextField txtMaterialName;
    private javax.swing.JTextField txtMaterialTypeID;
    private javax.swing.JTextField txtMaterialTypeName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtSearchMaterial;
    private javax.swing.JTextField txtSearchMaterialType;
    private javax.swing.JTextField txtSearchSupplier;
    private javax.swing.JTextField txtSupplierID;
    private javax.swing.JTextField txtSupplierName;
    private javax.swing.JLabel txtTrangThai;
    private javax.swing.JTextField txtUnit;
    // End of variables declaration//GEN-END:variables
}
