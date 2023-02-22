package algonquin.cst2335.li000793;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** This is a page that simulates a login page
 * @author Jiaqiao Li
 * @version 1.0
 * @since Version 1.0
 */
public class MainActivity extends AppCompatActivity {


    /** This holds the edit text where the user puts their password **/

    private EditText thePasswordText;
    /** This hold the text at the centre of the screen */
    private TextView tv = null;
    /** This holds edit text under the text of the screen */
    private  Button btn = null;
    /** This holds the button on the bottom of the screen*/
    private EditText et = null;

    @Override  //This starts the application
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*loads buttons / text on screen */
        setContentView(R.layout.activity_main);

        //R means res
        //layout is the folder
        //activity_main is the file

        TextView tv = findViewById(R.id.textView);
        Button btn = findViewById(R.id.button);
        EditText et = findViewById(R.id.editText);

        btn.setOnClickListener( clk -> {
            String password = et.getText().toString();

            //looking for Uppercase, lowercase, numbber, and special character

            checkPasswordComplexity(password);
             if (checkPasswordComplexity(password)) {
                 tv.setText("Your password meets the requirements");
             }else
                    tv.setText("you shall not pass!");


        });
    }


    /**
     * Checks whether a given password meets the complexity requirements.
     * The password must contain at least one uppercase letter, one lowercase letter,
     * one number, and one special character from the set {@code # $ % ^ & * ! @}.
     *
     * @param pw the password to check.
     * @return {@code true} if the password meets the complexity requirements,
     *         {@code false} otherwise.
     */
    public boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {
            Toast.makeText(this, "Your password must contain at least one uppercase letter.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(this, "Your password must contain at least one lowercase letter.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundNumber) {
            Toast.makeText(this, "Your password must contain at least one number.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(this, "Your password must contain at least one special character.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Determines whether a given character is a special character
     * from the set {@code # $ % ^ & * ! @}.
     *
     * @param c the character to check.
     * @return {@code true} if the character is a special character, {@code false} otherwise.
     */
    public static boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
                return true;
            default:
                return false;
        }
    }



}