package ex_4_advnced_vr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class baglanti extends otobus {
    private static Connection mycon;
    
    public baglanti() {
        super("", "", 0, "", 0);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            mycon = DriverManager.getConnection("jdbc:mysql://localhost:3306/deneme?userTimezone=true&serverTimezone=UTC", "root", "");
            System.out.println("Başarılı bağlantı");
            createYolcuTable();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Bağlantı başarısız");
        }
    }

    private void createYolcuTable() {
        try (Statement statement = mycon.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS yolcu (" +
                    "yolcu_ismi VARCHAR(255)," +
                    "yolcu_cinsiyeti VARCHAR(255)," +
                    "yolcu_yasi INT," +
                    "yolcu_tc VARCHAR(11)," +
                    "koltuk_no INT" +
                    ")";
            statement.executeUpdate(createTableQuery);
            System.out.println("yolcu table oluşturuldu veya zaten var.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("yolcu table oluşturulurken bir hata oluştu: " + e.getMessage());
        }
    }

    public static void insertUserData(yolcu yolcu) {
        boolean eklemeBasarili = false;
        try {
            if (isSeatNumberExists(yolcu.koltuk_no)) {
                JOptionPane.showMessageDialog(null, "Bu koltuk numarası zaten kullanılmaktadır. Lütfen başka bir koltuk seçin.");
                eklemeBasarili = false;
                return;
            }
            else {
            	eklemeBasarili = true;
            }

            String query = "INSERT INTO yolcu (yolcu_ismi, yolcu_cinsiyeti, yolcu_yasi, yolcu_tc, koltuk_no) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = mycon.prepareStatement(query);

            preparedStatement.setString(1, yolcu.Get_name());
            preparedStatement.setString(2, yolcu.Get_gender());
            preparedStatement.setInt(3, yolcu.Get_age());
            preparedStatement.setString(4, yolcu.yolcu_tc);
            preparedStatement.setInt(5, yolcu.koltuk_no);

            preparedStatement.executeUpdate();
            System.out.println("Veri başarıyla eklendi.");
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Veri eklenirken bir hata oluştu: " + e.getMessage());
        }

        if (eklemeBasarili) {
            JOptionPane.showMessageDialog(null, "Veri başarıyla eklendi.");
        }
    }


    private static boolean isSeatNumberExists(int koltukNo) {
        try {
            String query = "SELECT COUNT(*) FROM yolcu WHERE koltuk_no = ?";
            PreparedStatement preparedStatement = mycon.prepareStatement(query);
            preparedStatement.setInt(1, koltukNo);
            int rowCount = 0;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    rowCount = resultSet.getInt(1);
                }
            }

            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Koltuk numarası kontrolü yapılırken bir hata oluştu: " + e.getMessage());
            return false; 
        }
    }


    
}

    

