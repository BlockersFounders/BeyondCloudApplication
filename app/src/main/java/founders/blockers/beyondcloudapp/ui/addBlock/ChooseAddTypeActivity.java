package founders.blockers.beyondcloudapp.ui.addBlock;

import androidx.appcompat.app.AppCompatActivity;
import founders.blockers.beyondcloudapp.MainActivity;
import founders.blockers.beyondcloudapp.R;
import founders.blockers.beyondcloudapp.SendFormActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseAddTypeActivity extends AppCompatActivity {

    Button myLoveBtn;
    Button meBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_add_type);

        myLoveBtn = findViewById(R.id.add_btn_1);
        meBtn = findViewById(R.id.add_btn_2);

        myLoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SendFormActivity.class );
                startActivity(intent);
            }
        });

    }
}
