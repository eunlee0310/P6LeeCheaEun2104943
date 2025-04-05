package my.edu.utar.p6_leecheaeun_2104943;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class CompareNumbersActivity extends AppCompatActivity {
    private TextView cprNum1;
    private TextView cprNum2;
    private TextView congratsMessage;
    private RelativeLayout congratsBoard, thinkAgainBoard;
    private int num1, num2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        cprNum1 = findViewById(R.id.cprNum1);
        cprNum2 = findViewById(R.id.cprNum2);
        congratsBoard = findViewById(R.id.congratsBoard);
        congratsMessage = findViewById(R.id.congratsMessage);
        thinkAgainBoard = findViewById(R.id.thinkAgainBoard);
        TextView dropZone = findViewById(R.id.dropZone);
        Button btnGreater = findViewById(R.id.btnGreater);
        Button btnSmaller = findViewById(R.id.btnSmaller);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);
        Button btnQuit = findViewById(R.id.btnQuit);

        generateNumbers();

        // Set drag listener
        btnGreater.setOnTouchListener(this::startDrag);
        btnSmaller.setOnTouchListener(this::startDrag);
        dropZone.setOnDragListener(this::onDrop);
        btnGreater.setTag("greater");
        btnSmaller.setTag("smaller");

        // Set button actions
        btnBack.setOnClickListener(v -> goToHomePage());
        btnQuit.setOnClickListener(v -> goToHomePage());
        btnContinue.setOnClickListener(v -> continueGame());
    }

    private void generateNumbers() {
        Random random = new Random();
        do {
            num1 = random.nextInt(100);
            num2 = random.nextInt(100);
        } while (num1 == num2);  // Ensure they are different
        cprNum1.setText(String.valueOf(num1));
        cprNum2.setText(String.valueOf(num2));
    }

    private boolean startDrag(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("button_drag", view.getTag().toString());
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(data, shadowBuilder, view, 0);
            return true;
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    private boolean onDrop(View v, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            ClipData data = event.getClipData();
            if (data == null || data.getItemCount() == 0) return false;

            String droppedData = data.getItemAt(0).getText().toString();
            boolean isCorrect = (droppedData.equals("greater") && num1 > num2) ||
                    (droppedData.equals("smaller") && num1 < num2);

            if (isCorrect) {
                congratsMessage.setText("Yes!\n" + num1 + " is " + (num1 > num2 ?
                        "greater than" : "less than") + " " + num2 + "!");
                congratsBoard.setVisibility(View.VISIBLE);
                thinkAgainBoard.setVisibility(View.GONE);
            }
            else {
                thinkAgainBoard.setVisibility(View.VISIBLE);
            }
        }
        return true;
    }

    // Continue to a new round
    private void continueGame() {
        congratsBoard.setVisibility(View.GONE);
        thinkAgainBoard.setVisibility(View.GONE);
        generateNumbers();
    }

    // Return to home page
    private void goToHomePage() {
        Intent intent = new Intent(CompareNumbersActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}
