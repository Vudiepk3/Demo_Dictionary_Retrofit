package com.example.demo_dictionary_retrofit.adapter;

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

    // Constructor
    public MeaningAdapter(List<Meaning> meaningList) {
        this.meaningList = meaningList;
    }

    // ViewHolder cho RecyclerView
    public static class MeaningViewHolder extends RecyclerView.ViewHolder {
        private final MeaningRecyclerRowBinding binding;

        public MeaningViewHolder(MeaningRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Phương thức để bind dữ liệu với các view
        public void bind(Meaning meaning) {
            binding.partOfSpeechTextview.setText(meaning.getPartOfSpeech());
            binding.definitionsTextview.setText(
                    meaning.getDefinitions().stream()
                            .map(definition -> {
                                int currentIndex = meaning.getDefinitions().indexOf(definition);
                                return (currentIndex + 1) + ". " + definition.getDefinition();
                            })
                            .reduce((acc, element) -> acc + "\n\n" + element)
                            .orElse("")
            );

            if (meaning.getSynonyms().isEmpty()) {
                binding.synonymsTitleTextview.setVisibility(View.GONE);
                binding.synonymsTextview.setVisibility(View.GONE);
            } else {
                binding.synonymsTitleTextview.setVisibility(View.VISIBLE);
                binding.synonymsTextview.setVisibility(View.VISIBLE);
                binding.synonymsTextview.setText(String.join(", ", meaning.getSynonyms()));
            }

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
    public void updateNewData(List<Meaning> newMeaningList) {
        this.meaningList = newMeaningList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MeaningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MeaningRecyclerRowBinding binding = MeaningRecyclerRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MeaningViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MeaningViewHolder holder, int position) {
        holder.bind(meaningList.get(position));
    }

    @Override
    public int getItemCount() {
        return meaningList.size();
    }
}
