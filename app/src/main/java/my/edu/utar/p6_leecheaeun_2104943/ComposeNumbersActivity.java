package my.edu.utar.p6_leecheaeun_2104943;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComposeNumbersActivity extends AppCompatActivity {
    private TextView targetNum, cpsShowBar, congratsMessage;
    private HorizontalScrollView scrollView;
    private RelativeLayout congratsBoard, thinkAgainBoard;
    private List<Integer> selectedNumbers = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        targetNum = findViewById(R.id.targetNum);
        cpsShowBar = findViewById(R.id.cpsShowBar);
        scrollView = findViewById(R.id.horizontalScrollView);
        congratsBoard = findViewById(R.id.congratsBoard);
        congratsMessage = findViewById(R.id.congratsMessage);
        thinkAgainBoard = findViewById(R.id.thinkAgainBoard);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnCheck = findViewById(R.id.btnCheck);
        Button btnContinue = findViewById(R.id.btnContinue);
        Button btnQuit = findViewById(R.id.btnQuit);
        Button btnBack = findViewById(R.id.btnBack);

        generateTargetNumber();
        setupNumberButtons();

        // Set button actions
        btnDelete.setOnClickListener(v -> removeLastNumber());
        btnCheck.setOnClickListener(v -> checkAnswer());
        btnBack.setOnClickListener(v -> goToHomePage());
        btnQuit.setOnClickListener(v -> goToHomePage());
        btnContinue.setOnClickListener(v -> continueGame());

        congratsMessage.setMovementMethod(new ScrollingMovementMethod());
    }

    // Generate a random two-digit target number (0-50)
    private void generateTargetNumber() {
        Random random = new Random();
        int number = random.nextInt(51); // 0–50
        targetNum.setText(String.valueOf(number)); // display without leading zero
    }

    // Set up number buttons for clicking
    private void setupNumberButtons() {
        int[] buttonIds = { R.id.cpsNum0, R.id.cpsNum1, R.id.cpsNum2, R.id.cpsNum3, R.id.cpsNum4,
                R.id.cpsNum5, R.id.cpsNum6, R.id.cpsNum7, R.id.cpsNum8, R.id.cpsNum9 };

        for (int id : buttonIds) {
            TextView numButton = findViewById(id);
            numButton.setOnClickListener(v -> addNumber(Integer.parseInt(numButton.getText().toString())));
        }
    }

    // Add a number when clicked
    private void addNumber(int number) {
        selectedNumbers.add(number);
        updateDisplay();
    }

    // Remove the last entered number
    private void removeLastNumber() {
        if (!selectedNumbers.isEmpty()) {
            selectedNumbers.remove(selectedNumbers.size() - 1);
            updateDisplay();
        }
    }

    // Update the display with selected numbers
    private void updateDisplay() {
        StringBuilder displayText = new StringBuilder();
        for (int i = 0; i < selectedNumbers.size(); i++) {
            displayText.append(selectedNumbers.get(i));
            if (i < selectedNumbers.size() - 1) {
                displayText.append(" + ");
            }
        }
        cpsShowBar.setText(displayText.toString());

        // Auto-scroll to the right
        scrollView.post(() -> scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT));

    }

    // Check if user input matches the target number
    @SuppressLint("SetTextI18n")
    private void checkAnswer() {
        if (selectedNumbers.isEmpty()) return;

        int sum = 0;
        StringBuilder equation = new StringBuilder();

        for (int i = 0; i < selectedNumbers.size(); i++) {
            sum += selectedNumbers.get(i);
            equation.append(selectedNumbers.get(i));

            if (i < selectedNumbers.size() - 1) {
                equation.append(" + ");
            }
        }

        if (sum == Integer.parseInt(targetNum.getText().toString())) {
            congratsMessage.setText("Yes!\n" + equation.toString() + " = " + sum);
            congratsBoard.setVisibility(View.VISIBLE);
            thinkAgainBoard.setVisibility(View.GONE);
        }
        else {
            thinkAgainBoard.setVisibility(View.VISIBLE);
        }
    }


    // Continue to a new round
    private void continueGame() {
        congratsBoard.setVisibility(View.GONE);
        thinkAgainBoard.setVisibility(View.GONE);
        selectedNumbers.clear();
        updateDisplay();
        generateTargetNumber();
    }

    // Return to home page
    private void goToHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
