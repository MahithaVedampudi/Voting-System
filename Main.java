package src;

import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;// For displaying the Voter list in table format

class start_stop // To start and stop the voting process
{
    public static int start = 0;
    public static int stop = 0;
}

class Frame extends JFrame {// Base frame
    protected int st = 1;
    protected int sp = 0;

    Frame(String text) {
        super(text);
        this.setSize(1200, 700);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

    }
}

class Vote extends Frame implements ActionListener { // Frame used to display th houses for voter to vote
    JButton pegasus = new JButton();
    JButton phoenix = new JButton();
    JButton orion = new JButton();
    JButton ursa = new JButton();
    JButton nota = new JButton();
    String id;

    Vote(String s) {
        super("VOTING");
        id = s;

        // Get screen size
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Load and scale image to full screen
        ImageIcon originalIcon = new ImageIcon("src/houses.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Add background label with scaled image
        JLabel backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);

        // Set bounds and text for voting buttons
        orion.setBounds(675, 500, 100, 50);
        orion.setText("Orion");
        ursa.setBounds(675, 200, 100, 50);
        ursa.setText("Ursa");
        pegasus.setBounds(675, 300, 100, 50);
        pegasus.setText("Pegasus");
        phoenix.setBounds(675, 400, 100, 50);
        phoenix.setText("Phoenix");
        nota.setBounds(675, 600, 100, 50);
        nota.setText("NOTA");

        // Set frame properties
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(screenWidth, screenHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // Add components
        this.setLayout(null); // Make sure absolute positioning is used
        this.add(orion);
        this.add(ursa);
        this.add(pegasus);
        this.add(phoenix);
        this.add(nota);
        this.add(backgroundLabel); // Add image label last so it stays in the background

        // Add action listeners
        ursa.addActionListener(this);
        orion.addActionListener(this);
        pegasus.addActionListener(this);
        phoenix.addActionListener(this);
        nota.addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {
        String house = "";
        Object obj = e.getSource();
        if (obj == orion) {
            house = "Orion";
        } else if (obj == ursa) {
            house = "Ursa";
        } else if (obj == pegasus) {
            house = "Pegasus";
        } else if (obj == phoenix) {
            house = "Phoenix";
        } else if (obj == nota) {
            house = "NOTA";
        }
        this.dispose();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/demo", "root", "Mahitha@05");
            String sql = "update details " + "set house = '" + house + "'" + " where voter_id = '" + id + "'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

class Vframe extends Frame implements ActionListener {
    JTextField v = new JTextField();

    Vframe() {
        super("VOTING");

        ImageIcon pic = new ImageIcon("src/voting2.jpg");
        JLabel pl = new JLabel(pic);
        pl.setBounds(-500, -450, 1800, 1500);

        // Label placed ABOVE the textbox
        JLabel l1 = new JLabel("Voter ID", JLabel.CENTER);
        l1.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 25));
        l1.setBounds(700, 240, 200, 30);

        v.setBounds(700, 280, 200, 30);
        JButton ok = new JButton("OK");
        ok.setBounds(740, 320, 100, 30);

        ok.setVisible(true); // Ensure visible
        ok.setEnabled(true); // Ensure clickable
        this.add(l1);
        this.add(v);
        this.add(ok);
        this.add(pl);

        ok.addActionListener(this);

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    /*
     * public void actionPerformed(ActionEvent e) {
     * try {
     * int flag = 0;
     * Class.forName("com.mysql.cj.jdbc.Driver");
     * Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/demo",
     * "root", "Mahitha@05");
     * PreparedStatement stmt = conn.prepareStatement("select * from details");
     * ResultSet rs = stmt.executeQuery();
     * String id = v.getText();
     * while (rs.next()) {
     * if (rs.getString(1).equals(id)) {
     * flag = 1;
     * if (rs.getString(4).equals("NULL")) {
     * //Vote V = new Vote(id);
     * new Vote(id);
     * } else {
     * JOptionPane.showMessageDialog(null, "Vote has been casted.");
     * }
     * }
     * }
     * if (flag == 0) {
     * JOptionPane.showMessageDialog(null, "Voter Id not found.");
     * }
     * conn.close();
     * this.dispose();
     * } catch (Exception ex) {
     * System.out.println(ex);
     * 
     * }
     * }
     * 
     */

    public void actionPerformed(ActionEvent e) {
        String id = v.getText().trim(); // Get & trim voter ID input

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a Voter ID!");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/demo",
                    "root",
                    "Mahitha@05");

            // ▼ Improved query: Checks if the voter exists ▼
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT house FROM details WHERE voter_id = ?");
            stmt.setString(1, id); // Bind voter ID to the query

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // If voter exists
                String houseStatus = rs.getString(1); // Get 'house' column value

                if (houseStatus == null || houseStatus.equalsIgnoreCase("NULL")) {
                    // Allow voting
                    new Vote(id); // Open voting page
                    this.dispose(); // Close current window
                } else {
                    JOptionPane.showMessageDialog(null, "You have already voted!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Voter ID not found!");
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
}

class Admin extends Frame implements ActionListener {
    static boolean isLoggedIn = false;

    JButton start = new JButton("Start Voting");
    JButton stop = new JButton("Stop Voting");
    JButton results = new JButton("Results");
    JButton addvoter = new JButton("Add Voter");
    JButton deletevoter = new JButton("Delete Voter");
    JButton analysis = new JButton("Analysis");
    JButton display_list = new JButton("Display List");
    JPasswordField password;

    Admin() {
        super("ADMIN");
        ImageIcon pic = new ImageIcon("src/buttons.jpg");
        JLabel bl = new JLabel(pic);
        bl.setBounds(-250, -300, 1800, 1500);
        start.setBounds(150, 120, 100, 50);
        stop.setBounds(275, 120, 100, 50);
        addvoter.setBounds(400, 120, 100, 50);
        deletevoter.setBounds(525, 120, 125, 50);
        results.setBounds(675, 120, 100, 50);
        analysis.setBounds(800, 120, 100, 50);
        display_list.setBounds(925, 120, 100, 50);
        start.addActionListener(this);
        stop.addActionListener(this);
        results.addActionListener(this);
        addvoter.addActionListener(this);
        deletevoter.addActionListener(this);
        analysis.addActionListener(this);
        display_list.addActionListener(this);
        this.add(start);
        this.add(stop);
        this.add(results);
        this.add(addvoter);
        this.add(deletevoter);
        this.add(analysis);
        this.add(display_list);
        this.add(bl);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    static void display() throws SQLException { // Function to display the details of the voters in table format.
        JFrame display_list = new JFrame();
        String[] columnNames = { "Voter_id", "First_name", "Last_name", "Vote casted" };
        Object[][] data = {};
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable voterListTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(voterListTable);
        display_list.add(scrollPane);
        display_list.setTitle("List");
        display_list.setSize(800, 400);
        display_list.setLocationRelativeTo(null);
        display_list.setVisible(true);
        Connection con1 = null;
        try {
            // Class.forName("com.mysql.cj.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost/demo", "root", "Mahitha@05");
            PreparedStatement stmt = con1.prepareStatement("Select * from details");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String voter_id = rs.getString("Voter_ID");
                String firstName = rs.getString("First_Name");
                String lastName = rs.getString("Last_Name");
                String house = "YES";
                if (rs.getString("House").equals("NULL")) {
                    house = "NO";
                }
                Object[] row = { voter_id, firstName, lastName, house };
                model.addRow(row);
            }
            con1.close();
        } catch (SQLException e) {
            e.printStackTrace();
            con1.close();
        }
    }

    static String percentage(double total, double a) {
        String str = String.valueOf((a / total) * 100);
        if (str.length() >= 5) {
            return str.substring(0, 5);
        } else {
            return str;
        }
    }

    static void analysis() { // Function to display analysis of the voting process once it is done.
        JFrame Analysis = new JFrame("Analysis");
        ImageIcon ab = new ImageIcon("analysis.jpg");
        JLabel jl = new JLabel(ab);
        jl.setBounds(300, -300, 1800, 1500);
        JLabel total = new JLabel("Votes casted");
        total.setBounds(200, 150, 150, 30);
        total.setForeground(Color.black);
        total.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel nl = new JLabel("Votes not casted");
        nl.setBounds(200, 200, 150, 30);
        nl.setForeground(Color.black);
        nl.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel phoenix = new JLabel("Phoenix");
        phoenix.setBounds(200, 250, 100, 30);
        phoenix.setForeground(Color.orange);
        phoenix.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel pegasus = new JLabel("Pegasus");
        pegasus.setBounds(200, 300, 100, 30);
        pegasus.setForeground(Color.blue);
        pegasus.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel orion = new JLabel("Orion");
        orion.setBounds(200, 350, 100, 30);
        orion.setForeground(Color.red);
        orion.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel ursa = new JLabel("Ursa");
        ursa.setBounds(200, 400, 100, 30);
        ursa.setForeground(Color.green);
        ursa.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel nota = new JLabel("NOTA");
        nota.setBounds(200, 450, 100, 30);
        nota.setForeground(Color.black);
        nota.setFont(new Font("Calibri", Font.PLAIN, 20));
        // labels to print percentages
        JLabel total_per = new JLabel();
        total_per.setBounds(450, 150, 100, 30);
        total_per.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel nl_per = new JLabel();
        nl_per.setBounds(450, 200, 100, 30);
        nl_per.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel phoenix_per = new JLabel();
        phoenix_per.setBounds(450, 250, 100, 30);
        phoenix_per.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel pegasus_per = new JLabel();
        pegasus_per.setBounds(450, 300, 100, 30);
        pegasus_per.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel orion_per = new JLabel();
        orion_per.setBounds(450, 350, 100, 30);
        orion_per.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel ursa_per = new JLabel();
        ursa_per.setBounds(450, 400, 100, 30);
        ursa_per.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel nota_per = new JLabel();
        nota_per.setBounds(450, 450, 100, 30);
        nota_per.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel text = new JLabel("The analysis is displayed in percentages: ");
        text.setBounds(200, 80, 550, 40);
        text.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 25));
        jl.add(total);
        jl.add(nl);
        jl.add(phoenix);
        jl.add(pegasus);
        jl.add(orion);
        jl.add(ursa);
        jl.add(nota);
        jl.add(total_per);
        jl.add(nl_per);
        jl.add(phoenix_per);
        jl.add(pegasus_per);
        jl.add(orion_per);
        jl.add(ursa_per);
        jl.add(nota_per);
        jl.add(text);
        Analysis.add(jl);
        int total_count = 0;
        int nl_count = 0;
        int phoenix_count = 0;
        int pegasus_count = 0;
        int orion_count = 0;
        int ursa_count = 0;
        int nota_count = 0;
        Analysis.setVisible(true);
        Analysis.setSize(1200, 700);
        Analysis.setResizable(false);
        Analysis.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Analysis.setLocationRelativeTo(null);
        Connection conn;
        try {
            String st1 = "select count(*) from details";
            String st2 = "select count(*) from details where House = 'NULL'";
            String st3 = "select count(*) from details where House = 'Phoenix'";
            String st4 = "select count(*) from details where House = 'Pegasus'";
            String st5 = "select count(*) from details where House = 'Orion'";
            String st6 = "select count(*) from details where House = 'Ursa'";
            String st7 = "select count(*) from details where House = 'NOTA'";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/demo", "root", "Mahitha@05");
            Statement stmt1 = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            Statement stmt3 = conn.createStatement();
            Statement stmt4 = conn.createStatement();
            Statement stmt5 = conn.createStatement();
            Statement stmt6 = conn.createStatement();
            Statement stmt7 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(st1);
            ResultSet rs2 = stmt2.executeQuery(st2);
            ResultSet rs3 = stmt3.executeQuery(st3);
            ResultSet rs4 = stmt4.executeQuery(st4);
            ResultSet rs5 = stmt5.executeQuery(st5);
            ResultSet rs6 = stmt6.executeQuery(st6);
            ResultSet rs7 = stmt7.executeQuery(st7);
            rs1.next();
            rs2.next();
            rs3.next();
            rs4.next();
            rs5.next();
            rs6.next();
            rs7.next();
            total_count = rs1.getInt(1);
            nl_count = rs2.getInt(1);
            phoenix_count = rs3.getInt(1);
            pegasus_count = rs4.getInt(1);
            orion_count = rs5.getInt(1);
            ursa_count = rs6.getInt(1);
            nota_count = rs7.getInt(1);
            String c_t = percentage(total_count, (total_count - nl_count));
            total_per.setText(c_t);
            String c_n = percentage(total_count, nl_count);
            nl_per.setText(c_n);
            String c_ph = percentage(total_count, phoenix_count);
            phoenix_per.setText(c_ph);
            String c_p = percentage(total_count, pegasus_count);
            pegasus_per.setText(c_p);
            String c_o = percentage(total_count, orion_count);
            orion_per.setText(c_o);
            String c_u = percentage(total_count, ursa_count);
            ursa_per.setText(c_u);
            String c_no = percentage(total_count, nota_count);
            nota_per.setText(c_no);
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    static void results() { // Function to display winner if exists else displays the votes casted for all
                            // houses.
        JFrame Results = new JFrame("Results");
        JLabel result = new JLabel("Votes casted for each house: ");
        result.setBounds(200, 150, 400, 40);
        result.setFont(new Font("Lucida Calligraphy", Font.BOLD, 25));
        JLabel phoenix = new JLabel("Phoenix");
        phoenix.setForeground(Color.orange);
        phoenix.setFont(new Font("Calibri", Font.PLAIN, 20));
        phoenix.setBounds(200, 200, 100, 40);
        JLabel phoenix_vote = new JLabel();
        phoenix_vote.setBounds(400, 200, 100, 30);
        phoenix_vote.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel pegasus = new JLabel("Pegasus");
        pegasus.setForeground(Color.blue);
        pegasus.setBounds(200, 250, 100, 30);
        pegasus.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel pegasus_vote = new JLabel();
        pegasus_vote.setBounds(400, 250, 100, 40);
        pegasus_vote.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel orion = new JLabel("Orion");
        orion.setForeground(Color.red);
        orion.setBounds(200, 300, 100, 30);
        orion.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel orion_vote = new JLabel();
        orion_vote.setBounds(400, 300, 100, 40);
        orion_vote.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel ursa = new JLabel("Ursa");
        ursa.setForeground(Color.green);
        ursa.setBounds(200, 350, 100, 30);
        ursa.setFont(new Font("Calibri", Font.PLAIN, 20));
        JLabel ursa_vote = new JLabel();
        ursa_vote.setBounds(400, 350, 100, 40);
        ursa_vote.setFont(new Font("Calibri", Font.PLAIN, 20));
        ImageIcon pic;
        JLabel text = new JLabel("WINNER:");
        text.setBounds(250, 50, 200, 100);
        text.setFont(new Font("Book Antiqua", Font.BOLD, 35));
        int phoenix_count = 0;
        int pegasus_count = 0;
        int orion_count = 0;
        int ursa_count = 0;
        Results.setResizable(false);
        Results.setVisible(true);
        Results.setSize(1200, 700);
        Results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Results.setLocationRelativeTo(null);
        try {
            String st1 = "select count(*) from details where House = 'Phoenix'";
            String st2 = "select count(*) from details where House = 'Pegasus'";
            String st3 = "select count(*) from details where House = 'Orion'";
            String st4 = "select count(*) from details where House = 'Ursa'";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/demo", "root", "Mahitha@05");
            Statement stmt1 = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            Statement stmt3 = conn.createStatement();
            Statement stmt4 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(st1);
            ResultSet rs2 = stmt2.executeQuery(st2);
            ResultSet rs3 = stmt3.executeQuery(st3);
            ResultSet rs4 = stmt4.executeQuery(st4);
            rs1.next();
            rs2.next();
            rs3.next();
            rs4.next();
            phoenix_count = rs1.getInt(1);
            pegasus_count = rs2.getInt(1);
            orion_count = rs3.getInt(1);
            ursa_count = rs4.getInt(1);
            if (phoenix_count > pegasus_count && phoenix_count > orion_count && phoenix_count > ursa_count) {
                pic = new ImageIcon("src/phoenix.jpg");
                result = new JLabel(pic);
                result.setBounds(-50, -300, 1800, 1500);
                result.add(text);
                Results.add(result);

            } else if (pegasus_count > phoenix_count && pegasus_count > orion_count && pegasus_count > ursa_count) {
                pic = new ImageIcon("src/pegasus.jpg");
                result = new JLabel(pic);
                result.setBounds(-50, -300, 1800, 1500);
                result.add(text);
                Results.add(result);
            } else if (orion_count > pegasus_count && orion_count > phoenix_count && orion_count > ursa_count) {
                pic = new ImageIcon("src/orion.jpg");
                result = new JLabel(pic);
                result.setBounds(-50, -300, 1800, 1500);
                result.add(text);
                Results.add(result);
            } else if (ursa_count > pegasus_count && ursa_count > phoenix_count && ursa_count > orion_count) {
                pic = new ImageIcon("src/ursa.jpg");
                result = new JLabel(pic);
                result.setBounds(-50, -300, 1800, 1500);
                result.add(text);
                Results.add(result);
            } else {
                String c_ph = String.valueOf(phoenix_count);
                phoenix_vote.setText(c_ph);
                String c_p = String.valueOf(pegasus_count);
                pegasus_vote.setText(c_p);
                String c_o = String.valueOf(orion_count);
                orion_vote.setText(c_o);
                String c_u = String.valueOf(ursa_count);
                ursa_vote.setText(c_u);
                Results.add(result);
                Results.add(phoenix);
                Results.add(pegasus);
                Results.add(orion);
                Results.add(ursa);
                Results.add(phoenix_vote);
                Results.add(pegasus_vote);
                Results.add(orion_vote);
                Results.add(ursa_vote);

            }
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    static void delete_voter() // Function to delete an existing voter.
    {
        JFrame Delete_voter = new JFrame("Delete Voter");
        Delete_voter.setLayout(null);

        // Load and scale image to fit right half
        ImageIcon b1 = new ImageIcon("src/addel.jpg");
        Image img = b1.getImage().getScaledInstance(600, 500, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(img);

        // Image label on the right half
        JLabel jl = new JLabel(scaledIcon);
        jl.setBounds(600, 0, 600, 700); // Right half

        // UI Components on the left half
        JLabel tv = new JLabel("Enter Voter ID");
        tv.setBounds(200, 100, 150, 40);

        JTextField vid = new JTextField();
        vid.setBounds(200, 150, 200, 40);

        JButton delete = new JButton("Delete");
        delete.setBounds(200, 220, 200, 40);

        // Add components to frame
        Delete_voter.add(tv);
        Delete_voter.add(vid);
        Delete_voter.add(delete);
        Delete_voter.add(jl);

        Delete_voter.setResizable(false);
        Delete_voter.setVisible(true);
        Delete_voter.setSize(1200, 700);
        Delete_voter.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Delete_voter.setLocationRelativeTo(null);

        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String sq = "select * from details";
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost/demo", "root", "Mahitha@05");
                    PreparedStatement stm = con.prepareStatement(sq);
                    ResultSet rs = stm.executeQuery();
                    int flag = 0;
                    while (rs.next()) {
                        if (rs.getString(1).equals(vid.getText())) {
                            try {
                                String statement = "Delete from details where Voter_ID = " + "'" + vid.getText() + "'";
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/demo", "root",
                                        "Mahitha@05");
                                PreparedStatement stmt = conn.prepareStatement(statement);
                                stmt.executeUpdate();
                                conn.close();
                                JOptionPane.showMessageDialog(null, "Voter deleted successfully.");
                                Delete_voter.dispose();
                            } catch (Exception ex) {
                                System.out.println(ex);
                            }
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        JOptionPane.showMessageDialog(null, "Voter ID doesn't exists");
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
    }

    static void add_voter() // Function to add a new voter.
    {
        JFrame Add_voter = new JFrame("Add Voter");
        Add_voter.setLayout(null);

        // Load and scale image to fit right half
        ImageIcon ab = new ImageIcon("src/addel.jpg");
        Image img = ab.getImage().getScaledInstance(600, 500, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(img);
        JLabel jl = new JLabel(scaledIcon);
        jl.setBounds(600, 0, 600, 700); // Right half of frame

        // UI Components on the left half
        JLabel tv = new JLabel("Enter Voter ID");
        tv.setBounds(150, 80, 150, 30);
        JTextField epi = new JTextField();
        epi.setBounds(150, 110, 200, 30);

        JLabel tf = new JLabel("Enter First Name");
        tf.setBounds(150, 160, 150, 30);
        JTextField first = new JTextField();
        first.setBounds(150, 190, 200, 30);

        JLabel tl = new JLabel("Enter Last Name");
        tl.setBounds(150, 240, 150, 30);
        JTextField last = new JTextField();
        last.setBounds(150, 270, 200, 30);

        JButton add = new JButton("Add");
        add.setBounds(180, 330, 140, 40);

        // Add components to frame
        Add_voter.add(tv);
        Add_voter.add(epi);
        Add_voter.add(tf);
        Add_voter.add(first);
        Add_voter.add(tl);
        Add_voter.add(last);
        Add_voter.add(add);
        Add_voter.add(jl);

        Add_voter.setSize(1200, 700);
        Add_voter.setResizable(false);
        Add_voter.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Add_voter.setLocationRelativeTo(null);
        Add_voter.setVisible(true);

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (epi.getText().length() != 13) {
                    JOptionPane.showMessageDialog(null, "Invalid Voter ID");
                } else if (first.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter First name");
                } else if (last.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Last name");
                } else {
                    try {
                        String sq = "select * from details";// jdbc
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/demo", "root",
                                "Mahitha@05");
                        PreparedStatement stmt = conn.prepareStatement(sq);
                        ResultSet rs = stmt.executeQuery();
                        int flag = 0;
                        while (rs.next()) {
                            if (rs.getString(1).equals(epi.getText())) {
                                JOptionPane.showMessageDialog(null, "Voter ID already exists");
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            try {
                                String sql1 = "Insert into details values (?,?,?,?)";// jdbc
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root",
                                        "Mahitha@05");

                                // Connection
                                // con=DriverManager.getConnection("jdbc:mysql://localhost/demo","root",
                                // "Mahitha@05");
                                PreparedStatement stm = con.prepareStatement(sql1);
                                stm.setString(1, epi.getText());
                                stm.setString(2, first.getText());
                                stm.setString(3, last.getText());
                                stm.setString(4, "NULL");
                                stm.executeUpdate();
                                conn.close();
                                JOptionPane.showMessageDialog(null, "Voter added successfully.");
                                Add_voter.dispose();
                            } catch (Exception ex) {
                                System.out.println(ex);
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            start_stop.start = 1;
            start_stop.stop = 0;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/demo", "root", "Mahitha@05");
                String sql = "update details " + "set House = '" + "NULL" + "'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.executeUpdate();
                conn.close();
            } catch (Exception ex) {
                System.out.println(ex);

            }
            JOptionPane.showMessageDialog(null, "Voting has started.");
        } else if (e.getSource() == stop) {
            start_stop.stop = 1;
            start_stop.start = 0;
            JOptionPane.showMessageDialog(null, "Voting has stopped.");
        } else if (e.getSource() == results) {
            if (start_stop.stop == 1 && start_stop.start == 0)
                results();
            else {
                JOptionPane.showMessageDialog(null, "Voting has not been completed.");
            }
        } else if (e.getSource() == addvoter) {
            if (start_stop.stop == 0 && start_stop.start == 0)
                add_voter();
            else {
                JOptionPane.showMessageDialog(null, "Changes can't be made.");
            }
        } else if (e.getSource() == deletevoter) {
            if (start_stop.stop == 0 && start_stop.start == 0)
                delete_voter();
            else {
                JOptionPane.showMessageDialog(null, "Changes can't be made");
            }

        } else if (e.getSource() == analysis) {
            if (start_stop.stop == 1 && start_stop.start == 0)
                analysis();
            else {
                JOptionPane.showMessageDialog(null, "Voting has not been completed.");
            }

        } else if (e.getSource() == display_list) {
            try {
                display();
            } catch (SQLException e1) {

                e1.printStackTrace();
            }
        }
        this.dispose();
    }
}

class Aframe extends Frame implements ActionListener {
    JPasswordField password;
    JTextField username;

    Aframe() {
        super("ADMIN");

        ImageIcon ad = new ImageIcon("src/admin2.jpg");
        JLabel jb = new JLabel(ad);
        jb.setBounds(-130, -300, 1800, 1400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);

        JLabel userLabel = new JLabel("Enter Username:");
        userLabel.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 20));
        userLabel.setBounds(150, 120, 300, 30);
        userLabel.setForeground(Color.BLUE);
        username = new JTextField();
        username.setBounds(150, 160, 300, 30);

        JLabel passLabel = new JLabel("Enter Password:");
        passLabel.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 20));
        passLabel.setBounds(150, 210, 300, 30);
        passLabel.setForeground(Color.BLUE);
        password = new JPasswordField();
        password.setBounds(150, 250, 300, 30);

        JButton submit = new JButton("Submit");
        submit.setBounds(200, 310, 150, 30);
        submit.addActionListener(this);

        this.add(userLabel);
        this.add(username);
        this.add(passLabel);
        this.add(password);
        this.add(submit);
        this.add(jb);

        this.setResizable(false);
    }

    public void actionPerformed(ActionEvent e) {
        String user = username.getText();
        String pass = new String(password.getPassword());

        if (user.equals("admin") && pass.equals("admin")) {
            Admin.isLoggedIn = true;
            // Admin A = new Admin();
            new Admin();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Wrong username or password");
        }
    }
}

class Frame1 extends Frame implements ActionListener // main frame that displays admin and voter.
{
    JButton admin, voter;

    Frame1(String text) {
        super(text);

        // Load and scale the image to fit the window size
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize(); // Get full screen resolution

        ImageIcon originalIcon = new ImageIcon("src/vote2.jpg");
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
        ImageIcon bg = new ImageIcon(scaledImage);

        JLabel label = new JLabel(bg);
        label.setBounds(0, 0, screenSize.width, screenSize.height); // full screen bounds

        // Buttons
        admin = new JButton();
        admin.setBounds(550, 600, 100, 50);
        admin.setText("Admin");
        admin.addActionListener(this);
        this.add(admin);

        voter = new JButton();
        voter.setBounds(750, 600, 100, 50);
        voter.setText("Voter");
        voter.addActionListener(this);
        this.add(voter);

        // Add the background label last so it's behind everything
        this.add(label);
        this.setLayout(null);
        this.setSize(screenSize.width, screenSize.height);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

    }

    public void actionPerformed(ActionEvent e) {
        try {
            Object obj = e.getSource();
            if (obj == voter) {
                if (start_stop.start == 1 && start_stop.stop == 0) {

                    // Vframe v = new Vframe();
                    new Vframe();
                } else if (start_stop.stop == 1 && start_stop.start == 0) {
                    JOptionPane.showMessageDialog(null, "Voting has been completed.");
                } else if (start_stop.start == 0 && start_stop.stop == 0) {
                    JOptionPane.showMessageDialog(null, "Voting has not started yet.");
                }
            } else {
                if (Admin.isLoggedIn) {
                    // Admin A = new Admin();
                    new Admin();
                } else {
                    // Aframe a = new Aframe();
                    new Aframe();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {

        Frame1 f = new Frame1("VOTING SYSTEM");
        f.setVisible(true);
    }
}
