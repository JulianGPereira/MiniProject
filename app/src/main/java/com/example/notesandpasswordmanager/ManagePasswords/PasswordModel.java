package com.example.notesandpasswordmanager.ManagePasswords;

public class PasswordModel {
    private String title;
    private String email;
    private String password;

    EncryptDecrypt d=new EncryptDecrypt();
    public String getTitle() {
        return d.decrypt(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return d.decrypt(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return d.decrypt(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
