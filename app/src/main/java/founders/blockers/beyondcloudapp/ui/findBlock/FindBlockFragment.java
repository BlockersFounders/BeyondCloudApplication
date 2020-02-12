package founders.blockers.beyondcloudapp.ui.findBlock;

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

public class FindBlockFragment extends Fragment {

    private FindBlockViewModel findBlockViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        findBlockViewModel =
                ViewModelProviders.of(this).get(FindBlockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_findblock, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        findBlockViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}