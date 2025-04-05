package my.edu.utar.p6_leecheaeun_2104943;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCompare = findViewById(R.id.compare);
        Button btnOrder = findViewById(R.id.order);
        Button btnCompose = findViewById(R.id.compose);
        Button btnExit = findViewById(R.id.btnExit);

        btnCompare.setOnClickListener(v -> startActivity(new Intent(this, CompareNumbersActivity.class)));
        btnOrder.setOnClickListener(v -> startActivity(new Intent(this, OrderNumbersActivity.class)));
        btnCompose.setOnClickListener(v -> startActivity(new Intent(this, ComposeNumbersActivity.class)));
        btnExit.setOnClickListener(v -> finishAffinity());

    }
}
