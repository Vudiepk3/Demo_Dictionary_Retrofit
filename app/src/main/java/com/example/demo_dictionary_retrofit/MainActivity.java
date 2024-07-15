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

    private ActivityMainBinding binding;
    private MeaningAdapter adapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchBtn.setOnClickListener(v -> {
            String word = binding.searchInput.getText().toString();
            getMeaning(word);
        });

        adapter = new MeaningAdapter(Collections.emptyList());
        binding.meaningRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.meaningRecyclerView.setAdapter(adapter);
    }

    private void getMeaning(String word) {
        setInProgress(true);

        executorService.execute(() -> {
            try {
                Response<List<WordResult>> response = RetrofitInstance.dictionaryApi.getMeaning(word).execute();
                if (response.body() == null) {
                    throw new Exception();
                }
                mainHandler.post(() -> {
                    setInProgress(false);
                    WordResult wordResult = response.body().get(0);
                    setUI(wordResult);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    setInProgress(false);
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void setUI(WordResult response) {
        binding.wordTextview.setText(response.getWord());
        binding.phoneticTextview.setText(response.getPhonetic());
        adapter.updateNewData(response.getMeanings());
    }

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
