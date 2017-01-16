package ch.dss.gadgeothek;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StepDoneFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_step_done, container, false);
        Button button1 = (Button) root.findViewById(R.id.loginButton);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).switchTo(new StepLoginFragment());
            }
        });
        return root;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }
}

