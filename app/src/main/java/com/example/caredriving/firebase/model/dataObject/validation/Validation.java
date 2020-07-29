package com.example.caredriving.firebase.model.dataObject.validation;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.caredriving.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Validation {

    private ArrayList<String> errors;
    private HashSet<Integer> infoErrors;

    public Validation() {
        errors = new ArrayList<>();
        infoErrors = new HashSet<>();
    }

    public HashSet<Integer> getRegistrationErrors() {
        return infoErrors;
    }

    public void checkFirstName(String firstName) {
        if (firstName.length() == 0) {
            errors.add("Please enter your name");
            infoErrors.add(R.id.tvInformationFirstNameError);
        }

        for (int i = 0; i < firstName.length(); i++) {
            if (firstName.charAt(i) >= '0' && firstName.charAt(i) <= '9') {
                errors.add("Name is incorrect");
                infoErrors.add(R.id.tvInformationFirstNameError);
                break;
            }
        }
    }

    public void checkLastName(String firstName) {
        if (firstName.length() == 0) {
            errors.add("Please enter your last name");
            infoErrors.add(R.id.tvInformationLastNameError);
        }

        for (int i = 0; i < firstName.length(); i++) {
            if (firstName.charAt(i) >= '0' && firstName.charAt(i) <= '9') {
                errors.add("Last name is incorrect.");
                infoErrors.add(R.id.tvInformationLastNameError);
                break;
            }
        }
    }

    public void checkAge(String sAge) {

        if (sAge.isEmpty()) {
            errors.add("Please enter your age");
            infoErrors.add(R.id.tvInformationAgeError);
        } else {
            try {
                int age = Integer.parseInt(sAge);
                if (age <= 1 || age >= 100) {
                    errors.add("Age is incorrect.");
                    infoErrors.add(R.id.tvInformationAgeError);
                }
            } catch (Exception e) {
                errors.add("Age is incorrect.");
                infoErrors.add(R.id.tvInformationAgeError);
            }
        }
    }

    public void checkPrice(String sPrice) {
        try {
            int price = Integer.parseInt(sPrice);
            if (price <= 0) {
                errors.add("Price is incorrect.");
//                infoErrors.add(R.id.tvError5);
            }
        } catch (Exception e) {
            errors.add("Price is incorrect.");
//            infoErrors.add(R.id.tvError5);
        }
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return errors.size() != 0;
    }

    public void checkNumber(String number) {
        if (number.length() != 7) {
            errors.add("Number is incorrect.");
            infoErrors.add(R.id.tvInformationNumberError);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkLessonDate(String date) {
        String[] currentDate = java.time.LocalDate.now().toString().split("-");
        String[] checkDate = date.split("/");

        if (Integer.parseInt(currentDate[0]) <= Integer.parseInt(checkDate[2])) {
            if (Integer.parseInt(checkDate[1]) == Integer.parseInt(currentDate[1])) {
                if (Integer.parseInt(currentDate[2]) > Integer.parseInt(checkDate[0])) {
                    errors.add("Date is incorrect.\nUnable to create lesson in the past.");
                }
            } else if (Integer.parseInt(checkDate[1]) < Integer.parseInt(currentDate[1])) {
                errors.add("Date is incorrect.\nUnable to create lesson in the past.");
            }
        } else {
            errors.add("Date is incorrect.\nUnable to create lesson in the past.");
        }
    }

    public void checkTime(String date, String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date curDate = new Date();
        String[] fullCurrentTime = formatter.format(curDate).toString().split(" ");
        String[] currentTime = fullCurrentTime[1].split(":");
        String[] checkedTime = time.split(":");

        date = convertToSameFormat(date);

        if (Integer.parseInt(checkedTime[1]) != 0) {
            errors.add("Time is incorrect\nLesson should start at beginning of hour.\nFor example 9:00 or 13:00.");
        } else {
            if (date.equals(fullCurrentTime[0])) {
                if (Integer.parseInt(currentTime[0]) >= Integer.parseInt(checkedTime[0])) {
                    errors.add("Time is incorrect.\nUnable to create lesson in the past.");
                }
            }
        }
    }

    //convert my lesson date to dd/MM/yyyy format
    private String convertToSameFormat(String date1) {
        String[] firstDateArr = date1.split("/");
        String res = "";
        for (int i = 0; i < firstDateArr.length - 1; i++) {
            if (firstDateArr[i].length() == 1) {
                firstDateArr[i] = "0" + firstDateArr[i];
            }
            res = res + firstDateArr[i] + "/";
        }
        res = res + firstDateArr[2];
        return res;
    }

    public void clearErrors() {
        errors.clear();
        infoErrors.clear();
    }
}
