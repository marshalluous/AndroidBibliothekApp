package ch.dss.gadgeothek;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ch.dss.gadgeothek.domain.Gadget;
import ch.dss.gadgeothek.domain.Loan;
import ch.dss.gadgeothek.domain.Reservation;
import ch.dss.gadgeothek.service.Callback;
import ch.dss.gadgeothek.service.LibraryService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SingleViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SingleViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleViewFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_ITEM = "note_to_show";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Activity activity;

    public enum Availability {
        LOAN, RESERVED, AVAILABLE
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SingleViewFragment.
     */
    public static SingleViewFragment newInstance(String param1, String param2) {
        SingleViewFragment fragment = new SingleViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_ITEM);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        // Inflate the layout for this fragment
        final View parent = inflater.inflate(R.layout.fragment_single_view, container, false);
        GadgetApplication app = (GadgetApplication) getActivity().getApplication();

        Gadget gadget = app.getAllGadgets().get(getArguments().getInt(ARG_ITEM));

        setFragmentTitle(parent, gadget.getManufacturer() + " " + gadget.getName());
        setGadgetConditionDescription(parent, gadget.getCondition().toString());
        setGadgetPrice(parent, gadget.getPrice());
        setImage(parent, gadget.getName());
        setContextActions(parent, gadget);
        return parent;
    }

    private void setContextActions(final View parent, final Gadget gadget) {

        final List<Reservation> reservations = new ArrayList<>();
        LibraryService.getReservationsForCustomer(new Callback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> input) {
                reservations.clear();
                reservations.addAll(input);
                Reservation myReservation = null;
                for (Reservation reservation : reservations) {
                    if(reservation.getGadget().equals(gadget)){
                        myReservation = reservation;
                        break;
                    }
                }
                if(myReservation != null){
                    // gadget is reserved from current user --> delete action
                    applyContextToView(Availability.RESERVED, parent, gadget, myReservation, null);
                } else {

                    final List<Loan> loans = new ArrayList<>();
                    LibraryService.getLoansForCustomer(new Callback<List<Loan>>() {
                        @Override
                        public void onCompletion(List<Loan> input) {
                            loans.clear();
                            loans.addAll(input);
                            Loan myLoan = null;
                            for (Loan loan : loans) {
                                if(loan.getGadget().equals(gadget)){
                                    myLoan = loan;
                                    break;
                                }
                            }
                            if(myLoan != null){
                                // gadget is loan from current user --> no action
                                applyContextToView(Availability.LOAN, parent, gadget, null, myLoan);
                            } else {
                                // gadget is not reserved nor loan from current user --> reservation action
                                applyContextToView(Availability.AVAILABLE, parent, gadget, null, null);
                            }
                        }
                        @Override
                        public void onError(String message) {
                            Toast toast = Toast.makeText(activity, "Fehler beim Laden der Ausleihen", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                }
            }
            @Override
            public void onError(String message) {
                Toast toast = Toast.makeText(activity, "Fehler beim Laden der Reservationen", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    public void applyContextToView(Availability context, final View parent, final Gadget gadget,
                                   final Reservation myReservation, final Loan myLoan){
        final TextView availability = (TextView) parent.findViewById(R.id.availability);
        final Button actionButton = (Button) parent.findViewById(R.id.buttonAction1);

        switch (context) {
            case RESERVED:
                availability.setText("reserviert");
                actionButton.setText("Reservation löschen");
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        LibraryService.deleteReservation(myReservation, new Callback<Boolean>() {
                            @Override
                            public void onCompletion(Boolean input) {
                                Toast toast = Toast.makeText(activity, "Reservation gelöscht!", Toast.LENGTH_SHORT);
                                toast.show();
                                setContextActions(parent, gadget);
                            }
                            @Override
                            public void onError(String message) {
                                Toast toast = Toast.makeText(activity, "Fehler: Reservation konnte nicht gelöscht werden", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                });
                break;
            case LOAN:
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateToReturn = dateFormat.format(myLoan.overDueDate());
                availability.setText("Ausgeliehen - zurückgeben am: " + dateToReturn);
                actionButton.setVisibility(View.INVISIBLE);
                break;
            case AVAILABLE:
                availability.setText("verfügbar");
                actionButton.setText("Reservieren");
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        LibraryService.reserveGadget(gadget, new Callback<Boolean>() {
                            @Override
                            public void onCompletion(Boolean input) {
                                Toast toast = Toast.makeText(activity, "Reservation bestätigt!", Toast.LENGTH_SHORT);
                                toast.show();
                                setContextActions(parent, gadget);
                            }
                            @Override
                            public void onError(String message) {
                                Toast toast = Toast.makeText(activity, "Reservierung fehlgeschlagen!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                });
                break;
            default:
                availability.setText("unbekannt");
                actionButton.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void setFragmentTitle(View parent, String title) {
        TextView fragmentTitle = (TextView) parent.findViewById(R.id.textGadgetTitle);
        fragmentTitle.setText(title);
        fragmentTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setGadgetConditionDescription(View parent, String state) {
        TextView gadgetState = (TextView) parent.findViewById(R.id.textGadgetCondition);
        gadgetState.setText(state);
    }

    private void setGadgetPrice(View parent, double price) {
        TextView gadgetState = (TextView) parent.findViewById(R.id.textPrice);
        gadgetState.setText(Double.toString(price));
    }

    private void setImage(View parent, String gadgetName) {
        ImageView gadgetImage = (ImageView) parent.findViewById(R.id.gadgetimage);
        switch (gadgetName) {
            case "IPhone":
                gadgetImage.setImageResource(R.drawable.iphone7);
                break;
            case "IPhone1":
                gadgetImage.setImageResource(R.drawable.iphone1);
                break;
            case "IPhone2":
                gadgetImage.setImageResource(R.drawable.iphone2);
                break;
            case "IPhone3":
                gadgetImage.setImageResource(R.drawable.iphone3);
                break;
            case "IPhone4":
                gadgetImage.setImageResource(R.drawable.iphone4);
                break;
            case "Android1":
                gadgetImage.setImageResource(R.drawable.android1);
                break;
            case "Android2":
                gadgetImage.setImageResource(R.drawable.android2);
                break;
            default:
                gadgetImage.setImageResource(R.drawable.placeholderimage);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
