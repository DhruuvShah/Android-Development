package com.example.calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etNum1, etNum2;
    private TextView tvResult;
    private AppCompatButton btnAdd, btnSub, btnMul, btnDiv, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
    }

    private void initViews() {
        etNum1 = findViewById(R.id.etNum1);
        etNum2 = findViewById(R.id.etNum2);
        tvResult = findViewById(R.id.tvResult);
        btnAdd = findViewById(R.id.btnAdd);
        btnSub = findViewById(R.id.btnSub);
        btnMul = findViewById(R.id.btnMul);
        btnDiv = findViewById(R.id.btnDiv);
        btnClear = findViewById(R.id.btnClear);
    }

    private void setupListeners() {
        // Tapping any operation button immediately computes and reflects the result
        btnAdd.setOnClickListener(v -> calculate("+"));
        btnSub.setOnClickListener(v -> calculate("-"));
        btnMul.setOnClickListener(v -> calculate("×"));
        btnDiv.setOnClickListener(v -> calculate("÷"));

        // Clear button resets inputs and result
        btnClear.setOnClickListener(v -> {
            etNum1.setText("");
            etNum2.setText("");
            tvResult.setText(R.string.default_zero);
            etNum1.requestFocus();
        });
    }

    private void calculate(String operator) {
        String sNum1 = etNum1.getText() != null ? etNum1.getText().toString().trim() : "";
        String sNum2 = etNum2.getText() != null ? etNum2.getText().toString().trim() : "";

        // Proper validation checks using if/else
        if (TextUtils.isEmpty(sNum1)) {
            etNum1.setError(getString(R.string.error_empty));
            Toast.makeText(this, R.string.error_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sNum2)) {
            etNum2.setError(getString(R.string.error_empty));
            Toast.makeText(this, R.string.error_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        double num1, num2;
        try {
            num1 = Double.parseDouble(sNum1);
            num2 = Double.parseDouble(sNum2);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_invalid_number, Toast.LENGTH_SHORT).show();
            return;
        }

        double result;

        // Proper if/else calculation logic for each operator
        if (operator.equals("+")) {
            result = num1 + num2;
        } else if (operator.equals("-")) {
            result = num1 - num2;
        } else if (operator.equals("*") || operator.equals("×")) {
            result = num1 * num2;
        } else if (operator.equals("/") || operator.equals("÷")) {
            if (num2 == 0.0) {
                Toast.makeText(this, R.string.error_division_by_zero, Toast.LENGTH_SHORT).show();
                tvResult.setText(R.string.error_text);
                return;
            } else {
                result = num1 / num2;
            }
        } else {
            result = num1 + num2;
        }

        // Format result: remove decimal if whole number
        if (result == (long) result) {
            tvResult.setText(String.format(Locale.getDefault(), "%d", (long) result));
        } else {
            tvResult.setText(String.format(Locale.getDefault(), "%.4f", result));
        }
    }
}
