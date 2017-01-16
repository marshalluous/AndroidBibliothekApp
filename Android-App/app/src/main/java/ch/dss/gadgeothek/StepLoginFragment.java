package ch.dss.gadgeothek;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StepLoginFragment extends Fragment {
    Login login;

    public interface Login {
        void login(String mail, String password);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_step_login, container, false);
        Button loginButton = (Button) root.findViewById(R.id.loginButton);
        Button registrationButton = (Button) root.findViewById(R.id.regButton);
        Button changeServerButton = (Button) root.findViewById(R.id.changeServer);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                TextInputLayout passwordWrapper = (TextInputLayout) root.findViewById(R.id.passwordLayout);
                final String password = passwordWrapper.getEditText().getText().toString();
                TextInputLayout emailWrapper = (TextInputLayout) root.findViewById(R.id.emailLayout);
                final String email = emailWrapper.getEditText().getText().toString();
                login.login(email, password);
            }
        });
        registrationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).switchTo(new StepRegFragment());
            }
        });
        changeServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeServer();
            }
        });
        return root;
    }

     @Override
    public void onAttach(Context activity) {
       super.onAttach(activity);
           login = (Login) activity;
     }
}