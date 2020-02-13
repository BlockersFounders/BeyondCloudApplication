package founders.blockers.beyondcloudapp.ui.findBlock;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import founders.blockers.beyondcloudapp.R;
import founders.blockers.beyondcloudapp.Record;

public class FindBlockFragment extends Fragment {

    Button findBtn;

    private FindBlockViewModel FindBlockViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FindBlockViewModel =
                ViewModelProviders.of(this).get(FindBlockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_findblock, container, false);

        findBtn= (Button) root.findViewById(R.id.add_btn_1);


        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Record.class);
                startActivity(intent);
            }
        });


        return root;
    }





}