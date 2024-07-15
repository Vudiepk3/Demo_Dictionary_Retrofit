package com.example.demo_dictionary_retrofit.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demo_dictionary_retrofit.databinding.MeaningRecyclerRowBinding;
import com.example.demo_dictionary_retrofit.models.Meaning;
import java.util.List;

// Adapter cho RecyclerView để hiển thị danh sách các Meaning
public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.MeaningViewHolder> {

    private List<Meaning> meaningList;

    // Constructor - Khởi tạo adapter với danh sách Meaning
    public MeaningAdapter(List<Meaning> meaningList) {
        this.meaningList = meaningList;
    }

    // ViewHolder cho RecyclerView
    public static class MeaningViewHolder extends RecyclerView.ViewHolder {
        private final MeaningRecyclerRowBinding binding;

        // Constructor của ViewHolder
        public MeaningViewHolder(MeaningRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Phương thức để bind dữ liệu với các view
        public void bind(Meaning meaning) {
            // Đặt phần văn bản cho phần từ loại (part of speech)
            binding.partOfSpeechTextview.setText(meaning.getPartOfSpeech());

            // Đặt phần văn bản cho định nghĩa, với mỗi định nghĩa được đánh số thứ tự
            binding.definitionsTextview.setText(
                    meaning.getDefinitions().stream()
                            .map(definition -> {
                                int currentIndex = meaning.getDefinitions().indexOf(definition);
                                return (currentIndex + 1) + ". " + definition.getDefinition();
                            })
                            .reduce((acc, element) -> acc + "\n\n" + element)
                            .orElse("")
            );

            // Hiển thị hoặc ẩn phần từ đồng nghĩa (synonyms) dựa trên danh sách từ đồng nghĩa
            if (meaning.getSynonyms().isEmpty()) {
                binding.synonymsTitleTextview.setVisibility(View.GONE);
                binding.synonymsTextview.setVisibility(View.GONE);
            } else {
                binding.synonymsTitleTextview.setVisibility(View.VISIBLE);
                binding.synonymsTextview.setVisibility(View.VISIBLE);
                binding.synonymsTextview.setText(String.join(", ", meaning.getSynonyms()));
            }

            // Hiển thị hoặc ẩn phần từ trái nghĩa (antonyms) dựa trên danh sách từ trái nghĩa
            if (meaning.getAntonyms().isEmpty()) {
                binding.antonymsTitleTextview.setVisibility(View.GONE);
                binding.antonymsTextview.setVisibility(View.GONE);
            } else {
                binding.antonymsTitleTextview.setVisibility(View.VISIBLE);
                binding.antonymsTextview.setVisibility(View.VISIBLE);
                binding.antonymsTextview.setText(String.join(", ", meaning.getAntonyms()));
            }
        }
    }

    // Cập nhật dữ liệu mới và thông báo thay đổi
    @SuppressLint("NotifyDataSetChanged")
    public void updateNewData(List<Meaning> newMeaningList) {
        this.meaningList = newMeaningList;
        notifyDataSetChanged();
    }

    // Tạo ViewHolder mới khi RecyclerView cần
    @NonNull
    @Override
    public MeaningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item trong RecyclerView
        MeaningRecyclerRowBinding binding = MeaningRecyclerRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MeaningViewHolder(binding);
    }

    // Gán dữ liệu cho ViewHolder tại vị trí cụ thể
    @Override
    public void onBindViewHolder(MeaningViewHolder holder, int position) {
        holder.bind(meaningList.get(position));
    }

    // Trả về số lượng item trong RecyclerView
    @Override
    public int getItemCount() {
        return meaningList.size();
    }
}
