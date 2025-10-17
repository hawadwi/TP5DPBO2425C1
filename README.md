# TP5DPBO2425C1
Saya Hawa Dwiafina Azahra dengan NIM 2400336 mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

### DESAIN PROGRAM
Product berperan sebagai model data murni: satu objek merepresentasikan satu baris produk, berisi id (String), nama (String), harga (double), kategori (String), dan ratingProduk (int). Kelas ini hanya menyediakan konstruktor serta getter/setter untuk semua atribut.

1. Database adalah data access layer yang menangani koneksi ke MySQL dan eksekusi query. Kelas ini memiliki 3 metode utama:
  - selectQuery(String sql) - Menjalankan SELECT query dan mengembalikan ResultSet
  - insertUpdateDeleteQuery(String sql) - Menjalankan INSERT/UPDATE/DELETE query
  - isIdExists(String id) - Mengecek apakah ID sudah ada di database

  a. Konfigurasi database:
    - Host: localhost:3306
    - Database: db_product
    - User: root
    - Password: (kosong)

2. ProductMenu adalah jendela utama (JFrame) yang menggabungkan tampilan dan logika kontrol. Di dalamnya terdapat: s
   a. selectedIndex yang menyimpan indeks item yang sedang dipilih di tabel. Nilai -1 berarti tidak ada baris yang aktif, nilai >= 0 berarti ada baris yang dipilih dan form berada pada mode "Update/Delete".
   b. oldId yang menyimpan ID lama saat melakukan update, untuk membedakan apakah ID berubah atau tidak.
   c. Komponen input:
     - idField, namaField, hargaField untuk input teks
     - sliderRating untuk nilai 1–5 sehingga pengguna tidak salah memasukkan skala
     - cbKategori untuk memilih kategori valid, dengan placeholder "????" yang menandakan belum memilih
   d. Komponen aksi:
     - addUpdateButton yang labelnya berubah otomatis sesuai konteks (Add saat idle, Update saat baris dipilih)
     - cancelButton untuk mereset form
     - deleteButton yang hanya ditampilkan saat ada baris terpilih

3. Tabel (JTable) dengan DefaultTableModel sebagai jembatan tampilan. Kolom disusun sebagai ID Produk, Nama, Harga, Kategori, dan Rating. Data tabel tidak diedit langsung; pengeditan selalu lewat form agar validasi dapat dijalankan konsisten.

### ALUR PROGRAM
1. Inisialisasi:

   - Pas ProductMenu dibuat, buat object database untuk koneksi MySQL.
   - Atur ukuran window 700x600 px, letakkan di tengah layar, set background putih.
   - Atur slider rating 1–5, isi combobox kategori (plus "????"), set jumlah kolom tabel (ID, Nama, Harga, Kategori, Rating).
   - Panggil setTable() untuk isi tabel dari database.
   - Sembunyiin tombol Delete, pasang semua listener (tombol + klik tabel), lalu tampilkan window.

2. Read (Baca Data):
   - Saat aplikasi startup atau setelah INSERT/UPDATE/DELETE, panggil setTable().
   - Method ini jalankan query "SELECT * FROM product", loop hasil query, dan isi tabel baris per baris. Setiap baris berisi ID, Nama, Harga, Kategori, dan Rating dari database.

3. Add (Tambah Data):
   - Jalan kalau nggak ada baris dipilih (selectedIndex == -1) dan tombol "Add" ditekan.
   - Ambil value dari form (ID, Nama, Harga, Kategori, Rating).
   - Cek ID & Nama nggak kosong, Kategori bukan "????", Harga bisa diparse ke angka.
   - Cek ID tidak duplikat dengan isIdExists().
   - Kalau lolos jalankan INSERT query ke database.
   - Panggil setTable() untuk refresh tabel, clearForm() untuk reset form, terus kasih notifikasi sukses.

4. Pilih Baris:
   - Pas klik baris di tabel, simpan index ke selectedIndex lalu isi form dengan data baris itu (ID, Nama, Harga, Rating, Kategori).
   - Tombol utama berubah jadi "Update" dan tombol Delete ditampilin.
   - Simpan ID lama ke oldId untuk keperluan update nanti.

5. Update (Ubah Data):
   - Kalau selectedIndex valid dan tombol "Update" ditekan.
   - Ambil value dari form.
   - Validasi lagi: ID & Nama nggak kosong, Kategori bukan "????", Harga bisa diparse.
   - Kalau ID berubah, cek ID baru tidak duplikat.
   - Kalau lolos jalankan UPDATE query ke database dengan WHERE id = oldId.
   - Panggil setTable() untuk refresh tabel, clearForm() untuk balik ke mode tambah.

6. Delete (Hapus Data):
   - Selalu pakai konfirmasi. Pastikan ada baris kepilih (selectedIndex >= 0).
   - Kalau nggak ada, stop dan kasih peringatan.
   - Kalau ada, munculin dialog konfirmasi yang nunjukin info penting (ID, Nama, Harga, Kategori, Rating).
   - Kalau YES, jalankan DELETE query ke database dengan WHERE id = idField.getText().
   - Panggil setTable() untuk refresh tabel, clearForm() untuk reset form. Kalau NO, ya nggak ngapa-ngapain.

7. Cancel (Batal/Reset):
   - Kosongin semua field (idField, namaField, hargaField), balikin slider ke default (3), combobox ke "????", sembunyiin Delete, set tombol utama ke "Add", dan selectedIndex = -1.
   - Jalan cepat buat balik ke mode tambah tanpa ngubah data di database.

8. Validasi & Error Handling:
   - Konsisten di semua aksi yang pakai form.
   - Field wajib (ID, Nama, Harga, Kategori) dicek dulu.
   - Kalau Harga nggak bisa diparse (mis. ketik huruf), tangkap NumberFormatException dan tampilin pesan error yang jelas.
   - Kalau ID duplikat, tampilkan warning. Kalau database error, tangkap SQLException dan throw RuntimeException.

### DOKUMENTASI
#### INSERT DATA
![insert](Dokumentasi/INSERT_data.gif)

#### INSERT PROMPT ERROR & ID ERROR
![insert](Dokumentasi/INSERT_prompt_error.gif)
![insert](Dokumentasi/INSERT_Id_error.gif)

#### UPDATE DATA
![update](Dokumentasi/UPDATE_data.gif)

#### UPDATE PROMPT ERROR
![update](Dokumentasi/UPDATE_prompt_error.gif)

#### DELETE DATA
![delete](Dokumentasi/DELETE_data.gif)
