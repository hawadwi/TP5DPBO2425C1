import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductMenu extends JFrame {
    public static void main(String[] args) {
        // buat object window
        ProductMenu menu = new ProductMenu();

        // atur ukuran window
        menu.setSize(700, 600);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // index baris yang diklik
    private int selectedIndex = -1;
    private String oldId = "";

    private Database database;

    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField hargaField;
    private JTable productTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> kategoriComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel hargaLabel;
    private JLabel kategoriLabel;
    private JSlider ratingSlider;
    private JLabel ratingLabel;

    // constructor
    public ProductMenu() {


        // buat object database
        database = new Database();



        // isi tabel produk
        productTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] kategoriData = { "????", "BodyCare", "Haircare", "Skincare", "Lipcare", "Oralcare", "Handcare", "Footcare", "Eyecare" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // atur slider rating (1-5)
        ratingSlider.setMinimum(1);
        ratingSlider.setMaximum(5);
        ratingSlider.setValue(3);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setSnapToTicks(true);

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex < 0 ) {
                    JOptionPane.showMessageDialog(ProductMenu.this,
                            "Pilih data yang ingin dihapus terlebih dahulu.",
                            "Tidak ada data dipilih",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Ambil data langsung dari tabel yang sedang ditampilkan
                String curId = productTable.getModel().getValueAt(selectedIndex, 0).toString();
                String curNama = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curHarga = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curKategori = productTable.getModel().getValueAt(selectedIndex, 3).toString();
                int curRating = Integer.parseInt(productTable.getModel().getValueAt(selectedIndex, 4).toString());

                double harga = Double.parseDouble(curHarga);

                String message = String.format(
                        "Hapus produk berikut?\nID: %s\nNama: %s\nHarga: Rp%,.2f\nKategori: %s\nRating: %d",
                        curId, curNama, harga, curKategori, curRating
                );

                int confirm = JOptionPane.showConfirmDialog(
                        ProductMenu.this,
                        message,
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = productTable.getSelectedRow();

                // simpan value textfield dan combo box
                String curId = productTable.getModel().getValueAt(selectedIndex, 0).toString();
                String curNama = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curHarga = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curKategori = productTable.getModel().getValueAt(selectedIndex, 3).toString();
                int curRating = Integer.parseInt(productTable.getModel().getValueAt(selectedIndex, 4).toString());

                // simpan id lama
                oldId = curId;

                // ubah isi textfield, combo box, dan slider
                idField.setText(curId);
                namaField.setText(curNama);
                hargaField.setText(curHarga);
                kategoriComboBox.setSelectedItem(curKategori);
                ratingSlider.setValue(curRating);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);

            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = {"ID Produk", "Nama", "Harga", "Kategori", "Rating" };

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        // isi tabel dengan listProduct
        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM product");

            //isi tabel dengan hasil query
            int i = 0;
            while (resultSet.next()) {
                Object[] row = new Object[5];
                row[0] = resultSet.getString("id");
                row[1] = resultSet.getString("nama");
                row[2] = resultSet.getString("harga");
                row[3] = resultSet.getString("kategori");
                row[4] = resultSet.getInt("ratingProduk");
                tmp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tmp;
    }

    public void insertData() {
        try {
            // ambil value dari textfield, combobox, dan slider
            String id = idField.getText();
            String nama = namaField.getText();
            String hargaStr = hargaField.getText();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int rating = ratingSlider.getValue();

            // validasi input kosong
            if (id.isEmpty() || nama.isEmpty() || hargaStr.isEmpty() || kategori.equals("????")) {
                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // validasi input kosong
            if (id.isEmpty() || nama.isEmpty() || hargaStr.isEmpty() || kategori.equals("????")) {
                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // validasi ID sudah ada atau belum
            if (database.isIdExists(id)) {
                JOptionPane.showMessageDialog(null,
                        "ID Produk '" + id + "' sudah digunakan! Silakan gunakan ID lain.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double harga = Double.parseDouble(hargaStr);

            // tambahkan data ke dalam database
            String sqlQuery = "INSERT INTO product VALUES ('" + id + "', '" + nama + "', " + harga + ", '" + kategori
                    + "', " + rating + ")";
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            productTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Harga harus berupa angka!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        try {
            // ambil data dari form
            String newId = idField.getText();
            String nama = namaField.getText();
            String hargaStr = hargaField.getText();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int rating = ratingSlider.getValue();

            // validasi input kosong
            if (newId.isEmpty() || nama.isEmpty() || hargaStr.isEmpty() || kategori.equals("????")) {
                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double harga = Double.parseDouble(hargaStr);

            if (!newId.equals(oldId)) {
                // Cek apakah ID baru sudah ada di database
                if (database.isIdExists(newId)) {
                    JOptionPane.showMessageDialog(null,
                            "ID Produk '" + newId + "' sudah digunakan!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // ubah data produk di list
           String sqlQuery = " UPDATE product SET id = '" + newId + "', nama = '" + nama + "', harga = " + harga + ", kategori = '" +
                   kategori + "', ratingProduk = " + rating + " WHERE id = '" + oldId + "'";
            database.insertUpdateDeleteQuery(sqlQuery);

            if (newId.isEmpty() || nama.isEmpty() || hargaStr.isEmpty() || kategori.equals("????")) {
                JOptionPane.showMessageDialog(null,
                        "Data dengan ID tersebut tidak ditemukan!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // update tabel
            productTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diupdate");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Harga harus berupa angka!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        // hapus data dari list
        String id = idField.getText();
        String sqlQuery = "DELETE FROM product WHERE id = '" + id + "'";
        database.insertUpdateDeleteQuery(sqlQuery);

        // update tabel
        productTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        // kosongkan semua texfield, combo box, dan slider
        idField.setText("");
        namaField.setText("");
        hargaField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        ratingSlider.setValue(3);

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;

        //reset oldId
        oldId = "";
    }

    // panggil prosedur ini untuk mengisi list produk
//    private void populateList() {
//        listProduct.add(new Product("PC001", "Brightening Serum", 120000.0, "Skincare", 5));
//        listProduct.add(new Product("PC002", "Moisture Lotion", 112000.0, "Bodycare", 4));
//        listProduct.add(new Product("PC003", "Herbal Shampoo", 187000.0, "Haircare", 5));
//        listProduct.add(new Product("PC004", "Whitening Toothpaste", 15000.0, "Oralcare", 3));
//        listProduct.add(new Product("PC005", "Body Mist Vanilla", 85000.0, "Bodycare", 4));
//        listProduct.add(new Product("PC006", "Natur Hair Tonic Ginseng", 125000.0, "Haircare", 4));
//        listProduct.add(new Product("PC007", "Scarlett Jolly Body Lotion", 75000.0, "Bodycare", 5));
//        listProduct.add(new Product("PC008", "The Body Shop White Musk Mist", 180000.0, "Bodycare", 3));
//        listProduct.add(new Product("PC009", "Sunscreen", 41000.0, "Skincare", 4));
//        listProduct.add(new Product("PC010", "Sensodyne Gentle Whitening", 5000.0, "Oralcare", 4));
//        listProduct.add(new Product("PC011", "Lip Therapy Advanced Healing", 50000.0, "Lipcare", 5));
//        listProduct.add(new Product("PC012", "Face Wash Mugwort", 44000.0, "Skincare", 5));
//        listProduct.add(new Product("PC013", "White Secret Eye Cream", 93000.0, "Eyecare", 4));
//        listProduct.add(new Product("PC014", "Dove Deep Moisture Hand Cream", 170000.0, "Handcare", 3));
//        listProduct.add(new Product("PC015", "Oriflame Feet Up Cream", 57000.0, "Footcare", 4));
//    }
}
