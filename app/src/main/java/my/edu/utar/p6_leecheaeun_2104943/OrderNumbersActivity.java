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

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class OrderNumbersActivity extends AppCompatActivity {
    private TextView[] dropZone = new TextView[5];
    private TextView[] odrNum = new TextView[5];
    private int[] numbers = new int[5];
    private boolean ascendingOrder = true;
    private TextView congratsMessage, orderType;
    private RelativeLayout congratsBoard, thinkAgainBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        dropZone[0] = findViewById(R.id.dropZone1);
        dropZone[1] = findViewById(R.id.dropZone2);
        dropZone[2] = findViewById(R.id.dropZone3);
        dropZone[3] = findViewById(R.id.dropZone4);
        dropZone[4] = findViewById(R.id.dropZone5);

        odrNum[0] = findViewById(R.id.odrNum1);
        odrNum[1] = findViewById(R.id.odrNum2);
        odrNum[2] = findViewById(R.id.odrNum3);
        odrNum[3] = findViewById(R.id.odrNum4);
        odrNum[4] = findViewById(R.id.odrNum5);

        orderType = findViewById(R.id.orderType);
        congratsBoard = findViewById(R.id.congratsBoard);
        congratsMessage = findViewById(R.id.congratsMessage);
        thinkAgainBoard = findViewById(R.id.thinkAgainBoard);
        Button btnContinue = findViewById(R.id.btnContinue);
        Button btnQuit = findViewById(R.id.btnQuit);
        Button btnBack = findViewById(R.id.btnBack);

        generateNumbers();
        setupDragAndDrop();

        btnBack.setOnClickListener(v -> goToHomePage());
        btnQuit.setOnClickListener(v -> goToHomePage());
        btnContinue.setOnClickListener(v -> continueGame());
    }

    private void generateNumbers() {
        Random random = new Random();

        // generate unique random numbers
        for (int i = 0; i < 5; i++) {
            numbers[i] = random.nextInt(100);
            // ensure uniqueness
            for (int j = 0; j < i; j++) {
                if (numbers[i] == numbers[j]) {
                    i--;
                    break;
                }
            }
        }

        // Create a shuffled copy of the numbers
        Integer[] shuffledNumbers = new Integer[5];
        for (int i = 0; i < 5; i++) {
            shuffledNumbers[i] = numbers[i];
        }
        Collections.shuffle(Arrays.asList(shuffledNumbers));

        // Set the text for each number view
        for (int i = 0; i < 5; i++) {
            String numStr = String.valueOf(shuffledNumbers[i]);
            odrNum[i].setText(numStr);
            odrNum[i].setVisibility(View.VISIBLE);
            odrNum[i].setTag(numStr);
        }

        ascendingOrder = random.nextBoolean();
        orderType.setText(ascendingOrder ? "Ascending" : "Descending");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDragAndDrop() {
        // Set up drag for original numbers
        for (TextView number : odrNum) {
            number.setOnTouchListener((view, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", ((TextView) view).getText());
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                    return true;
                }
                return false;
            });
        }

        // Set up drag for drop zones to allow movement between zones
        for (TextView zone : dropZone) {
            zone.setOnTouchListener((view, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN && !((TextView) view).getText().toString().isEmpty()) {
                    ClipData data = ClipData.newPlainText("", ((TextView) view).getText());
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                    return true;
                }
                return false;
            });

            zone.setOnDragListener((v, event) -> {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        String draggedText = event.getClipData().getItemAt(0).getText().toString();
                        View sourceView = (View) event.getLocalState();
                        TextView targetZone = (TextView) v;
                        String targetText = targetZone.getText().toString(); // Save the existing number in the target zone

                        if (sourceView instanceof TextView) {
                            TextView sourceTextView = (TextView) sourceView;
                            boolean isSourceDropZone = Arrays.asList(dropZone).contains(sourceTextView);
                            if (isSourceDropZone) {
                                // Swap values if dragging from one drop zone to another
                                sourceTextView.setText(targetText);
                            }
                            else {
                                // If dragging from the original number list
                                if (!targetText.isEmpty()) {
                                    // Return the existing number to its original position
                                    makeNumberVisible(targetText);
                                }
                                sourceTextView.setVisibility(View.INVISIBLE);
                            }
                            // Place the dragged number into the target drop zone
                            targetZone.setText(draggedText);
                        }

                        checkCompletion();
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        if (!event.getResult()) {
                            View draggedView = (View) event.getLocalState();
                            if (draggedView instanceof TextView) {
                                draggedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                }
                return true;
            });
        }
    }

    // Helper method to make a number visible based on its value
    private void makeNumberVisible(String numberText) {
        for (TextView numView : odrNum) {
            if (numView.getTag() != null && numView.getTag().toString().equals(numberText)) {
                numView.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void checkCompletion() {
        // First check if all drop zones have been filled
        for (TextView zone : dropZone) {
            if (zone.getText().toString().isEmpty()) {
                // If any zone is empty, don't show any messages yet
                thinkAgainBoard.setVisibility(View.GONE);
                return;
            }
        }

        // Only proceed with checking the order if all zones are filled
        String[] placedNumbers = new String[5];
        for (int i = 0; i < 5; i++) placedNumbers[i] = dropZone[i].getText().toString();

        Integer[] sortedNumbers = Arrays.stream(numbers).boxed().toArray(Integer[]::new);
        Arrays.sort(sortedNumbers);
        if (!ascendingOrder) Collections.reverse(Arrays.asList(sortedNumbers));

        if (Arrays.equals(placedNumbers, Arrays.stream(sortedNumbers).map(String::valueOf).toArray(String[]::new))) {
            String orderType = ascendingOrder ? "ascending" : "descending";
            congratsMessage.setText("Yes!\n The " + orderType + " order is " + Arrays.toString(sortedNumbers));
            congratsBoard.setVisibility(View.VISIBLE);
            thinkAgainBoard.setVisibility(View.GONE);
        }
        else {
            thinkAgainBoard.setVisibility(View.VISIBLE);
        }
    }

    private void continueGame() {
        congratsBoard.setVisibility(View.GONE);
        thinkAgainBoard.setVisibility(View.GONE);
        generateNumbers();
        for (TextView zone : dropZone) zone.setText("");
    }

    private void goToHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}