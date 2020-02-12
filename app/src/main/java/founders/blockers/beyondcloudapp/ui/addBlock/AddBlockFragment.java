package founders.blockers.beyondcloudapp.ui.addBlock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import founders.blockers.beyondcloudapp.R;

public class AddBlockFragment extends Fragment {

    private AddBlockViewModel addBlockViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addBlockViewModel =
                ViewModelProviders.of(this).get(AddBlockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addblock, container, false);
        final TextView textView = root.findViewById(R.id.text_addblock);
        addBlockViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


}