package com.northq.learninghub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/** Master pane: high-level settings categories. Tapping "currency" swaps in the detail Fragment below it. */
public class SettingsMasterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_master, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences(SettingsActivity.PREFS, android.content.Context.MODE_PRIVATE);

        Switch darkModeSwitch = view.findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setChecked(prefs.getBoolean(SettingsActivity.KEY_DARK_MODE, false));
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(SettingsActivity.KEY_DARK_MODE, isChecked).apply());

        TextView currencyValue = view.findViewById(R.id.currencyValue);
        currencyValue.setText(labelFor(prefs.getString(SettingsActivity.KEY_CURRENCY, "INR")));

        view.findViewById(R.id.rowCurrency).setOnClickListener(v -> {
            SettingsDetailFragment detail = SettingsDetailFragment.newInstance();
            detail.setOnCurrencyChosen(code -> {
                prefs.edit().putString(SettingsActivity.KEY_CURRENCY, code).apply();
                currencyValue.setText(labelFor(code));
            });
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.settingsDetailContainer, detail)
                    .commit();
        });
    }

    private String labelFor(String code) {
        switch (code) {
            case "USD": return "$ USD";
            case "GBP": return "£ GBP";
            default: return "₹ INR";
        }
    }
}
