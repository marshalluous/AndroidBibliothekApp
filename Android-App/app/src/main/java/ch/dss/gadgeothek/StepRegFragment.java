package ch.dss.gadgeothek;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StepRegFragment extends Fragment {
    Register register;

    public interface Register {
        void register(String mail, String password, String name, String studentenNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_step_reg, container, false);
        Button button1 = (Button) root.findViewById(R.id.loginButton);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                TextInputLayout usernameWrapper = (TextInputLayout) root.findViewById(R.id.nameLayout);
                final String name = usernameWrapper.getEditText().getText().toString();
                TextInputLayout passwordWrapper = (TextInputLayout) root.findViewById(R.id.passwordLayout);
                final String password = passwordWrapper.getEditText().getText().toString();
                TextInputLayout emailWrapper = (TextInputLayout) root.findViewById(R.id.emailLayout);
                final String email = emailWrapper.getEditText().getText().toString();
                TextInputLayout mnumberWrapper = (TextInputLayout) root.findViewById(R.id.mnumberLayout);
                final String studentenNumber = mnumberWrapper.getEditText().getText().toString();
                register.register(email, password, name, studentenNumber);
            }
        });
        return root;
    }

    @Override
      public void onAttach(Context activity) {
        super.onAttach(activity);
           register = (Register) activity;
      }
}