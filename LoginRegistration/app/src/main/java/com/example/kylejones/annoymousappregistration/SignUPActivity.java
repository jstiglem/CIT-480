package com.example.kylejones.annoymousappregistration; /**
 * Created by Kyle Jones on 1/21/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.kylejones.annoymousappregistration.R;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SignUPActivity extends Activity
{
    EditText editTextUserName,editTextPassword,editTextConfirmPassword,editTextEMail,editTextConfirmMail;
    Button btnCreateAccount;

    LoginDataBaseAdapter loginDataBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // get Instance  of Database Adapter
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        // Get Refferences of Views
        editTextUserName=(EditText)findViewById(R.id.editTextUserName);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextEMail=(EditText)findViewById(R.id.editTextEMail);
        editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);
        editTextConfirmMail=(EditText)findViewById(R.id.editTextConfirmMail);

        btnCreateAccount=(Button)findViewById(R.id.buttonCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();
                String confirmPassword=editTextConfirmPassword.getText().toString();
                String eMail=editTextEMail.getText().toString();
                String confirmMail=editTextConfirmMail.getText().toString();

                // check if any of the fields are vaccant
                if(userName.equals("")||eMail.equals("")||password.equals("")||confirmPassword.equals("")||confirmMail.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Field Vacant", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if both password matches
                if(!password.equals(confirmPassword))
                {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }
                //check if emails match
                if(!confirmMail.equals(eMail)){
                    Toast.makeText(getApplicationContext(), "E-Mail does not match", Toast.LENGTH_LONG).show();
                    return;
                }
                //create account
                else
                {
                    // Save the Data in Database
                    loginDataBaseAdapter.insertEntry(userName, password, eMail);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created! You will receive an E-Mail shortly.",
                            Toast.LENGTH_LONG).show();
                    //Send EMail
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.socketFactory.port", "465");
                    props.put("mail.smtp.socketFactory.class",
                            "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "465");

                    Session session = Session.getDefaultInstance(props,
                            new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication("anonques480@gmail.com","Anonym0us");
                                }
                            });

                    try {

                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress("from@no-spam.com"));
                        message.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse(confirmMail));
                        message.setSubject("Anonymous App Registration");
                        message.setText("Dear " + userName + "," +
                                "\n\nYou have successfully created an account! " +
                                "\nThank you for using our application!" +
                                "\n\nYour password is: " + confirmPassword +
                                "\n");

                        Transport.send(message);

                        System.out.println("Done");

                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }
}
