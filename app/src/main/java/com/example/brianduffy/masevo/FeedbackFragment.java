package com.example.brianduffy.masevo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static com.example.brianduffy.masevo.LoginActivity.emailAddress;

/**
 * Created by vpasuma on 11/20/17.
 */

public class FeedbackFragment extends android.support.v4.app.Fragment implements  View.OnClickListener{
    Button sendEmail;
    EditText emailField;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Define Your Functionality Here
           Find any view  => v.findViewById()
          Specifying Application Context in Fragment => getActivity() */
        //returns the view

        return inflater.inflate(R.layout.feedback_layout,container,false); // TODO maybe true
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Initilize variables
        sendEmail = getView().findViewById(R.id.sendEmail);
        sendEmail.setOnClickListener(this);
        emailField = getView().findViewById(R.id.event_desc);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendEmail:
                String editEmailField = emailField.getText().toString();
                Intent feedbackEmail = new Intent(Intent.ACTION_SEND);
                feedbackEmail.setType("text/email");
                feedbackEmail.putExtra(Intent.EXTRA_EMAIL,  new String[] {"feedback.masevo@gmail.com"});
                feedbackEmail.putExtra(Intent.EXTRA_SUBJECT, "Masevo User Feedback");
                feedbackEmail.putExtra(Intent.EXTRA_TEXT, editEmailField);
                startActivity(Intent.createChooser(feedbackEmail, "Send Feedback:"));
                break;

        }

    }
}
