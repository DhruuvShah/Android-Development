package com.northq.learninghub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/** Detail pane: currency picker, shown inside the master Fragment's child container. */
public class SettingsDetailFragment extends Fragment {

    public interface OnCurrencyChosen { void onChosen(String code); }

    private OnCurrencyChosen listener;

    public static SettingsDetailFragment newInstance() {
        return new SettingsDetailFragment();
    }

    public void setOnCurrencyChosen(OnCurrencyChosen listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RadioGroup group = view.findViewById(R.id.currencyRadioGroup);
        group.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            String code = "INR";
            if (checkedId == R.id.radioUsd) code = "USD";
            else if (checkedId == R.id.radioGbp) code = "GBP";
            if (listener != null) listener.onChosen(code);
        });
    }
}
