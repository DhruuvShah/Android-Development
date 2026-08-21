package com.northq.learninghub;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {

    private TextView display, expression;
    private String currentInput = "";
    private double firstOperand = 0;
    private String pendingOperator = null;
    private boolean startFresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        
        display = findViewById(R.id.calcDisplay);
        expression = findViewById(R.id.calcExpression);
        
        display.setText("0");
        expression.setText("");

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        setupDigitButtons();
        setupOperatorButtons();
    }

    private void setupDigitButtons() {
        int[] digitIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        
        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            appendDigit(b.getText().toString());
        };

        for (int id : digitIds) findViewById(id).setOnClickListener(listener);

        findViewById(R.id.btnDot).setOnClickListener(v -> {
            if (!currentInput.contains(".")) appendDigit(".");
        });
    }

    private void setupOperatorButtons() {
        findViewById(R.id.btnPlus).setOnClickListener(v -> setOperator("+"));
        findViewById(R.id.btnMinus).setOnClickListener(v -> setOperator("−"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> setOperator("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> setOperator("÷"));
        
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculate());
        findViewById(R.id.btnClear).setOnClickListener(v -> clearAll());
        findViewById(R.id.btnDel).setOnClickListener(v -> deleteLast());
        
        findViewById(R.id.btnPercent).setOnClickListener(v -> {
            if (!currentInput.isEmpty()) {
                double val = Double.parseDouble(currentInput) / 100.0;
                currentInput = format(val);
                display.setText(currentInput);
            }
        });

        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> {
            if (!currentInput.isEmpty() && !currentInput.equals("0")) {
                if (currentInput.startsWith("-")) currentInput = currentInput.substring(1);
                else currentInput = "-" + currentInput;
                display.setText(currentInput);
            }
        });
    }

    private void appendDigit(String d) {
        if (startFresh) {
            currentInput = "";
            startFresh = false;
        }
        currentInput += d;
        display.setText(currentInput);
    }

    private void setOperator(String op) {
        if (!currentInput.isEmpty()) {
            firstOperand = Double.parseDouble(currentInput);
            pendingOperator = op;
            expression.setText(format(firstOperand) + " " + op);
            startFresh = true;
        }
    }

    private void calculate() {
        if (pendingOperator == null || currentInput.isEmpty()) return;
        
        double second = Double.parseDouble(currentInput);
        double result = 0;
        
        switch (pendingOperator) {
            case "+": result = firstOperand + second; break;
            case "−": result = firstOperand - second; break;
            case "×": result = firstOperand * second; break;
            case "÷": result = second == 0 ? Double.NaN : firstOperand / second; break;
        }

        expression.setText(format(firstOperand) + " " + pendingOperator + " " + format(second) + " =");
        currentInput = format(result);
        display.setText(currentInput);
        
        pendingOperator = null;
        startFresh = true;
    }

    private void clearAll() {
        currentInput = "";
        firstOperand = 0;
        pendingOperator = null;
        startFresh = true;
        display.setText("0");
        expression.setText("");
    }

    private void deleteLast() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            display.setText(currentInput.isEmpty() ? "0" : currentInput);
        }
    }

    private String format(double value) {
        if (Double.isNaN(value)) return "Error";
        if (value == (long) value) return String.format("%d", (long) value);
        return String.format("%s", value);
    }
}
