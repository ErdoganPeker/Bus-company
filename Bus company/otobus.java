package ex_4_advnced_vr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class otobus extends yolcu implements otobus_bilgi {
    int yolcu_sayisi = 0;
    int bilet_fiyati = 200;
    private static Connection mycon;
    
    otobus(String yolcu_ismi, String yolcu_cinsiyeti, int yolcu_yasi, String yolcu_tc, int koltuk_no) {
        super(yolcu_ismi, yolcu_cinsiyeti, yolcu_yasi, yolcu_tc, koltuk_no);
    }

    public void fiyat_hesapla() {
        for (int i = 0; i < 2; i++) {
            if (haraket_gunleri[i].equals("Pazartesi")) {
                bilet_fiyati += bilet_fiyati * 0.15;
            } else {
                bilet_fiyati += bilet_fiyati * 0.25;
            }
        }
    }
    public static void goruntuleBiletBilgileri(String tc) {
        try {
            if (mycon == null || mycon.isClosed()) {
                System.out.println("Bağlantı null veya kapalı durumda.");
                return;
            }

            String query = "SELECT * FROM yolcu WHERE yolcu_tc = ?";
            PreparedStatement preparedStatement = mycon.prepareStatement(query);
            preparedStatement.setString(1, tc);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String yolcuIsmi = resultSet.getString("yolcu_ismi");
                    String cinsiyet = resultSet.getString("yolcu_cinsiyeti");
                    int yas = resultSet.getInt("yolcu_yasi");
                    String tcNo = resultSet.getString("yolcu_tc");
                    int koltukNo = resultSet.getInt("koltuk_no");

                    System.out.println("Yolcu İsmi: " + yolcuIsmi);
                    System.out.println("Cinsiyet: " + cinsiyet);
                    System.out.println("Yaş: " + yas);
                    System.out.println("TC Kimlik No: " + tcNo);
                    System.out.println("Koltuk No: " + koltukNo);
                    System.out.println("----------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Bilet bilgileri görüntülenirken bir hata oluştu: " + e.getMessage());
        }
    }


    public static void biletIptalEt(String tc) {
        try {
            if (mycon == null || mycon.isClosed()) {
                System.out.println("Bağlantı null veya kapalı durumda.");
                return;
            }

            String query = "DELETE FROM yolcu WHERE yolcu_tc = ?";
            PreparedStatement preparedStatement = mycon.prepareStatement(query);
            preparedStatement.setString(1, tc);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Bilet başarıyla iptal edildi: TC " + tc);
            } else {
                System.out.println("Belirtilen TC'ye sahip bir bilet bulunamadı: TC " + tc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Bilet iptali yapılırken bir hata oluştu: " + e.getMessage());
        }
    }



    void get_info() {
        System.out.println("Firma Adı: " + firmaadi);
        System.out.println("Plaka: " + plaka);
        System.out.println("Hareket Günleri: " + haraket_gunleri[0] + " " + haraket_gunleri[1]);
        System.out.println("Hareket Saatleri: " + haraket_saatleri[0] + " " + haraket_saatleri[0]);
        System.out.println("Güzergah: " + guzergah);
        System.out.println("Yolcu Sayısı: " + yolcu_sayisi);
        System.out.println("Kaptan Adı: " + kaptan_adi);
        System.out.println("Yolcu Cinsiyeti:  " + super.yolcu_cinsiyeti);
        System.out.println("Yolcu İsmi " + super.yolcu_ismi);
        System.out.println("Yolcu Tcsi: " + super.yolcu_tc);
        System.out.println("Yolcu Yasi: " + super.yolcu_yasi);
        System.out.println("Yolcu Yasi: " + super.koltuk_no);
    }

    public static void main(String[] args) {
    	baglanti baglanti = new baglanti();
        JFrame mainFrame = new JFrame("Otobüs Bilet İşlemleri");
        mainFrame.setSize(300, 150);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        JButton biletAlButton = new JButton("1 - Otobüs Bileti Al");
        JButton biletIptalButton = new JButton("2 - Otobüs Bileti İptal");
        JButton biletGoruntuleButton = new JButton("3 - Bilet Görüntüle");

        
        mainPanel.add(biletGoruntuleButton);
        mainPanel.add(biletIptalButton);
        mainPanel.add(biletAlButton);
        mainFrame.getContentPane().add(mainPanel);

        JFrame biletAlFrame = new JFrame("Otobüs Bilet Alma");
        biletAlFrame.setSize(400, 200);
        biletAlFrame.setLocationRelativeTo(null);
        biletAlFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel biletAlPanel = new JPanel();
        biletAlPanel.setLayout(new BorderLayout());

        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(null);
        seatPanel.setLayout(new GridLayout(5, 4, 10, 5));
        // Adjust rows and columns as needed
        int buttonWidth = 90;
        int buttonHeight = 40;
        int buttonMargin = 5;
        int rowMaxButtons = 4;

        for (int i = 1; i <= 20; i++) {
            int col = (i - 1) % rowMaxButtons;
            int row = (i - 1) / rowMaxButtons;

            JButton seatButton = new JButton("Koltuk " + i);
            int x = col * (buttonWidth + buttonMargin) + buttonMargin;
            int y = row * (buttonHeight + buttonMargin) + buttonMargin;

            seatButton.setBounds(x, y, buttonWidth, buttonHeight);
            seatButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton clickedButton = (JButton) e.getSource();
                    String seatNumber = clickedButton.getText();
                    JFrame bilgiIstemeFrame = new JFrame("Bilgi İsteme");
                    bilgiIstemeFrame.setSize(400, 200);
                    bilgiIstemeFrame.setLocationRelativeTo(null);

                    JPanel bilgiIstemePanel = new JPanel();
                    bilgiIstemePanel.setLayout(new GridLayout(6, 2, 5, 5));

                    JLabel adSoyadLabel = new JLabel("Ad Soyad:");
                    JTextField adSoyadField = new JTextField();

                    JLabel cinsiyetLabel = new JLabel("Cinsiyet:");
                    JTextField cinsiyetField = new JTextField();

                    JLabel yasLabel = new JLabel("Yaş:");
                    JTextField yasField = new JTextField();

                    JLabel tcLabel = new JLabel("TC Kimlik No:");
                    JTextField tcField = new JTextField();

                    JLabel koltukNoLabel = new JLabel("Koltuk No:");
                    JTextField koltukNoField = new JTextField(seatNumber.substring(seatNumber.indexOf(" ") + 1));

                    JButton bilgiGonderButton = new JButton("Bilgileri Gönder");
                    bilgiGonderButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String adSoyad = adSoyadField.getText();
                            String cinsiyet = cinsiyetField.getText();
                            int yas = Integer.parseInt(yasField.getText());
                            String tc = tcField.getText();
                            int koltukNo = Integer.parseInt(koltukNoField.getText());
                            baglanti.insertUserData(new yolcu(adSoyad, cinsiyet, yas, tc, koltukNo));
                            String message = "Bilgiler:\n" +
                                    "Ad Soyad: " + adSoyad + "\n" +
                                    "Cinsiyet: " + cinsiyet + "\n" +
                                    "Yaş: " + yas + "\n" +
                                    "TC Kimlik No: " + tc + "\n" +
                                    "Koltuk No: " + koltukNo;
                            JOptionPane.showMessageDialog(null, message);
                            bilgiIstemeFrame.dispose();
                        }
                    });

                    bilgiIstemePanel.add(adSoyadLabel);
                    bilgiIstemePanel.add(adSoyadField);
                    bilgiIstemePanel.add(cinsiyetLabel);
                    bilgiIstemePanel.add(cinsiyetField);
                    bilgiIstemePanel.add(yasLabel);
                    bilgiIstemePanel.add(yasField);
                    bilgiIstemePanel.add(tcLabel);
                    bilgiIstemePanel.add(tcField);
                    bilgiIstemePanel.add(koltukNoLabel);
                    bilgiIstemePanel.add(koltukNoField);
                    bilgiIstemePanel.add(bilgiGonderButton);

                    bilgiIstemeFrame.getContentPane().add(bilgiIstemePanel);
                    bilgiIstemeFrame.setVisible(true);
                }
            });
            seatPanel.add(seatButton);
        }

        biletAlPanel.add(seatPanel, BorderLayout.CENTER);
        biletAlFrame.getContentPane().add(biletAlPanel);
        JFrame biletGoruntuleFrame = new JFrame("Bilet Görüntüleme");
        biletGoruntuleFrame.setSize(400, 200);
        biletGoruntuleFrame.setLocationRelativeTo(null);
        biletGoruntuleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel biletGoruntulePanel = new JPanel();
        biletGoruntulePanel.setLayout(new GridLayout(2, 2, 5, 5));

        JLabel tcGoruntuleLabel = new JLabel("TC Kimlik No:");
        JTextField tcGoruntuleField = new JTextField();

        JButton goruntuleButton = new JButton("Biletleri Görüntüle");
        goruntuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tcGoruntule = tcGoruntuleField.getText();
                
                goruntuleBiletBilgileri(tcGoruntule);
            }
        });

        biletGoruntulePanel.add(tcGoruntuleLabel);
        biletGoruntulePanel.add(tcGoruntuleField);
        biletGoruntulePanel.add(goruntuleButton);
        biletGoruntuleFrame.getContentPane().add(biletGoruntulePanel);

        JFrame biletIptalFrame = new JFrame("Bilet İptal");
        biletIptalFrame.setSize(400, 200);
        biletIptalFrame.setLocationRelativeTo(null);
        biletIptalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel biletIptalPanel = new JPanel();
        biletIptalPanel.setLayout(new GridLayout(2, 2, 5, 5));

        JLabel tcIptalLabel = new JLabel("TC Kimlik No:");
        JTextField tcIptalField = new JTextField();

        JButton iptalButton = new JButton("Bileti İptal Et");
        iptalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tcIptal = tcIptalField.getText();
                biletIptalEt(tcIptal);
            }
        });

        biletIptalPanel.add(tcIptalLabel);
        biletIptalPanel.add(tcIptalField);
        biletIptalPanel.add(iptalButton);

        biletIptalFrame.getContentPane().add(biletIptalPanel);

        biletAlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                biletAlFrame.setVisible(true);
            }
        });

        biletIptalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Otobüs bileti iptal edildi.");
            }
        });

        biletGoruntuleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tcGoruntule = JOptionPane.showInputDialog(mainFrame, "Bilet görüntülemek için TC Kimlik No girin:");
                if (tcGoruntule != null) {
                    goruntuleBiletBilgileri(tcGoruntule);
                }
            }
        });

        biletIptalButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                String tcIptal = JOptionPane.showInputDialog(mainFrame, "Bilet iptali için TC Kimlik No girin:");
                if (tcIptal != null) {
                    biletIptalEt(tcIptal);
                }
            }
        });
        mainFrame.setVisible(true);
       
        
        
        
    }
}
