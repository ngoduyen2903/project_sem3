package UserInterFace;

import java.awt.Color;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

public class Requestion extends javax.swing.JFrame {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    private boolean Add = false, Change = false;
    private String sql = "SELECT * FROM MaterialRequestion";
    private String date = "01/10/2021";

    private Detail detail;

    public Requestion(Detail d) {
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        connectionSQLServer();
        detail = new Detail(d);
        loadRequestion(sql);
        disabled();
        loadPaymentStatus();
        txtStatus.setForeground(Color.red);
        loadMaterial();
        loadMaterialSupplier();
    }

    private void connectionSQLServer() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;"
                    + "databaseName=MilkTeaShopManagement;user=sa;password=123456", "sa", "123456");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadMaterial() {
        txbMaterialID.removeAllItems();
        String sqlcbxPosition = "SELECT * FROM Material";
        try {
            pst = conn.prepareStatement(sqlcbxPosition);
            rs = pst.executeQuery();
            while (rs.next()) {
                this.txbMaterialID.addItem(rs.getString("MaterialID").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMaterialSupplier() {
        txbSupplierID.removeAllItems();
        String sqlcbxPosition = "SELECT * FROM MaterialSupplier";
        try {
            pst = conn.prepareStatement(sqlcbxPosition);
            rs = pst.executeQuery();
            while (rs.next()) {
                this.txbSupplierID.addItem(rs.getString("Material_SupplierID").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enabled() {
        txtRequestionID.setEnabled(true);
        txbMaterialID.setEnabled(true);
        txbSupplierID.setEnabled(true);
        txtImporter.setEnabled(true);
        txtDate.setEnabled(true);
        txtQuantity.setEnabled(true);
        txtInputPrice.setEnabled(true);
        txtTotalMoney.setEnabled(true);
        txtPaymentStatus.setEnabled(true);
        txtStatus.setText("Status");
    }

    private void disabled() {
        txtRequestionID.setEnabled(false);
        txbMaterialID.setEnabled(false);
        txbSupplierID.setEnabled(false);
        txtImporter.setEnabled(false);
        txtDate.setEnabled(false);
        txtTotalMoney.setEnabled(false);
        txtPaymentStatus.setEnabled(false);
        txtQuantity.setEnabled(false);
        txtInputPrice.setEnabled(false);
    }

    private void refresh() {
        Add = false;
        Change = false;
        txtRequestionID.setText("");
        txtImporter.setText("");
        // ((JTextField) jDayChooser1.getDate().getUiComponent()).setText("");
        txtTotalMoney.setText("");
        txtQuantity.setText("");
        txtInputPrice.setText("");
        btnCreateRequestion.setEnabled(true);
        btnUpdateRequestion.setEnabled(false);
        btnDeleteRequestion.setEnabled(false);
        btnSaveRequestion.setEnabled(false);
        loadPaymentStatus();
        txtStatus.setText("Status");
    }

    private void loadPaymentStatus() {
        txtPaymentStatus.removeAllItems();
        txtPaymentStatus.addItem("Paid");
        txtPaymentStatus.addItem("Unpaid");
    }

    private void loadRequestion(String sql) {
        try {
            String[] arr = {"Material Requestion ID", "Material ID", "Material Supplier ID", "Importer", "Import Date", "Quantity", "Input Price", "Total Money", "Payment Status"};
            DefaultTableModel modle = new DefaultTableModel(arr, 0);
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("MaterialRequestionID").trim());
                vector.add(rs.getString("MaterialID").trim());
                vector.add(rs.getString("MaterialSupplierID").trim());
                vector.add(rs.getString("Importer").trim());
                vector.add(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("ImportDate")));
                vector.add(rs.getString("Quantity").trim());
                vector.add(rs.getString("InputPrice").trim());
                vector.add(rs.getString("TotalMoney").trim());
                vector.add(rs.getString("PaymentStatus").trim());
                modle.addRow(vector);
            }
            tableRequestion.setModel(modle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkNull() {
        boolean kq = true;
        if (String.valueOf(this.txtRequestionID.getText()).length() == 0) {
            txtStatus.setText("Material Requestion ID cannot be blank. Pls re-enter!");
            return false;
        }
        if (String.valueOf(this.txtImporter.getText()).length() == 0) {
            txtStatus.setText("Importer cannot be blank. Pls re-enter!!");
            return false;
        }
        /*   if (String.valueOf(((JTextField) this.txtDate.getDateEditor().getUiComponent()).getText()).length() == 0) {
            txtStatus.setText("Import Date cannot be blank. Pls re-enter!");
            return false;
        }*/
        if (String.valueOf(this.txtDate.getAccessibleContext()).length() == 0) {
            txtStatus.setText("Quantity cannot be blank. Pls re-enter!!");
            return false;
        }
        if (String.valueOf(this.txtQuantity.getAccessibleContext()).length() == 0) {
            txtStatus.setText("Quantity cannot be blank. Pls re-enter!!");
            return false;
        }
        if (String.valueOf(this.txtInputPrice.getText()).length() == 0) {
            txtStatus.setText("Input Price cannot be blank. Pls re-enter!!");
            return false;
        }
        if (String.valueOf(this.txtTotalMoney.getText()).length() == 0) {
            txtStatus.setText("Total Money cannot be blank. Pls re-enter!!");
            return false;
        }

        return kq;
    }

    private void addRequestion() {
        if (checkNull()) {
            String sqlInsert = "INSERT INTO [dbo].[MaterialRequestion] ([MaterialRequestionID],[MaterialID] ,[MaterialSupplierID],[Importer] ,[ImportDate],[Quantity],[InputPrice], [TotalMoney],[PaymentStatus] )\n"
                    + "VALUES (?,?,?,?,?,?,?,?,?)";
            try {
                pst = conn.prepareStatement(sqlInsert);
                pst.setString(1, this.txtRequestionID.getText());
                pst.setString(2, (String) txbMaterialID.getSelectedItem());
                pst.setString(3, (String) txbSupplierID.getSelectedItem());
                pst.setString(4, (txtImporter.getText()));
                //  pst.setString(5, (txtDate.getText()));
                pst.setDate(5, new java.sql.Date(this.txtDate.getDate().getTime()));
                pst.setString(6, (txtQuantity.getText()));
                pst.setString(7, txtInputPrice.getText() + " " + "VND");
                pst.setString(8, txtTotalMoney.getText());
                pst.setString(9, (String) txtPaymentStatus.getSelectedItem());

                pst.executeUpdate();
                refresh();
                txtStatus.setText("Added Success!");
                disabled();
                loadRequestion(sql);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateRequestion() {
        int Click = tableRequestion.getSelectedRow();
        TableModel model = tableRequestion.getModel();

        if (checkNull()) {
            String sqlChange = "UPDATE [dbo].[MaterialRequestion] SET [MaterialRequestionID]=?, [MaterialID]=?, [MaterialSupplierID]=?, [Importer]=?, [ImportDate]=?, [Quantity]=?, [InputPrice]=?, [TotalMoney]=?, [PaymentStatus]=? \n"
                    + "WHERE [MaterialRequestionID]='"
                    + model.getValueAt(Click, 1).toString().trim() + "'";;
            try {
                pst = conn.prepareStatement(sqlChange);
                pst.setString(1, this.txtRequestionID.getText());
                pst.setString(2, (String) txbMaterialID.getSelectedItem());
                pst.setString(3, (String) txbSupplierID.getSelectedItem());
                pst.setString(4, this.txtImporter.getText());
                //   pst.setString(5, this.txtDate.getText());
                // pst.setDate(5,new java.sql.Date(this.txtDate.getDate().getTime()));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String date = sdf.format(txtDate.getDate());
                pst.setString(5, date);
                pst.setString(6, (String) this.txtQuantity.getText());
                pst.setString(7, this.txtInputPrice.getText() + " " + "VND");
                pst.setString(8, this.txtTotalMoney.getText());
                pst.setString(9, (String) txtPaymentStatus.getSelectedItem());
                pst.executeUpdate();
                txtStatus.setText("Save Changes Successfully!");
                disabled();
                refresh();
                loadRequestion(sql);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean check() {
        boolean kq = true;
        String sqlCheck = "SELECT * FROM MaterialRequestion";
        try {
            pst = conn.prepareStatement(sqlCheck);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (this.txtRequestionID.getText().equals(rs.getString("MaterialRequestionID").toString().trim())) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableRequestion = new javax.swing.JTable();
        txtStatus = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jRequestion = new javax.swing.JLabel();
        jDate = new javax.swing.JLabel();
        jMaterialID = new javax.swing.JLabel();
        jQuantity = new javax.swing.JLabel();
        jSupplierID = new javax.swing.JLabel();
        jInputPrice = new javax.swing.JLabel();
        txtInputPrice = new javax.swing.JTextField();
        jPaymentstatus = new javax.swing.JLabel();
        jImporter = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jTotalMoney = new javax.swing.JLabel();
        txtTotalMoney = new javax.swing.JTextField();
        txtRequestionID = new javax.swing.JTextField();
        txbMaterialID = new javax.swing.JComboBox<>();
        txbSupplierID = new javax.swing.JComboBox<>();
        txtImporter = new javax.swing.JTextField();
        txtPaymentStatus = new javax.swing.JComboBox<>();
        txtDate = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnCreateRequestion = new javax.swing.JButton();
        btnUpdateRequestion = new javax.swing.JButton();
        btnDeleteRequestion = new javax.swing.JButton();
        btnSaveRequestion = new javax.swing.JButton();
        btnSearchRequestion = new javax.swing.JButton();
        txtSearchRequestion = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnHomeBack = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTitle = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EmployeeManagement");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tableRequestion.setBackground(new java.awt.Color(80, 180, 155));
        tableRequestion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tableRequestion.setForeground(new java.awt.Color(255, 255, 255));
        tableRequestion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableRequestion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRequestionMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableRequestion);

        txtStatus.setBackground(new java.awt.Color(82, 180, 180));
        txtStatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtStatus.setText("Status");

        jPanel1.setBackground(new java.awt.Color(82, 180, 155));

        jRequestion.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jRequestion.setForeground(new java.awt.Color(255, 255, 255));
        jRequestion.setText("Material Requesion ID:");

        jDate.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jDate.setForeground(new java.awt.Color(255, 255, 255));
        jDate.setText("Import Date:");

        jMaterialID.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jMaterialID.setForeground(new java.awt.Color(255, 255, 255));
        jMaterialID.setText("Material ID:");

        jQuantity.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jQuantity.setForeground(new java.awt.Color(255, 255, 255));
        jQuantity.setText("Quantity:");

        jSupplierID.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jSupplierID.setForeground(new java.awt.Color(255, 255, 255));
        jSupplierID.setText("Material Supplier ID:");

        jInputPrice.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jInputPrice.setForeground(new java.awt.Color(255, 255, 255));
        jInputPrice.setText("Input Price:");

        txtInputPrice.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jPaymentstatus.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jPaymentstatus.setForeground(new java.awt.Color(255, 255, 255));
        jPaymentstatus.setText("Payment Status:");

        jImporter.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jImporter.setForeground(new java.awt.Color(255, 255, 255));
        jImporter.setText("Importer:");

        txtQuantity.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQuantityKeyReleased(evt);
            }
        });

        jTotalMoney.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jTotalMoney.setForeground(new java.awt.Color(255, 255, 255));
        jTotalMoney.setText("Total Money:");

        txtTotalMoney.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTotalMoney.setEnabled(false);

        txtRequestionID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtRequestionID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRequestionIDKeyReleased(evt);
            }
        });

        txbMaterialID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txbSupplierID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtPaymentStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRequestion)
                    .addComponent(jMaterialID, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSupplierID, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jImporter, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDate, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtRequestionID)
                    .addComponent(txbMaterialID, 0, 291, Short.MAX_VALUE)
                    .addComponent(txbSupplierID, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtImporter)
                    .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jQuantity)
                    .addComponent(jTotalMoney)
                    .addComponent(jInputPrice)
                    .addComponent(jPaymentstatus))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTotalMoney)
                    .addComponent(txtInputPrice)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                    .addComponent(txtPaymentStatus, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRequestion)
                            .addComponent(txtRequestionID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jMaterialID)
                            .addComponent(txbMaterialID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSupplierID)
                            .addComponent(txbSupplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jImporter)
                            .addComponent(txtImporter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jQuantity)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtInputPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jInputPrice))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTotalMoney))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jPaymentstatus)
                            .addComponent(txtPaymentStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDate)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        jPanel2.setBackground(new java.awt.Color(82, 180, 155));

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refresh-icon.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnCreateRequestion.setBackground(new java.awt.Color(255, 255, 255));
        btnCreateRequestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add.png"))); // NOI18N
        btnCreateRequestion.setText("Insert");
        btnCreateRequestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateRequestionActionPerformed(evt);
            }
        });

        btnUpdateRequestion.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdateRequestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Change.png"))); // NOI18N
        btnUpdateRequestion.setText("Update");
        btnUpdateRequestion.setEnabled(false);
        btnUpdateRequestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateRequestionActionPerformed(evt);
            }
        });

        btnDeleteRequestion.setBackground(new java.awt.Color(255, 255, 255));
        btnDeleteRequestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btnDeleteRequestion.setText("Delete");
        btnDeleteRequestion.setEnabled(false);
        btnDeleteRequestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteRequestionActionPerformed(evt);
            }
        });

        btnSaveRequestion.setBackground(new java.awt.Color(255, 255, 255));
        btnSaveRequestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSaveRequestion.setText("Save");
        btnSaveRequestion.setEnabled(false);
        btnSaveRequestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveRequestionActionPerformed(evt);
            }
        });

        btnSearchRequestion.setBackground(new java.awt.Color(255, 255, 255));
        btnSearchRequestion.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnSearchRequestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Find.png"))); // NOI18N
        btnSearchRequestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchRequestionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCreateRequestion)
                .addGap(18, 18, 18)
                .addComponent(btnUpdateRequestion)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteRequestion, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSaveRequestion, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearchRequestion, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchRequestion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdateRequestion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCreateRequestion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDeleteRequestion))
                        .addGap(1, 1, 1))
                    .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearchRequestion)
                    .addComponent(btnSaveRequestion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSearchRequestion)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(80, 180, 155));

        btnHomeBack.setBackground(new java.awt.Color(255, 255, 255));
        btnHomeBack.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnHomeBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Home.png"))); // NOI18N
        btnHomeBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeBackMouseClicked(evt);
            }
        });

        jButton1.setText("English");

        jTitle.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jTitle.setForeground(new java.awt.Color(255, 255, 255));
        jTitle.setText("MATERIAL REQUESTION");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHomeBack, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
                .addComponent(jTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(161, 161, 161)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnHomeBack, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(txtStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeBackMouseClicked
        HomeAdmin home = new HomeAdmin(detail);
        this.setVisible(false);
        home.setVisible(true);
    }//GEN-LAST:event_btnHomeBackMouseClicked

    private void tableRequestionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRequestionMouseClicked
        txbMaterialID.removeAllItems();
        txbSupplierID.removeAllItems();
        txtPaymentStatus.removeAllItems();
        int Click = tableRequestion.getSelectedRow();
        TableModel model = tableRequestion.getModel();
        txtRequestionID.setText(model.getValueAt(Click, 0).toString());
        txbMaterialID.addItem(model.getValueAt(Click, 1).toString());
        txbSupplierID.addItem(model.getValueAt(Click, 2).toString());
        txtImporter.setText(model.getValueAt(Click, 3).toString());
        // txtDate.setText(model.getValueAt(Click, 4).toString());
        ((JTextField) txtDate.getDateEditor().getUiComponent()).setText(model.getValueAt(Click, 4).toString());
        txtQuantity.setText(model.getValueAt(Click, 5).toString());
        String[] s = model.getValueAt(Click, 6).toString().split("\\s");
        txtInputPrice.setText(s[0]);
        //  txtTotalMoney.setText(model.getValueAt(Click, 7).toString());
        String[] s1 = model.getValueAt(Click, 7).toString().split("\\s");
        txtTotalMoney.setText(s1[0]);
        txtPaymentStatus.addItem(model.getValueAt(Click, 8).toString());
        //  txtPaymentStatus.setText(model.getValueAt(Click, 3).toString());
        btnCreateRequestion.setEnabled(false);
        btnDeleteRequestion.setEnabled(true);
        btnUpdateRequestion.setEnabled(true);

    }//GEN-LAST:event_tableRequestionMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int lick = JOptionPane.showConfirmDialog(null, "Do you want to close the application or not??", "Notification", 2);
        if (lick == JOptionPane.OK_OPTION) {
            System.exit(0);
        } else {
            if (lick == JOptionPane.CANCEL_OPTION) {
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnSaveRequestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveRequestionActionPerformed
        if (Add == true) {
            if (check()) {
                addRequestion();
            } else {
                txtStatus.setText("Material Requestion ID already exists!");
            }
        } else if (Change == true) {
            updateRequestion();
        }
    }//GEN-LAST:event_btnSaveRequestionActionPerformed

    private void btnDeleteRequestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteRequestionActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Do you want to delete or not??", "Notification", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM MaterialRequestion WHERE MaterialRequestionID=?";
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, String.valueOf(this.txtRequestionID.getText()));
                pst.executeUpdate();
                disabled();
                refresh();
                txtStatus.setText("Deleted Successfully!!");
                loadRequestion(sql);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteRequestionActionPerformed

    private void btnUpdateRequestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateRequestionActionPerformed
        Add = false;
        Change = true;
        btnCreateRequestion.setEnabled(false);
        btnUpdateRequestion.setEnabled(false);
        btnDeleteRequestion.setEnabled(false);
        btnSaveRequestion.setEnabled(true);
        //loadPaymentStatus();
        enabled();

    }//GEN-LAST:event_btnUpdateRequestionActionPerformed

    private void btnCreateRequestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateRequestionActionPerformed
        refresh();
        Add = true;
        btnCreateRequestion.setEnabled(false);
        btnSaveRequestion.setEnabled(true);
        loadPaymentStatus();
        enabled();
        //  loadSupplier();
    }//GEN-LAST:event_btnCreateRequestionActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadPaymentStatus();
        refresh();
        disabled();
        //LoadPosition();
        loadRequestion(sql);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void txtRequestionIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRequestionIDKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRequestionIDKeyReleased

    private void txtQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantityKeyReleased
        /* String sql = "SELECT * FROM Position where Position=?";
        txtPhone.setText(cutChar(txtPhone.getText()));
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, this.cbxPosition.getSelectedItem().toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (txtPhone.getText().equals("")) {
                    txtUsername.setText("0");
                } else {
                    if (txtPhone.getText().equals("0")) {
                        int Level = Integer.parseInt(txtPhone.getText());

                        String[] s = rs.getString("Payroll").trim().split("\\s");

                        txtUsername.setText(formatter.format((int) (convertedToNumbers(s[0]) / 2)));
                    } else {
                        int Level = Integer.parseInt(txtPhone.getText().toString());
                        String[] s = rs.getString("Payroll").trim().split("\\s");

                        txtUsername.setText(formatter.format(Level * convertedToNumbers(s[0])));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }//GEN-LAST:event_txtQuantityKeyReleased

    private void btnSearchRequestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchRequestionActionPerformed
        String sql = "SELECT * FROM MaterialRequestion WHERE MaterialRequestionID LIKE N'%"
                + this.txtSearchRequestion.getText() + "%'";
        loadRequestion(sql);
        txtSearchRequestion.setText("");
    }//GEN-LAST:event_btnSearchRequestionActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Requestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Requestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Requestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Requestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail = new Detail();
                new Requestion(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateRequestion;
    private javax.swing.JButton btnDeleteRequestion;
    private javax.swing.JButton btnHomeBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSaveRequestion;
    private javax.swing.JButton btnSearchRequestion;
    private javax.swing.JButton btnUpdateRequestion;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jDate;
    private javax.swing.JLabel jImporter;
    private javax.swing.JLabel jInputPrice;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jMaterialID;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel jPaymentstatus;
    private javax.swing.JLabel jQuantity;
    private javax.swing.JLabel jRequestion;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jSupplierID;
    private javax.swing.JLabel jTitle;
    private javax.swing.JLabel jTotalMoney;
    private javax.swing.JTable tableRequestion;
    private javax.swing.JComboBox<String> txbMaterialID;
    private javax.swing.JComboBox<String> txbSupplierID;
    private com.toedter.calendar.JDateChooser txtDate;
    private javax.swing.JTextField txtImporter;
    private javax.swing.JTextField txtInputPrice;
    private javax.swing.JComboBox<String> txtPaymentStatus;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtRequestionID;
    private javax.swing.JTextField txtSearchRequestion;
    private javax.swing.JLabel txtStatus;
    private javax.swing.JTextField txtTotalMoney;
    // End of variables declaration//GEN-END:variables
}
