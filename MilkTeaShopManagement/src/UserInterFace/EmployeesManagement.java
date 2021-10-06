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

public class EmployeesManagement extends javax.swing.JFrame {

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    private boolean Add = false, Change = false;
    private String sql = "SELECT * FROM Employee";

    private Detail detail;

    public EmployeesManagement(Detail d) {
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        connectionSQLServer();
        detail = new Detail(d);
        load(sql);
        disabled();
        // LoadPosition();
        txtStatus.setForeground(Color.red);
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

    private void enabled() {
        txtEmployeeCode.setEnabled(true);
        txtFullname.setEnabled(true);
        txtEmail.setEnabled(true);
        txtFullname.setEnabled(true);
        txtBirthday.setEnabled(true);
        txtGender.setEnabled(true);
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);
        txtPhone.setEnabled(true);
        txtAddress.setEnabled(true);
        txtWorkStartDate.setEnabled(true);
        txtPayroll.setEnabled(true);
        // btn.setEnabled(true);
        txtStatus.setText("Status!");
    }

    private void disabled() {
        txtEmployeeCode.setEnabled(false);
        txtFullname.setEnabled(false);
        txtEmail.setEnabled(false);
        txtFullname.setEnabled(false);
        txtBirthday.setEnabled(false);
        txtGender.setEnabled(false);
        txtUsername.setEnabled(false);
        txtPassword.setEnabled(false);
        txtPhone.setEnabled(false);
        txtAddress.setEnabled(false);
        txtWorkStartDate.setEnabled(false);
        txtPayroll.setEnabled(false);
        //btn.setEnabled(false);
    }

    private void refresh() {
        Add = false;
        Change = false;
        txtEmployeeCode.setText("");
        txtFullname.setText("");

        txtEmail.setText("");
        ((JTextField) txtBirthday.getDateEditor().getUiComponent()).setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        ((JTextField) txtWorkStartDate.getDateEditor().getUiComponent()).setText("");
        txtPayroll.setText("");
        btnCreate.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        // LoadPosition();
        loadGender();
        txtStatus.setText("Status");
    }

    private void loadGender() {
        txtGender.removeAllItems();
        txtGender.addItem("Male");
        txtGender.addItem("Female");
        txtGender.addItem("Other");
    }

    private void load(String sql) {
        try {
            String[] arr = {"ID", "Fullname", "Birthday", "Gender", "Email", "Phone", "Address", "Username", "Work Start Date", "Payroll"};
            DefaultTableModel modle = new DefaultTableModel(arr, 0);
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("EmployeeID").trim());
                vector.add(rs.getString("EmployeeName").trim());
                vector.add(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("Birthday")));
                vector.add(rs.getString("Gender").trim());
                vector.add(rs.getString("Email"));
                vector.add(rs.getString("Phone").trim());
                vector.add(rs.getString("Address").trim());
                vector.add(rs.getString("Username").trim());
               // vector.add(rs.getString("Password").trim());
                vector.add(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("Work_start_date")));
                vector.add(rs.getString("Payroll").trim());
                modle.addRow(vector);
            }
            TableEmployee.setModel(modle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*private void LoadPosition() {
        cbxPosition.removeAllItems();
        String sqlcbxPosition = "SELECT * FROM Position";
        try {
            pst = conn.prepareStatement(sqlcbxPosition);
            rs = pst.executeQuery();
            while (rs.next()) {
                this.cbxPosition.addItem(rs.getString("Position").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    private boolean checkNull() {
        boolean kq = true;
        if (String.valueOf(this.txtEmployeeCode.getText()).length() == 0) {
            txtStatus.setText("ID cannot be blank. Pls re-enter!");
            return false;
        }
        if (String.valueOf(this.txtFullname.getText()).length() == 0) {
            txtStatus.setText("Fullname cannot be blank. Pls re-enter!!");
            return false;
        }
        if (String.valueOf(((JTextField) this.txtBirthday.getDateEditor().getUiComponent()).getText()).length() == 0) {
            txtStatus.setText("Birthday cannot be blank. Pls re-enter!");
            return false;
        }
        if (String.valueOf(this.txtGender.getAccessibleContext()).length() == 0) {
            txtStatus.setText("Gender cannot be blank. Pls re-enter!!");
            return false;
        }
        if (String.valueOf(this.txtEmail.getText()).length() == 0) {
            txtStatus.setText("Email cannot be blank. Pls re-enter!!");
            return false;
        }
        if (String.valueOf(this.txtPhone.getText()).length() == 0) {
            txtStatus.setText("Phone cannot be blank. Pls re-enter!!");
            return false;
        }

        if (String.valueOf(this.txtAddress.getText()).length() == 0) {
            txtStatus.setText("Address cannot be blank. Pls re-enter!");
            return false;
        }
        if (String.valueOf(this.txtUsername.getText()).length() == 0) {
            txtStatus.setText("Username cannot be blank. Pls re-enter!");
            return false;
        }
        if (String.valueOf(this.txtPassword.getText()).length() == 0) {
            txtStatus.setText("Password cannot be blank. Pls re-enter!!");
            return false;
        }
        if (String.valueOf(((JTextField) this.txtWorkStartDate.getDateEditor().getUiComponent()).getText()).length() == 0) {
            txtStatus.setText("Work Start Date cannot be blank. Pls re-enter!");
            return false;
        }
        if (String.valueOf(this.txtPayroll.getText()).length() == 0) {
            txtStatus.setText("Payroll cannot be blank. Pls re-enter!");
            return false;
        }

        return kq;
    }

    private void addEmployees() {
        if (checkNull()) {
            String sqlInsert = "INSERT INTO [dbo].[Employee] ([EmployeeID],[EmployeeName],[Birthday],[Gender],[Email],[Phone],[Address],[Username],[Password],[Work_start_date],[Payroll])\n"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?);";
            try {
                pst = conn.prepareStatement(sqlInsert);
                // pst.setString(1, (String) cbxPosition.getSelectedItem());
                pst.setString(1, this.txtEmployeeCode.getText());
                pst.setString(2, txtFullname.getText());
                pst.setDate(3, new java.sql.Date(this.txtBirthday.getDate().getTime()));
                pst.setString(4, (String) txtGender.getSelectedItem());
                pst.setString(5, (txtEmail.getText()));
                pst.setString(6, (txtPhone.getText()));
                pst.setString(7, txtAddress.getText());
                pst.setString(8, txtUsername.getText());
                pst.setString(9, this.txtPassword.getText());
                pst.setDate(10, new java.sql.Date(this.txtWorkStartDate.getDate().getTime()));
                pst.setString(11, txtPayroll.getText() + " " + "VND");
                pst.executeUpdate();
                refresh();
                txtStatus.setText("Thêm nhân viên thành công!");
                disabled();
                load(sql);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void changeEmployees() {
        int Click = TableEmployee.getSelectedRow();
        TableModel model = TableEmployee.getModel();

        if (checkNull()) {
            String sqlChange = "UPDATE [dbo].[Employee] SET [EmployeeID]=?,[EmployeeName]=?,[Birthday]=?,[Gender]=?,[Email]=?,[Phone]=?,[Address]=?,[Username]=?,[Password]=?,[Work_start_date]=?,[Payroll]=?\n"
                    + "WHERE [EmployeeID]='"
                    + model.getValueAt(Click, 1).toString().trim() + "'";;
            try {
                pst = conn.prepareStatement(sqlChange);
                pst.setString(1, this.txtEmployeeCode.getText());
                pst.setString(2, this.txtFullname.getText());
                pst.setDate(3, new java.sql.Date(this.txtBirthday.getDate().getTime()));
                pst.setString(4, (String) txtGender.getSelectedItem());
                pst.setString(5, this.txtEmail.getText());
                pst.setString(6, (String) this.txtPhone.getText());
                pst.setString(7, this.txtAddress.getText());
                pst.setString(8, this.txtUsername.getText());
                pst.setString(9, this.txtPassword.getText());
                pst.setDate(10, new java.sql.Date(this.txtWorkStartDate.getDate().getTime()));
                pst.setString(11, (String) this.txtPayroll.getText() + " " + "VND");
                pst.executeUpdate();
                txtStatus.setText("Save Changes Successfully!");
                disabled();
                refresh();
                load(sql);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean Check() {
        boolean kq = true;
        String sqlCheck = "SELECT * FROM Employee";
        try {
            pst = conn.prepareStatement(sqlCheck);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (this.txtEmployeeCode.getText().equals(rs.getString("EmployeeID").toString().trim())) {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        TableEmployee = new javax.swing.JTable();
        txtStatus = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jEmployeeID = new javax.swing.JLabel();
        jEmail = new javax.swing.JLabel();
        jFullname = new javax.swing.JLabel();
        jPhone = new javax.swing.JLabel();
        jBirthday = new javax.swing.JLabel();
        jAddress = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtFullname = new javax.swing.JTextField();
        txtGender = new javax.swing.JComboBox<>();
        txtAddress = new javax.swing.JTextField();
        txtBirthday = new com.toedter.calendar.JDateChooser();
        jPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        jWorkStartDate = new javax.swing.JLabel();
        jGender = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtEmployeeCode = new javax.swing.JTextField();
        jPayroll = new javax.swing.JLabel();
        txtPayroll = new javax.swing.JTextField();
        txtWorkStartDate = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnHomeBack = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EmployeeManagement");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        TableEmployee.setBackground(new java.awt.Color(80, 180, 155));
        TableEmployee.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TableEmployee.setForeground(new java.awt.Color(255, 255, 255));
        TableEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TableEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableEmployeeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableEmployee);

        txtStatus.setBackground(new java.awt.Color(82, 180, 180));
        txtStatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtStatus.setText("Status");

        jPanel1.setBackground(new java.awt.Color(82, 180, 155));

        jEmployeeID.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jEmployeeID.setForeground(new java.awt.Color(255, 255, 255));
        jEmployeeID.setText("ID:");

        jEmail.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jEmail.setForeground(new java.awt.Color(255, 255, 255));
        jEmail.setText("Email:");

        jFullname.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jFullname.setForeground(new java.awt.Color(255, 255, 255));
        jFullname.setText("Fullname:");

        jPhone.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jPhone.setForeground(new java.awt.Color(255, 255, 255));
        jPhone.setText("Phone:");

        jBirthday.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jBirthday.setForeground(new java.awt.Color(255, 255, 255));
        jBirthday.setText("Birthday:");

        jAddress.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jAddress.setForeground(new java.awt.Color(255, 255, 255));
        jAddress.setText("Address:");

        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtFullname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtGender.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtBirthday.setDateFormatString("dd/MM/yyyy");

        jPassword.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jPassword.setForeground(new java.awt.Color(255, 255, 255));
        jPassword.setText("Password:");

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });

        jWorkStartDate.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jWorkStartDate.setForeground(new java.awt.Color(255, 255, 255));
        jWorkStartDate.setText("Work Start Date:");

        jGender.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jGender.setForeground(new java.awt.Color(255, 255, 255));
        jGender.setText("Gender:");

        txtPhone.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPhoneKeyReleased(evt);
            }
        });

        jUsername.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jUsername.setForeground(new java.awt.Color(255, 255, 255));
        jUsername.setText("Username:");

        txtUsername.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtUsername.setEnabled(false);

        txtEmployeeCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtEmployeeCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmployeeCodeKeyReleased(evt);
            }
        });

        jPayroll.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jPayroll.setForeground(new java.awt.Color(255, 255, 255));
        jPayroll.setText("Payroll:");

        txtWorkStartDate.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(jEmployeeID))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jGender)
                            .addComponent(jEmail)))
                    .addComponent(jFullname, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jBirthday, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtEmployeeCode, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(txtFullname, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtGender, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtBirthday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPhone)
                    .addComponent(jUsername)
                    .addComponent(jAddress)
                    .addComponent(jPassword)
                    .addComponent(jWorkStartDate)
                    .addComponent(jPayroll))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPassword)
                    .addComponent(txtUsername)
                    .addComponent(txtAddress)
                    .addComponent(txtPayroll, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                    .addComponent(txtPhone)
                    .addComponent(txtWorkStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jEmployeeID)
                    .addComponent(txtEmployeeCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFullname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFullname))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jBirthday)
                    .addComponent(txtBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jGender)
                    .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jEmail))
                .addGap(45, 45, 45))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPhone)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jAddress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jUsername))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtWorkStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jWorkStartDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPayroll))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(82, 180, 155));

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refresh-icon.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnCreate.setBackground(new java.awt.Color(255, 255, 255));
        btnCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add.png"))); // NOI18N
        btnCreate.setText("Register");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Change.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setEnabled(false);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(255, 255, 255));
        btnSearch.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Find.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCreate)
                .addGap(18, 18, 18)
                .addComponent(btnUpdate)
                .addGap(18, 18, 18)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCreate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelete))
                        .addGap(1, 1, 1))
                    .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField2)
                    .addComponent(btnSearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("EMPLOYEE MANAGEMENT");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHomeBack, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(168, 168, 168)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnHomeBack, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 1053, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void TableEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableEmployeeMouseClicked
        txtGender.removeAllItems();
        int Click = TableEmployee.getSelectedRow();
        TableModel model = TableEmployee.getModel();
        txtEmployeeCode.setText(model.getValueAt(Click, 0).toString());
        txtFullname.setText(model.getValueAt(Click, 1).toString());
        ((JTextField) txtBirthday.getDateEditor().getUiComponent()).setText(model.getValueAt(Click, 2).toString());
        txtGender.addItem(model.getValueAt(Click, 3).toString());
        txtEmail.setText(model.getValueAt(Click, 4).toString());
        txtPhone.setText(model.getValueAt(Click, 5).toString());
        txtAddress.setText(model.getValueAt(Click, 6).toString());
        txtUsername.setText(model.getValueAt(Click, 7).toString());
        txtPassword.setText(model.getValueAt(Click, 8).toString());
        ((JTextField) txtWorkStartDate.getDateEditor().getUiComponent()).setText(model.getValueAt(Click, 9).toString());
        String[] s = model.getValueAt(Click, 10).toString().split("\\s");
        txtPayroll.setText(s[0]);
        btnCreate.setEnabled(false);
        btnDelete.setEnabled(true);
        btnUpdate.setEnabled(true);

    }//GEN-LAST:event_TableEmployeeMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int lick = JOptionPane.showConfirmDialog(null, "Bạn Có Muốn Thoát Khỏi Chương Trình Hay Không?", "Thông Báo", 2);
        if (lick == JOptionPane.OK_OPTION) {
            System.exit(0);
        } else {
            if (lick == JOptionPane.CANCEL_OPTION) {
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (Add == true) {
            if (Check()) {
                addEmployees();
            } else {
                txtStatus.setText("Không thể thêm nhân viên vì mã số nhân viên bạn nhập đã tồn tại");
            }
        } else if (Change == true) {
            changeEmployees();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa nhân viên hay không?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            String sqlDelete = "DELETE FROM Employee WHERE EmployeeID=? AND EmployeeName=?";//
            try {
                pst = conn.prepareStatement(sqlDelete);
                pst.setString(1, String.valueOf(this.txtEmployeeCode.getText()));
                pst.setString(2, txtFullname.getText());
                // pst.setString(3, txtPassword.getText());
                pst.executeUpdate();

                disabled();
                refresh();
                txtStatus.setText("Xóa nhân viên thành công!");
                load(sql);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        Add = false;
        Change = true;
        txtPhone.setText("");
        txtUsername.setText("");
        btnCreate.setEnabled(false);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        loadGender();
        enabled();
        //LoadPosition();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        refresh();
        Add = true;
        btnCreate.setEnabled(false);
        btnSave.setEnabled(true);
        loadGender();
        enabled();
        //  LoadPosition();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadGender();
        refresh();
        disabled();
        //LoadPosition();
        load(sql);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void txtEmployeeCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeCodeKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmployeeCodeKeyReleased

    private void txtPhoneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneKeyReleased
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
    }//GEN-LAST:event_txtPhoneKeyReleased

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        txtPayroll.setText(cutChar(txtPayroll.getText()));
    }//GEN-LAST:event_txtPasswordKeyReleased

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EmployeesManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeesManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeesManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeesManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail = new Detail();
                new EmployeesManagement(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableEmployee;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHomeBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jAddress;
    private javax.swing.JLabel jBirthday;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jEmail;
    private javax.swing.JLabel jEmployeeID;
    private javax.swing.JLabel jFullname;
    private javax.swing.JLabel jGender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel jPassword;
    private javax.swing.JLabel jPayroll;
    private javax.swing.JLabel jPhone;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel jUsername;
    private javax.swing.JLabel jWorkStartDate;
    private javax.swing.JTextField txtAddress;
    private com.toedter.calendar.JDateChooser txtBirthday;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmployeeCode;
    private javax.swing.JTextField txtFullname;
    private javax.swing.JComboBox<String> txtGender;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtPayroll;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JLabel txtStatus;
    private javax.swing.JTextField txtUsername;
    private com.toedter.calendar.JDateChooser txtWorkStartDate;
    // End of variables declaration//GEN-END:variables
}
