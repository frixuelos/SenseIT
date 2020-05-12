package com.android.tfg.viewmodel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.android.tfg.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

public class LoginViewModel extends ViewModel {

    private UserRepository userRepository;

    public LoginViewModel(){
        userRepository= UserRepository.getInstance();
        loginResult=new MutableLiveData<>();
        resetPasswordResult=new MutableLiveData<>();
    }

    /*********
     * LOGIN *
     *********/
    private MutableLiveData<Task<AuthResult>> loginResult;
    private final Observer<Task<AuthResult>> loginResultObserver = new Observer<Task<AuthResult>>() {
        @Override
        public void onChanged(Task<AuthResult> authResultTask) {
            loginResult.setValue(authResultTask); // actualizar resultado
        }
    };

    public boolean isLoggedIn(){
        return userRepository.getCurrentUser()!=null;
    }

    public void login(String email, String passwd){
        userRepository.login(email, passwd);
        userRepository.getLogin().observeForever(loginResultObserver);
    }

    public MutableLiveData<Task<AuthResult>> getLogin(){
        return loginResult;
    }


    /******************
     * RESET PASSWORD *
     ******************/
    private MutableLiveData<Task<Void>> resetPasswordResult;
    private Observer<Task<Void>> resetPasswordResultObserver = new Observer<Task<Void>>() {
        @Override
        public void onChanged(Task<Void> voidTask) {
            resetPasswordResult.setValue(voidTask);
        }
    };

    public void resetPassword(String email){
        userRepository.resetPassword(email);
        userRepository.getResetPassword().observeForever(resetPasswordResultObserver);
    }

    public MutableLiveData<Task<Void>> getResetPassword(){
        return resetPasswordResult;
    }

}
