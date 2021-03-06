package com.example.sondrehj.familymedicinereminderclient.fragments;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sondrehj.familymedicinereminderclient.MainActivity;
import com.example.sondrehj.familymedicinereminderclient.R;
import com.example.sondrehj.familymedicinereminderclient.api.MyCyFAPPServiceAPI;
import com.example.sondrehj.familymedicinereminderclient.api.RestService;
import com.example.sondrehj.familymedicinereminderclient.database.MySQLiteHelper;
import com.example.sondrehj.familymedicinereminderclient.models.User;
import com.example.sondrehj.familymedicinereminderclient.models.User2;
import com.example.sondrehj.familymedicinereminderclient.sync.ServiceManager;
import com.example.sondrehj.familymedicinereminderclient.utility.TitleSupplier;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WelcomeFragment.OnWelcomeListener} interface
 * to handle interaction events.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// TODO: Remove welcomeFragment from the back stack after first-time use to deny the user navigating back to it
public class WelcomeFragment extends android.support.v4.app.Fragment {

    private final String TAG = "WelcomeFragment";
    private OnWelcomeListener mListener;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WelcomeFragment.
     */
    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_welcome, container, false);
        getActivity().setTitle("Welcome");
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.create_patient_button)
    public void onCreatePatientButtonClick() {
        createUserWithRole("patient");
    }

    @OnClick(R.id.create_guardian_button)
    public void onCreateGuardianButtonClick() {
        createUserWithRole("guardian");
    }

    private boolean createUserWithRole(String role) {
        Context context = getActivity();

        final ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Creating user");
        progress.setMessage("Please wait while a user is created...");

        final Toast failureToast = Toast.makeText(context,
                "An internet connection is required to create an account. Please try again once " +
                        "you have connected to the internet.",
                Toast.LENGTH_SHORT);

        if (!ServiceManager.isNetworkAvailable(context)) {
            failureToast.show();
        }

        MyCyFAPPServiceAPI service = RestService.createRestService();
        User user = new User(role);
        Call<User> call = service.createUser("myfirstsecret", user);
        Log.d(TAG, call.request().toString());
        progress.show();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> user) {
                String userId = user.body().getUserID();
                String password = user.body().getPassword();
                String userRole = user.body().getUserRole();
                String jwtToken = user.headers().get("access_token");
                Log.d(TAG, "response: userID: " + userId);
                Log.d(TAG, "response: password: " + password);
                Log.d(TAG, "response: userRole: " + userRole);
                Log.d(TAG, "response: userRole: " + userRole);
                Log.d(TAG, "response: accessToken: " + jwtToken);

                progress.dismiss();
                User2 dbUser = new User2(userId, "Me");
                new MySQLiteHelper(getActivity()).addUser(dbUser);
                ((MainActivity) getActivity()).setCurrentUser(dbUser);
                ((MainActivity) getActivity()).userSpinnerToggle.toggle();
                ((MainActivity) getActivity()).userSpinnerToggle.updateSpinnerContent();
                if(userRole.equals("guardian")){
                    ((MainActivity) getActivity()).userSpinnerToggle.showUserActionBar(true);
                }

                //TODO: Update with password != null as well
                if (mListener != null) {
                    if (userId != null) {
                        mListener.OnNewAccountCreated(userId, password, userRole, jwtToken);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progress.dismiss();
                failureToast.show();
            }
        });
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWelcomeListener) {
            mListener = (OnWelcomeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    //For devices with low API-level
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnWelcomeListener) {
            mListener = (OnWelcomeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ButterKnife.unbind(this);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnWelcomeListener {
        void OnNewAccountCreated(String userId, String password, String userRole, String jwtToken);
    }
}
