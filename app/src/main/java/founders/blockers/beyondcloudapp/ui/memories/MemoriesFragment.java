package founders.blockers.beyondcloudapp.ui.memories;

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

public class MemoriesFragment extends Fragment {

    private MemoriesViewModel memoriesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        memoriesViewModel =
                ViewModelProviders.of(this).get(MemoriesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_memories, container, false);



        return root;
    }
}