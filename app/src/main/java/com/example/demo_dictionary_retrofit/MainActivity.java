package com.example.demo_dictionary_retrofit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.demo_dictionary_retrofit.adapter.MeaningAdapter;
import com.example.demo_dictionary_retrofit.databinding.ActivityMainBinding;
import com.example.demo_dictionary_retrofit.models.WordResult;
import com.example.demo_dictionary_retrofit.retrofit.RetrofitInstance;

import retrofit2.Response;

// Lớp MainActivity kế thừa AppCompatActivity
public class MainActivity extends AppCompatActivity {

    // Biến cho việc binding giao diện người dùng
    private ActivityMainBinding binding;

    // Biến cho adapter để hiển thị danh sách các Meaning
    private MeaningAdapter adapter;

    // ExecutorService để thực hiện các tác vụ không đồng bộ
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Handler để đăng các tác vụ trở lại luồng chính
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo binding cho Activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thiết lập sự kiện click cho nút tìm kiếm
        binding.searchBtn.setOnClickListener(v -> {
            String word = binding.searchInput.getText().toString();
            getMeaning(word);
        });

        // Khởi tạo adapter với danh sách rỗng và thiết lập cho RecyclerView
        adapter = new MeaningAdapter(Collections.emptyList());
        binding.meaningRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.meaningRecyclerView.setAdapter(adapter);
    }

    // Phương thức để lấy nghĩa của từ
    private void getMeaning(String word) {
        setInProgress(true); // Hiển thị tiến trình

        // Thực hiện cuộc gọi API trên luồng nền
        executorService.execute(() -> {
            try {
                // Gọi API để lấy nghĩa của từ
                Response<List<WordResult>> response = RetrofitInstance.dictionaryApi.getMeaning(word).execute();
                if (response.body() == null) {
                    throw new Exception(); // Ném ngoại lệ nếu không có kết quả
                }
                // Chuyển kết quả trở lại luồng chính để cập nhật giao diện người dùng
                mainHandler.post(() -> {
                    setInProgress(false); // Ẩn tiến trình
                    WordResult wordResult = response.body().get(0);
                    setUI(wordResult); // Cập nhật giao diện với kết quả
                });
            } catch (Exception e) {
                // Xử lý lỗi và thông báo cho người dùng
                mainHandler.post(() -> {
                    setInProgress(false); // Ẩn tiến trình
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // Phương thức để cập nhật giao diện người dùng với kết quả tìm kiếm
    private void setUI(WordResult response) {
        binding.wordTextview.setText(response.getWord());
        binding.phoneticTextview.setText(response.getPhonetic());
        adapter.updateNewData(response.getMeanings());
    }

    // Phương thức để hiển thị hoặc ẩn tiến trình
    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            binding.searchBtn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.searchBtn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
