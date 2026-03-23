/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstore;

/**
 *
 * @author ayarramr
 */
public class SilverStatus extends CustomerState{
    
    @Override
    public void updateState(Customer c) {
        if (c.getPoints() < 1000) {
            c.setStatus(new GoldStatus());
        }
    }
}